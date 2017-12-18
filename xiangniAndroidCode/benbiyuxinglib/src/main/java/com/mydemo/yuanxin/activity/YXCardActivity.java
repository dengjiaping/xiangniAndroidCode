package com.mydemo.yuanxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mydemo.yuanxin.R;
import com.mydemo.yuanxin.base.BaseActivity;
import com.mydemo.yuanxin.bean.PersonMian;
import com.mydemo.yuanxin.util.HttpUtil;
import com.mydemo.yuanxin.util.UrlUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class YXCardActivity extends BaseActivity {
    private Button  record, tx, zz, cz, jyjl, gm;
    private ImageView back;
    private TextView cardNum;
    private TextView balance, userName;
    private Intent intentGetId;
    private String superId;
    private String userTel;
    private String yuE;
    private SwipeRefreshLayout refresh;
    public String grade;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yx_card);
        //初始化控件
        initView();
        //设置下拉刷新
        setRefresh();

        intentGetId = getIntent();
        superId = intentGetId.getStringExtra("superId");
        //加点击监听
        setListener();

        data();

    }

    private void setRefresh() {
        refresh.setColorSchemeResources(R.color.colorPrimary);
    }

    private void setListener() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data();
            }
        });

        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(userTel)||TextUtils.isEmpty(grade)) {
                    Toast.makeText(YXCardActivity.this, "获取用户信息失败，请下拉刷新主页面", Toast.LENGTH_SHORT).show();
                } else {
                    if("A".equals(grade)||"B".equals(grade)){
                        Intent intent = new Intent(YXCardActivity.this, WithdrawalsActivity.class);
                        intent.putExtra("userTel", userTel);
                        intent.putExtra("superId", superId);
                        intent.putExtra("balance", yuE);

                        startActivity(intent);
                    }else {
                        Toast.makeText(YXCardActivity.this, "非常抱歉，你尚未拥有此权限！", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        zz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(userTel)) {
                    Toast.makeText(YXCardActivity.this, "获取用户信息失败，请下拉刷新主页面", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(YXCardActivity.this, TurnActivity.class);
                    intent.putExtra("userTel", userTel);
                    intent.putExtra("superId", superId);
                    intent.putExtra("balance", yuE);
                    startActivity(intent);
                }
            }
        });
        cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(userTel)||TextUtils.isEmpty(grade)) {
                    Toast.makeText(YXCardActivity.this, "获取用户信息失败，请下拉刷新主页面", Toast.LENGTH_SHORT).show();
                } else {
                    if("A".equals(grade)||"B".equals(grade)){
                        Intent intent = new Intent(YXCardActivity.this, RechargeActivity.class);
                        intent.putExtra("superId", superId);
                        startActivity(intent);
                    }else {
                        Toast.makeText(YXCardActivity.this, "非常抱歉，你尚未拥有此权限！", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });



        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userTel)) {
                    Toast.makeText(YXCardActivity.this, "获取用户信息失败，请下拉刷新主页面", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(YXCardActivity.this, RecordActivity.class);
                    intent.putExtra("superId_record", superId);
                    startActivity(intent);
                }
            }
        });
        jyjl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userTel)) {
                    Toast.makeText(YXCardActivity.this, "获取用户信息失败，请下拉刷新主页面", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(YXCardActivity.this, RecordActivity.class);
                    intent.putExtra("superId_record", superId);
                    startActivity(intent);
                }

            }
        });
        gm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userTel)) {
                    Toast.makeText(YXCardActivity.this, "获取用户信息失败，请下拉刷新主页面", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(YXCardActivity.this, ModifyPWActivity.class);
                    intent.putExtra("superId", superId);
                    startActivity(intent);
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YXCardActivity.this.finish();
            }
        });


    }


    /**
     * 从网络获取数据
     */
    private void data() {
        final RequestBody requsetBody = new FormBody.Builder()
                .add("userId", superId)
                .build();

        //获取余额 ,卡号，姓名，手机号
        HttpUtil.sendOkHttpPostRequest(UrlUtil.UserMain_URL(), requsetBody, new Callback() {

//            public String cardNum;

            @Override
            public void onFailure(Call call, IOException e) {
                //设置刷新结束
                refresh.setRefreshing(false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(YXCardActivity.this, "连接服务器失败，无法进入", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                PersonMian personMian = parseJsonWithGson(responseText);
                final String MemberName = personMian.getMemberName();
                String MemberPhone = personMian.getMemberPhone();
                final String YinxinCARDID = personMian.getYinxinCARDID();
                //用户级别
                grade= personMian.getPositionGrade();
                final String YinxincoinNumber = personMian.getYinxincoinNumber();
                yuE = YinxincoinNumber;

//                if (!TextUtils.isEmpty(YinxincoinNumber)) {
//                    cardNum = YinxinCARDID.substring(0, 4) + "  " + YinxinCARDID.substring(4, 8) + "  " + YinxinCARDID.substring(8, 12) + "  " + YinxinCARDID.substring(12, 15);
//                }


                userTel = MemberPhone;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        YXCardActivity.this.cardNum.setText(YinxinCARDID);
                        if (TextUtils.isEmpty(YinxincoinNumber)) {
                            balance.setText("0.00");
                        } else {

                            balance.setText(YinxincoinNumber);
                        }
                        userName.setText(MemberName);
                        //设置刷新结束
                        refresh.setRefreshing(false);

                    }
                });


            }
        });

    }

    private void initView() {
        back = (ImageView) findViewById(R.id.iv_back);
        record = (Button) findViewById(R.id.button_Record);
        tx = (Button) findViewById(R.id.wytx);
        zz = (Button) findViewById(R.id.bbzz);
        cz = (Button) findViewById(R.id.cz);
        jyjl = (Button) findViewById(R.id.jyjl);
        gm = (Button) findViewById(R.id.gm);
        cardNum = (TextView) findViewById(R.id.tv_card_num);
        balance = (TextView) findViewById(R.id.tv_balance);
        userName = (TextView) findViewById(R.id.tv_user_name);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
    }


    private PersonMian parseJsonWithGson(String jsonData) {
        //CleanJsonUtil.cleanErrorCode(jsonData);
        Gson gson = new Gson();
        PersonMian personMian = gson.fromJson(jsonData, PersonMian.class);
        return personMian;

    }

    @Override
    protected void onResume() {
        super.onResume();
        data();
    }
}
