package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class PersonInfoBean {

    /**
     * message : 操作成功!
     * data : {"userid":5,"usernick":"阿尔萨斯","usercountrycode":"+86","usermobile":"13220120480","userpass":"e10adc3949ba59abbe56e057f20f883e","userstatus":0,"usertype":0,"userlogintype":0,"userpic":"http://handongkeji.com:8090/ixiangniupload/head/2017-06-28/QF1498630479294_mid.jpg","userweixinid":null,"userweixintoken":null,"userxinlangid":null,"userxinlangtoken":null,"userqqid":null,"userqqtoken":null,"taobaoid":null,"taobaotoken":null,"userlng":null,"userlat":null,"userregisttime":null,"username":null,"usernumber":"88888888","usersex":0,"userbirthday":null,"usermail":null,"areaid":null,"userisapply":null,"userisvalidate":null,"userdesc":null,"userispush":1,"userapptype":2,"usercurrentlogintime":1499926586000,"usercurrentloginip":"58.135.80.31","userrecommendedid":null,"isnewfocus":null,"userispopular":null,"userlogincount":41,"browsemomenttime":null,"commenttime":null,"getsysmessage":0,"remind":0,"applyadd":0,"authentication":1,"friendnum":500,"retention":null,"paymentpass":"e10adc3949ba59abbe56e057f20f883e","useraccount":866,"userisdel":0,"userno":"ixn5","messagetime":null,"addfriendlisttime":null,"charactersing":null,"areainfo":null,"address":null,"branchid":null,"jobid":null,"focusnum":0,"islock":0,"recommender":null,"recommendtel":null,"recommendnum":null,"provinceid":null,"cityid":null,"districtid":null,"branchname":null,"jobname":null,"juli":null,"newsVoList":[],"isfollow":0,"isfriend":1}
     * status : 1
     */

    private String message;
    private PersonBean data;
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PersonBean getData() {
        return data;
    }

    public void setData(PersonBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
