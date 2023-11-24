package com.noahedu.noah_permissions;

import java.util.List;

public interface OnPermissionCallback {
    /**
     * 需要的权限列表
     */
    List<String> permissions();

    /**
     * 拒绝权限的回调
     *
     * @return
     */
    void permissionsDenied();

    /**
     * 所有权限通过后调用
     */
    void allPermissionsGranted();

    /**
     * 无权限申请时调用
     */
    void onNoPermissions();
}
