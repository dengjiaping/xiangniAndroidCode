package com.ixiangni.app.message;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.handongkeji.common.QFragmentPagerAdapter;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 消息中心
 *
 * @ClassName:MessageCenterActivity
 * @PackageName:com.ixiangni.app.message
 * @Create On 2017/6/19 0019   09:45
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/19 0019 handongkeji All rights reserved.
 */
public class MessageCenterActivity extends BaseActivity {


//    状态:0未读;1已读;不传该参数为全部消息


    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.vp_message)
    ViewPager vpMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(SortFragment.newInstance(null));
        fragmentList.add(SortFragment.newInstance("0"));
        fragmentList.add(SortFragment.newInstance("1"));
        QFragmentPagerAdapter adapter = new QFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);

        List<String> titles = new ArrayList<>();
        titles.add("全部");
        titles.add("未读");
        titles.add("已读");
        adapter.setTitles(titles);
        vpMessage.setAdapter(adapter);
        vpMessage.setOffscreenPageLimit(fragmentList.size());
        tabLayout.setupWithViewPager(vpMessage);


    }


}
