package com.handongkeji.login;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public interface ILoginUi {


    /**
     * 登录之前回调
     */
    void onPreLogin();


    /**
     * 请求登录结束
     */
    void onLoginEnd();

    /**
     * 登录成功
     */
    void onLoginSuccess(String uid,String token);

    /**
     * 登录失败
     */
    void onLoginFailed(String error);
}
