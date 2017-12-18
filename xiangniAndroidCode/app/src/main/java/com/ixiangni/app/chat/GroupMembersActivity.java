package com.ixiangni.app.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.handongkeji.interfaces.OnResult;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.ixiangni.adapter.MyRvAdapter;
import com.ixiangni.adapter.MyRvViewHolder;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.app.user.PersonPageActivity;
import com.ixiangni.bean.GroupMLBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.interfaces.OnGroupMemberChang;
import com.ixiangni.presenters.contract.GroupMemberPresenter;
import com.ixiangni.ui.TopBar;
import com.ixiangni.util.GlideUtil;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 群成员
 *
 * @ClassName:GroupMembersActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/6/19 0019   11:38
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/19 0019 handongkeji All rights reserved.
 */
public class GroupMembersActivity extends BaseActivity {

    @Bind(R.id.tv_count)
    TextView tvCount;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.top_bar)
    TopBar topBar;
    private GroupMemberPresenter memberPresenter;
    private String groupno;
    private GroupMemberAdapter memberAdapter;

    private final int REQUEST_REMOVE_FRIEND_CODE = 100;
    private int alowinInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        ButterKnife.bind(this);

        groupno = getIntent().getStringExtra(XNConstants.groupchatno);

        alowinInvite = getIntent().getIntExtra(XNConstants.alown_invite, 0);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupno);
        if (group != null && group.getOwner().equals(EMClient.getInstance().getCurrentUser())) {
            topBar.setRightText("移除");
            topBar.setOnRightClickListener(v -> {

                if (memberAdapter.getItemCount() <= 2) {
                    toast("没有可以移除的好友！");
                } else {
                    Intent intent = new Intent(GroupMembersActivity.this, RemoveFriendsActivity.class);
                    intent.putExtra(XNConstants.groupchatno, groupno);
                    startActivity(intent);
                }


            });
        } else {
            topBar.setRightText("");
        }

        memberPresenter = new GroupMemberPresenter();

        initData();

        SuperObservableManager.
                getInstance()
                .getObservable(OnGroupMemberChang.class)
                .registerObserver(memberChang);
    }

    @Override
    protected void onDestroy() {
        SuperObservableManager.
                getInstance().getObservable(OnGroupMemberChang.class)
                .unregisterObserver(memberChang);
        super.onDestroy();
    }

    private OnGroupMemberChang memberChang = new OnGroupMemberChang() {
        @Override
        public void onMemberChange() {
            getMemberList();
        }
    };


    private class GroupMemberAdapter extends MyRvAdapter<GroupMLBean.DataBean> {

        private boolean reachToMax = false;

        @Override
        public void replaceAll(List<GroupMLBean.DataBean> newDataList) {
            //未达到上限
            if (newDataList != null && newDataList.size() < XNConstants.MAX_GROUP_MEMBER_COUNT) {
                initMemberids(newDataList);
                newDataList.add(new GroupMLBean.DataBean());
                reachToMax = false;
            } else {
                initMemberids(newDataList);
                reachToMax = true;
            }
            super.replaceAll(newDataList);


        }


        String memberis = null;

        private void initMemberids(List<GroupMLBean.DataBean> newDataList) {

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < newDataList.size(); i++) {
                GroupMLBean.DataBean dataBean = newDataList.get(i);
                String userid = String.valueOf(dataBean.getUserid());

                if (i < newDataList.size() - 1) {
                    sb.append(userid).append(",");
                } else {
                    sb.append(userid);
                }
            }
            memberis = sb.toString();
        }

        public GroupMemberAdapter(Context context) {
            super(context, R.layout.item_group_member);
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, GroupMLBean.DataBean data) {

            ImageView iv = holder.getView(R.id.iv_member_icon);
            if (!reachToMax && position == getItemCount() - 1) {
                iv.setImageResource(R.mipmap.add);
                holder.setText(R.id.tv_member_name, "");

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(alowinInvite==0){
                            Intent intent = new Intent(context, AddmemberActivity.class);
                            if (memberis != null) {
                                intent.putExtra(XNConstants.memberids, memberis);
                                intent.putExtra(XNConstants.groupchatno, groupno);
                            }
                            startActivity(intent);
                        }else {
                            toast("该群已设置不允许加人...");
                        }
                    }
                });
//                holder.setOnitemClickListener(v -> {
//
////                    EMGroup group = EMClient.getInstance().groupManager().getGroup(groupno);
////                    if (group == null) {
////                        return;
////                    } else {
////                        boolean memberAllowToInvite = group.isMemberAllowToInvite();
////                        if(!memberAllowToInvite){
////                            toast("该群已设置不允许邀请人");
////                            return;
////                        }
////                    }
//                    if(alowinInvite==0){
//                    Intent intent = new Intent(context, AddmemberActivity.class);
//                    if (memberis != null) {
//                        intent.putExtra(XNConstants.memberids, memberis);
//                        intent.putExtra(XNConstants.groupchatno, groupno);
//                    }
//                    startActivity(intent);
//                    }else {
//                        toast("该群已设置不允许加人...");
//                    }
//                });


            } else {
                GlideUtil.loadRoundImage(context, data.getUserpic(), iv, R.mipmap.touxiangmoren);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userid = data.getUserid() + "";
                        String curUserid = LoginHelper.getInstance().getUserid(context);
                        if (!curUserid.equals(userid)) {
                            PersonPageActivity.start(context, userid);
                        }

                    }
                });

                holder.setText(R.id.tv_member_name, data.getUsernick());
            }


        }
    }


    private void initData() {

        memberAdapter = new GroupMemberAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(memberAdapter);
        getMemberList();
    }


    /**
     * 获取成员列表
     */
    private void getMemberList() {
        showProgressDialog("加载中...", false);
        memberPresenter.getMembers(this, groupno, new OnResult<List<GroupMLBean.DataBean>>() {
            @Override
            public void onSuccess(List<GroupMLBean.DataBean> dataBeen) {
                if (recyclerView != null) {

                    for (GroupMLBean.DataBean g : dataBeen) {
                        log(g.getUsernick());
                    }
                    dismissProgressDialog();

                    String membercount = "群成员(%d)";
                    String format = String.format(membercount, dataBeen == null ? 0 : dataBeen.size());
                    tvCount.setText(format);

                    log(dataBeen.toString());
                    memberAdapter.replaceAll(dataBeen);

                }
            }

            @Override
            public void onFailed(String errorMsg) {
                if (recyclerView != null) {
                    dismissProgressDialog();
                    toast(errorMsg);
                }

            }
        });
    }
}
