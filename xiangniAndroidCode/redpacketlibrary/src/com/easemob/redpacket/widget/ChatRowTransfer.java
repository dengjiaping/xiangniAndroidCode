package com.easemob.redpacket.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easemob.redpacket.R;
import com.easemob.redpacket.utils.TransferConstance;
import com.easemob.redpacket.utils.TransferUtil;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class ChatRowTransfer extends EaseChatRow {

    private TextView mTvGreeting;
    private TextView mTvSponsorName;
    private TextView mTvPacketType;
    private TextView mMoneyCount;

    public ChatRowTransfer(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        if (TransferUtil.isTransferMsg(message)) {
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    R.layout.em_row_received_transfer : R.layout.em_row_sent_transfer, this);
        }
    }

    @Override
    protected void onFindViewById() {
        mTvGreeting = (TextView) findViewById(R.id.tv_money_greeting);
        mTvSponsorName = (TextView) findViewById(R.id.tv_sponsor_name);
        mTvPacketType = (TextView) findViewById(R.id.tv_packet_type);
        mMoneyCount = (TextView) findViewById(R.id.tv_money_count);

    }

    @Override
    protected void onSetUpView() {
        String sponsorName = message.getStringAttribute(RPConstant.EXTRA_SPONSOR_NAME, "想你转账");
        String greetings = message.getStringAttribute(TransferConstance.message, "想你转账");
        mTvGreeting.setText(greetings);
        mTvSponsorName.setText(sponsorName);

        String moneyCount = message.getStringAttribute(TransferConstance.moneynum, "0.0");
        mMoneyCount.setText("¥"+moneyCount);

        String packetType = message.getStringAttribute(RPConstant.MESSAGE_ATTR_RED_PACKET_TYPE, "");
//        if (!TextUtils.isEmpty(packetType) && TextUtils.equals(packetType, RPConstant.GROUP_RED_PACKET_TYPE_EXCLUSIVE)) {
//            mTvPacketType.setVisibility(VISIBLE);
            mTvPacketType.setText(R.string.msg_transfer);
//        } else {
//            mTvPacketType.setVisibility(GONE);
//        }
        handleTextMessage();
    }

    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    // 发送消息
                    break;
                case SUCCESS: // 发送成功
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL: // 发送失败
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS: // 发送中
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
    }
}
