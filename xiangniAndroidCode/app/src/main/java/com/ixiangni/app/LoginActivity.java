package com.ixiangni.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.handongkeji.login.ILoginUi;
import com.handongkeji.login.LoginPresenter;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.app.user.FindPasswordActivity;
import com.ixiangni.constants.UrlString;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class LoginActivity extends BaseActivity implements ILoginUi {

    @Bind(R.id.edt_id_card_num)
    EditText edtIdCardNum;
    @Bind(R.id.edt_password)
    EditText edtPassword;
    @Bind(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @Bind(R.id.btn_login)
    //            登录按钮
    public Button btnLogin;
    //            登录的用户名
    private String idCardNum;
    //            登录的密码
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       添加当前的activity的布局文件
        setContentView(R.layout.activity_login);
//        初始化控件
        ButterKnife.bind(this);
//        让当前的控件获取焦点
        edtIdCardNum.requestFocus();
    }

    //    当前的两个按钮的点击事件的处理逻辑
    @OnClick({R.id.tv_forget_password, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_password:
//                忘记密码
                startActivity(FindPasswordActivity.class);
                break;
            case R.id.btn_login:
//                隐藏软键盘
                hideSoftKeyboard();
                //尝试登录
                tryLogin();
                break;
        }
    }

    /**
     * 验证用户名和密码
     */
    private void tryLogin() {
//        获取两个控件的输入的内容
        idCardNum = edtIdCardNum.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(idCardNum)) {
            toast("身份证号不能为空！");
            return;
        }

//        if (!RegexUtils.checkIdCard(idCardNum)) {
//            toast("身份证号错误！");
//            return;
//        }

        if (TextUtils.isEmpty(password)) {
            toast("请输入密码！");
        }

        startLogin(idCardNum, password);
    }

    /**
     * 登录
     *
     * @param idCardNum 用户名
     * @param password  密码
     */
    private void startLogin(String idCardNum, String password) {
//        创建一个map集合存储用户的数据
        HashMap<String, String> params = new HashMap<>();
        params.put("usernumber", idCardNum);
        params.put("password", password);
        params.put("appType", "1");
//      打印用户的登录信息
        log(params.toString());
//       登录的功能的实现
        new LoginPresenter(UrlString.BASE_URL).login(this, params);
    }

    //    登录之前的回调的方法 弹出对话框显示正在登录
    @Override
    public void onPreLogin() {
        showProgressDialog("登录中...", false);
    }

    //    登录结束的回调的方法 销毁对话框
    @Override
    public void onLoginEnd() {

        dismissProgressDialog();
    }

    //   登陆成功的回调方法
    @Override
    public void onLoginSuccess(String uid, String token) {

        toast("登录成功！");
        log(uid);
        LoginHelper.getInstance().login(this, token, uid, idCardNum, password);
//      跳转到主界面
        startActivity(MainActivity.class);
//      销毁当前的activity
        finish();
    }

    @Override
    public void onBackPressed() {

//        System.exit(0);
//        super.onBackPressed();

        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);

        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        mContext.startActivity(mHomeIntent);

    }

    @Override
    public void onLoginFailed(String error) {
        toast(error);
    }
}
