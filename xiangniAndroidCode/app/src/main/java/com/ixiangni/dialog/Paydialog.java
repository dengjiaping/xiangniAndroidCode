package com.ixiangni.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.dialog.XDialog;
import com.handongkeji.interfaces.ICallback;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.user.ModifyPasswordActivity;
import com.ixiangni.bean.PassSetBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.MyPresenter;
import com.ixiangni.util.StateLayout;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class Paydialog extends XDialog {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.edt_password)
    EditText edtPassword;
    @Bind(R.id.tv_left)
    TextView tvLeft;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;

    public Paydialog(Context context) {
        super(context, R.layout.dialog_buy_emotin);
        ButterKnife.bind(this);
    }

    /**
     * 判断是否设置过支付密码
     *
     * @param showView
     */
    public void checkSetPayPassword(Activity activity,View showView) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        RemoteDataHandler.asyncPost(UrlString.URL_CHECK_PAY_SET, params, getContext(), false, response -> {
            String json = response.getJson();
            if (showView != null) {
                if (TextUtils.isEmpty(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    PassSetBean passSetBean = new Gson().fromJson(json, PassSetBean.class);
                    if (1 == passSetBean.getStatus()) {
                        int data = passSetBean.getData();
                        if (0 == data) {
                            MyAlertdialog alertdialog = new MyAlertdialog(getContext());
                            alertdialog.setCancelable(false);
                            alertdialog.setMessage("您还未设置支付密码，\n是否去设置？")
                                    .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                                        @Override
                                        public void onLeftClick(View view) {
                                            activity.finish();
                                        }

                                        @Override
                                        public void onRightClick(View view) {

                                            Intent intent = new Intent(getContext(), ModifyPasswordActivity.class);
                                            intent.putExtra(ModifyPasswordActivity.isLoginPassword, false);
                                            activity.startActivity(intent);
//                                            activity.finish();

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

    public void checkSetPayPassword(Activity activity, View showView, ICallback<String> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        RemoteDataHandler.asyncPost(UrlString.URL_CHECK_PAY_SET, params, getContext(), false, response -> {
            String json = response.getJson();
            if (showView != null) {
                callback.call("");
                if (TextUtils.isEmpty(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    PassSetBean passSetBean = new Gson().fromJson(json, PassSetBean.class);
                    if (1 == passSetBean.getStatus()) {
                        int data = passSetBean.getData();
                        if (0 == data) {
                            MyAlertdialog alertdialog = new MyAlertdialog(getContext());
                            alertdialog.setCancelable(false);
                            alertdialog.setMessage("您还未设置支付密码，\n是否去设置？")
                                    .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                                        @Override
                                        public void onLeftClick(View view) {
                                            activity.finish();
                                        }

                                        @Override
                                        public void onRightClick(View view) {

                                            Intent intent = new Intent(getContext(), ModifyPasswordActivity.class);
                                            intent.putExtra(ModifyPasswordActivity.isLoginPassword, false);
                                            activity.startActivity(intent);
//                                            activity.finish();

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

    @OnClick({R.id.tv_left, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                dismiss();
                break;
            case R.id.tv_right:
                String password = edtPassword.getText().toString().trim();
                if(TextUtils.isEmpty(password)){
                    toast("请输入支付密码！");
                    return;
                }

                if(mListener!=null){
                checkPassword(password);
                }
                if(passwordCallback!=null){
                    passwordCallback.call(password);
                }
                dismiss();

                break;
        }
    }

    public ICallback<String> passwordCallback;

    public ICallback<String> getPasswordCallback() {
        return passwordCallback;
    }

    public void setPasswordCallback(ICallback<String> passwordCallback) {
        this.passwordCallback = passwordCallback;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        edtPassword.setText("");
        stateLayout.showContenView();
    }

    private void checkPassword(String password) {
        //验证支付密码
        stateLayout.showLoadView("验证中...");
        HashMap<String, String> params = new HashMap<>();
        params.put("token",MyApp.getInstance().getUserTicket());
        params.put("paymentPass",password);

        MyPresenter.request(getContext(), UrlString.URL_CHEK_PAYPASWORD, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                dismiss();
                if(mListener!=null){
                    mListener.onCorrectInpu();
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                stateLayout.showContenView();
                toast(errorMsg);
            }
        });
    }

    private OnPasswordCorrect mListener;

    public void setmListener(OnPasswordCorrect mListener) {
        this.mListener = mListener;
    }

    public interface OnPasswordCorrect{
        void onCorrectInpu();
    }
}
