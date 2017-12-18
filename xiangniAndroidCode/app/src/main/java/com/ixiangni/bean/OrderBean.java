package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/8/4 0004.
 */

public  class OrderBean {

    /**
     * orderid : 368
     * userid : 10
     * ordername : 武汉丰颐大酒店
     * orderprice : 298.0
     * orderstatus : 0
     * ordercreatetime : 1501831721000
     * outtradeno : null
     * systradeno : 150183172118905
     * orderpaytype : 105
     * orderisdel : 0
     * ordertype : 8
     * mainuserid : null
     * mainusernick : null
     * luckmoneytype : null
     * groupchatname : null
     * shanglvsysno : HO170804152842526205
     * shanglvstr : {"uid":"3588","sid":"65","hid":"29802","hname":"武汉丰颐大酒店","rid":"85890","pid":"1785658","roomtype":"0","lasttime":"18:00","tm1":"2017-08-04","tm2":"2017-08-05","amount":"298","rooms":"1","passenger":"啊KKK","contacts":"啊KKK","phone":"18575358666","email":"425866588666699966","suppid":"zn","iscard":"0","cardno":"","year":"","month":"","code":"","name":"","idtype":"","idno":"","isInvoice":"0","invoiceType":"","recipient":"","invoicePhone":"","postCode":"","postAddress":"","allotmenttype":"","ratetype":"","supplierid":"","facepaytype":"","faceprice":"","includebreakfastqty2":""}
     * shanglvname : 啊KKK
     * shanglvtel : 18575358666
     * shanglvidno : 425866588666699966
     * describe1 : 普通标准间
     * describe2 : 双床1.2米
     * describe3 : 23
     * orderpic : http://tp1.znimg.com/hotel_images/17904/160x120_31801164_0_10_0_1.jpg
     * tax : null
     * oil : null
     * parmjson : null
     */

    private long orderid;
    private int userid;
    private String ordername;
    private double orderprice;
    private int orderstatus;
    private long ordercreatetime;
    private Object outtradeno;
    private String systradeno;
    private int orderpaytype;
    private int orderisdel;
    private int ordertype;
    private Object mainuserid;
    private Object mainusernick;
    private Object luckmoneytype;
    private Object groupchatname;
    private String shanglvsysno;
    private String shanglvstr;
    private String shanglvname;
    private String shanglvtel;
    private String shanglvidno;
    private String describe1;
    private String describe2;
    private String describe3;
    private String orderpic;
    private Object tax;
    private Object oil;
    private Object parmjson;

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getOrdername() {
        return ordername;
    }

    public void setOrdername(String ordername) {
        this.ordername = ordername;
    }

    public double getOrderprice() {
        return orderprice;
    }

    public void setOrderprice(double orderprice) {
        this.orderprice = orderprice;
    }

    public int getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(int orderstatus) {
        this.orderstatus = orderstatus;
    }

    public long getOrdercreatetime() {
        return ordercreatetime;
    }

    public void setOrdercreatetime(long ordercreatetime) {
        this.ordercreatetime = ordercreatetime;
    }

    public Object getOuttradeno() {
        return outtradeno;
    }

    public void setOuttradeno(Object outtradeno) {
        this.outtradeno = outtradeno;
    }

    public String getSystradeno() {
        return systradeno;
    }

    public void setSystradeno(String systradeno) {
        this.systradeno = systradeno;
    }

    public int getOrderpaytype() {
        return orderpaytype;
    }

    public void setOrderpaytype(int orderpaytype) {
        this.orderpaytype = orderpaytype;
    }

    public int getOrderisdel() {
        return orderisdel;
    }

    public void setOrderisdel(int orderisdel) {
        this.orderisdel = orderisdel;
    }

    public int getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(int ordertype) {
        this.ordertype = ordertype;
    }

    public Object getMainuserid() {
        return mainuserid;
    }

    public void setMainuserid(Object mainuserid) {
        this.mainuserid = mainuserid;
    }

    public Object getMainusernick() {
        return mainusernick;
    }

    public void setMainusernick(Object mainusernick) {
        this.mainusernick = mainusernick;
    }

    public Object getLuckmoneytype() {
        return luckmoneytype;
    }

    public void setLuckmoneytype(Object luckmoneytype) {
        this.luckmoneytype = luckmoneytype;
    }

    public Object getGroupchatname() {
        return groupchatname;
    }

    public void setGroupchatname(Object groupchatname) {
        this.groupchatname = groupchatname;
    }

    public String getShanglvsysno() {
        return shanglvsysno;
    }

    public void setShanglvsysno(String shanglvsysno) {
        this.shanglvsysno = shanglvsysno;
    }


    public String getShanglvstr() {
        return shanglvstr;
    }

    public void setShanglvstr(String shanglvstr) {
        this.shanglvstr = shanglvstr;
    }

    public String getShanglvname() {
        return shanglvname;
    }

    public void setShanglvname(String shanglvname) {
        this.shanglvname = shanglvname;
    }

    public String getShanglvtel() {
        return shanglvtel;
    }

    public void setShanglvtel(String shanglvtel) {
        this.shanglvtel = shanglvtel;
    }

    public String getShanglvidno() {
        return shanglvidno;
    }

    public void setShanglvidno(String shanglvidno) {
        this.shanglvidno = shanglvidno;
    }

    public String getDescribe1() {
        return describe1;
    }

    public void setDescribe1(String describe1) {
        this.describe1 = describe1;
    }

    public String getDescribe2() {
        return describe2;
    }

    public void setDescribe2(String describe2) {
        this.describe2 = describe2;
    }

    public String getDescribe3() {
        return describe3;
    }

    public void setDescribe3(String describe3) {
        this.describe3 = describe3;
    }

    public String getOrderpic() {
        return orderpic;
    }

    public void setOrderpic(String orderpic) {
        this.orderpic = orderpic;
    }

    public Object getTax() {
        return tax;
    }

    public void setTax(Object tax) {
        this.tax = tax;
    }

    public Object getOil() {
        return oil;
    }

    public void setOil(Object oil) {
        this.oil = oil;
    }

    public Object getParmjson() {
        return parmjson;
    }

    public void setParmjson(Object parmjson) {
        this.parmjson = parmjson;
    }
}
