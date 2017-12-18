package com.ixiangni.presenters.contract;

/**
 * Created by Administrator on 2016/11/12.
 */
public interface BaseView {
    void showLoading(String msg);
    void hideLoading();
    void showError(String errorMsg);
}
