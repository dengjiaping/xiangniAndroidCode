package com.ixiangni.bean;

import java.util.List;

/**
 * Created by John on 2016/11/27.
 */

public class RedPacketDetialBean  {

    /**
     * luckymoneyid : 389
     * userid : 92
     * targetid : 18
     * luckymoney : 12
     * luckymoneytype : 0
     * createtime : 1480167173000
     * message : 恭喜发财，大吉大利！
     * newskind : 0
     * luckmoneynum : 1
     * allocationtype : 0
     * yilingnum : 1
     * weilingnum : 0
     * tuihuinum : 0
     * yilingmoney : 12
     * weilingmoney : 0
     * tuihuimoney : 0
     * luckMoneyAllocationVoList : [{"allocationid":1410,"luckymoneyid":389,"mainuserid":92,"userid":18,"moneynum":12,"bestluck":0,"isoccupancy":1,"occupancytime":1480168512000,"createtime":1480167173000,"invalidtime":1480253573000,"orderlist":null,"luckmoneytype":0,"isbesttype":0,"groupno":null,"temp":null,"usernick":"soso","userpic":"http://mx.jlcp.com/yinxinupload/head/2016-11-18/TH1479458049018_mid.jpg"}]
     */

    private int luckymoneyid;
    private int userid;
    private long targetid;
    private double luckymoney;
    private int luckymoneytype;
    private long createtime;
    private String message;
    private int newskind;
    private int luckmoneynum;
    private int allocationtype;
    private int yilingnum;
    private int weilingnum;
    private int tuihuinum;
    private double yilingmoney;
    private double weilingmoney;
    private double tuihuimoney;
    private List<LuckMoneyAllocationVoListBean> luckMoneyAllocationVoList;

    public int getLuckymoneyid() {
        return luckymoneyid;
    }

    public void setLuckymoneyid(int luckymoneyid) {
        this.luckymoneyid = luckymoneyid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public long getTargetid() {
        return targetid;
    }

    public void setTargetid(long targetid) {
        this.targetid = targetid;
    }

    public double getLuckymoney() {
        return luckymoney;
    }

    public void setLuckymoney(double luckymoney) {
        this.luckymoney = luckymoney;
    }

    public int getLuckymoneytype() {
        return luckymoneytype;
    }

    public void setLuckymoneytype(int luckymoneytype) {
        this.luckymoneytype = luckymoneytype;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNewskind() {
        return newskind;
    }

    public void setNewskind(int newskind) {
        this.newskind = newskind;
    }

    public int getLuckmoneynum() {
        return luckmoneynum;
    }

    public void setLuckmoneynum(int luckmoneynum) {
        this.luckmoneynum = luckmoneynum;
    }

    public int getAllocationtype() {
        return allocationtype;
    }

    public void setAllocationtype(int allocationtype) {
        this.allocationtype = allocationtype;
    }

    public int getYilingnum() {
        return yilingnum;
    }

    public void setYilingnum(int yilingnum) {
        this.yilingnum = yilingnum;
    }

    public int getWeilingnum() {
        return weilingnum;
    }

    public void setWeilingnum(int weilingnum) {
        this.weilingnum = weilingnum;
    }

    public int getTuihuinum() {
        return tuihuinum;
    }

    public void setTuihuinum(int tuihuinum) {
        this.tuihuinum = tuihuinum;
    }

    public double getYilingmoney() {
        return yilingmoney;
    }

    public void setYilingmoney(double yilingmoney) {
        this.yilingmoney = yilingmoney;
    }

    public double getWeilingmoney() {
        return weilingmoney;
    }

    public void setWeilingmoney(double weilingmoney) {
        this.weilingmoney = weilingmoney;
    }

    public double getTuihuimoney() {
        return tuihuimoney;
    }

    public void setTuihuimoney(double tuihuimoney) {
        this.tuihuimoney = tuihuimoney;
    }

    public List<LuckMoneyAllocationVoListBean> getLuckMoneyAllocationVoList() {
        return luckMoneyAllocationVoList;
    }

    public void setLuckMoneyAllocationVoList(List<LuckMoneyAllocationVoListBean> luckMoneyAllocationVoList) {
        this.luckMoneyAllocationVoList = luckMoneyAllocationVoList;
    }

    public static class LuckMoneyAllocationVoListBean {
        /**
         * allocationid : 1410
         * luckymoneyid : 389
         * mainuserid : 92
         * userid : 18
         * moneynum : 12
         * bestluck : 0
         * isoccupancy : 1
         * occupancytime : 1480168512000
         * createtime : 1480167173000
         * invalidtime : 1480253573000
         * orderlist : null
         * luckmoneytype : 0
         * isbesttype : 0
         * groupno : null
         * temp : null
         * usernick : soso
         * userpic : http://mx.jlcp.com/yinxinupload/head/2016-11-18/TH1479458049018_mid.jpg
         */

        private int allocationid;
        private int luckymoneyid;
        private int mainuserid;
        private int userid;
        private double moneynum;
        private int bestluck;
        private int isoccupancy;
        private long occupancytime;
        private long createtime;
        private long invalidtime;
        private String orderlist;
        private int luckmoneytype;
        private int isbesttype;
        private String groupno;
        private String temp;
        private String usernick;
        private String userpic;

        public int getAllocationid() {
            return allocationid;
        }

        public void setAllocationid(int allocationid) {
            this.allocationid = allocationid;
        }

        public int getLuckymoneyid() {
            return luckymoneyid;
        }

        public void setLuckymoneyid(int luckymoneyid) {
            this.luckymoneyid = luckymoneyid;
        }

        public int getMainuserid() {
            return mainuserid;
        }

        public void setMainuserid(int mainuserid) {
            this.mainuserid = mainuserid;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public double getMoneynum() {
            return moneynum;
        }

        public void setMoneynum(double moneynum) {
            this.moneynum = moneynum;
        }

        public int getBestluck() {
            return bestluck;
        }

        public void setBestluck(int bestluck) {
            this.bestluck = bestluck;
        }

        public int getIsoccupancy() {
            return isoccupancy;
        }

        public void setIsoccupancy(int isoccupancy) {
            this.isoccupancy = isoccupancy;
        }

        public long getOccupancytime() {
            return occupancytime;
        }

        public void setOccupancytime(long occupancytime) {
            this.occupancytime = occupancytime;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public long getInvalidtime() {
            return invalidtime;
        }

        public void setInvalidtime(long invalidtime) {
            this.invalidtime = invalidtime;
        }

        public String getOrderlist() {
            return orderlist;
        }

        public void setOrderlist(String orderlist) {
            this.orderlist = orderlist;
        }

        public int getLuckmoneytype() {
            return luckmoneytype;
        }

        public void setLuckmoneytype(int luckmoneytype) {
            this.luckmoneytype = luckmoneytype;
        }

        public int getIsbesttype() {
            return isbesttype;
        }

        public void setIsbesttype(int isbesttype) {
            this.isbesttype = isbesttype;
        }

        public String getGroupno() {
            return groupno;
        }

        public void setGroupno(String groupno) {
            this.groupno = groupno;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
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
