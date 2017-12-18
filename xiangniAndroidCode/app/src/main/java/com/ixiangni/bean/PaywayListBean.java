package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/8.
 */

public class PaywayListBean {

    /**
     * message : 操作成功!
     * data : [{"paymentid":6,"userid":3,"username":"玄幻听","accountcode":"42555225555555555555","accounttype":103,"isdefault":1,"spare1":"edffgy","spare2":null,"spare3":null,"spare4":null,"spare5":null},{"paymentid":7,"userid":3,"username":"哭咯","accountcode":"35582824242417173538","accounttype":103,"isdefault":0,"spare1":"出来聚聚","spare2":null,"spare3":null,"spare4":null,"spare5":null}]
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
         * paymentid : 6
         * userid : 3
         * username : 玄幻听
         * accountcode : 42555225555555555555
         * accounttype : 103
         * isdefault : 1
         * spare1 : edffgy
         * spare2 : null
         * spare3 : null
         * spare4 : null
         * spare5 : null
         */

        private int paymentid;
        private int userid;
        private String username;
        private String accountcode;
        private int accounttype;
        private int isdefault;
        private String spare1;
        private Object spare2;
        private Object spare3;
        private Object spare4;
        private Object spare5;

        public int getPaymentid() {
            return paymentid;
        }

        public void setPaymentid(int paymentid) {
            this.paymentid = paymentid;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAccountcode() {
            return accountcode;
        }

        public void setAccountcode(String accountcode) {
            this.accountcode = accountcode;
        }

        public int getAccounttype() {
            return accounttype;
        }

        public void setAccounttype(int accounttype) {
            this.accounttype = accounttype;
        }

        public int getIsdefault() {
            return isdefault;
        }

        public void setIsdefault(int isdefault) {
            this.isdefault = isdefault;
        }

        public String getSpare1() {
            return spare1;
        }

        public void setSpare1(String spare1) {
            this.spare1 = spare1;
        }

        public Object getSpare2() {
            return spare2;
        }

        public void setSpare2(Object spare2) {
            this.spare2 = spare2;
        }

        public Object getSpare3() {
            return spare3;
        }

        public void setSpare3(Object spare3) {
            this.spare3 = spare3;
        }

        public Object getSpare4() {
            return spare4;
        }

        public void setSpare4(Object spare4) {
            this.spare4 = spare4;
        }

        public Object getSpare5() {
            return spare5;
        }

        public void setSpare5(Object spare5) {
            this.spare5 = spare5;
        }
    }
}
