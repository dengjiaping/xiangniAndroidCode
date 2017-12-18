package com.mydemo.yuanxin.util;

import com.mydemo.yuanxin.bean.BankInfo;

/**
 * Created by Administrator on 2017/11/14.
 */

public class MessageEvent {
    private String message;
    private BankInfo bankInfo;

    public MessageEvent(String message) {
        this.message = message;
    }
    public MessageEvent(BankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }

    public BankInfo getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(BankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
