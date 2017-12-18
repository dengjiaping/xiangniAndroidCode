package com.ixiangni.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.handongkeji.impactlib.util.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 多种item adapter
 *
 * @ClassName:MultiItemAdapter
 * @PackageName:com.xiaoweiqiye.adapter
 * @Create On 2017/5/9 0009   14:52
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/5/9 0009 handongkeji All rights reserved.
 */

public abstract class MultiItemAdapter<T> extends BaseAdapter {


    protected List<T> mList;
    protected Context mContext;
    protected SparseIntArray types = new SparseIntArray();
    protected LayoutInflater mInflater;

    public MultiItemAdapter(Context mContext) {
        this(mContext,null);
    }

    public List<T> getDatas(){
        return mList;
    }

    public void addTypeLayoutRes(int type, @LayoutRes int itemLayoutid) {
        types.put(type, itemLayoutid);
    }


    public void remove(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    public void replaceAll (List<T> dataList){
        mList.clear();
        mList.addAll(dataList);
        notifyDataSetChanged();
    }


    public MultiItemAdapter(Context mContext, List<T> dataList) {
        this.mContext = mContext;
        if (mContext != null) {
            mInflater = LayoutInflater.from(mContext);
        }
        if (dataList == null) {
            this.mList = new ArrayList<>();
        } else {
            this.mList = dataList;
        }
        initItemLayout();
    }

    public Context getContext() {
        return mContext;
    }


    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public abstract int getDataType(T t);

    @Override
    public int getItemViewType(int position) {

        return getDataType(mList.get(position));
    }

    @Override
    public int getViewTypeCount() {
        return types.size();
    }

    public void addAll(List<T> dataList) {
        mList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(dataSizeChange!=null){
            dataSizeChange.onSize(mList.size());
        }
        return mList.size();
    }

    public interface OnDataSizeChange{
        void onSize(int size);
    }

    private OnDataSizeChange dataSizeChange;

    public OnDataSizeChange getDataSizeChange() {
        return dataSizeChange;
    }

    public void setDataSizeChange(OnDataSizeChange dataSizeChange) {
        this.dataSizeChange = dataSizeChange;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHelper helper;

        if (convertView == null) {
            //type
            int itemViewType = getItemViewType(position);
            //layoutid
            int layoutid = types.get(itemViewType);
            convertView = mInflater.inflate(layoutid, parent, false);
            helper = new ViewHelper(convertView);
            convertView.setTag(helper);
        } else {
            helper = (ViewHelper) convertView.getTag();
        }

        bindView(position, mList.get(position), helper, parent);

        return convertView;
    }

    protected abstract void bindView(int position, T t, ViewHelper helper, ViewGroup parent);

    protected abstract void initItemLayout();

}
