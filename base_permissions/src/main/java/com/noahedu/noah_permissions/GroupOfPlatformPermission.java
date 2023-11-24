package com.noahedu.noah_permissions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 针对android Q的权限组名字
 */
public class GroupOfPlatformPermission {
    private static final ArrayMap<String, String> sd = new ArrayMap<>();
    private static final ArrayMap<String, String> PLATFORM_PERMISSIONS = new ArrayMap<String, String>();

    static {

        PLATFORM_PERMISSIONS.put(Manifest.permission.READ_CONTACTS, Manifest.permission_group.CONTACTS);
        PLATFORM_PERMISSIONS.put(Manifest.permission.WRITE_CONTACTS, Manifest.permission_group.CONTACTS);
        PLATFORM_PERMISSIONS.put(Manifest.permission.GET_ACCOUNTS, Manifest.permission_group.CONTACTS);

        PLATFORM_PERMISSIONS.put(Manifest.permission.READ_CALENDAR, Manifest.permission_group.CALENDAR);
        PLATFORM_PERMISSIONS.put(Manifest.permission.WRITE_CALENDAR, Manifest.permission_group.CALENDAR);

        PLATFORM_PERMISSIONS.put(Manifest.permission.SEND_SMS, Manifest.permission_group.SMS);
        PLATFORM_PERMISSIONS.put(Manifest.permission.RECEIVE_SMS, Manifest.permission_group.SMS);
        PLATFORM_PERMISSIONS.put(Manifest.permission.READ_SMS, Manifest.permission_group.SMS);
        PLATFORM_PERMISSIONS.put(Manifest.permission.RECEIVE_MMS, Manifest.permission_group.SMS);
        PLATFORM_PERMISSIONS.put(Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission_group.SMS);
        //PLATFORM_PERMISSIONS.put(Manifest.permission.READ_CELL_BROADCASTS, Manifest.permission_group.SMS);
        PLATFORM_PERMISSIONS.put("android.permission.READ_CELL_BROADCASTS", Manifest.permission_group.SMS);

        PLATFORM_PERMISSIONS.put(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE);
        PLATFORM_PERMISSIONS.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE);
//        PLATFORM_PERMISSIONS.put(Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission_group.STORAGE);

        PLATFORM_PERMISSIONS.put(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission_group.LOCATION);
        PLATFORM_PERMISSIONS.put(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission_group.LOCATION);
//        PLATFORM_PERMISSIONS.put(Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission_group.LOCATION);

        PLATFORM_PERMISSIONS.put(Manifest.permission.READ_CALL_LOG, Manifest.permission_group.CALL_LOG);
        PLATFORM_PERMISSIONS.put(Manifest.permission.WRITE_CALL_LOG, Manifest.permission_group.CALL_LOG);
        PLATFORM_PERMISSIONS.put(Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission_group.CALL_LOG);

        PLATFORM_PERMISSIONS.put(Manifest.permission.READ_PHONE_STATE, Manifest.permission_group.PHONE);
        PLATFORM_PERMISSIONS.put(Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission_group.PHONE);
        PLATFORM_PERMISSIONS.put(Manifest.permission.CALL_PHONE, Manifest.permission_group.PHONE);
        PLATFORM_PERMISSIONS.put(Manifest.permission.ADD_VOICEMAIL, Manifest.permission_group.PHONE);
        PLATFORM_PERMISSIONS.put(Manifest.permission.USE_SIP, Manifest.permission_group.PHONE);
        PLATFORM_PERMISSIONS.put(Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission_group.PHONE);
        PLATFORM_PERMISSIONS.put(Manifest.permission.ACCEPT_HANDOVER, Manifest.permission_group.PHONE);

        PLATFORM_PERMISSIONS.put(Manifest.permission.RECORD_AUDIO, Manifest.permission_group.MICROPHONE);

//        PLATFORM_PERMISSIONS.put(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission_group.ACTIVITY_RECOGNITION);

        PLATFORM_PERMISSIONS.put(Manifest.permission.CAMERA, Manifest.permission_group.CAMERA);

        PLATFORM_PERMISSIONS.put(Manifest.permission.BODY_SENSORS, Manifest.permission_group.SENSORS);
    }

    private static @Nullable
    String getGroupOfPlatformPermission(@NonNull String permission) {
        return PLATFORM_PERMISSIONS.get(permission);
    }

    private static @Nullable
    String getGroupOfPermission(@NonNull PermissionInfo permission) {
        String groupName = getGroupOfPlatformPermission(permission.name);
        if (groupName == null) {
            groupName = permission.group;
        }

        return groupName;
    }

    public static String getPermissionName(Context context, String strPermission) {
        String strName = "";
        PermissionInfo permissionInfo = null;
        PackageManager mPkm = context.getPackageManager();
        try {
            permissionInfo = mPkm.getPermissionInfo(strPermission, 0);
        } catch (PackageManager.NameNotFoundException e) {

        }
        String group = getGroupOfPermission(permissionInfo);
        PackageItemInfo groupInfo = permissionInfo;
        if (group != null) {
            try {
                groupInfo = context.getPackageManager().getPermissionGroupInfo(group, 0);
            } catch (PackageManager.NameNotFoundException e) {
                /* ignore */
            }
        }
        return strName = groupInfo.loadLabel(mPkm).toString();

    }
}
