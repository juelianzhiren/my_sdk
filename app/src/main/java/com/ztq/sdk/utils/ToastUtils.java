package com.ztq.sdk.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Toast 辅助类
 * <p>
 * 单例模式
 * <p>
 * 只会显示一个toast 提示
 */
public class ToastUtils {

    /**
     * toast 对象
     */
    private static Toast toast;

    private static Handler handler;

    private static boolean DEBUG_MODE = false;

    public static void setDebugMode(boolean debugMode) {
        DEBUG_MODE = debugMode;
    }

    /**
     * 显示自定义Toast
     *
     * @param str     自定义提示
     */
    public static void show(String str) {
        show(str, -1, -1, -1);
    }

    public static void show(String str, int gravity, int x, int y) {

        Context context = ApplicationGetter.get();
        if (str.isEmpty()) {
            return;
        }
        if (toast == null) {
            toast = new Toast(context);
        }
        toast.cancel();
        toast = Toast.makeText(context, Html.fromHtml(str), Toast.LENGTH_SHORT);
        LinearLayout layout = (LinearLayout) toast.getView();
        TextView textView = (TextView) layout.getChildAt(0);
//        Resources resources = context.getResources();
//        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
//            textView.setTextSize(15);
//        }else {
//            textView.setTextSize(20);
//        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        if (gravity >= 0) {
            toast.setGravity(gravity, x, y);
        }
        toast.show();
    }

    /**
     * 显示自定义Toast
     */
    public static void show(int resourceId) {
        show(ApplicationGetter.get().getString(resourceId));
    }

    /**
     * 显示自定义Toast
     *
     * @param context 上下文
     * @param str     自定义提示
     */
    public static void showLongToast(Context context, String str) {
        if (str.isEmpty()) {
            return;
        }
        if (toast == null) {
            toast = new Toast(context);
        }
        toast.cancel();
        toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * 显示自定义Toast
     *
     * @param context    上下文
     * @param resourceId 自定义提示
     */
    public static void showLongToast(Context context, int resourceId) {
        if (toast == null) {
            toast = new Toast(context);
        }
        toast.cancel();
        toast = Toast.makeText(context, context.getString(resourceId), Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * 显示网络错误Toast
     *
     * @param context 上下文
     */
    public static void showNetError(Context context) {
        if (toast == null) {
            toast = new Toast(context);
        }
        toast.cancel();
        toast = Toast.makeText(context, "网络无法连接,请稍后再试!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showDebug(String text) {
        if (DEBUG_MODE) {
            if (Looper.myLooper() != null) {
                Toast.makeText(ApplicationGetter.get(), text, Toast.LENGTH_SHORT).show();
            } else {
                if (handler == null) {
                    handler = new Handler(Looper.getMainLooper());
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showDebug(text);
                    }
                });
            }
        }
    }

    public static void cancel() {
        if (toast == null) {
            return;
        }
        toast.cancel();
    }
}
