package com.ztq.sdk.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.ztq.sdk.R;

/**
 * Created by ztq on 2019/10/25.
 */
public class ZoomImageActivity extends Activity {
    private final String TAG = "noahedu." + getClass().getSimpleName();
    private Context mContext;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        mContext = this;
    }
}