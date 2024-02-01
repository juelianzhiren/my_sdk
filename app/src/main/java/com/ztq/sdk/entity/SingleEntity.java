package com.ztq.sdk.entity;

public class SingleEntity implements Cloneable {
    public static final int TAG_NORMAL_TEXT = 0;
    public static final int TAG_EDIT_TEXT = 1;
    public static final int TAG_PIC = 2;
    public static final int TAG_SOUND = 3;
    public static final int TAG_VIDEO = 4;
    public static final int TAG_FONT = 5;
    public static final int TAG_NEWLINE = 6;
    public static final int TAG_PINYIN = 7;
    public static final int TAG_USER_ANSWER_AREA_TEXT = 8;

    public static final int TEXT_STYLE_NORMAL = 0;
    public static final int TEXT_STYLE_BOLD = 1;
    public static final int TEXT_STYLE_ITALIC = 2;
    public static final int TEXT_STYLE_BOLD_AND_ITALIC = 3;

    public static final int TEXT_FLAG_BOTTOM_DOT = (int) Math.pow(2, 7);
    public static final int TEXT_FLAG_BOTTOM_WAVE = (int) Math.pow(2, 8);
    public static final int TEXT_FLAG_UNDERLINE = (int) Math.pow(2, 9);

    private String pinyin;
    private String str;
    private int tag;
    private float textSize;  // 只针对TAG_NORMAL_TEXT、TAG_EDIT_TEXT、TAG_FONT有效
    private int textColor;
    private int textStyle;
    private int width;
    private int textFlag;
    private float pinyinSize;
    private int pinyinColor;

    private String sourceStr;
    private int index;

    public SingleEntity(int tag) {
        this.tag = tag;
    }

    public SingleEntity(String sourceStr, float textSize, int textColor) {
        this.sourceStr = sourceStr;
        this.textSize = textSize;
        this.textColor = textColor;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public float getPinyinSize() {
        return pinyinSize;
    }

    public void setPinyinSize(float pinyinSize) {
        this.pinyinSize = pinyinSize;
    }

    public int getPinyinColor() {
        return pinyinColor;
    }

    public void setPinyinColor(int pinyinColor) {
        this.pinyinColor = pinyinColor;
    }

    public int getTextFlag() {
        return textFlag;
    }

    public void setTextFlag(int textFlag) {
        this.textFlag = textFlag;
    }

    public int getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSourceStr() {
        return sourceStr;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

//    @Override
//    public String toString() {
//        return "SingleEntity{" +
//                "str='" + str + '\'' +
//                ", tag=" + tag +
//                ", textSize=" + textSize +
//                ", textColor=" + textColor +
//                ", textStyle=" + textStyle +
//                ", width=" + width +
//                ", textFlag=" + textFlag +
////                ", sourceStr='" + sourceStr + '\'' +
//                ", index=" + index +
//                '}';
//    }

    @Override
    public String toString() {
        return "SingleEntity{" +
                "str='" + str + '\'' +
                "，pinyin='" + pinyin + '\'' +
                ", tag=" + tag +
                '}';
    }
}