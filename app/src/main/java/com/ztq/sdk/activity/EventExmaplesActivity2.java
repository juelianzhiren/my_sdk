package com.ztq.sdk.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ztq.sdk.R;
import com.ztq.sdk.widget.CustomListView;

public class EventExmaplesActivity2 extends BaseActivity {

    private String[] data = {"Apple", "Banana", "Orange", "Watermelon",
            "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango",
            "Apple", "Banana", "Orange", "Watermelon",
            "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_exmaples_2);
        showList();
    }

    private void showList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EventExmaplesActivity2.this, android.R.layout.simple_list_item_1, data);
        CustomListView listView = findViewById(R.id.demo_lv2);
        listView.setAdapter(adapter);
    }
}