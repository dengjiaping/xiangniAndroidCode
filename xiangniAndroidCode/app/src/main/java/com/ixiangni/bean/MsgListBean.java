package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class MsgListBean {

    /**
     * message : 操作成功!
     * data : [{"msgid":300,"msgcontent":"想你狗请求加您为好友","msgtime":1501744986000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":299,"msgcontent":"想你狗请求加您为好友","msgtime":1501744416000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":298,"msgcontent":"想你狗请求加您为好友","msgtime":1501744198000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":297,"msgcontent":"想你狗请求加您为好友","msgtime":1501744165000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":296,"msgcontent":"想你狗请求加您为好友","msgtime":1501743400000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":295,"msgcontent":"想你狗请求加您为好友","msgtime":1501743393000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":294,"msgcontent":"想你狗请求加您为好友","msgtime":1501743388000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":290,"msgcontent":"想你狗请求加您为好友","msgtime":1501742882000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":285,"msgcontent":"想你狗请求加您为好友","msgtime":1501726574000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":283,"msgcontent":"想你狗请求加您为好友","msgtime":1501726558000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":282,"msgcontent":"想你狗请求加您为好友","msgtime":1501726285000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":281,"msgcontent":"想你狗请求加您为好友","msgtime":1501723901000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":280,"msgcontent":"想你狗请求加您为好友","msgtime":1501723873000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":268,"msgcontent":"虾米请求加您为好友","msgtime":1501664853000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":249,"msgcontent":"赵请求加您为好友","msgtime":1501643591000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":247,"msgcontent":"赵请求加您为好友","msgtime":1501641652000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":240,"msgcontent":"想你狗请求加您为好友","msgtime":1501579511000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":235,"msgcontent":"想你狗同意您的好友添加请求","msgtime":1501577854000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":232,"msgcontent":"想你狗请求加您为好友","msgtime":1501575843000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null},{"msgid":230,"msgcontent":"想你狗请求加您为好友","msgtime":1501575703000,"msgstatus":0,"msgtype":null,"userid":10,"areaid":null,"postid":null}]
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
         * msgid : 300
         * msgcontent : 想你狗请求加您为好友
         * msgtime : 1501744986000
         * msgstatus : 0
         * msgtype : null
         * userid : 10
         * areaid : null
         * postid : null
         */

        private int msgid;
        private String msgcontent;
        private long msgtime;
        private int msgstatus;
        private Object msgtype;
        private int userid;
        private Object areaid;
        private Object postid;


        public int getMsgid() {
            return msgid;
        }

        public void setMsgid(int msgid) {
            this.msgid = msgid;
        }

        public String getMsgcontent() {
            return msgcontent;
        }

        public void setMsgcontent(String msgcontent) {
            this.msgcontent = msgcontent;
        }

        public long getMsgtime() {
            return msgtime;
        }

        public void setMsgtime(long msgtime) {
            this.msgtime = msgtime;
        }

        public int getMsgstatus() {
            return msgstatus;
        }

        public void setMsgstatus(int msgstatus) {
            this.msgstatus = msgstatus;
        }

        public Object getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(Object msgtype) {
            this.msgtype = msgtype;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public Object getAreaid() {
            return areaid;
        }

        public void setAreaid(Object areaid) {
            this.areaid = areaid;
        }

        public Object getPostid() {
            return postid;
        }

        public void setPostid(Object postid) {
            this.postid = postid;
        }
    }
}
