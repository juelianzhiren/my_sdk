package com.ztq.sdk.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.ztq.sdk.R;
import com.ztq.sdk.jni.JniInterface;

/**
 * ndk demo 的actiivty
 */
public class NdkDemoActivity extends BaseActivity {
    private static final String TAG = "noahedu.NdkDemoActivity";
    private TextView mTv;
    private JniInterface mJniInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_demo);

        mJniInterface = new JniInterface();
        mTv = findViewById(R.id.jni_demo_tv);
        mTv.setText(mJniInterface.get());
        mJniInterface.set("我哦我我我我");
    }
}