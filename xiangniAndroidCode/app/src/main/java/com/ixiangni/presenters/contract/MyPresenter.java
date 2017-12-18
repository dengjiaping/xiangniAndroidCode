package com.ixiangni.presenters.contract;

import android.content.Context;
import android.util.Log;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/6 0006.
 */

public class MyPresenter {

    /**
     * 提交修改类请求 根据status判断请求结果，
     * 无数据返回
     * @param context
     * @param url
     * @param params
     * @param callback
     */
    public static void request(Context context, String url, HashMap<String,String> params, OnResult<String> callback){
        RemoteDataHandler.asyncPost(url,params,context,false, response -> {

            String json = response.getJson();
            Log.i("MyPresenter", "request: "+json);

            handJson(response.getJson(),callback,"");
        });

    }
    public static void request1(Context context, String url, HashMap<String,String> params, OnResult<String> callback){
        RemoteDataHandler.asyncPost(url,params,context,false, response -> {
            handJson1(response.getJson(),callback,"");
        });

    }

    protected static void handJson(String json, OnResult<String> callback,String successmsg){
        if(CommonUtils.isStringNull(json)){
            if(callback!=null){
                callback.onFailed(Constants.CONNECT_SERVER_FAILED);
            }
        }else {
            try {
                JSONObject object = new JSONObject(json);
                int status = object.getInt(Constants.status);
                String message = object.getString(Constants.message);
                if(status == 1){
                    if(callback!=null){
                        callback.onSuccess(successmsg);
                    }
                }else {
                    if(callback!=null){
                        callback.onFailed(message);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                if(callback!=null){
                    callback.onFailed(e.getMessage());
                }
            }

        }
    }
    protected static void handJson1(String json, OnResult<String> callback,String successmsg){
        if(CommonUtils.isStringNull(json)){
            if(callback!=null){
                callback.onFailed(Constants.CONNECT_SERVER_FAILED);
            }
        }else {
            try {
                JSONObject object = new JSONObject(json);
                int status = object.getInt(Constants.status);
                String message = object.getString(Constants.message);
                if(status == 1){
                    if(callback!=null){
                        callback.onSuccess(successmsg);
                    }
                }else {
                    if(callback!=null){
                        if (status == 4) {
                            callback.onFailed("代理商余额不足");
                        }
                        if (status == 7) {
                            callback.onFailed("银信币余额不足");
                        }
                        callback.onFailed(message);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                if(callback!=null){
                    callback.onFailed(e.getMessage());
                }
            }

        }
    }
}
