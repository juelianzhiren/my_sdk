package com.ztq.sdk.api;

import android.util.ArrayMap;

import com.ztq.sdk.api.callback.CallBack;
import com.ztq.sdk.constant.Constants;
import com.ztq.sdk.log.Log;

import java.util.Map;

public class NoaheduAPI<T> extends BaseAPI<T> {
    private static final String TAG = "noahedu.NoaheduAPI";

    /**
     * 获取应用宝库的启动页广告
     * @param machineno
     * @param product
     * @param versionCode
     * @param xueduanName
     * @param callback
     */
    public void getLaunchAdInAppStore(String machineno, String product, int versionCode, String xueduanName, CallBack<T> callback) {
        Map<String, String> map = new ArrayMap<String, String>();
        try {
            map.put(Constants.KEY_MACHINENO, machineno);
            map.put(Constants.KEY_PRODUCT, product);
            map.put(Constants.KEY_VERSION_CODE, versionCode + "");
            map.put(Constants.KEY_XUEDUANNAME, xueduanName);
            map.put(Constants.KEY_RD, (int) (Math.random() * 1000) + "");
            String url = Constants.API_GET_LAUNCH_ADS;
            asynGet(url, map, callback);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "exception in getLaunchAdInAppStore %s", e.getMessage());
        }
    }
}