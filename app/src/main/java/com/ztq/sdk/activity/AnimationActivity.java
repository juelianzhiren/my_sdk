package com.ztq.sdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ztq.sdk.R;
import com.ztq.sdk.adapter.OneDataSourceAdapter;
import com.ztq.sdk.helper.Rotate3dAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztq on 2019/11/23.
 */
public class AnimationActivity extends BaseActivity {
    private final String TAG = "noahedu.AnimationActivity";
    private Context mContext;
    private ImageView mIv;
    private ListView mListView;
    private MyAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        mContext = this;
        initViewAndVariables();
        addListener();
    }

    private void initViewAndVariables() {
        mIv = findViewById(R.id.animation_iv);
        Rotate3dAnimation animation = new Rotate3dAnimation(0, 360, 200, 200, 500, false);
        animation.setFillAfter(true);
        animation.setDuration(2000);
//        animation.setRepeatCount(-1);
        mIv.startAnimation(animation);

        mListView = findViewById(R.id.animation_lv);
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        List<String> list = new ArrayList<>();
        list.add("first item");
        list.add("second item");
        list.add("third item");
        list.add("forth item");
        list.add("fifth item");
        list.add("sixth item");
        mAdapter.addData(list);
    }

    private void addListener() {

    }

    private class MyAdapter extends OneDataSourceAdapter<String> {
        @Override
        public int getCount() {
            return getDataSource().size();
        }

        @Override
        public Object getItem(int position) {
            return getDataSource().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, null);
                viewHolder.nameTv = convertView.findViewById(R.id.item_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String str = getDataSource().get(position);
            viewHolder.nameTv.setText(str);
            return convertView;
        }

        private class ViewHolder {
            TextView nameTv;
        }
    }
}