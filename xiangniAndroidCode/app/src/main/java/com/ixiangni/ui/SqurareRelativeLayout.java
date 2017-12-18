package com.ixiangni.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class SqurareRelativeLayout extends FrameLayout {
    public SqurareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
//                getDefaultSize(0, heightMeasureSpec));
//
//        int childWidthSize = getMeasuredWidth();
//        // 高度和宽度一样
//        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(
//                childWidthSize, MeasureSpec.EXACTLY);
        int size = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.EXACTLY);
        super.onMeasure(size, size);

    }
}
