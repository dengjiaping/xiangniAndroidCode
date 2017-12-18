package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class EmotionListBean {

    /**
     * message : 操作成功!
     * data : [{"browbagid":1,"browbagname":"流氓兔表情包","browbaginfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/WN1499405174093_mid.jpg","browbagprice":5,"createtime":1498073165000,"isdel":0,"browList":[{"browid":1,"browname":"表情1","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/GO1499405061606_mid.jpg","browtype":0,"createtime":1498075719000,"isdel":0},{"browid":2,"browname":"表情2","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/UC1499405033980_mid.jpg","browtype":0,"createtime":1498126119000,"isdel":0},{"browid":3,"browname":"表情3","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/FF1499405016262_mid.jpg","browtype":0,"createtime":1498075719000,"isdel":0}],"isbuy":null},{"browbagid":2,"browbagname":"小屁孩表情包","browbaginfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/VZ1499405154723_mid.jpg","browbagprice":4,"createtime":1498073165000,"isdel":0,"browList":[{"browid":4,"browname":"表情4","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/EH1499404999196_mid.jpg","browtype":0,"createtime":1498075719000,"isdel":0},{"browid":5,"browname":"表情5","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/LJ1499404981151_mid.jpg","browtype":0,"createtime":1498075719000,"isdel":0},{"browid":6,"browname":"表情6","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/ZL1499404959653_mid.jpg","browtype":0,"createtime":1498075719000,"isdel":0}],"isbuy":null},{"browbagid":3,"browbagname":"张学友表情包","browbaginfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/OU1499405126653_mid.jpg","browbagprice":8,"createtime":1498073241000,"isdel":0,"browList":[{"browid":7,"browname":"表情7","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/UB1499404939564_mid.jpg","browtype":0,"createtime":1498126119000,"isdel":0},{"browid":8,"browname":"表情8","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/AJ1499404921977_mid.jpg","browtype":0,"createtime":1498176519000,"isdel":0}],"isbuy":null},{"browbagid":4,"browbagname":"非主流表情包","browbaginfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/ZJ1499405105909_mid.jpg","browbagprice":3,"createtime":1498238831000,"isdel":0,"browList":[{"browid":11,"browname":"表情11","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/ND1499404664529_mid.jpg","browtype":0,"createtime":1498235311000,"isdel":0},{"browid":10,"browname":"表情10","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/SO1499404800188_mid.jpg","browtype":0,"createtime":1498219397000,"isdel":0}],"isbuy":null},{"browbagid":5,"browbagname":"尔康表情包","browbaginfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/KL1499405087856_mid.jpg","browbagprice":8,"createtime":1498349233000,"isdel":0,"browList":[{"browid":1,"browname":"表情1","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/GO1499405061606_mid.jpg","browtype":0,"createtime":1498075719000,"isdel":0},{"browid":11,"browname":"表情11","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/ND1499404664529_mid.jpg","browtype":0,"createtime":1498235311000,"isdel":0}],"isbuy":null}]
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
         * browbagid : 1
         * browbagname : 流氓兔表情包
         * browbaginfo : http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/WN1499405174093_mid.jpg
         * browbagprice : 5.0
         * createtime : 1498073165000
         * isdel : 0
         * browList : [{"browid":1,"browname":"表情1","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/GO1499405061606_mid.jpg","browtype":0,"createtime":1498075719000,"isdel":0},{"browid":2,"browname":"表情2","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/UC1499405033980_mid.jpg","browtype":0,"createtime":1498126119000,"isdel":0},{"browid":3,"browname":"表情3","browinfo":"http://handongkeji.com:8090/ixiangnimgrupload/common/2017-07-07/FF1499405016262_mid.jpg","browtype":0,"createtime":1498075719000,"isdel":0}]
         * isbuy : null
         */

        private int browbagid;
        private String browbagname;
        private String browbaginfo;
        private double browbagprice;
        private long createtime;
        private int isdel;
        private int isbuy;
        private List<EmtionBean> browList;

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

        public double getBrowbagprice() {
            return browbagprice;
        }

        public void setBrowbagprice(double browbagprice) {
            this.browbagprice = browbagprice;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public int getIsdel() {
            return isdel;
        }

        public void setIsdel(int isdel) {
            this.isdel = isdel;
        }

        public int getIsbuy() {
            return isbuy;
        }

        public void setIsbuy(int isbuy) {
            this.isbuy = isbuy;
        }

        public List<EmtionBean> getBrowList() {
            return browList;
        }

        public void setBrowList(List<EmtionBean> browList) {
            this.browList = browList;
        }


    }
}
