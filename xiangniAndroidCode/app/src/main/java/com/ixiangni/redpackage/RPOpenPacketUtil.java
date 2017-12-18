package com.ixiangni.redpackage;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

import com.easemob.redpacketsdk.bean.RedPacketInfo;
import com.easemob.redpacketsdk.bean.TokenData;
import com.google.gson.reflect.TypeToken;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chat.EMClient;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.money.RPRedPacketDetailActivity;
import com.ixiangni.bean.BaseBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.util.GsonUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/25.
 */
public class RPOpenPacketUtil {

    private static RPOpenPacketUtil instance;

    public static RPOpenPacketUtil getInstance() {
        if (null == instance) {
            synchronized (RPOpenPacketUtil.class) {
                if (null == instance) {
                    instance = new RPOpenPacketUtil();
                }
            }
        }

        return instance;
    }

    private RPOpenPacketUtil() {
    }

    public void openRedPacket(RedPacketInfo info, TokenData tokenData, FragmentActivity activity, RPOpenPacketUtil.RPOpenPacketCallBack callBack) {
        if (callBack == null) {
            throw new IllegalArgumentException("callback is null!");
        } else {
            callBack.showLoading();
            checkLuckMoney(info, activity, callBack);
        }
    }

    /**
     * 查询红包是否可领取
     *
     * @param info
     * @param activity
     * @param callBack
     */
    private void checkLuckMoney(RedPacketInfo info, Activity activity, RPOpenPacketUtil.RPOpenPacketCallBack callBack) {
        String url = UrlString.URL_CHECK_LUCKMONEY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", ((MyApp) activity.getApplicationContext()).getUserTicket());
        params.put("luckmoneyid", info.redPacketId);
        RemoteDataHandler.asyncTokenPost(url, activity, false, params, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                String json = data.getJson();
                if (!CommonUtils.isStringNull(json)) {
                    Type type = new TypeToken<BaseBean<Integer>>() {
                    }.getType();
                    BaseBean<Integer> baseBean = (BaseBean) GsonUtils.fromJson(json, type);
                    if (baseBean.getStatus() == 1) {

                        if (info.chatType == com.hyphenate.easeui.EaseConstant.CHATTYPE_SINGLE && EMClient.getInstance().getCurrentUser().equals(info.fromUserId)) {
                            activity.startActivity(new Intent(activity, RPRedPacketDetailActivity.class).putExtra("luckmoneyid", info.redPacketId));
                            callBack.hideLoading();
                        } else {
                            callBack.hideLoading();
                            Integer baseBeanData = baseBean.getData();
                            if (baseBeanData == 0) {   //  未领取
                                callBack.onOpen();
                            } else if (baseBeanData == 1) {     //  已领取
                                activity.startActivity(new Intent(activity, RPRedPacketDetailActivity.class)
                                        .putExtra("luckmoneyid", info.redPacketId));
                            } else if (baseBeanData == 2) {      //  红包已过期
                                callBack.onExpir();
                            } else {                     //  红包已领完
                                callBack.onNoRedPacket();
                            }
                        }

                    }
                }
            }
        });
    }

    public void getRedPacketMoney(RedPacketInfo info, Activity activity, RPOpenPacketUtil.RPOpenPacketCallBack callBack) {
        String url = UrlString.URL_CREATE_LUCKMONEY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", ((MyApp) activity.getApplicationContext()).getUserTicket());
        params.put("luckmoneyid", info.redPacketId);
        RemoteDataHandler.asyncTokenPost(url, activity, false, params, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                String json = data.getJson();
                if (!CommonUtils.isStringNull(json)) {
                    try {
                        JSONObject obj = new JSONObject(json);
                        int status = obj.getInt("status");
                        if (status == 1) {
                            JSONObject object = obj.getJSONObject("data");
                            String moneynum = object.getString("moneynum");
                            callBack.onSuccess(info.fromUserId, info.fromNickName, moneynum);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callBack.hideLoading();
            }
        });
    }


    public void openRedPacket(RedPacketInfo var1, FragmentActivity var2, RPOpenPacketUtil.RPOpenPacketCallBack var3) {
        if (var3 == null) {
            throw new IllegalArgumentException("callback is null!");
        } else {
            var3.showLoading();
//            RedPacket.getInstance().initRPToken(new TokenData(), new h(this, var1, var2, var3));
        }
    }

//    @NonNull
//    private OpenPacketPresenter openPacket(RedPacketInfo var1, FragmentActivity var2, RPOpenPacketUtil.RPOpenPacketCallBack var3) {
//        return new OpenPacketPresenter(var2, var1, new i(this, var3, var2));
//    }

    private void playSound(FragmentActivity var1) {
        AssetFileDescriptor var2;
        if (this.isWAVSoundFileExist(var1) == null) {
            if (this.isMP3SoundFileExist(var1) == null) {
                return;
            }

            var2 = this.isMP3SoundFileExist(var1);
        } else {
            var2 = this.isWAVSoundFileExist(var1);
        }

        SoundPool var3;
        if (Build.VERSION.SDK_INT >= 21) {
            AudioAttributes var4 = (new AudioAttributes.Builder()).setLegacyStreamType(3).build();
            android.media.SoundPool.Builder var5 = new android.media.SoundPool.Builder();
            var5.setMaxStreams(10);
            var5.setAudioAttributes(var4);
            var3 = var5.build();
        } else {
            var3 = new SoundPool(10, 3, 5);
        }

//        var3.load(var2, 1);
//        var3.setOnLoadCompleteListener(new l(this));
    }

    private AssetFileDescriptor isMP3SoundFileExist(FragmentActivity var1) {
//        try {
//            return var1.getResources().getAssets().openFd("open_packet_sound.mp3");
//        } catch (IOException var3) {
//            return null;
//        }
        return null;
    }

    private AssetFileDescriptor isWAVSoundFileExist(FragmentActivity var1) {
        try {
            return var1.getResources().getAssets().openFd("open_packet_sound.wav");
        } catch (IOException var3) {
            return null;
        }
    }

    public interface RPOpenPacketCallBack {
        void onSuccess(String var1, String var2, String var3);

        void showLoading();

        void hideLoading();

        void onError(String var1, String var2);

        void onOpen();

        void onExpir();

        void onNoRedPacket();
    }
}
