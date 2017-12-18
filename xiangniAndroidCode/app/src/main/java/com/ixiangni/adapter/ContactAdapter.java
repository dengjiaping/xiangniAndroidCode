package com.ixiangni.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.easeui.domain.EaseUser;
import com.ixiangni.app.R;
import com.ixiangni.app.user.PersonPageActivity;
import com.ixiangni.bean.FriendListBean;
import com.ixiangni.util.GlideUtil;
import com.ixiangni.util.HuanXinHelper;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class ContactAdapter extends QuickAdapter<FriendListBean.DataBean> {

    @Override
    public void addAll(List<FriendListBean.DataBean> elem) {
        Collections.sort(elem, new NickCompator());
        super.addAll(elem);
    }

    @Override
    public void replaceAll(List<FriendListBean.DataBean> elem) {
        this.data.clear();
        Collections.sort(elem, new NickCompator());
        this.data.addAll(elem);
        this.notifyDataSetChanged();
    }

    public ContactAdapter(Context context) {
        super(context, R.layout.item_contact_list);
    }

    public int getLettrePoi(String s) {
        for (int i = 0; i < data.size(); i++) {
            FriendListBean.DataBean dataBean = data.get(i);
            if (s.equals(dataBean.getFirstLetter())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, FriendListBean.DataBean dataBean) {

        int position = helper.getPosition();
        TextView tvLetter = helper.getView(R.id.tv_letter);

        String firstLetter = dataBean.getFirstLetter();
        if (position - 1 >= 0 && data.get(position - 1).getFirstLetter().equals(firstLetter)) {
            tvLetter.setVisibility(View.GONE);
        } else {
            tvLetter.setText(firstLetter);
        }
        helper.setOnClickListener(R.id.rl_chat_user, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChatActivity.startChat(context, HuanXinHelper.getHuanXinidbyUseid(dataBean.getUserid()+""),1);
                PersonPageActivity.start(context, dataBean.getUserid() + "", dataBean.getUsernick(), dataBean.getUserpic());
            }
        });

        String userpic = dataBean.getUserpic();
        String usernick = dataBean.getUsernick();
        int userid = dataBean.getUserid();
        String huanxinid = HuanXinHelper.getHuanXinidbyUseid(userid + "");
        EaseUser user = new EaseUser(huanxinid);
        user.setAvatar(userpic);

        String remind = dataBean.getRemind();

        if (CommonUtils.isStringNull(remind)) {

            user.setNickname(usernick);
            helper.setText(R.id.tv_user_nick, usernick);
        } else {

            helper.setText(R.id.tv_user_nick, remind);
            user.setNickname(CommonUtils.getMaskRemindName(remind));
        }

        DemoHelper.getInstance().saveContact(user);

        ImageView iv = helper.getView(R.id.iv_icon);
        GlideUtil.loadRoundImage(context, userpic, iv, R.mipmap.touxiangmoren);

    }
}
