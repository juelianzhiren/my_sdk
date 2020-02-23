package com.ztq.sdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ztq.sdk.R;
import com.ztq.sdk.entity.TimelineItem;
import com.ztq.sdk.utils.Utils;

public class TimelineAdapter extends OneDataSourceAdapter<TimelineItem> {
    private static final String TAG = "noahedu.TimelineAdapter";
    private Context mContext;

    public TimelineAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return getDataSource().size();
    }

    @Override
    public Object getItem(int i) {
        return getDataSource().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_timeline, null);
            viewHolder.totalView = convertView.findViewById(R.id.item_timeline_total_view);
            viewHolder.topLineTv = convertView.findViewById(R.id.item_timeline_top_line_tv);
            viewHolder.circleIv = convertView.findViewById(R.id.item_timeline_circle_iv);
            viewHolder.remainingLineTv = convertView.findViewById(R.id.item_timeline_remaining_line_tv);
            viewHolder.iv = convertView.findViewById(R.id.item_timeline_iv);
            viewHolder.contentTv = convertView.findViewById(R.id.item_timeline_content_tv);
            viewHolder.dateTv = convertView.findViewById(R.id.item_timeline_date_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TimelineItem item = getDataSource().get(i);
        if (item != null) {
            if (i == 0) {
                viewHolder.topLineTv.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.topLineTv.setVisibility(View.VISIBLE);
            }
            if (i == getDataSource().size() - 1) {
                viewHolder.remainingLineTv.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.remainingLineTv.setVisibility(View.VISIBLE);
            }
            viewHolder.contentTv.setText(Utils.getNullOrNil(item.getContent()));
            viewHolder.dateTv.setText(Utils.getNullOrNil(item.getDateInfo()));
            if (Utils.isNullOrNil(item.getImg())) {
                viewHolder.iv.setVisibility(View.GONE);
            } else {
                viewHolder.iv.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(item.getImg()).into(viewHolder.iv);
            }
        }
        return convertView;
    }

    private class ViewHolder{
        View totalView;
        TextView topLineTv;
        ImageView circleIv;
        TextView remainingLineTv;
        TextView contentTv;
        TextView dateTv;
        ImageView iv;
    }
}