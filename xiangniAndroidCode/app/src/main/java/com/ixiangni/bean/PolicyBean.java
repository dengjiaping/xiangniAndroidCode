package com.ixiangni.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 优惠政策
 * @ClassName:PolicyBean

 * @PackageName:com.ixiangni.bean

 * @Create On 2017/8/7 0007   09:12

 * @Site:http://www.handongkeji.com

 * @author:xuchuanting

 * @Copyrights 2017/8/7 0007 handongkeji All rights reserved.
 */

public  class PolicyBean implements Parcelable {
    /**
     * fare : 2420
     * sale : 2420
     * flightno : CZ6135
     * cabin : D
     * policyid : 1708051611010466
     * platname : JiKe
     * totalrate : 0.0
     * userrate : 0
     * bookdata : EFB3AB0B7E3736492886C53B48217ED3CA5EF17AB0886CE1132E5ABE0FB9D84FCADAD031063FE01445EFB625EA0FDCDEF81FED32B4293F2823930BD99664BDFF1F72B33C92B5E4D2FD0DC14CC00106B273600613C8D5059CDEF1F18BF252BD367E07351CC0FA08A44829542EEC7C179738887875406DF49DD14C6137C051E15E8A9DC5C0C8C7ABE8896D5F2F3891FE3FD234A19D59D110D30BFC25E9391094E16C0DC494081DBABA518EEEB0D7647E92F3B6CCE84E4E51B91EAAE2FA12ECE344333E80F5696381057722B9410C244DC1E8D03BAA6BAB7E264A3E339975A5EB3F6358D6B2B7D93175
     * rateinfo : 0,0.0,0,0,0
     * remark : 退票改签按照航司规定执行<br />如果自行从航空公司申请改签，平台将收回代理费！
     * isspepolicy : 0
     * wtime : 00:00-23:59
     * rftime : 00:00-23:59
     */

    private int fare;
    private int sale;
    private String flightno;
    private String cabin;
    private String policyid;
    private String platname;
    private String totalrate;
    private String userrate;
    private String bookdata;
    private String rateinfo;
    private String remark;
    private String isspepolicy;
    private String wtime;
    private String rftime;

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public String getFlightno() {
        return flightno;
    }

    public void setFlightno(String flightno) {
        this.flightno = flightno;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getPolicyid() {
        return policyid;
    }

    public void setPolicyid(String policyid) {
        this.policyid = policyid;
    }

    public String getPlatname() {
        return platname;
    }

    public void setPlatname(String platname) {
        this.platname = platname;
    }

    public String getTotalrate() {
        return totalrate;
    }

    public void setTotalrate(String totalrate) {
        this.totalrate = totalrate;
    }

    public String getUserrate() {
        return userrate;
    }

    public void setUserrate(String userrate) {
        this.userrate = userrate;
    }

    public String getBookdata() {
        return bookdata;
    }

    public void setBookdata(String bookdata) {
        this.bookdata = bookdata;
    }

    public String getRateinfo() {
        return rateinfo;
    }

    public void setRateinfo(String rateinfo) {
        this.rateinfo = rateinfo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsspepolicy() {
        return isspepolicy;
    }

    public void setIsspepolicy(String isspepolicy) {
        this.isspepolicy = isspepolicy;
    }

    public String getWtime() {
        return wtime;
    }

    public void setWtime(String wtime) {
        this.wtime = wtime;
    }

    public String getRftime() {
        return rftime;
    }

    public void setRftime(String rftime) {
        this.rftime = rftime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.fare);
        dest.writeInt(this.sale);
        dest.writeString(this.flightno);
        dest.writeString(this.cabin);
        dest.writeString(this.policyid);
        dest.writeString(this.platname);
        dest.writeString(this.totalrate);
        dest.writeString(this.userrate);
        dest.writeString(this.bookdata);
        dest.writeString(this.rateinfo);
        dest.writeString(this.remark);
        dest.writeString(this.isspepolicy);
        dest.writeString(this.wtime);
        dest.writeString(this.rftime);
    }

    public PolicyBean() {
    }

    protected PolicyBean(Parcel in) {
        this.fare = in.readInt();
        this.sale = in.readInt();
        this.flightno = in.readString();
        this.cabin = in.readString();
        this.policyid = in.readString();
        this.platname = in.readString();
        this.totalrate = in.readString();
        this.userrate = in.readString();
        this.bookdata = in.readString();
        this.rateinfo = in.readString();
        this.remark = in.readString();
        this.isspepolicy = in.readString();
        this.wtime = in.readString();
        this.rftime = in.readString();
    }

    public static final Parcelable.Creator<PolicyBean> CREATOR = new Parcelable.Creator<PolicyBean>() {
        @Override
        public PolicyBean createFromParcel(Parcel source) {
            return new PolicyBean(source);
        }

        @Override
        public PolicyBean[] newArray(int size) {
            return new PolicyBean[size];
        }
    };
}
