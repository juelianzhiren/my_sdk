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
     * 拼音字符串是否由pinyin4j生成的
     */
    private boolean mIsPinyinGeneratedByPinyin4jJar;

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

    public void setmIsPinyinGeneratedByPinyin4jJar(boolean mIsPinyinGeneratedByPinyin4jJar) {
        this.mIsPinyinGeneratedByPinyin4jJar = mIsPinyinGeneratedByPinyin4jJar;
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.v(TAG, "onMeasure");
        // 需要根据文本测量高度
        int height = 0;
        indexList.clear();
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int viewWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        width = 0;
        int totalColumns = 1;
        float pinyinUnitWidth = 0;
        float hanziUnitWidth = 0;
        float unitWidth = 0;
        if (mIsPinyinGeneratedByPinyin4jJar) {
            if (pinyinList != null && pinyinList.size() != 0) {
                for (int index = 0; index < pinyinList.size(); index++) {
                    Log.v(TAG, "2222 pinyin[" + index + "] = " + pinyinList.get(index) + "; flag = " + TextUtils.equals(pinyinList.get(index), "null"));

                    pinyinUnitWidth = pinyinPaint.measureText(pinyinList.get(index).substring(0, pinyinList.get(index).length() - 1));
                    hanziUnitWidth = textPaint.measureText(hanziList.get(index));
                    unitWidth = (pinyinUnitWidth >= hanziUnitWidth ? pinyinUnitWidth : hanziUnitWidth);
                    if (TextUtils.equals(pinyinList.get(index), "null")) {
                        width += textPaint.measureText(hanziList.get(index));
                    } else {
                        Log.v(TAG, "index = " + index + "; pinyin[index] = " + pinyinList.get(index) + "; length = " + pinyinList.get(index).length());
                        width += unitWidth;
                    }
                    if (width > viewWidth) {
                        indexList.add(index);
                        totalColumns++;
                        width = (TextUtils.equals(pinyinList.get(index), "null") ? textPaint.measureText(hanziList.get(index)) : unitWidth);
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
                    String hanzi = hanziList.get(index);
                    if (!Utils.isNullOrNil(hanzi)) {
                        if (index < pinyinList.size()) {
                            String pinyin = pinyinList.get(index);
                            if (Utils.isNullOrNil(pinyin) || pinyin.toLowerCase().equals("null")) {
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
                        }
                    }
                }
                height = (int) Math.ceil(totalColumns * (textPaint.getFontSpacing() + pinyinPaint.getFontSpacing() + density * 2));
            }
        }
        setMeasuredDimension(viewWidth, height);
    }

    private float getMaxLength(int startIndex) {
        if (pinyinPaint == null || textPaint == null || startIndex >= pinyinList.size()) {
            return 0.0f;
        }
        float maxWidth = 0;
        if (pinyinList == null || hanziList == null) {
            return 0.0f;
        }
        for (int i = startIndex; i < pinyinList.size(); i++) {
            String pinyin = pinyinList.get(i);
            String hanzi = hanziList.get(i);
            if (Utils.isNullOrNil(pinyin) || Utils.isNullOrNil(hanzi)) {
                continue;
            }
            if (mIsPinyinGeneratedByPinyin4jJar) {
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

        float maxWidth = 0;
        int count = 0;
        float pinyinUnitWidth = 0;
        float hanziUnitWidth = 0;
        boolean flag = false;
        float unitWidth = 0;
        if (mIsPinyinGeneratedByPinyin4jJar) {
            if (indexList.isEmpty()) {
                // 单行数据处理
                if (pinyinList != null && pinyinList.size() > 0) {
                    maxWidth = getMaxLength(0);
                    if (gravity == Gravity.CENTER) {
                        widthMesure = (getWidth() - maxWidth) / 2;
                    }
                    Log.e(TAG, "widthMesure1 === " + widthMesure + "; maxWidth = " + maxWidth);
                }
            }
            count = 0;
            width = 0;
            comlum = 1;
            if (pinyinList != null && pinyinList.size() > 0) {
                for (int index = 0; index < pinyinList.size(); index++) {
                    Log.v(TAG, "pinyin[" + index + "] = " + pinyinList.get(index));
                    pinyinUnitWidth = pinyinPaint.measureText(pinyinList.get(index).substring(0, pinyinList.get(index).length() - 1));
                    hanziUnitWidth = textPaint.measureText(hanziList.get(index));
                    flag = pinyinUnitWidth >= hanziUnitWidth;
                    unitWidth = flag ? pinyinUnitWidth : hanziUnitWidth;

                    Log.v(TAG, "hanzi[" + index + "] = " + hanziList.get(index) + "; pinyinStr = " + pinyinList.get(index).substring(0, pinyinList.get(index).length() - 1) + "; hanziUnitWidth = " + hanziUnitWidth + "; pinyinUnitWidth = " + pinyinUnitWidth);

                    if (!TextUtils.equals(pinyinList.get(index), "null") && !TextUtils.equals(pinyinList.get(index), " ")) {
                        width = widthMesure + unitWidth;
                        if (width > getWidth()) {
                            comlum++;
                            widthMesure = 0;
                            // 多行考虑最后一行居中问题
                            if (indexList.size() > 0 && indexList.get(indexList.size() - 1) == index) {
                                Log.v(TAG, "more than one line");
                                if (gravity == Gravity.CENTER) {
                                    widthMesure = (getWidth() - getMaxLength(index)) / 2;
                                }
                            }
                        }

                        canvas.drawText(pinyinList.get(index).substring(0, pinyinList.get(index).length() - 1), (flag ? widthMesure : widthMesure + (hanziUnitWidth - pinyinUnitWidth) / 2), comlum * pinyinPaint.getFontSpacing() + (comlum - 1) * textPaint.getFontSpacing() + pinyinPaint.getFontMetrics().bottom, pinyinPaint);
                        Log.e(TAG, "widthmeasure2 === " + widthMesure + "; y = " + (comlum * pinyinPaint.getFontSpacing() + (comlum - 1) * textPaint.getFontSpacing()) + "; baseline = " + (pinyinPaint.getFontMetrics().ascent - pinyinPaint.getFontMetrics().descent));
                        String tone = " ";
                        switch (pinyinList.get(index).charAt(pinyinList.get(index).length() - 1)) {
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
                        int toneIndex = pinyinList.get(index).length() - 3;  // 去掉数字和空格符,这里是等于5
                        Log.v(TAG, "toneIndex = " + toneIndex);
                        int stateIndex = -1;
                        for (; toneIndex >= 0; toneIndex--) {
                            if (pinyinList.get(index).charAt(toneIndex) == 'a' || pinyinList.get(index).charAt(toneIndex) == 'e'
                                    || pinyinList.get(index).charAt(toneIndex) == 'i' || pinyinList.get(index).charAt(toneIndex) == 'o'
                                    || pinyinList.get(index).charAt(toneIndex) == 'u' || pinyinList.get(index).charAt(toneIndex) == 'v') {
                                if (stateIndex == -1 || pinyinList.get(index).charAt(toneIndex) < pinyinList.get(index).charAt(stateIndex)) {
                                    stateIndex = toneIndex;
                                }
                            }
                        }
                        // iu同时存在规则
                        if (pinyinList.get(index).contains("u") && pinyinList.get(index).contains("i") && !pinyinList.get(index).contains("a") && !pinyinList.get(index).contains("o") && !pinyinList.get(index).contains("e")) {
                            stateIndex = pinyinList.get(index).indexOf("u") > pinyinList.get(index).indexOf("i") ? pinyinList.get(index).indexOf("u") : pinyinList.get(index).indexOf("i");
                        }
                        Log.e(TAG, "stateIndex === " + stateIndex + "; tone = " + tone);
                        if (stateIndex != -1) {
                            // 没有声母存在时，stateIndex一直为-1 （'嗯' 转成拼音后变成 ng,
                            // 导致没有声母存在，stateIndex一直为-1，数组越界crash）

                            float toneX = flag ? widthMesure + pinyinPaint.measureText(pinyinList.get(index).substring(0, stateIndex)) + (pinyinPaint.measureText(pinyinList.get(index).charAt(stateIndex) + "") - pinyinPaint.measureText(tone + "")) / 2 : widthMesure + (hanziUnitWidth - pinyinUnitWidth) / 2 + pinyinPaint.measureText(pinyinList.get(index).substring(0, stateIndex)) + (pinyinPaint.measureText(pinyinList.get(index).charAt(stateIndex) + "") - pinyinPaint.measureText(tone + "")) / 2;
                            canvas.drawText(tone, toneX, comlum * pinyinPaint.getFontSpacing() + (comlum - 1) * textPaint.getFontSpacing() + pinyinPaint.getFontMetrics().bottom, pinyinPaint);
                            Log.v(TAG, "tone y = " + (comlum * 2 - 1) * (textPaint.getFontSpacing()));
                        }
                        canvas.drawText(hanziList.get(index), flag ? (widthMesure + (pinyinPaint.measureText(pinyinList.get(index).substring(0, pinyinList.get(index).length() - 1)) - textPaint.measureText(hanziList.get(index))) / 2 - moveHalfIfNeed(pinyinList.get(index).substring(0, pinyinList.get(index).length() - 1), textPaint)) : widthMesure, comlum * (pinyinPaint.getFontSpacing() + textPaint.getFontSpacing()), textPaint);  // 由于拼音长度固定，采用居中显示策略，计算拼音实际长度不需要去掉拼音后面空格
                        Log.e(TAG, "widthmeasure2 === " + widthMesure + "; y = " + (comlum * pinyinPaint.getFontSpacing() + (comlum) * textPaint.getFontSpacing()));
                        widthMesure += unitWidth;
                        Log.v(TAG, "unitWidth = " + unitWidth);
                        count = count + 1;       // 有效拼音
                    } else if (TextUtils.equals(pinyinList.get(index), "null")) {
                        float hanziWidth = widthMesure + textPaint.measureText(hanziList.get(index));
                        if (hanziWidth > getWidth()) {
                            comlum++;
                            widthMesure = 0;
                        }
                        canvas.drawText(hanziList.get(index), widthMesure, comlum * (pinyinPaint.getFontSpacing() + textPaint.getFontSpacing()), textPaint);
                        widthMesure += textPaint.measureText(hanziList.get(index));
                        count = count + 1;
                    }
                }
            }
        } else {
            if (indexList.isEmpty()) {
                // 单行数据处理
                if (hanziList != null && hanziList.size() > 0) {
                    if (gravity == Gravity.CENTER) {
                        widthMesure = (getWidth() - getMaxLength(0)) / 2;
                    }
                }
            }
            count = 0;
            width = 0;
            comlum = 1;
            if (hanziList != null && hanziList.size() != 0 && pinyinList != null) {
                flag = false;
                for (int index = 0; index < hanziList.size(); index++) {
                    String hanzi = hanziList.get(index);
                    if (!Utils.isNullOrNil(hanzi)) {
                        if (index < pinyinList.size()) {
                            String pinyin = pinyinList.get(index);
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
                                width = widthMesure + textPaint.measureText(hanzi);
                            } else {
                                width = widthMesure + unitWidth;
                            }
                            if (width > getWidth()) {
                                comlum++;
                                widthMesure = 0;
                                // 多行考虑最后一行居中问题
                                if (indexList.size() > 0 && indexList.get(indexList.size() - 1) == index) {
                                    Log.v(TAG, "more than one line");
                                    if (gravity == Gravity.CENTER) {
                                        widthMesure = (getWidth() - getMaxLength(index)) / 2;
                                    }
                                }
                            }
                            if (flag) {
                                canvas.drawText(hanzi, widthMesure, comlum * (pinyinPaint.getFontSpacing() + textPaint.getFontSpacing()), textPaint);  // 由于拼音长度固定，采用居中显示策略，计算拼音实际长度不需要去掉拼音后面空格
                            } else {
                                canvas.drawText(hanzi, (hanziUnitWidth >= pinyinUnitWidth ? widthMesure : widthMesure + (pinyinUnitWidth - hanziUnitWidth) / 2), comlum * (pinyinPaint.getFontSpacing() + textPaint.getFontSpacing()), textPaint);
                                canvas.drawText(pinyin, (hanziUnitWidth >= pinyinUnitWidth ? (widthMesure + (hanziUnitWidth - pinyinUnitWidth) / 2) : widthMesure), comlum * pinyinPaint.getFontSpacing() + (comlum - 1) * textPaint.getFontSpacing() + pinyinPaint.getFontMetrics().bottom, pinyinPaint);
                                Paint.FontMetrics fm = pinyinPaint.getFontMetrics();
                                float top = fm.top;
                                float bottom = fm.bottom;
                                float ascent = fm.ascent;
                                float descent = fm.descent;
                                float leading = fm.leading;
                                Log.v(TAG, "delta = " + ((pinyinPaint.getFontMetrics().descent - pinyinPaint.getFontMetrics().ascent) / 2) + "; " + leading + "; top = " + top + "; bottom = " + bottom + "; ascent = " + ascent + "; descent = " + descent);
                            }
                            widthMesure += unitWidth;
                            Log.v(TAG, "unitWidth = " + unitWidth);
                            count = count + 1;       // 有效拼音
                        }
                    }
                }
            }
        }
        super.onDraw(canvas);
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