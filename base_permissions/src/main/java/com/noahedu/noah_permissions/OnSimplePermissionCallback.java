package com.noahedu.noah_permissions;

import java.util.ArrayList;
import java.util.List;

/**
 * 因为noah_player中有ACopy拷贝类，因此尽量不改该类
 */
public class OnSimplePermissionCallback implements OnPermissionCallback {

    private List<String> permissions = new ArrayList<>();

    public OnSimplePermissionCallback(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public List<String> permissions() {
        return permissions;
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
