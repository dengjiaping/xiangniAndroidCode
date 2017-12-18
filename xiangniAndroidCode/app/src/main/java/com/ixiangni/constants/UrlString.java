package com.ixiangni.constants;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class UrlString {
    //    public static final String BASE_URL = "http://180.76.185.203:8080/ixiangni/";//圆心服务器
    //    public static final String BASE_URL = "http://yxk.rxphpt.com/ixiangni/";
    //    public static final String BASE_URL = "http://192.168.1.114:8080/ixiangni/";//yonghong本地
    //    public static final String BASE_URL = "http://180.76.245.58:80/ixiangni/";//想你服务器的
    //    public static final String BASE_URL = "http://123.59.187.120:80/ixiangni/";//想你新服务器的
    //    public static final String BASE_URL = "http://192.168.10.222:8080/ixiangni/";//想你新服务器的
    public static final String BASE_URL = "http://ixiangni.cn/ixiangni/";//想你新服务器的
    //    public static final String BASE_URL = "http://123.59.187.121:80/ixiangni/";//想你新服务器的
    //    public static final String BASE_URL = "http://180.76.245.58:8081/ixiangni/";//58ceshi服务器的


    //    public static final String YUANXIN_URL = "http://180.76.185.203:8080/";//圆心IP,充值接口用
    //    public static final String YUANXIN_URL = "http://123.59.187.120:80/";//圆心新IP,充值接口用
    public static final String YUANXIN_URL = "http://ixiangni.cn/";//圆心新IP,充值接口用
    //    public static final String YUANXIN_URL = "http://123.59.187.121:80/";//圆心新IP,充值接口用
    //    public static final String YUANXIN_URL = "180.76.245.58:80/";//永红服务器，圆心IP,充值接口用
    //    http://{url}/mbuser/auth/userLocation.json

    /**
     * 判断有没有注册环信
     */
    public static final String URL_HUANXINUSER_ISNOTEXIST = "http://180.76.185.203:8080/RxphSign/huanxinEnroll/add.json";

    /**
     * 上传用户位置
     */
    public static final String URL_USER_LOCATION = BASE_URL + "mbuser/auth/userLocation.json";


    /**
     * 3.5.1添加图片
     */
    public static final String ADD_PUBLISH_IMAGE = BASE_URL
            + "momentNews/auth/addInsert.json";

    /**
     * 忘登录密码（重置）
     */
    public static final String URL_FORGET_PASSWORD = BASE_URL + "mbuser/forgotPassword.json";

    /**
     * 忘记支付密码（重置）
     */
    public static final String URL_RESET_PAYMENT_PASSWORD = BASE_URL + "mbuser/resetPaymentPwd.json";


    /**
     * 获取用户信息
     */
    public static final String URL_GET_USER_INFO = BASE_URL + "mbuser/auth/getUserInfo.json";

    /**
     * 修改登录密码
     */
    public static final String URL_CHANGE_LOGIN_PASSWORD = BASE_URL + "mbuser/auth/changePass.json";
    /**
     * 设置支付密码
     */
    public static final String URL_SET_PAYMENT_PASSWORD = BASE_URL + "mbuser/auth/setPaymentPwd.json";

    /**
     * 意见反馈
     */
    public static final String URL_FEEDBACK = BASE_URL + "sysOpinionback/auth/saveSysOpinionback.json";
    /**
     * 更改用户设置
     */
    public static final String URL_CHANGE_SETTING = BASE_URL + "mbuser/auth/changeSetting.json";
    /**
     * 部门列表
     */
    public static final String URL_DEPARTMENT_LIST = BASE_URL + "mbBranch/auth/getBranchList.json";
    /**
     * 职位列表
     */
    public static final String URL_JOB_LIST = BASE_URL + "mbJob/auth/getJobList.json";
    /**
     * 编辑用户资料
     */
    public static final String URL_SAVE_USER_INFO = BASE_URL + "mbuser/auth/changeUserInfo.json";

    /**
     * 上传图片
     */
    public static final String URL_UPLOAD_IMAGE = BASE_URL + "tool/uploadAPI.json";

    /**
     * 修改手机号
     */
    public static final String URL_MODIFY_PHONE_NUMBER = BASE_URL + "mbuser/auth/updateUserMobile.json";

    /**
     * 二维码
     */
    public static final String URL_QCODE = BASE_URL + "friendship/qcodeurl.html";

    /**
     * 发布想你圈
     */
    public static final String URL_PUBLISH_MISSYOUCIRCLE = BASE_URL + "momentNews/auth/addMomentNews.json";

    /**
     * 我的想你全动态
     */
    public static final String URL_MY_JIYOUQUAN = BASE_URL
            + "momentList/auth/getMeNewsList.json";
    public static final String URL_MY_CIRCRLE = BASE_URL + "momentList/auth/getMeNewsList.json";


    /**
     * 添加评论
     */
    public static final String URL_ADD_COMMENT = BASE_URL
            + "momentNews/auth/addComment.json";

    /**
     * 3.4.4删除评论
     */
    public static final String URL_DELETE_COMMENT = BASE_URL
            + "momentNews/auth/delComment.json";

    /**
     * 红包-可用余额
     */
    public static final String URL_AVAILABLE_MONEY = BASE_URL + "mbLuckymoney/auth/myAccount.json";
    /**
     * 直播列表
     */
    public static final String URL_LIVE_LIST = BASE_URL + "sysAd/getList.json";

    /**
     * 我的发布列表
     */
    public static final String URL_MY_PUBLISH_LIST = BASE_URL + "momentNews/auth/getMyselfList.json";


    /**
     * 添加文件夹
     */
    public static final String URL_ADD_DIRECTORY = BASE_URL + "mbFolder/auth/addFolder.json";
    /**
     * 文件夹列表
     */
    public static final String URL_FOLDER_LIST = BASE_URL + "mbFolder/auth/getFolderList.json";
    /**
     * 搜索用户
     */
    public static final String URL_SEARCH_USER = BASE_URL + "friendship/auth/addFriendsBySearch.json";

    /**
     * 3.11.3	添加标签
     */
    public static final String URL_ADD_LABEL = BASE_URL + "momentNews/auth/addLabel.json";

    /**
     * 3.11.4	修改标签
     */
    public static final String URL_UPDATE_LABEL = BASE_URL + "momentNews/auth/updateLabel.json";

    /**
     * 3.11.5	删除标签
     */
    public static final String URL_DELETE_LABEL = BASE_URL + "momentNews/auth/delLabel.json";

    /**
     * 删除单条想你圈
     */
    public static final String URL_DELETE_CIRCLE_ITEM = BASE_URL + "momentNews/auth/delNewsId.json";

    /**
     * 搜索用户
     */

    public static final String ULR_SEARCH_USER = BASE_URL + "friendship/auth/addFriendsBySearch.json";

    /**
     * 上传录音
     */
    public static final String URL_UPLOAD_AUDIO = BASE_URL + "tool/uploadAudio.json";
    /**
     * 添加好友记录展示
     */

    public static final String URL_GET_ADD_RECORD = BASE_URL + "friendship/auth/getAddRecord.json";
    /**
     * 接受好友请求
     */
    public static final String URL_AGREE_FRIENDSHIP = BASE_URL + "friendship/auth/acceptMakeFriendsAsk.json";
    /**
     * 好友列表
     */
    public static final String URL_FRIEND_LIST = BASE_URL + "friendship/auth/getList.json";
    /**
     * 添加好友
     */
    public static final String URL_ADD_FRIEND = BASE_URL + "friendship/auth/applyFriending.json";
    /**
     * 创建群
     */
    public static final String URL_BUILD_GROUPCHAT = BASE_URL + "groupchat/auth/buildGroupchat.json";


    /**
     * 黑名单列表
     */
    public static final String URL_BLACK_LIST = BASE_URL + "imBlacklist/auth/getBlacklist.json";

    /**
     * 加入黑名单
     */
    public static final String URL_ADD_BLACK_LIST = BASE_URL + "imBlacklist/auth/addBlack.json";
    /**
     * 移除黑名单
     */
    public static final String URL_REMOVE_BLACK = BASE_URL + "imBlacklist/auth/delBlack.json";

    /**
     * 所有群
     */
    public static final String URL_ALL_GROUP = BASE_URL + "groupchat/auth/myGroupList.json";

    /**
     * 我将创建的
     */
    public static final String URL_MY_CREATE_GROUP = BASE_URL + "groupchat/auth/iBuildGroups.json";
    /**
     * 邀请我加入的
     */
    public static final String URL_INVITED_GROUP = BASE_URL + "groupchat/auth/iJoinGroups.json";
    /**
     * 退出群/解散群
     */
    public static final String URL_QUIT_GROUP = BASE_URL + "groupchat/auth/disbandGroupchat.json";
    /**
     * 群设置
     */
    public static final String URL_MODIFY_GROUP_DATA = BASE_URL + "groupchat/auth/updateGroupChatSetting.json";
    /**
     * 群成员
     */
    public static final String URL_GROUP_MEMBER = BASE_URL + "groupchat/getMemberListByNo.json";

    /**
     * 添加好友到群
     */
    public static final String URL_ADD_MEMBER_TO_GROUP = BASE_URL + "groupchat/auth/addMemToGroup.json";
    /**
     * 解散退出群
     */
    public static final String URL_DELETE_GROUP = BASE_URL + "groupchat/auth/disbandGroupchat.json";
    /**
     * 删除群成员
     */
    public static final String URL_DELETE_GROUPMEMBER = BASE_URL + "groupchat/auth/delMember.json";
    /**
     * 搜索添加群
     */
    public static final String URL_SEARCH_ADD_GROUP = BASE_URL + "groupchat/getGroupsByName.json";
    /**
     * 添加支付方式
     */
    public static final String URL_ADD_PAY_WAY = BASE_URL + "paymentManage/auth/addAPayment.json";
    /**
     * 支付管理列表
     */
    public static final String URL_PAY_WAY_LIST = BASE_URL + "paymentManage/auth/getPayments.json";

    /**
     * 删除支付方式
     */
    public static final String URL_DELETE_PAYMENT = BASE_URL + "paymentManage/auth/delPayment.json";
    /**
     * 设置默认支付方式
     */
    public static final String URL_SET_DEFAULT_PAYMENT = BASE_URL + "paymentManage/auth/updateDefault.json";
    /**
     * 我要提现
     */
    public static final String URL_GET_CASH = BASE_URL + "withdraw/auth/liquidate.json";
    /**
     * 验证支付密码
     */
    public static final String URL_CHEK_PAYPASWORD = BASE_URL + "mbuser/auth/chkPaymentPass.json";
    /**
     * 收支明细
     */
    public static final String URL_INOUTCOME_DETAIL = BASE_URL + "mbLuckymoney/auth/accountDedatil.json";
    public static final String URL_CHECK_PAY_SET = BASE_URL + "mbuser/auth/validatePaymentPwd.json";

    /**
     * 发红包
     */
    public static final String URL_SEND_RP = BASE_URL + "mbLuckymoney/auth/awardLuckyMoney.json";

    /**
     * 1.1发红包
     */
    public static final String URL_SEND_REDPACKET = BASE_URL + "luckMoney/auth/createLuckMoney.json";

    /**
     * 1.4查询红包
     */
    public static final String URL_CHECK_LUCKMONEY = BASE_URL + "luckMoney/auth/checkLuckMoney.json";

    /**
     * 1.2拆红包
     */
    public static final String URL_CREATE_LUCKMONEY = BASE_URL + "luckMoney/auth/getLuckMoney.json";
    /**
     * 1.3领取红包列表
     */
    public static final String URL_LUCKMONEY_LIST = BASE_URL + "luckMoney/auth/getLuckMoneyList.json";
    public static final String URL_GET_USERINFOBYID = BASE_URL
            + "momentList/auth/getUserInfoById.json";

    /**
     * 表情包列表
     */
    public static final String URL_EMOTION_LIST = BASE_URL + "imBrowbag/auth/getBrowbagList.json";
    /**
     *
     */
    public static final String URL_GET_OTHER_INFO = BASE_URL + "mbuser/auth/getOtherUserById.json";


    /**
     * 购买表情包
     */
    public static final String URL_BUY_EMOTION = BASE_URL + "imBrowbag/auth/getBuyBag.json";
    /**
     * 已购买的表情包列表
     */
    public static final String URL_BROUGHT_EMOTION_LIST = BASE_URL + "imBrowbag/auth/getUserBagList.json";

    /**
     * 删除已购买表情包
     */
    public static final String URL_DELETE_EMOTIONS = BASE_URL + "imBrowbag/auth/delBuyBag.json";
    /**
     * 收藏表情
     */
    public static final String URL_COLLECT_EMOTION = BASE_URL + "imBrowbag/auth/collectBrow.json";
    /**
     * 收藏的表情列表
     */
    public static final String URL_COLLECTED_EMOTION_LIST = BASE_URL + "imBrowbag/auth/getUserBrowList.json";
    /**
     * 删除已收藏的表情
     */
    public static final String URL_DELETE_COLLECTED_EMOTIONS = BASE_URL + "imBrowbag/auth/delCollect.json";

    /**
     * 点赞
     */
    /**
     * 3.7.6	文件列表
     */
    public static final String FILE_LIST_URL = BASE_URL + "mbRecord/auth/getRecordList.json";
    public static final String URL_ADD_LIKE = BASE_URL + "momentNews/auth/addLike.json";
    /**
     * 取消点餐
     */
    public static final String URL_CANCEL_LIKE = BASE_URL + "momentNews/auth/delLike.json";

    /**
     * 转发朋友圈
     */

    public static final String URL_FORWARD_CIRCLE = BASE_URL + "momentNews/auth/addForward.json";
    /**
     * 关注
     */
    public static final String URL_ADD_FOLLOW = BASE_URL + "mbFollow/auth/addFollow.json";

    /**
     * 取消关注
     */
    public static final String URL_DELETE_FOLLOW = BASE_URL + "mbFollow/auth/delFollow.json";

    /**
     * 删除好友
     */
    public static final String URL_DELETE_FRIEND = BASE_URL + "friendship/auth/delFriend.json";
    /**
     * 我的发布
     */
    public static final String URL_MY_PUBLISH = BASE_URL + "momentNews/auth/getMyselfList.json";
    /**
     * 别人的想你圈
     */
    public static final String URL_OTHER_CIRCLE = BASE_URL + "momentList/auth/getNewsByUserId.json";
    /**
     * 附近的人
     */
    public static final String url_nearby_people = BASE_URL + "mbuser/auth/nearman.json";
    /**
     * 添加文件
     */
    public static final String URL_ADD_FILE = BASE_URL + "mbRecord/auth/addRecord.json";

    public static final String URL_DELETE_FILE = BASE_URL + "mbRecord/auth/delRecord.json";
    /**
     * 地区
     */
    public static final String URL_AREA = BASE_URL + "sysArea/getList.json";

    /**
     * 收支记录。转账、红包
     */
    public static final String URL_SHOUZHI_RECORD = BASE_URL + "busOrder/auth/getOrderList.json";

    /**
     * 转账
     */
    public static final String URL_TRANSFER = BASE_URL + "luckMoney/auth/transfer.json";
    /**
     * 转账详情
     */
    public static final String URL_TRANSFER_DETAIL = BASE_URL + "luckMoney/auth/gettransfer.json";

    /**
     * 获取文本内容
     */
    public static final String URL_GET_TEXT = BASE_URL + "sysText/getText.json";

    /**
     * 充值记录
     */
    public static final String URL_RECORD_CHARGE = BASE_URL + "mbReFill/auth/getReFillListById.json";
    /**
     * 版本更新
     */
    public static final String URL_UPDATE = BASE_URL + "autoUpdate/getVersion.json?configtype=1";
    /**
     * 我的通知
     */
    public static final String URL_TONGZHI = BASE_URL + "sysmsg/auth/getMsgList.json";
    /**
     * 搜索通讯录的好友
     */
    public static final String URL_SEARCH_FRIEND = BASE_URL + "friendship/auth/searchFromAddrList.json";

    /**
     * 飞机城市里表
     */
    public static final String URL_CITY_LIST = BASE_URL + "sysFlycode/auth/getFlycodeList.json";
    /**
     * 酒店列表
     */
    public static final String URL_HOTEL_LIST = BASE_URL + "shangLv/auth/gethlist.json";

    /**
     * 酒店详情
     */
    public static final String URL_HOTEL_DETAIL = BASE_URL + "shangLv/auth/gethotelinfo.json";
    /**
     * 酒店火车票 的城市列表
     */
    public static final String URL_CITY_LIST2 = BASE_URL + "sysArea/cityList.json";


    /**
     * 房型详情
     */
    public static final String URL_ROOM_DETAIL = BASE_URL + "shangLv/auth/roominfo.json";
    /**
     * 投诉用户
     */
    public static final String URL_COMPLAIN = BASE_URL + "mbSue/auth/saveSue.json";

    /**
     * 设置备注名
     */
    public static final String URL_SET_REMIND_NAME = BASE_URL + "friendship/auth/setRemind.json";
    /**
     * 添加好友回复
     */
    public static final String URL_REPLY_FRIENDS = BASE_URL + "friendship/auth/replyMsg.json";

    /**
     * 对好友设置想你圈权限
     */
    public static final String URL_CIRCLE_PERMISSION = BASE_URL + "friendship/auth/seeMomentLimit.json";
    /**
     * 酒店下单
     */
    public static final String URL_SUBMIT_HOTEL_ORDER = BASE_URL + "shangLv/auth/savehotelorder.json";


    /**
     * 订单列表
     */

    public static final String URL_ORDER_LIST = BASE_URL + "shangLv/auth/shangLvOrderList.json";
    /**
     * 订单详情
     */
    public static final String URL_ORDER_DETIAL = BASE_URL + "shangLv/auth/shangLvOrder.json";
    /**
     * 飞机票列表
     */
    public static final String URL_PLANE_TICKET_LIST = BASE_URL + "shangLv/auth/getflist.json";
    /**
     * 政策列表
     */

    public static final String URL_POLICY_LIST = BASE_URL + "shangLv/auth/getpolicylist.json";
    /**
     * 飞机票下单
     */
    public static final String URL_SUBMIT_PLANE_ORDER = BASE_URL + "shangLv/auth/saveforder.json";

    /**
     * 火车票列表
     */
    public static final String URL_TRAIN_LIST = BASE_URL + "shangLv/auth/gettrainlist.json";
    /**
     * 火车票下单
     */
    public static final String URL_SUBMIT_TRAIN_ORDER = BASE_URL + "shangLv/auth/savetrainorder.json";
    /**
     * 飞机票订单支付
     */
    public static final String URL_PAY_PLANE_ORDER = BASE_URL + "shangLv/auth/payforder.json";
    /**
     * 火车票订单支付
     */
    public static final String URL_PAY_TRAIN_ORDER = BASE_URL + "shangLv/auth/paytrainorder.json";
    /**
     * 消息个数
     */
    public static final String URL_MESSAGE_COUNT = BASE_URL + "sysmsg/auth/msgCount.json";

    /**
     * 通知列表
     */
    public static final String URL_MESSAGE_LIST = BASE_URL + "sysmsg/auth/getMsgList.json";
    /**
     * 消息标记为已读
     */
    public static final String URL_READ_MESSAGE = BASE_URL + "sysmsg/auth/changeRead.json";

    //    /**
    //     * 充值
    //     */
    //    public static final String URL_CHARGE=BASE_URL+"mbRefillapply/auth/saveRefillapply.json";


    /**
     * 充值
     */
    public static final String URL_CHARGE = YUANXIN_URL + "RxphMember/RxphMenber?method=rechargeIn";
    /**
     * 绑定手机号
     */
    public static final String URL_BIND_PHONE = BASE_URL + "mbuser/auth/binkPhone.json";

    /**
     * 想你圈新消息
     */
    public static final String URL_NEW_CIRCLE_MESSAGE = BASE_URL + "momentList/auth/getMeNewsStatus.json";

    /**
     * 群成员信息
     */
    public static final String URL_GET_GROUP_INFO = BASE_URL + "groupchat/getGroupByNo.json";

    /**
     * 机票退票接口
     */
    public static final String URL_PLANE_TUIPIAO = BASE_URL + "shangLv/auth/airticketRefund.json";
    /**
     * 火车票退票接口
     */
    public static final String URL_TRAIN_TUIPIAO = BASE_URL + "shangLv/auth/getTrainCancelApply.json";


    /**
     * 酒店取消预定接口
     */
    public static final String URL_HOTEL_CANCEL = BASE_URL + "shangLv/auth/getHotelCancelApply.json";

    /**
     * 删除订单接口
     */
    public static final String URL_DELETE_ORDER = BASE_URL + "shangLv/auth/airticketDelete.json";

    /**
     * 点亮查询
     */
    public static final String URL_QUERY_JINENG = BASE_URL + "rxphProductUserRelevance/getProductUserRelevanceList.json";

    /**
     * 点亮激活
     */
    public static final String URL_JINENG_LIGHTEN = BASE_URL + "rxphProductUserRelevance/getProductUserRelevanceModify.json";
    /**
     * 获取级别
     */
    public static final String URL_USER_LEVEL = BASE_URL + "mbuser/userMessage.json";
    /**
     * 公告
     */
    public static final String URL_GONGGAO = YUANXIN_URL + "jsp/notice.jsp";


}
