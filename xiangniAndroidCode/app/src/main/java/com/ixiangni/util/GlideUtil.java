package com.ixiangni.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.handongkeji.utils.CommonUtils;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class GlideUtil {

    public static void loadRoundImage(Context context, String url, ImageView iv){
        Glide.with(context).load(url).asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedBitmapDrawable.setCircular(true);
                        iv.setImageDrawable(roundedBitmapDrawable);
                    }
                });
    }
    public static void loadRoundImage(Context context,String url,ImageView iv,int placeholderresid){
        if(CommonUtils.isStringNull(url)){
            iv.setImageResource(placeholderresid);
            return;
        }
        Glide.with(context).load(url).asBitmap()
                .placeholder(placeholderresid)
                .error(placeholderresid)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedBitmapDrawable.setCircular(true);
                        iv.setImageDrawable(roundedBitmapDrawable);
                    }
                });
    }
}
