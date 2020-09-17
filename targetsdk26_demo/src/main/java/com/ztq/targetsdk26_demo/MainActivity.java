package com.ztq.targetsdk26_demo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ztq.targetsdk26_demo.utils.PermissionUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 本工程处理的是针对 targetSDK>=23 的权限处理
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "noahedu.MainActivity";
    private static final String mCfgFileName = "my_sdk.cfg";
    private static final String KEY_IS_SHOW_LOG = "is_show_log";
    private boolean mIsShowLog;
    private Context mContext;
    private static final int PERMISSSION_CODE_READ_EXTERNAL_STORAGE = 5;
    private static final int SETTINGS_CODE = 1;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate, savedInstanceState = " + savedInstanceState);
        mContext = this;
        if (PermissionUtil.checkPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Log.v(TAG, "checkPermission = true");
            //读取sdcard文件
            readConfigAndSetting();
        } else {
            Log.v(TAG, "checkPermission = false");
            PermissionUtil.requestPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSSION_CODE_READ_EXTERNAL_STORAGE);
        }
    }

    /**
     * 读取配置文件，以及设置
     */
    private void readConfigAndSetting() {
        Properties props = new Properties();

        String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(new File(externalPath, mCfgFileName)));
            props.load(in);

            String isShowLogStr = props.getProperty(KEY_IS_SHOW_LOG);
            mIsShowLog = Boolean.valueOf(isShowLogStr);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSSION_CODE_READ_EXTERNAL_STORAGE) {
            for (int i = 0; i < permissions.length; i++) {
                final String permission = permissions[i];
                int grantResult = grantResults[i];
                Log.v(TAG, "i = " + i + "; permission = " + permission + "; grantResult = " + grantResult);
                if (grantResult == PackageManager.PERMISSION_GRANTED) {       //选择了“始终允许”
                    Toast.makeText(this, "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                    //读取sdcard文件
                    readConfigAndSetting();
                } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){   //用户选择了禁止不再询问
                    Log.v(TAG, "用户选择了禁止不再询问");
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("permission")
                            .setMessage("当前应用缺少【读取文件】权限，请点击“权限”-打开所需权限")
                            .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (alertDialog != null && alertDialog.isShowing()) {
                                        alertDialog.dismiss();
                                    }
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
                                    intent.setData(uri);
                                    startActivityForResult(intent, SETTINGS_CODE);
                                }
                            });
                    alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                } else {                       //选择禁止
                    Log.v(TAG, "选择禁止");
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("permission")
                            .setMessage("点击允许才可以使用我们的app哦")
                            .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (alertDialog != null && alertDialog.isShowing()) {
                                        alertDialog.dismiss();
                                    }
                                    PermissionUtil.requestPermission(mContext, permission, requestCode);
                                }
                            });
                    alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "requestCode = " + requestCode + "; resultCode = " + resultCode);
        if (requestCode == SETTINGS_CODE) {
            if (PermissionUtil.checkPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.v(TAG, "checkPermission = true");
                //读取sdcard文件
                readConfigAndSetting();
            } else {
                Log.v(TAG, "checkPermission = false");
                PermissionUtil.requestPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSSION_CODE_READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }
}