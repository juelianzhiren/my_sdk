package com.ztq.sdk.mvvm.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.ztq.sdk.BR;
import com.ztq.sdk.mvvm.model.MVVMDataModel;

public class MVVMDataViewModel extends BaseObservable implements MVVMLoadDataCallback {
    private MVVMDataModel model;

    public MVVMDataViewModel() {
        model = new MVVMDataModel();
    }

    /**
     * 必须添加@Bindable注释
     * @return
     */
    @Bindable
    public String getData() {
        return model.mData;
    }

    public void loadUserData() {
        model.requestData(this);
    }

    @Override
    public void onSuccess() {
        // BR.data对应注解Bindable的getData方法，如果是BR.data1则是对应注解Bindable的getData1方法
        notifyPropertyChanged(BR.data);
    }

    @Override
    public void onFailure() {

    }
}