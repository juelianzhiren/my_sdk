package com.ztq.sdk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztq.sdk.R;
import com.ztq.sdk.utils.Utils;

/**
 * Created by ztq on 2019/10/24.
 */
public class GridviewAdapter extends OneDataSourceAdapter<String> {
    private final String TAG = "noahedu.GridviewAdapter";
    private Context mContext;
    private int mPossition0Count = 0;

    public GridviewAdapter(Context mContext){
        this.mContext = mContext;
    }

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
        Log.v(TAG, "getView, position = " + position + "; childCount = " + parent.getChildCount());
        if (position == 0) {
            mPossition0Count++;
        }
        if (position == 0 && parent.getChildCount() == 0 && mPossition0Count != 1) {
            return convertView;
        }
        Log.v(TAG, "after getView, position = " + position + "; childCount = " + parent.getChildCount());
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, null);
            viewHolder.tv = convertView.findViewById(R.id.item_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String str = getDataSource().get(position);
        viewHolder.tv.setText(Utils.getNullOrNil(str));
        return convertView;
    }

    private class ViewHolder {
        TextView tv;
    }
}