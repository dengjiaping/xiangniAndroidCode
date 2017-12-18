package com.nevermore.oceans.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


/**
 *
 * author: XuNeverMore
 * create on 2017/5/16 0016
 * github https://github.com/XuNeverMore
 */


public class BottomTabLayout extends LinearLayout {
    public BottomTabLayout(Context context) {
        this(context, null);
    }

    public BottomTabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(HORIZONTAL);//水平方向

    }

    private static final String TAG = "BottomTabLayout";

    private void setListeners() {
        int childCount = getChildCount();
        Log.i(TAG, "setListeners: "+childCount);

        for (int i = 0; i < childCount; i++) {

            final int position = i;

            final View child = getChildAt(i);
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemTabClickListener != null) {

                        //防止选中的tab被重复点击
                        if(lastSelectedItemView!=null&&lastSelectedItemView==v){
                            return;
                        }
                        onItemTabClickListener.onItemTabClick(position, v);
                    }
                }
            });
        }
    }


    private OnItemTabClickListener onItemTabClickListener;

    public void setOnItemTabClickListener(OnItemTabClickListener onItemTabClickListener) {
        this.onItemTabClickListener = onItemTabClickListener;
        setListeners();
    }



    private View lastSelectedItemView;//上一个被选中的item

    public void selectItem(View itemView){

        //让上一个被选中的tab恢复原来状态
        if(lastSelectedItemView!=null){
            changeItemSelectState(lastSelectedItemView,false);
        }
        //选中itemView
        changeItemSelectState(itemView,true);
        //保存itemView 下次切换修改
        lastSelectedItemView = itemView;

    }


    public void selectItem(int position){
        if(position<getChildCount()){
            View childAt = getChildAt(position);
            selectItem(childAt);
        }
    }


    //改变tab 选中状态
    private void changeItemSelectState(View view,boolean selected){

        if(view instanceof BottomTabView){
            ((BottomTabView) view).setSelectState(selected);
        }else {
            view.setSelected(selected);
        }

    }


    public interface OnItemTabClickListener {

        void onItemTabClick(int position, View itemView);
    }
}
