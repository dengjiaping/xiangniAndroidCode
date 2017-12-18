package com.ixiangni.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.ImageView;

import com.handongkeji.impactlib.dialog.XDialog;
import com.handongkeji.interfaces.Stringable;
import com.ixiangni.adapter.ImagePagerAdapter;
import com.ixiangni.app.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class PhotoBroswdialog extends XDialog {
    private final List<? extends Stringable> dataList;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.iv_close)
    ImageView ivClose;

    public PhotoBroswdialog(Context context, List<? extends Stringable> list) {
        super(context, R.layout.dialog_photo_browse);
        ButterKnife.bind(this);
        this.dataList = list;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        ImagePagerAdapter adapter = new ImagePagerAdapter(dataList);
        viewPager.setAdapter(adapter);
        ivClose.setOnClickListener(v -> dismiss());
    }

    public void show(int position) {
        super.show();
        if (position > 0 && position < dataList.size()) {
            viewPager.setCurrentItem(position);
        }
    }
}
