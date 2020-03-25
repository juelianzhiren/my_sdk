package com.ztq.sdk.constant;

/**
 * 常量类
 */
public class Constants {
    public static final String CHARACTER_COMMA = ",";

    /**是否显示日志，即打印Log日志*/
    private static boolean mIsShowLog = false;

    public static void updateShowLogValue(boolean isShowLog) {
        mIsShowLog = isShowLog;
    }

    public static boolean isShowLog() {
        return mIsShowLog;
    }
}