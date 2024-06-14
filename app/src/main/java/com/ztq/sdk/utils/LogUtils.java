package com.ztq.sdk.utils;

import android.util.Log;

public class LogUtils {
    public static boolean isDebug = true;    // 是否需要打印bug，可以在application的onCreate函数里面初始化
    public static final int LOG_LEVEL_V = 1;
    public static final int LOG_LEVEL_I = 2;
    public static final int LOG_LEVEL_W = 3;
    public static final int LOG_LEVEL_E = 4;

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void log(int logLevel, String tag, String msg) {
        if (logLevel == LOG_LEVEL_V) {
            v(tag, msg);
        } else if (logLevel == LOG_LEVEL_I) {
            i(tag, msg);
        } else if (logLevel == LOG_LEVEL_W) {
            w(tag, msg);
        } else if (logLevel == LOG_LEVEL_E) {
            e(tag, msg);
        }
    }

    /**
     * 分段打印出较长log文本
     * @param logLevel
     * @param tag        打印log的标记
     * @param logContent 打印文本
     * @param showLength 规定每段显示的长度（AndroidStudio控制台打印log的最大信息量大小为4k），经过试验，中文最大设置为1950，英文最大设置为3900
     */
    public static void showLargeLog(int logLevel, String tag, String logContent, int showLength) {
        if (logContent.length() > showLength) {
            String show = logContent.substring(0, showLength);
            log(logLevel, tag, show);
            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
            if ((logContent.length() - showLength) > showLength) {
                String partLog = logContent.substring(showLength);
                showLargeLog(logLevel, tag, partLog, showLength);
            } else {
                String printLog = logContent.substring(showLength);
                log(logLevel, tag, printLog);
            }
        } else {
            log(logLevel, tag, logContent);
        }
    }
}