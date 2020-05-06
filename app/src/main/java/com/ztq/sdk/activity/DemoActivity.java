package com.ztq.sdk.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.ztq.sdk.R;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.utils.Utils;
import com.ztq.sdk.widget.GifView;
import com.ztq.sdk.widget.ProgressButton;
import com.ztq.sdk.widget.ProgressView;
import com.ztq.sdk.widget.RoundImageView;
import com.ztq.sdk.widget.SelectableTextView;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.external.ExternalAdaptInfo;

/**
 * Created by ztq on 2019/7/29.
 */
public class DemoActivity extends BaseActivity {
    private final String TAG = "noahedu.DemoActivity";
    private SelectableTextView mSelectableTv;
    private GifView mGifView;
    private ImageView mLayerDrawableIv;
    private ProgressView mDownloadView;
    private ProgressButton mDownloadBtn;
    private int mProgress;
    private Context mContext;
    private Button mBtn;
    private ConstraintLayout mConstraintLayout;
    private boolean mIsBold;
    private ImageView mImageView;
    private RoundImageView mRoundImageView;

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
        mConstraintLayout = findViewById(R.id.constatinlayout);
        mBtn = findViewById(R.id.demo_btn);
        final Button btn = new Button(mContext);
        btn.setText("动画1");
        mConstraintLayout.addView(btn);
        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "download btn click");
                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.property_animator);
                set.setTarget(btn);
                btn.setVisibility(View.VISIBLE);
                set.start();
                performAnimate();
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        Log.v(TAG, "onAnimationStart");
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Log.v(TAG, "onAnimationEnd");
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        Log.v(TAG, "onAnimationCancel");
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        Log.v(TAG, "onAnimationRepeat");
                    }
                });
            }
        });
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsBold = !mIsBold;
                mBtn.getPaint().setFakeBoldText(mIsBold);
                if (mIsBold) {
                    mBtn.setTextColor(mContext.getResources().getColor(R.color.light_green));
                } else {
                    mBtn.setTextColor(mContext.getResources().getColor(R.color.black));
                }
            }
        });
        mImageView = findViewById(R.id.demo_image_view);
        mImageView.setImageBitmap(Utils.createCircleImage(Utils.getBitmapFromDrawableRes(this, R.drawable.pic_6)));

        mRoundImageView = findViewById(R.id.demo_round_image_view);
        mRoundImageView.setImageResource(R.drawable.shape_gray);
        mRoundImageView.setBorderRadius(Utils.dp2px(mContext, 20));
        int statusHeight = Utils.getStatusHeight(mContext);
        Log.v(TAG, "statusBarHeight = " + statusHeight);
        statusHeight = Utils.getStatusBarHeight(mContext);
        Log.v(TAG, "statusBarHeight = " + statusHeight);
        int navigationBarHeight = Utils.getNavigationBarHeight(mContext);
        Log.v(TAG, "navigationBarHeight = " + navigationBarHeight);

        int sizeInDp = 1024;
        ExternalAdaptInfo externalAdaptInfo = new ExternalAdaptInfo(true, sizeInDp);
        AutoSizeConfig.getInstance().getExternalAdaptManager().addExternalAdaptInfoOfActivity(SupportRequestManagerFragment.class, externalAdaptInfo);
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

    private void performAnimate() {
        ViewWrapper wrapper = new ViewWrapper(mBtn);
        ObjectAnimator.ofInt(wrapper, "width", 500).setDuration(5000).start();
    }

    private static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View target) {
            mTarget = target;
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }
}