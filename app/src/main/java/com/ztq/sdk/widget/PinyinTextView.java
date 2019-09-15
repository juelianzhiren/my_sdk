package com.ztq.sdk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ztq.sdk.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PinyinTextView extends TextView {
    private final String TAG = "noahedu.PinyinTextView";

    private List<String> pinyinList;
    private List<String> hanziList;
    private int color = Color.rgb(99, 99, 99);
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint pinyinPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private ArrayList<Integer> indexList = new ArrayList<>();    // 存储每行首个String位置
    private int comlum = 1;
    private float density;
    private float width = 0;

    /**
     * 每个拼音是否由7个字符组成，
     */
    private boolean mIsPinyinComposeOf7Chars;

    public PinyinTextView(Context context) {
        this(context, null);
    }

    public PinyinTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinyinTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTextPaint();
    }

    public void initTextPaint() {
        textPaint.setColor(color);
        textPaint.setTextSize(60);
        textPaint.setStrokeWidth(density * 2);

        pinyinPaint.setColor(color);
        pinyinPaint.setTextSize(20);
        pinyinPaint.setStrokeWidth(density * 2);

        density = getResources().getDisplayMetrics().density;

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        Log.v(TAG, "screenWidth = " + screenWidth + "; screenHeight = " + screenHeight);
    }

    public void setIsPinyinComposeOf7Chars(boolean mIsPinyinComposeOf7Chars) {
        this.mIsPinyinComposeOf7Chars = mIsPinyinComposeOf7Chars;
    }

    public void setTextSize(int textSize) {
        textPaint.setTextSize(textSize);
    }

    public void setTextColor(int textColor) {
        textPaint.setColor(textColor);
    }

    public void setPinyinSize(int pinyinSize) {
        pinyinPaint.setTextSize(pinyinSize);
    }

    public void setPinyinColor(int pinyinColor) {
        pinyinPaint.setColor(pinyinColor);
    }

    public void setPinyinList(List<String> pinyinList) {
        this.pinyinList = pinyinList;
    }

    public void setHanziList(List<String> hanziList) {
        this.hanziList = hanziList;
    }

    public void setScrollEnable(boolean isScrollEnable) {
        Log.e(TAG, "isScrollEnable == " + isScrollEnable);
        if (isScrollEnable) {
            setMovementMethod(ScrollingMovementMethod.getInstance());
        } else {
            setMovementMethod(null);
        }
    }

    /**
     * 整理数据，让hanziList和pinyinList的size一样，一一对应
     */
    public void organzieData() {
        if (hanziList  != null || pinyinList != null) {
            for(int i = 0; i < hanziList.size(); i++) {
                if (i < pinyinList.size()) {
                    if (Utils.isNullOrNil(pinyinList.get(i))) {
                        pinyinList.set(i, "null");
                    }
                } else {
                    pinyinList.add("null");
                }
            }
            if (pinyinList.size() > hanziList.size()) {
                for(int i = pinyinList.size() - 1; i >= hanziList.size(); i--) {
                    pinyinList.remove(i);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.v(TAG, "onMeasure");
        // 需要根据文本测量高度
        int height = 0;
        indexList.clear();
        indexList.add(0);
        int viewWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        width = 0;
        int totalColumns = 1;
        float pinyinUnitWidth = 0;
        float hanziUnitWidth = 0;
        float unitWidth = 0;
        String hanzi = "";
        if (mIsPinyinComposeOf7Chars) {
            if (pinyinList != null && pinyinList.size() != 0) {
                for (int index = 0; index < pinyinList.size(); index++) {
                    Log.v(TAG, "2222 pinyin[" + index + "] = " + pinyinList.get(index) + "; flag = " + TextUtils.equals(pinyinList.get(index), "null"));

                    pinyinUnitWidth = pinyinPaint.measureText(pinyinList.get(index).substring(0, pinyinList.get(index).length() - 1));
                    hanziUnitWidth = textPaint.measureText(hanziList.get(index));
                    unitWidth = (pinyinUnitWidth >= hanziUnitWidth ? pinyinUnitWidth : hanziUnitWidth);
                    hanzi = hanziList.get(index);
                    if (!hanzi.equals("\n")) {
                        if (TextUtils.equals(pinyinList.get(index), "null")) {
                            width += textPaint.measureText(hanzi);
                        } else {
                            Log.v(TAG, "index = " + index + "; pinyin[index] = " + pinyinList.get(index) + "; length = " + pinyinList.get(index).length());
                            width += unitWidth;
                        }
                        if (width > viewWidth) {
                            indexList.add(index);
                            totalColumns++;
                            width = (TextUtils.equals(pinyinList.get(index), "null") ? textPaint.measureText(hanzi) : unitWidth);
                        }
                    } else {
                        indexList.add(index);
                        totalColumns++;
                        width = 0;
                    }
                }
                height = (int) Math.ceil(totalColumns * (textPaint.getFontSpacing() + pinyinPaint.getFontSpacing() + density * 2));
            } else if (hanziList != null) {
                height = (int) textPaint.getFontSpacing();
            }
        } else {
            if (hanziList != null && hanziList.size() != 0 && pinyinList != null) {
                boolean flag = false;
                for (int index = 0; index < hanziList.size(); index++) {
                    hanzi = hanziList.get(index);
                    if (!Utils.isNullOrNil(hanzi)) {
                        if (index < pinyinList.size()) {
                            String pinyin = pinyinList.get(index);
                            if (!hanzi.equals("\n")) {
                                if ((Utils.isNullOrNil(pinyin) || pinyin.toLowerCase().equals("null"))) {
                                    width += textPaint.measureText(hanzi);
                                    flag = true;
                                } else {
                                    pinyinUnitWidth = pinyinPaint.measureText(pinyin);
                                    hanziUnitWidth = textPaint.measureText(hanzi);
                                    unitWidth = (pinyinUnitWidth >= hanziUnitWidth ? pinyinUnitWidth : hanziUnitWidth);
                                    width += unitWidth;
                                    flag = false;
                                }
                                if (width > viewWidth) {
                                    indexList.add(index);
                                    totalColumns++;
                                    width = flag ? textPaint.measureText(hanzi) : unitWidth;
                                }
                            } else {
                                indexList.add(index);
                                totalColumns++;
                                width = 0;
                            }
                        }
                    }
                }
                height = (int) Math.ceil(totalColumns * (textPaint.getFontSpacing() + pinyinPaint.getFontSpacing() + density * 2));
            }
        }
        setMeasuredDimension(viewWidth, height);
    }

    private float getMaxLength(int startIndex, int endIndex) {
        if (pinyinList == null || hanziList == null) {
            return 0.0f;
        }
        if (pinyinPaint == null || textPaint == null || startIndex >= pinyinList.size() || startIndex > endIndex || endIndex > pinyinList.size()) {
            return 0.0f;
        }
        float maxWidth = 0;
        for (int i = startIndex; i < endIndex; i++) {
            String pinyin = pinyinList.get(i);
            String hanzi = hanziList.get(i);
            if (Utils.isNullOrNil(pinyin) || Utils.isNullOrNil(hanzi)) {
                continue;
            }
            if (mIsPinyinComposeOf7Chars) {
                if (pinyin.toLowerCase().equals("null")) {
                    maxWidth += (textPaint.measureText(hanzi) >= pinyinPaint.measureText(pinyin) ? textPaint.measureText(hanzi) : pinyinPaint.measureText(pinyin));
                } else {
                    maxWidth += (textPaint.measureText(hanzi) >= pinyinPaint.measureText(pinyin.substring(0, pinyin.length() - 1)) ? textPaint.measureText(hanzi) : pinyinPaint.measureText(pinyin.substring(0, pinyin.length() - 1)));
                }
            } else {
                if (pinyin.toLowerCase().equals("null")) {
                    maxWidth += textPaint.measureText(hanzi);
                } else {
                    maxWidth += (textPaint.measureText(hanzi) >= pinyinPaint.measureText(pinyin) ? textPaint.measureText(hanzi) : pinyinPaint.measureText(pinyin));
                }
            }
        }
        return maxWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.v(TAG, "onDraw");
        int gravity = getGravity();
        float widthMesure = 0f;

        float pinyinUnitWidth = 0;
        float hanziUnitWidth = 0;
        boolean flag = false;
        float unitWidth = 0;
        if (mIsPinyinComposeOf7Chars) {
            for(int i = 0; i < indexList.size(); i++) {
                widthMesure = 0;
                int startIndex = indexList.get(i);
                String hanzi = hanziList.get(startIndex);
                int endIndex = hanziList.size();

                if (i != indexList.size() - 1) {
                    endIndex = indexList.get(i + 1);
                }
                if (indexList.size() == 1) {
                    if (gravity == Gravity.CENTER) {
                        widthMesure = (getWidth() - getMaxLength(0, pinyinList.size())) / 2;
                    }
                } else {
                    Log.v(TAG, "line " + i + ": " + hanzi + "; flag = " + flag + "; " + hanzi.equals("\n"));
                    if (!hanzi.equals("\n")) {
                        if (gravity == Gravity.CENTER) {
                            widthMesure = (getWidth() - getMaxLength(startIndex, endIndex)) / 2;
                        }
                    } else {
                        if (gravity == Gravity.CENTER) {
                            startIndex = getIndex(startIndex + 1);
                            widthMesure = (getWidth() - getMaxLength(startIndex, endIndex)) / 2;

                            Log.v(TAG, "==\n, widthMeasure = " + widthMesure);
                        }
                    }
                }
                Log.v(TAG, "widthMeasure = " + widthMesure + "; startIndex = " + startIndex + "; endIndex = " + endIndex + "; getWidth = " + getWidth() + "; getMaxLength = " + getMaxLength(startIndex, endIndex));
                for(int j = startIndex; j < endIndex; j++) {
                    hanzi = hanziList.get(j);
                    String pinyin = pinyinList.get(j);
                    if (hanzi.equals("\n")) {
                        continue;
                    } else {
                        Log.v(TAG, "pinyin = " + pinyin);
                        if (Utils.isNullOrNil(pinyin) || pinyin.toLowerCase().equals("null")) {
                            flag = true;
                            unitWidth = textPaint.measureText(hanzi);
                        } else {
                            pinyinUnitWidth = pinyinPaint.measureText(pinyinList.get(j).substring(0, pinyinList.get(j).length() - 1));
                            hanziUnitWidth = textPaint.measureText(hanziList.get(j));
                            flag = false;
                            unitWidth = pinyinUnitWidth >= hanziUnitWidth ? pinyinUnitWidth : hanziUnitWidth;
                        }
                        if (flag) {
                            canvas.drawText(hanziList.get(j), widthMesure, (i + 1) * (pinyinPaint.getFontSpacing() + textPaint.getFontSpacing()), textPaint);
                        } else {
                            canvas.drawText(hanziList.get(j), pinyinUnitWidth >= hanziUnitWidth ? (widthMesure + (pinyinPaint.measureText(pinyinList.get(j).substring(0, pinyinList.get(j).length() - 1)) - textPaint.measureText(hanziList.get(j))) / 2 - moveHalfIfNeed(pinyinList.get(j).substring(0, pinyinList.get(j).length() - 1), textPaint)) : widthMesure, (i + 1) * (pinyinPaint.getFontSpacing() + textPaint.getFontSpacing()), textPaint);  // 由于拼音长度固定，采用居中显示策略，计算拼音实际长度不需要去掉拼音后面空格
                            canvas.drawText(pinyinList.get(j).substring(0, pinyinList.get(j).length() - 1), (pinyinUnitWidth >= hanziUnitWidth ? widthMesure : widthMesure + (hanziUnitWidth - pinyinUnitWidth) / 2), (i + 1) * pinyinPaint.getFontSpacing() + i * textPaint.getFontSpacing() + pinyinPaint.getFontMetrics().bottom, pinyinPaint);
                            String tone = " ";
                            switch (pinyinList.get(j).charAt(pinyinList.get(j).length() - 1)) {
                                case '1':
                                    tone = "ˉ";
                                    break;
                                case '2':
                                    tone = "ˊ";
                                    break;
                                case '3':
                                    tone = "ˇ";
                                    break;
                                case '4':
                                    tone = "ˋ";
                                    break;
                            }
                            int toneIndex = pinyinList.get(j).length() - 3;  // 去掉数字和空格符,这里是等于5
                            Log.v(TAG, "toneIndex = " + toneIndex);
                            int stateIndex = -1;
                            for (; toneIndex >= 0; toneIndex--) {
                                if (pinyinList.get(j).charAt(toneIndex) == 'a' || pinyinList.get(j).charAt(toneIndex) == 'e'
                                        || pinyinList.get(j).charAt(toneIndex) == 'i' || pinyinList.get(j).charAt(toneIndex) == 'o'
                                        || pinyinList.get(j).charAt(toneIndex) == 'u' || pinyinList.get(j).charAt(toneIndex) == 'v') {
                                    if (stateIndex == -1 || pinyinList.get(j).charAt(toneIndex) < pinyinList.get(j).charAt(stateIndex)) {
                                        stateIndex = toneIndex;
                                    }
                                }
                            }
                            // iu同时存在规则
                            if (pinyinList.get(j).contains("u") && pinyinList.get(j).contains("i") && !pinyinList.get(j).contains("a") && !pinyinList.get(j).contains("o") && !pinyinList.get(j).contains("e")) {
                                stateIndex = pinyinList.get(j).indexOf("u") > pinyinList.get(j).indexOf("i") ? pinyinList.get(j).indexOf("u") : pinyinList.get(j).indexOf("i");
                            }
                            Log.e(TAG, "stateIndex === " + stateIndex + "; tone = " + tone);
                            if (stateIndex != -1) {
                                // 没有声母存在时，stateIndex一直为-1 （'嗯' 转成拼音后变成 ng,
                                // 导致没有声母存在，stateIndex一直为-1，数组越界crash）

                                float toneX = pinyinUnitWidth >= hanziUnitWidth ? widthMesure + pinyinPaint.measureText(pinyinList.get(j).substring(0, stateIndex)) + (pinyinPaint.measureText(pinyinList.get(j).charAt(stateIndex) + "") - pinyinPaint.measureText(tone + "")) / 2 : widthMesure + (hanziUnitWidth - pinyinUnitWidth) / 2 + pinyinPaint.measureText(pinyinList.get(j).substring(0, stateIndex)) + (pinyinPaint.measureText(pinyinList.get(j).charAt(stateIndex) + "") - pinyinPaint.measureText(tone + "")) / 2;
                                canvas.drawText(tone, toneX, (i + 1) * pinyinPaint.getFontSpacing() + i * textPaint.getFontSpacing() + pinyinPaint.getFontMetrics().bottom, pinyinPaint);
                                Log.v(TAG, "tone y = " + ((i + 1) * 2 - 1) * (textPaint.getFontSpacing()));
                            }
                        }
                        widthMesure += unitWidth;
                    }
                }
            }
        } else {
            for(int i = 0; i < indexList.size(); i++) {
                widthMesure = 0;
                int startIndex = indexList.get(i);
                String hanzi = hanziList.get(startIndex);
                int endIndex = hanziList.size();

                if (i != indexList.size() - 1) {
                    endIndex = indexList.get(i + 1);
                }
                if (indexList.size() == 1) {
                    if (gravity == Gravity.CENTER) {
                        widthMesure = (getWidth() - getMaxLength(0, pinyinList.size())) / 2;
                    }
                } else {
                    if (!hanzi.equals("\n")) {
                        if (gravity == Gravity.CENTER) {
                            widthMesure = (getWidth() - getMaxLength(startIndex, endIndex)) / 2;
                        }
                    } else {
                        if (gravity == Gravity.CENTER) {
                            startIndex = getIndex(startIndex + 1);
                            widthMesure = (getWidth() - getMaxLength(startIndex, endIndex)) / 2;
                        }
                    }
                }
                Log.v(TAG, "widthMeasure = " + widthMesure + "; startIndex = " + startIndex + "; endIndex = " + endIndex + "; getWidth = " + getWidth() + "; getMaxLength = " + getMaxLength(startIndex, endIndex));
                for(int j = startIndex; j < endIndex; j++) {
                    hanzi = hanziList.get(j);
                    String pinyin = pinyinList.get(j);
                    if (hanzi.equals("\n")) {
                        continue;
                    } else {
                        Log.v(TAG, "pinyin = " + pinyin);
                        if (Utils.isNullOrNil(pinyin) || pinyin.toLowerCase().equals("null")) {
                            flag = true;
                            unitWidth = textPaint.measureText(hanzi);
                        } else {
                            pinyinUnitWidth = pinyinPaint.measureText(pinyin);
                            hanziUnitWidth = textPaint.measureText(hanzi);
                            unitWidth = (pinyinUnitWidth >= hanziUnitWidth ? pinyinUnitWidth : hanziUnitWidth);
                            flag = false;
                        }
                        if (flag) {
                            canvas.drawText(hanzi, widthMesure, (i + 1) * (pinyinPaint.getFontSpacing() + textPaint.getFontSpacing()), textPaint);  // 由于拼音长度固定，采用居中显示策略，计算拼音实际长度不需要去掉拼音后面空格
                        } else {
                            canvas.drawText(hanzi, (hanziUnitWidth >= pinyinUnitWidth ? widthMesure : widthMesure + (pinyinUnitWidth - hanziUnitWidth) / 2), (i + 1) * (pinyinPaint.getFontSpacing() + textPaint.getFontSpacing()), textPaint);
                            canvas.drawText(pinyin, (hanziUnitWidth >= pinyinUnitWidth ? (widthMesure + (hanziUnitWidth - pinyinUnitWidth) / 2) : widthMesure), (i + 1) * pinyinPaint.getFontSpacing() + i * textPaint.getFontSpacing() + pinyinPaint.getFontMetrics().bottom, pinyinPaint);
                            Paint.FontMetrics fm = pinyinPaint.getFontMetrics();
                            float top = fm.top;
                            float bottom = fm.bottom;
                            float ascent = fm.ascent;
                            float descent = fm.descent;
                            float leading = fm.leading;
                            Log.v(TAG, "delta = " + ((pinyinPaint.getFontMetrics().descent - pinyinPaint.getFontMetrics().ascent) / 2) + "; " + leading + "; top = " + top + "; bottom = " + bottom + "; ascent = " + ascent + "; descent = " + descent);
                        }
                        widthMesure += unitWidth;
                    }
                }
            }
        }
        super.onDraw(canvas);
    }

    private int getIndex(int sourceIndex) {
        if (hanziList == null || hanziList.size() == 0 || sourceIndex < 0) {
            return -1;
        }
        if (sourceIndex < hanziList.size()) {
            return sourceIndex;
        }
        return hanziList.size() - 1;
    }

    private float moveHalfIfNeed(String pinyinUnit, TextPaint paint) {
        if (pinyinUnit.trim().length() % 2 == 0) {
            return paint.measureText(" ") / 2;
        } else {
            return 0;
        }
    }

    private String combinePinEnd(int index, int length) {
        StringBuilder sb = new StringBuilder();
        for (int subIndex = index; subIndex < length; subIndex++) {
            String pendString = pinyinList.get(index).substring(0, pinyinList.get(index).length() - 1);
            sb.append(pendString);
        }
        return sb.toString();
    }

    private String combineHanziEnd(int index, int length) {
        StringBuilder sb = new StringBuilder();
        for (int subIndex = index; subIndex < length; subIndex++) {
            sb.append(hanziList.get(subIndex));
        }
        return sb.toString();
    }
}