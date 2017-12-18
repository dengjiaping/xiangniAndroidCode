package com.ixiangni.presenters;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.bean.FolderListBean;
import com.ixiangni.constants.UrlString;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class FolderListPresenter {

    public void getFolderList(Context context, String searchText, OnResult<List<FolderListBean.DataBean>> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put("token", LoginHelper.getInstance().getToken(context));
        if (!TextUtils.isEmpty(searchText)) {
            params.put("searchtext", searchText);
        }
        RemoteDataHandler.asyncPost(UrlString.URL_FOLDER_LIST, params, context, true, callBack -> {
            if(context!=null){
                String json = callBack.getJson();
                if(TextUtils.isEmpty(json)){
                    callback.onFailed(Constants.CONNECT_SERVER_FAILED);
                }else {
                    FolderListBean folderListBean = new Gson().fromJson(json, FolderListBean.class);
                    if(1==folderListBean.getStatus()){
                        callback.onSuccess(folderListBean.getData());
                    }else {
                        callback.onFailed(folderListBean.getMessage());
                    }

                }
            }


        });
    }
}
