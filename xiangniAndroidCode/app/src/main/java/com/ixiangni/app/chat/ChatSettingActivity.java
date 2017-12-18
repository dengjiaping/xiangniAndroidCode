package com.ixiangni.app.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.DemoModel;
import com.hyphenate.chatuidemo.ui.ChatFragment;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.homepage.MessageListFragment;
import com.ixiangni.bean.PersonBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.util.GlideUtil;
import com.ixiangni.util.HuanXinHelper;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 聊天设置
 *
 * @ClassName:ChatSettingActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/6/19 0019   14:01
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/19 0019 handongkeji All rights reserved.
 */
public class ChatSettingActivity extends BaseActivity {

    @Bind(R.id.iv_icon)
    ImageView ivIcon;
    @Bind(R.id.tv_mark_name)
    TextView tvMarkName;
    @Bind(R.id.tv_sign)
    TextView tvSign;
    @Bind(R.id.tv_empty_history)
    TextView tvEmptyHistory;

    @Bind(R.id.cb_msg_disable)
    CheckBox cbMsgDisable;
    @Bind(R.id.cb_set_message_top)
    CheckBox cbSetMessageTop;
    private String userid;
    private MyAlertdialog alertdialog;
    private DemoModel model;
    private String hxid;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_setting);
        ButterKnife.bind(this);
        initClearHistoryDialog();
        userid = getIntent().getStringExtra(XNConstants.USERID);
        getSimpleUserInfo();

        initUI();
    }

    private void initUI() {
        model = DemoHelper.getInstance().getModel();
        List<String> disabledIds = model.getDisabledIds();
        hxid = HuanXinHelper.getHuanXinidbyUseid(userid);


        cbMsgDisable.setChecked(disabledIds.contains(hxid));


        cbMsgDisable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbMsgDisable.isChecked()) {
                    model.addDisableId(hxid);
                } else {
                    model.deleteDisableId(hxid);
                }
                toast("设置成功");
            }
        });

        //消息置顶
        sharedPreferences = getSharedPreferences(EaseConstant.SET_MESSAGE_TOP, MODE_PRIVATE);
        cbSetMessageTop.setChecked(sharedPreferences.getBoolean(hxid,false));
        cbSetMessageTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean checked = cbSetMessageTop.isChecked();
                sharedPreferences.edit().putBoolean(hxid,checked).commit();
                if(MessageListFragment.instance!=null){
                    MessageListFragment.instance.refresh();
                }
                toast("设置成功");

            }
        });
    }

    private void initClearHistoryDialog() {

        alertdialog = new MyAlertdialog(this);
        alertdialog.setMessage("是否清空所有聊天记录？");
        alertdialog.setMyClickListener(new MyAlertdialog.OnMyClickListener() {
            @Override
            public void onLeftClick(View view) {

            }

            @Override
            public void onRightClick(View view) {

                setResult(RESULT_OK);
            }
        });

    }

    private void getSimpleUserInfo() {

        showProgressDialog("加载中...", true);
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("goalid", userid);

        RemoteDataHandler.asyncPost(UrlString.URL_GET_OTHER_INFO, params, this, true, response -> {

            String json = response.getJson();
            log(json);
            if (tvMarkName != null) {
                dismissProgressDialog();
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    PersonBean personInfoBean = new Gson().fromJson(json, PersonBean.class);
                    if (1 == personInfoBean.getStatus()) {
                        setPersonInfo(personInfoBean.getData());

                    } else {
                        toast(personInfoBean.getMessage());
                    }
                }

            }
        });

    }

    private void setPersonInfo(PersonBean.DataBean data) {

        String usernick = data.getUsernick();
        String userpic = data.getUserpic();
        String charactersing = data.getCharactersing();

        tvMarkName.setText(usernick);
        GlideUtil.loadRoundImage(this, userpic, ivIcon, R.mipmap.touxiangmoren);
        tvSign.setText(CommonUtils.isStringNull(charactersing) ? "个性签名:未填写" : "个性签名:" + charactersing);
    }

    @OnClick(R.id.tv_empty_history)
    public void onViewClicked() {

        alertdialog.show();
    }
}
