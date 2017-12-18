package com.ixiangni.adapter;

import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.handongkeji.adapter.QPagerAdapter;
import com.handongkeji.interfaces.Stringable;
import com.ixiangni.app.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class ImagePagerAdapter<T extends Stringable> extends QPagerAdapter<T> {


    public ImagePagerAdapter(List<T> dataList) {
        super(dataList);
    }

    @Override
    public View getView(LayoutInflater inflater) {
        Context context = inflater.getContext();
        ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(params);

        return imageView;
    }


    @Override
    public void bindView(T t, View itemView) {
        String string = t.getString();
        Glide.with(itemView.getContext()).load(string).placeholder(R.mipmap.loading).into((ImageView) itemView);

    }
}
