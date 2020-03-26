package com.ztq.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ztq.sdk.log.Log;

/**
 * Created by ztq on 2019/7/25.
 */
public class MyImageView extends ImageView {
    private final String TAG = "noahedu.MyImageView";

    public MyImageView(Context context) {
        super(context);
        Log.v(TAG, "MyImageView (context)");
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.v(TAG, "MyImageView (context, attrs)");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.v(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.v(TAG, "onDetachedFromWindow");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.v(TAG, "onFinishInflate");
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        Log.v(TAG, "onWindowVisibilityChanged, visibility = " + visibility);
    }
}