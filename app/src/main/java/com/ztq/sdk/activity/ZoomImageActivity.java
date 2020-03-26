package com.ztq.sdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.ztq.sdk.R;
import com.ztq.sdk.log.Log;

/**
 * Created by ztq on 2019/10/25.
 */
public class ZoomImageActivity extends BaseActivity {
    private final String TAG = "noahedu." + getClass().getSimpleName();
    private Context mContext;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        mContext = this;

        findViewById(R.id.zoom_image_view_rl).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v(TAG, "onTouch");
                return true;
            }
        });
    }
}