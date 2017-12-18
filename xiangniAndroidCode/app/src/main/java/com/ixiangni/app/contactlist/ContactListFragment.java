package com.ixiangni.app.contactlist;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.util.ViewHelper;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.widget.SideBar;
import com.handongkeji.zxing.ZxingConstants;
import com.handongkeji.zxing.activity.CaptureActivity;
import com.hyphenate.chatuidemo.db.InviteMessgeDao;
import com.ixiangni.adapter.ContactAdapter;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseFragment;
import com.ixiangni.app.chat.AgreeActivity;
import com.ixiangni.app.chat.GroupActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.app.login.LoginState;
import com.ixiangni.app.user.PersonPageActivity;
import com.ixiangni.bean.FriendListBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.interfaces.OnRefreshContactList;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;
import com.nevermore.oceans.ob.SuperObservable;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactListFragment extends BaseFragment implements View.OnClickListener, LoginState {


    @Bind(R.id.iv_jia)
    ImageView ivJia;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.side_bar)
    SideBar sideBar;
    @Bind(R.id.tv_show)
    TextView tvShow;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;


    private TextView tvWeiDu;

    private ContactAdapter mAdapter;
    private InviteMessgeDao messgeDao;
    private StateReceiver mreceiver;

    public ContactListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SuperObservable<OnRefreshContactList> observable = SuperObservableManager.getInstance().
                getObservable(OnRefreshContactList.class);

        observable.registerObserver(refreshContactList);
        SuperObservableManager.getInstance().getObservable(LoginState.class).registerObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SuperObservableManager.getInstance().getObservable(OnRefreshContactList.class).unregisterObserver(refreshContactList);
        SuperObservableManager.getInstance().getObservable(LoginState.class).unregisterObserver(this);
    }

    private OnRefreshContactList refreshContactList = new OnRefreshContactList() {
        @Override
        public void onRefresh() {
            getFriendList();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ButterKnife.bind(this, view);

        IntentFilter filter = new IntentFilter(RemoteDataHandler.STATE_REMOTE_LOGIN);
        mreceiver = new StateReceiver();
        filter.addAction(XNConstants.NewInviteMessage);
        getActivity().registerReceiver(mreceiver,filter);


        messgeDao = new InviteMessgeDao(getActivity());

//        updateInviteMessage();
        return view;
    }

    private void updateInviteMessage() {
        int unreadMessagesCount = messgeDao.getUnreadMessagesCount();
        if(unreadMessagesCount==0){
            tvWeiDu.setVisibility(View.GONE);
        }else {
            tvWeiDu.setVisibility(View.VISIBLE);
            tvWeiDu.setText(unreadMessagesCount+"");
//            tvWeiDu.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_contact_header, null);

        ViewHelper helper = new ViewHelper(headerView);
        helper.getView(R.id.rl_new_friend).setOnClickListener(this);
        helper.getView(R.id.rl_people_around).setOnClickListener(this);
        helper.getView(R.id.rl_scan).setOnClickListener(this);
        helper.getView(R.id.rl_group_chat).setOnClickListener(this);

        tvWeiDu= (TextView) headerView.findViewById(R.id.tv_weiduxiaoxi);
        tvWeiDu.setText("1");


        listView.addHeaderView(headerView);

        mAdapter = new ContactAdapter(getContext());

        listView.setAdapter(mAdapter);

        sideBar.setTextView(tvShow);


        smartPullLayout.setOnPullListener(new SmartPullableLayout.OnPullListener() {
            @Override
            public void onPullDown() {
                getFriendList();
            }

            @Override
            public void onPullUp() {

            }
        });
        sideBar.setOnTouchingLetterChangedListener(touchListener);
        getFriendList();
    }

    private SideBar.OnTouchingLetterChangedListener touchListener = new SideBar.OnTouchingLetterChangedListener() {
        @Override
        public void onTouchingLetterChanged(String s) {
            int lettrePoi = mAdapter.getLettrePoi(s);
            if ("#".equals(s)) {
                listView.setSelection(0);
            }
            if (lettrePoi != -1) {
                listView.setSelection(lettrePoi + 1);
            }
        }
    };


    private boolean isLoading = false;
    private boolean isfirst = true;

    /**
     * 获取好友列表
     */
    private void getFriendList() {
        if (!LoginHelper.getInstance().isLogin(getContext())) {
            return;
        }

        isLoading = true;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());

//        showProgressDialog("加载中...", false);
        if (isfirst) {
            stateLayout.showLoadView("加载中...");

        }
        RemoteDataHandler.asyncPost(UrlString.URL_FRIEND_LIST, params, getContext(), true, callback -> {

            if (listView != null) {
//                dismissProgressDialog();
                isLoading = false;
                isfirst = false;
                smartPullLayout.stopPullBehavior();
                String json = callback.getJson();
                if (!TextUtils.isEmpty(json)) {
                    log("好友列表：" + json);
                    FriendListBean friendListBean = new Gson().fromJson(json, FriendListBean.class);
                    if (1 == friendListBean.getStatus()) {
                        stateLayout.showContenView();
                        mAdapter.replaceAll(friendListBean.getData());
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.iv_search, R.id.iv_jia})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                startActivity(SearchContactsActivity.class);
//                getActivity().overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
                break;
            case R.id.iv_jia:


                startActivity(AgreeActivity.class);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_new_friend:
                startActivity(AgreeActivity.class);
                break;
            case R.id.rl_people_around:
                startActivity(NearbyPeopleActivity.class);
                break;
            case R.id.rl_scan:
                int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    scanCode();
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 11);
                }

                break;
            case R.id.rl_group_chat:
                startActivity(GroupActivity.class);
                break;
        }
    }

    private void scanCode() {
        startActivityForResult(new Intent(getContext(), CaptureActivity.class), 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanCode();
            } else {
                toast("照相机权限被禁止，不能使用扫一扫！");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (101 == requestCode && resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra(ZxingConstants.RESULT);
            if (!CommonUtils.isStringNull(result)) {
                String[] split = result.split("\\?");
                if (split.length >= 2 && TextUtils.isDigitsOnly(split[split.length - 1])) {
                    PersonPageActivity.start(getContext(), split[split.length - 1]);
                } else {
                    toast("信息错误...");
                }

            } else {
                toast("信息错误...");
            }
        }

    }

    @Override
    public void onLogin() {
        getFriendList();
    }

    @Override
    public void onLogout() {
        mAdapter.clear();
    }


    @Override
    public void onResume() {
        super.onResume();
        updateInviteMessage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mreceiver);
    }

    private class StateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

                    updateInviteMessage();


        }

        }
}

