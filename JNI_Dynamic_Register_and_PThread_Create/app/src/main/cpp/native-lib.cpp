#include <jni.h>
#include <string>

// 日志输出
#include <android/log.h>

#define TAG "JNISTUDY"
// __VA_ARGS__ 代表 ...的可变参数
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG,  __VA_ARGS__);
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG,  __VA_ARGS__);
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG,  __VA_ARGS__);

#include <jni.h>
#include <string>
#include <pthread.h> // 在AS上 pthread不需要额外配置，默认就有

extern "C" JNIEXPORT jstring JNICALL
Java_com_mac_jni04_1study_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "默认就是静态注册的方式";
    return env->NewStringUTF(hello.c_str());
}

// 什么是静态注册？
// 默认情况下，就是静态注册，静态注册比动态注册要简单，但是在诸多系统源码中，会发现大量都是采用动态注册，
// 因为动态注册虽然麻烦，但是比静态注册安全性高，不要暴露包名类名等信息

// 静态注册：优点：开发简单
// 缺点：
// 1.JNI 函数名很长
// 2.捆绑 上层 包名  + 类名
// 3.运行期 才会去 匹配JNI函数，性能上 低于一点点 动态注册
extern "C"
JNIEXPORT void JNICALL
Java_com_mac_jni04_1study_MainActivity_staticRegister(JNIEnv *env, jobject thiz) {}

// ============ 下面是 动态注册区域

// void actionDerry1(JNIEnv * env, jobject mainThis) {
void actionDerry1() {
    // 我想用env，我想用 jobject怎么办？
    LOGI("C++ 动态注册的函数 actionDerry1 执行了啊")
}

void * actionDerry2ABCDEFGDDDDDDDDDD(JNIEnv * env, jobject mainThis, jstring str) {
    // 我想用env，我想用 jobject怎么办？
    const char * str_ = env->GetStringUTFChars(str, NULL);
    LOGD("C++ 动态注册的函数 actionDerry2 str_%s\n", str_)
    env->ReleaseStringUTFChars(str, str_);
    return reinterpret_cast<void *>(800);
}

// 研究第二个参数
/*typedef struct {
    const char* name;      动态注册JNI的函数名 --- Java的动态注册函数
    const char* signature; 函数签名 --- Java的动态注册函数签名
    void*       fnPtr;     函数指针 -- C++的函数
} JNINativeMethod;*/

static const JNINativeMethod methods[] = {
        {"dynamicJavaMethod01", "()V", (void *) (actionDerry1) },
        {"dynamicJavaMethod02", "(Ljava/lang/String;)I", (jint *) (actionDerry2ABCDEFGDDDDDDDDDD)}
};


// java:像 Java的构造函数，如果你不写构造函数，默认就有构造函数，如果你写了 会覆盖
// JNI_OnLoad，如果你不写JNI_OnLoad，默认就有JNI_OnLoad，如果你写了，会覆盖

JavaVM * vm = nullptr; // 不规范  3532532  -3534255

// 逆向工程师，做坏事的
// 在很多的系统源码，或者是其他源码，会大量采用动态注册，动态注册虽然很麻烦，但是这个是必学项
// 动态的优点：1.被反编译后 安全性高一点， 2.在native中的调用，函数名简洁， 3.编译后的函数标记 较短一些
jint JNI_OnLoad(JavaVM * vm, void * args) {
    ::vm = vm;

    JNIEnv * env;

    jint r = vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6);

    // Java中 请求成功值，大概率，200
    // C 中 0就是成功
    // if (r) { // 非0就是ture
    // if (r != 0) {
    if (r != JNI_OK) {
        return -1; // 这个就是让程序奔溃
    }

    // 框架的设计是，一级指针，那么我们只需要传递地址即可   JNIEnv  env;   系统(&env)
    // 框架的设计是，二级指针，那么我们只需要传递地址即可   JNIEnv * env;   系统(&env)

    // env->RegisterMethods(一次性注册1000个 动态注册的JNI函数)
    // jint RegisterNatives(jclass clazz, const JNINativeMethod* methods, jint nMethods)
    jclass mainActivityClass = env->FindClass("com/mac/jni04_study/MainActivity");
    r = env->RegisterNatives(mainActivityClass, methods, sizeof methods / sizeof(JNINativeMethod));
    if (r) { // 非零true 进if
        LOGD("哎哟，动态注册失败了")
    } else {
        LOGI("动态注册成功")
    }

    return JNI_VERSION_1_6; // 一般会使用最新的JNI版本标记
}

