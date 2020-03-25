package com.ztq.sdk;

import android.content.Context;
import android.os.Environment;

import com.ztq.sdk.constant.Constants;
import com.ztq.sdk.helper.MyHandlerThread;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationHolder {
    private static final String TAG = "noahedu.ApplicationHolder";
    private static ApplicationHolder mInstance;
    private Context mContext;
    private static final String mCfgFileName = "my_sdk.cfg";
    private static final String KEY_IS_SHOW_LOG = "is_show_log";

    private ApplicationHolder() {

    }

    public static ApplicationHolder getInstance() {
        if (mInstance == null) {
            synchronized (ApplicationHolder.class) {
                if (mInstance == null) {
                    mInstance = new ApplicationHolder();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        new MyHandlerThread();
        readConfigAndSetting();
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 读取配置文件，以及设置
     */
    private void readConfigAndSetting() {
        Properties props = new Properties();

        String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(new File(externalPath, mCfgFileName)));
            props.load(in);

            String isShowLogStr = props.getProperty(KEY_IS_SHOW_LOG);
            boolean isShowLog = Boolean.valueOf(isShowLogStr);
            Constants.updateShowLogValue(isShowLog);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}