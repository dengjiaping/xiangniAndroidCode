package com.ixiangni.presenters;

import android.content.Context;
import android.text.TextUtils;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.MyApp;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.MyPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class AddFriendPresenter {

    public void addFriend(Context context, String friendid, int showType, OnResult<String> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("friendid",friendid);
        params.put("showType",""+showType);
        RemoteDataHandler.asyncPost(UrlString.URL_ADD_FRIEND,params,context,false, response -> {
            String json = response.getJson();
            if(!TextUtils.isEmpty(json)){
                try {
                    JSONObject object = new JSONObject(json);
                    int anInt = object.getInt(Constants.status);
                    if(1==anInt){
                        callback.onSuccess("添加成功！");
                    }else {
                        callback.onFailed(object.getString(Constants.message));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                callback.onFailed(Constants.CONNECT_SERVER_FAILED);
            }

        });
    }

    /**
     * 回复信息
     *
     token	是	String	token
     receiverId	是	Long	用户id
     friendshipid	是	Long	用户关系表主键
     msgContent	否	String	消息的内容

     */
    public void reply(Context context,String receiverId,String friendshipid,String msgContent,OnResult<String> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put("token",MyApp.getInstance().getUserTicket());
        params.put("receiverId",receiverId);
        params.put("friendshipid",friendshipid);
        params.put("msgContent",msgContent);
        MyPresenter.request(context,UrlString.URL_REPLY_FRIENDS,params,callback);
    }

}
