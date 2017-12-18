package com.ixiangni.util;

import android.widget.ImageView;

import com.ixiangni.app.R;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class UserInfoUtil {

    public static void setGenderIcon(int gender, ImageView iv){

        if(gender==1){
            iv.setImageResource(R.mipmap.man);
        }else {
            iv.setImageResource(R.mipmap.woman);
        }

    }
}
