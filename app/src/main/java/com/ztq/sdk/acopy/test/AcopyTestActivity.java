package com.ztq.sdk.acopy.test;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ztq.sdk.R;
import com.ztq.sdk.activity.BaseActivity;

public class AcopyTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acopy_test);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoke(v);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invokeStatic(v);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invokeHide(v);
            }
        });
    }

    public void invoke(View view) {
        ProcessorCopy processor = new ProcessorCopy(this);
        processor.toast("你好");
    }

    public void invokeStatic(View view) {
        String text = ProcessorCopy.toast(this, "你好吗？？");
        ((TextView)view).setText(text);
    }

    public void invokeHide(View view) {
        ActivityCopy activityCopy = new ActivityCopy(this);
        boolean canStartActivityForResult = activityCopy.canStartActivityForResult();
        // 隐藏方法
//        boolean canStartActivityForResult = this.canStartActivityForResult();

        Toast.makeText(this, "canStartActivityForResult: " + canStartActivityForResult, Toast.LENGTH_SHORT).show();
    }
}