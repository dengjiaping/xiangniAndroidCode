package com.ixiangni.adapter;

import android.content.Context;

import com.ixiangni.app.R;
import com.ixiangni.bean.LikeBean;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class LikeAdapter extends QuickAdapter<LikeBean> {


    public LikeAdapter(Context context, List<LikeBean> data) {
        super(context, R.layout.item_like, data);
    }

    @Override
    public void replaceAll(List<LikeBean> elem) {
        super.replaceAll(elem);

    }

    @Override
    protected void convert(BaseAdapterHelper helper, LikeBean likeBean) {
        String usernick = likeBean.getUsernick();


        if (helper.getPosition() != data.size() - 1) {
            usernick += "、";
        }

        if (helper.getPosition() == data.size() - 1) {
            //            usernick+="觉得很赞";
            usernick += "";
        }


//                if (data.size() <= 20) {
//                    if (helper.getPosition() != data.size() - 1) {
//                        usernick += "、";
//                    }
//
//                    if (helper.getPosition() == data.size() - 1) {
//                        //            usernick+="觉得很赞";
//                        usernick += "";
//                    }
//
//
//                } else {
//                    if (helper.getPosition() != 19) {
//                        usernick += "、";
//                    }
//
//
//                    if (helper.getPosition() == 19) {
//                        //            usernick+="觉得很赞";
//                        usernick += " ";
//                    }
//                }


        helper.setText(R.id.tv_like, usernick);
    }
}
