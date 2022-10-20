package com.ztq.sdk.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ztq.sdk.R;

public class EventExmaplesActivity extends BaseActivity {

    private String[] data = {"Apple", "Banana", "Orange", "Watermelon",
            "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango",
            "Apple", "Banana", "Orange", "Watermelon",
            "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_exmaples);
        showList();
    }

    private void showList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EventExmaplesActivity.this, android.R.layout.simple_list_item_1, data);
        ListView listView = findViewById(R.id.demo_lv);
        listView.setAdapter(adapter);
    }
}