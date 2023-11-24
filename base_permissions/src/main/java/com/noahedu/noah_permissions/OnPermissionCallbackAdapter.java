package com.noahedu.noah_permissions;

import java.util.List;

public abstract class OnPermissionCallbackAdapter implements OnPermissionCallback {

    @Override
    public List<String> permissions() {
        return null;
    }

    @Override
    public void permissionsDenied() {

    }

    @Override
    public void allPermissionsGranted() {

    }

    @Override
    public void onNoPermissions() {

    }
}
