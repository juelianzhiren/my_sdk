package com.ztq.sdk;

import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;

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

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        Log.v(TAG, "density = " + density);
    }
}