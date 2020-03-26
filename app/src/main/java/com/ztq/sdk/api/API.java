package com.ztq.sdk.api;

import com.ztq.sdk.api.callback.CallBack;
import com.ztq.sdk.entity.ResultLaunchAds;

public class API {
    private static final String TAG = "noahedu.API";

    /**
     * 获取应用宝库的启动页广告
     * @param machineno
     * @param product
     * @param versionCode
     * @param xueduanName
     * @param callback
     */
    public static void getLaunchAdInAppStore(String machineno, String product, int versionCode, String xueduanName, CallBack<ResultLaunchAds> callback) {
        NoaheduAPI<ResultLaunchAds> noaheduAPI = new NoaheduAPI<ResultLaunchAds>();
        noaheduAPI.getLaunchAdInAppStore(machineno, product, versionCode, xueduanName, callback);
    }
}