package com.mydemo.yuanxin.util;

/**
 * Created by zcq on 2017/7/21.
 */

public class CleanJsonUtil {
    /**
     * <br>对服务器发来的json格式的数据进行格式修正
     * <br>清除掉看不见的隐藏数据
     * @param json
     * @return
     */
    public static String cleanErrorCode(String json){
        if(!json.startsWith("{")){
            json = json.substring( json.indexOf("{"),json.length());
        }
        if(!json.endsWith("}")){
            json = json.substring(0,json.lastIndexOf("}")+1);
        }
        return json;
    }
}
