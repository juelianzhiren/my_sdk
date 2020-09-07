package com.ztq.sdk.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ztq.sdk.R;
import com.ztq.sdk.widget.CropImageView;

public class CropImageViewActivity extends BaseActivity {
    private static final String TAG = "noahedu.CropImageViewActivity";
    private Context mContext;
    private CropImageView mCropImageView;
    private boolean mIsOverlay = true;
    private ImageView mCropResultIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        mCropImageView = (CropImageView) findViewById(R.id.crop_image_view);
        mCropResultIv = (ImageView) findViewById(R.id.crop_image_result_iv);
        findViewById(R.id.crop_image_switch_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsOverlay = !mIsOverlay;
                mCropImageView.setContainTranslucentBackground(mIsOverlay);
            }
        });
        findViewById(R.id.crop_image_get_crop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = mCropImageView.getCropImage();
                if (bitmap != null) {
                    mCropResultIv.setImageBitmap(bitmap);
                }
            }
        });
    }
}