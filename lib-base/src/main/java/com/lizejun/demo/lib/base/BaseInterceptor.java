package com.lizejun.demo.lib.base;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;

@Interceptor(priority = 1, name = "重新分组进行拦截")
public class BaseInterceptor implements IInterceptor {
    private static final String TAG = "noahedu.BaseInterceptor";

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        Log.v(TAG, "process, extra = " + postcard.getExtra() + "; isLogin = " + postcard.getExtras().getBoolean(ConstantMap.IS_LOGIN));
        if (postcard.getExtra() == ConstantMap.LOGIN_EXTRA) {
            boolean isLogin = postcard.getExtras().getBoolean(ConstantMap.IS_LOGIN);
            if (!isLogin) {
                ARouter.getInstance().build(RouterMap.INTER_MIDDLE_ACTIVITY).navigation();
            } else {
                postcard.withString(ConstantMap.IS_LOGIN_EXTRA, "登录了!");
                callback.onContinue(postcard);
            }
        } else {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
        Log.v(TAG, "init");
    }
}
