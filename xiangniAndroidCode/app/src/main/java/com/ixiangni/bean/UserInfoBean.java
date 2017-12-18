package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class UserInfoBean {

    /**
     * message : 操作成功!
     * data : {"userid":2,"usernick":"任勇","usercountrycode":"+86","usermobile":"13813881388","userpass":"e10adc3949ba59abbe56e057f20f883e","userstatus":0,"usertype":0,"userlogintype":0,"userpic":null,"userweixinid":null,"userweixintoken":null,"userxinlangid":null,"userxinlangtoken":null,"userqqid":null,"userqqtoken":null,"taobaoid":null,"taobaotoken":null,"userlng":null,"userlat":null,"userregisttime":null,"username":null,"usernumber":"410222222222222222","usersex":0,"userbirthday":null,"usermail":null,"areaid":null,"userisapply":null,"userisvalidate":null,"userdesc":"null  null","userispush":1,"userapptype":1,"usercurrentlogintime":1498184341000,"usercurrentloginip":"58.135.80.31","userrecommendedid":null,"isnewfocus":1,"userispopular":null,"userlogincount":12,"browsemomenttime":null,"commenttime":null,"getsysmessage":null,"remind":null,"applyadd":null,"authentication":null,"friendnum":null,"retention":null,"paymentpass":null,"useraccount":0,"userisdel":0,"userno":null,"messagetime":1498014161000,"addfriendlisttime":null,"charactersing":null,"areainfo":null,"address":null,"branchid":null,"jobid":null,"focusnum":1}
     * status : 1
     */

    private String message;
    private DataBean data;
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * userid : 2
         * usernick : 任勇
         * usercountrycode : +86
         * usermobile : 13813881388
         * userpass : e10adc3949ba59abbe56e057f20f883e
         * userstatus : 0
         * usertype : 0
         * userlogintype : 0
         * userpic : null
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
         * userdesc : null  null
         * userispush : 1
         * userapptype : 1
         * usercurrentlogintime : 1498184341000
         * usercurrentloginip : 58.135.80.31
         * userrecommendedid : null
         * isnewfocus : 1
         * userispopular : null
         * userlogincount : 12
         * browsemomenttime : null
         * commenttime : null
         * getsysmessage : null
         * remind : null
         * applyadd : null
         * authentication : null
         * friendnum : null
         * retention : null
         * paymentpass : null
         * useraccount : 0.0
         * userisdel : 0
         * userno : null
         * messagetime : 1498014161000
         * addfriendlisttime : null
         * charactersing : null
         * areainfo : null
         * address : null
         * branchid : null
         * jobid : null
         * focusnum : 1
         */

        private String userid;
        private String usernick;
        private String usercountrycode;
        private String usermobile;
        private String userpass;
        private int userstatus;
        private int usertype;
        private int userlogintype;
        private String userpic;
        private String userweixinid;
        private Object userweixintoken;
        private Object userxinlangid;
        private Object userxinlangtoken;
        private Object userqqid;
        private Object userqqtoken;
        private String taobaoid;
        private String taobaotoken;
        private Object userlng;
        private Object userlat;
        private Object userregisttime;
        private String username;
        private String usernumber;
        private int usersex;
        private Object userbirthday;
        private Object usermail;
        private Object areaid;
        private Object userisapply;
        private Object userisvalidate;
        private String userdesc;
        private int userispush;
        private int userapptype;
        private long usercurrentlogintime;
        private String usercurrentloginip;
        private Object userrecommendedid;
        private int isnewfocus;
        private Object userispopular;
        private int userlogincount;
        private Object browsemomenttime;
        private long commenttime;
        private int getsysmessage;
        private int remind;
        private int applyadd;
        private int authentication;
        private Object friendnum;
        private Object retention;
        private String paymentpass;
        private double useraccount;
        private int userisdel;
        private String userno;
        private long messagetime;
        private long addfriendlisttime;
        private String charactersing;
        private String areainfo;
        private String address;
        private String branchid;
        private String jobid;
        private int focusnum;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsernick() {
            return usernick;
        }

        public void setUsernick(String usernick) {
            this.usernick = usernick;
        }

        public String getUsercountrycode() {
            return usercountrycode;
        }

        public void setUsercountrycode(String usercountrycode) {
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

        public int getUserstatus() {
            return userstatus;
        }

        public void setUserstatus(int userstatus) {
            this.userstatus = userstatus;
        }

        public int getUsertype() {
            return usertype;
        }

        public void setUsertype(int usertype) {
            this.usertype = usertype;
        }

        public int getUserlogintype() {
            return userlogintype;
        }

        public void setUserlogintype(int userlogintype) {
            this.userlogintype = userlogintype;
        }

        public String getUserpic() {
            return userpic;
        }

        public void setUserpic(String userpic) {
            this.userpic = userpic;
        }

        public String getUserweixinid() {
            return userweixinid;
        }

        public void setUserweixinid(String userweixinid) {
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

        public String getTaobaoid() {
            return taobaoid;
        }

        public void setTaobaoid(String taobaoid) {
            this.taobaoid = taobaoid;
        }

        public String getTaobaotoken() {
            return taobaotoken;
        }

        public void setTaobaotoken(String taobaotoken) {
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
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

        public String getUserdesc() {
            return userdesc;
        }

        public void setUserdesc(String userdesc) {
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

        public int getIsnewfocus() {
            return isnewfocus;
        }

        public void setIsnewfocus(int isnewfocus) {
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

        public Object getBrowsemomenttime() {
            return browsemomenttime;
        }

        public void setBrowsemomenttime(Object browsemomenttime) {
            this.browsemomenttime = browsemomenttime;
        }

        public long getCommenttime() {
            return commenttime;
        }

        public void setCommenttime(long commenttime) {
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

        public Object getFriendnum() {
            return friendnum;
        }

        public void setFriendnum(Object friendnum) {
            this.friendnum = friendnum;
        }

        public Object getRetention() {
            return retention;
        }

        public void setRetention(Object retention) {
            this.retention = retention;
        }

        public String getPaymentpass() {
            return paymentpass;
        }

        public void setPaymentpass(String paymentpass) {
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

        public long getMessagetime() {
            return messagetime;
        }

        public void setMessagetime(long messagetime) {
            this.messagetime = messagetime;
        }

        public long getAddfriendlisttime() {
            return addfriendlisttime;
        }

        public void setAddfriendlisttime(long addfriendlisttime) {
            this.addfriendlisttime = addfriendlisttime;
        }

        public String getCharactersing() {
            return charactersing;
        }

        public void setCharactersing(String charactersing) {
            this.charactersing = charactersing;
        }

        public String getAreainfo() {
            return areainfo;
        }

        public void setAreainfo(String areainfo) {
            this.areainfo = areainfo;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBranchid() {
            return branchid;
        }

        public void setBranchid(String branchid) {
            this.branchid = branchid;
        }

        public String getJobid() {
            return jobid;
        }

        public void setJobid(String jobid) {
            this.jobid = jobid;
        }

        public int getFocusnum() {
            return focusnum;
        }

        public void setFocusnum(int focusnum) {
            this.focusnum = focusnum;
        }
    }
}
