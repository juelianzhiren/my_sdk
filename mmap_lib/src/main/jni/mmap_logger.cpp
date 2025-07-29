#include <jni.h>
#include <sys/mman.h>
#include <unistd.h>
#include <fcntl.h>
#include <android/log.h>
#include <string.h>

#define LOG_TAG "MMapLogger"

#define PAGE_SIZE sysconf(_SC_PAGE_SIZE)

struct MMapContext {
    int fd;
    char *buffer;
    size_t capacity;
    size_t offset;
};

extern "C" JNIEXPORT jlong JNICALL
Java_com_example_mmap_1lib_MMapLogger_nativeInit(JNIEnv *env, jclass, jstring path, jint size) {
    const char *filePath = env->GetStringUTFChars(path, nullptr);
    int fd = open(filePath, O_RDWR | O_CREAT, 0644);
    env->ReleaseStringUTFChars(path, filePath);

    if (fd < 0) {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "File open failed");
        return 0;
    }

    size_t alignedSize = (size + PAGE_SIZE - 1) & ~(PAGE_SIZE - 1); // 这段代码是Linux系统中用于内存页对齐的经典位运算操作，其核心作用是将任意输入值size向上舍入到最接近的PAGE_SIZE（内存页大小）的整数倍‌
    ftruncate(fd, alignedSize); // 用于调整已打开文件的大小至指定字节长度‌

    char *buffer = (char *) mmap(nullptr, alignedSize,
                                 PROT_READ | PROT_WRITE,
                                 MAP_SHARED, fd, 0);
    if (buffer == MAP_FAILED) {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "mmap failed");
        close(fd);
        return 0;
    }

    auto ctx = new MMapContext{fd, buffer, alignedSize, 0};
    return reinterpret_cast<jlong>(ctx);
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_mmap_1lib_MMapLogger_nativeWrite(JNIEnv *env, jclass, jlong handle, jstring log) {
    auto ctx = reinterpret_cast<MMapContext *>(handle);
    const char *logStr = env->GetStringUTFChars(log, nullptr);
    size_t logLen = strlen(logStr);

    if (ctx->offset + logLen >= ctx->capacity) {
        ctx->offset = 0; // 循环写入
    }

    memcpy(ctx->buffer + ctx->offset, logStr, logLen);
    ctx->offset += logLen;
    msync(ctx->buffer, ctx->capacity, MS_SYNC);  // Linux系统中用于强制同步内存映射数据到磁盘的系统调用
    env->ReleaseStringUTFChars(log, logStr);
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_mmap_1lib_MMapLogger_nativeRelease(JNIEnv *, jclass, jlong handle) {
    auto ctx = reinterpret_cast<MMapContext *>(handle);
    munmap(ctx->buffer, ctx->capacity);
    close(ctx->fd);
    delete ctx;
}