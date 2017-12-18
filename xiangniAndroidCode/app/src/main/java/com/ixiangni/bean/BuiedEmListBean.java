package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class BuiedEmListBean {

    /**
     * message : 操作成功!
     * data : [{"browbaguserid":10,"userid":10,"browbagid":1,"browbagname":"流氓兔表情包","browbaginfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/WN1499405174093_mid.jpg","buyprice":5,"buytime":1499736492000,"userBrowList":[{"browuserid":28,"browbaguserid":10,"userid":10,"browid":1,"browname":"表情1","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/GO1499405061606_mid.jpg","browtype":1},{"browuserid":29,"browbaguserid":10,"userid":10,"browid":2,"browname":"表情2","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/UC1499405033980_mid.jpg","browtype":1},{"browuserid":30,"browbaguserid":10,"userid":10,"browid":3,"browname":"表情3","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/FF1499405016262_mid.jpg","browtype":1}]},{"browbaguserid":11,"userid":10,"browbagid":2,"browbagname":"小屁孩表情包","browbaginfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/VZ1499405154723_mid.jpg","buyprice":4,"buytime":1499738295000,"userBrowList":[{"browuserid":31,"browbaguserid":11,"userid":10,"browid":4,"browname":"表情4","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/EH1499404999196_mid.jpg","browtype":1},{"browuserid":32,"browbaguserid":11,"userid":10,"browid":5,"browname":"表情5","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/LJ1499404981151_mid.jpg","browtype":1},{"browuserid":33,"browbaguserid":11,"userid":10,"browid":6,"browname":"表情6","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/ZL1499404959653_mid.jpg","browtype":1}]},{"browbaguserid":12,"userid":10,"browbagid":3,"browbagname":"张学友表情包","browbaginfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/OU1499405126653_mid.jpg","buyprice":8,"buytime":1499739008000,"userBrowList":[{"browuserid":34,"browbaguserid":12,"userid":10,"browid":7,"browname":"表情7","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/UB1499404939564_mid.jpg","browtype":1},{"browuserid":35,"browbaguserid":12,"userid":10,"browid":8,"browname":"表情8","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/AJ1499404921977_mid.jpg","browtype":1}]}]
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
         * browbaguserid : 10
         * userid : 10
         * browbagid : 1
         * browbagname : 流氓兔表情包
         * browbaginfo : http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/WN1499405174093_mid.jpg
         * buyprice : 5.0
         * buytime : 1499736492000
         * userBrowList : [{"browuserid":28,"browbaguserid":10,"userid":10,"browid":1,"browname":"表情1","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/GO1499405061606_mid.jpg","browtype":1},{"browuserid":29,"browbaguserid":10,"userid":10,"browid":2,"browname":"表情2","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/UC1499405033980_mid.jpg","browtype":1},{"browuserid":30,"browbaguserid":10,"userid":10,"browid":3,"browname":"表情3","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/FF1499405016262_mid.jpg","browtype":1}]
         */

        private int browbaguserid;
        private int userid;
        private int browbagid;
        private String browbagname;
        private String browbaginfo;
        private double buyprice;
        private long buytime;
        private List<UserBrowListBean> userBrowList;

        public int getBrowbaguserid() {
            return browbaguserid;
        }

        public void setBrowbaguserid(int browbaguserid) {
            this.browbaguserid = browbaguserid;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public int getBrowbagid() {
            return browbagid;
        }

        public void setBrowbagid(int browbagid) {
            this.browbagid = browbagid;
        }

        public String getBrowbagname() {
            return browbagname;
        }

        public void setBrowbagname(String browbagname) {
            this.browbagname = browbagname;
        }

        public String getBrowbaginfo() {
            return browbaginfo;
        }

        public void setBrowbaginfo(String browbaginfo) {
            this.browbaginfo = browbaginfo;
        }

        public double getBuyprice() {
            return buyprice;
        }

        public void setBuyprice(double buyprice) {
            this.buyprice = buyprice;
        }

        public long getBuytime() {
            return buytime;
        }

        public void setBuytime(long buytime) {
            this.buytime = buytime;
        }

        public List<UserBrowListBean> getUserBrowList() {
            return userBrowList;
        }

        public void setUserBrowList(List<UserBrowListBean> userBrowList) {
            this.userBrowList = userBrowList;
        }

        public static class UserBrowListBean {
            /**
             * browuserid : 28
             * browbaguserid : 10
             * userid : 10
             * browid : 1
             * browname : 表情1
             * browinfo : http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/GO1499405061606_mid.jpg
             * browtype : 1
             */

            private int browuserid;
            private int browbaguserid;
            private int userid;
            private int browid;
            private String browname;
            private String browinfo;
            private int browtype;

            public int getBrowuserid() {
                return browuserid;
            }

            public void setBrowuserid(int browuserid) {
                this.browuserid = browuserid;
            }

            public int getBrowbaguserid() {
                return browbaguserid;
            }

            public void setBrowbaguserid(int browbaguserid) {
                this.browbaguserid = browbaguserid;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public int getBrowid() {
                return browid;
            }

            public void setBrowid(int browid) {
                this.browid = browid;
            }

            public String getBrowname() {
                return browname;
            }

            public void setBrowname(String browname) {
                this.browname = browname;
            }

            public String getBrowinfo() {
                return browinfo;
            }

            public void setBrowinfo(String browinfo) {
                this.browinfo = browinfo;
            }

            public int getBrowtype() {
                return browtype;
            }

            public void setBrowtype(int browtype) {
                this.browtype = browtype;
            }
        }
    }
}
