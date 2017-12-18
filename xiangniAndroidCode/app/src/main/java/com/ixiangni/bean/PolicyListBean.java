package com.ixiangni.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/5 0005.
 */
/**
 * 优惠政策列表
 * @ClassName:PolicyListBean

 * @PackageName:com.ixiangni.bean

 * @Create On 2017/8/5 0005   16:03

 * @Site:http://www.handongkeji.com

 * @author:xuchuanting

 * @Copyrights 2017/8/5 0005 handongkeji All rights reserved.
 */
public class PolicyListBean {

    /**
     * message : 操作成功!
     * data : [{"fare":2420,"sale":2420,"flightno":"CZ6135","cabin":"D","policyid":"1708051611010466","platname":"JiKe","totalrate":"0.0","userrate":"0","bookdata":"EFB3AB0B7E3736492886C53B48217ED3CA5EF17AB0886CE1132E5ABE0FB9D84FCADAD031063FE01445EFB625EA0FDCDEF81FED32B4293F2823930BD99664BDFF1F72B33C92B5E4D2FD0DC14CC00106B273600613C8D5059CDEF1F18BF252BD367E07351CC0FA08A44829542EEC7C179738887875406DF49DD14C6137C051E15E8A9DC5C0C8C7ABE8896D5F2F3891FE3FD234A19D59D110D30BFC25E9391094E16C0DC494081DBABA518EEEB0D7647E92F3B6CCE84E4E51B91EAAE2FA12ECE344333E80F5696381057722B9410C244DC1E8D03BAA6BAB7E264A3E339975A5EB3F6358D6B2B7D93175","rateinfo":"0,0.0,0,0,0","remark":"退票改签按照航司规定执行<br />如果自行从航空公司申请改签，平台将收回代理费！","isspepolicy":"0","wtime":"00:00-23:59","rftime":"00:00-23:59"}]
     * status : 1
     */

    private String message;
    private int status;
    private List<PolicyBean> data;

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

    public List<PolicyBean> getData() {
        return data;
    }

    public void setData(List<PolicyBean> data) {
        this.data = data;
    }
}
