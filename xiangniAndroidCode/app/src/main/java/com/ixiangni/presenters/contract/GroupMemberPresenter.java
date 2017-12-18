package com.ixiangni.presenters.contract;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.bean.GroupMLBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6 0006.
 */

public class GroupMemberPresenter {

    private static final String TAG = "GroupMemberPresenter";

    public void getMembers(Context context, String groupno, @NonNull OnResult<List<GroupMLBean.DataBean>> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put(XNConstants.groupchatno,groupno);
        RemoteDataHandler.asyncPost(UrlString.URL_GROUP_MEMBER,params,context,true, response -> {
            Log.i(TAG, "getMembers: "+response.getJson());
            String json = response.getJson();
            if(CommonUtils.isStringNull(json)){
                callback.onFailed(Constants.CONNECT_SERVER_FAILED);
            }else {
                GroupMLBean groupMLBean = new Gson().fromJson(json, GroupMLBean.class);
                if(1==groupMLBean.getStatus()){
                    callback.onSuccess(groupMLBean.getData());
                }else {
                    callback.onFailed(groupMLBean.getMessage());
                }
            }

        });
    }
}
