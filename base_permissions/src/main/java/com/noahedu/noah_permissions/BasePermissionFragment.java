package com.noahedu.noah_permissions;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

public abstract class BasePermissionFragment extends Fragment implements OnClickListener, EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {

    private static final String TAG = BasePermissionFragment.class.getSimpleName();
    private static final int REQUEST_PERMISSION_CODE = 12345;

    private Dialog mDialog;
    private boolean mInterrupt;

    /**
     * 需要的权限列表
     */
    protected abstract List<String> permissions();

    /**
     * 无权限设置调用
     */
    protected abstract void onNoPermissions();

    /**
     * 拒绝的权限
     *
     * @return
     */
    protected abstract void permissionsDenied();

    /**
     * 所有权限通过后调用
     */
    protected abstract void allPermissionsGranted();

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        if (mInterrupt || (mDialog != null && mDialog.isShowing())) {
            return;
        }
        String[] permissionArray = null;
        List<String> strPermissions = permissions();
        if (strPermissions != null && strPermissions.size() > 0) {
            permissionArray = new String[strPermissions.size()];
            permissionArray = strPermissions.toArray(permissionArray);
//            permissionArray = (String[]) strPermissions.toArray();
            if (EasyPermissions.hasPermissions(getActivity(), permissionArray)) {
                Log.d(TAG, "onResume allPermissionsGranted");
                mInterrupt = true;
                allPermissionsGranted();
            } else {
                Log.d(TAG, "onResume requestPermissions");
                String strRationaleText = PermissionsUtil.getRationaleText(getActivity(), permissionArray);
                // 请求权限
                EasyPermissions.requestPermissions(this, strRationaleText, REQUEST_PERMISSION_CODE, permissionArray);
            }
        } else {
            Log.d(TAG, "onResume onNoPermissions");
            onNoPermissions();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_CODE)
    public void allRequestPermissionGranted() {
        Log.d(TAG, "allRequestPermissionGranted");
        mInterrupt = true;
        allPermissionsGranted();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted requestCode : " + requestCode + " perms : " + perms.size());
//        permissionsGranted(perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied requestCode : " + requestCode + " perms : " + perms.size());
        if (mInterrupt) {
            return;
        }
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Log.d(TAG, "onPermissionsDenied somePermissionPermanentlyDenied");
            if (mDialog == null) {
                mDialog = PermissionsUtil.createDialog(getActivity(), "", this);
            }

            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        } else {
            Log.d(TAG, "onPermissionsDenied permissionsDenied");
//            permissionsDenied(perms);
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d(TAG, "onRationaleAccepted requestCode : " + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Log.d(TAG, "onRationaleDenied requestCode : " + requestCode);
        mInterrupt = true;
        permissionsDenied();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_POSITIVE) {
            PermissionsUtil.jumpToSettings(this.getActivity());
        } else if (which == Dialog.BUTTON_NEGATIVE) {
            permissionsDenied();
        } else {
            throw new IllegalStateException("Unknown button type: " + which);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.cancel();
            }

            mDialog = null;
        }
    }
}
