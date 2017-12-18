package com.ixiangni.presenters;

import android.content.Context;
import android.text.TextUtils;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.util.ToastUtils;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.constants.UrlString;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class ModifySettingPresenter {


//    token	是	String	token
//    getSysMessage	否	Integer	接受消息
//    remind	否	Integer	想你圈更新提醒
//    applyAdd	否	Integer	允许加好友
//    authentication	否	Integer	加好友验证

    public void changeSetting(Context context, final OnResult<String> callback,String getSysMessage,
                              String remind, String applyAdd, String authentication){

        if (!LoginHelper.getInstance().isLogin(context)) {
            ToastUtils.show(context,"请先登录！");
            return;
        }

        HashMap<String, String> params = new HashMap<>();


        if(!TextUtils.isEmpty(getSysMessage)){
            params.put("getSysMessage",getSysMessage);
        }

        if (!TextUtils.isEmpty(remind)) {
            params.put("remind",remind);
        }
        if (!TextUtils.isEmpty(applyAdd)) {
            params.put("applyAdd",applyAdd);
        }

        if (!TextUtils.isEmpty(authentication)) {
            params.put("authentication",authentication);
        }

        params.put("token",LoginHelper.getInstance().getToken(context));

        RemoteDataHandler.asyncPost(UrlString.URL_CHANGE_SETTING,params,context,false, response -> {
            if(context!=null){
                String json = response.getJson();
                if(TextUtils.isEmpty(json)){
                    if(callback!=null){
                        callback.onFailed(Constants.CONNECT_SERVER_FAILED);
                    }

                }else {

                    try {
                        JSONObject object = new JSONObject(json);
                        int anInt = object.getInt(Constants.status);
                        if(1==anInt){
                            if(callback!=null){
                                callback.onSuccess("修改成功！");
                            }
                        }else {
                            if(callback!=null){
                                callback.onFailed(object.getString(Constants.message));
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

    }

}
