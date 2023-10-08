package com.ztq.sdk.jetpack_demo;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class MyLocationListener implements LifecycleObserver {
    private static final String TAG = "noahedu.MyLocationListener";

    public MyLocationListener(Activity activity, OnLocationChangedListener onLocationChangedListener) {
        initLocationManager();
    }

    private void initLocationManager() {
        Log.v(TAG, "initLocationManager");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void startGetLocation() {
        Log.v(TAG, "startGetLocation");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void stopGetLocation() {
        Log.v(TAG, "stopGetLocation");
    }

    public interface OnLocationChangedListener {
        void onChanged(long longitude, long latitude);
    }
}