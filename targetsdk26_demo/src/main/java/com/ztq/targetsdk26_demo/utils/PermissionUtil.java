package com.ztq.targetsdk26_demo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {

    public static String[] requestPermissionsIfNeed(Context context, int reqCode, String... permissions) {
        List<String> reqPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                reqPermissions.add(permission);
            }
        }
        String[] reqPermissionArr = new String[reqPermissions.size()];
        reqPermissions.toArray(reqPermissionArr);

        if (reqPermissionArr.length != 0) {
            requestPermissions(context, reqPermissionArr, reqCode);
        }
        return reqPermissionArr;
    }

    /**
     * 查看是否有权限 context必须是activity
     *
     * @param permission 如(Manifest.permission.CAMERA)
     */
    public static boolean checkPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context != null && context instanceof Activity) {
                Activity activity = (Activity) context;
                if (activity.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public static void requestPermission(Context context, String permission, int reqCode) {
        String[] permissions = new String[]{permission};
        requestPermissions(context, permissions, reqCode);
    }

    public static void requestPermissions(Context context, String[] permissions, int reqCode) {
        if (context != null && context instanceof Activity) {
            Activity activity = (Activity) context;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(permissions, reqCode);
            }
        }
    }
}