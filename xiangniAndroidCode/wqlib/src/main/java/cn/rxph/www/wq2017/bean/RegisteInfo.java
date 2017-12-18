package cn.rxph.www.wq2017.bean;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/31.
 */

public class RegisteInfo implements Serializable {

    private String qianyuerenId;   //签约人Id
    private String qianyuerentoken;   //签约人token
    private String userName;
    private String userSfz;
    private String zllm;
    private String zllmId;
    private String userAddress;
    private String userTel;

    private String tjrSfz;
    private String tjrName;
    private String tjrTel;

    private String contactName;
    private String contactTel;
    private String contactShgx;

    private String contactName1;
    private String contactTel1;
    private String contactShgx1;
    private File sfzzm;
    private File sfzfm;
    private File scsfz;
    private File hy;
    private File qm;
    private String agreementTime;
    //0  第一次  1重新提交的
    private int again;
    private String  apply_id="-1";								//会员申请主键ID

    public void setQianyuerenId(String qianyuerenId) {
        this.qianyuerenId = qianyuerenId;
    }

    public String getQianyuerenId() {
        return qianyuerenId;
    }

    public String getQianyuerentoken() {
        return qianyuerentoken;
    }

    public void setQianyuerentoken(String qianyuerentoken) {
        this.qianyuerentoken = qianyuerentoken;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }

    public String getApply_id() {
        return apply_id;
    }


    public void setAgain(int again) {
        this.again = again;
    }

    public int getAgain() {
        return again;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserSfz(String userSfz) {
        this.userSfz = userSfz;
    }

    public void setZllm(String zllm) {
        this.zllm = zllm;
    }

    public String getZllmId() {
        return zllmId;
    }

    public void setZllmId(String zllmId) {
        this.zllmId = zllmId;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setTjrSfz(String tjrSfz) {
        this.tjrSfz = tjrSfz;
    }

    public void setTjrName(String tjrName) {
        this.tjrName = tjrName;
    }

    public void setTjrTel(String tjrTel) {
        this.tjrTel = tjrTel;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public void setContactShgx(String contactShgx) {
        this.contactShgx = contactShgx;
    }

    public void setContactName1(String contactName1) {
        this.contactName1 = contactName1;
    }

    public void setContactTel1(String contactTel1) {
        this.contactTel1 = contactTel1;
    }

    public void setContactShgx1(String contactShgx1) {
        this.contactShgx1 = contactShgx1;
    }

    public void setSfzzm(File sfzzm) {
        this.sfzzm = sfzzm;
    }

    public void setSfzfm(File sfzfm) {
        this.sfzfm = sfzfm;
    }

    public void setScsfz(File scsfz) {
        this.scsfz = scsfz;
    }

    public void setHy(File hy) {
        this.hy = hy;
    }

    public void setQm(File qm) {
        this.qm = qm;
    }

    public void setAgreementTime(String agreementTime) {
        this.agreementTime = agreementTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserSfz() {
        return userSfz;
    }

    public String getZllm() {
        return zllm;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getTjrSfz() {
        return tjrSfz;
    }

    public String getTjrName() {
        return tjrName;
    }

    public String getTjrTel() {
        return tjrTel;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public String getContactShgx() {
        return contactShgx;
    }

    public String getContactName1() {
        return contactName1;
    }

    public String getContactTel1() {
        return contactTel1;
    }

    public String getContactShgx1() {
        return contactShgx1;
    }

    public File getSfzzm() {
        return sfzzm;
    }

    public File getSfzfm() {
        return sfzfm;
    }

    public File getScsfz() {
        return scsfz;
    }

    public File getHy() {
        return hy;
    }

    public File getQm() {
        return qm;
    }

    public String getAgreementTime() {
        return agreementTime;
    }
}
