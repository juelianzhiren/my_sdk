package com.ztq.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build.VERSION;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.noahedu.entity.SelectionInfo;
import com.noahedu.hanzidictation.R;
import com.noahedu.util.CharacterUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by ztq on 2018/8/31.
 */
public class SelectableTextView extends AppCompatTextView {
    private static final int DRAW_RADIUS = 50;
    private static final char[] INVALID_CHARS = new char[]{'\n', '\t', '\u3000'};
    private static final int MAX_SELECTION_TEXT_NUMBER = 8;
    private static final int SPACE_LEFT_RIGHT_PADDING = 0;
    private static final int SPACE_TOP_BOTTOM_PADDING = 10;
    private static final String TAG = "noahedu.SelectableTextView";
    private static final int VERIFY_LINE_NUMBER = 2;
    private boolean isSliding;
    private boolean isTouchValid;
    private int mDefaultSelectionColor = Color.parseColor("#fff600");
    private int mLineSpacing;
    private int mMaxTextLength;
    private int mMoveTouchX;
    private int mMoveTouchY;
    private OnSelectableTextViewListener mOnSelectableTextViewListener;
    private Rect mParentRect;
    private Rect mSelectedBound;
    private int mSelectedLine;
    private ArrayList<SelectionInfo> mSelectionInfoList;
    private ArrayList<SelectionInfo> mSelectionInfoListTemp;
    private long mStartSelectedTime = -1L;
    private int mTouchX;
    private int mTouchY;
    private Paint paint;
    private RectF rectF;
    private Bitmap bitmap;
    private Drawable mNinePatchDrawable;

    public SelectableTextView(Context var1) {
        super(var1);
        this.init();
    }

    public SelectableTextView(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.init();
    }

