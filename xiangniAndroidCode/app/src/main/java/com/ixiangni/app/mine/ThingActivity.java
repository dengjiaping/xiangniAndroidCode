package com.ixiangni.app.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ixiangni.app.R;

import java.util.ArrayList;
import java.util.List;

public class ThingActivity extends FragmentActivity {

    List<Fragment> list = new ArrayList<Fragment>();
    private ImageView back;
    private ViewPager vp;
    private RadioGroup rg;
    private RadioButton rb0;
    private RadioButton rb1;
    private RadioButton rb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing);
        init();
        //在viewpager上显示Fragment使用的的适配。
        //构造方法需要接受一个FragmentManager对象
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(
                getSupportFragmentManager()) {
            //list的长度
            @Override
            public int getCount() {
                return list.size();
            }
            //指定位置的Fragment
            @Override
            public Fragment getItem(int arg0) {
                return list.get(arg0);
            }
        };
        vp.setAdapter(adapter);
        setLinger();
        rb0.setChecked(true);
    }
    private void init() {
        back= (ImageView) findViewById(R.id.iv_back);
        rg = (RadioGroup) findViewById(R.id.radioGroup1);
        rb0 = (RadioButton) findViewById(R.id.radio0);
        rb1 = (RadioButton) findViewById(R.id.radio1);
        rb2 = (RadioButton) findViewById(R.id.radio2);
        vp = (ViewPager) findViewById(R.id.fragment_container);
        list.add(new GonggaoFragment());
        list.add(new ZhiboFragment());
        list.add(new HuodongFragment());
    }
    private void setLinger() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThingActivity.this.finish();
            }
        });
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        rb0.setChecked(true);
                        break;
                    case 1:
                        rb1.setChecked(true);

                        break;
                    case 2:
                        rb2.setChecked(true);

                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio0:
                        vp.setCurrentItem(0);
                        break;
                    case R.id.radio1:
                        vp.setCurrentItem(1);

                        break;
                    case R.id.radio2:
                        vp.setCurrentItem(2);

                        break;
                }
            }
        });
    }

}


