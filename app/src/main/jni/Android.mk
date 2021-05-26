LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := jni-test

#LOCAL_STATIC_LIBRARIES  := libHWRecogPinYin.a
#LOCAL_STATIC_LIBRARY := $(LOCAL_PATH)/arm64-v8a/libHWRecogPinYin.a
APP_ALLOW_MISSING_DEPS := true

LOCAL_SRC_FILES := com_ztq_sdk_jni_JniInterface.cpp


include $(BUILD_SHARED_LIBRARY)