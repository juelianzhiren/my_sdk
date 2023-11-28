package com.demo.audiovideorelated.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.demo.audiovideorelated.R;
import com.demo.audiovideorelated.util.H264Encoder;
import com.noahedu.noah_permissions.OnPermissionCallback;
import com.noahedu.noah_permissions.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 音视频采集页面
 */
public class AudioVideoCollectionActivity extends BaseActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final String TAG = "noahedu.AudioVideoCollectionActivity";
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Button muxerButton;

    int width = 1280;
    int height = 720;
    int framerate = 30;
    H264Encoder encoder;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_video_collect);

        mContext = this;
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        muxerButton = (Button) findViewById(R.id.go_muxer);
        muxerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MediaMuxerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (supportH264Codec()) {
            Log.e(TAG, "support H264 hard codec");
        } else {
            Log.e(TAG, "not support H264 hard codec");
        }
        Permission.request(this, new OnPermissionCallback() {
            @Override
            public List<String> permissions() {
                List<String> pers = new ArrayList<>();
                pers.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                pers.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                pers.add(Manifest.permission.CAMERA);
                pers.add(Manifest.permission.RECORD_AUDIO);
                return pers;
            }

            @Override
            public void permissionsDenied() {
                Toast.makeText(mContext, "您拒绝了权限哦~", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void allPermissionsGranted() {
                surfaceView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNoPermissions() {

            }
        });
    }

    private boolean supportH264Codec() {
        // 遍历支持的编码格式信息
        if (Build.VERSION.SDK_INT >= 18) {
            for (int j = MediaCodecList.getCodecCount() - 1; j >= 0; j--) {
                MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(j);

                String[] types = codecInfo.getSupportedTypes();
                for (int i = 0; i < types.length; i++) {
                    Log.v(TAG, "supportedType " + i + ": " + types[i]);
                    if (types[i].equalsIgnoreCase("video/avc")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.w(TAG, "enter surfaceCreated method");
        // 目前设定的是，当surface创建后，就打开摄像头开始预览
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);
        parameters.setPreviewSize(1280, 720);

        try {
            camera.setParameters(parameters);
            camera.setPreviewDisplay(surfaceHolder);
            camera.setPreviewCallback(this);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

        encoder = new H264Encoder(width, height, framerate);
        encoder.startEncoder();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.w(TAG, "enter surfaceChanged method");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.w(TAG, "enter surfaceDestroyed method");

        // 停止预览并释放资源
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera = null;
        }

        if (encoder != null) {
            encoder.stopEncoder();
        }
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        if (encoder != null) {
            encoder.putData(bytes);
        }
    }
}