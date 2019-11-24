package com.ztq.sdk.activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.ztq.sdk.R;
import com.ztq.sdk.widget.ProgressButton;
import com.ztq.sdk.widget.ProgressView;
import com.ztq.sdk.widget.GifView;
import com.ztq.sdk.widget.SelectableTextView;

/**
 * Created by ztq on 2019/7/29.
 */
public class DemoActivity extends Activity {
    private final String TAG = "noahedu.DemoActivity";
    private SelectableTextView mSelectableTv;
    private GifView mGifView;
    private ImageView mLayerDrawableIv;
    private ProgressView mDownloadView;
    private ProgressButton mDownloadBtn;
    private int mProgress;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mContext = this;
        mSelectableTv = findViewById(R.id.selectable_tv);
        mSelectableTv.setText(
                "我我饿哦哦！我哦哦我饿我哦房东。偶发的搜房度搜，啊欧迪芬。辅导老师都爱发，拉风的搜阿斯顿发了的撒讲道理两间房的酸辣粉领导领导拉收到了sad" +
                        "领导看两三点加适量的健康李开复达萨罗联发科大厦");

        mGifView = findViewById(R.id.gifview);
//        mGifView.setmIsGifImage(true);

        mLayerDrawableIv = findViewById(R.id.demo_iv);
        ((SeekBar) findViewById(R.id.seekbar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = seekBar.getMax();
                double scale = (double) progress / (double) max;
                ClipDrawable drawable = (ClipDrawable) mLayerDrawableIv.getDrawable();
                drawable.setLevel((int) (10000 * scale));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mDownloadView = findViewById(R.id.demo_download_view);
        mDownloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "download click");
            }
        });
        mDownloadBtn = findViewById(R.id.demo_download_btn);
        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "download btn click");
                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.property_animator);
                set.setTarget(mDownloadBtn);
                set.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgress++;
        if (mProgress > 100) {
            mProgress = 100;
        }
        mDownloadView.setProgressAndText(mProgress / 100.0f, mProgress + "%");
        Log.v(TAG, "mProgress = " + mProgress);

        mDownloadBtn.setText(mProgress + "%");
        mDownloadBtn.setProgress(mProgress / 100.0f);
    }

    @Override
    public void onBackPressed() {
        mSelectableTv.getSelectedTextList();
        super.onBackPressed();
    }
}