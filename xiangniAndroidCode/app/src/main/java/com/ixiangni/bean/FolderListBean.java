package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class FolderListBean {

    /**
     * message : 操作成功!
     * data : [{"folderid":2,"userid":2,"foldername":"阿卡丽","folderpassword":"123456","isshow":0,"createtime":1498556259000,"delmark":0},{"folderid":1,"userid":2,"foldername":"啦啦啦","folderpassword":"123456","isshow":0,"createtime":1498556166000,"delmark":0}]
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
         * folderid : 2
         * userid : 2
         * foldername : 阿卡丽
         * folderpassword : 123456
         * isshow : 0
         * createtime : 1498556259000
         * delmark : 0
         */

        private int folderid;
        private int userid;
        private String foldername;
        private String folderpassword;
        private int isshow;
        private long createtime;
        private int delmark;

        public int getFolderid() {
            return folderid;
        }

        public void setFolderid(int folderid) {
            this.folderid = folderid;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getFoldername() {
            return foldername;
        }

        public void setFoldername(String foldername) {
            this.foldername = foldername;
        }

        public String getFolderpassword() {
            return folderpassword;
        }

        public void setFolderpassword(String folderpassword) {
            this.folderpassword = folderpassword;
        }

        public int getIsshow() {
            return isshow;
        }

        public void setIsshow(int isshow) {
            this.isshow = isshow;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public int getDelmark() {
            return delmark;
        }

        public void setDelmark(int delmark) {
            this.delmark = delmark;
        }
    }
}
