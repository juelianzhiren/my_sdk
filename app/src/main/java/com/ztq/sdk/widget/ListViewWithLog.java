package com.ztq.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import com.ztq.sdk.log.Log;

public class ListViewWithLog extends ListView {
    private static final String TAG = "noahedu.ListViewWithLog";

    public ListViewWithLog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //为listview/Y，设置初始值,默认为0.0(ListView条目一位置)
    private float mLastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        Log.v(TAG, "dispatchTouchEvent, action = " + (ev.getAction() == MotionEvent.ACTION_DOWN ? "action_down" : (ev.getAction() == MotionEvent.ACTION_MOVE ? "action_move" : "action_up")));
        return super.dispatchTouchEvent(ev);
    }
}