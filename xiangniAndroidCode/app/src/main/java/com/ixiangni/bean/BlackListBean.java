package com.ixiangni.bean;

import com.ixiangni.util.PinyinUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class BlackListBean {

    /**
     * message : 操作成功!
     * data : [{"blacklistid":3,"userid":10,"friendid":3,"usernick":"孙悟空","userpic":"http://handongkeji.com:8090/ixiangniupload/head/2017-07-03/ZX1499080374935_mid.jpg"}]
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
         * blacklistid : 3
         * userid : 10
         * friendid : 3
         * usernick : 孙悟空
         * userpic : http://handongkeji.com:8090/ixiangniupload/head/2017-07-03/ZX1499080374935_mid.jpg
         */

        private int blacklistid;
        private int userid;
        private int friendid;
        private String usernick;
        private String userpic;

        public int getBlacklistid() {
            return blacklistid;
        }

        public void setBlacklistid(int blacklistid) {
            this.blacklistid = blacklistid;
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

        public String getFirstLetter(){
            return PinyinUtil.getFrist(getUsernick()).toUpperCase();
        }
    }
}
