package com.ixiangni.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.handongkeji.utils.DateUtil;
import com.ixiangni.app.R;
import com.ixiangni.bean.FileListBean;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class FileWordAdapter extends MyRvAdapter<FileListBean.DataBean> {

    public FileWordAdapter(Context context, @NonNull int itemlayoutid) {
        super(context, itemlayoutid);
    }

    @Override
    protected void bindItemView(MyRvViewHolder holder, int position, FileListBean.DataBean data) {
        if (!StringUtils.isEmpty(data.getTextcotent())){
            holder.setText(R.id.tv_content,data.getTextcotent());
        }else {
            holder.setText(R.id.tv_content,"");
        }

        long createtime = data.getCreatetime();//上传时间
        String ymd = DateUtil.getYmd(createtime);
        holder.setText(R.id.tv_create_time, ymd);
    }
}