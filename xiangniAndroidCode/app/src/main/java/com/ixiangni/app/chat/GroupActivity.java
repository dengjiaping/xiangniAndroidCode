package com.ixiangni.app.chat;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.handongkeji.common.QFragmentPagerAdapter;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.constants.UrlString;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群聊
 *
 * @ClassName:GroupActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/7/5 0005   15:24
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/5 0005 handongkeji All rights reserved.
 */
public class GroupActivity extends BaseActivity {

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.iv_jia)
    ImageView ivJia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(GroupFragment.newInstance(UrlString.URL_ALL_GROUP));
        fragments.add(GroupFragment.newInstance(UrlString.URL_MY_CREATE_GROUP));
        fragments.add(GroupFragment.newInstance(UrlString.URL_INVITED_GROUP));

        QFragmentPagerAdapter adapter = new QFragmentPagerAdapter(getSupportFragmentManager(), fragments);

        ArrayList<String> titles = new ArrayList<>();
        titles.add("全部");
        titles.add("我创建的");
        titles.add("我加入的");
        adapter.setTitles(titles);

        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

    }

    @OnClick({R.id.iv_search, R.id.iv_jia})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                startActivity(AgreeActivity.class);
                break;
            case R.id.iv_jia:
//                startActivity(SearchGroupActivity.class);
                startActivity(CreatGroupActivity.class);
                break;
        }
    }
}
