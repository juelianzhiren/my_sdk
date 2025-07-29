package com.example.mmap_lib;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MMapLogger {
    static {
        System.loadLibrary("mmap_logger");
    }

    private static native long nativeInit(String filePath, int bufferSize);

    private static native void nativeWrite(long handle, String log);

    private static native void nativeRelease(long handle);

    private static long nativeHandle;

    public static void init(String filePath, int bufferSize) {
        nativeHandle = nativeInit(filePath, bufferSize);
    }

    public static void writeLog(String tag, String msg) {
        String log = String.format("[%s] %s: %s\n",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                tag, msg);
        nativeWrite(nativeHandle, log);
    }

    public static void release() {
        if (nativeHandle != 0) {
            nativeRelease(nativeHandle);
            nativeHandle = 0;
        }
    }
}
