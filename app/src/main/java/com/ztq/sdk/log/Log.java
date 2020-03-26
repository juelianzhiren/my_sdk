package com.ztq.sdk.log;

import com.ztq.sdk.constant.Constants;

public class Log {
    private static final String TAG = "noahedu.Log";

    public static void v(String tag, String str) {
        if (Constants.isShowLog()) {
            android.util.Log.v(tag, str);
        }
    }

    public static void i(String tag, String str) {
        if (Constants.isShowLog()) {
            android.util.Log.i(tag, str);
        }
    }

    public static void d(String tag, String str) {
        if (Constants.isShowLog()) {
            android.util.Log.d(tag, str);
        }
    }

    public static void w(String tag, String str) {
        if (Constants.isShowLog()) {
            android.util.Log.w(tag, str);
        }
    }

    public static void e(String tag, String str) {
        if (Constants.isShowLog()) {
            android.util.Log.e(tag, str);
        }
    }

    public static void v(String tag, String str, Object... obj) {
        if (Constants.isShowLog()) {
            android.util.Log.v(tag, String.format(str, obj));
        }
    }

    public static void i(String tag, String str, Object... obj) {
        if (Constants.isShowLog()) {
            android.util.Log.i(tag, String.format(str, obj));
        }
    }

    public static void w(String tag, String str, Object... obj) {
        if (Constants.isShowLog()) {
            android.util.Log.w(tag, String.format(str, obj));
        }
    }

    public static void d(String tag, String str, Object... obj) {
        if (Constants.isShowLog()) {
            android.util.Log.d(tag, String.format(str, obj));
        }
    }

    public static void e(String tag, String str, Object... obj) {
        if (Constants.isShowLog()) {
            android.util.Log.e(tag, String.format(str, obj));
        }
    }
}