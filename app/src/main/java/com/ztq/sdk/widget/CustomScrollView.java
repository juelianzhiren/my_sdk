package com.ztq.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import com.ztq.sdk.log.Log;

public class CustomScrollView extends ScrollView {
    private static final String TAG = "noahedu.CustomScrollView";
    ListView listView;
    private float mLastY;

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.v(TAG, "onInterceptTouchEvent, action = " + (ev.getAction() == MotionEvent.ACTION_DOWN ? "action_down" : (ev.getAction() == MotionEvent.ACTION_MOVE ? "action_move" : "action_up")));
        super.onInterceptTouchEvent(ev);
        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                listView = (ListView) ((ViewGroup) getChildAt(0)).getChildAt(1);
                //ListView滑动到顶部，且继续下滑，让scrollView拦截事件
                if (listView.getFirstVisiblePosition() == 0 && (ev.getY() - mLastY) > 0) {
                    //scrollView拦截事件
                    intercept = true;
                }
                //listView滑动到底部，如果继续上滑，就让scrollView拦截事件
                else if (listView.getLastVisiblePosition() == listView.getCount() - 1 && (ev.getY() - mLastY) < 0) {
                    //scrollView拦截事件
                    intercept = true;
                } else {
                    //不允许scrollView拦截事件
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
            default:
                break;
        }
        mLastY = ev.getY();
        return intercept;
    }
}