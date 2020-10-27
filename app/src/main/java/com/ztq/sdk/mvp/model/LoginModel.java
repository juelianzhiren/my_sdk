package com.ztq.sdk.mvp.model;

import com.ztq.sdk.mvp.presenter.OnLoginFinishedListener;

/**
 * Created by Anthony on 2016/2/15.
 * Class Note:模拟登陆的操作的接口，实现类为LoginModelImpl.相当于MVP模式中的Model层
 */
public interface LoginModel {
    void login(String username, String password, OnLoginFinishedListener listener);
}