package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6 0006.
 */

public class GroupMLBean {

    /**
     * message : 操作成功!
     * data : [{"groupchatid":9,"userid":10,"memberrole":1,"usernick":"徐传霆","userpic":"http://handongkeji.com:8090/ixiangniupload/head/2017-06-29/QT1498726653905_mid.jpg"}]
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
         * groupchatid : 9
         * userid : 10
         * memberrole : 1
         * usernick : 徐传霆
         * userpic : http://handongkeji.com:8090/ixiangniupload/head/2017-06-29/QT1498726653905_mid.jpg
         */

        private int groupchatid;
        private int userid;
        private int memberrole;
        private String usernick;
        private String userpic;
        private int admit;

        public int getAdmit() {
            return admit;
        }

        public void setAdmit(int admit) {
            this.admit = admit;
        }

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

        public int getMemberrole() {
            return memberrole;
        }

        public void setMemberrole(int memberrole) {
            this.memberrole = memberrole;
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
    }
}
