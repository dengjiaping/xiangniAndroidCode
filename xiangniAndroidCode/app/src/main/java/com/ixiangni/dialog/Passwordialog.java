package com.ixiangni.dialog;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.handongkeji.impactlib.dialog.XDialog;
import com.ixiangni.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/9.
 */

public class Passwordialog extends XDialog {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.edt_password)
    EditText edtPassword;
    @Bind(R.id.tv_left)
    TextView tvLeft;
    @Bind(R.id.tv_right)
    TextView tvRight;
    private final String mPassword;
    private final InputMethodManager im;

    public Passwordialog(Context context, String password) {
        super(context, R.layout.input_password);
        ButterKnife.bind(this);
        setCancelable(false);

        im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        mPassword = password;
    }

    @OnClick({R.id.tv_left, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                break;
            case R.id.tv_right:
                String password = edtPassword.getText().toString().trim();
                if(TextUtils.isEmpty(password)){
                    toast("请输入密码！");
                    return;
                }
                if(!mPassword.equals(password)){
                    toast("密码不正确!");
                    return;
                }
                if(mListener!=null){
                    mListener.onCorrectPassword();
                }

                break;
        }
        dismiss();
    }

    @Override
    public void show() {
        super.show();
        edtPassword.requestFocus();
        im.showSoftInput(edtPassword,InputMethodManager.SHOW_FORCED);

    }

    @Override
    public void dismiss() {
        super.dismiss();
        im.hideSoftInputFromWindow(edtPassword.getWindowToken(),0);

    }

    private OnPasswordResult mListener;

    public void setmListener(OnPasswordResult mListener) {
        this.mListener = mListener;
    }

    public interface OnPasswordResult{
        void onCorrectPassword();
    }
}
