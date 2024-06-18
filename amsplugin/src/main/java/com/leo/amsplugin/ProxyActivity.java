package com.leo.amsplugin;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// 代理Activity
public class ProxyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 代理类，不加载布局有问题吗？没问题
        setContentView(R.layout.activity_proxy);

        Log.e("leo", "onCreate: 我是代理的 Activity");
    }
}