// TODO ===================  下面是 线程的学习

class MyContext {
public:
    // JavaVM * 是全局的，是相当于 APP进程的 全局 成员，他什么都可以
    // JNIEnv *jniEnv = nullptr;  // 不能跨线程 ，会奔溃
    jobject instance = nullptr; // 不能跨线程 ，会奔溃, 并且不能跨函数 也会奔溃
};

// 相当于 Java的 run函数  子线程
// 返回类型    函数指针名       参数
// void* (*__start_routine)(void*)
// void*  return 34535.4f;   return 543532.5;  return 6;  return "sads";
void* cpp_thread_run(void * args) {
    LOGD("C++ Pthread 的 异步线程 启动啦")

    MyContext * context = static_cast<MyContext *>(args);

    // 缺少 env， 缺少 jobject 1
    /*jclass mainActivityCls = context->jniEnv->GetObjectClass(context->instance);
    jmethodID updateActivityUI = context->jniEnv->GetMethodID(mainActivityCls, "updateActivityUI", "()V");
    context->jniEnv->CallVoidMethod(context->instance, updateActivityUI);*/

    JNIEnv* env;
    // 缺少 env
    // jint AttachCurrentThread(JNIEnv** p_env, void* thr_args)
    jint r = ::vm->AttachCurrentThread(reinterpret_cast<JNIEnv **>(&env), nullptr);
    if (r) { // 非0 true 失败
        return nullptr;
    }

    jclass mainActivityCls = env->GetObjectClass(context->instance);
    jmethodID updateActivityUI = env->GetMethodID(mainActivityCls, "updateActivityUI", "()V");
    env->CallVoidMethod(context->instance, updateActivityUI);

    ::vm->DetachCurrentThread(); // 接触当前附加所参数的 env 环境

    return nullptr; // 这个是一个坑，之前有很多同行，就因为没有访问，奔溃，看才找到原因
}

extern "C"
JNIEXPORT void JNICALL
Java_com_mac_jni04_1study_MainActivity_naitveThread(JNIEnv *env, jobject thiz) {
    // 属于什么线程 == Android的主线程

    /*jclass mainActivityCls = env->GetObjectClass(thiz);
    jmethodID updateActivityUI = env->GetMethodID(mainActivityCls, "updateActivityUI", "()V");
    env->CallVoidMethod(thiz, updateActivityUI);*/

    // int pthread_create(pthread_t* __pthread_ptr,   参数一：线程标记ID
    // pthread_attr_t const* __attr,      参数二：pthread配置的参数集，我目前还没用到
    // void* (*__start_routine)(void*),   参数三：函数指针
    // void*);                            参数四：void * args

    MyContext * context = new MyContext;
    // context->jniEnv = env; // 他属于 局部成员，如果是把他提升全局成员？ 提升全局没有用，主线程的env不能切换到子线程作为env
    // context->instance = thiz; // 他属于 局部成员  如果是把他提升全局成员？ 可以，OK
    context->instance = env->NewGlobalRef(thiz); // 把局部成员 提升为 全局成员

    pthread_t pid;

    /*p_void_start; // 视频播放的 线程标记
    p_audio_start; // 音频播放的 线程标记*/

    pthread_create(&pid, nullptr, cpp_thread_run , context);

    // 如果 pthread_join 不写，就是 典型分离线程（说白了：老死不相往来）

    // 如果 pthread_join 写，就是 典型的非分离线程（说白了：类似于Java的守护）

     pthread_join(pid, nullptr); // 我等 子线程昨晚后，我才开始执行下面代码释放

    env->DeleteGlobalRef(context->instance); // 释放全局引用
    delete context;
    context = nullptr;
    // ... 释放工作
}


extern "C"
JNIEXPORT void JNICALL
Java_com_mac_jni04_1study_MainActivity_closeThread(JNIEnv *env, jobject thiz) {
// 释放全局成员  context->instance = env->NewGlobalRef(thiz);
}


// TODO 第三部分 纠结纠结细节 区域 ==================================================

