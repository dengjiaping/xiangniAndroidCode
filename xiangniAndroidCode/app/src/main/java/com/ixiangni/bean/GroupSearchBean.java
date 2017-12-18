package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/8.
 */

public class GroupSearchBean {


    /**
     * message : 操作成功!
     * data : [{"groupchatid":19,"userid":3,"groupchatname":"we","groupchatpic":"http://handongkeji.com:8090/ixiangniupload/groupHeadPic/2017-07-08/BE1499511341803.jpg","groupchatstatus":1,"groupchattype":null,"groupchatdesc":"ff","admit":null,"disturb":null,"groupchatno":"21089543585794"}]
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
         * groupchatid : 19
         * userid : 3
         * groupchatname : we
         * groupchatpic : http://handongkeji.com:8090/ixiangniupload/groupHeadPic/2017-07-08/BE1499511341803.jpg
         * groupchatstatus : 1
         * groupchattype : null
         * groupchatdesc : ff
         * admit : null
         * disturb : null
         * groupchatno : 21089543585794
         */

        private int groupchatid;
        private int userid;
        private String groupchatname;
        private String groupchatpic;
        private int groupchatstatus;
        private Object groupchattype;
        private String groupchatdesc;
        private Object admit;
        private Object disturb;
        private String groupchatno;

        public int getGroupchatid() {
            return groupchatid;
        }

        public void setGroupchatid(int groupchatid) {
            this.groupchatid = groupchatid;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getGroupchatname() {
            return groupchatname;
        }

        public void setGroupchatname(String groupchatname) {
            this.groupchatname = groupchatname;
        }

        public String getGroupchatpic() {
            return groupchatpic;
        }

        public void setGroupchatpic(String groupchatpic) {
            this.groupchatpic = groupchatpic;
        }

        public int getGroupchatstatus() {
            return groupchatstatus;
        }

        public void setGroupchatstatus(int groupchatstatus) {
            this.groupchatstatus = groupchatstatus;
        }

        public Object getGroupchattype() {
            return groupchattype;
        }

        public void setGroupchattype(Object groupchattype) {
            this.groupchattype = groupchattype;
        }

        public String getGroupchatdesc() {
            return groupchatdesc;
        }

        public void setGroupchatdesc(String groupchatdesc) {
            this.groupchatdesc = groupchatdesc;
        }

        public Object getAdmit() {
            return admit;
        }

        public void setAdmit(Object admit) {
            this.admit = admit;
        }

        public Object getDisturb() {
            return disturb;
        }

        public void setDisturb(Object disturb) {
            this.disturb = disturb;
        }

        public String getGroupchatno() {
            return groupchatno;
        }

        public void setGroupchatno(String groupchatno) {
            this.groupchatno = groupchatno;
        }
    }
}
