package com.ixiangni.app.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 联系我们
 *
 * @ClassName:ContactActivity
 * @PackageName:com.ixiangni.app.setting
 * @Create On 2017/6/20 0020   14:42
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/20 0020 handongkeji All rights reserved.
 */
public class ContactActivity extends BaseActivity {

    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.ll_contact_us)
    LinearLayout llContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ll_contact_us)
    public void onViewClicked() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+tvPhoneNum.getText().toString().trim()));
        startActivity(intent);
    }
}
