package com.ixiangni.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.baidubce.BceClientException;
import com.baidubce.BceServiceException;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.CompleteMultipartUploadRequest;
import com.baidubce.services.bos.model.CompleteMultipartUploadResponse;
import com.baidubce.services.bos.model.InitiateMultipartUploadRequest;
import com.baidubce.services.bos.model.InitiateMultipartUploadResponse;
import com.baidubce.services.bos.model.PartETag;
import com.baidubce.services.bos.model.UploadPartRequest;
import com.baidubce.services.bos.model.UploadPartResponse;
import com.baidubce.util.BLog;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class BaiduUpLoad {

    public static final String ENDPOINT_BOS = "bj.bcebos.com";

    public static final String BAIDU_ACCESSKEY = "4c2a3d2a7d884478bc5c72427bce52e3";
    public static final String BAIDU_SECRETKEY = "3207512c60094ca88d66a706f350b37b";

    public static final String BUCKET_AUDIO = "ixiangniaud";
    public static final String BUCKET_VIDEO = "ixiangnivdo";
    private BosClient client;

    private static final String TAG = "BaiduUpLoad";

    private String path;
    private String buketName;
    private String objectKey;
    private OnResult<String> mCallback;

    public BaiduUpLoad(String path, String buketName, String objectKey) {
        this.path = path;
        this.buketName = buketName;
        this.objectKey = objectKey;
    }

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mCallback.onSuccess("上传成功！");
            } else {
                mCallback.onFailed("上传失败！");
            }
        }
    };


    public interface OnProgressCallback {
        void onProgress(int completePart, int totalpart);
    }

    private OnProgressCallback progressCallback;

    public OnProgressCallback getCallback() {
        return progressCallback;
    }

    public void setCallback(OnProgressCallback callback) {
        this.progressCallback = callback;
    }

    public void upload(OnResult<String> callback) {
        this.mCallback = callback;
        RemoteDataHandler.threadPool.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    //创建BosClientConfiguration实例
                    BosClientConfiguration config = new BosClientConfiguration();
                    config.setCredentials(new DefaultBceCredentials(BAIDU_ACCESSKEY, BAIDU_SECRETKEY));  //您的原始AK/SK
                    config.setEndpoint(ENDPOINT_BOS);   //Bucket所在区域

                    client = new BosClient(config);

                    // 开始Multipart Upload
                    InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(buketName, objectKey);
                    InitiateMultipartUploadResponse initiateMultipartUploadResponse =
                            client.initiateMultipartUpload(initiateMultipartUploadRequest);

                    // 设置每块为 5MB
                    final long partSize = 1024 * 1024 * 5L;

                    File partFile = new File(path);
                    // 计算分块数目
                    int partCount = (int) (partFile.length() / partSize);

                    if (partFile.length() % partSize != 0) {
                        partCount++;
                    }
                    Log.e("partCount", partCount + "");
                    // prpgressBar.setMax(partCount);
                    // 新建一个List保存每个分块上传后的ETag和PartNumber
                    List<PartETag> partETags = new ArrayList<PartETag>();

                    // 获取文件流
                    FileInputStream fis = new FileInputStream(partFile);
                    for (int j = 0; j < partCount; j++) {

                        // 计算每个分块的大小
                        long skipBytes = partSize * j;
                        long size = partSize < partFile.length() - skipBytes ?
                                partSize : partFile.length() - skipBytes;

                        byte[] buf = new byte[(int) size];
                        int offset = 0;

                        while (true) {
                            int byteRead = fis.read(buf, offset, (int) size);
                            offset += byteRead;
                            if (byteRead < 0 || offset >= size) {
                                break;
                            }
                        }
                        ByteArrayInputStream bufStream = new ByteArrayInputStream(buf);

                        BLog.enableLog();
                        // 创建UploadPartRequest，上传分块
                        UploadPartRequest uploadPartRequest = new UploadPartRequest();
                        uploadPartRequest.setBucketName(buketName);
                        uploadPartRequest.setKey(objectKey);
                        uploadPartRequest.setUploadId(initiateMultipartUploadResponse.getUploadId());
                        uploadPartRequest.setInputStream(bufStream);
                        uploadPartRequest.setPartSize(size);
                        uploadPartRequest.setPartNumber(j + 1);
                        UploadPartResponse uploadPartResponse = client.uploadPart(uploadPartRequest);
                        // 将返回的PartETag保存到List中。
                        PartETag tag = uploadPartResponse.getPartETag();
                        partETags.add(tag);
//                            Log.e("partETags", String.valueOf(uploadPartResponse.getPartETag()));


                        int complete = j+1;
                        int total= partCount;
                        if (progressCallback != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    progressCallback.onProgress(complete, total);
                                }
                            });
                        }

                        // prpgressBar.setProgress(j);
                    }
                    // 关闭文件
                    fis.close();

                    CompleteMultipartUploadRequest completeMultipartUploadRequest =
                            new CompleteMultipartUploadRequest(buketName, objectKey, initiateMultipartUploadResponse.getUploadId(), partETags);

                    // 完成分块上传
                    CompleteMultipartUploadResponse completeMultipartUploadResponse =
                            client.completeMultipartUpload(completeMultipartUploadRequest);

                    // 打印Object的ETag
//                        Log.e("etag", completeMultipartUploadResponse.getETag());
//                EventBus.getDefault().post("a","UpdataToServer");

                    mHandler.sendEmptyMessage(1);

                } catch (BceServiceException e) {
                    Log.e(TAG, "Error ErrorCode: " + e.getErrorCode());
                    Log.e(TAG, "Error RequestId: " + e.getRequestId());
                    Log.e(TAG, "Error StatusCode: " + String.valueOf(e.getStatusCode()));
                    Log.e(TAG, "Error Message: " + e.getMessage());
                    Log.e(TAG, "Error ErrorType: " + String.valueOf(e.getErrorType()));
                    mHandler.sendEmptyMessage(0);
                } catch (BceClientException e) {
                    Log.e(TAG, "Error Message: " + e.getMessage());
                    mHandler.sendEmptyMessage(0);

                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(0);

                }
            }
        });
    }


}


