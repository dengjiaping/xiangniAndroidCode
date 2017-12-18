package com.mydemo.yuanxin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mydemo.yuanxin.R;
import com.mydemo.yuanxin.base.BaseActivity;
import com.mydemo.yuanxin.explainActivity.HelpActivity;
import com.mydemo.yuanxin.util.CountDownTimerUtils;
import com.mydemo.yuanxin.util.HttpUtil;
import com.mydemo.yuanxin.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TurnActivity extends BaseActivity {
    private Button submit, huoquyz;
    private ImageView back, Instructions;
    private EditText ckName, card, yzm, money;
    private TextView phone;

    private CountDownTimerUtils mCountDownTimerUtils;
    private String yzmNet = "";
    private Intent intentTel;
    private String userTel;
    private String superId;
    private String balance;
    private TextView useMoney;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn);
        //初始化控件
        initView();
        //验证码倒计时工具
        mCountDownTimerUtils = new CountDownTimerUtils(huoquyz, 60000, 1000);
        //加点击监听
        setListener();
        //获取手机号并显示
        userTelAndIdGet();

    }

//    private void findTelForNet() {
//        String userTel = "15311477893";
//        userTel = userTel.substring(0, 3) + "****" + userTel.substring(7, 11);
//        phone.setText(userTel);
//
//    }

    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnActivity.this.finish();
            }
        });
        Instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TurnActivity.this, HelpActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWrite();
            }
        });
        huoquyz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCountDownTimerUtils.start();

                //给服务器发电话号码获取验证码
                RequestBody requsetBody = new FormBody.Builder()
                        .add("phone", userTel)
                        .build();
                HttpUtil.sendOkHttpPostRequest(UrlUtil.ZZ_YZM_URL(), requsetBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TurnActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseText = response.body().string();
                        try {
                            JSONObject jo = new JSONObject(responseText);
                            String yzmNum = jo.getString("phoneVerify");
                            yzmNet = yzmNum;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });

    }

    private void initView() {
        back = (ImageView) findViewById(R.id.iv_back);
        Instructions = (ImageView) findViewById(R.id.iv_Instructions);
        submit = (Button) findViewById(R.id.btn_submit);
        huoquyz = (Button) findViewById(R.id.btn_huoqu_yz);

        ckName = (EditText) findViewById(R.id.cika_name);
        card = (EditText) findViewById(R.id.et_yx_card);
        phone = (TextView) findViewById(R.id.tv_phone);
        yzm = (EditText) findViewById(R.id.et_yz_number);
        money = (EditText) findViewById(R.id.et_turn_money);
        useMoney = (TextView) findViewById(R.id.tv_use_money);


    }

    private void sendWrite() {
        String CkrName = ckName.getText().toString();
        String yxCard = card.getText().toString();
        //String tel = phone.getText().toString();
        String yzNum = yzm.getText().toString();
        String moneyNum = money.getText().toString();
        if (TextUtils.isEmpty(CkrName) || TextUtils.isEmpty(yxCard) || TextUtils.isEmpty(yzNum) || TextUtils.isEmpty(moneyNum)) {
            Toast.makeText(this, "请把资料填写完整！！！", Toast.LENGTH_SHORT).show();
        } else {

            if (Double.parseDouble(moneyNum) >= 500 && yzNum.equals(yzmNet)) {
                if (Double.parseDouble(moneyNum) >= Double.parseDouble(balance)) {
                    Toast.makeText(this, "余额不足！！！", Toast.LENGTH_SHORT).show();
                } else {

                    showProgressDialog();
                    /**
                     * 给服务器发参数
                     */
                    RequestBody requsetBody = new FormBody.Builder()
                            .add("userId", superId)
                            .add("userName", CkrName)
                            .add("yinxinCard", yxCard)
                            .add("yinxinCoinNumber", moneyNum)
                            .build();
                    HttpUtil.sendOkHttpPostRequest(UrlUtil.ZZ_URL(), requsetBody, new Callback() {
                                public String yOrN;

                                @Override
                                public void onFailure(Call call, IOException e) {
                                    closeProgressDialog();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(TurnActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    closeProgressDialog();
                                    String responseText = response.body().string();
                                    try {
                                        JSONObject jo = new JSONObject(responseText);
                                        yOrN = jo.getString("returns");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    if ("true".equals(yOrN)) {
                                        startActivity(new Intent(TurnActivity.this, SubmitActivity.class));
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(TurnActivity.this, "发送失败，请核对卡号是否正确或者其他问题", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                }
                            }

                    );
                }


            } else {
                Toast.makeText(this, "转账金额小于500或者验证码错误！！！", Toast.LENGTH_SHORT).show();

            }

        }

    }

    private void userTelAndIdGet() {
        intentTel = getIntent();
        userTel = intentTel.getStringExtra("userTel");
        superId = intentTel.getStringExtra("superId");
        balance = intentTel.getStringExtra("balance");
        String userTelNotify = userTel.substring(0, 3) + "****" + userTel.substring(7, 11);
        phone.setText(userTelNotify);
        useMoney.setText("可用银信币:" + balance);

    }



    //加载中方法
    private  ProgressDialog progressDialog;

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {

        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(TurnActivity.this);
                progressDialog.setMessage("正在提交...");
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
