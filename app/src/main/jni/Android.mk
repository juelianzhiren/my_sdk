LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := jni-test
LOCAL_SRC_FILES := com_ztq_sdk_jni_JniInterface.cpp

include $(BUILD_SHARED_LIBRARY)