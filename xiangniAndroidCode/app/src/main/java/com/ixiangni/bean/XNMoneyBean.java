package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class XNMoneyBean {

    /**
     * message : 操作成功!
     * data : {"usernick":null,"userpic":null,"cityname":null,"provincename":null,"usersex":null,"luckmoneyid":73,"message":"想你转账"}
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
         * usernick : null
         * userpic : null
         * cityname : null
         * provincename : null
         * usersex : null
         * luckmoneyid : 73
         * message : 想你转账
         */

        private Object usernick;
        private Object userpic;
        private Object cityname;
        private Object provincename;
        private Object usersex;
        private int luckmoneyid;
        private String message;

        public Object getUsernick() {
            return usernick;
        }

        public void setUsernick(Object usernick) {
            this.usernick = usernick;
        }

        public Object getUserpic() {
            return userpic;
        }

        public void setUserpic(Object userpic) {
            this.userpic = userpic;
        }

        public Object getCityname() {
            return cityname;
        }

        public void setCityname(Object cityname) {
            this.cityname = cityname;
        }

        public Object getProvincename() {
            return provincename;
        }

        public void setProvincename(Object provincename) {
            this.provincename = provincename;
        }

        public Object getUsersex() {
            return usersex;
        }

        public void setUsersex(Object usersex) {
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
}
