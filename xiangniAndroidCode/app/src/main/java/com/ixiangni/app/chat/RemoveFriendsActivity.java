package com.ixiangni.app.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.bean.GroupMLBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.interfaces.OnGroupMemberChang;
import com.ixiangni.interfaces.OnGroupNameChange;
import com.ixiangni.presenters.contract.GroupMemberPresenter;
import com.ixiangni.presenters.contract.MyPresenter;
import com.ixiangni.ui.TopBar;
import com.ixiangni.util.GlideUtil;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nevermore.oceans.ob.SuperObservable;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 移除好友
 *
 * @ClassName:RemoveFriendsActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/6/19 0019   13:58
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/19 0019 handongkeji All rights reserved.
 */
public class RemoveFriendsActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.top_bar)
    TopBar topBar;
    private String groupno;
    private GroupMemberPresenter memberPresenter;
    private MemberAdapter memberAdapter;
    private int completecount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_friends);
        ButterKnife.bind(this);

        groupno = getIntent().getStringExtra(XNConstants.groupchatno);


        topBar.setOnRightClickListener(this);
        memberAdapter = new MemberAdapter(this);
        listView.setAdapter(memberAdapter);

        memberPresenter = new GroupMemberPresenter();
        initData();

    }

    @Override
    public void onClick(View v) {
        //移除好友
        List<Integer> selectedList = memberAdapter.getSelectedList();
        if(selectedList.size()==0){
            toast("您还有选择任何要移除的成员！");
        }else {

            HashMap<String, String> params = new HashMap<>();
            params.put("token", MyApp.getInstance().getUserTicket());
            params.put(XNConstants.groupchatno,groupno);
            StringBuilder sb = new StringBuilder();
            //拼接id字符串
            for (int i = 0; i < selectedList.size(); i++) {
                Integer integer = selectedList.get(i);
                if(i!=selectedList.size()-1){
                    sb.append(integer.intValue()).append(",");

                }else {
                    sb.append(integer.intValue());
                }

            }
            log(sb.toString());
            params.put(XNConstants.memberids,sb.toString());


            showProgressDialog("移除中...",false);
            MyPresenter.request(this, UrlString.URL_DELETE_GROUPMEMBER, params, new OnResult<String>() {
                @Override
                public void onSuccess(String s) {
                    deleteFromHuanxin(selectedList);
                }

                @Override
                public void onFailed(String errorMsg) {

                    dismissProgressDialog();
                    toast(errorMsg);
                }
            });


        }
    }

    /**
     * 从环信删除群成员
     * @param selectedList
     */
    private void deleteFromHuanxin(List<Integer> selectedList) {
        completecount = 0;
        Observable.from(selectedList)
                .flatMap(new Func1<Integer, Observable<String>>() {

                    @Override
                    public Observable<String> call(Integer integer) {
                        String huanxinid = "ixn" + integer.toString();
                        return Observable.just(huanxinid);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                try {
                                    EMClient.getInstance().groupManager().removeUserFromGroup(groupno,s);
                                    subscriber.onNext(s);
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                    subscriber.onError(e);
                                }

                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String o) {
                                        completecount++;
                                        if(completecount==selectedList.size()){
                                            dismissProgressDialog();
                                            setResult(RESULT_OK,null);
                                            SuperObservableManager.getInstance()
                                                    .getObservable(OnGroupMemberChang.class)
                                                    .notifyMethod(OnGroupMemberChang::onMemberChange);
                                            finish();
                                        }
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {

                                        dismissProgressDialog();
                                        toast(throwable.getMessage());
                                    }
                                });

                    }
                });

    }

    private class MemberAdapter extends QuickAdapter<GroupMLBean.DataBean> {

        private List<Integer> selectedList = new ArrayList<>();

        public MemberAdapter(Context context) {
            super(context, R.layout.item_contact);
        }


        public List<Integer> getSelectedList(){
            return selectedList;
        }
        @Override
        protected void convert(BaseAdapterHelper helper, GroupMLBean.DataBean dataBean) {

            helper.setText(R.id.tv_user_nick, dataBean.getUsernick());
            ImageView ivIcon = helper.getView(R.id.iv_icon);
            GlideUtil.loadRoundImage(context, dataBean.getUserpic(), ivIcon, R.mipmap.touxiangmoren);

            ImageView ivSelect = helper.getView(R.id.iv_select);
            Integer userid = dataBean.getUserid();
            ivSelect.setVisibility(selectedList.contains(userid) ? View.VISIBLE : View.GONE);


            helper.setOnClickListener(R.id.rl_chat_user, v -> {

                if (selectedList.contains(userid)) {
                    ivSelect.setVisibility(View.GONE);
                    selectedList.remove(userid);

                } else {
                    ivSelect.setVisibility(View.VISIBLE);
                    selectedList.add(userid);
                }

            });


        }
    }

    private void initData() {
        showProgressDialog(Constants.MESSAGE_LOADING, true);
        memberPresenter.getMembers(this, groupno, new OnResult<List<GroupMLBean.DataBean>>() {
            @Override
            public void onSuccess(List<GroupMLBean.DataBean> dataBeen) {
                if (listView != null) {
                    dismissProgressDialog();



                    String currentUserid = LoginHelper.getInstance().getUserid(RemoveFriendsActivity.this);
                    Observable.from(dataBeen)
                            .filter(new Func1<GroupMLBean.DataBean, Boolean>() {
                                @Override
                                public Boolean call(GroupMLBean.DataBean dataBean) {
                                    int userid = dataBean.getUserid();
                                    if (currentUserid.equals(userid+"")) {
                                        return false;
                                    }
                                    return true;
                                }
                            })
                            .toList()
                            .subscribeOn(Schedulers.from(RemoteDataHandler.threadPool))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<List<GroupMLBean.DataBean>>() {
                                @Override
                                public void call(List<GroupMLBean.DataBean> dataBeen) {

                                    memberAdapter.replaceAll(dataBeen);
                                }
                            });

                }

            }

            @Override
            public void onFailed(String errorMsg) {

                if (listView != null) {
                    dismissProgressDialog();
                    toast(errorMsg);

                }
            }
        });
    }
}
