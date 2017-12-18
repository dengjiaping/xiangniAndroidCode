package com.ixiangni.app.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 消息详情
 *
 * @ClassName:MessageDetailActivity
 * @PackageName:com.ixiangni.app.message
 * @Create On 2017/6/19 0019   08:55
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/19 0019 handongkeji All rights reserved.
 */
public class MessageDetailActivity extends BaseActivity {

    @Bind(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        String content = getIntent().getStringExtra("content");
        tvContent.setText(content);
    }

    public static void start(Context context,String message){
        Intent intent = new Intent(context,MessageDetailActivity.class);
        intent.putExtra("content",message);
        context.startActivity(intent);

    }
}
