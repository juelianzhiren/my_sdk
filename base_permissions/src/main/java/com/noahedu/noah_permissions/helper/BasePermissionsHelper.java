package com.noahedu.noah_permissions.helper;

import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;

import androidx.annotation.StyleRes;

import com.noahedu.noah_permissions.EasyPermissions;
import com.noahedu.noah_permissions.RationaleDialogFragment;

public abstract class BasePermissionsHelper<T> extends PermissionHelper<T> {

    private static final String TAG = "BPermissionsHelper";

    public BasePermissionsHelper(T host) {
        super(host);
    }

    public abstract FragmentManager getFragmentManager();

    @Override
    public void showRequestPermissionRationale(String rationale,
                                               String positiveButton,
                                               String negativeButton,
                                               @StyleRes int theme,
                                               int requestCode,
                                               String[] perms,
                                               EasyPermissions.RationaleCallbacks callbacks) {

        FragmentManager fm = getFragmentManager();

        // Check if fragment is already showing
        Fragment fragment = fm.findFragmentByTag(RationaleDialogFragment.TAG);
        if (fragment instanceof RationaleDialogFragment) {
            Log.d(TAG, "Found existing fragment, not showing rationale.");
            return;
        }

        RationaleDialogFragment
                .newInstance(rationale, positiveButton, negativeButton, theme, requestCode, perms)
                .setRationaleCallbacks(callbacks)
                .showAllowingStateLoss(fm, RationaleDialogFragment.TAG);
    }

}
