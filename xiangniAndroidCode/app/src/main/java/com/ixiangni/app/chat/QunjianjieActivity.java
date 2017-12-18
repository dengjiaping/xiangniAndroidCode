package com.ixiangni.app.chat;

import android.os.Bundle;
import android.widget.TextView;

import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.common.XNConstants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 群简介展示
 *
 * @ClassName:QunjianjieActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/8/1 0001   16:33
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/8/1 0001 handongkeji All rights reserved.
 */
public class QunjianjieActivity extends BaseActivity {

    @Bind(R.id.edt_group_profile)
    TextView edtGroupProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qunjianjie);
        ButterKnife.bind(this);
        edtGroupProfile.setText(getIntent().getStringExtra(XNConstants.groupdescirbe));
    }
}
