package com.ixiangni.bean;

import com.ixiangni.util.PinyinUtil;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public  class GroupBean {
    /**
     * groupchatid : 6
     * userid : 10
     * groupchatname : 考虑考虑
     * groupchatpic :
     * groupchatstatus : 1
     * groupchattype : null
     * groupchatdesc : 北京
     * admit : null
     * disturb : null
     * groupchatno : 20803738468355
     */

    private int groupchatid;
    private int userid;
    private String groupchatname;
    private String groupchatpic;
    private int groupchatstatus;
    private Object groupchattype;
    private String groupchatdesc;
    private Object admit;
    private Object disturb;
    private String groupchatno;

    public int getGroupchatid() {
        return groupchatid;
    }

    public String getFirstLetter(){

        String s = PinyinUtil.getFrist(getGroupchatname()).toUpperCase();
        return s;
    }

    public void setGroupchatid(int groupchatid) {
        this.groupchatid = groupchatid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getGroupchatname() {
        return groupchatname;
    }

    public void setGroupchatname(String groupchatname) {
        this.groupchatname = groupchatname;
    }

    public String getGroupchatpic() {
        return groupchatpic;
    }

    public void setGroupchatpic(String groupchatpic) {
        this.groupchatpic = groupchatpic;
    }

    public int getGroupchatstatus() {
        return groupchatstatus;
    }

    public void setGroupchatstatus(int groupchatstatus) {
        this.groupchatstatus = groupchatstatus;
    }

    public Object getGroupchattype() {
        return groupchattype;
    }

    public void setGroupchattype(Object groupchattype) {
        this.groupchattype = groupchattype;
    }

    public String getGroupchatdesc() {
        return groupchatdesc;
    }

    public void setGroupchatdesc(String groupchatdesc) {
        this.groupchatdesc = groupchatdesc;
    }

    public Object getAdmit() {
        return admit;
    }

    public void setAdmit(Object admit) {
        this.admit = admit;
    }

    public Object getDisturb() {
        return disturb;
    }

    public void setDisturb(Object disturb) {
        this.disturb = disturb;
    }

    public String getGroupchatno() {
        return groupchatno;
    }

    public void setGroupchatno(String groupchatno) {
        this.groupchatno = groupchatno;
    }
}
