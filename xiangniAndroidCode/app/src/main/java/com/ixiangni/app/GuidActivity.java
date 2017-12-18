package com.ixiangni.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.loopviewpager.ImgPagerAdapter;
import com.github.viewpagerindicator.CirclePageIndicator;
import com.ixiangni.app.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 引导页
 *
 * @ClassName:GuidActivity
 * @PackageName:com.ixiangni.app
 * @Create On 2017/7/31 0031   14:29
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/31 0031 handongkeji All rights reserved.
 */
public class GuidActivity extends BaseActivity {

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.indicator)
    CirclePageIndicator indicator;
    @Bind(R.id.tv_move)
    TextView tvMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add("");
        }
        GuidAdapter adapter = new GuidAdapter(this,list);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position==3){
                    tvMove.setVisibility(View.VISIBLE);
                    tvMove.setOnClickListener(v -> {
                        startActivity(LoginActivity.class);
                        finish();
                    });
                }else {
                    tvMove.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

//    class GuidAdapter extends PagerAdapter{
//
//        @Override
//        public int getCount() {
//            return 0;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view==object;
//        }
//    }

    class GuidAdapter extends ImgPagerAdapter<String>{

        public GuidAdapter(Context context, List<String> dataList) {
            super(context, dataList);
        }


        @Override
        public void loadImage(ImageView imageView, int position, String s) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            switch (position){
                case 0:
                    Glide.with(GuidActivity.this).load(R.mipmap.ydy_1).into(imageView);

                    break;
                case 1:
                    Glide.with(GuidActivity.this).load(R.mipmap.ydy_2).into(imageView);

                    break;
                case 2:
                    Glide.with(GuidActivity.this).load(R.mipmap.ydy_3).into(imageView);

                    break;
                case 3:
                    Glide.with(GuidActivity.this).load(R.mipmap.ydy_4).into(imageView);
                    break;
            }

        }
    }
}
