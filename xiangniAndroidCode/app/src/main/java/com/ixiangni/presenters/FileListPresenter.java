package com.ixiangni.presenters;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.modle.ResponseData;
import com.ixiangni.app.MyApp;
import com.ixiangni.bean.FileListBean;
import com.ixiangni.constants.UrlString;
import com.ta.utdid2.android.utils.StringUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class FileListPresenter {
    private Context context;
    private OnResult<List<FileListBean.DataBean>> callback;
    public FileListPresenter(Context context,OnResult<List<FileListBean.DataBean>> callback){
        this.context=context;
        this.callback=callback;
    }

    private static final String TAG = "FileListPresenter";


    public void initFileData(HashMap<String,String>params) {
        String url= UrlString.FILE_LIST_URL;

        RemoteDataHandler.asyncTokenPost(url, context, true, params, response -> {
            String json = response.getJson();

            if (!StringUtils.isEmpty(json)){
                Log.i(TAG, "initFileData: "+json);
                Gson gson=new Gson();
                FileListBean bean = gson.fromJson(json, FileListBean.class);
                int status = bean.getStatus();
                if (status==1){
                    List<FileListBean.DataBean> mlist=null;
                    try {
                        mlist = bean.getData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mlist.size()>0){
                        if (callback!=null){
                            callback.onSuccess(mlist);
                        }
                    }else {
                        if(callback!=null){
                            callback.onFailed(bean.getMessage());
                        }
                    }
                }else {
                    if (callback!=null){
                        callback.onFailed(bean.getMessage());
                    }
                }

            }
        });
    }

}
