package com.ztq.sdk.utils;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtil {

    private static Handler sMainHandler;

    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static Handler getMainH() {
        if (sMainHandler == null) {
            synchronized (Handler.class) {
                if (sMainHandler == null) {
                    sMainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return sMainHandler;
    }

    public static void postMain(Runnable runnable) {
        if (isInMainThread()) {
            runnable.run();
        } else {
            getMainH().post(runnable);
        }
    }
}
