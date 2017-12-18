package com.ixiangni.presenters.contract;

import android.content.Context;

import com.handongkeji.common.Constants;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.MyApp;
import com.ixiangni.constants.UrlString;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class ContactPresenter extends MyPresenter{

    public void deleteFriend(Context context, String friendid, OnResult<String> callback){

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("friendid",friendid);
        request(context, UrlString.URL_DELETE_FRIEND,params,callback);
    }

}
