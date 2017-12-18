package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class DepartmentBean {

    /**
     * message : 操作成功!
     * data : [{"branchid":1,"branchname":"部门1","createtime":1497869915000},{"branchid":2,"branchname":"部门2","createtime":1497869915000},{"branchid":3,"branchname":"部门3","createtime":1497869915000}]
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
         * branchid : 1
         * branchname : 部门1
         * createtime : 1497869915000
         */

        private int branchid;
        private String branchname;
        private long createtime;

        public int getBranchid() {
            return branchid;
        }

        public void setBranchid(int branchid) {
            this.branchid = branchid;
        }

        public String getBranchname() {
            return branchname;
        }

        public void setBranchname(String branchname) {
            this.branchname = branchname;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }
    }
}
