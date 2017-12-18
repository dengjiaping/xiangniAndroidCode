package com.ixiangni.presenters;

import android.content.Context;
import android.text.style.TtsSpan;

import com.handongkeji.common.Constants;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.MyApp;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.MyPresenter;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class FriendCirclePresenter extends MyPresenter{

    /**
     *
     * @param context
     * @param momentnewsid      朋友圈id
     * @param flag           true：点赞，false：取消
     * @param callback
     */
    public static void dianzan(Context context,String momentnewsid,boolean flag, OnResult<String> callback){

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put(XNConstants.momentnewsid,momentnewsid);
        request(context, flag?UrlString.URL_ADD_LIKE:UrlString.URL_CANCEL_LIKE,params,callback);
    }

    /**
     * 转发朋友圈
     */
    public static void forwardToCircle(Context context,String momentnewsid,OnResult<String> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token,MyApp.getInstance().getUserTicket());
        params.put(XNConstants.momentnewsid, momentnewsid);
        request(context,UrlString.URL_FORWARD_CIRCLE,params,callback);
    }


    /**
     * 关注，取消关注
     */
    public static void followOrNot(Context context,String followuserid,boolean flag,OnResult<String> callback){

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token,MyApp.getInstance().getUserTicket());
        params.put("followuserid",followuserid);
        request(context,flag?UrlString.URL_ADD_FOLLOW:UrlString.URL_DELETE_FOLLOW,params,callback);

    }



}
