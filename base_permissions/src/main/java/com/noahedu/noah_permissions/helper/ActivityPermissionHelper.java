package com.noahedu.noah_permissions.helper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.core.app.ActivityCompat;

import com.noahedu.noah_permissions.EasyPermissions;
import com.noahedu.noah_permissions.RationaleDialogFragment;


/**
 * Permissions helper for {@link Activity}.
 */
class ActivityPermissionHelper extends PermissionHelper<Activity> {
    private static final String TAG = "ActPermissionHelper";

    public ActivityPermissionHelper(Activity host) {
        super(host);
    }

    @Override
    public void directRequestPermissions(int requestCode, @NonNull String... perms) {
        ActivityCompat.requestPermissions(getHost().getParent() != null ? getHost().getParent() : getHost(), perms, requestCode);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String perm) {
        return ActivityCompat.shouldShowRequestPermissionRationale(getHost().getParent() != null ? getHost().getParent() : getHost(), perm);
    }

    @Override
    public Context getContext() {
        return getHost();
    }

    @Override
    public void showRequestPermissionRationale(@NonNull String rationale,
                                               @NonNull String positiveButton,
                                               @NonNull String negativeButton,
                                               @StyleRes int theme,
                                               int requestCode,
                                               @NonNull String[] perms,
                                               EasyPermissions.RationaleCallbacks rationaleCallbacks) {
        FragmentManager fm = getHost().getFragmentManager();

        // Check if fragment is already showing
        Fragment fragment = fm.findFragmentByTag(RationaleDialogFragment.TAG);
        if (fragment instanceof RationaleDialogFragment) {
            Log.d(TAG, "Found existing fragment, not showing rationale.");
            return;
        }

        RationaleDialogFragment
                .newInstance(rationale, positiveButton, negativeButton, theme, requestCode, perms)
                .setRationaleCallbacks(rationaleCallbacks)
                .showAllowingStateLoss(fm, RationaleDialogFragment.TAG);
    }
}
