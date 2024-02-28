package com.ztq.sdk.constant;

import android.graphics.Typeface;

/**
 * 常量类
 */
public class Constants {
    public static final String CHARACTER_COMMA = ",";

    /**是否显示日志，即打印Log日志，上线版本改为false*/
    private static boolean mIsShowLog = true;

    public static void updateShowLogValue(boolean isShowLog) {
        mIsShowLog = isShowLog;
    }

    public static boolean isShowLog() {
        return mIsShowLog;
    }

    public static final String API_GET_LAUNCH_ADS = "http://appstore.noahedu.com/search/v2/api/apk/getlaunchad";
    public static final String API_GET_LAUNCH_ADS_WITH_HTTPS = "https://appstore.youxuepai.com/search/v2/api/apk/getlaunchad";

    public static final String KEY_MACHINENO = "machineno";
    public static final String KEY_PRODUCT = "product";
    public static final String KEY_VERSION_CODE = "version_code";
    public static final String KEY_XUEDUANNAME = "xueduanname";
    public static final String KEY_RD = "rd";

    public static Typeface mCarcassTypeface;

    static {
        try {
            mCarcassTypeface = Typeface.createFromFile("/system/fonts/hifont_kai.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}