extern "C"
JNIEXPORT void JNICALL
Java_com_mac_jni04_1study_MainActivity_nativeFun1(JNIEnv *env, jobject job) {
    JavaVM * javaVm = nullptr;
    env->GetJavaVM(&javaVm);

    // 打印：当前函数env地址， 当前函数jvm地址， 当前函数job地址,  JNI_OnLoad的jvm地址
    LOGE("nativeFun1 当前函数env地址%p,  当前函数jvm地址:%p,  当前函数job地址:%p, JNI_OnLoad的jvm地址:%p\n", env, javaVm, job, ::vm);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_mac_jni04_1study_MainActivity_nativeFun2(JNIEnv *env, jobject job) {
    JavaVM * javaVm = nullptr;
    env->GetJavaVM(&javaVm);

    // 打印：当前函数env地址， 当前函数jvm地址， 当前函数job地址,  JNI_OnLoad的jvm地址
    LOGE("nativeFun2 当前函数env地址%p,  当前函数jvm地址:%p,  当前函数job地址:%p, JNI_OnLoad的jvm地址:%p\n", env, javaVm, job, ::vm);
}

// C++ 子线程
void * run(void *) { // native的子线程 env地址  和  Java的子线程env地址，一样吗  不一样的
    JNIEnv * newEnv = nullptr;
    ::vm->AttachCurrentThread(&newEnv, nullptr);
    // 打印：当前函数env地址， 当前函数jvm地址， 当前函数clazz地址,  JNI_OnLoad的jvm地址

    // env 2000H  C++的子线程
    LOGE("run jvm地址:%p,  当前run函数的newEnv地址:%p \n", ::vm, newEnv);

    ::vm->DetachCurrentThread();
    return nullptr;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_mac_jni04_1study_MainActivity_staticFun3(JNIEnv *env, jclass clazz) {
    JavaVM * javaVm = nullptr;
    env->GetJavaVM(&javaVm);

    // 打印：当前函数env地址， 当前函数jvm地址， 当前函数clazz地址,  JNI_OnLoad的jvm地址
    LOGE("nativeFun3 当前函数env地址%p,  当前函数jvm地址:%p,  当前函数clazz地址:%p, JNI_OnLoad的jvm地址:%p\n", env, javaVm, clazz, ::vm);

    // 调用run  C++ 子线程
    pthread_t pid;
    pthread_create(&pid, nullptr, run, nullptr);
}

// Java子线程调用的
extern "C"
JNIEXPORT void JNICALL
Java_com_mac_jni04_1study_MainActivity_staticFun4(JNIEnv *env, jclass clazz) {
    JavaVM * javaVm = nullptr;
    env->GetJavaVM(&javaVm);

    // 打印：当前函数env地址， 当前函数jvm地址， 当前函数clazz地址,  JNI_OnLoad的jvm地址
    LOGE("nativeFun4 当前函数env地址%p,  当前函数jvm地址:%p,  当前函数clazz地址:%p, JNI_OnLoad的jvm地址:%p\n", env, javaVm, clazz, ::vm);
}

// 此函数是被MainActivity2调用的：
extern "C"
JNIEXPORT void JNICALL
Java_com_mac_jni04_1study_MainActivity2_nativeFun5(JNIEnv *env, jobject job) {
    JavaVM * javaVm = nullptr;
    env->GetJavaVM(&javaVm);

    // 打印：当前函数env地址， 当前函数jvm地址， 当前函数clazz地址,  JNI_OnLoad的jvm地址
    LOGE("nativeFun5 当前函数env地址%p,  当前函数jvm地址:%p,  当前函数job地址:%p, JNI_OnLoad的jvm地址:%p\n", env, javaVm, job, ::vm);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_mac_jni04_1study_MainActivity2_nativeFun6(JNIEnv *env, jclass clazz) {
    JavaVM * javaVm = nullptr;
    env->GetJavaVM(&javaVm);

    // 打印：当前函数env地址， 当前函数jvm地址， 当前函数clazz地址,  JNI_OnLoad的jvm地址
    LOGE("nativeFun5 当前函数env地址%p,  当前函数jvm地址:%p,  当前函数clazz地址:%p, JNI_OnLoad的jvm地址:%p\n", env, javaVm, clazz, ::vm);
}