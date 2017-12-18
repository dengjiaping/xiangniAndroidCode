package com.ixiangni.bean;

/**
 * Created by Administrator on 2017/10/13.
 */

public class JiNengBean {
    private int relevanceSign; // 0未点亮,1已点亮
    private String productName; //产品名称
    private String productPicture; //图片路径
    private int relevanceId; //产品主键ID,调用修改接口需要

    public int getRelevanceSign() {
        return relevanceSign;
    }

    public void setRelevanceSign(int relevanceSign) {
        this.relevanceSign = relevanceSign;
    }

    public int getRelevanceId() {
        return relevanceId;
    }

    public void setRelevanceId(int relevanceId) {
        this.relevanceId = relevanceId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

}
