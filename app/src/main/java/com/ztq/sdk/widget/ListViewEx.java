package com.ztq.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import com.ztq.sdk.log.Log;

/**
 * 内部拦截事件
 */
public class ListViewEx extends ListView {
    private static final String TAG = "noahedu.ListViewEx";

    private int lastXIntercepted, lastYIntercepted;
    private HorizontalEx2 mHorizontalEx2;

    public ListViewEx(Context context) {
        super(context);
    }

    public ListViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HorizontalEx2 getmHorizontalEx2() {
        return mHorizontalEx2;
    }

    public void setmHorizontalEx2(HorizontalEx2 mHorizontalEx2) {
        this.mHorizontalEx2 = mHorizontalEx2;
    }

    /**
     * 使用 outter.requestDisallowInterceptTouchEvent();
     * 来决定父控件是否对事件进行拦截
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.v(TAG, "dispatchTouchEvent, action = " + (ev.getAction() == MotionEvent.ACTION_DOWN ? "action_down" : (ev.getAction() == MotionEvent.ACTION_MOVE ? "action_move" : "action_up")));
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                final int deltaX = x - lastXIntercepted;
                final int deltaY = y - lastYIntercepted;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        lastXIntercepted = x;
        lastYIntercepted = y;
        return super.dispatchTouchEvent(ev);
    }
}