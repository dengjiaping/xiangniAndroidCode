package com.ixiangni.app;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.clcus.EmojiBean;
import com.easemob.redpacketsdk.RPInitRedPacketCallback;
import com.easemob.redpacketsdk.RPValueCallback;
import com.easemob.redpacketsdk.RedPacket;
import com.easemob.redpacketsdk.bean.RedPacketInfo;
import com.easemob.redpacketsdk.bean.TokenData;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.handongkeji.autoupdata.CheckVersion;
import com.handongkeji.common.Constants;
import com.handongkeji.common.SystemHelper;
import com.handongkeji.impactlib.util.ToastUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.common.EmotionManager;
import com.ixiangni.common.GlideImageloader;
import com.ixiangni.constants.UrlString;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.List;

//import android.support.multidex.MultiDex;

//import android.support.multidex.MultiDex;

/**
 * Created by Administrator on 2017/6/16 0016.
 * 注释的添加和二次的开发
 */

public class MyApp extends Application {

//    设置屏幕的高度
    private static int screenHeight;
//    设置屏幕的宽度
    private static int screenWidth;
//    友盟推送的相关的知识
    private PushAgent mPushAgent;
//   存放表情数据的集合
    public List<EmojiBean> iterator;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        init();
        initYoumeng();
//        iterator = EmojiGetter.getInstance().getEmojiBeanList();
        CheckVersion.checkUrl= UrlString.URL_UPDATE;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }


    }

    private void initYoumeng() {
        PushAgent mPushAgent = PushAgent.getInstance(this);

        mPushAgent.setDebugMode(true);


//        mPushAgent.enable(new IUmengCallback() {
//            @Override
//            public void onSuccess() {
//                Log.i("Umeng", "onSuccess: ");
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//
//                Log.i("Umeng", "enable: "+s+s1);
//            }
//        });
        //注册推送服务 每次调用register都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                Log.i("Umeng", "device token: " + deviceToken);
//                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.i("Umeng", "register failed: " + s + " " + s1);
//                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }
        });

        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        // sdk关闭通知声音
//		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        // 通知声音由服务端控制
//		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);

        //闪光灯
//		mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        //震动
//		mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);

        //关闭免打扰模式
        mPushAgent.setNoDisturbMode(0, 0, 0, 0);

        //设置消息通知的时间
//        mPushAgent.setNoDisturbMode(int startHour, int startMinute, int endHour, int endMinute)

        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            @Override
            public void handleMessage(Context context, UMessage uMessage) {
                super.handleMessage(context, uMessage);

                Log.i("Umeng", "message" + uMessage.getRaw().toString());

                sendBroadcast(new Intent("new_message"));
                //处理消息
//                MessageManager.getInstance().handMessage(context,uMessage);
            }

            /**
             * 自定义消息的回调方法
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        Log.i("Umeng", "dealWithCustomMessage: ");

                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            /**
             * 自定义通知栏样式的回调方法
             * */
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        Notification.Builder builder = new Notification.Builder(context);
//                        //自定义通知栏样式
//                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
//                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
//                        builder.setContent(myNotificationView)
//                                .setSmallIcon(getSmallIconId(context, msg))
//                                .setTicker(msg.ticker)
//                                .setAutoCancel(true);

                        return builder.getNotification();
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 自定义行为的回调处理
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler1 = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                super.dealWithCustomAction(context, uMessage);
                //自定义行为的内容，存放在UMessage.custom中
                Log.i("Umeng", "dealWithCustomAction: " + uMessage.custom);
                Log.i("Umeng", "dealWithCustomAction: " + uMessage.toString());
                ToastUtils.show(context,"UmengNotificationClickHandler");
            }

            @Override
            public void openActivity(Context context, UMessage uMessage) {
                super.openActivity(context, uMessage);
//                ToastUtils.show(context,"openActivity");

            }

            @Override
            public void handleMessage(Context context, UMessage uMessage) {
                super.handleMessage(context, uMessage);
            }
        };

        //跳转到消息列表界面
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        //参考http://bbs.umeng.com/thread-11112-1-1.html
//        CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
//        mPushAgent.setNotificationClickHandler(notificationClickHandler1);

        //此处是完全自定义处理设置
//        mPushAgent.setPushIntentServiceClass(YouMengPushIntentService.class);
    }

    private static MyApp instance=null;

    public static MyApp getInstance(){
        if(instance==null){
            instance = new MyApp();
        }
        return instance;
    }

    public String getUserNick(){
        return "";
    }

    public String getUserTicket(){
        return LoginHelper.getInstance().getToken(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 应用程序的初始化的相关的操作
     */
    private void init() {

        //init demo helper
        DemoHelper.getInstance().init(getApplicationContext());
//        EaseUI.getInstance().init(this,null);
        //初始化红包
        initRedPackage();
//        表情管理对象的初始化
        EmotionManager.getInstance().init(this);

        DisplayMetrics dm = SystemHelper.getScreenInfo(this);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        Constants.init(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());


        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageloader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                PushAgent.getInstance(MyApp.this).onAppStart();
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

                MobclickAgent.onResume(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

                MobclickAgent.onPause(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void initRedPackage() {
        //初始化红包SDK，开启日志输出开关
        RedPacket.getInstance().initRedPacket(this, RPConstant.AUTH_METHOD_EASEMOB, new RPInitRedPacketCallback() {
            @Override
            public void initTokenData(RPValueCallback<TokenData> callback) {
                TokenData tokenData = new TokenData();
                tokenData.imUserId = EMClient.getInstance().getCurrentUser();
                //此处使用环信id代替了appUserId 开发者可传入App的appUserId
                tokenData.appUserId = EMClient.getInstance().getCurrentUser();
                tokenData.imToken = EMClient.getInstance().getAccessToken();
                //同步或异步获取TokenData 获取成功后回调onSuccess()方法
                callback.onSuccess(tokenData);
            }
            @Override
            public RedPacketInfo initCurrentUserSync() {
                //这里需要同步设置当前用户id、昵称和头像url
                String fromAvatarUrl = "";
                String fromNickname = EMClient.getInstance().getCurrentUser();
                EaseUser easeUser = EaseUserUtils.getUserInfo(fromNickname);
                if (easeUser != null) {
                    fromAvatarUrl = TextUtils.isEmpty(easeUser.getAvatar()) ? "none" : easeUser.getAvatar();
                    fromNickname = TextUtils.isEmpty(easeUser.getNick()) ? easeUser.getUsername() : easeUser.getNick();
                }
                RedPacketInfo redPacketInfo = new RedPacketInfo();
                redPacketInfo.fromUserId = EMClient.getInstance().getCurrentUser();
                redPacketInfo.fromAvatarUrl = fromAvatarUrl;
                redPacketInfo.fromNickName = fromNickname;
                return redPacketInfo;
            }
        });
        //打开Log开关 正式发布时请关闭
        RedPacket.getInstance().setDebugMode(true);

    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }



    public  void OpenSpeaker() {
        try {
            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.ROUTE_SPEAKER);
            int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);

            if (!audioManager.isSpeakerphoneOn()) {
                audioManager.setSpeakerphoneOn(true);

                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                        AudioManager.STREAM_VOICE_CALL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
