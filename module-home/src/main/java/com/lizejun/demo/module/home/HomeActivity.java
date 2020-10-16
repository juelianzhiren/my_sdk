package com.lizejun.demo.module.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.demo.lizejun.module.home.R;
import com.lizejun.demo.lib.base.util.FragmentUtils;

public class HomeActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addFragment();
    }

    private void addFragment() {
        FragmentUtils.addFragment(this, new HomeFragment(), R.id.fl_container);
    }
}