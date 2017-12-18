package com.ixiangni.app.mine;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.handongkeji.common.QFragmentPagerAdapter;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.emotion.BroughtEmFragment;
import com.ixiangni.app.emotion.EmCollectionFragment;
import com.ixiangni.ui.TopBar;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的表情
 *
 * @ClassName:MyEmotionActivity
 * @PackageName:com.ixiangni.app.mine
 * @Create On 2017/6/21 0021   15:13
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class MyEmotionActivity extends BaseActivity {

    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_emotion);
        ButterKnife.bind(this);
        initView();
    }

    private boolean isEdit = false;

    public interface OnEditChange{
        void mode(boolean isEdit);
    }
    private void initView() {
        topBar.setOnRightClickListener(v -> {
            isEdit=!isEdit;
            SuperObservableManager
                    .getInstance()
                    .getObservable(OnEditChange.class)
                    .notifyMethod(onEditChange -> onEditChange.mode(isEdit));
            topBar.setRightText(isEdit?"完成":"编辑");
        });


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new EmCollectionFragment());
        fragments.add(new BroughtEmFragment());

        QFragmentPagerAdapter adapter = new QFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        String[] titles = {"已收藏表情", "已购买表情"};
        adapter.setTitles(Arrays.asList(titles));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
