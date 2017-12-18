package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/24 0024.
 */

public class CityListBean {
    private List<CityBean> data;
    private String message;
    private int status;

    public List<CityBean> getData() {
        return data;
    }


    public void setData(List<CityBean> data) {
        this.data = data;
    }

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
}
