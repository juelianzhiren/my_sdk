package com.ztq.sdk.utils;


import com.ztq.sdk.entity.SingleEntity;

import java.util.List;

public class QuestionUtil {
    public static boolean isContainPinyinItem(List<SingleEntity> list) {
        if (list == null || list.size() == 0) {
            return false;
        }
        for (SingleEntity entity : list) {
            if (entity != null && entity.getTag() == SingleEntity.TAG_PINYIN) {
                return true;
            }
        }
        return false;
    }
}