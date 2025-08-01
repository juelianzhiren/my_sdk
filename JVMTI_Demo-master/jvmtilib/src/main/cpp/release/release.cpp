//
// Created by Administrator on 2021/7/8.
//

#include <elf.h>
#include <string.h>
#include <jni.h>
#include <android/log.h>
#include <dlfcn.h>
#include <fcntl.h>
#include <unistd.h>
#include <linux/mman.h>
#include <sys/mman.h>
#include <malloc.h>
#include <cstdlib>
#include <string>

// https://developer.android.google.cn/ndk/guides/cpu-features
#ifdef __arm__
#define Elf_Ehdr Elf32_Ehdr
#define Elf_Shdr Elf32_Shdr
#define Elf_Sym  Elf32_Sym
#elif defined(__aarch64__)
#define Elf_Ehdr Elf64_Ehdr
#define Elf_Shdr Elf64_Shdr
#define Elf_Sym  Elf64_Sym
#elif defined(__i386__)
#define Elf_Ehdr Elf32_Ehdr
#define Elf_Shdr Elf32_Shdr
#define Elf_Sym  Elf32_Sym
#elif defined(__x86_64__)
#define Elf_Ehdr Elf64_Ehdr
#define Elf_Shdr Elf64_Shdr
#define Elf_Sym  Elf64_Sym
#else
#error "Arch unknown, please port me"
#endif

#define LOG_TAG "Monitor"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

struct ctx {
    //通过/proc/self/proc 找到的libart.so的起始地址
    void *load_addr;
    // so中的动态符号
    void *dynsym;
    //动态符号表的个数
    int nsyms;
    //动态库中所有字符串
    void *dynstr;
    //程序定义区偏移地址
    off_t bias;
};


int fake_dlclose(void *handle) {
    if (handle) {
        struct ctx *ctx = (struct ctx *) handle;
        if (ctx->dynsym) free(ctx->dynsym);    /* we're saving dynsym and dynstr */
        if (ctx->dynstr) free(ctx->dynstr);    /* from library file just in case */
        free(ctx);
    }
    return 0;
}


