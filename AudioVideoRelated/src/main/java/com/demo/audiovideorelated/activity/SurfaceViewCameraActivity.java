package com.demo.audiovideorelated.activity;

import android.Manifest;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.demo.audiovideorelated.R;
import com.noahedu.noah_permissions.OnPermissionCallback;
import com.noahedu.noah_permissions.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SurfaceViewCameraActivity extends BaseActivity implements SurfaceHolder.Callback {
    private static final String TAG = "noahedu.SurfaceViewCameraActivity";

    SurfaceView surfaceView;
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surfaceview_camera);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceview_camera_sv);
        surfaceView.getHolder().addCallback(this);

        checkPermissions();
    }

    //------ Surface 预览 -------
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.v(TAG, "surfaceCreated");
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
        Log.v(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.v(TAG, "surfaceDestroyed");
        camera.release();
    }

    private void checkPermissions() {
        // Marshmallow开始才用申请运行时权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Permission.request(this, new OnPermissionCallback() {
                @Override
                public List<String> permissions() {
                    List<String> pers = new ArrayList<>();
                    pers.add(Manifest.permission.CAMERA);
                    pers.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    return pers;
                }

                @Override
                public void permissionsDenied() {
                    finish();
                }

                @Override
                public void allPermissionsGranted() {
                    camera = Camera.open();
                    camera.setDisplayOrientation(90);
                    surfaceView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNoPermissions() {

                }
            });
        } else {
            camera = Camera.open();
            camera.setDisplayOrientation(90);
            surfaceView.setVisibility(View.VISIBLE);
        }
    }
}