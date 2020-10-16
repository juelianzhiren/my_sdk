package com.edmodo.cropper.util;

import android.app.Activity;
import android.util.DisplayMetrics;

public class Utils {
    public static int getScreenWidth(Activity activity) {
        if (activity == null) {
            return 0;
        }
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

}