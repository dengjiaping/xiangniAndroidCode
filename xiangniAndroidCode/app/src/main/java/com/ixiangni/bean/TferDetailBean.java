package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class TferDetailBean {

    /**
     * message : 操作成功!
     * data : {"orderid":77,"userid":10,"ordername":"想你转账","orderprice":1,"orderstatus":null,"ordercreatetime":1500429327000,"outtradeno":null,"systradeno":"150042932745455","orderpaytype":103,"orderisdel":0,"ordertype":6,"mainuserid":3,"mainusernick":"想你狗","luckmoneytype":null,"groupchatname":null,"usernick":"徐传霆啊","userpic":"http://handongkeji.com:8090/ixiangniupload/head/2017-06-29/QT1498726653905_mid.jpg"}
     * status : 1
     */

    private String message;
    private DataBean data;
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * orderid : 77
         * userid : 10
         * ordername : 想你转账
         * orderprice : 1.0
         * orderstatus : null
         * ordercreatetime : 1500429327000
         * outtradeno : null
         * systradeno : 150042932745455
         * orderpaytype : 103
         * orderisdel : 0
         * ordertype : 6
         * mainuserid : 3
         * mainusernick : 想你狗
         * luckmoneytype : null
         * groupchatname : null
         * usernick : 徐传霆啊
         * userpic : http://handongkeji.com:8090/ixiangniupload/head/2017-06-29/QT1498726653905_mid.jpg
         */

        private int orderid;
        private int userid;
        private String ordername;
        private double orderprice;
        private Object orderstatus;
        private long ordercreatetime;
        private Object outtradeno;
        private String systradeno;
        private int orderpaytype;
        private int orderisdel;
        private int ordertype;
        private int mainuserid;
        private String mainusernick;
        private Object luckmoneytype;
        private Object groupchatname;
        private String usernick;
        private String userpic;

        public int getOrderid() {
            return orderid;
        }

        public void setOrderid(int orderid) {
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

        public Object getOrderstatus() {
            return orderstatus;
        }

        public void setOrderstatus(Object orderstatus) {
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

        public int getMainuserid() {
            return mainuserid;
        }

        public void setMainuserid(int mainuserid) {
            this.mainuserid = mainuserid;
        }

        public String getMainusernick() {
            return mainusernick;
        }

        public void setMainusernick(String mainusernick) {
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

        public String getUsernick() {
            return usernick;
        }

        public void setUsernick(String usernick) {
            this.usernick = usernick;
        }

        public String getUserpic() {
            return userpic;
        }

        public void setUserpic(String userpic) {
            this.userpic = userpic;
        }
    }
}
