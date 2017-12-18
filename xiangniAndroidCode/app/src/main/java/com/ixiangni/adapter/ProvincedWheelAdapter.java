package com.ixiangni.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ixiangni.app.R;
import com.ixiangni.bean.BenDiBean;
import com.ixiangni.ui.wheeladapter.WheelViewAdapter;


/**
 * Created by Administrator on 2016/11/29.
 */

public class ProvincedWheelAdapter implements WheelViewAdapter {
    private Context context;
    private BenDiBean allarea;
    public ProvincedWheelAdapter(BenDiBean allarea, Context context){
        this.context = context;
        this.allarea =allarea;
    }
    @Override
    public int getItemsCount() {
        if(allarea==null){
            return 0;
        }
        return allarea.getData().size();
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.list_litem,null);
        ((TextView)view.findViewById(R.id.listite)).setText(allarea.getData().get(index).getAreaName());
        return view;
    }

    @Override
    public View getEmptyItem(View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
}
