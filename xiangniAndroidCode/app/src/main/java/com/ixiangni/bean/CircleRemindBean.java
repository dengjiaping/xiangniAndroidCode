package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/8/12 0012.
 */

public class CircleRemindBean {

    /**
     * message : 无新动态
     * data : {"momentnewnum":0,"commentnum":0}
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
         * momentnewnum : 0
         * commentnum : 0
         */

        private int momentnewnum;
        private int commentnum;

        public int getMomentnewnum() {
            return momentnewnum;
        }

        public void setMomentnewnum(int momentnewnum) {
            this.momentnewnum = momentnewnum;
        }

        public int getCommentnum() {
            return commentnum;
        }

        public void setCommentnum(int commentnum) {
            this.commentnum = commentnum;
        }
    }
}
