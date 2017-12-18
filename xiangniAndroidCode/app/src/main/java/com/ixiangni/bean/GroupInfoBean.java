package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/8/12 0012.
 */

public class GroupInfoBean {

    /**
     * message : 操作成功!
     * data : {"groupchatid":100,"userid":10,"groupchatname":"BUG群","groupchatpic":"http://180.76.245.58/ixiangniupload/groupHeadPic/2017-08-12/ND1502530919632.jpg","groupchatstatus":1,"groupchattype":null,"groupchatdesc":"改改","admit":0,"disturb":null,"groupchatno":"24228129734657"}
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
         * groupchatid : 100
         * userid : 10
         * groupchatname : BUG群
         * groupchatpic : http://180.76.245.58/ixiangniupload/groupHeadPic/2017-08-12/ND1502530919632.jpg
         * groupchatstatus : 1
         * groupchattype : null
         * groupchatdesc : 改改
         * admit : 0
         * disturb : null
         * groupchatno : 24228129734657
         */

        private int groupchatid;
        private int userid;
        private String groupchatname;
        private String groupchatpic;
        private int groupchatstatus;
        private Object groupchattype;
        private String groupchatdesc;
        private int admit;
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

        public int getAdmit() {
            return admit;
        }

        public void setAdmit(int admit) {
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
