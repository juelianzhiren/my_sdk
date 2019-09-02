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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
    private OnSelectableTextViewListener mOnSelectableTextViewListener;
    private List<SelectionInfo> mSelectionInfoList;

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
        mSelectionInfoList = new ArrayList<>();
    }

    public void setOnSelectableTextViewListener(OnSelectableTextViewListener mOnSelectableTextViewListener) {
        this.mOnSelectableTextViewListener = mOnSelectableTextViewListener;
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
                    if (mOnSelectableTextViewListener != null) {
                        mOnSelectableTextViewListener.startSelectWord();
                    }
                    manageSelected(offsetForTouch, offsetForTouch);
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
                    manageSelected(offsetForTouch, getValidTextNumber(offsetForTouch, offsetForMoving));
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return true;
    }

    private int getValidTextNumber(int startOffset, int endOffset) {
        int validTextCount = 0;
        int i = 0;
        if (startOffset > endOffset) {
            for(i = startOffset; i >= endOffset; i--) {
                if (isValidText(i)) {
                    validTextCount++;
                }
                if (validTextCount > MAX_WORDS_SIZE) {
                    return i;
                }
            }
        } else {
            for(i = startOffset; i <= endOffset; i++) {
                if (isValidText(i)) {
                    validTextCount++;
                }
                if (validTextCount > MAX_WORDS_SIZE) {
                    return i;
                }
            }
        }
        return i + 1;
    }

    private boolean isValidText(int index) {
        boolean flag = true;
        char var2 = this.getTextCharByIndex(index);
        if (CharacterUtils.isChinesePunctuation(var2) || !CharacterUtils.isChineseByBlock(var2)) {
            flag = false;
        }
        return flag;
    }

    private char getTextCharByIndex(int index) {
        char[] chars = this.getText().toString().toCharArray();
        return index >= chars.length ? ' ' : chars[index];
    }

    private void manageSelected(int offsetStart, int offsetEnd) {
        ArrayList<Integer> list = this.createListByRange(getSelectedRanges(offsetStart, offsetEnd));
        ArrayList tempList = new ArrayList();
        for(int i = 0; i < mSelectionInfoList.size(); i++) {
            SelectionInfo selectionInfo = mSelectionInfoList.get(i);
            ArrayList<Integer> itemList = createListByRange(getSelectedRanges(selectionInfo.getStartIndex(), selectionInfo.getEndIndex()));
            tempList.clear();
            tempList.addAll(list);
            tempList.retainAll(itemList);
            if (!tempList.isEmpty()) {
                ArrayList var8 = this.removeCommonElement(itemList, tempList);
                list = this.removeCommonElement(list, tempList);
                mSelectionInfoList.add(getSelectionInfoByRangeArray(getRangeArrayByRangeList(var8)));
            } else {
                mSelectionInfoList.add(selectionInfo);
            }
        }
        if (list != null && list.size() != 0) {

        }
    }

    private ArrayList<SelectionInfo> getSelectionInfoByRangeArray(int[] arr) {
        ArrayList var4 = new ArrayList();
        int var3 = arr.length / 2;

        for (int var2 = 0; var2 < var3; ++var2) {
            var4.add(this.creatSelctionInfo(arr[var2 % 2 + var2], arr[var2 % 2 + var2 + 1]));
        }

        return var4;
    }

    private SelectionInfo creatSelctionInfo(int startIndex, int endIndex) {
        return new SelectionInfo(this, startIndex, endIndex);
    }

    private int[] getRangeArrayByRangeList(ArrayList<Integer> list) {
        Collections.sort(list);
        int[] arr = new int[2];
        arr[0] = list.get(0);
        arr[1] = list.get(list.size() - 1);
        return arr;
    }

    private ArrayList<Integer> removeCommonElement(ArrayList<Integer> list1, ArrayList<Integer> list2) {
        ArrayList list = new ArrayList();
        int size = list1.size();

        for(int i = 0; i < size; ++i) {
            if (!list2.contains(list1.get(i))) {
                list.add(list1.get(i));
            }
        }
        return list;
    }

    private ArrayList<Integer> createListByRange(int[] arr) {
        ArrayList list = new ArrayList();
        for(int i = arr[0]; i <= arr[1]; i++) {
            list.add(i);
        }
        return list;
    }

    private int[] getSelectedRanges(int start, int end) {
        int[] arr = new int[2];
        if (start < end) {
            arr[0] = start;
            arr[1] = end;
        } else {
            arr[0] = end;
            arr[1] = start;
        }
        return arr;
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

        public int getStartIndex() {
            return mStartIndex;
        }

        public void setStartIndex(int mStartIndex) {
            this.mStartIndex = mStartIndex;
        }

        public int getEndIndex() {
            return mEndIndex;
        }

        public void setEndIndex(int mEndIndex) {
            this.mEndIndex = mEndIndex;
        }

        public String getSelectedText() {
            return this.mTextiew.getText().subSequence(this.mStartIndex, this.mEndIndex + 1).toString();
        }
    }

    public ArrayList<String> getSelectedTextList() {
        ArrayList list = new ArrayList();
        if (this.mSelectionInfoList != null) {
            Iterator iterator = this.mSelectionInfoList.iterator();

            while(iterator.hasNext()) {
                list.add(((SelectionInfo)iterator.next()).getSelectedText());
            }
        }
        return list;
    }

    public interface OnSelectableTextViewListener {
        void endSelectWord();

        void startSelectWord();
    }
}