package com.ztq.sdk.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.ImageView;

import com.ztq.sdk.R;
import com.ztq.sdk.widget.LargeImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ztq on 2019/9/20.
 * 显示大图的activity
 */
public class BigPictureActivity extends Activity {
    private LargeImageView mLargeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture);

        mLargeImageView = (LargeImageView) findViewById(R.id.big_picture_iv);
        try {
            InputStream inputStream = getAssets().open("world.jpg");
            mLargeImageView.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}