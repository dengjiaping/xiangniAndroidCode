package com.ixiangni.presenters;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.bean.RoomInfo;
import com.ixiangni.bean.RoomYuding;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.MyPresenter;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/3 0003.
 */

public class HotelPresenter {

    private static final String TAG = "HotelPresenter";

    //    token	是	String	用户token
//    hid	是	String	酒店id
//    rid	是	String	房型id。酒店详情roomList中获取
//    pid	是	String	计划id。酒店详情roomList中plans中的planid
//    suppid	是	String	供应商。酒店详情roomList中plans中的hotelsupplier
//    tm1	否	String	入住时间(例：2017-07-18)
//    tm2	否	String	离店时间(例：2017-07-20)
    public void getRoomYudingInfo(Context context, RoomInfo info,
                                  String tm1, String tm2, final OnResult<RoomYuding.DataBean> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("hid",info.getHotelid());
        params.put("rid",info.getRoomid());
        params.put("pid",info.getPlainid());
        params.put("suppid",info.getSuppid());
        if(!CommonUtils.isStringNull(tm1)){
            params.put("tm1",tm1);
        }
        if(!CommonUtils.isStringNull(tm2)){
            params.put("tm2",tm2);
        }

        RemoteDataHandler.asyncPost(UrlString.URL_ROOM_DETAIL,params,context,false, response -> {
            String json = response.getJson();
            if(CommonUtils.isStringNull(json)){
                callback.onFailed(Constants.CONNECT_SERVER_FAILED);

            }else {

                RoomYuding roomYuding = new Gson().fromJson(json, RoomYuding.class);
                if(1==roomYuding.getStatus()){
                    callback.onSuccess(roomYuding.getData());
                }else {
                    callback.onFailed(roomYuding.getMessage());
                }

            }


        });
    }


//    token	是	String	用户token
//    hid	是	String	酒店id
//    hname	是	String	酒店名称
//    rid	是	String	房型id。酒店详情roomList中获取
//    pid	是	String	计划id。酒店详情roomList中plans中的planid
//    roomtype	是	String	房型类型：0现付;1预付(房型详情中的roomtype)
//    tm1	是	String	入住时间(例：2017-07-18)
//    tm2	是	String	离店时间(例：2017-07-20)
//    amount	是	String	订单金额。房型详情中prices中的TotalPrice
//    contacts	是	String	联系人
//    phone	是	String	手机号
//    idno	是	String	身份证
//    suppid	是	String	房型详情中的 hotelsupplier
//    paypassword	是	String	支付密码
//    title	是	String	房型。房型详情中的RoomName
//    bed	是	String	床型。房型详情中的roomsInfo中的bed
//    area	是	String	面积。房型详情中的roomsInfo中的area
//    orderpic	是	String	图片。酒店列表中的Picture


    public void submitOrder(Context context,RoomInfo info,String roomtype,OnResult<String> callback){
        HashMap<String, String> params = new HashMap<>();

        params.put(Constants.token,MyApp.getInstance().getUserTicket());
        params.put("hid",info.getHotelid());
        params.put("hname",info.getHotelName());
        params.put("rid",info.getRoomid());
        params.put("pid",info.getPlainid());
        params.put("roomtype",roomtype);
        params.put("tm1",info.getTm1());
        params.put("tm2",info.getTm2());
        params.put("amount",info.getAmount());
        params.put("contacts",info.getContacts());
        params.put("phone",info.getPhone());
        params.put("idno",info.getIdno());
        params.put("suppid",info.getSuppid());
        params.put("paypassword",info.getPaypassword());
        params.put("title",info.getTitle());
        params.put("bed",info.getBed());
        params.put("area",info.getArea());
        params.put("orderpic",info.getOrderpic());

//        Log.i(TAG, "params: "+params.toString());
//        RemoteDataHandler.asyncPost(UrlString.URL_SUBMIT_HOTEL_ORDER,params,context,false, response -> {
//            String json = response.getJson();
//            Log.i(TAG, "submitOrder: "+json);
//
//
//        });
        MyPresenter.request1(context,UrlString.URL_SUBMIT_HOTEL_ORDER,params,callback);
    }







}
