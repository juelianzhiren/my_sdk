package com.leo.amsplugin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

// 启动未注册的Activity
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HookUtil.hookAMS();
        HookUtil.hookHandler();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动插件的Activity
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.leo.amsplugin", "com.leo.amsplugin.UnregisteredActivity"));

//                intent.setComponent(new ComponentName("com.leo.amsplugin", "com.leo.amsplugin.ProxyActivity"));
                startActivity(intent);
            }
        });
    }
}