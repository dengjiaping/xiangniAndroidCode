package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class GroupListBean {

    /**
     * message : 操作成功!
     * data : {"list":[{"groupchatid":6,"userid":10,"groupchatname":"考虑考虑","groupchatpic":"","groupchatstatus":1,"groupchattype":null,"groupchatdesc":"北京","admit":null,"disturb":null,"groupchatno":"20803738468355"},{"groupchatid":7,"userid":10,"groupchatname":"考虑考虑","groupchatpic":"","groupchatstatus":1,"groupchattype":null,"groupchatdesc":"北京","admit":null,"disturb":null,"groupchatno":"20803753148417"},{"groupchatid":8,"userid":10,"groupchatname":"考虑考虑","groupchatpic":"","groupchatstatus":1,"groupchattype":null,"groupchatdesc":"北京","admit":null,"disturb":null,"groupchatno":"20803900997633"},{"groupchatid":9,"userid":10,"groupchatname":"巴黎","groupchatpic":"localhost:80/ixiangniupload/groupHeadPic/2017-07-05/ZS1499238835426.jpg","groupchatstatus":1,"groupchattype":null,"groupchatdesc":"咯","admit":null,"disturb":null,"groupchatno":"20804113858562"},{"groupchatid":10,"userid":10,"groupchatname":"JJ了","groupchatpic":"http://handongkeji.com:8090/ixiangniupload/groupHeadPic/2017-07-05/JQ1499238972758.jpg","groupchatstatus":1,"groupchattype":null,"groupchatdesc":"姐姐的","admit":null,"disturb":null,"groupchatno":"20804350836737"}]}
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
        private List<GroupBean> list;

        public List<GroupBean> getList() {
            return list;
        }

        public void setList(List<GroupBean> list) {
            this.list = list;
        }


    }
}
