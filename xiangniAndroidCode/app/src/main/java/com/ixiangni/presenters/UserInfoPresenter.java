package com.ixiangni.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.modle.ResponseData;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.bean.UserInfoBean;
import com.ixiangni.constants.UrlString;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class UserInfoPresenter implements RemoteDataHandler.Callback{

    private static final String TAG = "UserInfoPresenter";
    private OnResult<UserInfoBean.DataBean> mCallback;
    private Context mContext;

    public void getUserInfo(Context context, @NonNull OnResult<UserInfoBean.DataBean> callback){
        this.mContext = context;
        this.mCallback = callback;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", LoginHelper.getInstance().getToken(context));
        RemoteDataHandler.asyncPost(UrlString.URL_GET_USER_INFO,params,context,true,this);
    }

    @Override
    public void dataLoaded(ResponseData response) {
        if(mContext==null){
            return;
        }
        String json = response.getJson();
        if(TextUtils.isEmpty(json)){

            if(BaseActivity.DEBUG){
                Log.i(TAG, "dataLoaded: "+json);
            }
            if(mCallback!=null){
                mCallback.onFailed(Constants.CONNECT_SERVER_FAILED);
            }
        }else {
            Log.i(TAG, "dataLoaded: "+json);
            UserInfoBean userInfoBean = new Gson().fromJson(json, UserInfoBean.class);
            if(1==userInfoBean.getStatus()){
                mCallback.onSuccess(userInfoBean.getData());
            }else {
                mCallback.onFailed(userInfoBean.getMessage());
            }
        }
    }
}
