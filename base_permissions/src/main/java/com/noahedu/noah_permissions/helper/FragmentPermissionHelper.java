package com.noahedu.noah_permissions.helper;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;

public class FragmentPermissionHelper extends BasePermissionsHelper<Fragment> {
    public FragmentPermissionHelper(Fragment host) {
        super(host);
    }

    @Override
    public FragmentManager getFragmentManager() {
        return getHost().getChildFragmentManager();
    }

    @Override
    public void directRequestPermissions(int requestCode, String... perms) {
        getHost().requestPermissions(perms, requestCode);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(String perm) {
        return getHost().shouldShowRequestPermissionRationale(perm);
    }

    @Override
    public Context getContext() {
        return getHost().getActivity();
    }
}

