package com.ztq.sdk.activity;

import android.app.Activity;
import android.os.Bundle;

import com.ztq.sdk.R;
import com.ztq.sdk.widget.SelectableTextView;

/**
 * Created by ztq on 2019/7/29.
 */
public class DemoActivity extends Activity {
    private SelectableTextView mSelectableTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mSelectableTv = findViewById(R.id.selectable_tv);
        mSelectableTv.setText("我我饿哦哦我哦哦我饿我哦房东偶发的搜房度搜啊欧迪芬辅导老师都爱发拉风的搜阿斯顿发了的撒讲道理两间房的酸辣粉领导领导拉收到了sad领导看两三点加适量的健康李开复达萨罗联发科大厦");
    }

    @Override
    public void onBackPressed() {
        mSelectableTv.getSelectedTextList();
        super.onBackPressed();
    }
}