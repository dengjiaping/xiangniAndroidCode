package com.ixiangni.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6 0006.
 */

public abstract class MyRvAdapter<T> extends RecyclerView.Adapter<MyRvViewHolder> {

    protected List<T> dataList;
    protected Context context;
    private boolean isMultiItemType = false;
    protected LayoutInflater inflater;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;

    private int itemlayoutid;
    protected SparseIntArray itemlaoutres;



    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public MyRvAdapter(Context context, @NonNull int itemlayoutid) {
        this.context = context;
        this.itemlayoutid = itemlayoutid;
        dataList = new ArrayList<T>();
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void addAll(List<T> newDataList){
        dataList.addAll(newDataList);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> newDataList){
        dataList.clear();
        addAll(newDataList);
    }

    public void remove(int position){
        dataList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public MyRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(inflater==null){
            inflater = LayoutInflater.from(context);
        }

        if(!isMultiItemType){
            View itemView = inflater.inflate(itemlayoutid, parent, false);
            return new MyRvViewHolder(itemView);
        }
        return new MyRvViewHolder(null);
    }

    public T getItem(int position){
        return dataList.get(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(MyRvViewHolder holder, int position) {
        View view = holder.getView();
        if(onItemClickListener!=null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null,view,position,position);
                }
            });
        }

        if(onItemLongClickListener!=null){
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(null,view,position,position);
                    return true;
                }
            });
        }
        bindItemView(holder,position,getItem(position));
    }

    protected abstract void bindItemView(MyRvViewHolder holder, int position,T data);


    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AdapterView.OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