    public SelectableTextView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.init();
    }

    private boolean checkIsValid(int var1) {
        boolean var2 = false;
        char[] var3 = this.getText().toString().toCharArray();
        if (var3.length <= var1) {
            return false;
        } else {
            if (this.isValidByChar(var3[var1])) {
                var2 = true;
            }
            return var2;
        }
    }

    private SelectionInfo creatSelctionInfo(int var1, int var2) {
        return new SelectionInfo(this, var1, var2);
    }

    private ArrayList<Integer> createListByRange(int[] var1) {
        ArrayList var3 = new ArrayList();

        for(int var2 = var1[0]; var2 <= var1[1]; ++var2) {
            var3.add(var2);
        }
        return var3;
    }

    private void endSelectionText() {
        this.mStartSelectedTime = -1L;
        this.isTouchValid = false;
        this.mSelectionInfoListTemp.clear();
        this.mSelectionInfoListTemp.addAll(this.mSelectionInfoList);
        if (this.mOnSelectableTextViewListener != null) {
            this.mOnSelectableTextViewListener.endSelectWord();
        }
        this.showSelectionInfo();
    }

    private int[] getRangeArrayByRangeList(ArrayList<Integer> var1) {
        Collections.sort(var1);
        ArrayList var6 = new ArrayList();
        int var5 = var1.size();
        int var2 = -1;

        int var3;
        for(int var4 = 0; var4 < var5; ++var4) {
            var3 = var2;
            if (var2 != -1) {
                var3 = var2 + 1;
            }

            if (var3 == -1) {
                var2 = var1.get(var4);
                var6.add(var2);
            } else {
                var2 = var3;
                if (var3 != var1.get(var4)) {
                    var6.add(var3 - 1);
                    var2 = var1.get(var4);
                    var6.add(var2);
                }
            }
        }

        if (var2 != -1) {
            var6.add(var2);
        }

        var3 = var6.size();
        int[] var7 = new int[var3];

        for(var2 = 0; var2 < var3; ++var2) {
            var7[var2] = (Integer)var6.get(var2);
        }

        return var7;
    }

    private int[] getSelectedRanges(int var1, int var2) {
        int[] var3 = new int[2];
        if (var1 < var2) {
            var3[0] = var1;
            var3[1] = var2;
            return var3;
        } else {
            var3[0] = var2;
            var3[1] = var1;
            return var3;
        }
    }

    private ArrayList<SelectionInfo> getSelectionInfoByRangeArray(int[] var1) {
        ArrayList var4 = new ArrayList();
        int var3 = var1.length / 2;

        for(int var2 = 0; var2 < var3; ++var2) {
            var4.add(this.creatSelctionInfo(var1[var2 % 2 + var2], var1[var2 % 2 + var2 + 1]));
        }

        return var4;
    }

    private char getTextCharByIndex(int var1) {
        char[] var2 = this.getText().toString().toCharArray();
        return var1 >= var2.length ? ' ' : var2[var1];
    }

    private int getValidTextNumber(int var1, int var2) {
        byte var7 = 0;
        byte var6 = 0;
        int var5 = 0;
        int var4 = 0;
        int var3;
        if (var1 > var2) {
            var3 = var1;

            for(var1 = var6; var3 >= var2; var4 = var5) {
                var5 = var4;
                if (this.isValidText(var3)) {
                    var5 = var4 + 1;
                }

                var1 = var3;
                if (var5 >= MAX_SELECTION_TEXT_NUMBER) {
                    break;
                }

                --var3;
            }
        } else {
            var3 = var1;

            for(var1 = var7; var3 <= var2; var1 = var4) {
                int var8 = var5;
                if (this.isValidText(var3)) {
                    var8 = var5 + 1;
                }

                var4 = var3;
                var1 = var3;
                if (var8 >= MAX_SELECTION_TEXT_NUMBER) {
                    break;
                }

                ++var3;
                var5 = var8;
            }
        }
        return var1;
    }

    private void init() {
        if (VERSION.SDK_INT >= 21) {
            this.setLetterSpacing(0.1F);
        }

        mNinePatchDrawable = getContext().getDrawable(R.drawable.ic_smear_part);
        this.mParentRect = new Rect();
        this.mSelectionInfoList = new ArrayList();
        this.mSelectionInfoListTemp = new ArrayList();
        this.rectF = new RectF();
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(this.mDefaultSelectionColor);
        this.paint.setStrokeWidth(3.0F);
        this.mSelectedBound = new Rect();
        this.rectF = new RectF();
    }

    private boolean isScorllTextView() {
        boolean var3 = false;
        int var1 = this.mSelectionInfoList.size();
        boolean var2 = var3;
        if (this.isTouchValid) {
            var2 = var3;
            if (var1 > 0) {
                var2 = var3;
                if (Math.abs((this.mSelectionInfoList.get(var1 - 1)).getStart() - (this.mSelectionInfoList.get(var1 - 1)).getEnd()) < 1) {
                    var2 = true;
                }
            }
        }

        return var2;
    }

    private boolean isValidByChar(char var1) {
        boolean var5 = true;
        int var3 = INVALID_CHARS.length;
        int var2 = 0;

        boolean var4;
        while(true) {
            var4 = var5;
            if (var2 >= var3) {
                break;
            }
            if (var1 == INVALID_CHARS[var2]) {
                var4 = false;
                break;
            }
            ++var2;
        }
        return var4;
    }

    private boolean isValidText(int var1) {
        boolean flag = true;
        char var2 = this.getTextCharByIndex(var1);
        if (CharacterUtils.isChinesePunctuation(var2) || !CharacterUtils.isChineseByBlock(var2)) {
            flag = false;
        }
        return flag;
    }

    private RectF manageDrawPadding(RectF var1) {
        var1.top -= 0.0F;
        var1.bottom += 0.0F;
        var1.left -= 0.0F;
        var1.right += 0.0F;
        return var1;
    }

    private void manageSelected(int var1, int var2) {
        ArrayList var3 = this.createListByRange(this.getSelectedRanges(var1, var2));
        ArrayList var4 = new ArrayList();
        this.mSelectionInfoList.clear();

        for(var1 = 0; var1 < this.mSelectionInfoListTemp.size(); ++var1) {
            SelectionInfo var5 = (SelectionInfo)this.mSelectionInfoListTemp.get(var1);
            ArrayList var6 = this.createListByRange(this.getSelectedRanges(var5.getStart(), var5.getEnd()));
            var4.clear();
            var4.addAll(var3);
            var4.retainAll(var6);
            if (!var4.isEmpty()) {
                ArrayList var8 = this.removeCommonElement(var6, var4);
                var3 = this.removeCommonElement(var3, var4);
                this.mSelectionInfoList.addAll(this.getSelectionInfoByRangeArray(this.getRangeArrayByRangeList(var8)));
            } else {
                this.mSelectionInfoList.add(var5);
            }
        }

        if (var3 != null && var3.size() > 0) {
            int[] var7 = this.getRangeArrayByRangeList(var3);
            this.mSelectionInfoList.addAll(this.getSelectionInfoByRangeArray(var7));
        }
    }

    private ArrayList<Integer> removeCommonElement(ArrayList<Integer> var1, ArrayList<Integer> var2) {
        ArrayList var5 = new ArrayList();
        int var4 = var1.size();

        for(int var3 = 0; var3 < var4; ++var3) {
            if (!var2.contains(var1.get(var3))) {
                var5.add(var1.get(var3));
            }
        }
        return var5;
    }

    private void showSelectionInfo() {
        this.verifySelectionInfo();
        this.postInvalidate();
    }

    private void startSelectionText() {
        this.mStartSelectedTime = System.currentTimeMillis();
        if (this.mOnSelectableTextViewListener != null) {
            this.mOnSelectableTextViewListener.startSelectWord();
        }

        this.mSelectionInfoListTemp.clear();
        this.mSelectionInfoListTemp.addAll(this.mSelectionInfoList);
    }

    private void verifySelectionInfo() {
        int var1 = 0;
        while(var1 < this.mSelectionInfoList.size()) {
            SelectionInfo var5 = (SelectionInfo)this.mSelectionInfoList.get(var1);
            int var2 = var5.getStart();
            int var4 = var5.getEnd();

            int var3;
            while(true) {
                var3 = var4;
                if (this.checkIsValid(var2)) {
                    break;
                }
                var3 = var4;
                if (var2 > var4) {
                    break;
                }
                ++var2;
            }

            while(!this.checkIsValid(var3) && var2 <= var3) {
                --var3;
            }

            if (var2 <= var3) {
                var5.setStart(var2);
                var5.setEnd(var3);
                ++var1;
            } else {
                this.mSelectionInfoList.remove(var1);
            }
        }
    }

    public void draw(Canvas var1) {
        int var7 = this.mSelectionInfoList.size();

        for(int var3 = 0; var3 < var7; ++var3) {
            int var8 = ((SelectionInfo)this.mSelectionInfoList.get(var3)).getStart();
            int var5 = ((SelectionInfo)this.mSelectionInfoList.get(var3)).getEnd() + 1;
            int var4 = var5;
            if (var5 >= this.mMaxTextLength) {
                var4 = this.mMaxTextLength;
            }

            int var6 = this.getLayout().getLineForOffset(var8);
            int var9 = this.getLayout().getLineForOffset(var4);
            int var10 = this.getLineCount();

            for(var5 = var6; var5 <= var9; ++var5) {
                this.getLayout().getLineBounds(var5, this.mSelectedBound);
                this.rectF.top = (float)this.mSelectedBound.top;
                RectF var11 = this.rectF;
                float var2;
                if (var5 == var10 - 2) {
                    var2 = (float)this.mSelectedBound.bottom;
                } else {
                    var2 = (float)(this.mSelectedBound.bottom - this.mLineSpacing);
                }

                var11.bottom = var2;
                this.rectF.left = (float)this.mSelectedBound.left;
                this.rectF.right = (float)this.mSelectedBound.right;
                if (var5 == var6) {
                    this.rectF.left = (float)((int)this.getLayout().getPrimaryHorizontal(var8));
                }

                if (var5 == var9) {
                    this.rectF.right = (float)((int)this.getLayout().getPrimaryHorizontal(var4));
                }

                if (var5 != 0) {
                    this.rectF = this.manageDrawPadding(this.rectF);
                }
                mNinePatchDrawable.setBounds((int)rectF.left, (int)rectF.top, (int)rectF.right, (int)rectF.bottom);
                mNinePatchDrawable.draw(var1);
            }
        }
        super.draw(var1);
    }

    public int getOffset(int var1, int var2) {
        Layout var4 = this.getLayout();
        int var3 = -1;
        if (var4 != null) {
            var3 = var4.getOffsetForHorizontal(var4.getLineForVertical(var2), (float)var1);
        }
        return var3;
    }

    public int getPreciseOffset(int var1, int var2) {
        Layout var4 = this.getLayout();
        if (var4 != null) {
            int var3 = var4.getOffsetForHorizontal(var4.getLineForVertical(var2), (float)var1);
            if ((int)var4.getPrimaryHorizontal(var3) > var1) {
                return var4.getOffsetToLeftOf(var3);
            }
        }
        return this.getOffset(var1, var2);
    }

    public ArrayList<String> getSelectedTextList() {
        ArrayList var1 = new ArrayList();
        if (this.mSelectionInfoList != null) {
            Iterator var2 = this.mSelectionInfoList.iterator();

            while(var2.hasNext()) {
                var1.add(((SelectionInfo)var2.next()).getSelectedText());
            }
        }
        return var1;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    protected void onSizeChanged(int var1, int var2, int var3, int var4) {
        super.onSizeChanged(var1, var2, var3, var4);
        ScrollView var5 = (ScrollView)this.getParent();
        this.mParentRect.left = var5.getLeft();
        this.mParentRect.top = var5.getTop();
        this.mParentRect.right = var5.getRight();
        this.mParentRect.bottom = var5.getBottom();
    }

    public boolean onTouchEvent(MotionEvent var1) {
        int var2 = (int)var1.getRawX();
        int var3 = (int)var1.getRawY();
        switch(var1.getAction()) {
            case 0:
                Log.v(TAG, "MotionEvent.ACTION_DOWN");
                this.mTouchX = (int)var1.getX();
                this.mTouchY = (int)var1.getY();
                var2 = this.getPreciseOffset(this.mTouchX, this.mTouchY);
                this.isTouchValid = this.checkIsValid(var2);
                this.mSelectedLine = this.getLayout().getLineForOffset(var2);
                this.isSliding = false;
                if (this.isTouchValid) {
                    this.startSelectionText();
                    this.manageSelected(var2, var2);
                    this.showSelectionInfo();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.v(TAG, "MotionEvent.ACTION_UP");
                this.endSelectionText();
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.v(TAG, "MotionEvent.ACTION_MOVE");
                if (this.isTouchValid && !this.isSliding) {
                    this.mMoveTouchX = (int)var1.getX();
                    this.mMoveTouchY = (int)var1.getY();
                    if (this.mParentRect.left <= var2 && var2 <= this.mParentRect.right) {
                        var2 = this.getPreciseOffset(this.mTouchX, this.mTouchY);
                        var3 = this.getPreciseOffset(this.mMoveTouchX, this.mMoveTouchY);
                        int var4 = this.getLayout().getLineForOffset(var3);
                        if (this.mSelectedLine != var4) {
                            this.isSliding = true;
                            return true;
                        }

                        this.manageSelected(var2, this.getValidTextNumber(var2, var3));
                        this.showSelectionInfo();
                        return true;
                    }
                }
                break;
            case 3:
                Log.v(TAG, "MotionEvent.ACTION_CANCEL");
                if (this.isScorllTextView()) {
                    this.mSelectionInfoList.remove(this.mSelectionInfoList.size() - 1);
                }

                this.endSelectionText();
                return true;
        }

        return true;
    }

    public void setDefaultSelectionColor(int var1) {
        this.mDefaultSelectionColor = var1;
        this.paint.setColor(this.mDefaultSelectionColor);
    }

    public void setOnSelectableTextViewListener(OnSelectableTextViewListener var1) {
        this.mOnSelectableTextViewListener = var1;
    }

    public void setText(CharSequence var1, BufferType var2) {
        super.setText(var1, var2);
        this.mMaxTextLength = this.getText().length();
        if (VERSION.SDK_INT >= 16) {
            this.mLineSpacing = (int)this.getLineSpacingExtra();
        }
    }

    public interface OnSelectableTextViewListener {
        void endSelectWord();

        void startSelectWord();
    }
}