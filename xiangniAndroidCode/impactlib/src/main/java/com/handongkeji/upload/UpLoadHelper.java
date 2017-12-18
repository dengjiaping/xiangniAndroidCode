package com.handongkeji.upload;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.handongkeji.common.HttpHelper;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.modle.ResponseData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

 class UpLoadHelper {
    ////////////////////////////////////////////////////////////////////
    //                          _ooOoo_                               //
    //                         o8888888o                              //
    //                         88" . "88                              //
    //                         (| ^_^ |)                              //
    //                         O\  =  /O                              //
    //                      ____/`---'\____                           //
    //                    .'  \\|     |//  `.                         //
    //                   /  \\|||  :  |||//  \                        //
    //                  /  _||||| -:- |||||-  \                       //
    //                  |   | \\\  -  /// |   |                       //
    //                  | \_|  ''\---/''  |   |                       //
    //                  \  .-\__  `-`  ___/-. /                       //
    //                ___`. .'  /--.--\  `. . ___                     //
    //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
    //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
    //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
    //      ========`-.____`-.___\_____/___.-`____.-'========         //
    //                           `=---='                              //
    //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
    //         佛祖保佑       永无BUG     永不修改                  //
    ////////////////////////////////////////////////////////////////////


    public interface UpLoadResult {
        void onResult(String imgUrl, int position);

        void onAllUpLoadFinish();
    }

    private ExecutorService imgPool = RemoteDataHandler.threadPool;


    //异步上传单张图片
    public static void asyncUpLoadImage(String url,String filemark,File file, RemoteDataHandler.Callback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("filemark", filemark);
        HashMap<String, File> fileMap = new HashMap<>();
        fileMap.put("file", file);
        RemoteDataHandler.asyncMultipartPost(url, params, fileMap, callback);
    }


    //同步上传单张图片
    public void syncUpLoadImage(String url,File file, RemoteDataHandler.Callback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("filemark", "3");
        HashMap<String, File> fileMap = new HashMap<>();
        fileMap.put("file", file);
        try {
            String result = HttpHelper.multipartPost(url, params, fileMap);
            ResponseData responseData = new ResponseData();
            responseData.setJson(result);
            callback.dataLoaded(responseData);

        } catch (IOException e) {
            e.printStackTrace();
            callback.dataLoaded(null);
        }
    }


//    //同步上传单张图片
//    public void syncUpLoadImage(String imgPath, RemoteDataHandler.Callback callback) {
//        File file = new File(imgPath);
//        this.syncUpLoadImage(file, callback);
//    }


    public void asyncUpLoadMultiImage(final Context context,final String url, @NonNull final List<String> imgPaths, final UpLoadResult callBack) {

        Looper mainLooper = context.getMainLooper();
        final Handler handler = new Handler(mainLooper);

        imgPool.execute(new Runnable() {
            @Override
            public void run() {
                final CountDownLatch latch = new CountDownLatch(imgPaths.size());

                for (int i = 0; i < imgPaths.size(); i++) {
                    String imgPath = imgPaths.get(i);
                    final File file = new File(imgPath);
                    Log.i("AskForJiZhangActivity", "file: " + i + "size:" + file.length());


                    final int position = i;
                    imgPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("filemark", "3");
                            HashMap<String, File> fileMap = new HashMap<>();

                            fileMap.put("file", file);
                            try {
                                final String result = HttpHelper.multipartPost(url, params, fileMap);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callBack.onResult(result, position);
                                    }
                                });

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            latch.countDown();
                        }
                    });
                }

                try {
                    latch.await();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onAllUpLoadFinish();
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });


    }


}
