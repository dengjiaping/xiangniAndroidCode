package com.handongkeji.login;


import android.content.Context;
import android.text.TextUtils;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.modle.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 校验手机验证码
 * @ClassName:CheckverificationcodePresenter

 * @PackageName:com.handongkeji.login

 * @Create On 2017/6/23 0023   13:33

 * @Site:http://www.handongkeji.com

 * @author:xuchuanting

 * @Copyrights 2017/6/23 0023 handongkeji All rights reserved.
 */
public class CheckverificationcodeUtil {

    public static void check(final Context context, String baseurl, String phoneNum, String code, final OnResult<String> callback){

        String url = baseurl+"mbUserPer/checkCode.json";
        HashMap<String, String> params = new HashMap<>();
        params.put("phoneNum",phoneNum);
        params.put("code",code);
        RemoteDataHandler.asyncPost(url, params, context, false, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData response) {
                if(context!=null&&callback!=null){
                    String json = response.getJson();
                    if(TextUtils.isEmpty(json)){
                        callback.onFailed(Constants.CONNECT_SERVER_FAILED);
                        return;
                    }
                    try {
                        JSONObject object = new JSONObject(json);
                        int status = object.getInt("status");
                        String message = object.getString("message");
                        if(1==status){
                            callback.onSuccess(message);
                        }else {
                            callback.onFailed(message);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
