package com.ixiangni.presenters;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.TextBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class GetTextPresenter {



//    文本类型标识：1 帮助中心，2 关于我们，3 联系我们,4支付说明，5提现说明

    public static final String TEXT_HELP_CENTER = "1";
    public static final String TEXT_ABOUT_US = "2";
    public static final String TEXT_CONTACT_US = "3";
    public static final String TEXT_PAYMENT_INTRODUCE = "4";
    public static final String TEXT_GET_CASH_INTRODUCE = "5";


    private static final String TAG = "GetTextPresenter";
    public void getText(Context context, String textflag, OnResult<TextBean.DataBean> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put(XNConstants.textflag,textflag);
        RemoteDataHandler.asyncPost(UrlString.URL_GET_TEXT,params,context,true, response -> {
            String json = response.getJson();
            if(CommonUtils.isStringNull(json)){
                callback.onFailed(Constants.CONNECT_SERVER_FAILED);
            }else {
                if(BaseActivity.DEBUG){
                    Log.i(TAG, "getText: "+json);
                }
                TextBean textBean = new Gson().fromJson(json, TextBean.class);
                if(1==textBean.getStatus()){
                    callback.onSuccess(textBean.getData());
                }else {
                    callback.onFailed(textBean.getMessage());
                }
            }
        });

    }


}
