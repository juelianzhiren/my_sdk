package com.ztq.sdk.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.ztq.sdk.log.Log;

public class MyLayoutWithViewTarget extends LinearLayout {
    private static final String TAG = "noahedu.MyLayoutWithViewTarget";

    private ViewTarget<MyLayoutWithViewTarget, GlideDrawable> viewTarget;

    public MyLayoutWithViewTarget(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewTarget = new ViewTarget<MyLayoutWithViewTarget, GlideDrawable>(this) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation glideAnimation) {
                Log.v(TAG, "onResourceReady");
                MyLayoutWithViewTarget myLayout = getView();
                myLayout.setImageAsBackground(resource);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                Log.e(TAG, "onLoadFailed, e = " + e);
                super.onLoadFailed(e, errorDrawable);
            }
        };
    }

    public ViewTarget<MyLayoutWithViewTarget, GlideDrawable> getTarget() {
        return viewTarget;
    }

    public void setImageAsBackground(GlideDrawable resource) {
        setBackground(resource);
    }
}