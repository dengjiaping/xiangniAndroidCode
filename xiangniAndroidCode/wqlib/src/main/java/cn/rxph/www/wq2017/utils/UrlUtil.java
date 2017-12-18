package cn.rxph.www.wq2017.utils;

/**
 * Created by zcq on 2017/7/19.
 */

public class UrlUtil {
    /**
     * 请求地址
     *
     * @author zcq
     */

//    public static final String BASE_URL = "http://123.59.187.120:8080/ixiangni/";//正式新服务器
//    public static final String BASE_URL = "http://192.168.10.232:8080/";//永红服务器
    public static final String BASE_URL = "http://180.76.185.203:8080/";//服务器

    //共用前置接口段
    public static String getHttpIpPost() {

        return "http://180.76.185.203:8080";//圆心新服务器

    }

//    /**
//     * 登陆接口
//     */
//    public static final String URL_LOGIN_WQ = BASE_URL + "RxphSign/mbUser/login.json";

    /**
     * 查询推荐人接口
     */
    public static final String URL_REFEREE_INFO = BASE_URL + "RxphSign/mbUser/referrerUser.json";

    /**
     * 手机重复查询
     */
    public static final String URL_QUERY_TEL_REPEAT = BASE_URL + "RxphSign/mbUser/isExitUserMobile.json";


    /**
     * 验证支付密码
     */
    public static final String URL_CHEK_PAYPASWORD = BASE_URL + "mbuser/auth/chkPaymentPass.json";

    /**
     * 意见反馈
     */
    public static final String URL_FEEDBACK = "http://123.59.187.120:80/ixiangni/" + "sysOpinionback/auth/saveSysOpinionback.json";
    /**
     * 发展中心查询
     */
    public static final String URL_QUERY_COMPANY = BASE_URL + "RxphSign/mbBranch/queryFuzzyAll.json";

    /**
     * 第一次上传接口
     */
    public static final String URL_SHANGCHUAN = BASE_URL + "RxphSign/Rxphmemberapply/add.json";

    /**
     * 重新上传接口
     */
    public static final String URL_AGAIN_SHANGCHUAN = BASE_URL + "RxphSign/Rxphmemberapply/againAdd.json";

    /**
     * 签约记录接口
     */
    public static final String URL_QY_RECORD = BASE_URL + "RxphSign/Rxphmemberapply/queryAll.json";

    /**
     * 重新提交查询原信息接口
     */
    public static final String URL_cxtijiaohuoquyuaninfo = BASE_URL + "RxphSign/Rxphmemberapply/queryAlone.json";


    /**
     * 常见问题文档地址
     */
    public static String CJ_QUESTION_URL() {
        String string = "/pcPage/annotation.html";
        return getHttpIpPost() + string;
    }

    /**
     * 条款文档地址
     */
    public static String CLAUSE_URL() {
        String string = "/pcPage/agreement.html";
        return getHttpIpPost() + string;
    }


}

