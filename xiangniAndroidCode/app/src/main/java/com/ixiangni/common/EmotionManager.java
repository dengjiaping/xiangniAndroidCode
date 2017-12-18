package com.ixiangni.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.common.HttpHelper;
import com.handongkeji.common.MD5Encoder;
import com.handongkeji.handler.JsonCallback;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.util.ToastUtils;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.upload.UpLoadPresenter;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.domain.IXNEmotionGroupEntity;
import com.hyphenate.easeui.domain.IXNEmotionIcon;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.login.LoginState;
import com.ixiangni.bean.BuiedEmListBean;
import com.ixiangni.bean.CollectedListBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.interfaces.OnEmtionChange;
import com.ixiangni.presenters.OnGetNewEmotion;
import com.ixiangni.presenters.contract.MyPresenter;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

/**
 * 想你表情包管理类
 *
 * @ClassName:EmotionManager
 * @PackageName:com.ixiangni.common
 * @Create On 2017/7/11 0011   10:49
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/11 0011 handongkeji All rights reserved.
 */

public class EmotionManager implements LoginState, OnGetNewEmotion {

    private static EmotionManager instance = new EmotionManager();
    private Context mContext;
    private final String cacheDirName = "emotions";
    private File emotionDir;
    private EaseEmojiconGroupEntity collectEmGroupEntity;


    public static EmotionManager getInstance() {
        return instance;
    }

    private EmotionManager() {
    }

    public void init(Context context) {
        this.mContext = context;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            emotionDir = new File(mContext.getExternalCacheDir(), cacheDirName);
        } else {
            emotionDir = new File(mContext.getCacheDir(), cacheDirName);
        }
        Log.i(TAG, "init: " + emotionDir.getAbsolutePath());

