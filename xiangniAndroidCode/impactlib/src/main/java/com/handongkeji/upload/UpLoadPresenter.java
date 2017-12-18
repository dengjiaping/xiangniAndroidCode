package com.handongkeji.upload;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.handongkeji.common.Constants;
import com.handongkeji.common.HttpHelper;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.modle.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class UpLoadPresenter {

    private static final String TAG = "UpLoadPresenter";

    public static void upLoadImage(String url, File file, String filemark, final OnResult<String> result) {
        UpLoadHelper.asyncUpLoadImage(url, filemark, file, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData response) {
                if (result == null) {
                    return;
                }

                String json = response.getJson();
                if (json == null) {
                    result.onFailed("上传失败...");
                } else {
                    try {
                        JSONObject object = new JSONObject(json);
                        int status = object.getInt("status");
                        if (1 == status) {
                            String data = object.getString("data");
                            result.onSuccess(data);
                        } else {
                            result.onFailed(object.getString("message"));
                        }

                    } catch (JSONException e) {
                        result.onFailed("json异常");
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    public interface UpLoadImagesCallback {
        void onUploadFailed(int position, String imagePath);

        void onAllUpLoadFinish(List<String> imgUrls);
    }

    public static void upLoadMultiImage(final Context context, final String url, @NonNull final List<File> imgFiles, final UpLoadImagesCallback callback) {
        final Handler handler = new Handler(context.getMainLooper());
        final List<String> imgUrls = new ArrayList<>();

        RemoteDataHandler.threadPool.execute(new Runnable() {
            @Override
            public void run() {
                final CountDownLatch latch = new CountDownLatch(imgFiles.size());

                for (int i = 0; i < imgFiles.size(); i++) {

                    final File file = imgFiles.get(i);

                    final int position = i;
                    RemoteDataHandler.threadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("filemark", "3");
                            HashMap<String, File> fileMap = new HashMap<>();

                            fileMap.put("file", file);
                            final String result;
                            try {
                                result = HttpHelper.multipartPost(url, params, fileMap);
                                JSONObject object = new JSONObject(result);
                                if (1 == object.getInt("status")) {
                                    imgUrls.add(object.getString("data"));
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            callback.onUploadFailed(position, file.getAbsolutePath());
                                        }
                                    });
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
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
                            callback.onAllUpLoadFinish(imgUrls);
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
