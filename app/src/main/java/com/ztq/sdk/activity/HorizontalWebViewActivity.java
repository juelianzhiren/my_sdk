package com.ztq.sdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ztq.sdk.R;
import com.ztq.sdk.widget.HorizontalWebView;

/**
 * 横向的webview activity
 */
public class HorizontalWebViewActivity extends BaseActivity {
    private static final String TAG = "noahedu.HorizontalWebViewActivity";
    private Context mContext;
    private WebView mWebView;

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
        webSettings.setDomStorageEnabled(true);       //这句话必须保留。。否则无法播放优酷视频网页。。其他的可以

        String mUrl = "https://blog.csdn.net/a1018875550/article/details/53519081";
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
        });
        findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}