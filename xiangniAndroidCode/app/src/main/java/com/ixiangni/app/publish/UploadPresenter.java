package com.ixiangni.app.publish;

import android.content.Context;
import android.text.TextUtils;

import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.MyApp;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.MyPresenter;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/17 0017.
 */

public class UploadPresenter {

//    token	是	String	token
//    folderid	是	Long	文件夹id
//    recordtype	是	int	记录类型 1：照片2：语音，3：视频，4：文字
//    memortydir	否	String	存储路径  存储 照片，语音，视频 的url链接
//    textcotent	否	String	文字内容  上传文字的内容
//    recordname	是	String	文件名（或者文字的标题）
//    mediatime	否	Stirng	音频文件的时长


    public static final int UP_IMG=1;
    public static final int UP_AUDIO=2;
    public static final int UP_VIDEO=3;
    public static final int UP_TEXT=4;


    public void upload(Context context, String folderid, int recordtype,
                       String memortydir, String textcotent, String recordname, String mediatime, OnResult<String> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("folderid",folderid);
        params.put("recordtype",""+recordtype);
        if(!TextUtils.isEmpty(memortydir)){
            params.put("memortydir",memortydir);
        }

        if(!TextUtils.isEmpty(textcotent)){
            params.put("textcotent",textcotent);
        }
        if(!TextUtils.isEmpty(recordname)){
            params.put("recordname",recordname);
        }
        if(!TextUtils.isEmpty(mediatime)){
            params.put("mediatime",mediatime);
        }
        MyPresenter.request(context,UrlString.URL_ADD_FILE,params,callback);

    }

}
