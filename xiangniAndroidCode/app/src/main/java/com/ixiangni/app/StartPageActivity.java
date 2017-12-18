package com.ixiangni.app;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;

public class StartPageActivity extends BaseActivity {

    private SharedPreferences sharedPreferences;

    public static final String firstLogin = "firstLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#297ae7"));
        }
        setContentView(R.layout.activity_start_page);
        sharedPreferences = getSharedPreferences("sys_info", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                go();
            }
        },900);
    }

    private void go() {
        if (sharedPreferences.getBoolean(firstLogin,true)) {
            sharedPreferences.edit().putBoolean(firstLogin,false).commit();
            startActivity(GuidActivity.class);
        }else {
            if(LoginHelper.getInstance().isLogin(this)){
                startActivity(MainActivity.class);
            }else {
                startActivity(LoginActivity.class);
            }
        }
        finish();

    }


}
