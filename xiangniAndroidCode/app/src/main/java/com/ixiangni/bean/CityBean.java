package com.ixiangni.bean;

import com.ixiangni.util.PinyinUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/24 0024.
 */

public class CityBean implements Serializable{

    /**
     * flycodeid : 1
     * cityname : 北京首都
     * citycode : PEK
     * elsecode : null
     * province : null
     * country : 中国
     */

    private int flycodeid;
    private String cityname;
    private String citycode;
    private Object elsecode;
    private Object province;
    private String country;


    public String getFirstLetter(){
        return PinyinUtil.getFrist(cityname).toUpperCase();
    }
    public int getFlycodeid() {
        return flycodeid;
    }

    public void setFlycodeid(int flycodeid) {
        this.flycodeid = flycodeid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public Object getElsecode() {
        return elsecode;
    }

    public void setElsecode(Object elsecode) {
        this.elsecode = elsecode;
    }

    public Object getProvince() {
        return province;
    }

    public void setProvince(Object province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
