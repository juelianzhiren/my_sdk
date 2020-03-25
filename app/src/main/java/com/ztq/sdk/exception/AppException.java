package com.ztq.sdk.exception;

import android.content.Context;

import com.ztq.sdk.ApplicationHolder;
import com.ztq.sdk.R;
import com.ztq.sdk.utils.Utils;

public class AppException extends Exception {
    private static final String TAG = "noahedu.AppException";
    private int code;
    private String errorMessage;

    /**网络异常*/
    public static final int CODE_NETWORK_ERROR = 1;
    /**JSON转换异常*/
    public static final int CODE_JSON_PARSE_ERROR = 2;
    /**服务器异常*/
    public static final int CODE_SERVER_ERROR = 3;
    /**IO异常*/
    public static final int CODE_IO_ERROR = 4;
    /**运行错误*/
    public static final int CODE_RUNTIME_ERROR = 5;
    /**未知异常*/
    public static final int CODE_UNKNOWN_ERROR = 6;

    public AppException(Exception e) {
        super(e);
    }

    public AppException(int code) {
        this.code = code;
        setMeaningFullText(ApplicationHolder.getInstance().getContext());
    }

    public AppException(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public AppException(Exception e, int code, String errorMessage) {
        super(e);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        setMeaningFullText(ApplicationHolder.getInstance().getContext());
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static AppException unkonw(Exception e){
        return new AppException(CODE_UNKNOWN_ERROR);
    }

    /**
     * 对错误信息做处理
     *Create at 2014-8-1 下午03:20:23
     */
    private void setMeaningFullText(Context ctx){
        if (ctx == null) {
            errorMessage = "";
            return;
        }
        switch(getCode()){
            case CODE_NETWORK_ERROR:
                errorMessage = Utils.getResuorceString(ctx, R.string.network_error);
                break;
            case CODE_JSON_PARSE_ERROR:
                errorMessage = Utils.getResuorceString(ctx, R.string.json_parse_error);
                break;
            case CODE_SERVER_ERROR:
                errorMessage = Utils.getResuorceString(ctx, R.string.server_error);
                break;
            case CODE_IO_ERROR:
                errorMessage = Utils.getResuorceString(ctx, R.string.io_error);
                break;
            case CODE_RUNTIME_ERROR:
                errorMessage = Utils.getResuorceString(ctx, R.string.runtime_error);
                break;
            case CODE_UNKNOWN_ERROR:
                errorMessage = Utils.getResuorceString(ctx, R.string.unknown_error);
                break;
        }
    }
}