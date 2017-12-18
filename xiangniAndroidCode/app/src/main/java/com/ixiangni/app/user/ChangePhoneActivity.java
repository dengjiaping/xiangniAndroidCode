package com.ixiangni.app.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.login.CheckverificationcodeUtil;
import com.handongkeji.login.IdentifyCodePresenter;
import com.handongkeji.utils.RegexUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.MyPresenter;
import com.nevermore.oceans.widget.CountDownButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 手机号修改
 *
 * @ClassName:ChangePhoneActivity
 * @PackageName:com.ixiangni.app.user
 * @Create On 2017/6/19 0019   15:07
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/19 0019 handongkeji All rights reserved.
 */
public class ChangePhoneActivity extends BaseActivity {

    @Bind(R.id.edt_new_phone)
    EditText edtNewPhone;
    @Bind(R.id.tv_code)
    TextView tvCode;
    @Bind(R.id.edt_code)
    EditText edtCode;
    @Bind(R.id.btn_send_code)
    CountDownButton btnSendCode;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    private IdentifyCodePresenter presenter;
    private String oldPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        ButterKnife.bind(this);
        oldPhoneNum = getIntent().getStringExtra("phoneNum");

        presenter = new IdentifyCodePresenter(UrlString.BASE_URL);

    }

    @OnClick({R.id.btn_send_code, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send_code:
                sendCode();
                break;
            case R.id.btn_confirm:
                changePhoneNum();
                break;
        }
    }

    private void changePhoneNum() {
        String trim = edtNewPhone.getText().toString().trim();
        if(!RegexUtils.checkMobile(trim)){
            toast("手机号格式不正确！");
            return;
        }

        String code = edtCode.getText().toString().trim();
        if(TextUtils.isEmpty(code)){
            toast("请填写验证码！");
            return;
        }

        showProgressDialog("修改中...",false);
        CheckverificationcodeUtil.check(this, UrlString.BASE_URL, trim, code, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {

                saveMobile(trim,code);
            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();
                toast(errorMsg);

            }
        });



    }

    /**
     * 绑定手机号
     * @param trim
     * @param code
     */
    private void saveMobile(String trim, String code) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("phone",trim);
        params.put("code",code);
        MyPresenter.request(this, UrlString.URL_BIND_PHONE, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                toast("修改成功！");
                dismissProgressDialog();
                Intent intent = new Intent();
                intent.putExtra("phoneNum",trim);
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();

                toast(errorMsg);

            }
        });
    }

    private void sendCode() {
        String trim = edtNewPhone.getText().toString().trim();
        if(!RegexUtils.checkMobile(trim)){
            toast("手机号格式不正确！");
            return;
        }

        presenter.getCode(new IdentifyCodePresenter.ISendResult() {
            @Override
            public void onSendSuccess(String message) {
                toast(message);
                btnSendCode.start();
            }

            @Override
            public void onSendFailed(String message) {
                toast(message);
            }

            @Override
            public Context getContext() {
                return ChangePhoneActivity.this;
            }
        },IdentifyCodePresenter.FOR_CHANGE_PHONE_NUMBER,trim);

    }

    @Override
    protected void onDestroy() {
        btnSendCode.cancel();
        super.onDestroy();
    }
}
