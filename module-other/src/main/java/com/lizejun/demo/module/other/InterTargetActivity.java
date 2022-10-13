package com.lizejun.demo.module.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lizejun.demo.lib.base.ConstantMap;
import com.lizejun.demo.lib.base.RouterMap;

@Route(path = RouterMap.INTER_TARGET_ACTIVITY, extras = ConstantMap.LOGIN_EXTRA)
public class InterTargetActivity extends FragmentActivity {
    private static final String TAG = "noahedu.InterTargetActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
    }
}