package com.ztq.sdk.activity;

import android.os.Bundle;

import com.ztq.sdk.R;
import com.ztq.sdk.widget.LargeImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ztq on 2019/9/20.
 * 显示大图的activity
 */
public class BigPictureActivity extends BaseActivity {
    private LargeImageView mLargeImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture);

        mLargeImageView = (LargeImageView) findViewById(R.id.big_picture_iv);
        try {
            InputStream inputStream = getAssets().open("qingming.jpg");
            mLargeImageView.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        mLargeImageView.clear();
        super.onDestroy();
    }
}