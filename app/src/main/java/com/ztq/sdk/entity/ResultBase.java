package com.ztq.sdk.entity;

/**
 * Created by ztq on 2018/9/17.
 */
public class ResultBase {
    private int msgCode;
    private String message;
    private String field;

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}