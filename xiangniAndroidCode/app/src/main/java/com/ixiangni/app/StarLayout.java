package com.ixiangni.app;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/8/7 0007.
 */

public class StarLayout extends LinearLayout {
    public StarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    private int margin = dp2px(5);//间距5dp

    private static final String TAG = "StarLayout";
    public void setStarCount(int count) {
        removeAllViews();



        for (int i = 0; i < count; i++) {

            LinearLayout.LayoutParams params = new LayoutParams(getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int size = dp2px(16);
            params.width=  size;
            params.height=  size;
            Log.i(TAG, "margin: "+margin);
            ImageView iv = new ImageView(getContext());
            iv.setImageResource(R.mipmap.shixing);
            if (i < 4) {
                params.setMargins(0, 0, margin, 0);
            } else {
                params.setMargins(0, 0, 0, 0);
            }

            addView(iv, params);
        }
        if (count < 5) {
            for (int i = 0; i < 5 - count; i++) {

                LinearLayout.LayoutParams params = new LayoutParams(getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                int size = dp2px(16);
                params.width=  size;
                params.height=  size;
                Log.i(TAG, "margin: "+margin);

                ImageView iv = new ImageView(getContext());
                iv.setImageResource(R.mipmap.kongxin);
                if (i + count < 4) {
                    params.setMargins(0, 0, margin, 0);
                } else {
                    params.setMargins(0, 0, 0, 0);
                }

                addView(iv, params);
            }
        }
    }

    private int dp2px(float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getContext().getResources().getDisplayMetrics());
    }
}
