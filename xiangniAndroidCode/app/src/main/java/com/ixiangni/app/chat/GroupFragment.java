package com.ixiangni.app.chat;


import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.easeui.domain.EaseUser;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseFragment;
import com.ixiangni.bean.GroupBean;
import com.ixiangni.bean.GroupListBean;
import com.ixiangni.interfaces.OnGroupDelete;
import com.ixiangni.interfaces.OnGroupNameChange;
import com.ixiangni.util.SmartPullableLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;

/**
 * 群
 *
 * @ClassName:GroupFragment
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/7/5 0005   15:47
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/5 0005 handongkeji All rights reserved.
 */
public class GroupFragment extends BaseFragment implements OnGroupNameChange, OnGroupDelete {


    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.tv_no_data_message)
    TextView tvNoDataMessage;
    @Bind(R.id.rl_no_data)
    RelativeLayout rlNoData;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.iv_icon_no_data)
    ImageView ivIconNoData;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    private GroupAdapter mAdapter;
    private String url;

    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SuperObservableManager.getInstance().getObservable(OnGroupNameChange.class).registerObserver(this);
        SuperObservableManager.getInstance().getObservable(OnGroupDelete.class).registerObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SuperObservableManager.getInstance().getObservable(OnGroupNameChange.class).unregisterObserver(this);
        SuperObservableManager.getInstance().getObservable(OnGroupDelete.class).unregisterObserver(this);

    }

    public static final String GROUP_URL = "group_url";

    public static GroupFragment newInstance(String url) {
        GroupFragment groupFragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(GROUP_URL, url);
        groupFragment.setArguments(args);

        return groupFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.bind(this, view);
        tvNoDataMessage.setText("暂无群聊");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        url = getArguments().getString(GROUP_URL);

        mAdapter = new GroupAdapter(getContext());
        listView.setAdapter(mAdapter);
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                rlNoData.setVisibility(mAdapter.getCount() != 0 ? View.GONE : View.VISIBLE);
            }
        });

        smartPullLayout.setOnPullListener(new SmartPullableLayout.OnPullListener() {
            @Override
            public void onPullDown() {
                getGroupList(url);
            }

            @Override
            public void onPullUp() {

            }
        });
        getGroupList(url);

    }

    @Override
    public void onResume() {
        super.onResume();
        getGroupList(url);
    }

    private boolean isFrist = true;
    private void getGroupList(String url) {

        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());

        if (progressBar != null && progressBar.getVisibility() != View.VISIBLE&&isFrist) {
            progressBar.setVisibility(View.VISIBLE);
        }
        RemoteDataHandler.asyncPost(url, params, getContext(), true, response -> {
            if(smartPullLayout==null){
                return;
            }
            smartPullLayout.stopPullBehavior();
            String json = response.getJson();
            log(json);
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
                isFrist=false;
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    GroupListBean groupListBean = new Gson().fromJson(json, GroupListBean.class);
                    if (1 == groupListBean.getStatus()) {

                        List<GroupBean> list = groupListBean.getData().getList();

                        ArrayList<GroupBean> dataList = new ArrayList<GroupBean>();
                        Observable.from(list)
                                .subscribe(new Action1<GroupBean>() {
                                    @Override
                                    public void call(GroupBean groupBean) {
                                        if (groupBean != null) {
                                            dataList.add(groupBean);
                                        }
                                    }
                                });

                        mAdapter.replaceAll(dataList);

                    } else {
                        toast(groupListBean.getMessage());
                    }
                }
            }

        });

    }

    //群名称发生改变
    @Override
    public void onNameChange(String groupno, String groupName) {

//        if(mAdapter!=null){
//            mAdapter.changeGroupname(groupno,groupName);
//        }
        getGroupList(url);

    }

    @Override
    public void onDelete() {
        getGroupList(url);
    }

    private class GroupAdapter extends QuickAdapter<GroupBean> {

        private final Comparator<GroupBean> comparator;

        public GroupAdapter(Context context) {
            super(context, R.layout.item_contact_list);
            comparator = new Comparator<GroupBean>() {
                @Override
                public int compare(GroupBean o1, GroupBean o2) {
                    if (o1 == null || o2 == null) {
                        return 1;
                    }
                    String l1 = o1.getFirstLetter();
                    String l2 = o2.getFirstLetter();

                    String jin = "#";
                    if (jin.equals(l1)) {
                        if (jin.equals(l2)) {
                            return 0;
                        } else {
                            return 1;
                        }
                    } else {
                        if (jin.equals(l2)) {
                            return -1;
                        } else {
                            return o1.getFirstLetter().compareTo(o2.getFirstLetter());
                        }
                    }


                }
            };
        }

        @Override
        public void addAll(List<GroupBean> elem) {
            this.data.addAll(elem);
            Collections.sort(data, comparator);
            notifyDataSetChanged();
        }

        /**
         * 改变成群名称
         *
         * @param groupno
         * @param groupname
         */
        public void changeGroupname(String groupno, String groupname) {
            for (int i = 0; i < data.size(); i++) {
                GroupBean groupBean = data.get(i);
                if (groupno.equals(groupBean.getGroupchatno())) {
                    groupBean.setGroupchatname(groupname);
                    notifyDataSetChanged();
                    break;
                }
            }
        }

        @Override
        public void replaceAll(List<GroupBean> elem) {
            Collections.sort(elem, comparator);
            super.replaceAll(elem);

        }

        @Override
        protected void convert(BaseAdapterHelper helper, GroupBean groupBean) {

            TextView tvLetter = helper.getView(R.id.tv_letter);
            String firstLetter = groupBean.getFirstLetter();

            //索引
            int position = helper.getPosition();
            if (position - 1 >=0 && firstLetter.equals(data.get(position - 1).getFirstLetter())) {
                tvLetter.setVisibility(View.GONE);
            } else {
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText(firstLetter);
            }

            //群名称
            String groupchatname = groupBean.getGroupchatname();
            helper.setText(R.id.tv_user_nick, groupchatname);
            ImageView ivGroupIcon = helper.getView(R.id.iv_icon);

            //设置群头像
            String groupchatpic = groupBean.getGroupchatpic();


            if (CommonUtils.isStringNull(groupchatpic) || groupchatpic.startsWith("localhost")) {
                ivGroupIcon.setImageResource(R.mipmap.qunliao);
            } else {
//                GlideUtil.loadRoundImage(context, groupchatpic, ivGroupIcon, R.mipmap.qunliao);
                Glide.with(context).load(groupchatpic).placeholder(R.mipmap.qunliao).error(R.mipmap.qunliao).into(ivGroupIcon);
            }

            //点击进群聊天
            helper.setOnClickListener(R.id.rl_chat_user, v -> {

                //打开群聊
//                ChatActivity.startChat(context, groupBean.getGroupchatno(), EaseConstant.CHATTYPE_GROUP);
                EaseUser user = new EaseUser(groupBean.getGroupchatno());
                user.setNickname(groupchatname);
                user.setAvatar(groupchatpic);
                DemoHelper.getInstance().saveContact(user);

                ChatActivity.startGroupChat(context, groupBean.getGroupchatno(), groupchatname);
                String groupchatno = groupBean.getGroupchatno();

//                getGroupInfo(groupchatno);
            });

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
