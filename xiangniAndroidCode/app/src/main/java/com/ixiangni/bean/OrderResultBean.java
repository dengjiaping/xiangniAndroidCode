package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/8/7 0007.
 */

public class OrderResultBean {
    private int status;
    private String message;
    private OrderBean data;

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

    public OrderBean getData() {
        return data;
    }

    public void setData(OrderBean data) {
        this.data = data;
    }
}
