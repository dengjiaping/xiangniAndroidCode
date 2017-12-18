package com.handongkeji.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.modle.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

/**
 * 发送验证码就几种类型，baseurl不同可封装
 *
 * @ClassName:IdentifyCodePresenter
 * @PackageName:com.handongkeji.login
 * @Create On 2017/6/23 0023   10:54
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/23 0023 handongkeji All rights reserved.
 */

public final class IdentifyCodePresenter implements RemoteDataHandler.Callback {

    private final String URL_GET_IDENTIFY_CODE;
    private Context context;
    private ISendResult sendResult;


    @IntDef(flag = true, value = {FOR_REGIST, FOR_CHANGE_PASSWORD, FOR_CHANGE_PHONE_NUMBER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IdentifyCodeType {
    }

    /**
     * 验证码用于注册
     */
    public static final int FOR_REGIST = 0;
    /**
     * 验证码用于更换手机号
     */
    public static final int FOR_CHANGE_PHONE_NUMBER = 1;
    /**
     * 验证码用于修改密码
     */
    public static final int FOR_CHANGE_PASSWORD = 2;


    public interface ISendResult {
        void onSendSuccess(String message);

        void onSendFailed(String message);

        Context getContext();
    }

    public IdentifyCodePresenter(@NonNull String baseUrl) {
        this.URL_GET_IDENTIFY_CODE = baseUrl + "mbUserPer/getCode.json";
    }

    /**
     * 获取验证码
     */
    public void getCode(ISendResult sendResult, @IdentifyCodeType int forwhat, String phoneNum) {

        context = sendResult.getContext();

        this.sendResult = sendResult;
        HashMap<String, String> params = new HashMap<>();
        params.put("phoneNum", phoneNum);
        params.put("forwhat", getFlag(forwhat));

        RemoteDataHandler.asyncPost(URL_GET_IDENTIFY_CODE, params, context, false, this);
    }


    @Override
    public void dataLoaded(ResponseData response) {

        if(context==null){
            return;
        }
        String json = response.getJson();
        if(TextUtils.isEmpty(json)){
            if(this.sendResult!=null){
                sendResult.onSendFailed(Constants.CONNECT_SERVER_FAILED);
            }
            return;
        }

        try {
            JSONObject object = new JSONObject(json);
            int status = object.getInt("status");
            String message = object.getString("message");
            if(this.sendResult!=null){
                if(1==status){
                    sendResult.onSendSuccess(message);
                }else {
                    sendResult.onSendFailed(message);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static final String TAG = "IdentifyCodePresenter";


    /**
     * 通过用途获取flag参数
     * @param forwhat
     * @return
     */
    private String getFlag(@IdentifyCodeType int forwhat) {
        String flag = "1";
        switch (forwhat) {

            case FOR_REGIST://注册和修改手机号1
            case FOR_CHANGE_PHONE_NUMBER:
                break;
            case FOR_CHANGE_PASSWORD:
                flag = "2";
                break;
        }

        return flag;
    }
}
