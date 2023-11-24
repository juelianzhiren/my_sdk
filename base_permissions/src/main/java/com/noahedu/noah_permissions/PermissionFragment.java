package com.noahedu.noah_permissions;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class PermissionFragment extends Fragment implements DialogInterface.OnClickListener, EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {

    private static final String TAG = PermissionFragment.class.getSimpleName();

    /**
     * 请求的权限组
     */
    private static final String REQUEST_PERMISSIONS = "request_permissions";

    /**
     * 请求码（自动生成）
     */
    private static final String REQUEST_CODE = "request_code";

    /**
     * 权限请求码存放集合
     */
    private static final List<Integer> REQUEST_CODE_ARRAY = new ArrayList<>();

    public PermissionFragment() {

    }

    public static PermissionFragment newInstance(int requestCode, ArrayList<String> permissions) {
        Log.d(TAG, "PermissionFragment newInstance");
        PermissionFragment fragment = new PermissionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(REQUEST_CODE, requestCode);
        bundle.putStringArrayList(REQUEST_PERMISSIONS, permissions);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static void beginRequest(Activity activity, ArrayList<String> permissions, OnPermissionCallback callback) {
        Activity rootActivity = activity.getParent() == null ? activity : activity.getParent();
        Fragment fragmentOld = rootActivity.getFragmentManager().findFragmentByTag(TAG);
        if (fragmentOld != null && fragmentOld instanceof PermissionFragment) {
            return;
        }

        int requestCode;
        // 请求码随机生成，避免随机产生之前的请求码，必须进行循环判断
        do {
            // 新版本的 Support 库限制请求码必须小于 65536
            // 旧版本的 Support 库限制请求码必须小于 256
            requestCode = new Random().nextInt((int) Math.pow(2, 8));
        } while (REQUEST_CODE_ARRAY.contains(requestCode));
        // 标记这个请求码已经被占用
        REQUEST_CODE_ARRAY.add(requestCode);
        PermissionFragment fragment = PermissionFragment.newInstance(requestCode, permissions);
        // 设置保留实例，不会因为屏幕方向或配置变化而重新创建
        fragment.setRetainInstance(true);
        // 设置权限申请标记
        fragment.setRequestFlag(true);
        // 设置权限回调监听
        fragment.setCallBack(callback);
        // 绑定到 Activity 上面
        fragment.attachActivity(rootActivity);
    }

    /**
     * 是否申请了特殊权限
     */
    private boolean mRequested;

    /**
     * 权限申请标记
     */
    private boolean mRequestFlag;

    /**
     * 权限回调对象
     */
    private OnPermissionCallback mCallBack;

    private Dialog mDialog;

    private boolean mInterrupt;

    private static boolean mPermanentlyDenied = false;

    /**
     * 绑定 Activity
     */
    public void attachActivity(Activity activity) {
        Log.d(TAG, "attachActivity activity : " + activity);
        activity.getFragmentManager().beginTransaction().add(this, TAG).commitAllowingStateLoss();
    }

    /**
     * 解绑 Activity
     */
    public void detachActivity(Activity activity) {
        Log.d(TAG, "detachActivity activity : " + activity);
        activity.getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }

    /**
     * 设置权限监听回调监听
     */
    public void setCallBack(OnPermissionCallback callback) {
        mCallBack = callback;
    }

    /**
     * 权限申请标记（防止系统杀死应用后重新触发请求的问题）
     */
    public void setRequestFlag(boolean flag) {
        mRequestFlag = flag;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消引用监听器，避免内存泄漏
        mCallBack = null;

        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.cancel();
            }
            mDialog = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mInterrupt || (mDialog != null && mDialog.isShowing())) {
            return;
        }

        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        final List<String> allPermissions = arguments.getStringArrayList(REQUEST_PERMISSIONS);
        final int requestCode = arguments.getInt(REQUEST_CODE);

        // 如果当前 Fragment 是通过系统重启应用触发的，则不进行权限申请
        if (!mRequestFlag) {
            // 释放对这个请求码的占用
            REQUEST_CODE_ARRAY.remove((Integer) requestCode);
            // 将 Fragment 从 Activity 移除
            detachActivity(getActivity());
            return;
        }

        // 如果在 Activity 不可见的状态下添加 Fragment 并且去申请权限会导致授权对话框显示不出来
        // 所以必须要在 Fragment 的 onResume 来申请权限，这样就可以保证应用回到前台的时候才去申请权限
        if (mRequested) {
            return;
        }
        mRequested = true;

        request(allPermissions, requestCode);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 永久拒绝的权限，引导startActivityForResult去设置界面返回后，可能需要重新判断授权结果，或者放resume也可以
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult requestCode : " + requestCode + ", resultCode : " + resultCode + ", data : " + data);
        Activity activity = getActivity();
        Bundle arguments = getArguments();
        if (activity == null || arguments == null ||
                requestCode != arguments.getInt(REQUEST_CODE)) {
            return;
        }

        final ArrayList<String> allPermissions = arguments.getStringArrayList(REQUEST_PERMISSIONS);
        if (allPermissions == null || allPermissions.isEmpty()) {
            return;
        }

        if (!isAdded()) {
            return;
        }

        request(allPermissions, requestCode);
    }

    /**
     * 请求权限
     *
     * @param allPermissions
     * @param requestCode
     */
    public void request(List<String> allPermissions, int requestCode) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (allPermissions == null || allPermissions.size() == 0) {
            return;
        }

        String[] permsArray = new String[allPermissions.size()];
        permsArray = allPermissions.toArray(permsArray);
        if (EasyPermissions.hasPermissions(activity, permsArray)) {
            mInterrupt = true;
            if (mCallBack != null) {
                Log.d(TAG, "mCallBack allPermissionsGranted");
                mCallBack.allPermissionsGranted();
            }
            // 释放对这个请求码的占用
            REQUEST_CODE_ARRAY.remove((Integer) requestCode);
            // 将 Fragment 从 Activity 移除
            detachActivity(getActivity());
        } else if (mPermanentlyDenied) { // 如果在OnResume里申请权限，可能会导致死循环，此处提前提示
            if (mDialog == null) {
                mDialog = PermissionsUtil.createDialog(getActivity(), PermissionsUtil.getPermanentlyDeniedText(getActivity(), allPermissions), true, this);
            }
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        } else {
            // 请求权限
            String strRationaleText = PermissionsUtil.getRationaleText(activity, permsArray);
            EasyPermissions.requestPermissions(this, strRationaleText, requestCode, permsArray);
            Log.d(TAG, "EasyPermissions.requestPermissions");
        }
