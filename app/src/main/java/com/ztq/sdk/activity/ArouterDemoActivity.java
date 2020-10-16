package com.ztq.sdk.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lizejun.demo.lib.base.RouterMap;
import com.lizejun.demo.lib.base.util.FragmentUtils;
import com.ztq.sdk.R;

public class ArouterDemoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arouter_demo);
        addFragment();
    }

    private void addFragment() {
        Fragment homeFragment = getHomeFragment();
        FragmentUtils.addFragment(this, homeFragment, R.id.fl_container);
    }

    private Fragment getHomeFragment() {
        return (Fragment) ARouter.getInstance().build(RouterMap.HOME_FRAGMENT).navigation();
    }
}