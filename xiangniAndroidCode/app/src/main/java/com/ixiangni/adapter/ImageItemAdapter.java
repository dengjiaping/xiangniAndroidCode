//package com.ixiangni.adapter;
//
//import android.content.Context;
//import android.support.annotation.LayoutRes;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.handongkeji.utils.ImageItem;
//import com.ixiangni.app.R;
//
///**
// * Created by Administrator on 2017/6/29 0029.
// */
//
//public class ImageItemAdapter extends BaseQuickAdapter<ImageItem,BaseViewHolder> {
//    public ImageItemAdapter(Context context) {
//        super(R.layout.item_label);
//        this.mContext = context;
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, com.handongkeji.utils.ImageItem item) {
//        Glide.with(mContext)
//                .load(item.getImagePath())
//                .into((ImageView) helper.getView(R.id.iv_image));
//        helper.addOnClickListener(R.id.iv_image);
//    }
//}
