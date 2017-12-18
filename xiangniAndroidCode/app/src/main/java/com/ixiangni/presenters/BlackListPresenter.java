package com.ixiangni.presenters;

import android.content.Context;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chatuidemo.Constant;
import com.ixiangni.app.MyApp;
import com.ixiangni.constants.UrlString;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/4 0004.
 */

public class BlackListPresenter {



    public void addToBlackList(Context context, String friendid, OnResult<String> callback){

        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("friendid",friendid);
        RemoteDataHandler.asyncPost(UrlString.URL_ADD_BLACK_LIST,params,context,false, response -> {
            String json = response.getJson();
            if (CommonUtils.isStringNull(json)) {
                callback.onFailed(Constants.CONNECT_SERVER_FAILED);
            }else {
                try {
                    JSONObject object = new JSONObject(json);
                    String string = object.getString(Constants.message);
                    if(1==object.getInt(Constants.status)){
                        callback.onSuccess(string);
                    }else {
                        callback.onFailed(string);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }



    public void deleteFromBlackList(Context context,int blacklistid,OnResult<String> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put("token",MyApp.getInstance().getUserTicket());
        params.put("blacklistid",blacklistid+"");

        RemoteDataHandler.asyncPost(UrlString.URL_REMOVE_BLACK,params,context,false, response -> {
            String json = response.getJson();
            if(CommonUtils.isStringNull(json)){
                callback.onFailed(Constants.CONNECT_SERVER_FAILED);
            }else {
                try {
                    JSONObject object = new JSONObject(json);
                    int anInt = object.getInt(Constants.status);
                    String message = object.getString(Constants.message);
                    if(1==anInt){
                        callback.onSuccess(message);
                    }else {
                        callback.onFailed(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }
}
