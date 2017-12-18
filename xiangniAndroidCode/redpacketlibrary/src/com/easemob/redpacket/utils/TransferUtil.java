package com.easemob.redpacket.utils;

import android.text.TextUtils;

import com.hyphenate.chat.EMMessage;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class TransferUtil {
    public static boolean isTransferMsg(EMMessage message){

        if(!TextUtils.isEmpty(message.getStringAttribute(TransferConstance.Transfer,null))){
            return true;
        }
        return false;
    }
}
