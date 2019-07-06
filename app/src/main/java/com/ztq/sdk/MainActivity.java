package com.ztq.sdk;

import android.app.Activity;
import android.os.Bundle;

import com.ztq.sdk.utils.Utils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.showToast(this, "弹出toast");
    }
}