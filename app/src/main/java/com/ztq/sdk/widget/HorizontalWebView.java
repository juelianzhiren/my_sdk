package com.ztq.sdk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.ztq.sdk.log.Log;

public class HorizontalWebView extends WebView {
    private static final String TAG = "noahedu.HorizontalWebView";
    private Context mContext;
    private boolean topDown = true;

    public HorizontalWebView(Context context) {
        this(context, null);
    }

    public HorizontalWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(topDown){
            Log.v(TAG, "height = " + getHeight() + "; width = " + getWidth());
            canvas.translate(getHeight(), 0);
            canvas.rotate(90);
        } else {
            canvas.translate(0, getWidth());
            canvas.rotate(90);
        }
//        canvas.clipRect(0, 0, getWidth(), getHeight(), android.graphics.Region.Op.REPLACE);
        super.onDraw(canvas);
    }
}