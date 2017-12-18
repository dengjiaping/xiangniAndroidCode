package com.ixiangni.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/8/5 0005.
 */

public class DateParams implements Parcelable {
    private String star;    //    star	是	String	出发城市三字码


    private String end;    //    end	是	String	到达城市三字码


    private String date;

    private String startCity;
    private String endCity;

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.star);
        dest.writeString(this.end);
        dest.writeString(this.date);
        dest.writeString(this.startCity);
        dest.writeString(this.endCity);
    }

    public DateParams() {
    }

    protected DateParams(Parcel in) {
        this.star = in.readString();
        this.end = in.readString();
        this.date = in.readString();
        this.startCity = in.readString();
        this.endCity = in.readString();
    }

    public static final Parcelable.Creator<DateParams> CREATOR = new Parcelable.Creator<DateParams>() {
        @Override
        public DateParams createFromParcel(Parcel source) {
            return new DateParams(source);
        }

        @Override
        public DateParams[] newArray(int size) {
            return new DateParams[size];
        }
    };
}
