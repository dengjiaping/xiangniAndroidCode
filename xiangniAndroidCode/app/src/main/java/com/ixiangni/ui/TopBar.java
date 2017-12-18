package com.ixiangni.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixiangni.app.R;

/**
 *
 * @ClassName:TopBar

 * @PackageName:com.ixiangni.ui

 * @Create On 2017/6/16 0016   13:19

 * @Site:http://www.handongkeji.com

 * @author:xuchuanting

 * @Copyrights 2017/6/16 0016 handongkeji All rights reserved.
 */


public class TopBar extends FrameLayout{

    private final TextView tvCenter;
    private final TextView tvRight;
    private final ImageView ivFinish;

    public TopBar(@NonNull  Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.top_bar_layout,this);
        tvCenter = (TextView) findViewById(R.id.tv_center);
        tvRight = (TextView) findViewById(R.id.tv_right);
        ivFinish = (ImageView) findViewById(R.id.iv_finish);

        final Activity activity = (Activity) context;


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        String centerText = typedArray.getString(R.styleable.TopBar_centerText);


        boolean isHideBack = typedArray.getBoolean(R.styleable.TopBar_hideBack, false);

        if(!isHideBack){
            //点击返回关闭当前Activity
            ivFinish.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });
        }else {//隐藏返回键
            ivFinish.setVisibility(GONE);
        }

        if(!TextUtils.isEmpty(centerText)){
            tvCenter.setText(centerText);
        }
        String rightText = typedArray.getString(R.styleable.TopBar_rightText);
        if(!TextUtils.isEmpty(rightText)){
            tvRight.setText(rightText);
        }
        typedArray.recycle();

    }

    public void setRightText(String text){
        tvRight.setText(text);
    }

    public void setCenterText(String text){
        tvCenter.setText(text);
    }

    public void setOnRightClickListener(OnClickListener listener){
        tvRight.setOnClickListener(listener);
    }

    public void setOnLeftClickListener(OnClickListener listener){
        ivFinish.setOnClickListener(listener);
    }
}
