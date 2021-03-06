/* DO NOT EDIT THIS FILE - it is machine generated */
#include <stdio.h>
#include "com_ztq_sdk_jni_JniInterface.h"
/* Header for class com_ztq_sdk_jni_JniInterface */

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_ztq_sdk_jni_JniInterface
 * Method:    get
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_ztq_sdk_jni_JniInterface_get (JNIEnv *env, jobject thiz){
    printf("invoke get in c++\n");
    return env->NewStringUTF("Hello from jni in libjni-test.so !");
}

/*
 * Class:     com_ztq_sdk_jni_JniInterface
 * Method:    set
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_ztq_sdk_jni_JniInterface_set (JNIEnv *env, jobject thiz, jstring string) {
    printf("invoke set in c++\n");
    char* str = (char*)env->GetStringUTFChars(string, NULL);
    printf("%s\n", str);
    env->ReleaseStringUTFChars(string, str);
}

#ifdef __cplusplus
}
#endif