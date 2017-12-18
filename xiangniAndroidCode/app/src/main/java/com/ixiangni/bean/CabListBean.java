package com.ixiangni.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/8/5 0005.
 */

public  class CabListBean implements Parcelable {
    /**
     * AirLineCode : CZ
     * Cabin : W
     * CabinName : 经济舱
     * Discount : 100
     * IsTeHui : 0
     * Fare : 1690
     * IsSpe : 1
     * IsSpePolicy : 0
     * Sale : 1690
     * TicketCount : 10
     * TuiGaiQian :
     以航空公司审核为准
     * UserRate : 0
     * BabySalePrice : 170
     * VTWorteTime :
     * WorkTime :
     * YouHui : 0
     * Bookpara : FAE728EEE207A887231B7EF27BFA02E96B15465997FD1A198DB0D562DE91DC3D006A7E5EF69263E579CC9665EB3DA34064C48C5C393FF858C8AF2E97443FBB49DB732779C723DA15116F10268EF737E6809A892CB51F18FF278990D126AB9D2FC3900C103F1853CB5FE6A2D17470A1E16BCD29DB09476FC39CAD58EAE674C75A1476D2DE6AC77C416D45E78782746735E39E1565FDBBD8EF47640657B9AD986B5460EA42678B167FA1296F87E337F6AEAAD43E3874A9917BC916713E928B09329D6B100A661B777387E916BE8470B7E502012B63608090B9CFA08209AE35C4CC5C994B853DE1C6D2600E47CFE5D26D35B6ECACFC8AF9C289C37E0C13A71A66C9BCD7DC76E73A4A90AEF888D296BFE49B6E70711222F0C584B5BFA754718F8E2906D55451A47C84F63BC61D01FD793D24EA6BC06A74B25071867993584C0B5E104101CE2D46152FB1FF2D6F9BD545BF5B25BDD8AFF40F593965671DBD6E3D65EC1E143C11ECDB083CC46B0E9A02DD63F33B71868146647DDE6D5955CA62FC471B03F2EE4BB7951046FAADBDB48F9887617175B2629DB69E55F25B0C748E25CD54E4F554CEB1242054AE06362A469516E68BC11F3493BEF8E80491073A00FA490764A9B37629A0ABF1
     */

    private String AirLineCode;
    private String Cabin;
    private String CabinName;
    private String Discount;
    private int IsTeHui;
    private String Fare;
    private String IsSpe;
    private String IsSpePolicy;
    private String Sale;
    private String TicketCount;
    private String TuiGaiQian;
    private String UserRate;
    private String BabySalePrice;
    private String VTWorteTime;
    private String WorkTime;
    private String YouHui;
    private String Bookpara;

    public String getAirLineCode() {
        return AirLineCode;
    }

    public void setAirLineCode(String AirLineCode) {
        this.AirLineCode = AirLineCode;
    }

    public String getCabin() {
        return Cabin;
    }

    public void setCabin(String Cabin) {
        this.Cabin = Cabin;
    }

    public String getCabinName() {
        return CabinName;
    }

    public void setCabinName(String CabinName) {
        this.CabinName = CabinName;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String Discount) {
        this.Discount = Discount;
    }

    public int getIsTeHui() {
        return IsTeHui;
    }

    public void setIsTeHui(int IsTeHui) {
        this.IsTeHui = IsTeHui;
    }

    public String getFare() {
        return Fare;
    }

    public void setFare(String Fare) {
        this.Fare = Fare;
    }

    public String getIsSpe() {
        return IsSpe;
    }

    public void setIsSpe(String IsSpe) {
        this.IsSpe = IsSpe;
    }

    public String getIsSpePolicy() {
        return IsSpePolicy;
    }

    public void setIsSpePolicy(String IsSpePolicy) {
        this.IsSpePolicy = IsSpePolicy;
    }

    public String getSale() {
        return Sale;
    }

    public void setSale(String Sale) {
        this.Sale = Sale;
    }

    public String getTicketCount() {
        return TicketCount;
    }

    public void setTicketCount(String TicketCount) {
        this.TicketCount = TicketCount;
    }

    public String getTuiGaiQian() {
        return TuiGaiQian;
    }

    public void setTuiGaiQian(String TuiGaiQian) {
        this.TuiGaiQian = TuiGaiQian;
    }

    public String getUserRate() {
        return UserRate;
    }

    public void setUserRate(String UserRate) {
        this.UserRate = UserRate;
    }

    public String getBabySalePrice() {
        return BabySalePrice;
    }

    public void setBabySalePrice(String BabySalePrice) {
        this.BabySalePrice = BabySalePrice;
    }

    public String getVTWorteTime() {
        return VTWorteTime;
    }

    public void setVTWorteTime(String VTWorteTime) {
        this.VTWorteTime = VTWorteTime;
    }

    public String getWorkTime() {
        return WorkTime;
    }

    public void setWorkTime(String WorkTime) {
        this.WorkTime = WorkTime;
    }

    public String getYouHui() {
        return YouHui;
    }

    public void setYouHui(String YouHui) {
        this.YouHui = YouHui;
    }

    public String getBookpara() {
        return Bookpara;
    }

    public void setBookpara(String Bookpara) {
        this.Bookpara = Bookpara;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.AirLineCode);
        dest.writeString(this.Cabin);
        dest.writeString(this.CabinName);
        dest.writeString(this.Discount);
        dest.writeInt(this.IsTeHui);
        dest.writeString(this.Fare);
        dest.writeString(this.IsSpe);
        dest.writeString(this.IsSpePolicy);
        dest.writeString(this.Sale);
        dest.writeString(this.TicketCount);
        dest.writeString(this.TuiGaiQian);
        dest.writeString(this.UserRate);
        dest.writeString(this.BabySalePrice);
        dest.writeString(this.VTWorteTime);
        dest.writeString(this.WorkTime);
        dest.writeString(this.YouHui);
        dest.writeString(this.Bookpara);
    }

    public CabListBean() {
    }

    protected CabListBean(Parcel in) {
        this.AirLineCode = in.readString();
        this.Cabin = in.readString();
        this.CabinName = in.readString();
        this.Discount = in.readString();
        this.IsTeHui = in.readInt();
        this.Fare = in.readString();
        this.IsSpe = in.readString();
        this.IsSpePolicy = in.readString();
        this.Sale = in.readString();
        this.TicketCount = in.readString();
        this.TuiGaiQian = in.readString();
        this.UserRate = in.readString();
        this.BabySalePrice = in.readString();
        this.VTWorteTime = in.readString();
        this.WorkTime = in.readString();
        this.YouHui = in.readString();
        this.Bookpara = in.readString();
    }

    public static final Parcelable.Creator<CabListBean> CREATOR = new Parcelable.Creator<CabListBean>() {
        @Override
        public CabListBean createFromParcel(Parcel source) {
            return new CabListBean(source);
        }

        @Override
        public CabListBean[] newArray(int size) {
            return new CabListBean[size];
        }
    };
}
