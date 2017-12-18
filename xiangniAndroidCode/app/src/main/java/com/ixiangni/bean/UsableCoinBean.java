package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class UsableCoinBean {


    /**
     * message : 操作成功!
     * data : 0.0
     * status : 1
     */

    private String message;
    private double data;
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getData() {

        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
