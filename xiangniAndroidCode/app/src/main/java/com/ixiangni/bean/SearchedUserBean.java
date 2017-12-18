package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class SearchedUserBean {

    /**
     * message : 操作成功!
     * data : [{"flag":0,"user":{"userid":10,"usernick":"徐传霆","usercountrycode":null,"usermobile":null,"userpass":"e10adc3949ba59abbe56e057f20f883e","userstatus":null,"usertype":null,"userlogintype":null,"userpic":"http://handongkeji.com:8090/ixiangniupload/head/2017-06-29/QT1498726653905_mid.jpg","userweixinid":null,"userweixintoken":null,"userxinlangid":null,"userxinlangtoken":null,"userqqid":null,"userqqtoken":null,"taobaoid":null,"taobaotoken":null,"userlng":null,"userlat":null,"userregisttime":null,"username":null,"usernumber":"410222222222222222","usersex":0,"userbirthday":null,"usermail":null,"areaid":null,"userisapply":null,"userisvalidate":null,"userdesc":null,"userispush":1,"userapptype":1,"usercurrentlogintime":1498717144000,"usercurrentloginip":"58.135.80.31","userrecommendedid":null,"isnewfocus":null,"userispopular":null,"userlogincount":10,"browsemomenttime":1498733149000,"commenttime":null,"getsysmessage":0,"remind":0,"applyadd":0,"authentication":0,"friendnum":500,"retention":null,"paymentpass":null,"useraccount":1221,"userisdel":0,"userno":"ixn10","messagetime":null,"addfriendlisttime":null,"charactersing":null,"areainfo":null,"address":null,"branchid":3,"jobid":5,"focusnum":null,"islock":0,"recommender":"谷俊","recommendtel":"88665544","recommendnum":"6666","provinceid":null,"cityid":null,"districtid":null}}]
     * status : 1
     */

    private String message;
    private int status;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * flag : 0
         * user : {"userid":10,"usernick":"徐传霆","usercountrycode":null,"usermobile":null,"userpass":"e10adc3949ba59abbe56e057f20f883e","userstatus":null,"usertype":null,"userlogintype":null,"userpic":"http://handongkeji.com:8090/ixiangniupload/head/2017-06-29/QT1498726653905_mid.jpg","userweixinid":null,"userweixintoken":null,"userxinlangid":null,"userxinlangtoken":null,"userqqid":null,"userqqtoken":null,"taobaoid":null,"taobaotoken":null,"userlng":null,"userlat":null,"userregisttime":null,"username":null,"usernumber":"410222222222222222","usersex":0,"userbirthday":null,"usermail":null,"areaid":null,"userisapply":null,"userisvalidate":null,"userdesc":null,"userispush":1,"userapptype":1,"usercurrentlogintime":1498717144000,"usercurrentloginip":"58.135.80.31","userrecommendedid":null,"isnewfocus":null,"userispopular":null,"userlogincount":10,"browsemomenttime":1498733149000,"commenttime":null,"getsysmessage":0,"remind":0,"applyadd":0,"authentication":0,"friendnum":500,"retention":null,"paymentpass":null,"useraccount":1221,"userisdel":0,"userno":"ixn10","messagetime":null,"addfriendlisttime":null,"charactersing":null,"areainfo":null,"address":null,"branchid":3,"jobid":5,"focusnum":null,"islock":0,"recommender":"谷俊","recommendtel":"88665544","recommendnum":"6666","provinceid":null,"cityid":null,"districtid":null}
         */

        private int flag;
        private UserBean user;

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * userid : 10
             * usernick : 徐传霆
             * usercountrycode : null
             * usermobile : null
             * userpass : e10adc3949ba59abbe56e057f20f883e
             * userstatus : null
             * usertype : null
             * userlogintype : null
             * userpic : http://handongkeji.com:8090/ixiangniupload/head/2017-06-29/QT1498726653905_mid.jpg
             * userweixinid : null
             * userweixintoken : null
             * userxinlangid : null
             * userxinlangtoken : null
             * userqqid : null
             * userqqtoken : null
             * taobaoid : null
             * taobaotoken : null
             * userlng : null
             * userlat : null
             * userregisttime : null
             * username : null
             * usernumber : 410222222222222222
             * usersex : 0
             * userbirthday : null
             * usermail : null
             * areaid : null
             * userisapply : null
             * userisvalidate : null
             * userdesc : null
             * userispush : 1
             * userapptype : 1
             * usercurrentlogintime : 1498717144000
             * usercurrentloginip : 58.135.80.31
             * userrecommendedid : null
             * isnewfocus : null
             * userispopular : null
             * userlogincount : 10
             * browsemomenttime : 1498733149000
             * commenttime : null
             * getsysmessage : 0
             * remind : 0
             * applyadd : 0
             * authentication : 0
             * friendnum : 500
             * retention : null
             * paymentpass : null
             * useraccount : 1221.0
             * userisdel : 0
             * userno : ixn10
             * messagetime : null
             * addfriendlisttime : null
             * charactersing : null
             * areainfo : null
             * address : null
             * branchid : 3
             * jobid : 5
             * focusnum : null
             * islock : 0
             * recommender : 谷俊
             * recommendtel : 88665544
             * recommendnum : 6666
             * provinceid : null
             * cityid : null
             * districtid : null
             */

            private int userid;
            private String usernick;
            private Object usercountrycode;
            private String usermobile;
            private String userpass;
            private Object userstatus;
            private Object usertype;
            private Object userlogintype;
            private String userpic;
            private Object userweixinid;
            private Object userweixintoken;
            private Object userxinlangid;
            private Object userxinlangtoken;
            private Object userqqid;
            private Object userqqtoken;
            private Object taobaoid;
            private Object taobaotoken;
            private Object userlng;
            private Object userlat;
            private Object userregisttime;
            private Object username;
            private String usernumber;
            private int usersex;
            private Object userbirthday;
            private Object usermail;
            private Object areaid;
            private Object userisapply;
            private Object userisvalidate;
            private Object userdesc;
            private int userispush;
            private int userapptype;
            private long usercurrentlogintime;
            private String usercurrentloginip;
            private Object userrecommendedid;
            private Object isnewfocus;
            private Object userispopular;
            private int userlogincount;
            private long browsemomenttime;
            private Object commenttime;
            private int getsysmessage;
            private int remind;
            private int applyadd;
            private int authentication;
            private int friendnum;
            private Object retention;
            private Object paymentpass;
            private double useraccount;
            private int userisdel;
            private String userno;
            private Object messagetime;
            private Object addfriendlisttime;
            private Object charactersing;
            private Object areainfo;
            private Object address;
            private int branchid;
            private int jobid;
            private int focusnum;
            private int islock;
            private String recommender;
            private String recommendtel;
            private String recommendnum;
            private Object provinceid;
            private Object cityid;
            private Object districtid;

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public String getUsernick() {
                return usernick;
            }

            public void setUsernick(String usernick) {
                this.usernick = usernick;
            }

            public Object getUsercountrycode() {
                return usercountrycode;
            }

            public void setUsercountrycode(Object usercountrycode) {
                this.usercountrycode = usercountrycode;
            }

            public String getUsermobile() {
                return usermobile;
            }

            public void setUsermobile(String usermobile) {
                this.usermobile = usermobile;
            }

            public String getUserpass() {
                return userpass;
            }

            public void setUserpass(String userpass) {
                this.userpass = userpass;
            }

            public Object getUserstatus() {
                return userstatus;
            }

            public void setUserstatus(Object userstatus) {
                this.userstatus = userstatus;
            }

            public Object getUsertype() {
                return usertype;
            }

            public void setUsertype(Object usertype) {
                this.usertype = usertype;
            }

            public Object getUserlogintype() {
                return userlogintype;
            }

            public void setUserlogintype(Object userlogintype) {
                this.userlogintype = userlogintype;
            }

            public String getUserpic() {
                return userpic;
            }

            public void setUserpic(String userpic) {
                this.userpic = userpic;
            }

            public Object getUserweixinid() {
                return userweixinid;
            }

            public void setUserweixinid(Object userweixinid) {
                this.userweixinid = userweixinid;
            }

            public Object getUserweixintoken() {
                return userweixintoken;
            }

            public void setUserweixintoken(Object userweixintoken) {
                this.userweixintoken = userweixintoken;
            }

            public Object getUserxinlangid() {
                return userxinlangid;
            }

            public void setUserxinlangid(Object userxinlangid) {
                this.userxinlangid = userxinlangid;
            }

            public Object getUserxinlangtoken() {
                return userxinlangtoken;
            }

            public void setUserxinlangtoken(Object userxinlangtoken) {
                this.userxinlangtoken = userxinlangtoken;
            }

            public Object getUserqqid() {
                return userqqid;
            }

            public void setUserqqid(Object userqqid) {
                this.userqqid = userqqid;
            }

            public Object getUserqqtoken() {
                return userqqtoken;
            }

            public void setUserqqtoken(Object userqqtoken) {
                this.userqqtoken = userqqtoken;
            }

            public Object getTaobaoid() {
                return taobaoid;
            }

            public void setTaobaoid(Object taobaoid) {
                this.taobaoid = taobaoid;
            }

            public Object getTaobaotoken() {
                return taobaotoken;
            }

            public void setTaobaotoken(Object taobaotoken) {
                this.taobaotoken = taobaotoken;
            }

            public Object getUserlng() {
                return userlng;
            }

            public void setUserlng(Object userlng) {
                this.userlng = userlng;
            }

            public Object getUserlat() {
                return userlat;
            }

            public void setUserlat(Object userlat) {
                this.userlat = userlat;
            }

            public Object getUserregisttime() {
                return userregisttime;
            }

            public void setUserregisttime(Object userregisttime) {
                this.userregisttime = userregisttime;
            }

            public Object getUsername() {
                return username;
            }

            public void setUsername(Object username) {
                this.username = username;
            }

            public String getUsernumber() {
                return usernumber;
            }

            public void setUsernumber(String usernumber) {
                this.usernumber = usernumber;
            }

            public int getUsersex() {
                return usersex;
            }

            public void setUsersex(int usersex) {
                this.usersex = usersex;
            }

            public Object getUserbirthday() {
                return userbirthday;
            }

            public void setUserbirthday(Object userbirthday) {
                this.userbirthday = userbirthday;
            }

            public Object getUsermail() {
                return usermail;
            }

            public void setUsermail(Object usermail) {
                this.usermail = usermail;
            }

            public Object getAreaid() {
                return areaid;
            }

            public void setAreaid(Object areaid) {
                this.areaid = areaid;
            }

            public Object getUserisapply() {
                return userisapply;
            }

            public void setUserisapply(Object userisapply) {
                this.userisapply = userisapply;
            }

            public Object getUserisvalidate() {
                return userisvalidate;
            }

            public void setUserisvalidate(Object userisvalidate) {
                this.userisvalidate = userisvalidate;
            }

            public Object getUserdesc() {
                return userdesc;
            }

            public void setUserdesc(Object userdesc) {
                this.userdesc = userdesc;
            }

            public int getUserispush() {
                return userispush;
            }

            public void setUserispush(int userispush) {
                this.userispush = userispush;
            }

            public int getUserapptype() {
                return userapptype;
            }

            public void setUserapptype(int userapptype) {
                this.userapptype = userapptype;
            }

            public long getUsercurrentlogintime() {
                return usercurrentlogintime;
            }

            public void setUsercurrentlogintime(long usercurrentlogintime) {
                this.usercurrentlogintime = usercurrentlogintime;
            }

            public String getUsercurrentloginip() {
                return usercurrentloginip;
            }

            public void setUsercurrentloginip(String usercurrentloginip) {
                this.usercurrentloginip = usercurrentloginip;
            }

            public Object getUserrecommendedid() {
                return userrecommendedid;
            }

            public void setUserrecommendedid(Object userrecommendedid) {
                this.userrecommendedid = userrecommendedid;
            }

            public Object getIsnewfocus() {
                return isnewfocus;
            }

            public void setIsnewfocus(Object isnewfocus) {
                this.isnewfocus = isnewfocus;
            }

            public Object getUserispopular() {
                return userispopular;
            }

            public void setUserispopular(Object userispopular) {
                this.userispopular = userispopular;
            }

            public int getUserlogincount() {
                return userlogincount;
            }

            public void setUserlogincount(int userlogincount) {
                this.userlogincount = userlogincount;
            }

            public long getBrowsemomenttime() {
                return browsemomenttime;
            }

            public void setBrowsemomenttime(long browsemomenttime) {
                this.browsemomenttime = browsemomenttime;
            }

            public Object getCommenttime() {
                return commenttime;
            }

            public void setCommenttime(Object commenttime) {
                this.commenttime = commenttime;
            }

            public int getGetsysmessage() {
                return getsysmessage;
            }

            public void setGetsysmessage(int getsysmessage) {
                this.getsysmessage = getsysmessage;
            }

            public int getRemind() {
                return remind;
            }

            public void setRemind(int remind) {
                this.remind = remind;
            }

            public int getApplyadd() {
                return applyadd;
            }

            public void setApplyadd(int applyadd) {
                this.applyadd = applyadd;
            }

            public int getAuthentication() {
                return authentication;
            }

            public void setAuthentication(int authentication) {
                this.authentication = authentication;
            }

            public int getFriendnum() {
                return friendnum;
            }

            public void setFriendnum(int friendnum) {
                this.friendnum = friendnum;
            }

            public Object getRetention() {
                return retention;
            }

            public void setRetention(Object retention) {
                this.retention = retention;
            }

            public Object getPaymentpass() {
                return paymentpass;
            }

            public void setPaymentpass(Object paymentpass) {
                this.paymentpass = paymentpass;
            }

            public double getUseraccount() {
                return useraccount;
            }

            public void setUseraccount(double useraccount) {
                this.useraccount = useraccount;
            }

            public int getUserisdel() {
                return userisdel;
            }

            public void setUserisdel(int userisdel) {
                this.userisdel = userisdel;
            }

            public String getUserno() {
                return userno;
            }

            public void setUserno(String userno) {
                this.userno = userno;
            }

            public Object getMessagetime() {
                return messagetime;
            }

            public void setMessagetime(Object messagetime) {
                this.messagetime = messagetime;
            }

            public Object getAddfriendlisttime() {
                return addfriendlisttime;
            }

            public void setAddfriendlisttime(Object addfriendlisttime) {
                this.addfriendlisttime = addfriendlisttime;
            }

            public Object getCharactersing() {
                return charactersing;
            }

            public void setCharactersing(Object charactersing) {
                this.charactersing = charactersing;
            }

            public Object getAreainfo() {
                return areainfo;
            }

            public void setAreainfo(Object areainfo) {
                this.areainfo = areainfo;
            }

            public Object getAddress() {
                return address;
            }

            public void setAddress(Object address) {
                this.address = address;
            }

            public int getBranchid() {
                return branchid;
            }

            public void setBranchid(int branchid) {
                this.branchid = branchid;
            }

            public int getJobid() {
                return jobid;
            }

            public void setJobid(int jobid) {
                this.jobid = jobid;
            }

            public int getFocusnum() {
                return focusnum;
            }

            public void setFocusnum(int focusnum) {
                this.focusnum = focusnum;
            }

            public int getIslock() {
                return islock;
            }

            public void setIslock(int islock) {
                this.islock = islock;
            }

            public String getRecommender() {
                return recommender;
            }

            public void setRecommender(String recommender) {
                this.recommender = recommender;
            }

            public String getRecommendtel() {
                return recommendtel;
            }

            public void setRecommendtel(String recommendtel) {
                this.recommendtel = recommendtel;
            }

            public String getRecommendnum() {
                return recommendnum;
            }

            public void setRecommendnum(String recommendnum) {
                this.recommendnum = recommendnum;
            }

            public Object getProvinceid() {
                return provinceid;
            }

            public void setProvinceid(Object provinceid) {
                this.provinceid = provinceid;
            }

            public Object getCityid() {
                return cityid;
            }

            public void setCityid(Object cityid) {
                this.cityid = cityid;
            }

            public Object getDistrictid() {
                return districtid;
            }

            public void setDistrictid(Object districtid) {
                this.districtid = districtid;
            }
        }
    }
}
