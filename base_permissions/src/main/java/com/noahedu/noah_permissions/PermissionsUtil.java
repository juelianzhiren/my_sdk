package com.noahedu.noah_permissions;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class PermissionsUtil {


    /**
     * 查看是否有权限 context必须是activity
     *
     * @param permissions 如(Manifest.permission.CAMERA)
     */
    public static boolean hasPermissions(Context context, String... permissions) {

        if (permissions == null || permissions.length == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context != null) {
                for (String permission : permissions) {
                    if (context.checkSelfPermission(permission)
                            != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 获取权限名称
     *
     * @param strPermission 权限的英文字符串
     * @return
     */
    public static String getPermissionName(Context context, String strPermission) {
        String strName = "";
        PermissionInfo permissionInfo = null;
        try {
            permissionInfo = context.getPackageManager().getPermissionInfo(strPermission, 0);
//            Log.v(TAG, "permissionInfo : "+permissionInfo.toString());
        } catch (PackageManager.NameNotFoundException e) {
        }

        PackageItemInfo groupInfo = permissionInfo;
        if (permissionInfo.group != null) {
            try {
                groupInfo = context.getPackageManager().getPermissionGroupInfo(
                        permissionInfo.group, 0);
//                Log.v(TAG, "groupInfo : "+groupInfo.toString());
            } catch (PackageManager.NameNotFoundException e) {
                /* ignore */
            }
        }

        strName = groupInfo.loadLabel(context.getPackageManager()).toString();
        return strName;
    }

    public static String getRationaleText(Context context, String[] permissionArray) {
        StringBuilder strNotGrantedPermisssions = new StringBuilder();
        for (String perm : permissionArray) {
            if (ContextCompat.checkSelfPermission(context, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                String strTemp = GroupOfPlatformPermission.getPermissionName(context, perm);
                if (!TextUtils.isEmpty(strTemp)) {
                    if (!strNotGrantedPermisssions.toString().contains(strTemp)) {
                        strNotGrantedPermisssions.append("\"" + strTemp + "\"").append("、");
                    }
                }
            }
        }
        String strTmpStr = strNotGrantedPermisssions.toString();
        if (!TextUtils.isEmpty(strTmpStr)) {
            strTmpStr = strTmpStr.substring(0, strTmpStr.length() - 1);
        }

        return String.format(context.getResources().getString(R.string.rationale_text), strTmpStr);
    }

    public static String getPermanentlyDeniedText(Context context, List<String> permissionList) {
        StringBuilder strNotGrantedPermisssions = new StringBuilder();
        for (String perm : permissionList) {
            if (ContextCompat.checkSelfPermission(context, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                String strTemp = GroupOfPlatformPermission.getPermissionName(context, perm);
                if (!TextUtils.isEmpty(strTemp)) {
                    if (!strNotGrantedPermisssions.toString().contains(strTemp)) {
                        strNotGrantedPermisssions.append("\"" + strTemp + "\"").append("、");
                    }
                }
            }
        }
        String strTmpStr = strNotGrantedPermisssions.toString();
        if (!TextUtils.isEmpty(strTmpStr)) {
            strTmpStr = strTmpStr.substring(0, strTmpStr.length() - 1);
        }

        return String.format(context.getResources().getString(R.string.rationale_ask_again2), strTmpStr);
    }

    /**
     * 不再询问后调用去设置对话框
     *
     * @return
     */
    public static Dialog createDialog(Context context, String strText, DialogInterface.OnClickListener listener) {
        return createDialog(context, strText, false, listener);
    }

    /**
     * 不再询问后调用去设置对话框
     *
     * @return
     */
    public static Dialog createDialog(Context context, String strText, boolean isSetting, DialogInterface.OnClickListener listener) {
        try {
            final Dialog dialog = new Dialog(context, R.style.permission_dialogstyle);
            dialog.setContentView(R.layout.permission_dialog_common_layout);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(context.getResources().getDimensionPixelSize(R.dimen.permission_dialog_width), context.getResources().getDimensionPixelSize(R.dimen.permission_dialog_height));

            TextView content = (TextView) dialog.findViewById(R.id.permission_dialog_common_content_tv);
            Button iKnowBtn = (Button) dialog.findViewById(R.id.permission_dialog_common_exit_btn);
            Button continueRequestBtn = (Button) dialog.findViewById(R.id.permission_dialog_common_net_setting_btn);
            if (isSetting) {
                continueRequestBtn.setText(R.string.permission_dialog_setting);
            }

            content.setGravity(Gravity.LEFT);
            content.setText(!TextUtils.isEmpty(strText) ? strText : context.getResources().getString(R.string.rationale_ask_again));
            iKnowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(PermissionConsts.TAG, "dialog permissionsDenied");
                    listener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    dialog.cancel();
                }
            });
            continueRequestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    dialog.cancel();
                }
            });

            return dialog;
        } catch (Exception e) {
            System.out.println("faild to show net setting");
        }
        return null;
    }

    public static void jumpToSettings(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.MANAGE_APP_PERMISSIONS");
        intent.putExtra(Intent.EXTRA_PACKAGE_NAME, context.getPackageName());
        intent.putExtra("hideInfoButton", true);
        context.startActivity(intent);
    }

    public static void jumpToSettingsForResult(android.app.Fragment fragment, int requestCode) {
        if (fragment == null || fragment.getActivity() == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.MANAGE_APP_PERMISSIONS");
        intent.putExtra(Intent.EXTRA_PACKAGE_NAME, fragment.getActivity().getPackageName());
        intent.putExtra("hideInfoButton", true);
        fragment.startActivityForResult(intent, requestCode);
    }
}
