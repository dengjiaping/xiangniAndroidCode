package com.hyphenate.chatuidemo.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.easemob.redpacket.utils.RedPacketUtil;
import com.easemob.redpacket.utils.TransferConstance;
import com.easemob.redpacket.utils.TransferUtil;
import com.easemob.redpacket.widget.ChatRowRandomPacket;
import com.easemob.redpacket.widget.ChatRowRedPacket;
import com.easemob.redpacket.widget.ChatRowRedPacketAck;
import com.easemob.redpacket.widget.ChatRowTransfer;
import com.easemob.redpacketsdk.bean.RedPacketInfo;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.easemob.redpacketui.utils.RPRedPacketUtil;
import com.handongkeji.impactlib.util.ToastUtils;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.domain.RobotUser;
import com.hyphenate.chatuidemo.widget.ChatRowVoiceCall;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenu;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconScrollTabBar;
import com.hyphenate.util.PathUtil;
import com.ixiangni.app.R;
import com.ixiangni.app.chat.ChatSettingActivity;
import com.ixiangni.app.chat.GroupSettingActivity;
import com.ixiangni.app.mine.EmotionActivity;
import com.ixiangni.app.mine.MyEmotionActivity;
import com.ixiangni.app.mine.MyRedPackageUtil;
import com.ixiangni.app.money.TransferActivity;
import com.ixiangni.app.money.TransferDetailActivity;
import com.ixiangni.app.user.PersonPageActivity;
import com.ixiangni.common.EmotionManager;
import com.ixiangni.common.XNConstants;
import com.ixiangni.interfaces.OnEmtionChange;
import com.ixiangni.interfaces.OnGroupNameChange;
import com.ixiangni.interfaces.OnSendMessage;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hyphenate.easeui.EaseConstant.CHATTYPE_GROUP;

