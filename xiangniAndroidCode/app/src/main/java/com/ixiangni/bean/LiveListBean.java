package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class LiveListBean {

    /**
     * message : 操作成功!
     * data : [{"adid":55,"areaid":null,"adtitle":"维吾尔","adpostion":1,"userid":null,"adtype":null,"adcontents":"http://www.baidu.com","adpicture":"http://localhost:80/ixiangnimgrupload/common/2017-06-28/AS1498636222865_mid.jpg"},{"adid":54,"areaid":null,"adtitle":"王思聪直播间","adpostion":1,"userid":null,"adtype":null,"adcontents":"http://www.baidu.com","adpicture":"http://localhost:80/ixiangnimgrupload/common/2017-06-28/GS1498635646647_mid.jpg"},{"adid":53,"areaid":null,"adtitle":"儿童热帖","adpostion":1,"userid":1,"adtype":2,"adcontents":"http://baidu.com","adpicture":"http://localhost:80/ixiangnimgrupload/common/2017-06-28/FZ1498637082175_mid.jpg"},{"adid":52,"areaid":null,"adtitle":"儿童","adpostion":1,"userid":21,"adtype":2,"adcontents":"http://baidu.com","adpicture":"http://localhost:80/ixiangnimgrupload/common/2017-06-28/JO1498637176318_mid.jpg"}]
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
         * adid : 55
         * areaid : null
         * adtitle : 维吾尔
         * adpostion : 1
         * userid : null
         * adtype : null
         * adcontents : http://www.baidu.com
         * adpicture : http://localhost:80/ixiangnimgrupload/common/2017-06-28/AS1498636222865_mid.jpg
         */

        private int adid;
        private Object areaid;
        private String adtitle;
        private int adpostion;
        private Object userid;
        private Object adtype;
        private String adcontents;
        private String adpicture;

        public int getAdid() {
            return adid;
        }

        public void setAdid(int adid) {
            this.adid = adid;
        }

        public Object getAreaid() {
            return areaid;
        }

        public void setAreaid(Object areaid) {
            this.areaid = areaid;
        }

        public String getAdtitle() {
            return adtitle;
        }

        public void setAdtitle(String adtitle) {
            this.adtitle = adtitle;
        }

        public int getAdpostion() {
            return adpostion;
        }

        public void setAdpostion(int adpostion) {
            this.adpostion = adpostion;
        }

        public Object getUserid() {
            return userid;
        }

        public void setUserid(Object userid) {
            this.userid = userid;
        }

        public Object getAdtype() {
            return adtype;
        }

        public void setAdtype(Object adtype) {
            this.adtype = adtype;
        }

        public String getAdcontents() {
            return adcontents;
        }

        public void setAdcontents(String adcontents) {
            this.adcontents = adcontents;
        }

        public String getAdpicture() {
            return adpicture;
        }

        public void setAdpicture(String adpicture) {
            this.adpicture = adpicture;
        }
    }
}
