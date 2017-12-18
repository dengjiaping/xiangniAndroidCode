package com.ixiangni.interfaces;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class IOrdRefresh {

    //刷新飞机票订单列表
    public interface IRefreshPlane{
        void onRefresh();
    }


    //刷新火车票订单列表
    public interface IRefreshTrain{
        void onRefresh();
    }
    //刷新其他订单列表
    public interface IRefreshOther{
        void onRefresh();
    }
}
