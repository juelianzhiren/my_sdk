package com.ztq.sdk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ztq.sdk.R;
import com.ztq.sdk.log.Log;

import java.util.List;

/*
① 创建一个继承RecyclerView.Adapter<VH>的Adapter类
② 创建一个继承RecyclerView.ViewHolder的静态内部类
③ 在Adapter中实现3个方法：
   onCreateViewHolder()
   onBindViewHolder()
   getItemCount()
*/
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private static final String TAG = "noahedu.RecyclerViewAdapter";
    private Context mContext;
    private List<String> list;
    private View view;
    private boolean mFlag;

    private static final int TYPE_ITEM =0;     //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private boolean mIsShowFooter = false;

    //构造方法，传入数据
    public RecyclerViewAdapter(Context context, List<String> list, boolean flag) {
        this.mContext = context;
        this.list = list;
        this.mFlag = flag;
    }

    public void setIsShowFooter(boolean mIsShowFooter) {
        this.mIsShowFooter = mIsShowFooter;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //将数据和控件绑定
        holder.textView.setText(list.get(position));
        Log.v(TAG, "position = " + position + "; getCount = " + getItemCount() + "; mIsShowFooter = " + mIsShowFooter);
        if (position + 1 == getItemCount() && mIsShowFooter) {
            holder.footerLL.setVisibility(View.VISIBLE);
        } else {
            holder.footerLL.setVisibility(View.GONE);
        }
        ViewGroup.LayoutParams params = holder.textView.getLayoutParams();
        //假设有多种不同的类型
        int type = position % 3;
        //计算View的高度
        int height = com.ztq.sdk.utils.Utils.dp2px(mContext, 50);
        if (mFlag) {
            switch (type) {
                case 0:
                    height = com.ztq.sdk.utils.Utils.dp2px(mContext, 50);
                    break;
                case 1:
                    height = com.ztq.sdk.utils.Utils.dp2px(mContext, 50) + com.ztq.sdk.utils.Utils.dp2px(mContext, 10);
                    break;
                case 2:
                    height = com.ztq.sdk.utils.Utils.dp2px(mContext, 50);
                    break;
                default:
                    break;
            }
        }
        params.height = height;
//        holder.textView.setLayoutParams(params);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        //返回Item总条数
        return list.size();
    }

    //内部类，绑定控件
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout footerLL;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_recyclerview_tv);
            footerLL = (LinearLayout) itemView.findViewById(R.id.item_recyclerview_footer_ll);
        }
    }
}