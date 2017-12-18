package com.ixiangni.bean;

/**
 * Created by Administrator on 2016/11/25.
 */
public class RedPacketBean {

    /**
     * usernick : null
     * userpic : null
     * cityname : null
     * provincename : null
     * usersex : null
     * luckmoneyid : 376
     * message :
     */

    private String usernick;
    private String userpic;
    private String cityname;
    private String provincename;
    private String usersex;
    private int luckmoneyid;
    private String message;

    public String getUsernick() {
        return usernick;
    }

    public void setUsernick(String usernick) {
        this.usernick = usernick;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename;
    }

    public String getUsersex() {
        return usersex;
    }

    public void setUsersex(String usersex) {
        this.usersex = usersex;
    }

    public int getLuckmoneyid() {
        return luckmoneyid;
    }

    public void setLuckmoneyid(int luckmoneyid) {
        this.luckmoneyid = luckmoneyid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
