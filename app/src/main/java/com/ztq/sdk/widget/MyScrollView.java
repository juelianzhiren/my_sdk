package com.ztq.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.ztq.sdk.utils.PrintUtils;

/**
 * Created by Danny 姜.
 */
public class MyScrollView extends ScrollView {
    private static final String TAG = "noahedu." + MyScrollView.class.getSimpleName();

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        PrintUtils.printEvent(TAG,"onInterceptTouchEvent", event);
        boolean intercepted = super.onInterceptTouchEvent(event);
        PrintUtils.printParam(TAG, "MyScrollView onInterceptTouchEvent is " + intercepted);
        return intercepted;
    }
}