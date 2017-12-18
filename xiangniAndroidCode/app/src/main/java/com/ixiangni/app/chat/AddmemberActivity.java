package com.ixiangni.app.chat;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.ixiangni.adapter.NickCompator;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.bean.FriendListBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.interfaces.OnGroupMemberChang;
import com.ixiangni.interfaces.OnSendMessage;
import com.ixiangni.presenters.contract.MyPresenter;
import com.ixiangni.ui.TopBar;
import com.ixiangni.util.GlideUtil;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 添加群成员
 *
 * @ClassName:AddmemberActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/7/6 0006   15:57
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/6 0006 handongkeji All rights reserved.
 */
public class AddmemberActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.tv_no_data_message)
    TextView tvNoDataMessage;
    @Bind(R.id.rl_no_data)
    RelativeLayout rlNoData;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private SelectMemberAdapter memberAdapter;
    private List<Integer> alardyMemberList;
    private String groupno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmember);
        ButterKnife.bind(this);


        tvNoDataMessage.setText("没有好友可以邀请了");
        memberAdapter = new SelectMemberAdapter(this);
        listView.setAdapter(memberAdapter);
        memberAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (memberAdapter.getCount() > 0) {
                    rlNoData.setVisibility(View.GONE);
                    log("shuju" + memberAdapter.getCount());
                } else {
                    log("shuju" + memberAdapter.getCount());
                    rlNoData.setVisibility(View.VISIBLE);
                }
            }
        });

        String stringExtra = getIntent().getStringExtra(XNConstants.memberids);
        String[] split = stringExtra.split(",");

        alardyMemberList = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            alardyMemberList.add(Integer.parseInt(split[i]));
        }


        //群号
        groupno = getIntent().getStringExtra(XNConstants.groupchatno);

        topBar.setOnRightClickListener(this);

        getFriendList();
    }

    @Override
    public void onClick(View v) {
        addMember();
    }

    private void addMember() {
        List<Integer> selectedMemberids = memberAdapter.getSelectedMemberids();
        if (selectedMemberids.size() == 0) {
            toast("您还未选择任何好友！");
        } else {
            Intent intent = new Intent();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < selectedMemberids.size(); i++) {
                Integer integer = selectedMemberids.get(i);

                if (i < selectedMemberids.size() - 1) {
                    sb.append(String.valueOf(integer)).append(",");
                } else {
                    sb.append(String.valueOf(integer));
                }
            }

            String ids = sb.toString();
            intent.putExtra(XNConstants.memberids, ids);

            showProgressDialog("添加中...", false);

            mProgressDialog.setCanceledOnTouchOutside(false);
            //添加到想你服务器
            HashMap<String, String> params = new HashMap<>();
            params.put(Constants.token,MyApp.getInstance().getUserTicket());
            params.put(XNConstants.groupchatno, groupno);
            params.put(XNConstants.memberids, ids);
            log(params.toString());
            MyPresenter.request(this, UrlString.URL_ADD_MEMBER_TO_GROUP, params, new OnResult<String>() {
                @Override
                public void onSuccess(String s) {
                    if(tvNoDataMessage==null){
                        return;
                    }
                    SuperObservableManager.getInstance().getObservable(OnGroupMemberChang.class)
                            .notifyMethod(OnGroupMemberChang::onMemberChange);

                    List<String> selectedNames = memberAdapter.getSelectedNames();
                    if(selectedNames.size()>0){
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < selectedNames.size(); i++) {
                            sb.append(selectedNames.get(i));
                            if(i!=selectedNames.size()-1){
                                sb.append("、");
                            }
                        }
                        //提示邀请入群
                        String invitemessage = sb.toString()+"已被邀请进入该群.";
                        SuperObservableManager.notify(OnSendMessage.class,onSendMessage -> onSendMessage.onMessage(invitemessage));


                        //环信id数组
                        List<String> filterList = ListUtil.getFilterList(selectedMemberids, new ListUtil.OnGetResult<Integer, String>() {
                            @Override
                            public String filter(Integer integer) {
                                return "ixn" + integer;
                            }
                        });

                        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupno);
                        if (group != null) {
                            String owner = group.getOwner();

                            Observable.create(new Observable.OnSubscribe<Object>() {
                                @Override
                                public void call(Subscriber<? super Object> subscriber) {
                                    try {
                                        if (owner.equals(EMClient.getInstance().getCurrentUser())) {//群主加人

                                            EMClient.getInstance().groupManager().addUsersToGroup(groupno, filterList.toArray(new String[filterList.size()]));
                                        } else {
                                            EMClient.getInstance().groupManager().inviteUser(groupno, filterList.toArray(new String[filterList.size()]), null);
                                        }
                                        subscriber.onCompleted();
                                    } catch (HyphenateException e) {
                                        e.printStackTrace();
                                        subscriber.onError(e);
                                    }
                                }
                            }).subscribeOn(Schedulers.from(RemoteDataHandler.threadPool))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<Object>() {
                                        @Override
                                        public void onCompleted() {
                                            if(tvNoDataMessage==null){
                                                return;
                                            }
                                            toast("邀请成功");
                                            sendBroadcast(new Intent(XNConstants.groupchatno));
                                            dismissProgressDialog();
                                            onBackPressed();

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            if(tvNoDataMessage==null){
                                                return;
                                            }
                                            toast(e.getMessage());
                                            dismissProgressDialog();
                                            log(e.getMessage());
                                        }


                                        @Override
                                        public void onNext(Object o) {

                                        }
                                    });
                        }else {
                            toast("群失效");
                        }
                    }

                }

                @Override
                public void onFailed(String errorMsg) {
                    if(tvNoDataMessage==null){
                        return;
                    }
                    dismissProgressDialog();
                    toast(errorMsg);

                }
            });


        }
    }

    /**
     * 添加成员到想你服务器的群
     *
     * @param
     *//*
    private void saveMembers(String ids) {

        HashMap<String, String> params = new HashMap<>();
        params.put(XNConstants.groupchatno, groupno);
        params.put(XNConstants.memberids, ids);
        MyPresenter.request(this, UrlString.URL_ADD_MEMBER_TO_GROUP, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                if(tvNoDataMessage==null){
                    return;
                }
                dismissProgressDialog();
                SuperObservableManager.getInstance().getObservable(OnGroupMemberChang.class)
                        .notifyMethod(OnGroupMemberChang::onMemberChange);

                List<String> selectedNames = memberAdapter.getSelectedNames();
                if(selectedNames.size()>0){
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selectedNames.size(); i++) {
                        sb.append(selectedNames.get(i));
                        if(i!=selectedNames.size()-1){
                            sb.append("、");
                        }
                    }
                    //提示邀请入群
                    String invitemessage = sb.toString()+"已被邀请进入该群.";
                    SuperObservableManager.notify(OnSendMessage.class,onSendMessage -> onSendMessage.onMessage(invitemessage));
                }

                finish();
            }

            @Override
            public void onFailed(String errorMsg) {
                if(tvNoDataMessage==null){
                    return;
                }
                dismissProgressDialog();
                toast(errorMsg);

            }
        });

    }*/


    private class SelectMemberAdapter extends QuickAdapter<FriendListBean.DataBean> {

        @Override
        public void addAll(List<FriendListBean.DataBean> elem) {
            Collections.sort(elem, new NickCompator());
            super.addAll(elem);
        }

        private List<Integer> selectedList = new ArrayList<>();

        private List<String> selectedNames = new ArrayList<>();


        public SelectMemberAdapter(Context context) {
            super(context, R.layout.item_contact_list);
        }

        public List<Integer> getSelectedMemberids() {
            return selectedList;
        }

        public List<String> getSelectedNames() {
            return selectedNames;
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

            View viewSelect = helper.getView(R.id.iv_select);
            Integer userid = dataBean.getUserid();
            String usernick = dataBean.getUsernick();


            if (selectedList.contains(userid)) {
                viewSelect.setVisibility(View.VISIBLE);
            } else {
                viewSelect.setVisibility(View.GONE);
            }
            helper.setOnClickListener(R.id.rl_chat_user, v -> {
                if (selectedList.contains(userid)) {
                    selectedList.remove(userid);
                } else {
                    selectedList.add(userid);
                }

                if(selectedNames.contains(usernick)){
                    selectedNames.remove(usernick);
                }else {
                    selectedNames.add(usernick);
                }

                notifyDataSetChanged();
            });
            String userpic = dataBean.getUserpic();
            ImageView iv = helper.getView(R.id.iv_icon);
            GlideUtil.loadRoundImage(context, userpic, iv, R.mipmap.touxiangmoren);
            helper.setText(R.id.tv_user_nick, usernick);
        }
    }


    private void getFriendList() {
        if (!LoginHelper.getInstance().isLogin(this)) {
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());


        progressBar.setVisibility(View.VISIBLE);
        RemoteDataHandler.asyncPost(UrlString.URL_FRIEND_LIST, params, this, true, callback -> {

            if (listView != null) {
                progressBar.setVisibility(View.GONE);
                String json = callback.getJson();
                if (!TextUtils.isEmpty(json)) {
                    log("好友列表：" + json);
                    FriendListBean friendListBean = new Gson().fromJson(json, FriendListBean.class);
                    if (1 == friendListBean.getStatus()) {
                        List<FriendListBean.DataBean> data = friendListBean.getData();

                        List<FriendListBean.DataBean> mathcList = ListUtil.getMathcList(data, new ListUtil.OnMatch<FriendListBean.DataBean>() {
                            @Override
                            public boolean match(FriendListBean.DataBean dataBean) {
                                if (alardyMemberList.contains(dataBean.getUserid())) {

                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        });

                        memberAdapter.addAll(mathcList);
                    }

                }

            }

        });


    }
}
