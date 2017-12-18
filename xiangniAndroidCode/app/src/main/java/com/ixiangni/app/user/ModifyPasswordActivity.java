package com.ixiangni.app.user;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.login.IdentifyCodePresenter;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.utils.RegexUtils;
import com.ixiangni.app.LoginActivity;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.UserInfoBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.presenters.UserInfoPresenter;
import com.nevermore.oceans.widget.CountDownButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码
 *
 * @ClassName:ModifyPasswordActivity
 * @PackageName:com.ixiangni.app.user
 * @Create On 2017/6/20 0020   15:00
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/20 0020 handongkeji All rights reserved.
 */
public class ModifyPasswordActivity extends BaseActivity implements IdentifyCodePresenter.ISendResult {

    @Bind(R.id.rb_login)
    RadioButton rbLogin;
    @Bind(R.id.rb_pay)
    RadioButton rbPay;
    @Bind(R.id.edt_phone_num)
    EditText edtPhoneNum;
    @Bind(R.id.tv_code_title)
    TextView tvCodeTitle;
    @Bind(R.id.edt_identify_code)
    EditText edtIdentifyCode;
    @Bind(R.id.count_down_button)
    CountDownButton countDownButton;
    @Bind(R.id.edt_password)
    EditText edtPassword;
    @Bind(R.id.edt_password_again)
    EditText edtPasswordAgain;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    private boolean cspw;

    public static final String isLoginPassword = "isLoginPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ButterKnife.bind(this);

        boolean isLoginPassword = getIntent().getBooleanExtra("isLoginPassword", true);

        if (isLoginPassword) {
            rbLogin.setChecked(true);
        } else {
            rbPay.setChecked(true);
        }


        getPhoneNum();
    }

    private void getPhoneNum() {
        UserInfoPresenter presenter = new UserInfoPresenter();
        showProgressDialog("加载中...", true);
        presenter.getUserInfo(this, new OnResult<UserInfoBean.DataBean>() {
            @Override
            public void onSuccess(UserInfoBean.DataBean dataBean) {
                if (btnConfirm != null) {
                    dismissProgressDialog();
                    String usermobile = dataBean.getUsermobile();
                    edtPhoneNum.setText(usermobile);
                    edtPhoneNum.setEnabled(false);
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                if (btnConfirm != null) {

                    dismissProgressDialog();
                    toast(errorMsg);
                }

            }
        });


    }

    @OnClick({R.id.count_down_button, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.count_down_button://发送验证码并开始倒计时
                checkPhoneNumAndSendIdentifyCode();
                break;
            case R.id.btn_confirm:
                tryModifyPassword();
                break;
        }
    }

    private void tryModifyPassword() {
        boolean isLoginPassword = rbLogin.isChecked();

        String phoneNum = edtPhoneNum.getText().toString().trim();
        String code = edtIdentifyCode.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String passwordAgain = edtPasswordAgain.getText().toString().trim();

        if (!RegexUtils.checkMobile(phoneNum)) {
            toast("手机号不正确！");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            toast("请填写验证码！");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            toast("请填写密码！");
            return;
        }
        if (password.equals(phoneNum)) {
            toast("密码不可以设置为绑定的手机号");
            return;
        }

        if (isLoginPassword) {
            if (password.length() > 15 || password.length() < 6) {
                toast("请输入6-15位登录密码！");
                return;
            }
        } else {
            if (password.length() != 6) {
                toast("请输入6位支付密码！");
                return;
            }
        }

        if (TextUtils.isEmpty(passwordAgain)) {
            toast("请填写确认密码");
            return;
        }

        if (!password.equals(passwordAgain)) {
            toast("两次密码不一致！");
            return;
        }


        showProgressDialog("修改中...", false);
        if (isLoginPassword) {
            //修改登录密码
            changeLoginPassword(phoneNum, code, password);
        } else {
            changePaymentPassword(phoneNum, code, password);
        }
    }

    /**
     * 修改支付密码
     * <p>
     * phoneNum	True	String	手机号码
     * userCode	True	String	验证码
     * paymentPass	True	String	新支付密码
     * appType	True	Int	app类型  1 安卓，2 ios
     *
     * @param password
     */
    private void changePaymentPassword(String phoneNum, String code, String password) {

        HashMap<String, String> params = new HashMap<>();
        params.put("phoneNum", phoneNum);
        params.put("userCode", code);
        params.put("paymentPass", password);
        params.put("appType", "1");

        RemoteDataHandler.asyncPost(UrlString.URL_RESET_PAYMENT_PASSWORD, params, mContext, false, responseData -> {

            if (mContent != null) {
                dismissProgressDialog();

                String json = responseData.getJson();
                if (TextUtils.isEmpty(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    try {
                        JSONObject object = new JSONObject(json);
                        int status = object.getInt(Constants.status);
                        if (1 == status) {
                            toast("修改成功！");
                            finish();
                        } else {
                            toast(object.getString(Constants.message));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


        });
    }

    /**
     * 修改登录密码
     *
     * @param phoneNum
     * @param code
     * @param password
     */
    private void changeLoginPassword(String phoneNum, String code, String password) {

        HashMap<String, String> params = new HashMap<>();

        params.put("phoneNum", phoneNum);
        params.put("userCode", code);
        params.put("password", password);
        params.put("appType", "1");

        RemoteDataHandler.asyncPost(UrlString.URL_FORGET_PASSWORD, params, mContext, false, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData response) {

                if (mContext != null) {
                    dismissProgressDialog();
                    String json = response.getJson();
                    if (!TextUtils.isEmpty(json)) {
                        try {
                            JSONObject object = new JSONObject(json);
                            if (1 == object.getInt("status")) {
                                toast("修改成功！");
//                                finish();
                                remoteLogin();
                            } else {
                                toast(object.getString("message"));
                            }

                        } catch (JSONException e) {

                        }
                    } else {
                        toast(Constants.CONNECT_SERVER_FAILED);
                    }
                }
            }
        });
    }

    private void checkPhoneNumAndSendIdentifyCode() {
        String phoneNum = edtPhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            toast("请填写手机号！");
            return;
        }
        if (!RegexUtils.checkMobile(phoneNum)) {
            toast("手机号不正确！");
            return;
        }
        showProgressDialog("发送验证码...", true);
        new IdentifyCodePresenter(UrlString.BASE_URL).getCode(this, IdentifyCodePresenter.FOR_CHANGE_PASSWORD, phoneNum);
    }

    @Override
    public void onSendSuccess(String message) {
        dismissProgressDialog();
        toast("发送成功！");
        countDownButton.start();
    }

    @Override
    public void onSendFailed(String message) {
        dismissProgressDialog();
        toast(message);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        if (countDownButton != null) {
            countDownButton.cancel();
        }
        super.onDestroy();
    }


    private void remoteLogin() {

        MyAlertdialog alertdialog = new MyAlertdialog(ModifyPasswordActivity.this);
        alertdialog.setTitle("提示")
                .setMessage("密码已重置，请重新登录")
                .setLeftText("取消")
                .setRightText("确定")
                .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                    @Override
                    public void onLeftClick(View view) {
                        startActivity(LoginActivity.class);
                    }

                    @Override
                    public void onRightClick(View view) {
                        startActivity(LoginActivity.class);
                    }
                });
        alertdialog.setCancelable(false);
        alertdialog.show();



    }
}
