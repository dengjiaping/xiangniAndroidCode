package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class ChargeBean {

    /**
     * message : 操作成功!
     * data : [{"refillid":1,"userid":10,"refillnum":100,"createtime":1498613089000,"isdel":0},{"refillid":2,"userid":10,"refillnum":300,"createtime":1498613995000,"isdel":0},{"refillid":3,"userid":10,"refillnum":220,"createtime":1498614054000,"isdel":0},{"refillid":4,"userid":10,"refillnum":100,"createtime":1498614194000,"isdel":0},{"refillid":5,"userid":10,"refillnum":500,"createtime":1498614270000,"isdel":0},{"refillid":6,"userid":10,"refillnum":1,"createtime":1498614275000,"isdel":0}]
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
         * refillid : 1
         * userid : 10
         * refillnum : 100.0
         * createtime : 1498613089000
         * isdel : 0
         */

        private int refillid;
        private int userid;
        private double refillnum;
        private long createtime;
        private int isdel;

        public int getRefillid() {
            return refillid;
        }

        public void setRefillid(int refillid) {
            this.refillid = refillid;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public double getRefillnum() {
            return refillnum;
        }

        public void setRefillnum(double refillnum) {
            this.refillnum = refillnum;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public int getIsdel() {
            return isdel;
        }

        public void setIsdel(int isdel) {
            this.isdel = isdel;
        }
    }
}
