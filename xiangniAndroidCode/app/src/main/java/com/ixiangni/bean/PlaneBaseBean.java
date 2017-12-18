package com.ixiangni.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/8/5 0005.
 */

public  class PlaneBaseBean implements Parcelable {
    /**
     * AirLineCode : CZ
     * CarrinerName : 南方航空
     * StartPortName : 北京首都国际机场
     * StartPort : PEK
     * EndPortName : 成都双流国际机场
     * EndPort : CTU
     * FlightNo : CZ3903
     * JoinPort :
     * JoinDate : 0001-01-01
     * MinCabin : W
     * MinDiscount : 100
     * MinFare : 1690
     * MinTicketCount : 10
     * OffTime : 2017-08-05 14:45:00
     * ArriveTime : 2017-08-05 18:00:00
     * StartT : T2
     * EndT : T2
     * ByPass : 0
     * Meat : 1
     * StaFare : 1690
     * Oil : 0
     * Tax : 50
     * Distance : 0
     * PlaneType : 333
     * PlaneModel : 大
     * RunTime : 3小时15分钟
     * ETicket : 1
     */


    private String AirLineCode;
    private String CarrinerName;
    private String StartPortName;
    private String StartPort;
    private String EndPortName;
    private String EndPort;
    private String FlightNo;
    private String JoinPort;
    private String JoinDate;
    private String MinCabin;
    private String MinDiscount;
    private String MinFare;
    private String MinTicketCount;
    private String OffTime;
    private String ArriveTime;
    private String StartT;
    private String EndT;
    private String ByPass;
    private String Meat;
    private int StaFare;
    private int Oil;
    private int Tax;
    private int Distance;
    private String PlaneType;
    private String PlaneModel;
    private String RunTime;
    private String ETicket;

    public String getAirLineCode() {
        return AirLineCode;
    }

    public void setAirLineCode(String AirLineCode) {
        this.AirLineCode = AirLineCode;
    }

    public String getCarrinerName() {
        return CarrinerName;
    }

    public void setCarrinerName(String CarrinerName) {
        this.CarrinerName = CarrinerName;
    }

    public String getStartPortName() {
        return StartPortName;
    }

    public void setStartPortName(String StartPortName) {
        this.StartPortName = StartPortName;
    }

    public String getStartPort() {
        return StartPort;
    }

    public void setStartPort(String StartPort) {
        this.StartPort = StartPort;
    }

    public String getEndPortName() {
        return EndPortName;
    }

    public void setEndPortName(String EndPortName) {
        this.EndPortName = EndPortName;
    }

    public String getEndPort() {
        return EndPort;
    }

    public void setEndPort(String EndPort) {
        this.EndPort = EndPort;
    }

    public String getFlightNo() {
        return FlightNo;
    }

    public void setFlightNo(String FlightNo) {
        this.FlightNo = FlightNo;
    }

    public String getJoinPort() {
        return JoinPort;
    }

    public void setJoinPort(String JoinPort) {
        this.JoinPort = JoinPort;
    }

    public String getJoinDate() {
        return JoinDate;
    }

    public void setJoinDate(String JoinDate) {
        this.JoinDate = JoinDate;
    }

    public String getMinCabin() {
        return MinCabin;
    }

    public void setMinCabin(String MinCabin) {
        this.MinCabin = MinCabin;
    }

    public String getMinDiscount() {
        return MinDiscount;
    }

    public void setMinDiscount(String MinDiscount) {
        this.MinDiscount = MinDiscount;
    }

    public String getMinFare() {
        return MinFare;
    }

    public void setMinFare(String MinFare) {
        this.MinFare = MinFare;
    }

    public String getMinTicketCount() {
        return MinTicketCount;
    }

    public void setMinTicketCount(String MinTicketCount) {
        this.MinTicketCount = MinTicketCount;
    }

    public String getOffTime() {
        return OffTime;
    }

    public void setOffTime(String OffTime) {
        this.OffTime = OffTime;
    }

    public String getArriveTime() {
        return ArriveTime;
    }

    public void setArriveTime(String ArriveTime) {
        this.ArriveTime = ArriveTime;
    }

    public String getStartT() {
        return StartT;
    }

