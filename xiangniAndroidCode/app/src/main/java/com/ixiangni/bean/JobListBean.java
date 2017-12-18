package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class JobListBean {

    /**
     * message : 操作成功!
     * data : [{"jobid":1,"branchid":1,"jobname":"职位1","createtime":1497869985000},{"jobid":2,"branchid":1,"jobname":"职位2","createtime":1497869985000}]
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
         * jobid : 1
         * branchid : 1
         * jobname : 职位1
         * createtime : 1497869985000
         */

        private int jobid;
        private int branchid;
        private String jobname;
        private long createtime;

        public int getJobid() {
            return jobid;
        }

        public void setJobid(int jobid) {
            this.jobid = jobid;
        }

        public int getBranchid() {
            return branchid;
        }

        public void setBranchid(int branchid) {
            this.branchid = branchid;
        }

        public String getJobname() {
            return jobname;
        }

        public void setJobname(String jobname) {
            this.jobname = jobname;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }
    }
}
