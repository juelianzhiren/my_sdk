package com.ztq.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import com.ztq.sdk.log.Log;

public class CustomListView extends ListView {
    private static final String TAG = "noahedu.CustomListView";

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //为listview/Y，设置初始值,默认为0.0(ListView条目一位置)
    private float mLastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        Log.v(TAG, "dispatchTouchEvent, action = " + (ev.getAction() == MotionEvent.ACTION_DOWN ? "action_down" : (ev.getAction() == MotionEvent.ACTION_MOVE ? "action_move" : "action_up")) + "; getParent = " + getParent().getParent());
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //不允许上层的ScrollView拦截事件.
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                //满足listView滑动到顶部，如果继续下滑，那就允许scrollView拦截事件
                Log.v(TAG, "getFirstVisiblePosition() = " + getFirstVisiblePosition() + "; getLastVisiblePosition() = " + getLastVisiblePosition() + "; getCount = " + getCount() + "; ev.getY() = " + ev.getY() + "; mLastY = " + mLastY);
                if (getFirstVisiblePosition() == 0 && (ev.getY() - mLastY) > 0) {
                    //允许ScrollView拦截事件
                    Log.v(TAG, "move requestDisallowInterceptTouchEvent first");
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                //满足listView滑动到底部，如果继续上滑，允许scrollView拦截事件
                else if (getLastVisiblePosition() == getCount() - 1 && (ev.getY() - mLastY) < 0) {
                    //允许ScrollView拦截事件
                    Log.v(TAG, "move requestDisallowInterceptTouchEvent last");
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    //其它情形时不允ScrollView拦截事件
                    getParent().getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        mLastY = ev.getY();
        return super.dispatchTouchEvent(ev);
    }
}