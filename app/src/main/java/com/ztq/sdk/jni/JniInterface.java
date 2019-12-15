package com.ztq.sdk.jni;

public class JniInterface {
    static {
        System.loadLibrary("jni-test");
    }

    public native String get();
    public native void set(String str);
}