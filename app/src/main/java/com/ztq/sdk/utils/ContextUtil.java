package com.ztq.sdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * context工具
 */
public class ContextUtil {

    public static boolean isContextAlive(Context context) {
        if (context == null) {
            return false;
        }
        if (!(context instanceof Activity)) {
            return true;
        }
        Activity activity = (Activity)context;
        return !activity.isFinishing() && !activity.isDestroyed();
    }

    public static boolean isActivity(Context context) {
        if (context == null) {
            return false;
        }
        return context instanceof Activity;
    }

    public static Activity getActivity(Context context) {
        if (isActivity(context)) {
            return (Activity) context;
        }
        return null;
    }

    public static Intent getIntentOfActivity(Context context) {
        if (!isActivity(context)) {
            return null;
        }
        return ((Activity)context).getIntent();
    }

    public static void setResult(Context context, int resultCode) {
        if (isActivity(context)) {
            Activity activity = (Activity) context;
            activity.setResult(resultCode);
        }
    }

    public static void finish(Context context) {
        if (!isActivity(context)) {
            return;
        }
        try {
            ((Activity) context).finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isWindowFocus(Context context) {
        if (!isActivity(context)) {
            return false;
        }
        return ((Activity)context).hasWindowFocus();
    }
}
