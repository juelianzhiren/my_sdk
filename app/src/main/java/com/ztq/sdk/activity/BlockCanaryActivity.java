package com.ztq.sdk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ztq.sdk.R;

/**
 * 使用BlockCanary检查页面ANR的逻辑
 */
public class BlockCanaryActivity extends BaseActivity {
    private static final String TAG = "noahedu.BlockCanaryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blockcanary);
        Button button = (Button) findViewById(R.id.blockcanary_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟一个长时间操作,产生ANR
                try {
                    Thread.sleep(15 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Toast toast = Toast.makeText(getApplicationContext(), "点击完成", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}