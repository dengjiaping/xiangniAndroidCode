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
import com.ixiangni.app.setting.HelpCenterActivity;
import com.ixiangni.app.user.ModifyPasswordActivity;
import com.ixiangni.bean.PaywayListBean;
import com.ixiangni.bean.UserInfoBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.interfaces.OnUserInfoChange;
import com.ixiangni.interfaces.OndefaultbankChange;
import com.ixiangni.presenters.GetTextPresenter;
import com.ixiangni.presenters.UserInfoPresenter;
import com.ixiangni.presenters.contract.MyPresenter;
import com.ixiangni.ui.TopBar;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我要提现
 *
 * @ClassName:IWantCashActivity
 * @PackageName:com.ixiangni.app.money
 * @Create On 2017/6/22 0022   12:36
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/22 0022 handongkeji All rights reserved.
 */
public class IWantCashActivity extends BaseActivity implements OndefaultbankChange {

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
    private double useraccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iwant_cash);
        ButterKnife.bind(this);
        SuperObservableManager
                .getInstance()
                .getObservable(OndefaultbankChange.class)
                .registerObserver(this);
        tvForgetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(IWantCashActivity.this, ModifyPasswordActivity.class);
            intent.putExtra(ModifyPasswordActivity.isLoginPassword, false);
            startActivity(intent);
        });
        tvBankCard.setOnClickListener(v -> {
            startActivity(PaymentManageActivity.class);
        });

        topBar.setOnRightClickListener(v -> {
            Intent intent = new Intent(this, HelpCenterActivity.class);
            intent.putExtra("title", "提现说明");
            intent.putExtra(XNConstants.textflag, GetTextPresenter.TEXT_GET_CASH_INTRODUCE);
            startActivity(intent);

        });

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
                        Intent intent = new Intent(IWantCashActivity.this, ModifyPasswordActivity.class);
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
                useraccount = dataBean.getUseraccount();
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
            toast("请填写提现金额！");
            return;
        }
        double cash = Double.parseDouble(money);
        if (cash > useraccount) {
            toast("提取失败,您的账户余额为:" + useraccount);
            return;
        }

        String password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            toast("请填写支付密码！");
            return;
        }

        getCash(money, password);

    }

    /*token	是	String	token
        withdrawType	是	String	提现渠道：101微信，102支付宝
        withdrawCode	是	String	帐号
        withdrawPrice	是	String	金额
        paymentPass	是	String	支付密码*/
    private void getCash(String money, String password) {

        showProgressDialog("提现中...", false);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("paymentPass", password);
        MyPresenter.request(this, UrlString.URL_CHEK_PAYPASWORD, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.token, MyApp.getInstance().getUserTicket());

//                token	是	String	token
//                paymentid	是	Long	支付管理id（我的银行卡id）
//                withdrawPrice	是	String	提现金额
//                paymentPass	是	String	支付密码

                params.put("paymentid", bankCardBean.getPaymentid() + "");
                params.put("withdrawPrice", money);
                params.put("paymentPass", password);
                MyPresenter.request(IWantCashActivity.this, UrlString.URL_GET_CASH, params, new OnResult<String>() {
                    @Override
                    public void onSuccess(String s) {
                        dismissProgressDialog();
                        toast("申请提交成功！");
                        SuperObservableManager.getInstance().getObservable(OnUserInfoChange.class)
                                .notifyMethod(OnUserInfoChange::change);
                        finish();

                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        dismissProgressDialog();

                        toast(errorMsg);
                    }
                });
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


}
