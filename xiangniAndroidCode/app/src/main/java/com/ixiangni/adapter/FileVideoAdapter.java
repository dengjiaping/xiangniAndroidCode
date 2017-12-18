package com.ixiangni.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.handongkeji.utils.DateUtil;
import com.ixiangni.app.R;
import com.ixiangni.bean.FileListBean;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class FileVideoAdapter  extends MyRvAdapter<FileListBean.DataBean>{

    public FileVideoAdapter(Context context, @NonNull int itemlayoutid) {
        super(context, itemlayoutid);
    }

    @Override
    protected void bindItemView(MyRvViewHolder holder, int position, FileListBean.DataBean data) {
        Glide.with(context)
                .load(data.getMediapic())
                .into((ImageView) holder.getView(R.id.iv_head));
        long createtime = data.getCreatetime();//上传时间
        String ymd = DateUtil.getYmd(createtime);
        holder.setText(R.id.tv_time,ymd);
    }
}
