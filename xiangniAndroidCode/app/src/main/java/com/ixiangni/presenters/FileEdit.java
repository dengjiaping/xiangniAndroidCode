package com.ixiangni.presenters;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class FileEdit {
    public interface IFileView{
        void showLoading(String message);
        void loadingFinish();
        void showToast(String message);
        void onDeleteSuccess(int posiion);
    }


    public interface IFilePresenter{

        void deleteFile(int position,String recordid);

        void requestFileDetail(String recordid);


    }

}
