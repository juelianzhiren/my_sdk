package com.ztq.sdk.activity;

import android.content.Context;
import android.os.Bundle;

import com.ztq.sdk.R;
import com.ztq.sdk.api.API;
import com.ztq.sdk.api.callback.CallBack;
import com.ztq.sdk.entity.ResultLaunchAds;
import com.ztq.sdk.exception.AppException;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.utils.Utils;
import com.ztq.sdk.widget.DYLoadingView;

import me.jessyan.autosize.internal.CancelAdapt;

public class NetAPIActivity extends BaseActivity implements CancelAdapt {
    private static final String TAG = "noahedu.NetAPIActivity";
    private Context mContext;
    private DYLoadingView mLoadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_api);
        mLoadingView = findViewById(R.id.loadview);
        mLoadingView.start();
        Log.v(TAG, "onCreate");

        mContext = this;
        getData();
    }

    private void getData() {
        String machinno = "0290937677298729369";
        String product = "P381";
        int versionCode = 140;
        String xueduanname = "小学";
        Log.v(TAG, "getLaunchAdInAppStore begin");
        API.getLaunchAdInAppStore(machinno, product, versionCode, xueduanname, new CallBack<ResultLaunchAds>() {
            @Override
            public void prepare() {

            }

            @Override
            public void success(ResultLaunchAds resultLaunchAd) {
                Log.v(TAG, "getLaunchAdInAppStore success,");
            }

            @Override
            public void failure(AppException e) {
                Log.v(TAG, "getLaunchAdInAppStore failure, code = " + e.getCode() + "; errormsg = " + e.getErrorMessage());
                e.printStackTrace();
                if (e.getCode() == AppException.CODE_NETWORK_ERROR) {     // 网路异常
                    Utils.showToast(mContext, R.string.network_error);
                } else if (e.getCode() == AppException.CODE_API_NOT_FOUND) {   // 接口不存在
                    Utils.showToast(mContext, R.string.api_not_found);
                } else if (e.getCode() == AppException.CODE_SERVER_ERROR) {    // 服务器异常
                    Utils.showToast(mContext, R.string.server_error);
                }
            }

            @Override
            public void end() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoadingView.stop();
    }
}