package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/4 0004.
 */

public class OrderListBean {

    /**
     * message : 操作成功!
     * data : [{"orderid":368,"userid":10,"ordername":"武汉丰颐大酒店","orderprice":298,"orderstatus":0,"ordercreatetime":1501831721000,"outtradeno":null,"systradeno":"150183172118905","orderpaytype":105,"orderisdel":0,"ordertype":8,"mainuserid":null,"mainusernick":null,"luckmoneytype":null,"groupchatname":null,"shanglvsysno":"HO170804152842526205","shanglvstr":"{\"uid\":\"3588\",\"sid\":\"65\",\"hid\":\"29802\",\"hname\":\"武汉丰颐大酒店\",\"rid\":\"85890\",\"pid\":\"1785658\",\"roomtype\":\"0\",\"lasttime\":\"18:00\",\"tm1\":\"2017-08-04\",\"tm2\":\"2017-08-05\",\"amount\":\"298\",\"rooms\":\"1\",\"passenger\":\"啊KKK\",\"contacts\":\"啊KKK\",\"phone\":\"18575358666\",\"email\":\"425866588666699966\",\"suppid\":\"zn\",\"iscard\":\"0\",\"cardno\":\"\",\"year\":\"\",\"month\":\"\",\"code\":\"\",\"name\":\"\",\"idtype\":\"\",\"idno\":\"\",\"isInvoice\":\"0\",\"invoiceType\":\"\",\"recipient\":\"\",\"invoicePhone\":\"\",\"postCode\":\"\",\"postAddress\":\"\",\"allotmenttype\":\"\",\"ratetype\":\"\",\"supplierid\":\"\",\"facepaytype\":\"\",\"faceprice\":\"\",\"includebreakfastqty2\":\"\"}","shanglvname":"啊KKK","shanglvtel":"18575358666","shanglvidno":"425866588666699966","describe1":"普通标准间","describe2":"双床1.2米","describe3":"23","orderpic":"http://tp1.znimg.com/hotel_images/17904/160x120_31801164_0_10_0_1.jpg","tax":null,"oil":null,"parmjson":null}]
     * status : 1
     */

    private String message;
    private int status;
    private List<OrderBean> data;

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

    public List<OrderBean> getData() {
        return data;
    }

    public void setData(List<OrderBean> data) {
        this.data = data;
    }


}
