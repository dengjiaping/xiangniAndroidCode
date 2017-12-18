package com.ixiangni.presenters;

import android.content.Context;

import com.handongkeji.common.Constants;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.MyApp;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.MyPresenter;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class FilPresenter implements FileEdit.IFilePresenter {

    private final Context context;
    private FileEdit.IFileView iFileView;

    public FilPresenter(Context context,FileEdit.IFileView iFileView) {
        this.iFileView = iFileView;
        this.context= context;
    }

    @Override
    public void deleteFile(int position,String recordid) {

        iFileView.showLoading("删除中...");
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put(XNConstants.recordid,recordid);
        MyPresenter.request(context, UrlString.URL_DELETE_FILE, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {

                if(iFileView!=null){
                    iFileView.loadingFinish();
                    iFileView.showToast("删除成功");
                    iFileView.onDeleteSuccess(position);
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                if(iFileView!=null){
                    iFileView.loadingFinish();
                    iFileView.showToast(errorMsg);
                }
            }
        });

    }

    @Override
    public void requestFileDetail(String recordid) {

    }
}