//        EasyPermissions.somePermissionPermanentlyDenied(this, allPermissions)
//        EasyPermissions.somePermissionDenied(this, permsArray)
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult requestCode : " + requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        if (!mPermanentlyDenied) {
            // 释放对这个请求码的占用
            REQUEST_CODE_ARRAY.remove((Integer) requestCode);
            // 将 Fragment 从 Activity 移除
            detachActivity(getActivity());
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted requestCode : " + requestCode + " perms : " + perms.size());
        Bundle arguments = getArguments();
        if (arguments == null || requestCode != arguments.getInt(REQUEST_CODE)) {
            return;
        }
        final ArrayList<String> allPermissions = arguments.getStringArrayList(REQUEST_PERMISSIONS);
        if (allPermissions == null || allPermissions.isEmpty()) {
            return;
        }
        if (perms == null) {
            return;
        }
        if (perms.size() == allPermissions.size()) {
            mInterrupt = true;
            if (mCallBack != null) {
                Log.d(TAG, "mCallBack allPermissionsGranted");
                mCallBack.allPermissionsGranted();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied requestCode : " + requestCode + " perms : " + perms.size());
        if (mInterrupt) {
            return;
        }
        if (perms == null) {
            return;
        }
//        String[] permsArray = new String[perms.size()];
//        permsArray = perms.toArray(permsArray);
//        boolean flag1 = EasyPermissions.somePermissionPermanentlyDenied(this, perms);
//        boolean flag2 = EasyPermissions.somePermissionDenied(this, permsArray);
//        Log.d(TAG, "onPermissionsDenied somePermissionPermanentlyDenied : " + flag1);
//        Log.d(TAG, "onPermissionsDenied somePermissionDenied : " + flag2);

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Log.d(TAG, "onPermissionsDenied PermissionPermanentlyDenied");
            mInterrupt = true;
            mPermanentlyDenied = true;
            if (mDialog == null) {
                mDialog = PermissionsUtil.createDialog(getActivity(), PermissionsUtil.getPermanentlyDeniedText(getActivity(), perms), true, this);
            }
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        } else {
            if (mCallBack != null) {
                Log.d(TAG, "mCallBack permissionsDenied");
                mCallBack.permissionsDenied();
            }
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
        if (mCallBack != null) {
            Log.d(TAG, "mCallBack permissionsDenied");
            mCallBack.permissionsDenied();
        }
        // 释放对这个请求码的占用
        REQUEST_CODE_ARRAY.remove((Integer) requestCode);
        // 将 Fragment 从 Activity 移除
        detachActivity(getActivity());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        int requestCode = -1;
        Bundle arguments = getArguments();
        if (arguments != null) {
            requestCode = arguments.getInt(REQUEST_CODE, -1);
        }

        if (which == Dialog.BUTTON_POSITIVE) {
            if (requestCode != -1) {
                PermissionsUtil.jumpToSettingsForResult(this, requestCode);
            } else {
                PermissionsUtil.jumpToSettings(getActivity());
            }
        } else if (which == Dialog.BUTTON_NEGATIVE) {
            if (mCallBack != null) {
                Log.d(TAG, "mCallBack permissionsDenied");
                mCallBack.permissionsDenied();
            }
            // 释放对这个请求码的占用
            REQUEST_CODE_ARRAY.remove((Integer) requestCode);
            // 将 Fragment 从 Activity 移除
            detachActivity(getActivity());
        } else {
            throw new IllegalStateException("Unknown button type: " + which);
        }
    }
}
