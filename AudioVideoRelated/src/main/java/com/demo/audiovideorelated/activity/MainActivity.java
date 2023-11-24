package com.demo.audiovideorelated.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.audiovideorelated.R;

public class MainActivity extends BaseActivity {
    private static final String TAG = "noahedu.MainActivity";
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        findViewById(R.id.audio_record_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AudioRecordActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.surfaceview_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SurfaceViewCameraActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.textureview_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TextureViewCameraActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.media_extractor_muxer_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MediaExtractorMuxerActivity.class);
                startActivity(intent);
            }
        });
    }
}