    public void setStartT(String StartT) {
        this.StartT = StartT;
    }

    public String getEndT() {
        return EndT;
    }

    public void setEndT(String EndT) {
        this.EndT = EndT;
    }

    public String getByPass() {
        return ByPass;
    }

    public void setByPass(String ByPass) {
        this.ByPass = ByPass;
    }

    public String getMeat() {
        return Meat;
    }

    public void setMeat(String Meat) {
        this.Meat = Meat;
    }

    public int getStaFare() {
        return StaFare;
    }

    public void setStaFare(int StaFare) {
        this.StaFare = StaFare;
    }

    public int getOil() {
        return Oil;
    }

    public void setOil(int Oil) {
        this.Oil = Oil;
    }

    public int getTax() {
        return Tax;
    }

    public void setTax(int Tax) {
        this.Tax = Tax;
    }

    public int getDistance() {
        return Distance;
    }

    public void setDistance(int Distance) {
        this.Distance = Distance;
    }

    public String getPlaneType() {
        return PlaneType;
    }

    public void setPlaneType(String PlaneType) {
        this.PlaneType = PlaneType;
    }

    public String getPlaneModel() {
        return PlaneModel;
    }

    public void setPlaneModel(String PlaneModel) {
        this.PlaneModel = PlaneModel;
    }

    public String getRunTime() {
        return RunTime;
    }

    public void setRunTime(String RunTime) {
        this.RunTime = RunTime;
    }

    public String getETicket() {
        return ETicket;
    }

    public void setETicket(String ETicket) {
        this.ETicket = ETicket;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.AirLineCode);
        dest.writeString(this.CarrinerName);
        dest.writeString(this.StartPortName);
        dest.writeString(this.StartPort);
        dest.writeString(this.EndPortName);
        dest.writeString(this.EndPort);
        dest.writeString(this.FlightNo);
        dest.writeString(this.JoinPort);
        dest.writeString(this.JoinDate);
        dest.writeString(this.MinCabin);
        dest.writeString(this.MinDiscount);
        dest.writeString(this.MinFare);
        dest.writeString(this.MinTicketCount);
        dest.writeString(this.OffTime);
        dest.writeString(this.ArriveTime);
        dest.writeString(this.StartT);
        dest.writeString(this.EndT);
        dest.writeString(this.ByPass);
        dest.writeString(this.Meat);
        dest.writeInt(this.StaFare);
        dest.writeInt(this.Oil);
        dest.writeInt(this.Tax);
        dest.writeInt(this.Distance);
        dest.writeString(this.PlaneType);
        dest.writeString(this.PlaneModel);
        dest.writeString(this.RunTime);
        dest.writeString(this.ETicket);
    }

    public PlaneBaseBean() {
    }

    protected PlaneBaseBean(Parcel in) {
        this.AirLineCode = in.readString();
        this.CarrinerName = in.readString();
        this.StartPortName = in.readString();
        this.StartPort = in.readString();
        this.EndPortName = in.readString();
        this.EndPort = in.readString();
        this.FlightNo = in.readString();
        this.JoinPort = in.readString();
        this.JoinDate = in.readString();
        this.MinCabin = in.readString();
        this.MinDiscount = in.readString();
        this.MinFare = in.readString();
        this.MinTicketCount = in.readString();
        this.OffTime = in.readString();
        this.ArriveTime = in.readString();
        this.StartT = in.readString();
        this.EndT = in.readString();
        this.ByPass = in.readString();
        this.Meat = in.readString();
        this.StaFare = in.readInt();
        this.Oil = in.readInt();
        this.Tax = in.readInt();
        this.Distance = in.readInt();
        this.PlaneType = in.readString();
        this.PlaneModel = in.readString();
        this.RunTime = in.readString();
        this.ETicket = in.readString();
    }

    public static final Parcelable.Creator<PlaneBaseBean> CREATOR = new Parcelable.Creator<PlaneBaseBean>() {
        @Override
        public PlaneBaseBean createFromParcel(Parcel source) {
            return new PlaneBaseBean(source);
        }

        @Override
        public PlaneBaseBean[] newArray(int size) {
            return new PlaneBaseBean[size];
        }
    };
}
