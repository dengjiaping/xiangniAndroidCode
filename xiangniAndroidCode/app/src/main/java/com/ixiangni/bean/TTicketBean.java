package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7 0007.
 */

public  class TTicketBean {
    /**
     * ToStation :
     * FromStation :
     * Seatfeature :
     * TrainCode : Z107
     * StationS : 北京西
     * StationE : 深圳
     * SFType : 始-终
     * TrainType : 新空直达
     * TrainID : Z107
     * Distance :
     * RunTime : 1336
     * ETime : 18:11
     * GoTime : 19:55
     * YuDing : True
     * SeatList : [{"type":"无座","price":"254.5","shengyu":"100"}]
     */

    private String ToStation;
    private String FromStation;
    private String Seatfeature;
    private String TrainCode;
    private String StationS;
    private String StationE;
    private String SFType;
    private String TrainType;
    private String TrainID;
    private String Distance;
    private String RunTime;
    private String ETime;
    private String GoTime;
    private String YuDing;
    private List<SeatBean> SeatList;

    public String getToStation() {
        return ToStation;
    }

    public void setToStation(String ToStation) {
        this.ToStation = ToStation;
    }

    public String getFromStation() {
        return FromStation;
    }

    public void setFromStation(String FromStation) {
        this.FromStation = FromStation;
    }

    public String getSeatfeature() {
        return Seatfeature;
    }

    public void setSeatfeature(String Seatfeature) {
        this.Seatfeature = Seatfeature;
    }

    public String getTrainCode() {
        return TrainCode;
    }

    public void setTrainCode(String TrainCode) {
        this.TrainCode = TrainCode;
    }

    public String getStationS() {
        return StationS;
    }

    public void setStationS(String StationS) {
        this.StationS = StationS;
    }

    public String getStationE() {
        return StationE;
    }

    public void setStationE(String StationE) {
        this.StationE = StationE;
    }

    public String getSFType() {
        return SFType;
    }

    public void setSFType(String SFType) {
        this.SFType = SFType;
    }

    public String getTrainType() {
        return TrainType;
    }

    public void setTrainType(String TrainType) {
        this.TrainType = TrainType;
    }

    public String getTrainID() {
        return TrainID;
    }

    public void setTrainID(String TrainID) {
        this.TrainID = TrainID;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String Distance) {
        this.Distance = Distance;
    }

    public String getRunTime() {
        return RunTime;
    }

    public void setRunTime(String RunTime) {
        this.RunTime = RunTime;
    }

    public String getETime() {
        return ETime;
    }

    public void setETime(String ETime) {
        this.ETime = ETime;
    }

    public String getGoTime() {
        return GoTime;
    }

    public void setGoTime(String GoTime) {
        this.GoTime = GoTime;
    }

    public String getYuDing() {
        return YuDing;
    }

    public void setYuDing(String YuDing) {
        this.YuDing = YuDing;
    }

    public List<SeatBean> getSeatList() {
        return SeatList;
    }

    public void setSeatList(List<SeatBean> seatList) {
        SeatList = seatList;
    }
}
