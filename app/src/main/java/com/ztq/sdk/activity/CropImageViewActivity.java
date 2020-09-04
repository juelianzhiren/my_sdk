package com.ztq.sdk.activity;

import android.content.Context;
import android.os.Bundle;

import com.ztq.sdk.R;
import com.ztq.sdk.widget.CropImageView;

public class CropImageViewActivity extends BaseActivity {
    private static final String TAG = "noahedu.CropImageViewActivity";
    private Context mContext;
    private CropImageView mCropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        mCropImageView = (CropImageView) findViewById(R.id.crop_image_view);
    }
}