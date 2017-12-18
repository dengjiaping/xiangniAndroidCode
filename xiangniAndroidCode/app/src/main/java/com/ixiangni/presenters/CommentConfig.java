package com.ixiangni.presenters;

import com.ixiangni.bean.CircleListBean;

/**
 * Created by Administrator on 2016/11/12.
 */
public class CommentConfig {
    public static enum Type{
        PUBLIC("public"), REPLY("reply");

        private String value;
        private Type(String value){
            this.value = value;
        }

    }

    public int circlePosition;
    public int commentPosition;
    public String momentnewsid;
    public String pluserid;
    public String userid;
    public String usernick;
    public String plusernick;
    public Type commentType;
    public CircleListBean.DataBean.UserinfoBean replyUser;//自己

    @Override
    public String toString() {
        String replyUserStr = "";
        if(replyUser != null){
            replyUserStr = replyUser.toString();
        }
        return "circlePosition = " + circlePosition
                + "; commentPosition = " + commentPosition
                + "; commentType ＝ " + commentType
                + "; plusernick = " + plusernick
                + "; replyUser = " + replyUserStr;
    }
}
