package com.mydemo.yuanxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mydemo.yuanxin.R;
import com.mydemo.yuanxin.base.BaseActivity;

/**
 * 提交的activity
 */
public class SubmitActivity extends BaseActivity {
    private Button exist;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        exist= (Button) findViewById(R.id.btn_exist);
        back= (ImageView) findViewById(R.id.iv_back);
        exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubmitActivity.this,YXCardActivity.class));
                SubmitActivity.this.finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitActivity.this.finish();
            }
        });
    }
}
