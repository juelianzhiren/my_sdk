package com.ztq.sdk.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ztq on 2018/8/13.
 * 可以控制帧动画结束回调的imageview
 */
public class FrameAnimImageView extends ImageView {
    private final String TAG = "noahedu.FrameAnimImageView";
    private AnimationDrawable mAnim;

    public FrameAnimImageView(Context context) {
        super(context);
    }

    public FrameAnimImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 不带动画监听的播放
     * @param resId
     */
    public void loadAnimation(int resId){
        setImageResource(resId);
        if (mAnim != null && mAnim.isRunning()) {
            mAnim.stop();
        }
        mAnim = (AnimationDrawable)getDrawable();
        mAnim.start();
    }

    /**
     * 带动画监听的播放
     * @param resId
     * @param listener
     */
    public void loadAnimation(int resId, final OnFrameAnimationListener listener) {
        setImageResource(resId);
        if (mAnim != null && mAnim.isRunning()) {
            mAnim.stop();
        }
        mAnim = (AnimationDrawable)getDrawable();
        mAnim.start();
        if(listener != null){
        // 调用回调函数onStart
            listener.onStart();
        }

        // 计算动态图片所花费的事件
        int durationTime = 0;
        for (int i = 0; i < mAnim.getNumberOfFrames(); i++) {
            durationTime += mAnim.getDuration(i);
        }

        // 动画结束后
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(listener != null){
                    // 调用回调函数onEnd
                    listener.onEnd();
                }
            }
        }, durationTime);
    }

    /**
     * 停止动画
     */
    public void stopAnim() {
        if (mAnim != null && mAnim.isRunning()) {
            mAnim.stop();
        }
    }

    public interface OnFrameAnimationListener {
        void onStart();
        void onEnd();
    }
}