package com.noahedu.noah_permissions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: zhengjf
 * Date: 2021/8/09 20:41
 * 权限代理，如果不想用继承Activity的方式可以使用该代理进行控制
 * 用法：
 * 1 new PermissionProxy()初始化，并将权限列表作为参数传进来
 * 2 onResume时调用permissionProxy.onResume()
 * 3 onDestroy时调用permissionProxy.onDestroy()
 * 4 onRequestPermissionsResult时调用permissionProxy.onRequestPermissionsResult()
 * 默认拒绝权限会关闭Activity，如果自己处理可以调用setOnPermissionListener
 */
public class PermissionProxy implements DialogInterface.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_CODE = 54321;

    private WeakReference<Activity> activityRef;

    private Dialog mDialog;
    private boolean mInterrupt;
    private List<String> permissions = new ArrayList<>();

    public PermissionProxy(Activity activity, String... permissionArr) {
        activityRef = new WeakReference<>(activity);
        setPermissions(permissionArr);
    }

    public interface OnPermissionListener {
        void onPermissionsDenied();
        void onAllPermissionsGranted();
        /**
         * 无权限设置调用
         */
        void onNoPermissions();
    }

    /**
     * Activity/Fragment onResume时调用
     */
    public void onResume() {

        Log.d(PermissionConsts.TAG, "onResume");

        if (permissions.isEmpty()) {
            return;
        }

        if (mInterrupt || (mDialog != null && mDialog.isShowing())) {
            return;
        }

        String[] permissionArray = null;
        List<String> strPermissions = permissions;
        if (strPermissions != null && strPermissions.size() > 0) {
            permissionArray = new String[strPermissions.size()];
            permissionArray = strPermissions.toArray(permissionArray);
            if (EasyPermissions.hasPermissions(getContext(), permissionArray)) {
                Log.d(PermissionConsts.TAG, "onResume allPermissionsGranted");
                mInterrupt = true;
                notifyAllPermissionsGranted();
            } else {
                Log.d(PermissionConsts.TAG, "onResume requestPermissions");
                String strRationaleText = PermissionsUtil.getRationaleText(getContext(), permissionArray);
                if (activityRef.get() != null) {
                    // 请求权限
                    EasyPermissions.requestPermissions(activityRef.get(), strRationaleText, REQUEST_CODE, permissionArray, new MyRationaleCallbacks());
                }
            }
        } else {
            Log.d(PermissionConsts.TAG, "onResume onNoPermissions");
            notifyNoPermissions();
        }

    }

    /**
     * Activity/Fragment onResume时调用
     */
    public void onDestroy() {
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.cancel();
            }

            mDialog = null;
        }
    }

    /**
     * 设置权限回调，如果不设置的话默认拒绝权限后Activity将关闭
     */
    public void setOnPermissionListener(OnPermissionListener onPermissionListener) {
        this.onPermissionListener = onPermissionListener;
    }

    public void setPermissions(String... permissionArr) {
        permissions.clear();
        if (permissionArr == null) {
            return;
        }
        for (String permission : permissionArr) {
            permissions.add(permission);
        }
    }

    public String[] getPermissions() {
        String[] permissionArr = new String[permissions.size()];
        return permissions.toArray(permissionArr);
    }

    /**
     * Activity/Fragment onRequestPermissionsResult时调用
     */
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {

        Log.d(PermissionConsts.TAG, "requestPermissionsResult");
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 是否已经拥有申请的全部权限
     */
    @SuppressLint("Range")
    public boolean hasAllPermissions() {
        if (permissions.isEmpty()) {
            return true;
        }
        String[] permissionArr = new String[permissions.size()];
        permissions.toArray(permissionArr);
        return EasyPermissions.hasPermissions(getContext(), permissionArr);
    }

    @AfterPermissionGranted(REQUEST_CODE)
    public void allRequestPermissionGranted() {
        Log.d(PermissionConsts.TAG, "allRequestPermissionGranted");
        mInterrupt = true;
        notifyAllPermissionsGranted();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        Log.d(PermissionConsts.TAG, "onPermissionsDenied requestCode : " + requestCode + " perms : " + perms.size());
        if (mInterrupt) {
            return;
        }
        Activity activity = activityRef.get();
        if (activity != null) {
            if (EasyPermissions.somePermissionPermanentlyDenied(activity, perms)) {
                Log.d(PermissionConsts.TAG, "onPermissionsDenied somePermissionPermanentlyDenied");
                showDialog();
            } else {
                Log.d(PermissionConsts.TAG, "onPermissionsDenied permissionsDenied");
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        if (which == Dialog.BUTTON_POSITIVE) {
            PermissionsUtil.jumpToSettings(getContext());
        } else if (which == Dialog.BUTTON_NEGATIVE) {
            notifyPermissionsDenied();
        } else {
            throw new IllegalStateException("Unknown button type: " + which);
        }
    }

    private Context getContext() {
        if (activityRef.get() != null) {
            return activityRef.get();
        }
        return null;
    }

    private void showDialog() {

        Context context = getContext();
        if (context == null) {
            return;
        }
        if (mDialog == null) {
            mDialog = PermissionsUtil.createDialog(getContext(), "", this);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    private void notifyAllPermissionsGranted() {
        if (onPermissionListener == null) {
            return;
        }
        onPermissionListener.onAllPermissionsGranted();
    }

    private void notifyPermissionsDenied() {
        if (onPermissionListener == null) {
            return;
        }
        onPermissionListener.onPermissionsDenied();
    }

    private void notifyNoPermissions() {
        if (onPermissionListener == null) {
            return;
        }
        onPermissionListener.onNoPermissions();
    }

    private class MyRationaleCallbacks implements EasyPermissions.RationaleCallbacks {

        @Override
        public void onRationaleAccepted(int requestCode) {

        }

        @Override
        public void onRationaleDenied(int requestCode) {
            mInterrupt = true;
            notifyPermissionsDenied();
        }
    }

    private OnPermissionListener onPermissionListener = new OnPermissionListener() {
        @Override
        public void onPermissionsDenied() {
            Context context = getContext();
            if (context != null && context instanceof Activity) {
                ((Activity) context).finish();
            }
        }

        @Override
        public void onAllPermissionsGranted() {

        }

        @Override
        public void onNoPermissions() {

        }
    };
}
