package com.ztq.sdk;

import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ztq.sdk.helper.MyHandlerThread;

import me.jessyan.autosize.AutoSizeConfig;

/**
 * Created by ztq on 2019/9/25.
 */
public class MyApplication extends Application {
    private final String TAG = "noahedu.MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationHolder.getInstance().init(this);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        Log.v(TAG, "density = " + density);
    }

    @Override
    public void onTrimMemory(int level) {
        Log.v(TAG, "onTrimMemory, level = " + level);
        super.onTrimMemory(level);
    }

    @Override
    public void onTerminate() {
        Log.v(TAG, "onTerminate");
        super.onTerminate();
    }
}