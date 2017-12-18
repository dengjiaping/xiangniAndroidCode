package com.ixiangni.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ixiangni.app.R;
import com.ixiangni.bean.CircleListBean;
import com.ixiangni.bean.CircleListBean.DataBean.PicListBean;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class CellPictureAdapter extends QuickAdapter<PicListBean> {

    public CellPictureAdapter(Context context) {
        super(context, R.layout.item_pic);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, PicListBean picListBean) {
        ImageView iv=helper.getView(R.id.iv_pic);
        Glide.with(context).load(picListBean.getInserturl()).into(iv);
    }
}
