package com.ixiangni.app.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.homepage.MessageListFragment;
import com.ixiangni.bean.GroupInfoBean;
import com.ixiangni.bean.GroupMLBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.interfaces.OnGroupDelete;
import com.ixiangni.interfaces.OnGroupNameChange;
import com.ixiangni.presenters.GroupSetPresenter;
import com.ixiangni.presenters.contract.GroupMemberPresenter;
import com.ixiangni.presenters.contract.MyPresenter;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 群设置
 *
 * @ClassName:GroupSettingActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/6/20 0020   11:42
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/20 0020 handongkeji All rights reserved.
 */
public class GroupSettingActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.tv_member_count)
    TextView tvMemberCount;
    @Bind(R.id.tv_group_name)
    TextView tvGroupName;
    @Bind(R.id.tv_group_owner)
    TextView tvGroupOwner;
    @Bind(R.id.tv_group_profile)
    TextView tvGroupProfile;
    @Bind(R.id.cb_allow_enter)
    CheckBox cbAllowEnter;
    @Bind(R.id.rl_allow_enter)
    RelativeLayout rlAllowEnter;
    @Bind(R.id.cb_silence)
    CheckBox cbSilence;
    @Bind(R.id.btn_delete)
    Button btnDelete;
    private String groupno;


    private final int REQUEST_CODE_GROUP_NAME = 100;
    private final int REQUEST_CODE_GROUP_PROFILE = 101;
    private String owner = "";
    private EMGroup group;
    private GroupSetPresenter mPresenter;
    private GroupMemberPresenter groupMemberPresenter;
    private int admit = -1;
    private DataReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_setting);
        ButterKnife.bind(this);

        SuperObservableManager.getInstance().getObservable(OnGroupNameChange.class)
                .registerObserver(groupNameChange);
        groupMemberPresenter = new GroupMemberPresenter();

        tvMemberCount.setEnabled(false);
        initData();

        getGroupInfo(groupno);

        receiver = new DataReceiver();
        IntentFilter intentFilter = new IntentFilter(XNConstants.groupchatno);
        registerReceiver(receiver,intentFilter);

    }

    //查询群昵称和头像并保存
    private void getGroupInfo(String groupchatno) {
        showProgressDialog("加载中...",false);
        HashMap<String, String> params = new HashMap<>();
        params.put("groupchatno",groupchatno);

        log(groupchatno);
        RemoteDataHandler.asyncPost(UrlString.URL_GET_GROUP_INFO,params,this,false, response -> {

            if(tvMemberCount==null){
                return;
            }
            dismissProgressDialog();
            String json = response.getJson();
            log("群信息"+json);
            if(!CommonUtils.isStringNull(json)){
                GroupInfoBean groupInfoBean = new Gson().fromJson(json, GroupInfoBean.class);
                if(1==groupInfoBean.getStatus()){
                    GroupInfoBean.DataBean data = groupInfoBean.getData();

                    String groupchatno1 = data.getGroupchatno();
                    String groupchatname = data.getGroupchatname();

                    tvGroupName.setText(groupchatname);
                    tvGroupProfile.setText(data.getGroupchatdesc());
                    //群主名称
                    tvGroupOwner.setText(owner);

//                    NotifyHelper.notifyGroupChange(groupno,groupchatname);

                    String groupchatpic = data.getGroupchatpic();
                    EaseUser group = new EaseUser(groupchatno1);
                    group.setAvatar(groupchatpic);
                    group.setNickname(groupchatname);
                    DemoHelper.getInstance().saveContact(group);
                    if(MessageListFragment.instance!=null){
                        MessageListFragment.instance.refresh();
                    }

                }
            }else {
                toast(Constants.CONNECT_SERVER_FAILED);
            }


        });

    }

    private class DataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case XNConstants.groupchatno:
                    getGroupMemberCount();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        SuperObservableManager.getInstance().getObservable(OnGroupNameChange.class)
                .unregisterObserver(groupNameChange);
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private OnGroupNameChange groupNameChange = new OnGroupNameChange() {
        @Override
        public void onNameChange(String groupno, String groupName) {
            group = EMClient.getInstance().groupManager().getGroup(groupno);
            if (group != null) {

                showGroupCount();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getGroupMemberCount();
    }

    private void initData() {
        groupno = getIntent().getStringExtra(XNConstants.groupchatno);
        group = EMClient.getInstance().groupManager().getGroup(groupno);

        mPresenter = new GroupSetPresenter();
        if (group != null) {


            showGroupCount();

            //群名称
//            tvGroupName.setText(group.getGroupName());
            //群简介
//            tvGroupProfile.setText(group.getDescription());

            //消息免打扰
            boolean msgBlocked = group.isMsgBlocked();
            cbSilence.setChecked(msgBlocked);
            cbSilence.setOnCheckedChangeListener(this);

            //只有群主显示 群名称 群简介 允许加入
            owner = group.getOwner();
            //群主不能修改消息免打扰
            if (owner.equals(EMClient.getInstance().getCurrentUser())) {
                cbSilence.setEnabled(false);
            }

            //允许邀请人
            boolean memberAllowToInvite = group.isMemberAllowToInvite();




            if (!TextUtils.isEmpty(owner) && owner.equals(EMClient.getInstance().getCurrentUser())) {
                initFunction();
            } else {
                hideSomeFunction(group.getDescription());
            }

        } else {
            toast("未找到该群");
        }

    }

    private void showGroupCount() {

        getGroupMemberCount();

    }

    private void getGroupMemberCount() {
        groupMemberPresenter.getMembers(this, groupno, new OnResult<List<GroupMLBean.DataBean>>() {
            @Override
            public void onSuccess(List<GroupMLBean.DataBean> dataBeen) {
                if (btnDelete != null) {
                    dismissProgressDialog();
                    //群成员数量
                    int memberCount = 0;
                    if (!ListUtil.isEmptyList(dataBeen)) {
                        memberCount = dataBeen.size();
                        GroupMLBean.DataBean dataBean = dataBeen.get(0);
                        admit = dataBean.getAdmit();
                        cbAllowEnter.setChecked(admit ==0);
                        cbAllowEnter.setOnCheckedChangeListener(GroupSettingActivity.this);

                        tvMemberCount.setEnabled(true);
                    }

                    String mc = "群成员(%d)";
                    tvMemberCount.setText(String.format(Locale.CHINA,mc, memberCount));

                }
            }

            @Override
            public void onFailed(String errorMsg) {
                if (btnDelete != null) {

                    toast(errorMsg);
                }
            }
        });
    }

    private void initFunction() {
        tvGroupName.setOnClickListener(this);
        tvGroupProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_group_name://群名称
                GroupNameActivity.startForResult(this, groupno, REQUEST_CODE_GROUP_NAME);
                break;
            case R.id.tv_group_profile://群简介
                GroupProfileActivity.startForResult(this, groupno, REQUEST_CODE_GROUP_PROFILE);
                break;
        }
    }

    //删除群token
//    groupchatno

    private void deleteGroup() {


        showProgressDialog("删除中...", false);

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                EMGroup group = EMClient.getInstance().groupManager().getGroup(groupno);
                if (group != null) {
                    String owner = group.getOwner();
                    if (owner.equals(EMClient.getInstance().getCurrentUser())) {//群主解散群

                        try {
                            EMClient.getInstance().groupManager().destroyGroup(groupno);
                            subscriber.onCompleted();
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    } else {
                        try {
                            EMClient.getInstance().groupManager().leaveGroup(groupno);
                            subscriber.onCompleted();
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }
                } else {//群成员退出群

                    subscriber.onError(new Throwable("未找到该群"));
                }
            }
        }).subscribeOn(Schedulers.from(RemoteDataHandler.threadPool))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        deleteFromServer();
                    }

                    @Override
                    public void onError(Throwable e) {

                        dismissProgressDialog();
                        toast(e.getMessage());
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });

    }

    private void deleteFromServer() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put(XNConstants.groupchatno, groupno);
        MyPresenter.request(this, UrlString.URL_DELETE_GROUP, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                dismissProgressDialog();
                toast("删除成功！");
                SuperObservableManager.getInstance().getObservable(OnGroupDelete.class).notifyMethod(OnGroupDelete::onDelete);

                //删除会话
                try {
                EMClient.getInstance().chatManager().deleteConversation(groupno,false);

                }catch (Exception e){
                    e.printStackTrace();
                }

                finish();
            }

            @Override
            public void onFailed(String errorMsg) {
                toast(errorMsg);
                dismissProgressDialog();
            }
        });


    }

    private void hideSomeFunction(String description) {
        tvGroupName.setCompoundDrawables(null, null, null, null);
        tvGroupProfile.setCompoundDrawables(null, null, null, null);
        rlAllowEnter.setVisibility(View.GONE);
        tvGroupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GroupSettingActivity.this,QunjianjieActivity.class);
                intent.putExtra(XNConstants.groupdescirbe,description);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_GROUP_NAME://群名称

                    tvGroupName.setText("群名称 " + data.getStringExtra(XNConstants.groupname));

                    break;
                case REQUEST_CODE_GROUP_PROFILE://群简介
                    tvGroupProfile.setText("群简介 " + data.getStringExtra(XNConstants.groupdescirbe));

                    break;

            }


        }
    }

    /**
     * 打开群设置
     *
     * @param context
     * @param groupno 环信群号
     */
    public static void start(Context context, String groupno) {

        Intent intent = new Intent(context, GroupSettingActivity.class);
        intent.putExtra(XNConstants.groupchatno, groupno);
        context.startActivity(intent);
    }


    @OnClick({R.id.tv_member_count, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_member_count:
                Intent intent = new Intent(this, GroupMembersActivity.class);
                intent.putExtra(XNConstants.alown_invite,admit);
                intent.putExtra(XNConstants.groupchatno, groupno);
                startActivity(intent);

                break;
            case R.id.btn_delete:
                MyAlertdialog alertdialog = new MyAlertdialog(this);

                String msg = owner.equals(EMClient.getInstance().getCurrentUser()) ? "您确定要解散该群？" : "退出后将不再接收此群的消息";
                alertdialog.setMessage(msg)
                        .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                            @Override
                            public void onLeftClick(View view) {

                            }

                            @Override
                            public void onRightClick(View view) {

                                deleteGroup();
                            }
                        });
                alertdialog.show();
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_allow_enter://允许加人，只有群主才能设置
                boolean checked = cbAllowEnter.isChecked();


                showProgressDialog("设置中...",true);
                mPresenter.modify(this, new OnResult<String>() {
                        @Override
                        public void onSuccess(String s) {

                            if(cbAllowEnter!=null){
                                dismissProgressDialog();
                            toast("设置成功");

                                admit=checked?0:1;
                            }
                        }

                        @Override
                        public void onFailed(String errorMsg) {
                            if(cbAllowEnter!=null){
                                dismissProgressDialog();
                                toast(errorMsg);

                            }
                        }
                    },null,null,null,null,checked?"0":"1");


                break;
            case R.id.cb_silence:

                changeDisturb();
                break;
        }

    }

    //修改消息免打扰
    private void changeDisturb() {

        showProgressDialog("设置中...", false);
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                EMGroupManager groupManager = EMClient.getInstance().groupManager();
                boolean checked = cbSilence.isChecked();
                String distrub = checked ? "1" : "0";
                if (checked) {//消息免打扰
                    try {
                        groupManager.blockGroupMessage(groupno);
                        subscriber.onNext(distrub);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                } else {
                    try {
                        groupManager.unblockGroupMessage(groupno);
                        subscriber.onNext(distrub);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        subscriber.onError(e);

                    }
                }
            }
        }).subscribeOn(Schedulers.from(RemoteDataHandler.threadPool))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        dismissProgressDialog();
                        toast(e.getMessage());

                    }

                    @Override
                    public void onNext(String s) {

                        saveDistrub(s);
                    }
                });

    }

    //修改服务器上消息免打扰状态
    private void saveDistrub(String s) {
        mPresenter.modify(this, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                dismissProgressDialog();
                toast("设置成功！");
            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();
                toast(errorMsg);

            }
        }, groupno, null, null, s, null);
    }


}
