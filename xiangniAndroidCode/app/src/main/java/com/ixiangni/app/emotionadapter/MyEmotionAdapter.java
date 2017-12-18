package com.ixiangni.app.emotionadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ixiangni.adapter.MyRvAdapter;
import com.ixiangni.adapter.MyRvViewHolder;
import com.ixiangni.app.R;
import com.ixiangni.bean.BuiedEmListBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class MyEmotionAdapter extends MyRvAdapter<BuiedEmListBean.DataBean.UserBrowListBean> {
    /**
     *
     * @param context
     * @param itemlayoutid  显示的布局的id
     */
    public MyEmotionAdapter(Context context, @NonNull int itemlayoutid) {
        super(context, itemlayoutid);
    }
    //    父类的替换数据的方法
    @Override
    public void replaceAll(List<BuiedEmListBean.DataBean.UserBrowListBean> newDataList) {
        super.replaceAll(newDataList);
    }
    //  父类向集合中添加数据的方法
    @Override
    public void addAll(List<BuiedEmListBean.DataBean.UserBrowListBean> newDataList) {
        super.addAll(newDataList);
    }

    @Override
    protected void bindItemView(MyRvViewHolder holder, int position, BuiedEmListBean.DataBean.UserBrowListBean data) {
        ImageView imageview =  holder.getView(R.id.zly_image);
        String url = data.getBrowinfo();
        Glide.with(context).load(url).error(R.drawable.ease_default_expression)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);
    }
}