package com.ztq.sdk.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by ztq on 2019/8/13.
 * 监听软键盘弹起或者关闭的自定义RelativeLayout，不过有个缺点，如果软键盘内嵌在页面中，并不是从底部弹出，这个方法失效
 */
public class KeyboardLayout extends RelativeLayout {
    private final String TAG = "noahedu.KeyboardLayout";
    private KeyboardLayoutListener mListener;
    private boolean mIsKeyboardActive = false; //输入法是否激活
    private int mKeyboardHeight = 0;   // 输入法高度
    private KeyboardOnGlobalChangeListener mChangeListener;

    public KeyboardLayout(Context context) {
        this(context, null, 0);
    }

    public KeyboardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 通过视图树监听布局变化
        mChangeListener = new KeyboardOnGlobalChangeListener();
        getViewTreeObserver().addOnGlobalLayoutListener(mChangeListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        getViewTreeObserver().removeGlobalOnLayoutListener(mChangeListener);
        super.onDetachedFromWindow();
    }

    private class KeyboardOnGlobalChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {
        int mScreenHeight = 0;
        Rect mRect = new Rect();

        private int getScreenHeight() {
            if (mScreenHeight > 0) {
                return mScreenHeight;
            }
            mScreenHeight = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
            return mScreenHeight;
        }

        @Override
        public void onGlobalLayout() {
            // 获取当前页面窗口的显示范围
            getWindowVisibleDisplayFrame(mRect);

            int screenHeight = getScreenHeight(); //屏幕高度
            int keyboardHeight = screenHeight - mRect.bottom; // 输入法的高度
            boolean isActive = false;
            if (Math.abs(keyboardHeight) > screenHeight / 5) {
                isActive = true; // 超过屏幕五分之一则表示弹出了输入法
                mKeyboardHeight = keyboardHeight;
            }
            if (mIsKeyboardActive != isActive) {
                mIsKeyboardActive = isActive;
                if (mListener != null) {
                    mListener.onKeyboardStateChanged(isActive, keyboardHeight);
                }
            }
        }
    }

    public void setKeyboardListener(KeyboardLayoutListener listener) {
        mListener = listener;
    }

    public KeyboardLayoutListener getKeyboardListener() {
        return mListener;
    }

    public boolean isKeyboardActive() {
        return mIsKeyboardActive;
    }

    /**
     * 获取输入法高度
     *
     * @return
     */
    public int getKeyboardHeight() {
        return mKeyboardHeight;
    }

    public interface KeyboardLayoutListener {
        /**
         * @param isActive       输入法是否激活
         * @param keyboardHeight 输入法面板高度
         */
        void onKeyboardStateChanged(boolean isActive, int keyboardHeight);
    }
}