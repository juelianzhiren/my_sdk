package com.demo.audiovideorelated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

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
    }
}