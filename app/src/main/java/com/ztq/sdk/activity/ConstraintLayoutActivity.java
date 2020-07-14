package com.ztq.sdk.activity;

import android.os.Bundle;
import android.widget.Button;

import com.ztq.sdk.R;

/**
 * ConstraintLayout demo的activity
 */
public class ConstraintLayoutActivity extends BaseActivity {
    private static final String TAG = "noahedu.ConstraintLayoutActivity";
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contraint_layout);

        btn = findViewById(R.id.btn1);
    }
}