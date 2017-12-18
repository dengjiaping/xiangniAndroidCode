package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/8/7 0007.
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 火车票下单须传的参数
 * @ClassName:TParams

 * @PackageName:com.ixiangni.bean

 * @Create On 2017/8/7 0007   11:32

 * @Site:http://www.handongkeji.com

 * @author:xuchuanting

 * @Copyrights 2017/8/7 0007 handongkeji All rights reserved.
 */
public class TParams implements Parcelable {

//    TrainNo	是	String	车次编号
//    SCity	是	String	出发城市。火车票列表中的StationS
//    ECity	是	String	到达城市。火车票列表中的StationE
//    STime	是	String	出发时间。火车票列表中的GoTime
//    ETime	是	String	到达时间。火车票列表中的ETime
//    SDate	是	String	出发日期。查询火车票时传入的date(例:2017-07-21)
//    Amount	是	String	订单金额。火车票列表中的SeatList中的price+2
//    CardNo	是	String	身份证号
//    Phone	是	String	手机号
//    PsgName	是	String	乘车人姓名
//    SeatType	是	String	席别。火车票列表中的SeatList中的type(例:无座)
//    paypassword	是	String	支付密码
//    RunTime	是	String	运行时间。火车票列表中的RunTime

    private String TrainNo;
    private String SCity;
    private String ECity;
    private String STime;
    private String ETime;
    private String SDate;
    private String Amount;
    private String CardNo;
    private String Phone;
    private String PsgName;
    private String SeatType;
    private String paypassword;
    private String RunTime;
    private String TrainType;

    public String getTrainNo() {
        return TrainNo;
    }

    public void setTrainNo(String trainNo) {
        TrainNo = trainNo;
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

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPsgName() {
        return PsgName;
    }

    public void setPsgName(String psgName) {
        PsgName = psgName;
    }

    public String getSeatType() {
        return SeatType;
    }

    public void setSeatType(String seatType) {
        SeatType = seatType;
    }

    public String getPaypassword() {
        return paypassword;
    }

    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword;
    }

    public String getRunTime() {
        return RunTime;
    }

    public void setRunTime(String runTime) {

        RunTime = runTime;
    }

    public String getTrainType() {
        return TrainType;
    }

    public void setTrainType(String trainType) {
        TrainType = trainType;
    }

    public TParams() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.TrainNo);
        dest.writeString(this.SCity);
        dest.writeString(this.ECity);
        dest.writeString(this.STime);
        dest.writeString(this.ETime);
        dest.writeString(this.SDate);
        dest.writeString(this.Amount);
        dest.writeString(this.CardNo);
        dest.writeString(this.Phone);
        dest.writeString(this.PsgName);
        dest.writeString(this.SeatType);
        dest.writeString(this.paypassword);
        dest.writeString(this.RunTime);
        dest.writeString(this.TrainType);
    }

    protected TParams(Parcel in) {
        this.TrainNo = in.readString();
        this.SCity = in.readString();
        this.ECity = in.readString();
        this.STime = in.readString();
        this.ETime = in.readString();
        this.SDate = in.readString();
        this.Amount = in.readString();
        this.CardNo = in.readString();
        this.Phone = in.readString();
        this.PsgName = in.readString();
        this.SeatType = in.readString();
        this.paypassword = in.readString();
        this.RunTime = in.readString();
        this.TrainType = in.readString();
    }

    public static final Creator<TParams> CREATOR = new Creator<TParams>() {
        @Override
        public TParams createFromParcel(Parcel source) {
            return new TParams(source);
        }

        @Override
        public TParams[] newArray(int size) {
            return new TParams[size];
        }
    };
}
