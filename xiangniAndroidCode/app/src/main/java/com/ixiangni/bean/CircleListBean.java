package com.ixiangni.bean;

import com.ixiangni.interfaces.Picable;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class CircleListBean {

    /**
     * message : 操作成功!
     * data : [{"momentnewsid":54,"userid":10,"momentnewscontent":"tgggggg","inserttype":1,"videopic":null,"inserturl":"168","medianame":null,"mediaid":null,"mediatime":null,"momentlng":"39.914130072330515","momentlat":"116.48238613656655","momentnewsaddress":"大望路(地铁站)","releasetime":1498720256000,"likenum":0,"showtype":0,"del":0,"picList":[{"insertid":167,"momentnewsid":54,"inserturl":"http://handongkeji.com:8090/ixiangniupload/moment/2017-06-29/NP1498720224920_mid.jpg","inserttime":1498720225000,"picLabelList":[{"labelid":11,"insertid":167,"labelx":"0.5791015625","labely":"0.2944444417953491","labelcontent":"yyh"}]},{"insertid":168,"momentnewsid":54,"inserturl":"http://handongkeji.com:8090/ixiangniupload/moment/2017-06-29/WP1498720225366_mid.jpg","inserttime":1498720225000,"picLabelList":[{"labelid":10,"insertid":168,"labelx":"0.6308218240737915","labely":"0.3952922224998474","labelcontent":"uhhgg"}]}],"commentList":[],"likeList":null,"userinfo":[{"usernick":"徐传婷","userpic":"http://www.handongkeji.com:8090/ixiangniupload/head/2017-06-27/PO1498560775590_mid.jpg","cityname":null,"provincename":null,"usersex":0,"luckmoneyid":null,"message":null}],"readstatus":0}]
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
         * momentnewsid : 54
         * userid : 10
         * momentnewscontent : tgggggg
         * inserttype : 1
         * videopic : null
         * inserturl : 168
         * medianame : null
         * mediaid : null
         * mediatime : null
         * momentlng : 39.914130072330515
         * momentlat : 116.48238613656655
         * momentnewsaddress : 大望路(地铁站)
         * releasetime : 1498720256000
         * likenum : 0
         * showtype : 0
         * del : 0
         * picList : [{"insertid":167,"momentnewsid":54,"inserturl":"http://handongkeji.com:8090/ixiangniupload/moment/2017-06-29/NP1498720224920_mid.jpg","inserttime":1498720225000,"picLabelList":[{"labelid":11,"insertid":167,"labelx":"0.5791015625","labely":"0.2944444417953491","labelcontent":"yyh"}]},{"insertid":168,"momentnewsid":54,"inserturl":"http://handongkeji.com:8090/ixiangniupload/moment/2017-06-29/WP1498720225366_mid.jpg","inserttime":1498720225000,"picLabelList":[{"labelid":10,"insertid":168,"labelx":"0.6308218240737915","labely":"0.3952922224998474","labelcontent":"uhhgg"}]}]
         * commentList : []
         * likeList : null
         * userinfo : [{"usernick":"徐传婷","userpic":"http://www.handongkeji.com:8090/ixiangniupload/head/2017-06-27/PO1498560775590_mid.jpg","cityname":null,"provincename":null,"usersex":0,"luckmoneyid":null,"message":null}]
         * readstatus : 0
         */

        private int momentnewsid;
        private int userid;
        private String momentnewscontent;
        private int inserttype;
        private String videopic;
        private String inserturl;
        private String medianame;
        private String mediaid;
        private String mediatime;
        private double momentlng;
        private double momentlat;
        private String momentnewsaddress;
        private long releasetime;
        private int likenum;
        private int islike;

        public int getIslike() {
            return islike;
        }

        public void setIslike(int islike) {
            this.islike = islike;
        }

        private int commentnum;

        public int getCommentnum() {
            return commentnum;
        }

        public void setCommentnum(int commentnum) {
            this.commentnum = commentnum;
        }

        public int getForwardnum() {
            return forwardnum;
        }

        public void setForwardnum(int forwardnum) {
            this.forwardnum = forwardnum;
        }

        private int forwardnum;
        private int showtype;
        private int del;
        private List<LikeBean> likeList;
        private int readstatus;
        private List<PicListBean> picList;
        private List<CommentBean> commentList;
        private List<UserinfoBean> userinfo;


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

        public String getMomentnewscontent() {
            return momentnewscontent;
        }

        public void setMomentnewscontent(String momentnewscontent) {
            this.momentnewscontent = momentnewscontent;
        }

        public int getInserttype() {
            return inserttype;
        }

        public void setInserttype(int inserttype) {
            this.inserttype = inserttype;
        }


        public List<UserinfoBean> getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(List<UserinfoBean> userinfo) {
            this.userinfo = userinfo;
        }

        public String getInserturl() {
            return inserturl;
        }

        public void setInserturl(String inserturl) {
            this.inserturl = inserturl;
        }


        public String getVideopic() {
            return videopic;
        }

        public void setVideopic(String videopic) {
            this.videopic = videopic;
        }

        public String getMedianame() {
            return medianame;
        }

        public void setMedianame(String medianame) {
            this.medianame = medianame;
        }

        public String getMediaid() {
            return mediaid;
        }

        public void setMediaid(String mediaid) {
            this.mediaid = mediaid;
        }

        public String getMediatime() {
            return mediatime;
        }

        public void setMediatime(String mediatime) {
            this.mediatime = mediatime;
        }

        public double getMomentlng() {
            return momentlng;
        }

        public void setMomentlng(double momentlng) {
            this.momentlng = momentlng;
        }

        public double getMomentlat() {
            return momentlat;
        }

        public void setMomentlat(double momentlat) {
            this.momentlat = momentlat;
        }

        public String getMomentnewsaddress() {
            return momentnewsaddress;
        }

        public void setMomentnewsaddress(String momentnewsaddress) {
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

        public List<LikeBean> getLikeList() {
            return likeList;
        }

        public void setLikeList(List<LikeBean> likeList) {
            this.likeList = likeList;
        }

        public int getReadstatus() {
            return readstatus;
        }

        public void setReadstatus(int readstatus) {
            this.readstatus = readstatus;
        }

        public List<PicListBean> getPicList() {
            return picList;
        }

        public void setPicList(List<PicListBean> picList) {
            this.picList = picList;
        }

        public List<CommentBean> getCommentList() {
            return commentList;
        }

        public void setCommentList(List<CommentBean> commentList) {
            this.commentList = commentList;
        }


        public static class PicListBean implements Picable {
            /**
             * insertid : 167
             * momentnewsid : 54
             * inserturl : http://handongkeji.com:8090/ixiangniupload/moment/2017-06-29/NP1498720224920_mid.jpg
             * inserttime : 1498720225000
             * picLabelList : [{"labelid":11,"insertid":167,"labelx":"0.5791015625","labely":"0.2944444417953491","labelcontent":"yyh"}]
             */

            private int insertid;
            private int momentnewsid;
            private String inserturl;
            private long inserttime;
            private List<PicLabelListBean> picLabelList;

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

            public List<PicLabelListBean> getPicLabelList() {
                return picLabelList;
            }

            public void setPicLabelList(List<PicLabelListBean> picLabelList) {
                this.picLabelList = picLabelList;
            }

            @Override
            public String getPicUrl() {
                return inserturl;
            }

            public static class PicLabelListBean {
                /**
                 * labelid : 11
                 * insertid : 167
                 * labelx : 0.5791015625
                 * labely : 0.2944444417953491
                 * labelcontent : yyh
                 */

                private int labelid;
                private int insertid;
                private String labelx;
                private String labely;
                private String labelcontent;

                public int getLabelid() {
                    return labelid;
                }

                public void setLabelid(int labelid) {
                    this.labelid = labelid;
                }

                public int getInsertid() {
                    return insertid;
                }

                public void setInsertid(int insertid) {
                    this.insertid = insertid;
                }

                public String getLabelx() {
                    return labelx;
                }

                public void setLabelx(String labelx) {
                    this.labelx = labelx;
                }

                public String getLabely() {
                    return labely;
                }

                public void setLabely(String labely) {
                    this.labely = labely;
                }

                public String getLabelcontent() {
                    return labelcontent;
                }

                public void setLabelcontent(String labelcontent) {
                    this.labelcontent = labelcontent;
                }
            }
        }

        public static class UserinfoBean {
            /**
             * usernick : 徐传婷
             * userpic : http://www.handongkeji.com:8090/ixiangniupload/head/2017-06-27/PO1498560775590_mid.jpg
             * cityname : null
             * provincename : null
             * usersex : 0
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
    }
}
