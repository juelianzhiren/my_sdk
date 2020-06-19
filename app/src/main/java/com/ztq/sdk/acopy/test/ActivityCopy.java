package com.ztq.sdk.acopy.test;

import android.app.Activity;

import com.ztq.sdk.utils.ACopy;

// 声明替换类
@ACopy.CopyFrom("android.app.Activity")
public class ActivityCopy extends ACopy {

    public ActivityCopy(@Instance Activity activity) {
        super(activity);
    }

    //隐藏方法
    public boolean canStartActivityForResult() {
        return (boolean) super.invoke();
    }
}