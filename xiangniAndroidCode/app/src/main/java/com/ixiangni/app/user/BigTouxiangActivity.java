package com.ixiangni.app.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;

public class BigTouxiangActivity extends BaseActivity {
    private ImageView ivBigPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_touxiang);
        Intent intent = getIntent();
        String userpic = intent.getStringExtra("touxiang");

        ivBigPic= (ImageView) findViewById(R.id.big_touxiang);
//        GlideUtil.loadRoundImage(this, userpic, ivBigPic, R.mipmap.touxiangmoren);
        loadRoundImage(userpic, ivBigPic, R.mipmap.touxiangmoren);




        ivBigPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigTouxiangActivity.this.finish();
            }
        });
    }


    protected void loadRoundImage(String path, ImageView iv, int errorid) {
        if (CommonUtils.isStringNull(path)) {
            iv.setImageResource(errorid);
        } else {
            Glide.with(this).load(path).asBitmap().error(errorid).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                    //是否图圆化
                    drawable.setCircular(false);
                    iv.setImageDrawable(drawable);
                }
            });
        }

    }
}
