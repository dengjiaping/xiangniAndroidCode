package com.ixiangni.app.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ixiangni.app.R;
import com.ixiangni.common.XNConstants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MsgDetailActivity extends AppCompatActivity {

    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        String content = intent.getStringExtra(XNConstants.msgcontent);
        String time = intent.getStringExtra(XNConstants.msgtime);
        tvContent.setText(content);
        tvTime.setText(time);

    }
}
