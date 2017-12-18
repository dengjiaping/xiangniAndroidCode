package com.handongkeji.bean;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class LoginBean {

    /**
     * message : 操作成功
     * data : {"uid":1,"token":"c2Ur-Fj7MdZNt69cUdcYjhi8V5Oa6Df6MbDxMWjUkZD5H0xE99Rd3c"}
     * status : 1
     */

    private String message;
    private DataBean data;
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {

        /**
         * uid : 2
         * token : ZmZRMw-vSRPLAp5kMCRdAS4JI8ap7FZZgyVdG9owm4A6glq8b5SyKGA325PPf3VRtGaA7BpsYw_oyvqti3jqRQ
         * userNick : 任勇
         */

        private String uid;
        private String token;
        private String userNick;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUserNick() {
            return userNick;
        }

        public void setUserNick(String userNick) {
            this.userNick = userNick;
        }
    }
}
