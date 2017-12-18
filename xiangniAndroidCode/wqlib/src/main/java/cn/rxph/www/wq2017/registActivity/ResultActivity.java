package cn.rxph.www.wq2017.registActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.rxph.www.wq2017.R;
import cn.rxph.www.wq2017.activity.WQMainActivity;
import cn.rxph.www.wq2017.base.BaseActivity;
import cn.rxph.www.wq2017.utils.ActivityCollector;


public class ResultActivity extends BaseActivity {
    private Button backHome;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        backHome = (Button) findViewById(R.id.btn_back_home);
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.finishAll();
                Intent intent = new Intent(ResultActivity.this, WQMainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
