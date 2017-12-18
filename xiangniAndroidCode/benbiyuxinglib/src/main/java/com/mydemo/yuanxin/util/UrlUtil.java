package com.mydemo.yuanxin.util;

/**
 * Created by zcq on 2017/7/19.
 */

public class UrlUtil {
    /**
     * 请求地址
     *
     * @author zcq
     */

    // http://192.168.0.109:8080/RxphMember/RxphMenber?metho=Longin

    //共用前置接口段
    public static String getHttpIpPost() {
//        return "http://yxk.rxphpt.com";//服务器
//        return "http://180.76.185.203:8080";//圆心服务器
//        return "http://123.59.187.120:80";//圆心新服务器
        return "http://ixiangni.cn";//圆心新服务器
//        return "http://123.59.187.121:80";//圆心新服务器
//        return "http://192.168.10.232:8080";//永红服务器
//        return "http://180.76.245.58:8080";//想你服务器

    }

    /**
     * 登录接口
     */

    public static String BANK_INFO_URL() {
        String string = "http://180.76.185.203:8080/RxphSign/bankCard/bankCardDetail.json";
        return  string;
    }


    /**
     * 登录接口
     */

    public static String Login_URL() {
        String string = "/RxphMember/RxphMenber?method=Longin";
        return getHttpIpPost() + string;
    }

    /**
     * 忘记密码进入接口
     */
    public static String ForgetPW_URL() {
        String string = "/RxphMember/RxphMenber?method=forgetPass";
        return getHttpIpPost() + string;
    }

    /**
     * 忘记密码提交接口
     *
     * @return
     */
    public static String ForgetPW_SUBMIT_URL() {
        String string = "/RxphMember/RxphMenber?method=forgetPassword";
        return getHttpIpPost() + string;
    }

    /**
     * ⦁	修改密码验证
     */
    public static String GM_YZM_URL() {
        String string = "/RxphMember/RxphMenber?method=PhoneVerify&verifyType=strModifyPass";
        return getHttpIpPost() + string;
    }

    /**
     * ⦁	转账验证码
     */
    public static String ZZ_YZM_URL() {
        String string = "/RxphMember/RxphMenber?method=PhoneVerify&verifyType=strTransfer";
        return getHttpIpPost() + string;
    }

    /**
     * ⦁	提现验证码
     */
    public static String TX_YZM_URL() {
        String string = "/RxphMember/RxphMenber?method=PhoneVerify&verifyType=strWithdraw";
        return getHttpIpPost() + string;
    }


    /**
     * 获取用户首页信息接口
     */
    public static String UserMain_URL() {
        String string = "/RxphMember/RxphMenber?method=userData";
        return getHttpIpPost() + string;
    }


    /**
     * 提现接口
     */
    public static String TX_URL() {
        String string = "/RxphMember/RxphTradingrecord?method=outMoneyApply";
        return getHttpIpPost() + string;
    }

    /**
     * 转账接口
     */
    public static String ZZ_URL() {
        String string = "/RxphMember/RxphTradingrecord?method=transferOutIn";
        return getHttpIpPost() + string;
    }

    /**
     * 充值接口
     */
    public static String CZ_URL() {
        String string = "/RxphMember/RxphTradingrecord?method=addMoneyApply";
        return getHttpIpPost() + string;
    }

    /**
     * 获取交易记录接口
     */
    public static String JYRecord_URL() {
        String string = "/RxphMember/RxphTradingrecord?method=useUeal";
        return getHttpIpPost() + string;
    }

    /**
     * 修改密码接口
     */
    public static String Modify_URL() {
        String string = "/RxphMember/RxphMenber?method=ModifyPassword";
        return getHttpIpPost() + string;
    }

    /**
     * 提现帮助文档地址
     */


    public static String TX_HELP_URL() {
        String string = "/pcPage/help.html";
        return getHttpIpPost() + string;
    }
    /**
     * 转账帮助文档地址
     */


    public static String ZZ_HELP_URL() {
        String string = "/pcPage/transfer.html";
        return getHttpIpPost() + string;
    }
    /**
     * 充值帮助文档地址
     */
    public static String CZ_HELP_URL() {
        String string = "/pcPage/recharge.html";
        return getHttpIpPost() + string;
    }

}

