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
 * 可来回滑动的viewgroup，切放开手后会自动滑回到初始处，但有个限制，在布局中，宽高必须为全屏
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
    private int mActivePointerId = -1;
    private int mLeftX;
    private int mDiffX;
    private boolean mIsScrolling;

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

        Log.v(TAG, "mTouchSlop = " + mTouchSlop);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.v(TAG, "onInterceptTouchEvent, action = " + ev.getAction());
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
                if (Math.sqrt(Math.pow(mMovingX - mCurrentX, 2) + Math.pow(mMovingY - mCurrentY, 2)) >= mTouchSlop) {
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
                Log.v(TAG, "action_down");
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mActivePointerId = event.getPointerId(0);
                int pointerIndex = event.findPointerIndex(mActivePointerId);
                float x = 0;
                float y = 0;
                try {
                    mCurrentX = (int)event.getX(pointerIndex);
                    mCurrentY = (int)event.getY(pointerIndex);
                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                mIsScrolling = false;
                Log.v(TAG, "mCurrentX = " + mCurrentX + "; mCurrentY = " + mCurrentY);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.v(TAG, "action_move");
                pointerIndex = event.findPointerIndex(mActivePointerId);
                x = 0;
                y = 0;
                try {
                    x = event.getX(pointerIndex);
                    y = event.getY(pointerIndex);
                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                mDiffX = (int) Math.abs(x - mCurrentX);
                boolean xMoved = mDiffX > mTouchSlop;
                if (xMoved) {
                    mIsScrolling = true;
                    try {
                        mMovingX = (int) event.getX(pointerIndex);
                        mMovingY = (int) event.getY(pointerIndex);
                        Log.v(TAG, "mMovingX = " + mMovingX + "; mMovingY = " + mMovingY + "; pointerIndex = " + pointerIndex);
                        invalidate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsScrolling = false;
                pointerIndex = event.findPointerIndex(mActivePointerId);
                try {
                    mMovingX = (int) event.getX(pointerIndex);
                    mMovingY = (int) event.getY(pointerIndex);
                    mLeftX += mMovingX - mCurrentX;
                    Log.v(TAG, "action_up, mMovingX = " + mMovingX + "; mMovingY = " + mMovingY + "; pointerIndex = " + pointerIndex + "; mLeftX = " + mLeftX);
                    int durationTime = Math.abs(mLeftX);
                    if (durationTime == 0) {
                        durationTime = 1000;
                    }
                    mScroller.startScroll(-mLeftX, 0, mLeftX, 0, durationTime);
                    invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        Log.v(TAG, "computeScrollOffset = " + mScroller.computeScrollOffset() + "; mIsScrolling = " + mIsScrolling);
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            mMovingX = mScroller.getCurrX();
            mCurrentX = mMovingX;
            mLeftX = mCurrentX;
            postInvalidate();
        } else {
            if (mIsScrolling) {
                scrollTo(-(mMovingX - mCurrentX + mLeftX), 0);
                Log.v(TAG, "mMovingX = " + mMovingX + "; scrollTo = " + (-(mMovingX - mCurrentX + mLeftX)) + "; mCurrentX = " + mCurrentX + "; mLeftX = " + mLeftX + "; mDiffX = " + mDiffX + "; diff = " + (mMovingX - mCurrentX));
            }
        }
    }
}