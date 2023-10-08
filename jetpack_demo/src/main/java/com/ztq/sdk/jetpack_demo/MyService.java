package com.ztq.sdk.jetpack_demo;

import androidx.lifecycle.LifecycleService;

public class MyService extends LifecycleService {
    private static final String TAG = "noahedu.MyService";

    private MyServiceObserver myServiceObserver;

    public MyService() {
        myServiceObserver = new MyServiceObserver();
        getLifecycle().addObserver(myServiceObserver);
    }
}