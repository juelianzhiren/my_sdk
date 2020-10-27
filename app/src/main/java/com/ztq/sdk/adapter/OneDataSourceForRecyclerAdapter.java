package com.ztq.sdk.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 和OneDataSourceAdapter对应的Adapter，前者是listview的基类adapter，后者是recyclerview的基类适配器类
 * @param <T>
 * @param <Y>
 */
public class OneDataSourceForRecyclerAdapter<T, Y extends BaseViewHolder> extends BaseQuickAdapter<T, BaseViewHolder> {
    public OneDataSourceForRecyclerAdapter(int layoutResId) {
        super(layoutResId);
        mData = new ArrayList<>();
    }

    /**
     * @description: 添加数据
     * @param data   数据为T
     * @return void
     */
    public void addData(T data) {
        mData.add(data);
        notifyDataSetChanged();
    }

    /**
     * @description: 添加数据
     * @param data   数据为List<T>
     * @return void
     */
    public void addData(List<T> data){
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * @description: 添加数据到索引位置
     * @param data   数据为T
     * @return void
     */
    public void addData(int index, T data){
        mData.add(index, data);
        notifyDataSetChanged();
    }

    /**
     * @description: 添加数据到索引位置
     * @param data   数据为List<T>
     * @return void
     */
    public void addData(int index, List<T> data){
        mData.addAll(index, data);
        notifyDataSetChanged();
    }

    /**
     * @description: 删除索引位置index的数据
     * @param index
     * @return void
     */
    public void remove(int index){
        mData.remove(index);
        notifyDataSetChanged();
    }

    /**
     * @description: 清空list数据
     * @return void
     */
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * @description: 获取数据源
     * @return
     * @return List<T>
     */
    public List<T> getDataSource() {
        return mData;
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {

    }
}