package com.ztq.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.ztq.sdk.log.Log;

public class CustomScrollView2 extends ScrollView {
    private static final String TAG = "noahedu.CustomScrollView2";

    public CustomScrollView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.v(TAG, "onInterceptTouchEvent, action = " + (ev.getAction() == MotionEvent.ACTION_DOWN ? "action_down" : (ev.getAction() == MotionEvent.ACTION_MOVE ? "action_move" : "action_up")));
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return false;
        } else {
            return true;
        }
    }
}