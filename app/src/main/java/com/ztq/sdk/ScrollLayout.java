package com.ztq.sdk;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.ztq.sdk.log.Log;

/**
 * Created by ztq on 2019/9/17.
 */
public class ScrollLayout extends LinearLayout {
    private final String TAG = "noahedu.ScrollLayout";
    private Scroller mScroller;

    public ScrollLayout(Context context) {
        this(context, null);
    }

    public ScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
    }
    /**
     * 缓慢滑动的方法
     *
     * @param destX 水平滑动的距离
     * @param destY 竖起滑动的距离
     */
    public void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;

        mScroller.startScroll(scrollX, 0, delta, 0, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            Log.v(TAG, "curX = " + mScroller.getCurrX() + "; curY  = " + mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.v(TAG, "onDraw");
    }
}