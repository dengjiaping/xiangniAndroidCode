package com.ixiangni.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/27 0027.
 */

public class RoomInfo implements Serializable {

//    roomtype	是	String	房型类型：0现付;1预付(房型详情中的roomtype)



    private String roomPic;
    private String roomType;
    private String hotelid;
    private String roomid;
    private String plainid;
    private String roomSize;
    private String suppid;
    private String hotelName;

    private String tm1;
    private String tm2;
    private String amount;
    private String contacts;
    private String phone;
    private String idno;
    private String paypassword;
    private String title;
    private String bed;
    private String area;
    private String orderpic;

    public String getOrderpic() {
        return orderpic;
    }

    public void setOrderpic(String orderpic) {
        this.orderpic = orderpic;
    }

    public String getTm1() {
        return tm1;
    }

    public void setTm1(String tm1) {
        this.tm1 = tm1;
    }

    public String getTm2() {
        return tm2;
    }

    public void setTm2(String tm2) {
        this.tm2 = tm2;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getPaypassword() {
        return paypassword;
    }

    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelid() {
        return hotelid;
    }

    public void setHotelid(String hotelid) {
        this.hotelid = hotelid;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getPlainid() {
        return plainid;
    }

    public void setPlainid(String plainid) {
        this.plainid = plainid;
    }



    public String getRoomPic() {
        return roomPic;
    }

    public void setRoomPic(String roomPic) {
        this.roomPic = roomPic;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }

    public String getSuppid() {
        return suppid;
    }

    public void setSuppid(String suppid) {
        this.suppid = suppid;
    }
}