void *fake_dlopen(const char *libpath) {
    // so的mmap映射内存
    Elf_Ehdr *elf = 0;
    //so的文件句柄
    int fd = -1;
    // so的加载地址
    off_t load_addr;
    // so的大小
    off_t size;
    struct ctx *ctx = 0;
    uint64_t shoff;

#define fatal(fmt, args...) do { LOGE(fmt,##args); goto err_exit; } while(0)
    /**
     * 1、从 /proc/self/maps 读取so的映射地址 ：为了定位要执行的函数地址执行函数
     * cat /proc/1665/maps | grep libart.so
     *  e2d50000-e3473000 r-xp 00000000 08:06 1087     /system/lib/libart.so
     *  e3474000-e347c000 r--p 00723000 08:06 1087     /system/lib/libart.so
     *  e347c000-e347e000 rw-p 0072b000 08:06 1087     /system/lib/libart.so
     */
    FILE *maps = fopen("/proc/self/maps", "r");
    char buff[256];
    int found = 0;
    while (!found && fgets(buff, sizeof(buff), maps)) {
        //读取到映射地址
        if (strstr(buff, "r-xp") && strstr(buff, libpath)) {
            found = 1;
        }
    }
    fclose(maps);
    if (!found) fatal("%s not found in /proc/self/maps", libpath);
    //把首地址保存在load_addr
    if (sscanf(buff, "%lx", &load_addr) != 1) {
        fatal("failed to read load address for %s", libpath);
    }
    LOGI("%s loaded in Android at 0x%08lx", libpath, load_addr);

    /**
     * 2、对so进行mmap：为了解析so，后续可以查找函数在so中的偏移，从第一步的地址中获取函数地址
     */
    fd = open(libpath, O_RDONLY);
    if (fd < 0) fatal("failed to open %s", libpath);
    //seek到最后返回的偏移即：so的大小
    size = lseek(fd, 0, SEEK_END);
    if (size <= 0) fatal("lseek() failed for %s", libpath);
    //mmap内存映射
    elf = (Elf_Ehdr *) mmap(0, size, PROT_READ, MAP_SHARED, fd, 0);
    close(fd);
    fd = -1;

    if (elf == MAP_FAILED) fatal("mmap() failed for %s", libpath);
    ctx = (struct ctx *) calloc(1, sizeof(struct ctx));
    if (!ctx) fatal("no memory for %s", libpath);

    ctx->load_addr = (void *) load_addr;
    // section的地址
    shoff = (uint64_t) elf + elf->e_shoff;


    for (int k = 0; k < elf->e_shnum; k++) {
        shoff = shoff + elf->e_shentsize;
        Elf_Shdr *sh = (Elf_Shdr *) shoff;

        switch (sh->sh_type) {
            //动态符号
            case SHT_DYNSYM:
                if (ctx->dynsym) fatal("%s: duplicate DYNSYM sections", libpath); /* .dynsym */
                ctx->dynsym = malloc(sh->sh_size);
                if (!ctx->dynsym) fatal("%s: no memory for .dynsym", libpath);
                memcpy(ctx->dynsym, ((char *) elf) + sh->sh_offset, sh->sh_size);
                ctx->nsyms = (sh->sh_size / sizeof(Elf_Sym));
                break;
                //动态库中所有字符串
            case SHT_STRTAB:
                if (ctx->dynstr) break;    /* .dynstr is guaranteed to be the first STRTAB */
                ctx->dynstr = malloc(sh->sh_size);
                if (!ctx->dynstr) fatal("%s: no memory for .dynstr", libpath);
                memcpy(ctx->dynstr, ((char *) elf) + sh->sh_offset, sh->sh_size);
                break;

            case SHT_PROGBITS:
                if (!ctx->dynstr || !ctx->dynsym) break;
                ctx->bias = (off_t) sh->sh_addr - (off_t) sh->sh_offset;
                k = elf->e_shnum;
                break;
        }
    }
    munmap(elf, size);
    elf = 0;
    if (!ctx->dynstr || !ctx->dynsym) fatal("dynamic sections not found in %s", libpath);

#undef fatal
    LOGI("%s: ok, dynsym = %p, dynstr = %p", libpath, ctx->dynsym, ctx->dynstr);
    return ctx;
    err_exit:
    if (fd >= 0) close(fd);
    if (elf != MAP_FAILED) munmap(elf, size);
    fake_dlclose(ctx);
    return 0;
}

void *fake_dlsym(void *handle, const char *name) {

    struct ctx *ctx = (struct ctx *) handle;
    Elf_Sym *sym = (Elf_Sym *) ctx->dynsym;
    char *strings = (char *) ctx->dynstr;
    for (int k = 0; k < ctx->nsyms; k++, sym++) {
        if (k == ctx->nsyms - 1) {
            LOGI("查找完毕");
        }
        if (strstr(strings + sym->st_name, name) != 0) {
            LOGI("匹配:%s %d %d ", strings + sym->st_name, k, ctx->nsyms);
            //动态库的基地址 + 当前符号section地址 - 偏移地址
            return (char *) ctx->load_addr + sym->st_value - ctx->bias;
        }
    }
    return 0;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_dodola_jvmtilib_JVMTIHelper_openDebuggable(JNIEnv *env, jclass clazz, jstring filename, jobject obj) {
    char *libpath = "/system/lib/libart.so";
    void *handle = fake_dlopen(libpath);
    void (*setJdwpAllowed)(bool);
    //开启Debugged
    setJdwpAllowed = (void (*)(bool)) fake_dlsym(handle, "SetJdwpAllowed");
    setJdwpAllowed(true);

    fake_dlclose(handle);
}