package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class LikeBean {

    /**
     * momentnewsid : 245
     * userid : 10
     * usernick : 徐传霆
     * createtime : 1499932255000
     */

    private int momentnewsid;
    private int userid;
    private String usernick;
    private long createtime;

    public int getMomentnewsid() {
        return momentnewsid;
    }

    public void setMomentnewsid(int momentnewsid) {
        this.momentnewsid = momentnewsid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsernick() {
        return usernick;
    }

    public void setUsernick(String usernick) {
        this.usernick = usernick;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }
}
