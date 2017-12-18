package com.ixiangni.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class LayouManagerHelper {



    public static RecyclerView.LayoutManager createLinearManager(Context context){
        LinearLayoutManager manager = new LinearLayoutManager(context);

        manager.setOrientation(LinearLayoutManager.VERTICAL);

        return manager;
    }
}
