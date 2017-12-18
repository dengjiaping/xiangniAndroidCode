package com.ixiangni.app.mine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.easemob.redpacket.R;
import com.easemob.redpacketsdk.RPGroupMemberListener;
import com.easemob.redpacketsdk.RPSendPacketCallback;
import com.easemob.redpacketsdk.RPValueCallback;
import com.easemob.redpacketsdk.RedPacket;
import com.easemob.redpacketsdk.bean.RPUserBean;
import com.easemob.redpacketsdk.bean.RedPacketInfo;
import com.easemob.redpacketsdk.bean.TokenData;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.easemob.redpacketui.ui.activity.RPRedPacketActivity;
import com.easemob.redpacketui.utils.RPRedPacketUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.exceptions.HyphenateException;
import com.ixiangni.app.money.GroupRPActivity;
import com.ixiangni.app.money.RedPackageActivity;
import com.ixiangni.dialog.LuckDialog;
import com.ixiangni.redpackage.RPOpenPacketUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyRedPackageUtil {

    /**
     * 进入红包、转账页面的相关方法
     *
     * @param fragment       FragmentActivity
     * @param chatType       转账、单聊红包、群聊红包、小额随机红包
     * @param toChatUsername 接收者id或者群id
     * @param requestCode       requestCode
     */
    public static void startRedPacket(Fragment fragment, int chatType, final String toChatUsername,int requestCode) {
        RedPacketInfo redPacketInfo = new RedPacketInfo();

        //发送者头像url
        String fromAvatarUrl = "none";
        //发送者昵称 设置了昵称就传昵称 否则传id
        String fromNickname = EMClient.getInstance().getCurrentUser();
        EaseUser easeUser = EaseUserUtils.getUserInfo(fromNickname);
        if (easeUser != null) {
            fromAvatarUrl = TextUtils.isEmpty(easeUser.getAvatar()) ? "none" : easeUser.getAvatar();
            fromNickname = TextUtils.isEmpty(easeUser.getNick()) ? easeUser.getUsername() : easeUser.getNick();
        }

        redPacketInfo.fromAvatarUrl = fromAvatarUrl;
        redPacketInfo.fromNickName = fromNickname;

        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            //拉取最新群组数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(toChatUsername);
                        EMClient.getInstance().groupManager().fetchGroupMembers(toChatUsername, "", group.getMemberCount());
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            RedPacket.getInstance().setRPGroupMemberListener(new RPGroupMemberListener() {
                @Override
                public void getGroupMember(String groupId, RPValueCallback<List<RPUserBean>> callback) {
                    EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
                    List<String> members = group.getMembers();
                    members.add(group.getOwner());
                    members.addAll(group.getAdminList());
                    List<RPUserBean> userBeanList = new ArrayList<RPUserBean>();
                    EaseUser user;
                    for (int i = 0; i < members.size(); i++) {
                        RPUserBean userBean = new RPUserBean();
                        userBean.userId = members.get(i);
                        if (userBean.userId.equals(EMClient.getInstance().getCurrentUser())) {
                            continue;
                        }
                        user = EaseUserUtils.getUserInfo(userBean.userId);
                        if (user != null) {
                            userBean.userAvatar = TextUtils.isEmpty(user.getAvatar()) ? "none" : user.getAvatar();
                            userBean.userNickname = TextUtils.isEmpty(user.getNick()) ? user.getUsername() : user.getNick();
                        } else {
                            userBean.userNickname = userBean.userId;
                            userBean.userAvatar = "none";
                        }
                        userBeanList.add(userBean);
                    }
                    callback.onSuccess(userBeanList);
                }
            });
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            redPacketInfo.toGroupId = group.getGroupId();
            redPacketInfo.groupMemberCount = group.getAffiliationsCount();
            redPacketInfo.chatType = RPConstant.CHATTYPE_GROUP;

            Intent intent = new Intent(fragment.getContext(), GroupRPActivity.class);
            intent.putExtra(RPConstant.EXTRA_RED_PACKET_INFO, redPacketInfo);
            intent.putExtra(RPConstant.EXTRA_TOKEN_DATA, getTokenData());
            fragment.startActivityForResult(intent, requestCode);


        } else {//单人红包
            EaseUser easeToUser = EaseUserUtils.getUserInfo(toChatUsername);
            String toAvatarUrl = "none";
            String toUserName = "";
            if (easeToUser != null) {
                toAvatarUrl = TextUtils.isEmpty(easeToUser.getAvatar()) ? "none" : easeToUser.getAvatar();
                toUserName = TextUtils.isEmpty(easeToUser.getNick()) ? easeToUser.getUsername() : easeToUser.getNick();
            }
            redPacketInfo.toUserId = toChatUsername;
            redPacketInfo.toAvatarUrl = toAvatarUrl;
            redPacketInfo.toNickName = toUserName;
            redPacketInfo.chatType=chatType;
//        RPRedPacketUtil.getInstance().startRedPacket(fragment, itemType, redPacketInfo, callback);
            Intent intent = new Intent(fragment.getContext(), RedPackageActivity.class);
            intent.putExtra(RPConstant.EXTRA_RED_PACKET_INFO, redPacketInfo);
            fragment.startActivityForResult(intent, requestCode);
        }

    }

    /**
     * 拆红包的方法
     *
     * @param activity       FragmentActivity
     * @param chatType       聊天类型
     * @param message        EMMessage
     * @param toChatUsername 消息接收者id
     * @param messageList
     * @return
     */
    public static void openRedPacket(final FragmentActivity activity, final int chatType, final EMMessage message, final String toChatUsername, final EaseChatMessageList messageList) {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("正在打开...");
        progressDialog.setCanceledOnTouchOutside(false);
        String messageDirect;
        //接收者头像url 默认值为none
        String toAvatarUrl = "none";//测试用图片url:http://i.imgur.com/DvpvklR.png
        //接收者昵称 默认值为当前用户ID
        String toNickname = EMClient.getInstance().getCurrentUser();
        String currentUserId = toNickname;
        String moneyId = message.getStringAttribute("redpacketId", "");
        if (message.direct() == EMMessage.Direct.SEND) {
            messageDirect = RPConstant.MESSAGE_DIRECT_SEND;
        } else {
            messageDirect = RPConstant.MESSAGE_DIRECT_RECEIVE;
        }
        EaseUser easeUser = EaseUserUtils.getUserInfo(EMClient.getInstance().getCurrentUser());
        if (easeUser != null) {
            toAvatarUrl = TextUtils.isEmpty(easeUser.getAvatar()) ? "none" : easeUser.getAvatar();
            toNickname = TextUtils.isEmpty(easeUser.getNick()) ? easeUser.getUsername() : easeUser.getNick();
        }
        String specialAvatarUrl = "none";
        String specialNickname = "";
        String packetType;
        packetType = message.getStringAttribute("lucktype", "");
        String specialReceiveId = message.getStringAttribute("frientid", "");
        if (!TextUtils.isEmpty(packetType) && packetType.equals(RPConstant.GROUP_RED_PACKET_TYPE_EXCLUSIVE)) {
            EaseUser userInfo = EaseUserUtils.getUserInfo(specialReceiveId);
            if (userInfo != null) {
                specialAvatarUrl = TextUtils.isEmpty(userInfo.getAvatar()) ? "none" : userInfo.getAvatar();
                specialNickname = TextUtils.isEmpty(userInfo.getNick()) ? userInfo.getUsername() : userInfo.getNick();
            } else {
                specialNickname = specialReceiveId;
            }
        }
        RedPacketInfo redPacketInfo = new RedPacketInfo();
        redPacketInfo.redPacketId = moneyId;
        redPacketInfo.toAvatarUrl = toAvatarUrl;
        redPacketInfo.toNickName = toNickname;
        redPacketInfo.moneyMessage = messageDirect;
        redPacketInfo.toUserId = currentUserId;
        redPacketInfo.chatType = chatType;
        redPacketInfo.fromUserId = message.getFrom();

        String senderNickname = message.getStringAttribute("nick",message.getFrom());
        redPacketInfo.fromNickName = senderNickname;
        if (packetType.equals(RPConstant.GROUP_RED_PACKET_TYPE_EXCLUSIVE)) {
            redPacketInfo.specialAvatarUrl = specialAvatarUrl;
            redPacketInfo.specialNickname = specialNickname;
        }

        EaseUser userInfo = DemoHelper.getInstance().getUserInfo(message.getFrom());
        String userNick = userInfo.getNick();
        String avatar = userInfo.getAvatar();

        String greeting = message.getStringAttribute("message","恭喜发财，大吉大利");
        LuckDialog luckDialog = new LuckDialog(activity, moneyId, userNick, avatar, packetType, greeting);

        RPOpenPacketUtil.getInstance().openRedPacket(redPacketInfo, getTokenData(), activity, new RPOpenPacketUtil.RPOpenPacketCallBack() {
            @Override
            public void onSuccess(String senderId, String senderNickname, String myAmount) {

                luckDialog.startAnim();

                //领取红包成功 发送消息到聊天窗口
                String receiverId = EMClient.getInstance().getCurrentUser();
                //设置默认值为id
                String receiverNickname = receiverId;
                EaseUser receiverUser = EaseUserUtils.getUserInfo(receiverId);
                if (receiverUser != null) {
                    receiverNickname = TextUtils.isEmpty(receiverUser.getNick()) ? receiverUser.getUsername() : receiverUser.getNick();
                }
//                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
//                    EMMessage msg = EMMessage.createTxtSendMessage(String.format(activity.getResources().getString(R.string.msg_someone_take_red_packet), receiverNickname), toChatUsername);
//                    msg.setAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, true);
//                    msg.setAttribute(RPConstant.EXTRA_RED_PACKET_RECEIVER_NAME, receiverNickname);
//                    msg.setAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_NAME, senderNickname);
//                    EMClient.getInstance().chatManager().sendMessage(msg);
//                } else {
//                    sendRedPacketAckMessage(message, senderId, senderNickname, receiverId, receiverNickname, new EMCallBack() {
//                        @Override
//                        public void onSuccess() {
//                            messageList.refresh();
//                        }
//
//                        @Override
//                        public void onError(int i, String s) {
//
//                        }
//
//                        @Override
//                        public void onProgress(int i, String s) {
//
//                        }
//                    });
//                }
            }

            @Override
            public void showLoading() {
                progressDialog.show();
            }

            @Override
            public void hideLoading() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(String code, String message) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOpen() {

                luckDialog.show();
                luckDialog.setListener(v -> {
                    RPOpenPacketUtil.getInstance().getRedPacketMoney(redPacketInfo,activity,this);
                });
            }

            @Override
            public void onExpir() {  //  红包过期
                luckDialog.show();
                luckDialog.luckExpir();
            }

            @Override
            public void onNoRedPacket() {   //  红包已领完
                luckDialog.show();
                luckDialog.hasNoRedPacket();
            }


        });
    }

    private static TokenData getTokenData() {
        TokenData tokenData = new TokenData();
        tokenData.imUserId = EMClient.getInstance().getCurrentUser();
        //此处使用环信id代替了appUserId 开发者可传入App的appUserId
        tokenData.appUserId = EMClient.getInstance().getCurrentUser();
//        tokenData.imToken = RedPacket.getInstance().getAccessToken();
        return tokenData;
    }



    /**
     * 创建红包消息
     *
     * @param context        上下文
     * @param redPacketInfo  RedPacketInfo
     * @param toChatUsername 接收者id或群id
     * @return
     */
    public static EMMessage createRPMessage(Context context, RedPacketInfo redPacketInfo, String toChatUsername) {
        EMMessage message = EMMessage.createTxtSendMessage("[" + context.getResources().getString(R.string.easemob_red_packet) + "]" + redPacketInfo.redPacketGreeting, toChatUsername);
        message.setAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, true);
        message.setAttribute(RPConstant.EXTRA_SPONSOR_NAME, context.getResources().getString(R.string.easemob_red_packet));
        message.setAttribute(RPConstant.EXTRA_RED_PACKET_GREETING, redPacketInfo.redPacketGreeting);
        message.setAttribute(RPConstant.EXTRA_RED_PACKET_ID, redPacketInfo.redPacketId);
        message.setAttribute(RPConstant.MESSAGE_ATTR_RED_PACKET_TYPE, redPacketInfo.redPacketType);
        message.setAttribute(RPConstant.MESSAGE_ATTR_SPECIAL_RECEIVER_ID, redPacketInfo.toUserId);
        return message;
    }


    private static String getMessageDirect(EMMessage message) {
        String messageDirect;
        if (message.direct() == EMMessage.Direct.SEND) {
            messageDirect = RPConstant.MESSAGE_DIRECT_SEND;
        } else {
            messageDirect = RPConstant.MESSAGE_DIRECT_RECEIVE;
        }
        return messageDirect;
    }


    /**
     * 封装拆红包所需参数
     *
     * @param chatType 聊天类型
     * @param message  EMMessage
     * @return RedPacketInfo
     */
    private static RedPacketInfo wrapperRedPacketInfo(int chatType, EMMessage message) {
        String redPacketId = message.getStringAttribute(RPConstant.EXTRA_RED_PACKET_ID, "");
        RedPacketInfo redPacketInfo = new RedPacketInfo();
        redPacketInfo.redPacketId = redPacketId;
        redPacketInfo.messageDirect = getMessageDirect(message);
        redPacketInfo.chatType = chatType;
        return redPacketInfo;
    }


    /**
     * 使用cmd消息发送领到红包之后的回执消息
     */
    private static void sendRedPacketAckMessage(final EMMessage message, final String senderId, final String senderNickname, String receiverId, final String receiverNickname, final EMCallBack callBack) {
        //创建透传消息
        final EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        cmdMsg.setChatType(EMMessage.ChatType.Chat);
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        cmdMsg.addBody(cmdBody);
        cmdMsg.setTo(senderId);
        //设置扩展属性
        cmdMsg.setAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, true);
        cmdMsg.setAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_NAME, senderNickname);
        cmdMsg.setAttribute(RPConstant.EXTRA_RED_PACKET_RECEIVER_NAME, receiverNickname);
        cmdMsg.setAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_ID, senderId);
        cmdMsg.setAttribute(RPConstant.EXTRA_RED_PACKET_RECEIVER_ID, receiverId);
        cmdMsg.setAttribute(RPConstant.EXTRA_RED_PACKET_GROUP_ID, message.getTo());
        cmdMsg.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                //保存消息到本地
                EMMessage sendMessage = EMMessage.createTxtSendMessage("content", message.getTo());
                sendMessage.setChatType(EMMessage.ChatType.GroupChat);
                sendMessage.setFrom(message.getFrom());
                sendMessage.setTo(message.getTo());
                sendMessage.setMsgId(UUID.randomUUID().toString());
                sendMessage.setMsgTime(cmdMsg.getMsgTime());
                sendMessage.setUnread(false);//去掉未读的显示
                sendMessage.setDirection(EMMessage.Direct.SEND);
                sendMessage.setAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, true);
                sendMessage.setAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_NAME, senderNickname);
                sendMessage.setAttribute(RPConstant.EXTRA_RED_PACKET_RECEIVER_NAME, receiverNickname);
                sendMessage.setAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_ID, senderId);
                EMClient.getInstance().chatManager().saveMessage(sendMessage);
                callBack.onSuccess();
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }

    /**
     * 使用cmd消息收取领到红包之后的回执消息
     */
    public static void receiveRedPacketAckMessage(EMMessage message) {
        String senderNickname = message.getStringAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_NAME, "");
        String receiverNickname = message.getStringAttribute(RPConstant.EXTRA_RED_PACKET_RECEIVER_NAME, "");
        String senderId = message.getStringAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_ID, "");
        String receiverId = message.getStringAttribute(RPConstant.EXTRA_RED_PACKET_RECEIVER_ID, "");
        String groupId = message.getStringAttribute(RPConstant.EXTRA_RED_PACKET_GROUP_ID, "");
        String currentUser = EMClient.getInstance().getCurrentUser();
        //更新UI为 xx领取了你的红包
        if (currentUser.equals(senderId) && !receiverId.equals(senderId)) {//如果不是自己领取的红包更新此类消息UI
            EMMessage msg = EMMessage.createTxtSendMessage("content", groupId);
            msg.setChatType(EMMessage.ChatType.GroupChat);
            msg.setFrom(message.getFrom());
            if (TextUtils.isEmpty(groupId)) {
                msg.setTo(message.getTo());
            } else {
                msg.setTo(groupId);
            }
            msg.setMsgId(UUID.randomUUID().toString());
            msg.setMsgTime(message.getMsgTime());
            msg.setDirection(EMMessage.Direct.RECEIVE);
            msg.setUnread(false);//去掉未读的显示
            msg.setAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, true);
            msg.setAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_NAME, senderNickname);
            msg.setAttribute(RPConstant.EXTRA_RED_PACKET_RECEIVER_NAME, receiverNickname);
            msg.setAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_ID, senderId);
            //保存消息
            EMClient.getInstance().chatManager().saveMessage(msg);
        }
    }

    /**
     * 判断红包类型是否为小额随机红包
     *
     * @param message EMMessage
     * @return true or false
     */
    public static boolean isRandomRedPacket(EMMessage message) {
        String redPacketType = message.getStringAttribute(RPConstant.MESSAGE_ATTR_RED_PACKET_TYPE, "");
        return !TextUtils.isEmpty(redPacketType) && redPacketType.equals(RPConstant.RED_PACKET_TYPE_RANDOM);
    }

}
