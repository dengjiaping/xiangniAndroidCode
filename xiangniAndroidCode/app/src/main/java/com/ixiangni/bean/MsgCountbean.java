package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class MsgCountbean {

    /**
     * message : 操作成功!
     * data : {"quanbu":55,"yidu":0,"weidu":55}
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
         * quanbu : 55
         * yidu : 0
         * weidu : 55
         */

        private int quanbu;
        private int yidu;
        private int weidu;

        public int getQuanbu() {
            return quanbu;
        }

        public void setQuanbu(int quanbu) {
            this.quanbu = quanbu;
        }

        public int getYidu() {
            return yidu;
        }

        public void setYidu(int yidu) {
            this.yidu = yidu;
        }

        public int getWeidu() {
            return weidu;
        }

        public void setWeidu(int weidu) {
            this.weidu = weidu;
        }
    }
}
