package com.ztq.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ztq.sdk.utils.PrintUtils;

/**
 * Created by Danny 姜.
 */
public class CaptureTouchView extends View {
    private static final String TAG = "noahedu." + CaptureTouchView.class.getSimpleName();

    public CaptureTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        PrintUtils.printEvent(TAG, "dispatchTouchEvent", event);
        boolean result = super.dispatchTouchEvent(event);
        PrintUtils.printParam(TAG, "dispatchTouchEvent result is " + result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PrintUtils.printEvent(TAG, "onTouchEvent", event);
        int action = event.getAction();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(500, 300);
    }
}