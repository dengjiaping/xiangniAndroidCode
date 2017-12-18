package com.ixiangni.adapter;

import com.ixiangni.bean.FriendListBean;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class NickCompator implements Comparator<FriendListBean.DataBean> {
    @Override
    public int compare(FriendListBean.DataBean o1, FriendListBean.DataBean o2) {

        String l1 = o1.getFirstLetter();
        String l2 = o2.getFirstLetter();
        String jin = "#";
        if (jin.equals(l1)) {
            if (jin.equals(l2)) {
                return 0;
            } else {
                return 1;
            }


        } else {
            if (jin.equals(l2)) {

                return -1;
            } else {
                return o1.getFirstLetter().compareTo(o2.getFirstLetter());

            }
        }

    }
}
