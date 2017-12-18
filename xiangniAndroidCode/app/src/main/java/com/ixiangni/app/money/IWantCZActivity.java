package com.ixiangni.app.money;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.user.ModifyPasswordActivity;
import com.ixiangni.bean.PaywayListBean;
import com.ixiangni.bean.UserInfoBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.interfaces.OnUserInfoChange;
import com.ixiangni.interfaces.OndefaultbankChange;
import com.ixiangni.presenters.UserInfoPresenter;
import com.ixiangni.presenters.contract.MyPresenter;
import com.ixiangni.ui.TopBar;
import com.mydemo.yuanxin.util.HttpUtil;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 我要充值
 *最新版
 */
public class IWantCZActivity extends BaseActivity implements OndefaultbankChange {

    @Bind(R.id.tv_bank_card)
    TextView tvBankCard;
    @Bind(R.id.edt_money)
    TextInputEditText edtMoney;
    @Bind(R.id.input_layout)
    TextInputLayout inputLayout;
    @Bind(R.id.edt_password)
    EditText edtPassword;
    @Bind(R.id.btn_get_cash)
    Button btnGetCash;
    @Bind(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @Bind(R.id.top_bar)
    TopBar topBar;
    private PaywayListBean.DataBean bankCardBean;
    private MyAlertdialog alertdialog;
    private String userName;
    private String bankCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iwant_cz);
        ButterKnife.bind(this);
        Intent intentp = getIntent();
        userName = intentp.getStringExtra("userIdCard");
        SuperObservableManager
                .getInstance()
                .getObservable(OndefaultbankChange.class)
                .registerObserver(this);
        tvForgetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(IWantCZActivity.this, ModifyPasswordActivity.class);
            intent.putExtra(ModifyPasswordActivity.isLoginPassword, false);
            startActivity(intent);
        });
        tvBankCard.setOnClickListener(v -> {
            startActivity(PaymentManageActivity.class);
        });

//        topBar.setOnRightClickListener(v -> {
//            Intent intent = new Intent(this, HelpCenterActivity.class);
//            intent.putExtra("title", "充值说明");
//            intent.putExtra(XNConstants.textflag, GetTextPresenter.TEXT_GET_CASH_INTRODUCE);
//            startActivity(intent);
//
//        });

        initData();


    }

    private void initData() {
        //获取默认银行卡
        alertdialog = new MyAlertdialog(this);
        alertdialog.setCancelable(false);
        alertdialog.setMessage("您未设置过支付密码，\n是否立即设置？")
                .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                    @Override
                    public void onLeftClick(View view) {
                        finish();
                    }

                    @Override
                    public void onRightClick(View view) {
                        Intent intent = new Intent(IWantCZActivity.this, ModifyPasswordActivity.class);
                        intent.putExtra(ModifyPasswordActivity.isLoginPassword, false);
                        startActivity(intent);
                    }
                });

        //获取用户信息
        UserInfoPresenter userInfoPresenter = new UserInfoPresenter();

        showProgressDialog(Constants.MESSAGE_LOADING, false);
        userInfoPresenter.getUserInfo(this, new OnResult<UserInfoBean.DataBean>() {
            @Override
            public void onSuccess(UserInfoBean.DataBean dataBean) {
                dismissProgressDialog();
                String paymentpass = dataBean.getPaymentpass();
                if (CommonUtils.isStringNull(paymentpass)) {
                    //提示设置支付密码
                    alertdialog.show();
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();
                toast(errorMsg);

            }
        });

        getPayList();

    }

    @Override
    protected void onDestroy() {
        SuperObservableManager.getInstance().getObservable(OndefaultbankChange.class)
                .unregisterObserver(this);
        super.onDestroy();
    }

    @OnClick(R.id.btn_get_cash)
    public void onViewClicked() {
        if (bankCardBean == null) {
            toast("请选择银行卡");
            return;
        }
        String money = edtMoney.getText().toString().trim();

        if (TextUtils.isEmpty(money)) {
            toast("请填写充值金额！");
            return;
        }
        double cash = Double.parseDouble(money);

        if (cash <= 0) {
            toast("充值金额必须大于0");
            return;
        }
        if (cash > 1000000) {
            toast("充值金额最多为1000000");
            return;
        }

        String password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            toast("请填写支付密码！");
            return;
        }

        getCZ(money, password);

    }

    /*token	是	String	token
        withdrawType	是	String	提现渠道：101微信，102支付宝
        withdrawCode	是	String	帐号
        withdrawPrice	是	String	金额
        paymentPass	是	String	支付密码*/
    private void getCZ(String money, String password) {

        showProgressDialog("充值中...", false);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("paymentPass", password);
        MyPresenter.request(this, UrlString.URL_CHEK_PAYPASWORD, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                charge(money);
            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();
                toast("密码错误！");

            }
        });


    }


    private void getPayList() {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        RemoteDataHandler.asyncPost(UrlString.URL_PAY_WAY_LIST, params, this, true, response -> {
            String json = response.getJson();
            if (btnGetCash == null) {
                return;
            }
            if (TextUtils.isEmpty(json)) {
                toast(Constants.CONNECT_SERVER_FAILED);
            } else {
                PaywayListBean paywayListBean = new Gson().fromJson(json, PaywayListBean.class);
                if (1 == paywayListBean.getStatus()) {
                    setBankCard(paywayListBean.getData());
                } else {
                    toast(paywayListBean.getMessage());
                }
            }

        });
    }

    private void setBankCard(List<PaywayListBean.DataBean> data) {
        if (ListUtil.isEmptyList(data)) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            PaywayListBean.DataBean dataBean = data.get(i);
            if (dataBean.getIsdefault() == 1) {

                this.bankCardBean = dataBean;
                String format = "%s(%s)";
                String name = String.format(Locale.CHINA, format, dataBean.getSpare1(), hide(dataBean.getAccountcode()));
                tvBankCard.setText(name);
            }

        }
    }

    private String hide(String account) {
        bankCard = account;

        if (!CommonUtils.isStringNull(account) && account.length() >= 4) {
            int length = account.length();
//            int i = length - 4;
            int i = length - 4;
//            String result = account.substring(0, i) + "****";
            String result = "****" + account.substring(i, length);
            return result;
        }
        return "";
    }

    @Override
    public void onbankChange() {

        getPayList();
    }

    private void charge(String money) {
//        showProgressDialog("充值中", false);
        final RequestBody requsetBody = new FormBody.Builder()
                .add("userNumber", userName)
                .add("money", money)
                .add("yuanxinCARDID", bankCard)
                .build();
        HttpUtil.sendOkHttpPostRequest(UrlString.URL_CHARGE, requsetBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dismissProgressDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast("连接服务器失败");
                    }
                });

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
//                    2.status:  0:充值成功
//                    1:企业一卡通账号不存在
//                    2:该人员级别不够
//                    3:调用想你充值失败
//                    4:企业一卡通余额不足

//                    5:扣款账户错误，不是本人圆信卡

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

                            IWantCZActivity.this.finish();

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
                                    toast("充值失败，级别限制");
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
                        case 5:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toast("充值失败，扣款账户错误，不是本人的企业一卡通");
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
