package com.ztq.sdk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.ztq.sdk.R;
import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.utils.Utils;

import me.jessyan.autosize.internal.CancelAdapt;

public class WebViewActivity extends BaseActivity implements CancelAdapt {
    private static final String TAG = "noahedu.WebViewActivity";
    private Context mContext;
    private WebView mWebView;
    private static int webviewContentWidth = 0;
    private Mobile mMobile;

    private class Mobile {
        @JavascriptInterface
        public void onGetWebContentHeight() {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        findViews();
        init();
        addListener();
    }

    private void findViews() {
        mWebView = findViewById(R.id.webview);
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
        mWebView.setSaveEnabled(true);
        mMobile = new Mobile();
        mWebView.addJavascriptInterface(mMobile, "HTMLOUT");
        webSettings.setDomStorageEnabled(true);       //这句话必须保留。。否则无法播放优酷视频网页。。其他的可以
//        String mUrl = "https://blog.csdn.net/a1018875550/article/details/53519081";
//        mWebView.loadUrl(mUrl);

        String url = "file:///android_asset/" + "a.html";
        mWebView.loadUrl(url);
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
        });
        mWebView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.v(TAG, "onGlobalLayout, width = " + mWebView.getWidth() + "; mearsuredWidth = " + mWebView.getMeasuredWidth() + "; height = " + mWebView.getHeight() + "; measuredHeight = " + mWebView.getMeasuredHeight());
            }
        });
        findViewById(R.id.webview_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mWebView.getLayoutParams();
                int a = mWebView.getContentHeight();
                float scale = mWebView.getScale();
                params.height = (int)(a * scale);
                mWebView.setLayoutParams(params);

                MyHandlerThread.postToMainThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/webview.png";
                        boolean flag = Utils.saveWebviewContentToImage(mContext, mWebView, path);
                        Log.v(TAG, "flag = " + flag);
                        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                        mWebView.setLayoutParams(params);
                    }
                }, 500);
            }
        });

        findViewById(R.id.webview_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/webview_h.png";
                mWebView.measure(0, 0);
                final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mWebView.getLayoutParams();
                params.width = mWebView.getMeasuredWidth();
                params.height = mWebView.getMeasuredHeight();
                mWebView.setLayoutParams(params);

                MyHandlerThread.postToMainThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean flag = Utils.saveWebviewContentToImage(mContext, mWebView, path);
                        Log.v(TAG, "flag = " + flag);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mWebView.getLayoutParams();
                        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        mWebView.setLayoutParams(params);
                    }
                }, 500);
            }
        });

        findViewById(R.id.webview_jump_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HorizontalWebViewActivity.class);
                startActivity(intent);
            }
        });
    }
}