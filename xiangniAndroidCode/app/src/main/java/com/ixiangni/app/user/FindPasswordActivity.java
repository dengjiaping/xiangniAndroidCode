package com.ixiangni.app.user;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.login.IdentifyCodePresenter;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.utils.RegexUtils;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.constants.UrlString;
import com.nevermore.oceans.widget.CountDownButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 密码找回
 *
 * @ClassName:FindPasswordActivity
 * @PackageName:com.ixiangni.app.user
 * @Create On 2017/6/19 0019   11:25
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/19 0019 handongkeji All rights reserved.
 */
public class FindPasswordActivity extends BaseActivity implements IdentifyCodePresenter.ISendResult {

    @Bind(R.id.edt_phone)
    EditText edtPhone;
    @Bind(R.id.count_down_button)
    CountDownButton countDownButton;
    @Bind(R.id.edt_new_password)
    EditText edtNewPassword;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    @Bind(R.id.edt_code)
    EditText edtCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.count_down_button, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.count_down_button:
                trySendCode();
                break;
            case R.id.btn_confirm:
                tryChangePassword();
                break;
        }
    }

    /**
     * 修改密码
     */
    private void tryChangePassword() {
        String phoneNum = edtPhone.getText().toString().trim();

        if(!RegexUtils.checkMobile(phoneNum)){
            toast(Constants.ERROR_PHONE_NUMBER);
            return;
        }

        String code = edtCode.getText().toString().trim();
        if(TextUtils.isEmpty(code)){
            toast("请填写验证码");
            return;
        }

        String password = edtNewPassword.getText().toString().trim();

        if(TextUtils.isEmpty(password)){
            toast("请输入新密码！");
            return;
        }
        if(password.length()>15||password.length()<6){
            toast("请输入6-15位密码！");
            return;
        }


        modifyPassword(phoneNum,password,code);
    }

    private void modifyPassword(String phoneNum, String password, String code) {

        HashMap<String, String> params = new HashMap<>();

        params.put("phoneNum",phoneNum);
        params.put("userCode",code);
        params.put("password",password);
        params.put("appType","1");

        showProgressDialog("修改中...",false);
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
                                finish();
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

    private void trySendCode() {
        String phoneNum = edtPhone.getText().toString().trim();
        if (!RegexUtils.checkMobile(phoneNum)) {
            toast("手机号不正确！");
            return;
        }

        new IdentifyCodePresenter(UrlString.BASE_URL).getCode(this, IdentifyCodePresenter.FOR_CHANGE_PASSWORD, phoneNum);

    }

    @Override
    protected void onDestroy() {
        if (countDownButton != null) {
            countDownButton.cancel();
        }
        super.onDestroy();

    }

    @Override
    public void onSendSuccess(String message) {

        toast("发送成功！");
        countDownButton.start();
    }

    @Override
    public void onSendFailed(String message) {

        toast(message);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
