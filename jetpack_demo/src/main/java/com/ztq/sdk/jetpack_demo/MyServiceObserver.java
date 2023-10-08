package com.ztq.sdk.jetpack_demo;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class MyServiceObserver implements LifecycleObserver {
    private static final String TAG = "noahedu.MyServiceObserver";

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void startGetLocation() {
        Log.v(TAG, "startGetLocation");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void stopGetLocation() {
        Log.v(TAG, "stopGetLocation");
    }
}