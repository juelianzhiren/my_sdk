package com.ztq.sdk;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lizejun.demo.lib.base.BaseApplication;
import com.lizejun.demo.lib.base.util.Utils;
import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.log.Log;

import me.jessyan.autosize.AutoSizeConfig;

/**
 * Created by ztq on 2019/9/25.
 */
public class MyApplication extends Application {
    private final String TAG = "noahedu.MyApplication";
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationHolder.getInstance().init(this);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        Log.v(TAG, "density = " + density);

        mInstance = this;
        initRouter(this);
    }

    public static Context getInstance() {
        return mInstance;
    }

    private void initRouter(MyApplication mApplication) {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (Utils.isApkInDebug(mInstance)) {
            //打印日志
            ARouter.openLog();
            //开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！
            //线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(mApplication);
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