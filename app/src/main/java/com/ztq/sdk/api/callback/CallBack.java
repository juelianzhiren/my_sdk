package com.ztq.sdk.api.callback;

import android.text.Html;

import com.alibaba.fastjson.JSON;
import com.ztq.sdk.api.constant.ApiConstant;
import com.ztq.sdk.entity.ResultBase;
import com.ztq.sdk.exception.AppException;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.utils.Utils;

import java.lang.reflect.ParameterizedType;

public abstract  class CallBack<T> implements ICallBack<T> {
    private static final String TAG = "noahedu.CallBack";
    // 获得泛型的Class
    private Class<T> mEntityClass;
    private String mResult = "";

    public CallBack() {
        try {
            mEntityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Exception e) {
            e.printStackTrace();
            AppException.unkonw(e);
        }
    }

    public void onSuccess(String result) {
        if (Utils.isNullOrNil(result)) {
            end();
            return;
        }
        Log.v(TAG, "result = %s", Html.fromHtml(result.trim()).toString());
        try {
            T data = JSON.parseObject(result.trim().toString().replace("\uFEFF", ""), mEntityClass);  // 去掉bom头
            if (data != null) {
                ResultBase resultBase = (ResultBase) data;
                if (resultBase.getMsgCode() == ApiConstant.API_RETURN_SUCCESS_CODE) {       // 302表示服务器正确返回数据
                    try {
                        success(data);
                    } catch (Exception e) {
                        Log.e(TAG, "exception in getCode() == "+ ApiConstant.API_RETURN_SUCCESS_CODE + ": %s" + e.getMessage());
                        e.printStackTrace();
                        failure(new AppException(AppException.CODE_RUNTIME_ERROR));
                    }
                } else {
                    failure(new AppException(AppException.CODE_RUNTIME_ERROR, resultBase.getMessage()));
                }
            } else {
                Log.e(TAG, " parseObject get null");
                ResultBase error = getError(result);
                if (error != null) {
                    failure(new AppException(AppException.CODE_RUNTIME_ERROR));
                } else {
                    failure(new AppException(AppException.CODE_UNKNOWN_ERROR));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage() + " exception in onSuccess: %s", result);
            failure(new AppException(AppException.CODE_JSON_PARSE_ERROR));
        }
        end();
    }

    public void onFailure(Exception t, int errorNo, String strMsg) {
        Log.e(TAG, "failure: " + strMsg);
        failure(new AppException(t, errorNo, strMsg));
        end();
    }

    /**
     * 得到错误
     *
     * @param str
     * @return Create at 2014-7-11 下午03:28:40
     */
    private ResultBase getError(String str) {
        ResultBase error = null;
        try {
            error = JSON.parseObject(str, ResultBase.class);
        } catch (Exception e) {
            e.printStackTrace();
            // 抛出类型转换异常
            failure(new AppException(AppException.CODE_JSON_PARSE_ERROR));
        }
        return error;
    }
}