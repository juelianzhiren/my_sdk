package com.ztq.sdk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ztq.sdk.R;

public class SelectableTextView extends AppCompatTextView {
    private final String TAG = "noahedu.SelectableTextView";
    private Context mContext;
    private final int MAX_WORDS_SIZE = 8;    // 最多选择8个文字
    private Drawable mNinePatchDrawable;
    private int mTouchX;
    private int mTouchY;
    private int mSelectedLine;
    private boolean mIsTouchValid;
    private static final char[] INVALID_CHARS = new char[]{'\n', '\t', '\u3000'};
    private boolean mIsSliding;
    private int mMoveTouchX;
    private int mMoveTouchY;

    public SelectableTextView(Context context) {
        this(context, null);
    }

    public SelectableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mNinePatchDrawable = (NinePatchDrawable) getContext().getDrawable(R.drawable.ic_smear_part);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int offsetForTouch = 0;
        int offsetForMoving = 0;
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.v(TAG, "MotionEvent.ACTION_DOWN");
                mTouchX = (int)event.getX();
                mTouchY = (int)event.getY();
                offsetForTouch = getPreciseOffset(mTouchX, mTouchY);
                mIsTouchValid = checkIsValid(offsetForTouch);
                mSelectedLine = getLayout().getLineForOffset(offsetForTouch);
                Log.v(TAG, "mSelectedLine = " + mSelectedLine);
                this.mIsSliding = true;
                if (mIsTouchValid) {

                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.v(TAG, "MotionEvent.ACTION_MOVE");
                if (mIsTouchValid && mIsSliding) {
                    mMoveTouchX = (int)event.getX();
                    mMoveTouchY = (int)event.getY();
                    offsetForTouch = getPreciseOffset(mTouchX, mTouchY);
                    offsetForMoving = getPreciseOffset(mMoveTouchX, mMoveTouchY);
                    int line = getLayout().getLineForOffset(offsetForMoving);
                    if (this.mSelectedLine != line) {   // 如果是隔行的话， 就停止
                        this.mIsSliding = false;
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return true;
    }

    /**
     * 判断是否
     * @param offset 偏移量
     * @return
     */
    private boolean checkIsValid(int offset) {
        boolean flag = false;
        char[] chars = getText().toString().toCharArray();
        if (chars.length <= offset) {
            return false;
        } else {
            if (this.isValidByChar(chars[offset])) {
                flag = true;
            }
            return flag;
        }
    }

    private boolean isValidByChar(char character) {
        boolean flag = true;
        for(int i = 0; i < INVALID_CHARS.length; i++) {
            if (character == INVALID_CHARS[i]) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public int getPreciseOffset(int touchX, int touchY) {
        Layout layout = getLayout();
        int offset = 0;
        if (layout != null) {
            offset = layout.getOffsetForHorizontal(layout.getLineForVertical(touchY), (float)touchX);
            Log.v(TAG, "lineForVertical = " + layout.getLineForVertical(touchY) + "; getOffsetForHorizontal = " + layout.getOffsetForHorizontal(layout.getLineForVertical(touchY), (float)touchX) + "; layout.getPrimaryHorizontal(offset) = " + layout.getPrimaryHorizontal(offset) + "; touchX = " + touchX + "; layout.getOffsetToLeftOf(offset) = " + layout.getOffsetToLeftOf(offset));
            if ((int)layout.getPrimaryHorizontal(offset) > touchX) {
                return layout.getOffsetToLeftOf(offset);
            }
        }
        return offset;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public static class SelectionInfo {
        private int mStartIndex;
        private int mEndIndex;
        private TextView mTextiew;

        public SelectionInfo(TextView textView, int startIndex, int endIndex) {
            this.set(textView, startIndex, endIndex);
        }

        private void set(TextView textView, int startIndex, int endIndex) {
            this.mTextiew = textView;
            this.mStartIndex = startIndex;
            this.mEndIndex = endIndex;
        }

        public int getmStartIndex() {
            return mStartIndex;
        }

        public void setmStartIndex(int mStartIndex) {
            this.mStartIndex = mStartIndex;
        }

        public int getmEndIndex() {
            return mEndIndex;
        }

        public void setmEndIndex(int mEndIndex) {
            this.mEndIndex = mEndIndex;
        }

        public String getSelectedText() {
            return this.mTextiew.getText().subSequence(this.mStartIndex, this.mEndIndex + 1).toString();
        }
    }
}