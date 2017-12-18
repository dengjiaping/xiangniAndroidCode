package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public  class EmtionBean {
    /**
     * browid : 1
     * browname : 表情1
     * browinfo : http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/GO1499405061606_mid.jpg
     * browtype : 0
     * createtime : 1498075719000
     * isdel : 0
     */

    private int browid;
    private String browname;
    private String browinfo;
    private int browtype;
    private long createtime;
    private int isdel;

    public int getBrowid() {
        return browid;
    }

    public void setBrowid(int browid) {
        this.browid = browid;
    }

    public String getBrowname() {
        return browname;
    }

    public void setBrowname(String browname) {
        this.browname = browname;
    }

    public String getBrowinfo() {
        return browinfo;
    }

    public void setBrowinfo(String browinfo) {
        this.browinfo = browinfo;
    }

    public int getBrowtype() {
        return browtype;
    }

    public void setBrowtype(int browtype) {
        this.browtype = browtype;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getIsdel() {
        return isdel;
    }

    public void setIsdel(int isdel) {
        this.isdel = isdel;
    }
}
