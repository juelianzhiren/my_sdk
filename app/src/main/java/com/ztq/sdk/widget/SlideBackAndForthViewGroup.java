package com.ztq.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by ztq on 2019/9/12.
 * 可来回滑动的viewgroup，但有个限制，在布局中，宽高必须为全屏
 */
public class SlideBackAndForthViewGroup extends RelativeLayout {
    private final String TAG = "noahedu.SlideBackAndForthView";
    private Context mContext;
    private Scroller mScroller;
    private int mTouchSlop;
    private int mCurrentX;
    private int mCurrentY;
    private int mMovingX;
    private int mMovingY;

    public SlideBackAndForthViewGroup(Context context) {
        this(context, null);
    }

    public SlideBackAndForthViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBackAndForthViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mScroller = new Scroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mCurrentX = (int)ev.getX();
                mCurrentY = (int)ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mMovingX = (int)ev.getX();
                mMovingY = (int)ev.getY();
                if (Math.sqrt(Math.pow(mMovingX - mCurrentX, 2) + Math.pow(mMovingY - mCurrentY, 2)) > 10) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentX = (int)event.getX();
                mCurrentY = (int)event.getY();
                Log.v(TAG, "mCurrentX = " + mCurrentX + "; mCurrentY = " + mCurrentY);
                break;
            case MotionEvent.ACTION_MOVE:
                mMovingX = (int)event.getX();
                mMovingY = (int)event.getY();
                Log.v(TAG, "mMovingX = " + mMovingX + "; mMovingY = " + mMovingY);
                awakenScrollBars();
                mScroller.startScroll(mCurrentX, mCurrentY, (mMovingX - mCurrentX), (mMovingY - mCurrentY));
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        boolean flag = mScroller.computeScrollOffset();
        Log.v(TAG, "computeScrollOffset = " + flag);
//        super.computeScroll();
    }
}