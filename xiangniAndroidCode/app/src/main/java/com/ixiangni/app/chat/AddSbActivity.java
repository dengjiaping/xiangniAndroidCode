package com.ixiangni.app.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.common.XNConstants;

/**
 * 我加别人
 * @ClassName:AddSbActivity

 * @PackageName:com.ixiangni.app.chat

 * @Create On 2017/6/22 0022   10:42

 * @Site:http://www.handongkeji.com

 * @author:xuchuanting

 * @Copyrights 2017/6/22 0022 handongkeji All rights reserved.
 */
public class AddSbActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sb);
        getIntent().getStringExtra("");
    }

    public static void start(Context context,String userid){
        Intent intent = new Intent(context,AddSbActivity.class);
        intent.putExtra(XNConstants.USERID,userid);
        context.startActivity(intent);
    }
}
