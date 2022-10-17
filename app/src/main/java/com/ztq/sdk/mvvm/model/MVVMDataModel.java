package com.ztq.sdk.mvvm.model;

import com.ztq.sdk.mvvm.viewmodel.MVVMLoadDataCallback;

public class MVVMDataModel {
    public String mData;

    public MVVMDataModel() {
        this.mData = "初始数据";
    }

    public void requestData(MVVMLoadDataCallback callback) {
        this.mData = "数据请求成功";
        callback.onSuccess();
    }
}