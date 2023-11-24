package com.demo.audiovideorelated.activity;

import android.Manifest;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import com.demo.audiovideorelated.R;
import com.noahedu.noah_permissions.OnPermissionCallback;
import com.noahedu.noah_permissions.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextureViewCameraActivity extends BaseActivity implements TextureView.SurfaceTextureListener {
    private static final String TAG = "noahedu.SurfaceViewCameraActivity";

    TextureView textureView;
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textureview_camera);
        textureView = (TextureView) findViewById(R.id.textureview_camera_tv);
        textureView.setSurfaceTextureListener(this);// 打开摄像头并将展示方向旋转90度

        checkPermissions();
    }

    //------ Texture 预览 -------
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.v(TAG, "onSurfaceTextureAvailable");
        try {
            camera.setPreviewTexture(surfaceTexture);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.v(TAG, "onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        Log.v(TAG, "onSurfaceTextureDestroyed");
        camera.release();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

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
                    openCameraAndSetParameters();
                    textureView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNoPermissions() {

                }
            });
        } else {
            openCameraAndSetParameters();
            textureView.setVisibility(View.VISIBLE);
        }
    }

    private void openCameraAndSetParameters() {
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);
        camera.setParameters(parameters);
        camera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] bytes, Camera camera) {
                Log.v(TAG, "onPreviewFrame");
            }
        });
    }
}