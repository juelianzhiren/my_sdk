package com.ztq.sdk.utils;

import android.graphics.Color;
import android.text.TextUtils;

import com.ztq.sdk.decode.QuestionDecode;
import com.ztq.sdk.entity.SingleEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    private static final String TAG = "noahedu.StringUtils";
    public static final String PATTERN_IMG = "\\{\\{[a-zA-Z0-9-]+\\}\\}";
    public static final String PATTERN_SOUND = "<sound>[a-zA-Z0-9-]+</sound>";
    public static final String PATTERN_FONT = "(<font)([\\s\\S]+?)(>)([\\s\\S]+?)</font>";
    public static final String PATTERN_ITALIC = "(<i>)([\\s\\S]+?)</i>";
    public static final String PATTERN_BOLD = "(<b>)([\\s\\S]+?)</b>";
    public static final String PATTERN_UNDERLINE = "(<u>)([\\s\\S]+?)</u>";
    public static final String PATTERN_BOTTOM_DOT = "(<udot>)([\\s\\S]+?)</udot>";
    public static final String PATTERN_BOTTOM_WAVE = "(<wave>)([\\s\\S]+?)</wave>";
    public static final String PATTERN_PINYIN = "<pinyin([\\s\\S]+?)/>";

    public static final String PATTERN_IMG1 = "<img([\\s\\S]+?)/>";
    public static final String PATTERN_SOUND1 = "<sound([\\s\\S]+?)/>";
    public static final String PATTERN_VIDEO1 = "<video([\\s\\S]+?)/>";

    public static final String REG_CHOOSE_AREA_SYMBOL = "(\u000e\u0001\u000e\u0001|(\\\\u000e\\\\u0001\\\\u000e\\\\u0001))";         // 所在的区域为选择区域
    public static final String CHOOSE_AREA_SYMBOL = "\u000e\u0001\u000e\u0001";         // 所在的区域为选择区域
    public static final String CHOOSE_AREA_SYMBOL_2 = "\\u000e\\u0001\\u000e\\u0001";   // 所在的区域为选择区域

    public static final String REG_EDIT_AREA_SYMBOL = "(\u000e\u0003\u000e\u0003|(\\\\u000e\\\\u0003\\\\u000e\\\\u0003))";         // 所在的区域为编辑区域
    public static final String EDIT_AREA_SYMBOL = "\u000E\u0003\u000E\u0003";          // 所在的区域为编辑区域
    public static final String EDIT_AREA_SYMBOL_2 = "\\u000e\\u0003\\u000e\\u0003";    // 所在的区域为编辑区域

    public static final String NEW_LINE = "\n";          // 换行

    public static final String LEFT_ANGLE_BRACKET = "<";
    public static final String RIGHT_ANGLE_BRACKET = ">";

    public static String getNullOrNil(CharSequence str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str.toString();
    }

    public static int getInt(String str) {
        int result = Integer.MIN_VALUE;
        try {
            result = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean getBoolean(String str) {
        boolean result = false;
        try {
            result = Boolean.parseBoolean(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取str中包含的url路径，得到路径后，用本地路径替换（类似<img src=""/>），最后将url和path添加到map中
     *
     * @param str
     */
    public static String getDecodePath(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        Pattern imgPattern = Pattern.compile(PATTERN_IMG);
        Matcher matcher = imgPattern.matcher(str);
        String result = str;
        String sourceUrl = "";
        String realUrl = "";
        String replacePath = "";
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            sourceUrl = str.subSequence(start, end).toString();
            if (sourceUrl.length() > 4) {
                realUrl = new QuestionDecode(sourceUrl.substring(2, sourceUrl.length() - 2)).decode();
                replacePath = "<img src=\"" + realUrl + "\"/>";
                result = result.replace(sourceUrl, replacePath);
            }
        }

        Pattern soundPattern1 = Pattern.compile(PATTERN_SOUND);
        Matcher matcher1 = soundPattern1.matcher(result);
        String temp = result;
        while (matcher1.find()) {
            int start = matcher1.start();
            int end = matcher1.end();
            sourceUrl = temp.subSequence(start, end).toString();
            if (sourceUrl.length() > 15) {
                realUrl = new QuestionDecode(sourceUrl.substring(7, sourceUrl.length() - 8).trim()).decode();    // 去掉" <sound> "和" </sound> "两部分
                replacePath = "<sound src=\"" + realUrl + "\"/>";
                temp = temp.replace(sourceUrl, replacePath);
            }
        }
        return temp;
    }

    /**
     * 连接Get请求url，空格用%20替换
     */
    public static String joinGetUrl(String url, java.util.Map<String, String> param) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        if (param != null && !param.isEmpty() && !TextUtils.isEmpty(url)) {
            for (java.util.Map.Entry<String, String> entry : param.entrySet()) {
                if (isFirst) {
                    builder.append("?");
                    isFirst = false;
                } else {
                    builder.append("&");
                }
                builder.append(entry.getKey() + "=" + entry.getValue());
            }
        }
        String paramStr = builder.toString();
        paramStr = paramStr.replaceAll(" ", "%20"); // Volley框架Get请求，参数带空格会报错505
        return url + paramStr;
    }

    public static java.util.List<SingleEntity> parseData2(String str, float textSize, int textColor, float pinyinSize, int pinyinColor) {
        java.util.List<SingleEntity> list = null;
        if (TextUtils.isEmpty(str)) {
            return list;
        }
        String decodeStr = StringUtil.getDecodePath(str);
        if (decodeStr.contains("<uwavy>") && decodeStr.contains("</uwavy>")) {
            decodeStr = decodeStr.replace("<uwavy>", "<wave>").replace("</uwavy>", "</wave>");
        }
//        if (decodeStr.contains("\n")) {
//            decodeStr = decodeStr.replace("\n", NEW_LINE);
//        }
//        decodeStr = dealWithPinyinInfo(decodeStr);

        list = parseFontTag(list, decodeStr, textSize, textColor);
        android.util.Log.v(TAG, "parseFontTag, list = " + list);
        list = parseBAndITag(list, decodeStr, textSize, textColor);
        android.util.Log.v(TAG, "parseBAndITag, list = " + list);
        list = parseUTag(list, decodeStr, textSize, textColor);
        android.util.Log.v(TAG, "parseUTag, list = " + list);
        list = parseUDotTag(list, decodeStr, textSize, textColor);
        android.util.Log.v(TAG, "parseUDotTag, list = " + list);
        list = parseWaveTag(list, decodeStr, textSize, textColor);
        android.util.Log.v(TAG, "parseWaveTag, list = " + list);
        list = parsePinyinTag(list, decodeStr, textSize, textColor, pinyinSize, pinyinColor);
        android.util.Log.v(TAG, "parsePinyinTag, list = " + list);
        list = parseImgTag(list, decodeStr, textSize, textColor);
        android.util.Log.v(TAG, "parseImgTag, list = " + list);
        list = parseSoundTag(list, decodeStr, textSize, textColor);
        android.util.Log.v(TAG, "parseSoundTag, list = " + list);
        list = parseVideoTag(list, decodeStr, textSize, textColor);
        android.util.Log.v(TAG, "parseVideoTag, list = " + list);
        list = parseChooseAreaTag(list, decodeStr, textSize, textColor);
        android.util.Log.v(TAG, "parseChooseAreaTag, list = " + list);
        list = parseEditAreaTag(list, decodeStr, textSize, textColor);
        android.util.Log.v(TAG, "parseEditAreaTag, list = " + list);
        list = parseNewLineTag(list, decodeStr, textSize, textColor);
        android.util.Log.v(TAG, "parseNewLineTag, list = " + list);
        return list;
    }

    /**
     * 解析<font>标签
     *
     * @param str
     * @param textSize
     * @param textColor
     * @return
     */
    private static java.util.List<SingleEntity> parseFontTag(java.util.List<SingleEntity> sourceList, String str, float textSize, int textColor) {
        if (TextUtils.isEmpty(str)) {
            return sourceList;
        }
        java.util.List<SingleEntity> list = new ArrayList<>();
        int end = 0;
        if (sourceList != null && sourceList.size() != 0) {
            list.addAll(sourceList);
            for (int i = 0; i < sourceList.size(); i++) {
                SingleEntity entity = sourceList.get(i);
                java.util.List<SingleEntity> childList = new ArrayList<>();
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getStr())) {
                        try {
                            SingleEntity newEntity = (SingleEntity) entity.clone();

                            if (entity.getStr().contains("<font")) {
                                Pattern fontPattern = Pattern.compile(PATTERN_FONT, Pattern.DOTALL);
                                Matcher matcher = fontPattern.matcher(entity.getStr());
                                int start = 0;
                                end = 0;
                                while (matcher.find()) {
                                    String group4 = matcher.group(4);
                                    start = matcher.start();

                                    if (start > end) {
                                        newEntity.setStr(entity.getStr().substring(end, start));
                                        newEntity.setIndex(newEntity.getIndex() + end);
                                        childList.add(newEntity);

                                        newEntity = (SingleEntity) newEntity.clone();
                                    }
                                    end = matcher.end();

                                    int oldTextFlag = newEntity.getTextFlag();
                                    int oldTextColor = newEntity.getTextColor();
                                    float oldTextSize = newEntity.getTextSize();
                                    newEntity.setStr(group4);
                                    newEntity.setIndex(newEntity.getIndex() + start);
                                    String fontStr = matcher.group(1) + matcher.group(2) + matcher.group(3);
                                    String color = getFontTagPropertyValue(fontStr, "color");
                                    if (color != null) {
                                        if (isInt(color)) {
                                            newEntity.setTextColor(getInt(color));
                                        } else {
                                            newEntity.setTextColor(Color.parseColor(color));
                                        }
                                    }
                                    String size = getFontTagPropertyValue(fontStr, "size");
                                    if (size != null) {
                                        newEntity.setTextSize(6 * getInt(size));
                                    }
                                    String underline = getFontTagPropertyValue(fontStr, "underline");
                                    if (underline != null) {
                                        newEntity.setTextFlag(getBoolean(underline) ? SingleEntity.TEXT_FLAG_UNDERLINE : 0);
                                    }
                                    childList.add(newEntity);
                                    newEntity = (SingleEntity) newEntity.clone();
                                    newEntity.setTextFlag(oldTextFlag);
                                    newEntity.setTextColor(oldTextColor);
                                    newEntity.setTextSize(oldTextSize);
                                }
                                if (end != entity.getStr().length()) {
                                    newEntity.setStr(entity.getStr().substring(end));
                                    newEntity.setIndex(newEntity.getIndex() + end);
                                    childList.add(newEntity);
                                }
                                int index = list.indexOf(entity);
                                list.addAll(index, childList);
                                list.remove(entity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (str.contains("<font")) {
                Pattern fontPattern = Pattern.compile(PATTERN_FONT, Pattern.DOTALL);
                Matcher matcher = fontPattern.matcher(str);
                int start = 0;
                end = 0;
                while (matcher.find()) {
                    String group4 = matcher.group(4);
                    start = matcher.start();

                    if (start > end) {
                        SingleEntity entity = new SingleEntity(str, textSize, textColor);
                        entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                        entity.setStr(str.substring(end, start));
                        entity.setIndex(end);
                        list.add(entity);
                    }
                    end = matcher.end();

                    SingleEntity entity = new SingleEntity(str, textSize, textColor);
                    entity.setStr(group4);
                    entity.setIndex(start);
                    String fontStr = matcher.group(1) + matcher.group(2) + matcher.group(3);
                    String color = getFontTagPropertyValue(fontStr, "color");
                    if (color != null) {
                        if (isInt(color)) {
                            entity.setTextColor(getInt(color));
                        } else {
                            entity.setTextColor(Color.parseColor(color));
                        }
                    }
                    String size = getFontTagPropertyValue(fontStr, "size");
                    if (size != null) {
                        entity.setTextSize(6 * getInt(size));
                    }
                    String underline = getFontTagPropertyValue(fontStr, "underline");
                    if (underline != null) {
                        entity.setTextFlag(getBoolean(underline) ? SingleEntity.TEXT_FLAG_UNDERLINE : 0);
                    }
                    list.add(entity);
                }
            }
            if (end != str.length()) {
                SingleEntity entity = new SingleEntity(str, textSize, textColor);
                entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                entity.setStr(str.substring(end));
                entity.setIndex(end);
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 解析<b>、<i>标签
     *
     * @param str
     * @param textSize
     * @param textColor
     * @return
     */
    private static java.util.List<SingleEntity> parseBAndITag(java.util.List<SingleEntity> sourceList, String str, float textSize, int textColor) {
        if (TextUtils.isEmpty(str)) {
            return sourceList;
        }
        java.util.List<SingleEntity> list = new ArrayList<>();
        if (sourceList != null && sourceList.size() != 0) {
            list.addAll(sourceList);
            for (int i = 0; i < sourceList.size(); i++) {
                SingleEntity entity = sourceList.get(i);
                java.util.List<SingleEntity> childList = new ArrayList<>();
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getStr())) {
                        // [0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, 1, -1, -1, -1, -1, 0, 0, -1, -1, -1, 2, 2, -1, -1, -1, -1, 0, 0, 0, -1, -1, -1, 1, 1, -1, -1, -1, 3, -1, -1, -1, -1, 1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0]
                        int[] results = getFontTextStyles(entity.getStr());
                        int startValidIndex = 0;
                        int endValidIndex = 0;
                        int theSameResult = 0;
                        try {
                            SingleEntity newEntity = (SingleEntity) entity.clone();
                            if (results != null && results.length != 0) {
                                for (int j = 0; j < results.length; j++) {
                                    if (results[j] == -1) {
                                        if (endValidIndex != -1) {
                                            if (startValidIndex < endValidIndex) {
                                                newEntity.setStr(entity.getStr().substring(startValidIndex, endValidIndex));
                                                newEntity.setTextStyle(theSameResult);
                                                newEntity.setIndex(entity.getIndex() + startValidIndex);
                                                childList.add(newEntity);
                                                android.util.Log.v(TAG, "parseBAndITag, 111, list = " + list + ", childList = " + childList);
                                                newEntity = (SingleEntity) newEntity.clone();
                                            }
                                        }
                                        endValidIndex = -1;
                                    } else if (results[j] == theSameResult) {
                                        endValidIndex = j + 1;
                                    } else if (results[j] != -1 && endValidIndex == -1) {
                                        startValidIndex = j;
                                        endValidIndex = j + 1;
                                        theSameResult = results[j];
                                    }
                                }
                            }
                            if (endValidIndex != -1) {
                                if (startValidIndex < endValidIndex) {
                                    newEntity.setStr(entity.getStr().substring(startValidIndex, endValidIndex));
                                    newEntity.setIndex(entity.getIndex() + startValidIndex);
                                    newEntity.setTextStyle(theSameResult);
                                    childList.add(newEntity);
                                    android.util.Log.v(TAG, "parseBAndITag, 222, list = " + list + ", childList = " + childList);
                                }
                            }
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                int index = list.indexOf(entity);
                list.addAll(index, childList);
                list.remove(entity);
            }
        } else {
            // [0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, 1, -1, -1, -1, -1, 0, 0, -1, -1, -1, 2, 2, -1, -1, -1, -1, 0, 0, 0, -1, -1, -1, 1, 1, -1, -1, -1, 3, -1, -1, -1, -1, 1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0]
            int[] results = getFontTextStyles(str);
            int startValidIndex = 0;
            int endValidIndex = 0;
            int theSameResult = 0;
            SingleEntity entity = new SingleEntity(str, textSize, textColor);
            entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
            if (results != null && results.length != 0) {
                for (int i = 0; i < results.length; i++) {
                    if (results[i] == -1) {
                        if (endValidIndex != -1) {
                            if (startValidIndex < endValidIndex) {
                                entity.setStr(str.substring(startValidIndex, endValidIndex));
                                entity.setTextStyle(theSameResult);
                                list.add(entity);
                                try {
                                    entity = (SingleEntity) entity.clone();
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        endValidIndex = -1;
                    } else if (results[i] == theSameResult) {
                        endValidIndex = i + 1;
                    } else if (results[i] != -1 && endValidIndex == -1) {
                        startValidIndex = i;
                        endValidIndex = i + 1;
                        theSameResult = results[i];
                    }
                }
            }
            if (endValidIndex != -1) {
                if (startValidIndex < endValidIndex) {
                    entity.setStr(str.substring(startValidIndex, endValidIndex));
                    entity.setTextStyle(theSameResult);
                    list.add(entity);
                }
            }
        }
        return list;
    }

    /**
     * 解析<u>标签
     *
     * @param str
     * @param textSize
     * @param textColor
     * @return
     */
    private static java.util.List<SingleEntity> parseUTag(java.util.List<SingleEntity> sourceList, String str, float textSize, int textColor) {
        if (TextUtils.isEmpty(str)) {
            return sourceList;
        }
        java.util.List<SingleEntity> list = new ArrayList<>();
        int end = 0;
        if (sourceList != null && sourceList.size() != 0) {
            list.addAll(sourceList);
            for (int i = 0; i < sourceList.size(); i++) {
                SingleEntity entity = sourceList.get(i);
                java.util.List<SingleEntity> childList = new ArrayList<>();
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getStr())) {
                        try {
                            SingleEntity newEntity = (SingleEntity) entity.clone();
                            if (entity.getStr().contains("<u>") && entity.getStr().contains("</u>")) {
                                Pattern pattern = Pattern.compile(PATTERN_UNDERLINE);
                                Matcher matcher = pattern.matcher(entity.getStr());
                                int start = 0;
                                end = 0;
                                android.util.Log.v(TAG, "underline, str = " + entity.getStr());
                                while (matcher.find()) {
                                    android.util.Log.v(TAG, "underline, str = " + entity.getStr() + ", find = true");
                                    String group2 = matcher.group(2);
                                    start = matcher.start();

                                    if (start > end) {
                                        newEntity.setStr(entity.getStr().substring(end, start));
                                        newEntity.setIndex(newEntity.getIndex() + end);
                                        childList.add(newEntity);
                                        android.util.Log.v(TAG, "1111parseU list before = " + list + ", childList = " + childList);
                                        newEntity = (SingleEntity) newEntity.clone();
                                    }
                                    end = matcher.end();

                                    int oldTextFlag = newEntity.getTextFlag();

                                    newEntity.setStr(group2);
                                    newEntity.setIndex(newEntity.getIndex() + start);
                                    newEntity.setTextFlag(SingleEntity.TEXT_FLAG_UNDERLINE);
                                    childList.add(newEntity);
                                    newEntity = (SingleEntity) newEntity.clone();
                                    newEntity.setTextFlag(oldTextFlag);

                                    android.util.Log.v(TAG, "2222parseU list before = " + list + ", childList = " + childList);
                                }
                                if (end != entity.getStr().length()) {
                                    newEntity.setStr(entity.getStr().substring(end));
                                    newEntity.setIndex(newEntity.getIndex() + end);
                                    childList.add(newEntity);
                                }
                                android.util.Log.v(TAG, "parseU list before = " + list + ", childList = " + childList);
                                int index = list.indexOf(entity);
                                list.addAll(index, childList);
                                list.remove(entity);
                                android.util.Log.v(TAG, "parseU list after = " + list);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (str.contains("<u>") && str.contains("</u>")) {
                Pattern pattern = Pattern.compile(PATTERN_UNDERLINE, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(str);
                int start = 0;
                end = 0;
                while (matcher.find()) {
                    String group2 = matcher.group(2);
                    start = matcher.start();

                    if (start > end) {
                        SingleEntity entity = new SingleEntity(str, textSize, textColor);
                        entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                        entity.setStr(str.substring(end, start));
                        entity.setIndex(end);
                        list.add(entity);
                    }
                    end = matcher.end();

                    SingleEntity entity = new SingleEntity(str, textSize, textColor);
                    entity.setStr(group2);
                    entity.setIndex(start);
                    entity.setTextFlag(SingleEntity.TEXT_FLAG_UNDERLINE);

                    list.add(entity);
                }
            }
            if (end != str.length()) {
                SingleEntity entity = new SingleEntity(str, textSize, textColor);
                entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                entity.setStr(str.substring(end));
                entity.setIndex(end);
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 解析<udot>标签
     *
     * @param str
     * @param textSize
     * @param textColor
     * @return
     */
    private static java.util.List<SingleEntity> parseUDotTag(java.util.List<SingleEntity> sourceList, String str, float textSize, int textColor) {
        if (TextUtils.isEmpty(str)) {
            return sourceList;
        }
        java.util.List<SingleEntity> list = new ArrayList<>();
        int end = 0;
        if (sourceList != null && sourceList.size() != 0) {
            list.addAll(sourceList);
            for (int i = 0; i < sourceList.size(); i++) {
                SingleEntity entity = sourceList.get(i);
                java.util.List<SingleEntity> childList = new ArrayList<>();
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getStr())) {
                        try {
                            SingleEntity newEntity = (SingleEntity) entity.clone();
                            if (entity.getStr().contains("<udot>") && entity.getStr().contains("</udot>")) {
                                Pattern fontPattern = Pattern.compile(PATTERN_BOTTOM_DOT, Pattern.DOTALL);
                                Matcher matcher = fontPattern.matcher(entity.getStr());
                                int start = 0;
                                end = 0;
                                while (matcher.find()) {
                                    String group2 = matcher.group(2);
                                    start = matcher.start();

                                    if (start > end) {
                                        newEntity.setStr(entity.getStr().substring(end, start));
                                        newEntity.setIndex(newEntity.getIndex() + end);
                                        childList.add(newEntity);

                                        newEntity = (SingleEntity) newEntity.clone();
                                    }
                                    end = matcher.end();

                                    int oldTextFlag = newEntity.getTextFlag();
                                    newEntity.setStr(group2);
                                    newEntity.setIndex(newEntity.getIndex() + start);
                                    newEntity.setTextFlag(SingleEntity.TEXT_FLAG_BOTTOM_DOT);
                                    childList.add(newEntity);
                                    newEntity = (SingleEntity) newEntity.clone();
                                    newEntity.setTextFlag(oldTextFlag);
                                }
                                if (end != entity.getStr().length()) {
                                    newEntity.setStr(entity.getStr().substring(end));
                                    newEntity.setIndex(newEntity.getIndex() + end);
                                    childList.add(newEntity);
                                }
                                int index = list.indexOf(entity);
                                list.addAll(index, childList);
                                list.remove(entity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (str.contains("<udot>") && str.contains("</udot>")) {
                Pattern pattern = Pattern.compile(PATTERN_BOTTOM_DOT, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(str);
                int start = 0;
                end = 0;
                while (matcher.find()) {
                    String group2 = matcher.group(2);
                    start = matcher.start();

                    if (start > end) {
                        SingleEntity entity = new SingleEntity(str, textSize, textColor);
                        entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                        entity.setStr(str.substring(end, start));
                        entity.setIndex(end);
                        list.add(entity);
                    }
                    end = matcher.end();

                    SingleEntity entity = new SingleEntity(str, textSize, textColor);
                    entity.setStr(group2);
                    entity.setIndex(start);
                    entity.setTextFlag(SingleEntity.TEXT_FLAG_BOTTOM_DOT);

                    list.add(entity);
                }
            }
            if (end != str.length()) {
                SingleEntity entity = new SingleEntity(str, textSize, textColor);
                entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                entity.setStr(str.substring(end));
                entity.setIndex(end);
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 解析<wave>标签
     *
     * @param str
     * @param textSize
     * @param textColor
     * @return
     */
    private static java.util.List<SingleEntity> parseWaveTag(java.util.List<SingleEntity> sourceList, String str, float textSize, int textColor) {
        if (TextUtils.isEmpty(str)) {
            return sourceList;
        }
        java.util.List<SingleEntity> list = new ArrayList<>();
        int end = 0;
        if (sourceList != null && sourceList.size() != 0) {
            list.addAll(sourceList);
            for (int i = 0; i < sourceList.size(); i++) {
                SingleEntity entity = sourceList.get(i);
                java.util.List<SingleEntity> childList = new ArrayList<>();
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getStr())) {
                        try {
                            SingleEntity newEntity = (SingleEntity) entity.clone();
                            if (entity.getStr().contains("<wave>") && entity.getStr().contains("</wave>")) {
                                Pattern fontPattern = Pattern.compile(PATTERN_BOTTOM_WAVE, Pattern.DOTALL);
                                Matcher matcher = fontPattern.matcher(entity.getStr());
                                int start = 0;
                                end = 0;

                                while (matcher.find()) {
                                    String group2 = matcher.group(2);
                                    start = matcher.start();
                                    android.util.Log.v(TAG, "parseWaveTag start = " + start + ", end = " + end);
                                    if (start > end) {
                                        newEntity.setStr(entity.getStr().substring(end, start));
                                        newEntity.setIndex(newEntity.getIndex() + end);
                                        childList.add(newEntity);

                                        newEntity = (SingleEntity) newEntity.clone();
                                    }
                                    end = matcher.end();

                                    android.util.Log.v(TAG, "parseWaveTag TEXT_FLAG_BOTTOM_WAVE str = " + group2 + ", start = " + start + ", end = " + end);
                                    int oldTextFlag = newEntity.getTextFlag();
                                    newEntity.setStr(group2);
                                    newEntity.setIndex(newEntity.getIndex() + start);
                                    newEntity.setTextFlag(SingleEntity.TEXT_FLAG_BOTTOM_WAVE);
                                    childList.add(newEntity);
                                    newEntity = (SingleEntity) newEntity.clone();
                                    newEntity.setTextFlag(oldTextFlag);
                                }
                                if (end != entity.getStr().length()) {
                                    newEntity.setStr(entity.getStr().substring(end));
                                    newEntity.setIndex(newEntity.getIndex() + end);
                                    childList.add(newEntity);
                                }
                                int index = list.indexOf(entity);
                                list.addAll(index, childList);
                                list.remove(entity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (str.contains("<wave>") && str.contains("</wave>")) {
                Pattern pattern = Pattern.compile(PATTERN_BOTTOM_WAVE, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(str);
                int start = 0;
                end = 0;
                while (matcher.find()) {
                    String group2 = matcher.group(2);
                    start = matcher.start();

                    if (start > end) {
                        SingleEntity entity = new SingleEntity(str, textSize, textColor);
                        entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                        entity.setStr(str.substring(end, start));
                        entity.setIndex(end);
                        list.add(entity);
                    }
                    end = matcher.end();

                    SingleEntity entity = new SingleEntity(str, textSize, textColor);
                    entity.setStr(group2);
                    entity.setIndex(start);
                    entity.setTextFlag(SingleEntity.TEXT_FLAG_BOTTOM_WAVE);

                    list.add(entity);
                }
            }
            if (end != str.length()) {
                SingleEntity entity = new SingleEntity(str, textSize, textColor);
                entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                entity.setStr(str.substring(end));
                entity.setIndex(end);
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 解析<pinyin标签
     *
     * @param str
     * @param textSize
     * @param textColor
     * @return
     */
    private static java.util.List<SingleEntity> parsePinyinTag(java.util.List<SingleEntity> sourceList, String str, float textSize, int textColor, float pinyinSize, int pinyinColor) {
        if (TextUtils.isEmpty(str)) {
            return sourceList;
        }
        java.util.List<SingleEntity> list = new ArrayList<>();
        if (sourceList != null && sourceList.size() != 0) {
            list.addAll(sourceList);
            for (int i = 0; i < sourceList.size(); i++) {
                SingleEntity entity = sourceList.get(i);
                java.util.List<SingleEntity> childList = new ArrayList<>();
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getStr())) {
                        try {
                            SingleEntity newEntity = (SingleEntity) entity.clone();

                            if (entity.getStr().contains(PINYIN_SEPARATOR_SYMBOL)) {
                                String[] arr = entity.getStr().split(PINYIN_SEPARATOR_SYMBOL);
                                if (arr != null && arr.length != 0) {
                                    android.util.Log.v(TAG, "arr = " + Arrays.toString(arr) + ", length = " + arr.length);
                                    String text = "";
                                    String pinyin = "";
                                    int index = -1;
                                    int oldTag = entity.getTag();
                                    for (int j = 0; j < arr.length; j++) {
                                        String subStr = arr[j];
                                        if (TextUtils.isEmpty(subStr)) {
                                            continue;
                                        }
                                        index++;
                                        if (index % 2 == 0) {    // 属于汉字
                                            if (j != arr.length - 1) {   // 有对应的拼音
                                                if (subStr.length() > 1) {
                                                    newEntity.setStr(subStr.substring(0, subStr.length() - 1));
                                                    newEntity.setPinyin("");
                                                    newEntity.setTag(oldTag);
                                                    childList.add(newEntity);
                                                    newEntity = (SingleEntity) newEntity.clone();
                                                }
                                                text = subStr.substring(subStr.length() - 1);
                                            } else {                     // 没有对应的拼音
                                                newEntity.setStr(subStr);
                                                newEntity.setPinyin("");
                                                newEntity.setTag(oldTag);
                                                childList.add(newEntity);
                                                newEntity = (SingleEntity) newEntity.clone();
                                            }
                                        } else {             // 属于拼音
                                            pinyin = subStr;
                                            newEntity.setPinyinSize(pinyinSize);
                                            newEntity.setPinyinColor(pinyinColor);
                                            newEntity.setTag(SingleEntity.TAG_PINYIN);
                                            newEntity.setStr(text);
                                            newEntity.setPinyin(pinyin);
                                            childList.add(newEntity);
                                            newEntity = (SingleEntity) newEntity.clone();
                                            newEntity.setTag(oldTag);
                                        }
                                    }
                                }
                                int index = list.indexOf(entity);
                                list.addAll(index, childList);
                                list.remove(entity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (str.contains(PINYIN_SEPARATOR_SYMBOL)) {
                String[] arr = str.split(PINYIN_SEPARATOR_SYMBOL);
                if (arr != null && arr.length != 0) {
                    android.util.Log.v(TAG, "arr = " + Arrays.toString(arr) + ", length = " + arr.length);
                    String text = "";
                    String pinyin = "";
                    for (int j = 0; j < arr.length; j++) {
                        String subStr = arr[j];
                        if (TextUtils.isEmpty(subStr)) {
                            continue;
                        }
                        if (j % 2 == 0) {    // 属于汉字
                            if (j != arr.length - 1) {   // 有对应的拼音
                                if (subStr.length() > 1) {
                                    SingleEntity entity = new SingleEntity(str, textSize, textColor);
                                    entity.setStr(subStr.substring(0, subStr.length() - 1));
                                    list.add(entity);
                                }
                                text = subStr.substring(subStr.length() - 1);
                            } else {                     // 没有对应的拼音
                                SingleEntity entity = new SingleEntity(str, textSize, textColor);
                                entity.setStr(subStr);
                                list.add(entity);
                            }
                        } else {             // 属于拼音
                            pinyin = subStr;
                            SingleEntity entity = new SingleEntity(str, textSize, textColor);
                            entity.setPinyinSize(pinyinSize);
                            entity.setPinyinColor(pinyinColor);
                            entity.setTag(SingleEntity.TAG_PINYIN);
                            entity.setStr(text);
                            entity.setPinyin(pinyin);
                            list.add(entity);
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 解析图片标签
     * @param sourceList
     * @param str
     * @return
     */
    public static java.util.List<SingleEntity> parseImgTag(java.util.List<SingleEntity> sourceList, String str, float textSize, int textColor) {
        if (TextUtils.isEmpty(str)) {
            return sourceList;
        }
        java.util.List<SingleEntity> list = new ArrayList<>();
        int end = 0;
        if (sourceList != null && sourceList.size() != 0) {
            list.addAll(sourceList);
            for (int i = 0; i < sourceList.size(); i++) {
                SingleEntity entity = sourceList.get(i);
                java.util.List<SingleEntity> childList = new ArrayList<>();
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getStr())) {
                        try {
                            SingleEntity newEntity = (SingleEntity) entity.clone();
                            if (entity.getStr().contains("<img")) {
                                Pattern pattern = Pattern.compile(PATTERN_IMG1, Pattern.DOTALL);
                                Matcher matcher = pattern.matcher(entity.getStr());
                                int start = 0;
                                end = 0;
                                while (matcher.find()) {
                                    start = matcher.start();

                                    if (start > end) {
                                        newEntity.setStr(entity.getStr().substring(end, start));
                                        newEntity.setIndex(newEntity.getIndex() + end);
                                        childList.add(newEntity);

                                        newEntity = (SingleEntity) newEntity.clone();
                                    }
                                    end = matcher.end();

                                    String sourceUrl = entity.getStr().subSequence(start, end).toString();
                                    int oldTag = newEntity.getTag();
                                    if (sourceUrl.length() > 6) {
                                        String realUrl = sourceUrl.substring(4, sourceUrl.length() - 2).trim();    // 去掉"<img"和"/>"两部分
                                        if (realUrl.length() > 6) {
                                            realUrl = realUrl.substring(5, realUrl.length() - 1).trim();    // 去掉" src=" "和" " "两部分

                                            newEntity.setStr(realUrl);
                                            newEntity.setIndex(newEntity.getIndex() + start + 9);
                                            newEntity.setTag(SingleEntity.TAG_PIC);
                                            childList.add(newEntity);
                                            newEntity = (SingleEntity) newEntity.clone();
                                            newEntity.setTag(oldTag);
                                        }
                                    }
                                }
                                android.util.Log.v(TAG, "parseImgTag, end = " + end + ", entity.getStr().length() = " +entity.getStr().length());
                                if (end != entity.getStr().length()) {
                                    newEntity.setStr(entity.getStr().substring(end));
                                    newEntity.setIndex(newEntity.getIndex() + end);
                                    childList.add(newEntity);
                                }
                                int index = list.indexOf(entity);
                                list.addAll(index, childList);
                                list.remove(entity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (str.contains("<img")) {
                Pattern pattern = Pattern.compile(PATTERN_IMG1, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(str);
                int start = 0;
                end = 0;
                while (matcher.find()) {
                    start = matcher.start();

                    if (start > end) {
                        SingleEntity entity = new SingleEntity(str, textSize, textColor);
                        entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                        entity.setStr(str.substring(end, start));
                        entity.setIndex(end);
                        list.add(entity);
                    }
                    end = matcher.end();

                    String sourceUrl = str.subSequence(start, end).toString();
                    if (sourceUrl.length() > 6) {
                        String realUrl = sourceUrl.substring(4, sourceUrl.length() - 2).trim();    // 去掉"<img"和"/>"两部分
                        if (realUrl.length() > 6) {
                            realUrl = realUrl.substring(5, realUrl.length() - 1).trim();    // 去掉" src=" "和" " "两部分

                            SingleEntity entity = new SingleEntity(str, textSize, textColor);
                            entity.setStr(realUrl);
                            entity.setIndex(start + 9);
                            entity.setTag(SingleEntity.TAG_PIC);
                            list.add(entity);
                        }
                    }
                }
            }
            if (end != str.length()) {
                SingleEntity entity = new SingleEntity(str, textSize, textColor);
                entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                entity.setStr(str.substring(end));
                entity.setIndex(end);
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 解析声音标签
     * @param sourceList
     * @param str
     * @return
     */
    public static java.util.List<SingleEntity> parseSoundTag(java.util.List<SingleEntity> sourceList, String str, float textSize, int textColor) {
        if (TextUtils.isEmpty(str)) {
            return sourceList;
        }
        java.util.List<SingleEntity> list = new ArrayList<>();
        int end = 0;
        if (sourceList != null && sourceList.size() != 0) {
            list.addAll(sourceList);
            for (int i = 0; i < sourceList.size(); i++) {
                SingleEntity entity = sourceList.get(i);
                java.util.List<SingleEntity> childList = new ArrayList<>();
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getStr())) {
                        try {
                            SingleEntity newEntity = (SingleEntity) entity.clone();
                            if (entity.getStr().contains("<sound")) {
                                Pattern pattern = Pattern.compile(PATTERN_SOUND1, Pattern.DOTALL);
                                Matcher matcher = pattern.matcher(entity.getStr());
                                int start = 0;
                                end = 0;
                                while (matcher.find()) {
                                    start = matcher.start();

                                    if (start > end) {
                                        newEntity.setStr(entity.getStr().substring(end, start));
                                        newEntity.setIndex(newEntity.getIndex() + end);
                                        childList.add(newEntity);

                                        newEntity = (SingleEntity) newEntity.clone();
                                    }
                                    end = matcher.end();

                                    int oldTag = newEntity.getTag();
                                    String sourceUrl = entity.getStr().subSequence(start, end).toString();
                                    if (sourceUrl.length() > 8) {
                                        String realUrl = sourceUrl.substring(6, sourceUrl.length() - 2).trim();    // 去掉"<img"和"/>"两部分
                                        if (realUrl.length() > 6) {
                                            realUrl = realUrl.substring(5, realUrl.length() - 1).trim();    // 去掉" src=" "和" " "两部分

                                            newEntity.setStr(realUrl);
                                            newEntity.setIndex(newEntity.getIndex() + start + 11);
                                            newEntity.setTag(SingleEntity.TAG_SOUND);
                                            childList.add(newEntity);
                                            newEntity = (SingleEntity) newEntity.clone();
                                            newEntity.setTag(oldTag);
                                        }
                                    }
                                }
                                if (end != entity.getStr().length()) {
                                    newEntity.setStr(entity.getStr().substring(end));
                                    newEntity.setIndex(newEntity.getIndex() + end);
                                    childList.add(newEntity);
                                }
                                int index = list.indexOf(entity);
                                list.addAll(index, childList);
                                list.remove(entity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (str.contains("<sound")) {
                Pattern pattern = Pattern.compile(PATTERN_SOUND1, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(str);
                int start = 0;
                end = 0;
                while (matcher.find()) {
                    start = matcher.start();

                    if (start > end) {
                        SingleEntity entity = new SingleEntity(str, textSize, textColor);
                        entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                        entity.setStr(str.substring(end, start));
                        entity.setIndex(end);
                        list.add(entity);
                    }
                    end = matcher.end();

                    String sourceUrl = str.subSequence(start, end).toString();
                    if (sourceUrl.length() > 8) {
                        String realUrl = sourceUrl.substring(6, sourceUrl.length() - 2).trim();    // 去掉"<img"和"/>"两部分
                        if (realUrl.length() > 6) {
                            realUrl = realUrl.substring(5, realUrl.length() - 1).trim();    // 去掉" src=" "和" " "两部分

                            SingleEntity entity = new SingleEntity(str, textSize, textColor);
                            entity.setStr(realUrl);
                            entity.setIndex(start + 11);
                            entity.setTag(SingleEntity.TAG_SOUND);
                            list.add(entity);
                        }
                    }
                }
            }
            if (end != str.length()) {
                SingleEntity entity = new SingleEntity(str, textSize, textColor);
                entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                entity.setStr(str.substring(end));
                entity.setIndex(end);
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 解析视频标签
     * @param sourceList
     * @param str
     * @return
     */
    public static java.util.List<SingleEntity> parseVideoTag(java.util.List<SingleEntity> sourceList, String str, float textSize, int textColor) {
        if (TextUtils.isEmpty(str)) {
            return sourceList;
        }
        java.util.List<SingleEntity> list = new ArrayList<>();
        int end = 0;
        if (sourceList != null && sourceList.size() != 0) {
            list.addAll(sourceList);
            for (int i = 0; i < sourceList.size(); i++) {
                SingleEntity entity = sourceList.get(i);
                java.util.List<SingleEntity> childList = new ArrayList<>();
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getStr())) {
                        try {
                            SingleEntity newEntity = (SingleEntity) entity.clone();
                            if (entity.getStr().contains("<video")) {
                                Pattern pattern = Pattern.compile(PATTERN_VIDEO1, Pattern.DOTALL);
                                Matcher matcher = pattern.matcher(entity.getStr());
                                int start = 0;
                                end = 0;
                                while (matcher.find()) {
                                    start = matcher.start();

                                    if (start > end) {
                                        newEntity.setStr(entity.getStr().substring(end, start));
                                        newEntity.setIndex(newEntity.getIndex() + end);
                                        childList.add(newEntity);

                                        newEntity = (SingleEntity) newEntity.clone();
                                    }
                                    end = matcher.end();

                                    int oldTag = newEntity.getTag();
                                    String sourceUrl = entity.getStr().subSequence(start, end).toString();
                                    if (sourceUrl.length() > 8) {
                                        String realUrl = sourceUrl.substring(6, sourceUrl.length() - 2).trim();    // 去掉"<img"和"/>"两部分
                                        if (realUrl.length() > 6) {
                                            realUrl = realUrl.substring(5, realUrl.length() - 1).trim();    // 去掉" src=" "和" " "两部分

                                            newEntity.setStr(realUrl);
                                            newEntity.setIndex(newEntity.getIndex() + start + 11);
                                            newEntity.setTag(SingleEntity.TAG_VIDEO);
                                            childList.add(newEntity);
                                            newEntity = (SingleEntity) newEntity.clone();
                                            newEntity.setTag(oldTag);
                                        }
                                    }
                                }
                                if (end != entity.getStr().length()) {
                                    newEntity.setStr(entity.getStr().substring(end));
                                    newEntity.setIndex(newEntity.getIndex() + end);
                                    childList.add(newEntity);
                                }
                                int index = list.indexOf(entity);
                                list.addAll(index, childList);
                                list.remove(entity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (str.contains("<video")) {
                Pattern pattern = Pattern.compile(PATTERN_VIDEO1, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(str);
                int start = 0;
                end = 0;
                while (matcher.find()) {
                    start = matcher.start();

                    if (start > end) {
                        SingleEntity entity = new SingleEntity(str, textSize, textColor);
                        entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                        entity.setStr(str.substring(end, start));
                        entity.setIndex(end);
                        list.add(entity);
                    }
                    end = matcher.end();

                    String sourceUrl = str.subSequence(start, end).toString();
                    if (sourceUrl.length() > 8) {
                        String realUrl = sourceUrl.substring(6, sourceUrl.length() - 2).trim();    // 去掉"<img"和"/>"两部分
                        if (realUrl.length() > 6) {
                            realUrl = realUrl.substring(5, realUrl.length() - 1).trim();    // 去掉" src=" "和" " "两部分

                            SingleEntity entity = new SingleEntity(str, textSize, textColor);
                            entity.setStr(realUrl);
                            entity.setIndex(start + 11);
                            entity.setTag(SingleEntity.TAG_VIDEO);
                            list.add(entity);
                        }
                    }
                }
            }
            if (end != str.length()) {
                SingleEntity entity = new SingleEntity(str, textSize, textColor);
                entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                entity.setStr(str.substring(end));
                entity.setIndex(end);
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 解析选择区域标签
     * @param sourceList
     * @param str
     * @return
     */
    public static java.util.List<SingleEntity> parseChooseAreaTag(java.util.List<SingleEntity> sourceList, String str, float textSize, int textColor) {
        if (TextUtils.isEmpty(str)) {
            return sourceList;
        }
        java.util.List<SingleEntity> list = new ArrayList<>();
        int end = 0;
        if (sourceList != null && sourceList.size() != 0) {
            list.addAll(sourceList);
            for (int i = 0; i < sourceList.size(); i++) {
                SingleEntity entity = sourceList.get(i);
                java.util.List<SingleEntity> childList = new ArrayList<>();
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getStr())) {
                        try {
                            SingleEntity newEntity = (SingleEntity) entity.clone();
                            if (entity.getStr().contains(CHOOSE_AREA_SYMBOL) || entity.getStr().contains(CHOOSE_AREA_SYMBOL_2)) {
                                Pattern pattern = Pattern.compile(REG_CHOOSE_AREA_SYMBOL, Pattern.DOTALL);
                                Matcher matcher = pattern.matcher(entity.getStr());
                                int start = 0;
                                end = 0;
                                while (matcher.find()) {
                                    start = matcher.start();

                                    if (start > end) {
                                        newEntity.setStr(entity.getStr().substring(end, start));
                                        newEntity.setIndex(newEntity.getIndex() + end);
                                        childList.add(newEntity);

                                        newEntity = (SingleEntity) newEntity.clone();

                                        android.util.Log.v(TAG, "parseChooseAreaTag ahead, str = " + entity.getStr());
                                    }

                                    end = matcher.end();
                                    int oldTag = newEntity.getTag();
                                    newEntity.setIndex(newEntity.getIndex() + start);
                                    newEntity.setTag(SingleEntity.TAG_USER_ANSWER_AREA_TEXT);
                                    newEntity.setStr("");
                                    childList.add(newEntity);
                                    newEntity = (SingleEntity) newEntity.clone();
                                    newEntity.setTag(oldTag);
                                }
                                android.util.Log.v(TAG, "parseChooseAreaTag after, end = " + end + ", entity.getStr().length = " + entity.getStr().length());
                                if (end != entity.getStr().length()) {
                                    newEntity.setStr(entity.getStr().substring(end));
                                    android.util.Log.v(TAG, "parseChooseAreaTag after22, str = " + newEntity.getStr());
                                    newEntity.setIndex(newEntity.getIndex() + end);
                                    childList.add(newEntity);
                                }
                                int index = list.indexOf(entity);
                                list.addAll(index, childList);
                                list.remove(entity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (str.contains(CHOOSE_AREA_SYMBOL) || str.contains(CHOOSE_AREA_SYMBOL_2)) {
                Pattern pattern = Pattern.compile(REG_CHOOSE_AREA_SYMBOL, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(str);
                int start = 0;
                end = 0;
                while (matcher.find()) {
                    start = matcher.start();

                    if (start > end) {
                        SingleEntity entity = new SingleEntity(str, textSize, textColor);
                        entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                        entity.setStr(str.substring(end, start));
                        entity.setIndex(end);
                        list.add(entity);
                    }
                    end = matcher.end();

                    SingleEntity entity = new SingleEntity(str, textSize, textColor);
                    entity.setIndex(start);
                    entity.setTag(SingleEntity.TAG_USER_ANSWER_AREA_TEXT);
                    entity.setStr("");
                    list.add(entity);
                }
            }
            if (end != str.length()) {
                SingleEntity entity = new SingleEntity(str, textSize, textColor);
                entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                entity.setStr(str.substring(end));
                entity.setIndex(end);
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 解析编辑区域标签
     * @param sourceList
     * @param str
     * @return
     */
    public static java.util.List<SingleEntity> parseEditAreaTag(java.util.List<SingleEntity> sourceList, String str, float textSize, int textColor) {
        if (TextUtils.isEmpty(str)) {
            return sourceList;
        }
        java.util.List<SingleEntity> list = new ArrayList<>();
        int end = 0;
        if (sourceList != null && sourceList.size() != 0) {
            list.addAll(sourceList);
            for (int i = 0; i < sourceList.size(); i++) {
                SingleEntity entity = sourceList.get(i);
                java.util.List<SingleEntity> childList = new ArrayList<>();
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getStr())) {
                        try {
                            SingleEntity newEntity = (SingleEntity) entity.clone();
                            if (entity.getStr().contains(EDIT_AREA_SYMBOL) || entity.getStr().contains(EDIT_AREA_SYMBOL_2)) {
                                Pattern pattern = Pattern.compile(REG_EDIT_AREA_SYMBOL, Pattern.DOTALL);
                                Matcher matcher = pattern.matcher(entity.getStr());
                                int start = 0;
                                end = 0;
                                while (matcher.find()) {
                                    start = matcher.start();

                                    if (start > end) {
                                        newEntity.setStr(entity.getStr().substring(end, start));
                                        newEntity.setIndex(newEntity.getIndex() + end);
                                        childList.add(newEntity);

                                        newEntity = (SingleEntity) newEntity.clone();

                                        android.util.Log.v(TAG, "parseEditAreaTag ahead, str = " + entity.getStr() + ", newEntity str = " + newEntity.getStr());
                                    }
                                    end = matcher.end();

                                    int oldTag = newEntity.getTag();
                                    newEntity.setIndex(newEntity.getIndex() + start);
                                    newEntity.setTag(SingleEntity.TAG_EDIT_TEXT);
                                    newEntity.setStr("");
                                    childList.add(newEntity);
                                    newEntity = (SingleEntity) newEntity.clone();
                                    newEntity.setTag(oldTag);
                                }
                                android.util.Log.v(TAG, "parseEditAreaTag after, end = " + end + ", entity.getStr().length = " + entity.getStr().length());
                                if (end != entity.getStr().length()) {
                                    newEntity.setStr(entity.getStr().substring(end));
                                    android.util.Log.v(TAG, "parseEditAreaTag after22, str = " + newEntity.getStr());
                                    newEntity.setIndex(newEntity.getIndex() + end);
                                    childList.add(newEntity);
                                }
                                int index = list.indexOf(entity);
                                list.addAll(index, childList);
                                list.remove(entity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (str.contains(EDIT_AREA_SYMBOL) || str.contains(EDIT_AREA_SYMBOL_2)) {
                Pattern pattern = Pattern.compile(REG_EDIT_AREA_SYMBOL, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(str);
                int start = 0;
                end = 0;
                while (matcher.find()) {
                    start = matcher.start();

                    if (start > end) {
                        SingleEntity entity = new SingleEntity(str, textSize, textColor);
                        entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                        entity.setStr(str.substring(end, start));
                        entity.setIndex(end);
                        list.add(entity);
                    }
                    end = matcher.end();

                    SingleEntity entity = new SingleEntity(str, textSize, textColor);
                    entity.setIndex(start);
                    entity.setTag(SingleEntity.TAG_EDIT_TEXT);
                    entity.setStr("");
                    list.add(entity);
                }
            }
            if (end != str.length()) {
                SingleEntity entity = new SingleEntity(str, textSize, textColor);
                entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                entity.setStr(str.substring(end));
                entity.setIndex(end);
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 解析选择区域标签
     * @param sourceList
     * @param str
     * @return
     */
    public static java.util.List<SingleEntity> parseNewLineTag(java.util.List<SingleEntity> sourceList, String str, float textSize, int textColor) {
        if (TextUtils.isEmpty(str)) {
            return sourceList;
        }
        java.util.List<SingleEntity> list = new ArrayList<>();
        int end = 0;
        if (sourceList != null && sourceList.size() != 0) {
            list.addAll(sourceList);
            for (int i = 0; i < sourceList.size(); i++) {
                SingleEntity entity = sourceList.get(i);
                java.util.List<SingleEntity> childList = new ArrayList<>();
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getStr())) {
                        try {
                            SingleEntity newEntity = (SingleEntity) entity.clone();
                            if (entity.getStr().contains(NEW_LINE)) {
                                Pattern pattern = Pattern.compile(NEW_LINE, Pattern.DOTALL);
                                Matcher matcher = pattern.matcher(entity.getStr());
                                int start = 0;
                                end = 0;
                                while (matcher.find()) {
                                    start = matcher.start();

                                    if (start > end) {
                                        newEntity.setStr(entity.getStr().substring(end, start));
                                        newEntity.setIndex(newEntity.getIndex() + end);
                                        childList.add(newEntity);

                                        newEntity = (SingleEntity) newEntity.clone();

                                        android.util.Log.v(TAG, "parseChooseAreaTag ahead, str = " + entity.getStr());
                                    }

                                    end = matcher.end();

                                    int oldTag = newEntity.getTag();
                                    newEntity.setIndex(newEntity.getIndex() + start);
                                    newEntity.setTag(SingleEntity.TAG_NEWLINE);
                                    newEntity.setStr("");
                                    childList.add(newEntity);
                                    newEntity = (SingleEntity) newEntity.clone();
                                    newEntity.setTag(oldTag);
                                }
                                android.util.Log.v(TAG, "parseChooseAreaTag after, end = " + end + ", entity.getStr().length = " + entity.getStr().length());
                                if (end != entity.getStr().length()) {
                                    newEntity.setStr(entity.getStr().substring(end));
                                    android.util.Log.v(TAG, "parseChooseAreaTag after22, str = " + newEntity.getStr());
                                    newEntity.setIndex(newEntity.getIndex() + end);
                                    childList.add(newEntity);
                                }
                                int index = list.indexOf(entity);
                                list.addAll(index, childList);
                                list.remove(entity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (str.contains(NEW_LINE)) {
                Pattern pattern = Pattern.compile(NEW_LINE, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(str);
                int start = 0;
                end = 0;
                while (matcher.find()) {
                    start = matcher.start();

                    if (start > end) {
                        SingleEntity entity = new SingleEntity(str, textSize, textColor);
                        entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                        entity.setStr(str.substring(end, start));
                        entity.setIndex(end);
                        list.add(entity);
                    }
                    end = matcher.end();

                    SingleEntity entity = new SingleEntity(str, textSize, textColor);
                    entity.setIndex(start);
                    entity.setTag(SingleEntity.TAG_NEWLINE);
                    entity.setStr("");
                    list.add(entity);
                }
            }
            if (end != str.length()) {
                SingleEntity entity = new SingleEntity(str, textSize, textColor);
                entity.setTag(SingleEntity.TAG_NORMAL_TEXT);
                entity.setStr(str.substring(end));
                entity.setIndex(end);
                list.add(entity);
            }
        }
        return list;
    }

    /*
     * 适用于选择题，点选题选项
     * firstSplit和secSplit配对括起来
     * 类似数据：%%A%%. bedroom\n%%B%%. bathroom\n%%C%%. classroom
     * */
    public static OptionInfo[] dealOption(String strOptions, char firstSplit, char secSplit) {
        ArrayList<SplitStringInfo> splitInfos = obtainSplitStringInfo(strOptions, firstSplit, secSplit);

        if (splitInfos == null || splitInfos.size() < 1) {
            return null;
        }
        int count = splitInfos.size();

        OptionInfo[] options = new OptionInfo[count];

        int lastPos;
        int endPos;
        for (int i = 0; i < count; i++) {
            options[i] = new OptionInfo();
            options[i].fillStr = splitInfos.get(i).content;

            lastPos = splitInfos.get(i).startPos;
            if (i + 1 < count) {
                endPos = splitInfos.get(i + 1).startPos;
            } else {
                endPos = strOptions.length();
            }
            options[i].dispStr = strOptions.substring(lastPos, endPos);
            options[i].dispStr = options[i].dispStr.replace(String.valueOf(new char[]{firstSplit, secSplit}), "");
            options[i].dispStr = digLastEnter(options[i].dispStr);
        }
        return options;
    }

    /*
     * 获取被字符firstSplit及secSplit括起来的字串,及其在源串的位置（包括分隔字符）
     * */
    public static ArrayList<SplitStringInfo> obtainSplitStringInfo(String source, char firstSplit, char secSplit) {
        if (source == null) {
            return null;
        }
        ArrayList<SplitStringInfo> list = new ArrayList<>();
        SplitStringInfo info;
        CharSequence charSeq = source;
        int len = charSeq.length();
        int start;
        char ch;
        int i = 0;
        while (i < len) {
            ch = charSeq.charAt(i);
            if (ch == firstSplit) {
                if (i < len - 2) {
                    ch = charSeq.charAt(++i);
                    if (ch == secSplit) {
                        start = ++i;
                        while ((i + 1) < len && !(charSeq.charAt(i) == firstSplit && charSeq.charAt(i + 1) == secSplit)) {
                            ++i;
                        }
                        if (i + 1 < len) {
                            info = new SplitStringInfo();
                            info.content = charSeq.subSequence(start, i).toString();
                            info.startPos = start - 2;
                            info.endPos = i + 1;
                            list.add(info);
                        }
                        ++i;
                    }
                }
            }
            ++i;
        }
        return list;
    }

    public static String digLastEnter(String source) {
        if (source == null) {
            return null;
        }
        int count = source.length();
        while (count > 0) {
            char ch = source.charAt(count - 1);
            if (ch == '\t' || ch == ' ') {
                --count;
            } else if (ch == '\n') {
                --count;
                break;
            } else {
                break;
            }
        }
        return source.substring(0, count);
    }

    /*
     * 适用于选择题，点选题选项
     * 类似数据：%%A%%. bedroom\n%%B%%. bathroom\n%%C%%. classroom
     * fillStr 为 A B C
     * dispStr 为 （A. bedroom）、（B. bathroom）、（C. classroom）
     * */
    public static class OptionInfo {
        public String fillStr;
        public String dispStr;

        @Override
        public String toString() {
            return "OptionInfo{" +
                    "fillStr='" + fillStr + '\'' +
                    ", dispStr='" + dispStr + '\'' +
                    '}';
        }
    }

    /**
     * 被包括字串在源串的信息：如#%_____%#
     */
    public static class SplitStringInfo {
        public String content;//被括起来的内容（横线部分）
        public int startPos;//分隔符开始位置（第一个#的位置）
        public int endPos;//分隔符结束位置（最后#的位置）
    }

    /**
     * 获取font str属性为property的值
     * 例如<font color="ff0000">中color值为ff0000
     *
     * @param fontStr
     * @param property
     * @return
     */
    public static String getFontTagPropertyValue(String fontStr, String property) {
        if (TextUtils.isEmpty(fontStr) || !fontStr.startsWith("<font") || !fontStr.endsWith(">") || TextUtils.isEmpty(property)) {
            return null;
        }
        String subStr = fontStr.substring("<font".length());
        if (subStr.contains(property + "=")) {
            int index = subStr.indexOf(property + "=");
            subStr = subStr.substring(index + (property + "=").length());
            if (subStr.startsWith("\"")) {
                subStr = subStr.substring("\"".length());
                if (subStr.contains("\"")) {
                    return subStr.substring(0, subStr.indexOf("\""));
                }
            } else if (subStr.startsWith("'")) {
                subStr = subStr.substring("'".length());
                if (subStr.contains("'")) {
                    return subStr.substring(0, subStr.indexOf("'"));
                }
            }
        }
        return null;
    }

    /**
     * 获取str中html标签tag（假如有这个标签），属性名为attributeName（假如有该属性名）的相关信息（
     *
     * @param str
     * @param tag
     * @param attributeName
     * @return arr 数组第一项为属性值，第二项为<tag的初始位置，第三项为<tag color="">的结束位置
     */
    public static String getHtmlTagAttributeValue(String str, String tag, String attributeName) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(tag) || TextUtils.isEmpty(attributeName)) {
            return null;
        }
        if (!str.contains(LEFT_ANGLE_BRACKET + tag)) {
            return "";
        }
        String leaveStr = str.substring(str.indexOf(LEFT_ANGLE_BRACKET + tag) + (LEFT_ANGLE_BRACKET + tag).length());
        if (!leaveStr.contains(attributeName + "=")) {
            return null;
        }
        leaveStr = leaveStr.substring(leaveStr.indexOf(attributeName + "=") + (attributeName + "=").length());
        if (leaveStr.startsWith("\"")) {
            leaveStr = leaveStr.substring(1);
            String attributeValue = leaveStr.substring(0, leaveStr.indexOf("\""));
            return attributeValue;
        } else if (leaveStr.startsWith("'")) {
            leaveStr = leaveStr.substring(1);
            String attributeValue = leaveStr.substring(0, leaveStr.indexOf("'"));
            return attributeValue;
        }
        return "";
    }

    /**
     * 获取形如         我们的的惹人多拉风<b>辣</b>斗辣<i>阿萨</i>德发了<b>邓弗<i>里</i>斯</b>阿斯顿发斯蒂芬     字符串对应的textStyles数组
     *
     * @return 结果为： 0 0 0 0 0 0 0 0 0-1 1 -1  0 0 -1 2 2 -1 0 0 0 -1 1 1 -1 3 -1  1 -1 0 0 0 0 0 0 0
     * 解释：   <b> </b> <i> </i> 标签对应的数字为-1，而被<b> </b>包围的字符数字为1 被<i> </i>包围的字符数字为2，同时被<b></b> <i></i>包围的字符数字结果为3（1|2）
     */
    public static int[] getFontTextStyles(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int[] results = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            results[i] = 0;
        }
        int index = 0;
        int result = SingleEntity.TEXT_STYLE_NORMAL;
        while (index < str.length()) {
            if (str.substring(index).startsWith("<b>")) {
                Arrays.fill(results, index, index + "<b>".length(), -1);
                index += "<b>".length();
                result |= SingleEntity.TEXT_STYLE_BOLD;
            } else if (str.substring(index).startsWith("</b>")) {
                Arrays.fill(results, index, index + "</b>".length(), -1);
                index += "</b>".length();
                result ^= SingleEntity.TEXT_STYLE_BOLD;
            } else if (str.substring(index).startsWith("<i>")) {
                Arrays.fill(results, index, index + "<i>".length(), -1);
                index += "<i>".length();
                result |= SingleEntity.TEXT_STYLE_ITALIC;
            } else if (str.substring(index).startsWith("</i>")) {
                Arrays.fill(results, index, index + "</i>".length(), -1);
                index += "</i>".length();
                result ^= SingleEntity.TEXT_STYLE_ITALIC;
            } else {
                results[index] = result;
                index++;
            }
        }
        return results;
    }

    public static boolean isEnglishLetter(String str) {
        if (TextUtils.isEmpty(str) || str.length() > 1) {
            return false;
        }
        char ch = str.charAt(0);
        if (ch >= '\u0041' && ch <= '\u005a' || ch >= '\u0061' && ch <= '\u007a') {
            return true;
        }
        return false;
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        return str.matches("\\s*");
    }


    private static final String PINYIN_SEPARATOR_SYMBOL = "\u000b\u0044";

    /**
     * 是否含有拼音
     *
     * @param str
     * @return
     */
    public static boolean isContainPinyin(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.contains(PINYIN_SEPARATOR_SYMBOL);
    }

    public static String dealWithPinyinInfo(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String result = "";
        result = "";
        if (str.contains(PINYIN_SEPARATOR_SYMBOL)) {
            String[] arr = str.split(PINYIN_SEPARATOR_SYMBOL);
            if (arr != null && arr.length != 0) {
                android.util.Log.v(TAG, "arr = " + Arrays.toString(arr) + ", length = " + arr.length);
                String text = "";
                String pinyin = "";
                for (int i = 0; i < arr.length; i++) {
                    String subStr = arr[i];
                    if (TextUtils.isEmpty(subStr)) {
                        continue;
                    }
                    if (i % 2 == 0) {    // 属于汉字
                        if (i != arr.length - 1) {   // 有对应的拼音
                            if (subStr.length() > 1) {
                                result += subStr.substring(0, subStr.length() - 1);
                            }
                            text = subStr.substring(subStr.length() - 1);
                        } else {                     // 没有对应的拼音
                            result += subStr;
                        }
                    } else {             // 属于拼音
                        pinyin = subStr;
                        result += "<pinyin text=\"" + text + "\" pinyin=\"" + pinyin + "\"/>";
                    }
                }
            }
        } else {
            result = str;
        }
        return result;
    }
}