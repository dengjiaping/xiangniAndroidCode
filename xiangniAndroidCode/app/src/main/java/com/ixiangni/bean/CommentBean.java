package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/7/4 0004.
 */

public class CommentBean {

    /**
     * commentid : 119
     * newsuserid : 10
     * momentnewsid : 102
     * userid : 6
     * usernick : 伊利丹
     * commentcontent : I'm
     * commenttime : 1499068063000
     * additionalmark : 0
     * mainuserid : 6
     * mainnick : 伊利丹
     * delmark : 0
     */


    private String commentid;//评论编号
    private String newsuserid;//极有圈发布者的编号
    private String momentnewsid;//极有圈编号
    private String userid;//评论者的用户id
    private String usernick;//评论者的昵称
    private String commentcontent;//评论内容
    private String commenttime;//评论时间
    private String additionalmark;//追评标示
    private String mainuserid;//主评论编号
    private String mainnick;//主评论名称
    private String delmark;//删除不用

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getNewsuserid() {
        return newsuserid;
    }

    public void setNewsuserid(String newsuserid) {
        this.newsuserid = newsuserid;
    }

    public String getMomentnewsid() {
        return momentnewsid;
    }

    public void setMomentnewsid(String momentnewsid) {
        this.momentnewsid = momentnewsid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsernick() {
        return usernick;
    }

    public void setUsernick(String usernick) {
        this.usernick = usernick;
    }

    public String getCommentcontent() {
        return commentcontent;
    }

    public void setCommentcontent(String commentcontent) {
        this.commentcontent = commentcontent;
    }

    public String getCommenttime() {
        return commenttime;
    }

    public void setCommenttime(String commenttime) {
        this.commenttime = commenttime;
    }

    public String getAdditionalmark() {
        return additionalmark;
    }

    public void setAdditionalmark(String additionalmark) {
        this.additionalmark = additionalmark;
    }

    public String getMainuserid() {
        return mainuserid;
    }

    public void setMainuserid(String mainuserid) {
        this.mainuserid = mainuserid;
    }

    public String getMainnick() {
        return mainnick;
    }

    public void setMainnick(String mainnick) {
        this.mainnick = mainnick;
    }

    public String getDelmark() {
        return delmark;
    }

    public void setDelmark(String delmark) {
        this.delmark = delmark;
    }
}
