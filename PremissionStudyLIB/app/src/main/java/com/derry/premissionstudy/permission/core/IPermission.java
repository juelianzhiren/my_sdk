package com.derry.premissionstudy.permission.core;

// 告诉外界
public interface IPermission {

    void granted(); // 已经授权

    void cancel(); // 取消权限

    void denied(); // 拒绝权限
}
