package com.ixiangni.adapter;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/7/6 0006.
 */

public class MyRvViewHolder extends RecyclerView.ViewHolder {


    private SparseArray<View> views = new SparseArray<>();

    public MyRvViewHolder(View itemView) {
        super(itemView);
    }

    public void setOnitemClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }

    public View getView() {
        return itemView;
    }

    public void setOnItemChlidClickListener(int viewid, View.OnClickListener listener) {
        getView(viewid).setOnClickListener(listener);
    }


    public <T extends View> T getView(@IdRes int viewid) {

        View view = views.get(viewid);

        if (view == null) {
            view = itemView.findViewById(viewid);
            views.put(viewid, view);
        }

        return (T) view;
    }

    public void setText(int viewid, String text) {

        TextView tv = getView(viewid);
        tv.setText(text);
    }
}
