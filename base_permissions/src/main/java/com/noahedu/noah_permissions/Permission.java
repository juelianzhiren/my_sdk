package com.noahedu.noah_permissions;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Permission {

    private static final String TAG = Permission.class.getName();

    public static void request(Activity activity, OnPermissionCallback callback) {
        if (callback == null) {
            return;
        }
        String[] permissionArray = null;
        List<String> allPermissions = callback.permissions();
        if (allPermissions != null && allPermissions.size() > 0) {
            permissionArray = new String[allPermissions.size()];
            permissionArray = allPermissions.toArray(permissionArray);
            if (EasyPermissions.hasPermissions(activity, permissionArray)) {
                Log.d(TAG, "allPermissionsGranted");
                callback.allPermissionsGranted();
            } else {
                Log.d(TAG, "requestPermissions, activity  = " + activity + "; callback = " + callback);
                PermissionFragment.beginRequest(activity, new ArrayList<>(allPermissions), callback);
            }
        } else {
            Log.d(TAG, "onNoPermissions");
            callback.onNoPermissions();
        }
    }

    public static boolean hasPermissions(Context context, String... perms) {
        return EasyPermissions.hasPermissions(context, perms);
    }
}
