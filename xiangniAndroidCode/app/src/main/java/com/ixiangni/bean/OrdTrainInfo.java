package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class OrdTrainInfo {

    /**
     * uid : 3588
     * sid : 65
     * TrainNo : G103
     * SCity : 北京南
     * ECity : 上海虹桥
     * STime : 07:05
     * ETime : 12:42
     * SDate : 2017-08-10
     * Name : 啦啦啦
     * Mobile : 18457145367
     * Email :
     * Amount : 555.0
     * TicketCount : 1
     * vcode :
     * PsgInfo : [{"CardNo":"456647428385666666","CardType":"身份证","IncAmount":"0","Phone":"18457145367","PsgName":"啦啦啦","Saleprice":"553.0","SeatType":"二等座","TicketType":"成人"}]
     * RunTime : 337
     */

    private String uid;
    private String sid;
    private String TrainNo;
    private String SCity;
    private String ECity;
    private String STime;
    private String ETime;
    private String SDate;
    private String Name;
    private String Mobile;
    private String Email;
    private String Amount;
    private String TicketCount;
    private String vcode;
    private String RunTime;
    private List<PsgInfoBean> PsgInfo;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTrainNo() {
        return TrainNo;
    }

    public void setTrainNo(String TrainNo) {
        this.TrainNo = TrainNo;
    }

    public String getSCity() {
        return SCity;
    }

    public void setSCity(String SCity) {
        this.SCity = SCity;
    }

    public String getECity() {
        return ECity;
    }

    public void setECity(String ECity) {
        this.ECity = ECity;
    }

    public String getSTime() {
        return STime;
    }

    public void setSTime(String STime) {
        this.STime = STime;
    }

    public String getETime() {
        return ETime;
    }

    public void setETime(String ETime) {
        this.ETime = ETime;
    }

    public String getSDate() {
        return SDate;
    }

    public void setSDate(String SDate) {
        this.SDate = SDate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public String getTicketCount() {
        return TicketCount;
    }

    public void setTicketCount(String TicketCount) {
        this.TicketCount = TicketCount;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getRunTime() {
        return RunTime;
    }

    public void setRunTime(String RunTime) {
        this.RunTime = RunTime;
    }

    public List<PsgInfoBean> getPsgInfo() {
        return PsgInfo;
    }

    public void setPsgInfo(List<PsgInfoBean> PsgInfo) {
        this.PsgInfo = PsgInfo;
    }

    public static class PsgInfoBean {
        /**
         * CardNo : 456647428385666666
         * CardType : 身份证
         * IncAmount : 0
         * Phone : 18457145367
         * PsgName : 啦啦啦
         * Saleprice : 553.0
         * SeatType : 二等座
         * TicketType : 成人
         */

        private String CardNo;
        private String CardType;
        private String IncAmount;
        private String Phone;
        private String PsgName;
        private String Saleprice;
        private String SeatType;
        private String TicketType;

        public String getCardNo() {
            return CardNo;
        }

        public void setCardNo(String CardNo) {
            this.CardNo = CardNo;
        }

        public String getCardType() {
            return CardType;
        }

        public void setCardType(String CardType) {
            this.CardType = CardType;
        }

        public String getIncAmount() {
            return IncAmount;
        }

        public void setIncAmount(String IncAmount) {
            this.IncAmount = IncAmount;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String Phone) {
            this.Phone = Phone;
        }

        public String getPsgName() {
            return PsgName;
        }

        public void setPsgName(String PsgName) {
            this.PsgName = PsgName;
        }

        public String getSaleprice() {
            return Saleprice;
        }

        public void setSaleprice(String Saleprice) {
            this.Saleprice = Saleprice;
        }

        public String getSeatType() {
            return SeatType;
        }

        public void setSeatType(String SeatType) {
            this.SeatType = SeatType;
        }

        public String getTicketType() {
            return TicketType;
        }

        public void setTicketType(String TicketType) {
            this.TicketType = TicketType;
        }
    }
}
