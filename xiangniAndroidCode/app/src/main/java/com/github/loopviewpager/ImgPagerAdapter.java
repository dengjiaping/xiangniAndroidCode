package com.github.loopviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public abstract class ImgPagerAdapter<T> extends PagerAdapter {

    protected Context context;
    protected List<T> dataList;
    protected SparseArray<ImageView> views;

    public ImgPagerAdapter(Context context, List<T> dataList) {
        this.context = context;
        this.dataList = dataList == null ? new ArrayList<T>() : dataList;
        views = new SparseArray<>();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = views.get(position);
//        if (imageView == null) {
            imageView = new ImageView(context);

            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            views.put(position, imageView);
//        }
        loadImage(imageView, position, dataList.get(position));
//        if (imageView.getParent() == null) {
            container.addView(imageView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        }
        return imageView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public abstract void loadImage(ImageView imageView, int position, T t);
}
