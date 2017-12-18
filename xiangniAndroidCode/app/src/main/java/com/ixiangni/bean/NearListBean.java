package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class NearListBean {
    private int status;
    private String message;
    private List<NearBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NearBean> getData() {
        return data;
    }

    public void setData(List<NearBean> data) {
        this.data = data;
    }
}
