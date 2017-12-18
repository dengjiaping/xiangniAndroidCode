package com.handongkeji.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.modle.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 登录接口只是baseurl,he参数不同，可以封装
 * @ClassName:LoginPresenter
 * @PackageName:com.handongkeji.login
 * @Create On 2017/6/23 0023   09:28
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/23 0023 handongkeji All rights reserved.
 */

public class LoginPresenter implements RemoteDataHandler.Callback {


    private final String URL_LOGIN;

    private ILoginUi mLoginUi = null;

    public LoginPresenter(@NonNull String baseUrl) {
        URL_LOGIN = baseUrl+"mbuser/login.json";

    }

    public void login(final ILoginUi loginUi,HashMap<String, String> params) {
        mLoginUi = loginUi;

        Context context = null;

        if (loginUi instanceof Activity) {//在Activity里登录用Activity实现ILoginUI接口
            context = (Context) loginUi;
        } else if (loginUi instanceof Fragment) {//在Fragment里登录用Fragment实现ILoginUI接口
            context = ((Fragment) loginUi).getActivity();
        }

        if (context == null) {
            throw new RuntimeException("context 为null");
        }

        mLoginUi.onPreLogin();
        RemoteDataHandler.asyncPost(URL_LOGIN, params, context, false, this);

    }

    @Override
    public void dataLoaded(ResponseData response) {
        if (mLoginUi != null) {//Activiy关闭context=null,不解析

            //回调请求结束
            mLoginUi.onLoginEnd();

            String json = response.getJson();
            if (TextUtils.isEmpty(json)) {
                //json为null,提示连接服务器失败
                mLoginUi.onLoginFailed(Constants.CONNECT_SERVER_FAILED);
            } else {
                try {
                    JSONObject object = new JSONObject(json);
                    int status = object.getInt("status");

                    String message = object.getString("message");
                    if (status == 1) {//登录成功
                        JSONObject data = object.getJSONObject("data");
                        String uid = data.getString("uid");
                        String token = data.getString("token");
                        mLoginUi.onLoginSuccess(uid, token);

                    } else {//登录失败
                        mLoginUi.onLoginFailed(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
