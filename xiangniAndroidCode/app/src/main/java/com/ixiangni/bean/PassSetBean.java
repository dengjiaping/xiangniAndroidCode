package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class PassSetBean {

    /**
     * message : 操作成功!
     * data : 1
     * status : 1
     */

    private String message;
    private int data;
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
