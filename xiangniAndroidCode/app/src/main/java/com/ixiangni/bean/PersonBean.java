package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public  class PersonBean {

    /**
     * message : 操作成功!
     * data : {"userid":6,"usernick":"伊利丹","usercountrycode":null,"usermobile":null,"userpass":"e10adc3949ba59abbe56e057f20f883e","userstatus":null,"usertype":null,"userlogintype":null,"userpic":"http://handongkeji.com:8090/ixiangniupload/head/2017-06-28/HM1498630691562_mid.jpg","userweixinid":null,"userweixintoken":null,"userxinlangid":null,"userxinlangtoken":null,"userqqid":null,"userqqtoken":null,"taobaoid":null,"taobaotoken":null,"userlng":null,"userlat":null,"userregisttime":null,"username":null,"usernumber":"999999999","usersex":1,"userbirthday":null,"usermail":null,"areaid":null,"userisapply":null,"userisvalidate":null,"userdesc":null,"userispush":1,"userapptype":2,"usercurrentlogintime":1499834222000,"usercurrentloginip":"58.135.80.31","userrecommendedid":null,"isnewfocus":null,"userispopular":null,"userlogincount":48,"browsemomenttime":null,"commenttime":null,"getsysmessage":0,"remind":0,"applyadd":0,"authentication":0,"friendnum":500,"retention":null,"paymentpass":null,"useraccount":2,"userisdel":0,"userno":"ixn6","messagetime":null,"addfriendlisttime":null,"charactersing":null,"areainfo":null,"address":null,"branchid":null,"jobid":null,"focusnum":null,"islock":0,"recommender":"","recommendtel":"","recommendnum":"","provinceid":null,"cityid":null,"districtid":null,"branchname":null,"jobname":null,"juli":null,"newsVoList":[],"isfollow":0,"isfriend":1,"seehemoment":0,"seememoment":0}
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
         * userid : 6
         * usernick : 伊利丹
         * usercountrycode : null
         * usermobile : null
         * userpass : e10adc3949ba59abbe56e057f20f883e
         * userstatus : null
         * usertype : null
         * userlogintype : null
         * userpic : http://handongkeji.com:8090/ixiangniupload/head/2017-06-28/HM1498630691562_mid.jpg
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
         * usernumber : 999999999
         * usersex : 1
         * userbirthday : null
         * usermail : null
         * areaid : null
         * userisapply : null
         * userisvalidate : null
         * userdesc : null
         * userispush : 1
         * userapptype : 2
         * usercurrentlogintime : 1499834222000
         * usercurrentloginip : 58.135.80.31
         * userrecommendedid : null
         * isnewfocus : null
         * userispopular : null
         * userlogincount : 48
         * browsemomenttime : null
         * commenttime : null
         * getsysmessage : 0
         * remind : 0
         * applyadd : 0
         * authentication : 0
         * friendnum : 500
         * retention : null
         * paymentpass : null
         * useraccount : 2.0
         * userisdel : 0
         * userno : ixn6
         * messagetime : null
         * addfriendlisttime : null
         * charactersing : null
         * areainfo : null
         * address : null
         * branchid : null
         * jobid : null
         * focusnum : null
         * islock : 0
         * recommender :
         * recommendtel :
         * recommendnum :
         * provinceid : null
         * cityid : null
         * districtid : null
         * branchname : null
         * jobname : null
         * juli : null
         * newsVoList : []
         * isfollow : 0
         * isfriend : 1
         * seehemoment : 0
         * seememoment : 0
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
        private Object browsemomenttime;
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
        private String charactersing;
        private String areainfo;
        private String address;
        private Object branchid;
        private Object jobid;
        private int focusnum;



        private int islock;
        private String recommender;
        private String recommendtel;
        private String recommendnum;
        private Object provinceid;
        private Object cityid;
        private Object districtid;
        private String branchname;
        private Object jobname;
        private Object juli;
        private String remindname;
        private int isfollow;
        private int isfriend;
        private int seehemoment;
        private int seememoment;
        private List<NewsBean> newsVoList;

        public String getRemindname() {
            return remindname;
        }

        public void setRemindname(String remindname) {
            this.remindname = remindname;
        }

        public static class NewsBean{


            /**
             * momentnewsid : 259
             * userid : 15
             * momentnewscontent : null
             * inserttype : 1
             * videopic : null
             * inserturl : 341
             * medianame : null
             * mediaid : null
             * mediatime : null
             * momentlng : null
             * momentlat : null
             * momentnewsaddress : null
             * releasetime : 1500016115000
             * likenum : 0
             * commentnum : null
             * forwardnum : null
             * showtype : 0
             * del : 0
             * picList : [{"insertid":341,"momentnewsid":259,"inserturl":"http://handongkeji.com:8090/ixiangniupload/head/2017-07-14/RA1500016109614_mid.jpg","inserttime":1500016111000,"picLabelList":[]}]
             * commentList : []
             * likeList : null
             * userinfo : {"usernick":"斯蒂芬","userpic":"http://handongkeji.com:8090/ixiangniupload/head/2017-07-06/LL1499321822290_mid.jpg","cityname":null,"provincename":null,"usersex":1,"luckmoneyid":null,"message":null}
             * readstatus : null
             * islike : null
             */

            private int momentnewsid;
            private int userid;
            private Object momentnewscontent;
            private int inserttype;
            private Object videopic;
            private String inserturl;
            private Object medianame;
            private Object mediaid;
            private Object mediatime;
            private Object momentlng;
            private Object momentlat;
            private Object momentnewsaddress;
            private long releasetime;
            private int likenum;
            private Object commentnum;
            private Object forwardnum;
            private int showtype;
            private int del;
            private Object likeList;
            private UserinfoBean userinfo;
            private Object readstatus;
            private Object islike;
            private List<PicListBean> picList;
            private List<?> commentList;

            public int getMomentnewsid() {
                return momentnewsid;
            }

            public void setMomentnewsid(int momentnewsid) {
                this.momentnewsid = momentnewsid;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public Object getMomentnewscontent() {
                return momentnewscontent;
            }

            public void setMomentnewscontent(Object momentnewscontent) {
                this.momentnewscontent = momentnewscontent;
            }

            public int getInserttype() {
                return inserttype;
            }

            public void setInserttype(int inserttype) {
                this.inserttype = inserttype;
            }

            public Object getVideopic() {
                return videopic;
            }

            public void setVideopic(Object videopic) {
                this.videopic = videopic;
            }

            public String getInserturl() {
                return inserturl;
            }

            public void setInserturl(String inserturl) {
                this.inserturl = inserturl;
            }

            public Object getMedianame() {
                return medianame;
            }

            public void setMedianame(Object medianame) {
                this.medianame = medianame;
            }

            public Object getMediaid() {
                return mediaid;
            }

            public void setMediaid(Object mediaid) {
                this.mediaid = mediaid;
            }

            public Object getMediatime() {
                return mediatime;
            }

            public void setMediatime(Object mediatime) {
                this.mediatime = mediatime;
            }

            public Object getMomentlng() {
                return momentlng;
            }

            public void setMomentlng(Object momentlng) {
                this.momentlng = momentlng;
            }

            public Object getMomentlat() {
                return momentlat;
            }

            public void setMomentlat(Object momentlat) {
                this.momentlat = momentlat;
            }

            public Object getMomentnewsaddress() {
                return momentnewsaddress;
            }

            public void setMomentnewsaddress(Object momentnewsaddress) {
                this.momentnewsaddress = momentnewsaddress;
            }

            public long getReleasetime() {
                return releasetime;
            }

            public void setReleasetime(long releasetime) {
                this.releasetime = releasetime;
            }

            public int getLikenum() {
                return likenum;
            }

            public void setLikenum(int likenum) {
                this.likenum = likenum;
            }

            public Object getCommentnum() {
                return commentnum;
            }

            public void setCommentnum(Object commentnum) {
                this.commentnum = commentnum;
            }

            public Object getForwardnum() {
                return forwardnum;
            }

            public void setForwardnum(Object forwardnum) {
                this.forwardnum = forwardnum;
            }

            public int getShowtype() {
                return showtype;
            }

            public void setShowtype(int showtype) {
                this.showtype = showtype;
            }

            public int getDel() {
                return del;
            }

            public void setDel(int del) {
                this.del = del;
            }

            public Object getLikeList() {
                return likeList;
            }

            public void setLikeList(Object likeList) {
                this.likeList = likeList;
            }

            public UserinfoBean getUserinfo() {
                return userinfo;
            }

            public void setUserinfo(UserinfoBean userinfo) {
                this.userinfo = userinfo;
            }

            public Object getReadstatus() {
                return readstatus;
            }

            public void setReadstatus(Object readstatus) {
                this.readstatus = readstatus;
            }

            public Object getIslike() {
                return islike;
            }

            public void setIslike(Object islike) {
                this.islike = islike;
            }

            public List<PicListBean> getPicList() {
                return picList;
            }

            public void setPicList(List<PicListBean> picList) {
                this.picList = picList;
            }

            public List<?> getCommentList() {
                return commentList;
            }

            public void setCommentList(List<?> commentList) {
                this.commentList = commentList;
            }

            public static class UserinfoBean {
                /**
                 * usernick : 斯蒂芬
                 * userpic : http://handongkeji.com:8090/ixiangniupload/head/2017-07-06/LL1499321822290_mid.jpg
                 * cityname : null
                 * provincename : null
                 * usersex : 1
                 * luckmoneyid : null
                 * message : null
                 */

                private String usernick;
                private String userpic;
                private Object cityname;
                private Object provincename;
                private int usersex;
                private Object luckmoneyid;
                private Object message;

                public String getUsernick() {
                    return usernick;
                }

                public void setUsernick(String usernick) {
                    this.usernick = usernick;
                }

                public String getUserpic() {
                    return userpic;
                }

                public void setUserpic(String userpic) {
                    this.userpic = userpic;
                }

                public Object getCityname() {
                    return cityname;
                }

                public void setCityname(Object cityname) {
                    this.cityname = cityname;
                }

                public Object getProvincename() {
                    return provincename;
                }

                public void setProvincename(Object provincename) {
                    this.provincename = provincename;
                }

                public int getUsersex() {
                    return usersex;
                }

                public void setUsersex(int usersex) {
                    this.usersex = usersex;
                }

                public Object getLuckmoneyid() {
                    return luckmoneyid;
                }

                public void setLuckmoneyid(Object luckmoneyid) {
                    this.luckmoneyid = luckmoneyid;
                }

                public Object getMessage() {
                    return message;
                }

                public void setMessage(Object message) {
                    this.message = message;
                }
            }

            public static class PicListBean {
                /**
                 * insertid : 341
                 * momentnewsid : 259
                 * inserturl : http://handongkeji.com:8090/ixiangniupload/head/2017-07-14/RA1500016109614_mid.jpg
                 * inserttime : 1500016111000
                 * picLabelList : []
                 */

                private int insertid;
                private int momentnewsid;
                private String inserturl;
                private long inserttime;
                private List<?> picLabelList;

                public int getInsertid() {
                    return insertid;
                }

                public void setInsertid(int insertid) {
                    this.insertid = insertid;
                }

                public int getMomentnewsid() {
                    return momentnewsid;
                }

                public void setMomentnewsid(int momentnewsid) {
                    this.momentnewsid = momentnewsid;
                }

                public String getInserturl() {
                    return inserturl;
                }

                public void setInserturl(String inserturl) {
                    this.inserturl = inserturl;
                }

                public long getInserttime() {
                    return inserttime;
                }

                public void setInserttime(long inserttime) {
                    this.inserttime = inserttime;
                }

                public List<?> getPicLabelList() {
                    return picLabelList;
                }

                public void setPicLabelList(List<?> picLabelList) {
                    this.picLabelList = picLabelList;
                }
            }
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getUsernick() {
            return usernick;
        }

        public int getFocusnum() {
            return focusnum;
        }

        public void setFocusnum(int focusnum) {
            this.focusnum = focusnum;
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

        public Object getBrowsemomenttime() {
            return browsemomenttime;
        }

        public void setBrowsemomenttime(Object browsemomenttime) {
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

        public String getCharactersing() {
            return charactersing;
        }

        public void setCharactersing(String charactersing) {
            this.charactersing = charactersing;
        }

        public void setUsermobile(String usermobile) {
            this.usermobile = usermobile;
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

        public Object getBranchid() {
            return branchid;
        }

        public void setBranchid(Object branchid) {
            this.branchid = branchid;
        }

        public Object getJobid() {
            return jobid;
        }

        public void setJobid(Object jobid) {
            this.jobid = jobid;
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

        public String getBranchname() {
            return branchname;
        }

        public void setBranchname(String branchname) {
            this.branchname = branchname;
        }

        public Object getJobname() {
            return jobname;
        }

        public void setJobname(Object jobname) {
            this.jobname = jobname;
        }

        public Object getJuli() {
            return juli;
        }

        public void setJuli(Object juli) {
            this.juli = juli;
        }

        public int getIsfollow() {
            return isfollow;
        }

        public void setIsfollow(int isfollow) {
            this.isfollow = isfollow;
        }

        public int getIsfriend() {
            return isfriend;
        }

        public void setIsfriend(int isfriend) {
            this.isfriend = isfriend;
        }

        public int getSeehemoment() {
            return seehemoment;
        }

        public void setSeehemoment(int seehemoment) {
            this.seehemoment = seehemoment;
        }

        public int getSeememoment() {
            return seememoment;
        }

        public void setSeememoment(int seememoment) {
            this.seememoment = seememoment;
        }

        public List<NewsBean> getNewsVoList() {
            return newsVoList;
        }

        public void setNewsVoList(List<NewsBean> newsVoList) {
            this.newsVoList = newsVoList;
        }
    }
}
