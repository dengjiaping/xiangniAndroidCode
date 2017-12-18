package com.ixiangni.app.money;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.redpacket.utils.TransferConstance;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.dialog.XDialog;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.app.setting.HelpCenterActivity;
import com.ixiangni.app.user.ModifyPasswordActivity;
import com.ixiangni.bean.PassSetBean;
import com.ixiangni.bean.PersonBean;
import com.ixiangni.bean.UsableCoinBean;
import com.ixiangni.bean.XNMoneyBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.presenters.GetTextPresenter;
import com.ixiangni.presenters.contract.MyPresenter;
import com.ixiangni.ui.TopBar;
import com.ixiangni.util.GlideUtil;

import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 转账
 *
 * @ClassName:TransferActivity
 * @PackageName:com.ixiangni.app.money
 * @Create On 2017/6/19 0019   14:06
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/19 0019 handongkeji All rights reserved.
 */
public class TransferActivity extends BaseActivity {

    @Bind(R.id.tv_usable_coin)
    TextView tvUsableCoin;
    @Bind(R.id.iv_user_icon)
    ImageView ivUserIcon;
    @Bind(R.id.tv_nick_name)
    TextView tvNickName;
    @Bind(R.id.edt_transfer_money)
    EditText edtTransferMoney;
    @Bind(R.id.edt_extra_message)
    EditText edtExtraMessage;
    @Bind(R.id.btn_transfer_money)
    Button btnTransferMoney;
    @Bind(R.id.top_bar)
    TopBar topBar;
    private String goalid;
    private XDialog dialog;
    private double usableCoinBeanData = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);
        goalid = getIntent().getStringExtra(XNConstants.goalid);
        log(goalid);
        checkSetPayPassword();
        getYue();
        getSimpleUserInfo();

        topBar.setOnRightClickListener(v -> {
            Intent intent = new Intent(this, HelpCenterActivity.class);
            intent.putExtra("title", "转账说明");
            intent.putExtra(XNConstants.textflag, GetTextPresenter.TEXT_PAYMENT_INTRODUCE);
            startActivity(intent);
        });
        CommonUtils.setPoint(edtTransferMoney, 2);

    }

    private void checkSetPayPassword() {
        showProgressDialog("加载中...", false);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        RemoteDataHandler.asyncPost(UrlString.URL_CHECK_PAY_SET, params, this, false, response -> {
            String json = response.getJson();
            log(json);
            if (tvUsableCoin != null) {
                dismissProgressDialog();
                if (TextUtils.isEmpty(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    PassSetBean passSetBean = new Gson().fromJson(json, PassSetBean.class);
                    if (1 == passSetBean.getStatus()) {
                        int data = passSetBean.getData();
                        if (0 == data) {
                            MyAlertdialog alertdialog = new MyAlertdialog(TransferActivity.this);
                            alertdialog.setCancelable(false);
                            alertdialog.setMessage("您还未设置支付密码，\n是否去设置？")
                                    .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                                        @Override
                                        public void onLeftClick(View view) {
                                            finish();
                                        }

                                        @Override
                                        public void onRightClick(View view) {

                                            Intent intent = new Intent(TransferActivity.this, ModifyPasswordActivity.class);
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


    private void getYue() {
        //可用余额查询

        HashMap<String, String> params = new HashMap<>();
        params.put("token", LoginHelper.getInstance().getToken(this));
        RemoteDataHandler.asyncPost(UrlString.URL_AVAILABLE_MONEY, params, this, false, callback -> {

            String json = callback.getJson();
            if (mContext != null) {

                if (CommonUtils.isStringNull(json)) {

                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    UsableCoinBean usableCoinBean = new Gson().fromJson(json, UsableCoinBean.class);

                    String moneyName = String.format("可用%s:", getString(R.string.coinName));

                    if (1 == usableCoinBean.getStatus()) {
                        usableCoinBeanData = usableCoinBean.getData();
                        String moneyStr = moneyName + String.format(Locale.CHINA,"%.2f",usableCoinBeanData);
                        tvUsableCoin.setText(moneyStr);
                    } else {
                        tvUsableCoin.setText(moneyName + "0.0");
                    }
                }
            }
        });
    }

    private void getSimpleUserInfo() {

        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("goalid", goalid);

        RemoteDataHandler.asyncPost(UrlString.URL_GET_OTHER_INFO, params, this, true, response -> {

            String json = response.getJson();
            log(json);
            if (tvNickName != null) {
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    PersonBean personInfoBean = new Gson().fromJson(json, PersonBean.class);
                    if (1 == personInfoBean.getStatus()) {
                        setPersonInfo(personInfoBean.getData());

                    } else {
                        toast(personInfoBean.getMessage());
                    }
                }

            }
        });

    }

    private void setPersonInfo(PersonBean.DataBean data) {
        tvNickName.setText(data.getUsernick());
        GlideUtil.loadRoundImage(this, data.getUserpic(), ivUserIcon, R.mipmap.touxiangmoren);
    }


    //    token	是	String	用户token
//    goalid	是	Long	接收人id
//    price	是	Double	转账金额
//    paypassword	否	String	支付密码。不是指纹支付时必传
//    message	否	String	转账留言
    private void transfer(String price, String paypassword, String message) {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put(XNConstants.goalid, goalid);
        params.put("paypassword", paypassword);
        params.put("message", message);
        params.put("price", price);

        RemoteDataHandler.asyncPost(UrlString.URL_TRANSFER, params, this, false, response -> {
            String json = response.getJson();
            log(json);
            if (btnTransferMoney != null) {
                dismissProgressDialog();
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    XNMoneyBean xnMoneyBean = new Gson().fromJson(json, XNMoneyBean.class);
                    if (1 == xnMoneyBean.getStatus()) {
                        XNMoneyBean.DataBean data = xnMoneyBean.getData();
                        Intent intent = new Intent();
                        //留言
                        intent.putExtra(TransferConstance.message, edtExtraMessage.getText().toString().trim());
                        intent.putExtra(TransferConstance.moneynum, price);
                        intent.putExtra(TransferConstance.TransferId, data.getLuckmoneyid() + "");

                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        toast(xnMoneyBean.getMessage());
                    }
                }
            }


        });


    }


    @OnClick(R.id.btn_transfer_money)
    public void onViewClicked() {
        if (usableCoinBeanData == -1) {
            return;
        }
        String money = edtTransferMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            toast("请输入转账金额！");
            return;
        }

        double v = Double.parseDouble(money);
        if(v<=0){
            toast("转账金额必须大于0");
            return;
        }
        if(v>10000){
            toast("转账金额最多1万");
            return;
        }

        if (v > usableCoinBeanData) {
            toast("转账金额超支！");
            return;
        }

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

                MyPresenter.request(TransferActivity.this, UrlString.URL_CHEK_PAYPASWORD, params, new OnResult<String>() {
                    @Override
                    public void onSuccess(String s) {
                        dialog.dismiss();
                        mProgressDialog.setMessage("转账中...");
                        transfer(money, password, edtExtraMessage.getText().toString().trim());
                        dialog.dismiss();

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
}
