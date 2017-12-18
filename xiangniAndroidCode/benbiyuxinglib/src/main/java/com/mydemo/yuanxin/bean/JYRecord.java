package com.mydemo.yuanxin.bean;

/**
 * Created by zcq on 2017/7/20.
 */

public class JYRecord {
    private Long tradingrecordId; //交易主键ID
    private String selfMemberName;	//本卡人名
    private String selfYinxinCARDID;////本卡、卡号
    private String userId;			//用户ID
    private String userName;		//用户账号
    private String yinxincoinNumber;	//银信金额
    private String sideYinxinCARDID;	//对方卡号
    private String sideMemberName;		//对方名称
    private String setupDate;			//交易创建时间
    private String modifyDate;			//交易被改时间
    private String tradingrecordCondition;	//交易状态
    private String status;	//注销激活

    private String bank_Name;	//银行名称
    private String statusTypeName;	//交易类型名称

    public String getBank_Name() {
        return bank_Name;
    }

    public String getAudit_status() {
        return audit_status;
    }

    private String audit_status; //0代表审核中 1审核通过2审核失败

    public Long getTradingrecordId() {
        return tradingrecordId;
    }

    public String getSelfMemberName() {
        return selfMemberName;
    }

    public String getSelfYinxinCARDID() {
        return selfYinxinCARDID;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getYinxincoinNumber() {
        return yinxincoinNumber;
    }

    public String getSideYinxinCARDID() {
        return sideYinxinCARDID;
    }

    public String getSideMemberName() {
        return sideMemberName;
    }

    public String getSetupDate() {
        return setupDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }


    public String getTradingrecordCondition() {
        return tradingrecordCondition;
    }

    public String getStatus() {
        return status;
    }


    public String getStatusTypeName() {
        return statusTypeName;
    }
}
