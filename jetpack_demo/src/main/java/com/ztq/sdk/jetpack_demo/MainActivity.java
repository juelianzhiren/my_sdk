package com.ztq.sdk.jetpack_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "noahedu.MainActivity";
    private MyLocationListener myLocationListener;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        myLocationListener = new MyLocationListener(this, new MyLocationListener.OnLocationChangedListener() {
            @Override
            public void onChanged(long longitude, long latitude) {

            }
        });

        getLifecycle().addObserver(myLocationListener);

        findViewById(R.id.start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyService.class);
                startService(intent);
            }
        });

        findViewById(R.id.stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyService.class);
                stopService(intent);
            }
        });
    }
}