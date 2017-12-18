package com.ixiangni.records;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.util.ToastUtils;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.modle.Message;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.constants.UrlString;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/17.
 */
public class PublishAllPresenter {

    public static void publish(Context context, HashMap<String,String> params, OnResult<String> ca){
        RemoteDataHandler.asyncPost(UrlString.URL_PUBLISH_MISSYOUCIRCLE, params, context, false, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData response) {
                String json = response.getJson();
                if(BaseActivity.DEBUG){
                    Log.i("PublishAllPresenter", "dataLoaded: "+json);
                }
                if(TextUtils.isEmpty(json)){
                    ca.onFailed(Constants.CONNECT_SERVER_FAILED);
                }else {
                    try {
                        JSONObject object = new JSONObject(json);
                        if(1==object.getInt(Constants.status)){
                            ca.onSuccess("发布成功！");
                        }else {
                            ca.onFailed(object.getString(Constants.message));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private PublishAllPresenter.IView view;

    public PublishAllPresenter(PublishAllPresenter.IView view, Context context) {
        this.view =  view;
    }
    public void getParam(HashMap<String,String> params,Context context){
//        params.get("token");
//        params.get("momentnewscontent");//极友圈内容
//        params.get("inserttype");//插入类别 0：不插入 1：插入图片 2：插入语音 3：插入视频
//        params.get("momentlng");//经度
//        params.get("momentlat");//纬度
//        params.get("momentnewsaddress");//极友圈地标
//        params.get("showtype");//可见类型  0：全部  1：好友
//        params.get("inserturl");//插入链接 当插入视频和语音是使用该字段传url
//        params.get("insertid");//插入图片的Id串   逗号隔开  5,6,8,9
//        params.get("mediatime");//录音时间
        String url= UrlString.URL_PUBLISH_MISSYOUCIRCLE;

        RemoteDataHandler.asyncTokenPost(url, context, true, params, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                String json = data.getJson();
                if (!CommonUtils.isStringNull(json)){
                    try {
                        JSONObject object=new JSONObject(json);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        if ("1".equals(status)){
                            //成功
                            if (view!=null){
                                view.getDataFinish(status,message);
                            }
                            return;
                        }else {
                            ToastUtils.show(context,message);
                            view.onError();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    ToastUtils.show(context, Constants.CONNECT_SERVER_FAILED);
                    view.onError();
                }
                if (view == null) {
                    view.onError();
                }
            }
        });

    }

    public interface IView{
        /**获得数据结束之后*/
        void getDataFinish(String status, String message);
        void onError();
    }
}
