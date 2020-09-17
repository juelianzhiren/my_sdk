package com.ztq.targetsdk26_demo;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
    private static final String TAG = "noahedu.MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
    }
}