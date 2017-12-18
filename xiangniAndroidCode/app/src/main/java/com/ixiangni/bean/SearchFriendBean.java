package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/21 0021.
 */

public class SearchFriendBean {

    /**
     * message : 操作成功!
     * data : [{"friendshipid":19,"userid":5,"friendid":10,"friendshipstatus":1,"showtype":0,"createtime":1499667288000,"chattime":1499667288000,"remind":null,"isinviter":0,"seehemoment":0,"seememoment":0,"frienddel":0,"usernick":"阿尔萨斯","userpic":"http://handongkeji.com:8090/ixiangniupload/head/2017-06-28/QF1498630479294_mid.jpg","applyadd":0,"authentication":1,"isblack":null,"addmsg":null,"msgfrom":null}]
     * status : 1
     */

    private String message;
    private int status;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * friendshipid : 19
         * userid : 5
         * friendid : 10
         * friendshipstatus : 1
         * showtype : 0
         * createtime : 1499667288000
         * chattime : 1499667288000
         * remind : null
         * isinviter : 0
         * seehemoment : 0
         * seememoment : 0
         * frienddel : 0
         * usernick : 阿尔萨斯
         * userpic : http://handongkeji.com:8090/ixiangniupload/head/2017-06-28/QF1498630479294_mid.jpg
         * applyadd : 0
         * authentication : 1
         * isblack : null
         * addmsg : null
         * msgfrom : null
         */

        private int friendshipid;
        private int userid;
        private int friendid;
        private int friendshipstatus;
        private int showtype;
        private long createtime;
        private long chattime;
        private Object remind;
        private int isinviter;
        private int seehemoment;
        private int seememoment;
        private int frienddel;
        private String usernick;
        private String userpic;
        private int applyadd;
        private int authentication;
        private Object isblack;
        private Object addmsg;
        private Object msgfrom;

        public int getFriendshipid() {
            return friendshipid;
        }

        public void setFriendshipid(int friendshipid) {
            this.friendshipid = friendshipid;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public int getFriendid() {
            return friendid;
        }

        public void setFriendid(int friendid) {
            this.friendid = friendid;
        }

        public int getFriendshipstatus() {
            return friendshipstatus;
        }

        public void setFriendshipstatus(int friendshipstatus) {
            this.friendshipstatus = friendshipstatus;
        }

        public int getShowtype() {
            return showtype;
        }

        public void setShowtype(int showtype) {
            this.showtype = showtype;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public long getChattime() {
            return chattime;
        }

        public void setChattime(long chattime) {
            this.chattime = chattime;
        }

        public Object getRemind() {
            return remind;
        }

        public void setRemind(Object remind) {
            this.remind = remind;
        }

        public int getIsinviter() {
            return isinviter;
        }

        public void setIsinviter(int isinviter) {
            this.isinviter = isinviter;
        }

        public int getSeehemoment() {
            return seehemoment;
        }

        public void setSeehemoment(int seehemoment) {
            this.seehemoment = seehemoment;
        }

        public int getSeememoment() {
            return seememoment;
        }

        public void setSeememoment(int seememoment) {
            this.seememoment = seememoment;
        }

        public int getFrienddel() {
            return frienddel;
        }

        public void setFrienddel(int frienddel) {
            this.frienddel = frienddel;
        }

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

        public int getApplyadd() {
            return applyadd;
        }

        public void setApplyadd(int applyadd) {
            this.applyadd = applyadd;
        }

        public int getAuthentication() {
            return authentication;
        }

        public void setAuthentication(int authentication) {
            this.authentication = authentication;
        }

        public Object getIsblack() {
            return isblack;
        }

        public void setIsblack(Object isblack) {
            this.isblack = isblack;
        }

        public Object getAddmsg() {
            return addmsg;
        }

        public void setAddmsg(Object addmsg) {
            this.addmsg = addmsg;
        }

        public Object getMsgfrom() {
            return msgfrom;
        }

        public void setMsgfrom(Object msgfrom) {
            this.msgfrom = msgfrom;
        }
    }
}
