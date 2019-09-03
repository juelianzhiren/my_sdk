package com.ztq.sdk.utils;

import android.annotation.TargetApi;

/**
 * Created by ztq on 2018/8/31.
 */
public class CharacterUtils {
    public CharacterUtils() {
    }

    @TargetApi(19)
    public static boolean isChineseByBlock(char var0) {
        Character.UnicodeBlock var1 = Character.UnicodeBlock.of(var0);
        return var1 == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || var1 == Character
                .UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || var1 == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || var1 == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C || var1 == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D || var1 == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || var1 == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT;
    }

    @TargetApi(19)
    public static boolean isChinesePunctuation(char var0) {
        Character.UnicodeBlock var1 = Character.UnicodeBlock.of(var0);
        return var1 == Character.UnicodeBlock.GENERAL_PUNCTUATION || var1 == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || var1 == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || var1 == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS || var1 == Character.UnicodeBlock.VERTICAL_FORMS;
    }
}