package com.ixiangni.presenters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.bean.OrderBean;
import com.ixiangni.bean.OrderDetailBean;
import com.ixiangni.bean.OrderListBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/4 0004.
 */

public class OrderPresenter {

    private static final String TAG = "OrderPresenter";

//    token	是	String	用户token
//    ordertype	否	Integer	订单类型：8酒店；9火车票；10飞机票;11其他订单
//    orderstatus	否	Integer	订单状态：0未支付；1已支付  9火车出票中 10火车已出票
//    currentPage	否	int	当前页，默认为1
//    pageSize	否	int	每页记录数，默认为10

    public static final String ORDER_TYPE_HOTEL ="8";
    public static final String ORDER_TYPE_TRAIN ="9";
    public static final String ORDER_TYPE_PLANE ="10";

    public static final String ORDER_TYPE_OTHER ="11";

    public static final String ORDER_STATUS_YES ="1";
    public static final String ORDER_STATUS_NO ="0";
    public static final String TRAIN_ORDER_STATUS_YES ="10";
    public static final String TRAIN_ORDER_STATUS_NO ="9";
    public static final String PLAINE_ORDER_STATUS_YSE ="5";
    public static final String PLAINE_ORDER_STATUS_NO ="4";

    public static final String TUIPIAO_ORDER_STATUS_SUCCESS ="3";
    public static final String PLAINE_ORDER_STATUS_ING ="100";
    public static final String TRAIN_ORDER_STATUS_ING ="8";



    public void getOrderList(Context context, String ordertype, String orderstatus, String currentPage, String pageSize, OnResult<List<OrderBean>> result) {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        if (!TextUtils.isEmpty(ordertype)) {
            params.put("ordertype", ordertype);
        }

        if (!TextUtils.isEmpty(orderstatus)) {
            params.put("orderstatus", orderstatus);
        }

        params.put(XNConstants.currentPage,currentPage);
        params.put(XNConstants.pageSize,pageSize);

        Log.i(TAG, "getOrderList: "+params.toString());

        RemoteDataHandler.asyncPost(UrlString.URL_ORDER_LIST, params, context, true, response -> {
            String json = response.getJson();
            Log.i(TAG, "orderType: "+json);

            try {
                Log.i(TAG, "result: "+json.replace("\"",""));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(CommonUtils.isStringNull(json)){
              result.onFailed(Constants.CONNECT_SERVER_FAILED);
            }else {
                OrderListBean orderListBean = new Gson().fromJson(json, OrderListBean.class);
                if(1==orderListBean.getStatus()){
                    result.onSuccess(orderListBean.getData());
                }else {
                    result.onFailed(orderListBean.getMessage());
                }
            }

        });

    }


    public void getOrderDetail(Context context,String orderid,OnResult<OrderBean> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put(XNConstants.orderid,orderid);
        RemoteDataHandler.asyncPost(UrlString.URL_ORDER_DETIAL,params,context,false, response -> {
            String json = response.getJson();
            if(CommonUtils.isStringNull(json)){
                callback.onFailed(Constants.CONNECT_SERVER_FAILED);
            }else {
                OrderDetailBean orderDetailBean = new Gson().fromJson(json, OrderDetailBean.class);
                if(1==orderDetailBean.getStatus()){
                    callback.onSuccess(orderDetailBean.getData());
                }else {
                    callback.onFailed(orderDetailBean.getMessage());
                }
            }

        });
    }


}
