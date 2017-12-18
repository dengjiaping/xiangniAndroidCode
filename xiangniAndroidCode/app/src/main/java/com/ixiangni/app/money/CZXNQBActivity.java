package com.ixiangni.app.money;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.dialog.XDialog;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.user.ModifyPasswordActivity;
import com.ixiangni.bean.PassSetBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.interfaces.OnUserInfoChange;
import com.ixiangni.presenters.contract.MyPresenter;
import com.mydemo.yuanxin.util.HttpUtil;
import com.nevermore.oceans.ob.SuperObservableManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 充值想你币
 *
 * @ClassName:RedPackageActivity
 * @PackageName:com.ixiangni.app.money
 * @Create On 2017/6/22 0022   15:58
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/22 0022 handongkeji All rights reserved.
 */
public class CZXNQBActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.edt_money)
    EditText edtMoney;
    @Bind(R.id.btn_charge)
    Button btnCharge;


    private XDialog dialog;


    private HashMap<String, String> params = new HashMap<>();
    private String userName;
    private String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iwant_charge);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        userName = intent.getStringExtra("userIdCard");
        CommonUtils.setPoint(edtMoney, 2);

        checkSetPayPassword();
        btnCharge.setOnClickListener(this);
    }

    private void checkSetPayPassword() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        RemoteDataHandler.asyncPost(UrlString.URL_CHECK_PAY_SET, params, this, false, response -> {
            String json = response.getJson();
            log(json);
            if (btnCharge != null) {
                if (TextUtils.isEmpty(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    PassSetBean passSetBean = new Gson().fromJson(json, PassSetBean.class);
                    if (1 == passSetBean.getStatus()) {
                        int data = passSetBean.getData();
                        if (0 == data) {
                            MyAlertdialog alertdialog = new MyAlertdialog(CZXNQBActivity.this);
                            alertdialog.setCancelable(false);
                            alertdialog.setMessage("您还未设置支付密码，\n是否去设置？")
                                    .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                                        @Override
                                        public void onLeftClick(View view) {
                                            finish();
                                        }

                                        @Override
                                        public void onRightClick(View view) {

                                            Intent intent = new Intent(CZXNQBActivity.this, ModifyPasswordActivity.class);
                                            intent.putExtra(ModifyPasswordActivity.isLoginPassword, false);
                                            startActivity(intent);

                                        }
                                    });

                            alertdialog.show();
                        }

                    } else {
                        toast(passSetBean.getMessage());
                    }

                }


            }

        });

    }


    @Override
    public void onClick(View v) {

        money = edtMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            toast("请输入充值金额！");
            return;
        }

        double v1 = Double.parseDouble(money);
        if (v1 <= 0) {
            toast("充值金额必须大于0");
            return;
        }
        if (v1 > 1000000) {
            toast("充值金额最多为1000000");
            return;
        }
//        if(v1>usableCoin){
//            toast("超出可用银信币");
//            return;
//        }

        dialog = new XDialog(this, R.layout.layout_mima);
        EditText editText = (EditText) dialog.findViewById(R.id.edt_password);
        dialog.setOnClickListener(R.id.tv_left, v2 -> {
            dialog.dismiss();
        });

        dialog.setOnClickListener(R.id.tv_right, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editText.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    toast("请输入支付密码！");
                    return;
                }

                //验证支付密码
                showProgressDialog("验证密码中...", false);
                HashMap<String, String> params = new HashMap<>();
                params.put("token", MyApp.getInstance().getUserTicket());
                params.put("paymentPass", password);

                MyPresenter.request(CZXNQBActivity.this, UrlString.URL_CHEK_PAYPASWORD, params, new OnResult<String>() {
                    @Override
                    public void onSuccess(String s) {
                        dialog.dismiss();
//                        mProgressDialog.setMessage("充值中...");
                        charge(money);
                    }

                    @Override
                    public void onFailed(String errorMsg) {

                        dismissProgressDialog();
                        toast(errorMsg);
                    }
                });
            }
        });

        dialog.show();
    }


    private void charge(String money) {
        showProgressDialog("充值中", false);
        final RequestBody requsetBody = new FormBody.Builder()
                .add("userNumber", userName)
                .add("money", money)
                .build();
        HttpUtil.sendOkHttpPostRequest(UrlString.URL_CHARGE, requsetBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dismissProgressDialog();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dismissProgressDialog();

                String responseText = response.body().string();
                try {
                    JSONObject jo = new JSONObject(responseText);
                    JSONObject returns = jo.getJSONObject("returns");
                    String status = returns.getString("status").trim();
                    //returns:
//                    1.message:true||false(成功或失败)
//                            2.status:  0:充值成功
//                    1:企业一卡通账号不存在
//                    2:该人员级别不够
//                    3:调用想你充值失败
                    switch (Integer.parseInt(status)) {
                        case 0:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toast("充值成功");
                                    SuperObservableManager.getInstance().getObservable(OnUserInfoChange.class)
                                            .notifyMethod(OnUserInfoChange::change);
                                }
                            });

                            CZXNQBActivity.this.finish();

                            break;
                        case 1:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toast("企业一卡通账号不存在");
                                }
                            });
                            break;
                        case 2:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toast("总监以上级别才可以充值");
                                }
                            });
                            break;
                        case 3:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toast("调用想你钱包充值失败");
                                }
                            });

                            break;
                        case 4:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toast("企业一卡通余额不足，请进入本币运行进行充值");
                                }
                            });

                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }


}
