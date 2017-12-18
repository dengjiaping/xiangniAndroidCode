package com.ixiangni.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;

/**
 * Created by Administrator on 2017/8/2 0002.
 */

public class XNSettingProvider implements EaseUI.EaseSettingsProvider {


    private static XNSettingProvider instance;
    private final SharedPreferences sharedPreferences;

    public static final String isMsgNotifyAllowed = "isMsgNotifyAllowed";

    public static final String isMsgSoundAllowed = "isMsgSoundAllowed";

    public static final String isMsgVibrateAllowed = "isMsgVibrateAllowed";

    public static final String isSpeakerOpened = "isSpeakerOpened";


    public static XNSettingProvider getInstance(){
        return instance;
    }
    private XNSettingProvider(Context context) {


        sharedPreferences = context.getSharedPreferences("SettingProvider", Context.MODE_PRIVATE);
    }

    public static void init(Context context){
        if(instance==null){
        instance = new XNSettingProvider(context);
        }
    }

    @Override
    public boolean isMsgNotifyAllowed(EMMessage message) {
        return sharedPreferences.getBoolean(isMsgNotifyAllowed,true);
    }

    public void setMsgNotifyAllowed(boolean flag){
        sharedPreferences.edit().putBoolean(isMsgNotifyAllowed,flag).apply();
    }


    //是否有声音提醒
    @Override
    public boolean isMsgSoundAllowed(EMMessage message) {
        return sharedPreferences.getBoolean(isMsgSoundAllowed,true);
    }

    //设置声音
    public void setMsgSoundAllowed(boolean flag){
        sharedPreferences.edit().putBoolean(isMsgSoundAllowed,flag).apply();
    }

    //是否允许震动
    @Override
    public boolean isMsgVibrateAllowed(EMMessage message) {
        return sharedPreferences.getBoolean(isMsgVibrateAllowed,true);
    }

    //设置震动
    public void setMsgVibrateAllowed(boolean flag){
        sharedPreferences.edit().putBoolean(isMsgVibrateAllowed,flag).apply();
    }

    @Override
    public boolean isSpeakerOpened() {
        return sharedPreferences.getBoolean(isSpeakerOpened,true);
    }
}
