package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class CollectedListBean {

    /**
     * message : 操作成功!
     * data : [{"browuserid":62,"browbaguserid":null,"userid":10,"browid":null,"browname":null,"browinfo":"http://handongkeji.com:8090/ixiangniupload/record/2017-07-12/XR1499825956788_mid.jpg","browtype":2},{"browuserid":63,"browbaguserid":null,"userid":10,"browid":null,"browname":null,"browinfo":"http://handongkeji.com:8090/ixiangniupload/record/2017-07-12/NL1499825959889_mid.jpg","browtype":2},{"browuserid":64,"browbaguserid":null,"userid":10,"browid":null,"browname":null,"browinfo":"http://handongkeji.com:8090/ixiangniupload/record/2017-07-12/NC1499827146727_mid.jpg","browtype":2},{"browuserid":65,"browbaguserid":null,"userid":10,"browid":null,"browname":null,"browinfo":"http://handongkeji.com:8090/ixiangniupload/record/2017-07-12/YA1499827246657_mid.jpg","browtype":2}]
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
         * browuserid : 62
         * browbaguserid : null
         * userid : 10
         * browid : null
         * browname : null
         * browinfo : http://handongkeji.com:8090/ixiangniupload/record/2017-07-12/XR1499825956788_mid.jpg
         * browtype : 2
         */

        private int browuserid;
        private Object browbaguserid;
        private int userid;
        private Object browid;
        private Object browname;
        private String browinfo;
        private int browtype;

        public int getBrowuserid() {
            return browuserid;
        }

        public void setBrowuserid(int browuserid) {
            this.browuserid = browuserid;
        }

        public Object getBrowbaguserid() {
            return browbaguserid;
        }

        public void setBrowbaguserid(Object browbaguserid) {
            this.browbaguserid = browbaguserid;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public Object getBrowid() {
            return browid;
        }

        public void setBrowid(Object browid) {
            this.browid = browid;
        }

        public Object getBrowname() {
            return browname;
        }

        public void setBrowname(Object browname) {
            this.browname = browname;
        }

        public String getBrowinfo() {
            return browinfo;
        }

        public void setBrowinfo(String browinfo) {
            this.browinfo = browinfo;
        }

        public int getBrowtype() {
            return browtype;
        }

        public void setBrowtype(int browtype) {
            this.browtype = browtype;
        }
    }
}
