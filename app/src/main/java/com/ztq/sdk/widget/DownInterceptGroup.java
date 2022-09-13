package com.ztq.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.ztq.sdk.log.Log;
import com.ztq.sdk.utils.PrintUtils;

/**
 * Created by Danny 姜.
 */
public class DownInterceptGroup extends FrameLayout {
    private static final String TAG = "noahedu." + DownInterceptGroup.class.getSimpleName();

    public DownInterceptGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        PrintUtils.printEvent(TAG, "dispatchTouchEvent", event);
        boolean result =  super.dispatchTouchEvent(event);
        PrintUtils.printParam(TAG, "dispatchTouchEvent result is " + result);
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        PrintUtils.printEvent(TAG, "onInterceptTouchEvent", ev);
        boolean result =  super.onInterceptTouchEvent(ev);
        PrintUtils.printParam(TAG, "onInterceptTouchEvent result is " + result);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PrintUtils.printEvent(TAG, "onTouchEvent", event);
        return super.onTouchEvent(event);
    }
}