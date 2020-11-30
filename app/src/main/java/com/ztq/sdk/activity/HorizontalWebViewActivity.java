package com.ztq.sdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ztq.sdk.R;
import com.ztq.sdk.utils.Utils;

/**
 * 横向的webview activity
 */
public class HorizontalWebViewActivity extends BaseActivity {
    private static final String TAG = "noahedu.HorizontalWebViewActivity";
    private Context mContext;
    private WebView mWebView;
    private ScrollView mLL;
    private int mLLHeight;
    private int mLLWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_webview);
        findViews();
        init();
        addListener();
    }

    private void findViews() {
        mWebView = findViewById(R.id.webview);
        mLL = findViewById(R.id.horizontal_webview_ll);
    }

    private void init() {
        mContext = this;
        WebSettings webSettings = mWebView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setTextZoom(100);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webSettings.setJavaScriptEnabled(true);

        String mUrl = "https://www.cnblogs.com/cx709452428/p/6861709.html";
        mWebView.loadUrl(mUrl);
    }

    private void addListener() {
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.v(TAG, "onReceivedError1");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.v(TAG, "onReceivedError2");
            }
        });
        findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLL.setPivotX(0);
                mLL.setPivotY(0);
                mLL.setRotation(-90);

                mLL.setTranslationY(mLLWidth);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLL.getLayoutParams();
                params.height = mLLWidth;
                mLL.setLayoutParams(params);

//                mWebView.setPivotX(0);
//                mWebView.setPivotY(0);
//                mWebView.setRotation(-90);
//
//                mWebView.setTranslationY(Utils.dp2px(mContext, 500));
            }
        });

//        mLL.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Log.v(TAG, "mLL, width = " + mLL.getWidth() + "; height = " + mLL.getHeight());
//            }
//        });
        mWebView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.v(TAG, "mWebView, width = " + mWebView.getWidth() + "; height = " + mWebView.getHeight());
            }
        });
        mLL.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.v(TAG, "mLL, width = " + mLL.getWidth() + "; height = " + mLL.getHeight());
                mLLHeight = mLL.getHeight();
                mLLWidth = mLL.getWidth();
            }
        });
    }
}