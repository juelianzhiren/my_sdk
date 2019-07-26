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

import java.util.ArrayList;

public class PinyinTextView extends TextView {
    private final String TAG = "noahedu.PinyinTextView";

    private String[] pinyin;
    private String[] hanzi;
    private int color = Color.rgb(99, 99, 99);
    private int[] colors = new int[]{ Color.rgb(0x3d,0xb1,0x69), color};
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint pinyinPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private ArrayList<Integer> indexList = new ArrayList<>();    // 存储每行首个String位置
    private int comlum = 1;
    private float density;

    private boolean isScrollEnable;
    private float pinyinWidth = 0;

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
        textPaint.setTextSize(40);
        textPaint.setStrokeWidth(density * 2);

        pinyinPaint.setColor(color);
        pinyinPaint.setTextSize(40);
        pinyinPaint.setStrokeWidth(density * 2);

        density = getResources().getDisplayMetrics().density;

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        Log.v(TAG, "screenWidth = " + screenWidth + "; screenHeight = " + screenHeight);
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

    public void setPinyin(String[] pinyin) {
        this.pinyin = pinyin;
    }

    public void setHanzi(String[] hanzi) {
        this.hanzi = hanzi;
    }

    public void setScrollEnable(boolean isScrollEnable) {
        Log.e(TAG, "isScrollEnable == " + isScrollEnable);
        this.isScrollEnable = isScrollEnable;
        if (isScrollEnable) {
            setMovementMethod(ScrollingMovementMethod.getInstance());
        } else {
            setMovementMethod(null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 需要根据文本测量高度
        int width = 0;
        int height = 0;
        indexList.clear();
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);
        if (textPaint != null) {
            if (pinyin != null && pinyin.length != 0) {
                float pinyinWidth = 0;
                int totalColumns = 1;
                for (int index = 0; index < pinyin.length; index++) {

                    Log.v(TAG, "2222 pinyin[" + index + "] = " + pinyin[index] + "; flag = " + TextUtils.equals(pinyin[index], "null"));

                    if (TextUtils.equals(pinyin[index], "null")) {
                        pinyinWidth = pinyinWidth + textPaint.measureText(hanzi[index]);
                    } else {
                        Log.v(TAG, "index = " + index + "; pinyin[index] = " + pinyin[index] + "; length = " + pinyin[index].length());
                        pinyinWidth = pinyinWidth + pinyinPaint.measureText(pinyin[index].substring(0, pinyin[index].length() - 1));
                    }
                    if (pinyinWidth > width) {
                        indexList.add(index);
                        totalColumns++;
                        pinyinWidth = (TextUtils.equals(pinyin[index], "null") ? textPaint.measureText(pinyin[index]) : textPaint.measureText(pinyin[index].substring(0, pinyin[index].length() - 1)));
                    }
                }
                height = (int) Math.ceil((totalColumns * 2) * (textPaint.getFontSpacing() + density * 1));
            } else if (hanzi != null) {
                height = (int) textPaint.getFontSpacing();
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int gravity = getGravity();
        float widthMesure = 0f;
        if (indexList.isEmpty()) {
            // 单行数据处理
            if (pinyin != null && pinyin.length > 0) {
                if (gravity == Gravity.CENTER) {
                    widthMesure = (getWidth() - pinyinPaint.measureText(combinePinEnd(0, pinyin.length))) / 2;
                }
                Log.e(TAG, "widthMesure1 === " + widthMesure + "; combinePinEnd = " + combinePinEnd(0, pinyin.length));
            } else if (hanzi != null && hanzi.length > 0) {
                if (gravity == Gravity.CENTER) {
                    widthMesure = (getWidth() - textPaint.measureText(combineHanziEnd(0, hanzi.length))) / 2;
                }
            }
        }
        int count = 0;
        pinyinWidth = 0;
        comlum = 1;
        if (pinyin != null && pinyin.length > 0) {
            for (int index = 0; index < pinyin.length; index++) {
                Log.v(TAG, "pinyin[" + index + "] = " + pinyin[index]);
                if (!TextUtils.equals(pinyin[index], "null") && !TextUtils.equals(pinyin[index], " ")) {
                    pinyinWidth = widthMesure + pinyinPaint.measureText(pinyin[index].substring(0, pinyin[index].length() - 1));
                    if (pinyinWidth > getWidth()) {
                        comlum++;
                        widthMesure = 0;
                        // 多行考虑最后一行居中问题
                        if (indexList.size() > 0 && indexList.get(indexList.size() - 1) == index) {
                            Log.v(TAG, "more than one line");
                            if (gravity == Gravity.CENTER) {
                                widthMesure = (getWidth() - pinyinPaint.measureText(combinePinEnd(index, pinyin.length))) / 2;
                            }
                        }
                    }
                    canvas.drawText(pinyin[index].substring(0, pinyin[index].length() - 1), widthMesure, (comlum * 2 - 1) * (pinyinPaint.getFontSpacing()), pinyinPaint);
                    Log.e(TAG, "widthmeasure2 === " + widthMesure + "; y = " + (comlum * 2 - 1) * (pinyinPaint.getFontSpacing()));
                    String tone = " ";
                    switch (pinyin[index].charAt(pinyin[index].length() - 1)) {
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
                    int toneIndex = pinyin[index].length() - 3;  // 去掉数字和空格符
                    Log.v(TAG, "toneIndex = " + toneIndex);
                    int stateIndex = -1;
                    for (; toneIndex >= 0; toneIndex--) {
                        if (pinyin[index].charAt(toneIndex) == 'a' || pinyin[index].charAt(toneIndex) == 'e'
                                || pinyin[index].charAt(toneIndex) == 'i' || pinyin[index].charAt(toneIndex) == 'o'
                                || pinyin[index].charAt(toneIndex) == 'u' || pinyin[index].charAt(toneIndex) == 'v') {
                            if (stateIndex == -1 || pinyin[index].charAt(toneIndex) < pinyin[index].charAt(stateIndex)) {
                                stateIndex = toneIndex;
                            }
                        }
                    }
                    // iu同时存在规则
                    if (pinyin[index].contains("u") && pinyin[index].contains("i") && !pinyin[index].contains("a") && !pinyin[index].contains("o") && !pinyin[index].contains("e")) {
                        stateIndex = pinyin[index].indexOf("u") > pinyin[index].indexOf("i") ? pinyin[index].indexOf("u") : pinyin[index].indexOf("i");
                    }
                    Log.e(TAG, "stateIndex === " + stateIndex + "; tone = " + tone);
                    if (stateIndex != -1) {
                        // 没有声母存在时，stateIndex一直为-1 （'嗯' 转成拼音后变成 ng,
                        // 导致没有声母存在，stateIndex一直为-1，数组越界crash）
                        canvas.drawText(tone, widthMesure + pinyinPaint.measureText(pinyin[index].substring(0, stateIndex)) + (pinyinPaint.measureText(pinyin[index].charAt(stateIndex) + "") - pinyinPaint.measureText(tone + "")) / 2, (comlum * 2 - 1) * (textPaint.getFontSpacing()), pinyinPaint);
                        Log.v(TAG, "tone y = " + (comlum * 2 - 1) * (textPaint.getFontSpacing()));
                    }
                    canvas.drawText(hanzi[index], widthMesure + (pinyinPaint.measureText(pinyin[index].substring(0, pinyin[index].length() - 1)) - textPaint.measureText(hanzi[index])) / 2 - moveHalfIfNeed(pinyin[index].substring(0, pinyin[index].length() - 1), textPaint), (comlum * 2) * (textPaint.getFontSpacing()), textPaint);  // 由于拼音长度固定，采用居中显示策略，计算拼音实际长度不需要去掉拼音后面空格
                    if (index + 1 < pinyin.length && TextUtils.equals("null", pinyin[index + 1])) {
                        widthMesure = widthMesure + pinyinPaint.measureText(pinyin[index].substring(0, pinyin[index].length() - 1));
                    } else {
                        widthMesure = widthMesure + pinyinPaint.measureText(pinyin[index].substring(0, pinyin[index].length() - 1));    // 下个字符为拼音
                    }
                    count = count + 1;       // 有效拼音
                } else if (TextUtils.equals(pinyin[index], "null")) {
                    float hanziWidth = widthMesure + textPaint.measureText(hanzi[index]);
                    if (hanziWidth > getWidth()) {
                        comlum++;
                        widthMesure = 0;
                    }
                    canvas.drawText(hanzi[index], widthMesure, (comlum * 2) * textPaint.getFontSpacing(), textPaint);
                    widthMesure = widthMesure + textPaint.measureText(hanzi[index]);
                    count = count + 1;
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
            String pendString = pinyin[subIndex].substring(0, pinyin[subIndex].length() - 1);
            sb.append(pendString);
        }
        return sb.toString();
    }

    private String combineHanziEnd(int index, int length) {
        StringBuilder sb = new StringBuilder();
        for (int subIndex = index; subIndex < length; subIndex++) {
            sb.append(hanzi[subIndex]);
        }
        return sb.toString();
    }
}