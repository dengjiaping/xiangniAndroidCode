package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class TextBean {


    /**
     * message : 操作成功!
     * data : {"textid":9,"title":"联系我们","textflag":3,"text":"110"}
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
         * textid : 9
         * title : 联系我们
         * textflag : 3
         * text : 110
         */

        private int textid;
        private String title;
        private int textflag;
        private String text;

        public int getTextid() {
            return textid;
        }

        public void setTextid(int textid) {
            this.textid = textid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTextflag() {
            return textflag;
        }

        public void setTextflag(int textflag) {
            this.textflag = textflag;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
