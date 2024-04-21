package com.ztq.sdk.inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ztq.sdk.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    int i;


    int j;


    @InjectView(R.id.tv)
    private TextView tv;

    @InjectView(R.id.btn)
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        InjectUtils.injectView(this);
        InjectUtils.injectEvent(this);

        tv.setText("lance真tm帅！！！");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<UserParcelable> userParcelableList = new ArrayList<>();

                userParcelableList.add(new UserParcelable("Jett"));
                Intent intent = new Intent(MainActivity.this, SecondActivity.class)
                        .putExtra("name", "Lance")
                        .putExtra("attr","帅")
                        .putExtra("array", new int[]{1, 2, 3, 4, 5, 6})
                        .putExtra("userParcelable", new UserParcelable("Lance"))
                        .putExtra("userParcelables", new UserParcelable[]{new UserParcelable("Alvin")})
                        .putExtra("users",new UserSerializable[]{new UserSerializable("Jett")})
                        .putExtra("strs",new String[]{"1","2"})
                        .putParcelableArrayListExtra("userParcelableList", userParcelableList);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.btn1, R.id.btn2})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Log.i(TAG, "click: 按钮1");
                break;
            case R.id.btn2:
                Log.i(TAG, "click: 按钮2");
                break;
        }
    }


    @OnLongClick({R.id.btn1, R.id.btn2})
    public boolean longClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Log.i(TAG, "longClick: 按钮1");
                break;
            case R.id.btn2:
                Log.i(TAG, "longClick: 按钮2");
                break;
        }
        return false;
    }
}