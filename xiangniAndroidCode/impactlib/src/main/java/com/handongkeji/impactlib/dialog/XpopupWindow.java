package com.handongkeji.impactlib.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class XpopupWindow extends PopupWindow {

    private View view;

    public XpopupWindow(Context context, View view) {
        super(context);
        initView(view);
    }

    private void initView(View view) {
        this.view = view;
        this.setContentView(view);
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public XpopupWindow(Context context, int layoutid){
        super(context);
        View view = LayoutInflater.from(context).inflate(layoutid, null);
        initView(view);
    }

    public void setOnClickListener(int viewid,View.OnClickListener listener){
        view.findViewById(viewid).setOnClickListener(listener);
    }

    public <T> T getView(int viewid){
        if(view==null){
            return null;
        }
       return (T)view.findViewById(viewid);
    }


}
