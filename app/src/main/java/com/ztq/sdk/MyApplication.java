package com.ztq.sdk;

import android.app.Application;

import com.ztq.sdk.helper.MyHandlerThread;

/**
 * Created by ztq on 2019/9/25.
 */
public class MyApplication extends Application {
    private final String TAG = "noahedu.MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        new MyHandlerThread();
    }
}