public class ChatFragment extends EaseChatFragment implements EaseChatFragmentHelper,
        OnGroupNameChange, OnEmtionChange, OnSendMessage {

    // constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;
    private static final int ITEM_TRANSFER = 15;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;

    //red packet code : 红包功能使用的常量
    private static final int MESSAGE_TYPE_RECV_RED_PACKET = 5;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET = 6;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET_ACK = 7;
    private static final int MESSAGE_TYPE_RECV_RED_PACKET_ACK = 8;
    private static final int MESSAGE_TYPE_RECV_RANDOM = 11;
    private static final int MESSAGE_TYPE_SEND_RANDOM = 12;
    private static final int ITEM_RED_PACKET = 16;

    public static final int MESSAGE_TYPE_RECV_TRANSFER = 9;
    public static final int MESSAGE_TYPE_SEND_TRANSFER = 10;

    private static final int REQUEST_CODE_SEND_RED_PACKET = 17;
    private static final int REQUEST_CODE_TRANSFER = 18;

    private static final int REQUEST_CODE_CLEAR_HISTORY = 19;
    //end of red packet code

    /**
     * if it is chatBot
     */
    private boolean isRobot;
    //扩展消息 昵称
    public static final String IXN_USER_NICK_NAME = "IXN_USER_NICK_NAME";
    //扩展消息 头像
    public static final String IXN_USER_Advatar = "IXN_USER_Advatar";

    //扩展消息 接收者昵称
    public static final String IXN_RECV_NICKNAME = "RECEIVER_USER_NICK_NAME";
    //扩展消息 接收者头像
    public static final String IXN_RECV_ADVATAR = "RECEIVER_USER_ADVATAR";


    private EaseEmojiconMenu emojiconMenu;
    private ArrayList<EaseEmojiconGroupEntity> emojiconGroupList;
    private String[] caozuo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SuperObservableManager.getInstance().getObservable(OnGroupNameChange.class).registerObserver(this);
        SuperObservableManager.getInstance()
                .getObservable(OnEmtionChange.class)
                .registerObserver(this);
        SuperObservableManager.registerObserver(OnSendMessage.class, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SuperObservableManager.getInstance().getObservable(OnGroupNameChange.class).unregisterObserver(this);
        SuperObservableManager.getInstance().getObservable(OnEmtionChange.class).unregisterObserver(this);
        SuperObservableManager.unregisterObserver(OnSendMessage.class, this);
    }

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            Map<String, RobotUser> robotMap = DemoHelper.getInstance().getRobotList();
            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
                isRobot = true;
            }
        }
        //        else if(chatType==EaseConstant.CHATTYPE_GROUP){
        //            titleBar.setTitle(getArguments().getString(XNConstants.groupname));
        //        }
        super.setUpView();

        titleBar.setRightLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                    //                    emptyHistory();
                    Intent intent = new Intent(getContext(), ChatSettingActivity.class);
                    intent.putExtra(XNConstants.USERID, toChatUsername.replace("ixn", ""));
                    startActivityForResult(intent, REQUEST_CODE_CLEAR_HISTORY);

                } else {
                    toGroupDetails();
                }
            }
        });

        EaseUser easeUser = DemoHelper.getInstance().getContactList().get(toChatUsername);
        if (easeUser != null && !TextUtils.isEmpty(easeUser.getNickname())) {

            String nickname = easeUser.getNickname();
            nickname = CommonUtils.getParseName(nickname);
            titleBar.setTitle(nickname);
        }

        // set click listener
        titleBar.setLeftLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //                if (EasyUtils.isSingleActivity(getActivity())) {
                //                    Intent intent = new Intent(getActivity(), MainActivity.class);
                //                    startActivity(intent);
                //                }
                onBackPressed();
            }
        });
        emojiconMenu = (EaseEmojiconMenu) inputMenu.getEmojiconMenu();
        EaseEmojiconScrollTabBar tabBar = emojiconMenu.getTabBar();
        //表情添加
        tabBar.setEmojiAddClickListener((v) -> {
            startActivity(new Intent(getContext(), EmotionActivity.class));
        });

        //表情设置
        tabBar.setSettingClickListener(v -> startActivity(new Intent(getContext(), MyEmotionActivity.class)));

        //隐藏兔子
        //        emojiconMenu.addEmojiconGroup(EmojiconExampleGroupData.getData());
        //        inputMenu.init(emojiconGroupList);

        //添加已购买表情
        refreshEmotion();

        if (chatType == CHATTYPE_GROUP) {
            inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 1 && "@".equals(String.valueOf(s.charAt(start)))) {
                        startActivityForResult(new Intent(getActivity(), PickAtUserActivity.class).
                                putExtra("groupId", toChatUsername), REQUEST_CODE_SELECT_AT_USER);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }


    @Override
    protected void toGroupDetails() {
        if (chatType == CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            //                if (group == null) {
            //                    ToastUtils.show(getContext(),"未找到该群组");
            //                    return;
            //                }
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        }
    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
        //extend menu items
        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
        //        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
            inputMenu.registerExtendMenuItem("转账", R.mipmap.zhuanzhang, ITEM_TRANSFER, extendMenuItemClickListener);
        }
        //聊天室暂时不支持红包功能
        //red packet code : 注册红包菜单选项
        if (chatType != Constant.CHATTYPE_CHATROOM) {
            inputMenu.registerExtendMenuItem(R.string.attach_red_packet,
                    R.mipmap.hongbao, ITEM_RED_PACKET,
                    extendMenuItemClickListener);
        }
        //end of red packet code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // copy
                    clipboard.setPrimaryClip(ClipData.newPlainText(null,
                            ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                    break;

                case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
                    Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
                    intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
                    startActivity(intent);

                    break;

                default:
                    break;
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CLEAR_HISTORY:
                    emptyHistory();
                    break;
                case REQUEST_CODE_TRANSFER:

                    if (data != null) {
                        String transferid = data.getStringExtra(TransferConstance.TransferId);
                        String moneynum = data.getStringExtra(TransferConstance.moneynum);
                        String message = data.getStringExtra(TransferConstance.message);
                        EMMessage transferMsg = EMMessage.createTxtSendMessage("[" + "想你转账" + "]", toChatUsername);
                        transferMsg.setAttribute(TransferConstance.Transfer, TransferConstance.Transfer);
                        transferMsg.setAttribute(TransferConstance.TransferId, transferid);
                        transferMsg.setAttribute(TransferConstance.message, message);
                        transferMsg.setAttribute(TransferConstance.moneynum, moneynum);
                        sendMessage(transferMsg);
                    }
                    break;
                case REQUEST_CODE_SEND_RED_PACKET:
                    if (data != null) {
                        RedPacketInfo info = data.getParcelableExtra(RPConstant.EXTRA_RED_PACKET_INFO);
                        sendMessage(RedPacketUtil.createRPMessage(getActivity(), info, toChatUsername));
                    }
                    break;
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_AT_USER:
                    if (data != null) {
                        String username = data.getStringExtra("username");
                        inputAtUsername(username, false);
                    }
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        //        if(isRobot){
        //set message extension
        message.setAttribute("em_robot_message", isRobot);

        //将昵称和头像作为扩展详细发送
        EaseUser currentUserInfo = DemoHelper.getInstance().getUserProfileManager().getCurrentUserInfo();
        message.setAttribute(IXN_USER_NICK_NAME, currentUserInfo.getNick());
        message.setAttribute(IXN_USER_Advatar, currentUserInfo.getAvatar());
        if (chatType == CHATTYPE_GROUP) {
            //发送群头像和昵称
            EaseUser userInfo = DemoHelper.getInstance().getUserInfo(toChatUsername);
            if (userInfo != null && userInfo.getAvatar() != null) {
                message.setAttribute(IXN_RECV_ADVATAR, userInfo.getAvatar());
                EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                if (group != null) {
                    message.setAttribute(IXN_RECV_NICKNAME, group.getGroupName());
                }

            }
        }

        //        }
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }


    @Override
    public void onEnterToChatDetails() {
        if (chatType == CHATTYPE_GROUP) {
            //            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            //            if (group == null) {
            //                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
            //                return;
            //            }
            //            startActivityForResult(
            //                    (new Intent(getActivity(), GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
            //                    REQUEST_CODE_GROUP_DETAIL);
            //群详情
            GroupSettingActivity.start(getContext(), toChatUsername);


        } else if (chatType == Constant.CHATTYPE_CHATROOM) {
            startActivityForResult(new Intent(getActivity(), ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername), REQUEST_CODE_GROUP_DETAIL);
        }
    }

    @Override
    public void onAvatarClick(String username) {
        //        //handling when user click avatar
        //        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
        //        intent.putExtra("username", username);
        //        startActivity(intent);
        String userid = username.replace("ixn", "");
        PersonPageActivity.start(getActivity(), userid);
    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }


    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        //消息框点击事件，demo这里不做覆盖，如需覆盖，return true
        //red packet code : 拆红包页面
        if (RedPacketUtil.isRPMessage(message)) {
            MyRedPackageUtil.openRedPacket(getActivity(), chatType, message, toChatUsername, messageList);
            return true;
        }
        if (TransferUtil.isTransferMsg(message)) {
            String transferid = message.getStringAttribute(TransferConstance.TransferId, null);
            if (!TextUtils.isEmpty(transferid)) {
                Intent intent = new Intent(getContext(), TransferDetailActivity.class);
                intent.putExtra(TransferConstance.TransferId, transferid);
                startActivity(intent);
            }
            return true;
        }
        //end of red packet code
        return false;
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        //red packet code : 处理红包回执透传消息
        for (EMMessage message : messages) {
            EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
            String action = cmdMsgBody.action();//获取自定义action
            if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
                RedPacketUtil.receiveRedPacketAckMessage(message);
                messageList.refresh();
            }
        }
        //end of red packet code
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
        File file = new File(Environment.getExternalStorageDirectory().getPath());
        // no message forward when in chat room
        EMMessage.Type type = message.getType();
        String from = message.getFrom();
        String username = DemoHelper.getInstance().getUserProfileManager().getCurrentUserInfo().getUsername();
        Log.w(TAG, "DemoHelper.getInstance().etUserProfileManager().getCurrentUserInfo().getUsername():" + username);
        Log.w(TAG, " message.getFrom():" + from);
        //如果是自己有撤回，不是自己发的没有撤回
        if (from.equals(username)) {
            //            caozuo = new String[]{"收藏", "删除", "撤回"};
            caozuo = new String[]{"收藏", "删除"};
        } else {
            caozuo = new String[]{"收藏", "删除"};
        }

        if (EMMessage.Type.IMAGE.equals(type)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setItems(caozuo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 1) {
                                conversation.removeMessage(message.getMsgId());
                                messageList.refresh();
                            } else if (which == 0) {
                                ProgressDialog progressDialog = ProgressDialog.show(getContext(), null, "收藏" +
                                        "中...", true, true);
                                EmotionManager.getInstance().collectEmotion(getContext(), message, new OnResult<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        if (titleBar != null) {
                                            progressDialog.dismiss();
                                            ToastUtils.show(getContext(), s);

                                        }
                                    }

                                    @Override
                                    public void onFailed(String errorMsg) {
                                        if (titleBar != null) {

                                            progressDialog.dismiss();
                                            if ("图片已在表情包...".equals(errorMsg)) {
                                                ToastUtils.show(getContext(), errorMsg);
                                            } else {
                                                if (!CommonUtils.isStringNull(errorMsg)) {
                                                    ToastUtils.show(getContext(), "收藏失败...");

                                                }

                                            }
                                        }

                                    }
                                });
                            } else {
                                //                                //发送要回撤的透传消息
                                //                                EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
                                //                                // 如果是群聊，设置chattype，默认是单聊
                                //                                if (chatType == CHATTYPE_GROUP) {
                                //                                    cmdMsg.setChatType(EMMessage.ChatType.GroupChat);
                                //                                }
                                //                                String action = "REVOKE_FLAG";
                                //                                EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
                                //                                // 设置消息body
                                //                                cmdMsg.addBody(cmdBody);
                                //                                // 设置要发给谁，用户username或者群聊groupid
                                //                                cmdMsg.setTo(toChatUsername);
                                //                                // 通过扩展字段添加要撤回消息的id
                                //                                cmdMsg.setAttribute("msgId", message.getMsgId());
                                //                                EMChatManager em = new EMChatManager();
                                //                                em.sendMessage(cmdMsg);


                                //                                EMChatManager.getInstance().sendMessage(cmdMsg, new EMCallBack() {
                                //                                    @Override
                                //                                    public void onSuccess() {
                                //                                    }
                                //
                                //                                    @Override
                                //                                    public void onProgress(int progress, String status) {
                                //                                    }
                                //
                                //                                    @Override
                                //                                    public void onError(int code, String error) {
                                //                                    }
                                //                                });
                            }
                        }
                    });
            builder.show();
        } else if (EMMessage.Type.VIDEO.equals(type) || EMMessage.Type.LOCATION.equals(type)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setItems(new String[]{"删除"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    conversation.removeMessage(message.getMsgId());
                    messageList.refresh();
                }
            });
            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setItems(new String[]{"删除", "复制"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {

                        conversation.removeMessage(message.getMsgId());
                        messageList.refresh();
                    } else {

                    }
                }
            });
            builder.show();
        }


    }


    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        Intent intent = null;
        switch (itemId) {
            case ITEM_VIDEO:
                intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                break;
            //            case ITEM_FILE: //file
            //                selectFileFromLocal();
            //                break;
            case ITEM_VOICE_CALL:
                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
                startVideoCall();
                break;
            //red packet code : 进入发红包页面
            case ITEM_RED_PACKET:
                //注意：不再支持原有的startActivityForResult进入红包相关页面
                int itemType;
                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                    itemType = RPConstant.RP_ITEM_TYPE_SINGLE;
                    //小额随机红包
                    //itemType = RPConstant.RP_ITEM_TYPE_RANDOM;
                } else {
                    itemType = RPConstant.RP_ITEM_TYPE_GROUP;
                }
                MyRedPackageUtil.startRedPacket(this, chatType, toChatUsername, REQUEST_CODE_SEND_RED_PACKET);
                break;
            //end of red packet code
            case ITEM_TRANSFER://转账
                String goalid = toChatUsername.replace("ixn", "");
                intent = new Intent(getContext(), TransferActivity.class).putExtra(XNConstants.goalid, goalid);
                startActivityForResult(intent, REQUEST_CODE_TRANSFER);
                break;
            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // voiceCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // videoCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * 群名称发生改变
     *
     * @param groupno
     * @param groupName
     */
    @Override
    public void onNameChange(String groupno, String groupName) {
        if (chatType == CHATTYPE_GROUP) {
            titleBar.setTitle(groupName);
        }

    }

    //表情变化
    public void refreshEmotion() {
        if (emojiconGroupList == null) {
            emojiconGroupList = new ArrayList<EaseEmojiconGroupEntity>();
            //            emojiconGroupList.add(new EaseEmojiconGroupEntity(R.drawable.ee_1, Arrays.asList(EaseDefaultEmojiconDatas.getData())));
        }
        //添加默认的emoji表情包
        emojiconMenu.init(emojiconGroupList);
        //若有收藏的表情则添加到表情包菜单
        EaseEmojiconGroupEntity collectEmGroupEntity = EmotionManager.getInstance().getCollectEmGroupEntity();
        if (collectEmGroupEntity != null && !ListUtil.isEmptyList(collectEmGroupEntity.getEmojiconList())) {
            emojiconMenu.addEmojiconGroup(collectEmGroupEntity);
        }
        //若有已购买的表情则添加到表情包菜单
        List<EaseEmojiconGroupEntity> emojiconGroups = EmotionManager.getInstance().getEmojiconGroups();
        if (!ListUtil.isEmptyList(emojiconGroups)) {
            emojiconMenu.addEmojiconGroup1(emojiconGroups);
        }
    }

    @Override
    public void emotionChange() {
        refreshEmotion();
    }

    @Override
    public void onMessage(String string) {
        if (chatType == CHATTYPE_GROUP) {
            sendTextMessage(string);
        }
    }

    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 12;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                //red packet code : 红包消息、红包回执消息以及转账消息的chatrow type
                else if (RedPacketUtil.isRandomRedPacket(message)) {
                    //小额随机红包
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RANDOM : MESSAGE_TYPE_SEND_RANDOM;
                } else if (RedPacketUtil.isRPMessage(message)) {
                    //发送红包消息
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RED_PACKET : MESSAGE_TYPE_SEND_RED_PACKET;
                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
                    //领取红包消息
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RED_PACKET_ACK : MESSAGE_TYPE_SEND_RED_PACKET_ACK;
                } else if (TransferUtil.isTransferMsg(message)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TRANSFER : MESSAGE_TYPE_SEND_TRANSFER;
                }
                //end of red packet code
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                // voice call or video call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                        message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    return new ChatRowVoiceCall(getActivity(), message, position, adapter);
                }
                //red packet code : 红包消息、红包回执消息以及转账消息的chat row
                else if (RedPacketUtil.isRandomRedPacket(message)) {//小额随机红包
                    return new ChatRowRandomPacket(getActivity(), message, position, adapter);
                } else if (RedPacketUtil.isRPMessage(message)) {//红包消息
                    return new ChatRowRedPacket(getActivity(), message, position, adapter);
                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {//红包回执消息
                    return new ChatRowRedPacketAck(getActivity(), message, position, adapter);
                } else if (TransferUtil.isTransferMsg(message)) {
                    return new ChatRowTransfer(getActivity(), message, position, adapter);
                }
                //end of red packet code
            }
            return null;
        }
    }
    //    diction[@"moneynum"] = self.transferTextField.text;//总钱数
    //    diction[@"Transfer"] = @"Transfer";
    //    diction[@"TransferType"] = @"0";//0.转账1.收钱
    //    diction[@"message"] = @"";
    //    diction[@"TransferId"]=luckmoneyid;//红包id


    @Override
    public void onDestroy() {
        super.onDestroy();
        //调用该方法可防止红包SDK引起的内存泄漏
        RPRedPacketUtil.getInstance().detachView();
    }
}