        SuperObservableManager som = SuperObservableManager.getInstance();
        som.getObservable(LoginState.class)
                .registerObserver(this);
        som.getObservable(OnGetNewEmotion.class)
                .registerObserver(this);
    }


    public void loadEmotions() {
        SuperObservableManager
                .getInstance()
                .getObservable(OnGetNewEmotion.class)
                .notifyMethod(OnGetNewEmotion::onGet);
    }

    public void notifyEmotionChange() {
        loadEmotions();
    }

    @Override
    public void onLogin() {
        getAllIXNEmotion();
    }


    @Override
    public void onLogout() {

        emojiconGroups = null;
    }

    public EaseEmojiconGroupEntity getCollectEmGroupEntity() {
        return collectEmGroupEntity;
    }

    private void getCollectedEmotionList(CountDownLatch latch) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put(XNConstants.pageSize, "1000");
        params.put(XNConstants.currentPage, "1");
        RemoteDataHandler.syncPost(mContext, UrlString.URL_COLLECTED_EMOTION_LIST, params, new JsonCallback() {
            @Override
            public void onSuccess(String json) {

                Log.i(TAG, "收藏表情列表: " + json);
                CollectedListBean collectedListBean = new Gson().fromJson(json, CollectedListBean.class);
                if (collectedListBean.getStatus() == 1) {
                    saveCollectedEmotions(collectedListBean.getData());
                }
                latch.countDown();
            }

            @Override
            public void onFailed(Throwable throwable) {
                latch.countDown();
            }
        });
    }

    /**
     * 保存
     *
     * @param dataList
     */
    private void saveCollectedEmotions(List<CollectedListBean.DataBean> dataList) {
        collectEmGroupEntity = null;
        try {
            File favorDir = new File(emotionDir, "favorem");

//            File favorDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!ListUtil.isEmptyList(dataList) && FileHelper.creatDirIfNotExist(favorDir)) {
                List<EaseEmojicon> emojiList = new ArrayList<>();
                collectEmGroupEntity = new EaseEmojiconGroupEntity(R.mipmap.dianzanred, emojiList, EaseEmojicon.Type.BIG_EXPRESSION);
                for (int i = 0; i < dataList.size(); i++) {
                    CollectedListBean.DataBean dataBean = dataList.get(i);
                    String browinfo = dataBean.getBrowinfo();
                    String imgName = getLocalFileNameByUrl(browinfo);
                    File imgFile = new File(favorDir, imgName);
                    if (!imgFile.exists()) {
                        HttpHelper.downloadFile(browinfo, imgFile, new OnResult<String>() {
                            @Override
                            public void onSuccess(String s) {
                                try {
                                    MediaStore.Images.Media.insertImage(mContext.getContentResolver(), imgFile.getAbsolutePath(), null, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                IXNEmotionIcon emotionIcon = new IXNEmotionIcon(imgFile, browinfo);
                                emojiList.add(emotionIcon);
                            }

                            @Override
                            public void onFailed(String errorMsg) {

                            }
                        });

                    }

                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 表情数量发生变化
     */
    @Override
    public void onGet() {
        getAllIXNEmotion();

    }

    private void getAllIXNEmotion() {
        RemoteDataHandler.threadPool.execute(() -> {
            CountDownLatch latch = new CountDownLatch(2);

            //获取收藏的表情列表
            getCollectedEmotionList(latch);
            //获取已购买的表情列表
            getEmotionList(latch);

            try {
                latch.await();
                //表情变化
                Observable.just(null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object o) {
                                SuperObservableManager.getInstance().getObservable(OnEmtionChange.class)
                                        .notifyMethod(OnEmtionChange::emotionChange);
                            }
                        });


            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        });

    }

    public static final String TAG = "EmotionManager";

    private void getEmotionList(CountDownLatch latch) {

        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());

        RemoteDataHandler.syncPost(mContext, UrlString.URL_BROUGHT_EMOTION_LIST, params, new JsonCallback() {
            @Override
            public void onSuccess(String json) {
                Log.i(TAG, "getEmotionList: " + json);
                if (!CommonUtils.isStringNull(json)) {
                    BuiedEmListBean buiedEmListBean = new Gson().fromJson(json, BuiedEmListBean.class);
                    if (1 == buiedEmListBean.getStatus()) {
                        saveEmotions(buiedEmListBean.getData());
                    }
                    latch.countDown();
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                latch.countDown();
            }
        });

    }

    private List<EaseEmojiconGroupEntity> emojiconGroups = null;

    /**
     * 获取已购买表情包列表
     *
     * @return
     */
    public List<EaseEmojiconGroupEntity> getEmojiconGroups() {
        return emojiconGroups;
    }


    /**
     * 若没有保存表情包到本地
     * work thread
     *
     * @param data
     */
    private void saveEmotions(List<BuiedEmListBean.DataBean> data) {

        emojiconGroups = new ArrayList<>();

        try {
            if (FileHelper.creatDirIfNotExist(emotionDir) && data != null) {
                Log.i(TAG, "emotionDir: " + emotionDir.getAbsolutePath());
                for (int i = 0; i < data.size(); i++) {
                    BuiedEmListBean.DataBean dataBean = data.get(i);

                    //表情包名称
                    String browbagname = dataBean.getBrowbagname();

                    //表情包封面图
                    String browbaginfo = dataBean.getBrowbaginfo();

                    String thumb = getLocalFileNameByUrl(browbaginfo);

                    File thumbFile = new File(emotionDir, thumb);

                    List<EaseEmojicon> emotionList = new ArrayList<>();
                    IXNEmotionGroupEntity groupEntity = new IXNEmotionGroupEntity(browbaginfo, thumbFile, emotionList);
                    emojiconGroups.add(groupEntity);

                    if (!thumbFile.exists()) {
                        RemoteDataHandler.threadPool.execute(() -> {
                            try {
                                HttpHelper.download(browbaginfo, thumbFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    Log.i(TAG, "browbagname: " + browbagname);
                    File bagDir = new File(emotionDir, browbagname);
                    FileHelper.creatDirIfNotExist(bagDir);
                    List<BuiedEmListBean.DataBean.UserBrowListBean> userBrowList = dataBean.getUserBrowList();
                    if (userBrowList != null) {
                        for (int j = 0; j < userBrowList.size(); j++) {
                            BuiedEmListBean.DataBean.UserBrowListBean userBrowListBean = userBrowList.get(j);
                            String browinfo = userBrowListBean.getBrowinfo();
                            String fileName = getLocalFileNameByUrl(browinfo);
                            Log.i(TAG, "browinfo: " + browinfo);
                            File file = new File(bagDir, fileName);
                            if (!file.exists()) {
                                RemoteDataHandler.threadPool.execute(() -> {
                                    try {
                                        Log.i(TAG, "正下载: " + browinfo);
                                        HttpHelper.download(browinfo, file);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            } else {
                                Log.i(TAG, "已下载: " + browbaginfo);
                            }
                            IXNEmotionIcon emotionIcon = new IXNEmotionIcon(file, browinfo);
                            emotionList.add(emotionIcon);


                        }
                    }
                }

                //表情变化
                Observable.just(null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object o) {
                                SuperObservableManager.getInstance().getObservable(OnEmtionChange.class)
                                        .notifyMethod(OnEmtionChange::emotionChange);
                            }
                        });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 收藏表情
     *
     * @param context
     * @param message
     * @param callback
     */
    public void collectEmotion(Context context, EMMessage message, OnResult<String> callback) {
        EMImageMessageBody imgBody = (EMImageMessageBody) message.getBody();
        // received messages
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                //图片未下载完成不能收藏
                ToastUtils.show(context, "图片正在加载，请稍等...");
            } else {
                String filePath = imgBody.getLocalUrl();
                upLoadImageAndCollect(context, filePath, callback);
            }
        } else {
            String filePath = imgBody.getLocalUrl();


            Drawable fromPath = Drawable.createFromPath(filePath);
            if (filePath == null) {
                callback.onFailed(null);
                return;
            }

            upLoadImageAndCollect(context, filePath, callback);

        }

    }

    /**
     * 保存图的方法
     */
    public void saveMyImage(Context context, EMMessage emMessage, File file, OnResult<String> callback) {

        EMImageMessageBody imgBody = (EMImageMessageBody) emMessage.getBody();
        // received messages
        if (emMessage.direct() == EMMessage.Direct.RECEIVE) {
            if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                //图片未下载完成不能收藏
                ToastUtils.show(context, "图片正在加载，请稍等...");
            } else {
                String path = imgBody.getRemoteUrl();
                try {
                    HttpHelper.downloadFile(path, file, callback);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else {
            String filePath = imgBody.getLocalUrl();


            Drawable fromPath = Drawable.createFromPath(filePath);
            if (filePath == null) {
                callback.onFailed(null);
                return;
            }


        }


    }


    /**
     * 上传图片若不是以图片格式结尾会上传失败
     *
     * @param filePath
     * @return
     */
    private String wrapImgPath(String filePath) {
        String extendName = filePath.substring(filePath.lastIndexOf(".") + 1);

        if (!"png".equalsIgnoreCase(extendName) && !"jpg".equalsIgnoreCase(extendName) && !"jpeg".equalsIgnoreCase(extendName)) {
            filePath += ".jpg";
        }

        return filePath;
    }


    /**
     * 先上传获取图片地址，然后将该图片地址收藏到列表
     *
     * @param context
     * @param filePath
     * @param callback
     */
    private void upLoadImageAndCollect(Context context, String filePath, OnResult<String> callback) {
        File file = new File(filePath);

        UpLoadPresenter.upLoadImage(UrlString.URL_UPLOAD_IMAGE, file, "3", new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                Log.i(TAG, "collect上传成功");
                /*****************************开始收藏******************************/
                HashMap<String, String> params = new HashMap<String, String>();

                params.put(Constants.token, MyApp.getInstance().getUserTicket());
                params.put("browinfo", s);

                MyPresenter.request(context, UrlString.URL_COLLECT_EMOTION, params, new OnResult<String>() {
                    @Override
                    public void onSuccess(String s) {

                        Log.i(TAG, "collect收藏成功");
                        callback.onSuccess("收藏成功！");
                        getAllIXNEmotion();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Log.i(TAG, "collect收藏失败");

                        callback.onFailed(errorMsg);
                    }
                });

            }

            @Override
            public void onFailed(String errorMsg) {

                Log.i(TAG, "collect上传失败");
                callback.onFailed(errorMsg);
            }
        });
    }


    private String getLocalFileNameByUrl(String url) {
        String encode = MD5Encoder.encode(url);
        String extentName = url.substring(url.lastIndexOf(".") + 1);
        return encode + "." + extentName;
    }


}
