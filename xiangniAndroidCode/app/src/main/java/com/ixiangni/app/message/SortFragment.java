package com.ixiangni.app.message;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.utils.DateUtil;
import com.handongkeji.utils.DisplayUtil;
import com.ixiangni.adapter.MyRvAdapter;
import com.ixiangni.adapter.MyRvViewHolder;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseFragment;
import com.ixiangni.app.orders.OrderHelper;
import com.ixiangni.bean.MsgListBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.interfaces.IMsgCountChang;
import com.ixiangni.presenters.contract.MyPresenter;
import com.ixiangni.util.LayouManagerHelper;
import com.ixiangni.util.LinearDecoration;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 分类消息（全部，未读，已读）
 *
 * @ClassName:SortFragment
 * @PackageName:com.ixiangni.app.message
 * @Create On 2017/6/19 0019   09:54
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/19 0019 handongkeji All rights reserved.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class SortFragment extends BaseFragment implements IMsgCountChang {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    private String title;
    private String status;
    private OrderHelper helper;
    private MsgAdapter mAdapter;

    public SortFragment() {
        // Required empty public constructor
    }


    public static SortFragment newInstance(String sortName) {

        SortFragment sortFragment = new SortFragment();
        Bundle bundle = new Bundle();
        bundle.putString("status", sortName);
        sortFragment.setArguments(bundle);
        return sortFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SuperObservableManager.registerObserver(IMsgCountChang.class, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SuperObservableManager.unregisterObserver(IMsgCountChang.class, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sort, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        status = getArguments().getString("status");


        mAdapter = new MsgAdapter(getContext());
        recyclerView.setLayoutManager(LayouManagerHelper.createLinearManager(getContext()));
        recyclerView.addItemDecoration(new LinearDecoration(DisplayUtil.dip2px(getContext(), 5), ContextCompat.getColor(getContext(),R.color.colorT), LinearDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        helper = new OrderHelper(smartPullLayout, stateLayout, mAdapter);
        helper.setOnCurrentPageChange(new OrderHelper.OnCurrentPageChange() {
            @Override
            public void onChange(int currentPage) {
                getMessageList(currentPage);
            }
        });

        helper.init("暂无消息");

    }

    private boolean isAuto = true;

    private void getMessageList(int currentPage) {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put(XNConstants.currentPage, "" + currentPage);
        params.put(XNConstants.pageSize, helper.getPageSize() + "");
        if (!TextUtils.isEmpty(status)) {
            params.put("msgstatus", status);
        }

        if (isAuto) {
            stateLayout.showLoadView();
        }
        RemoteDataHandler.asyncPost(UrlString.URL_MESSAGE_LIST, params, getContext(), true, response -> {

            if (stateLayout == null) {
                return;
            }
            String json = response.getJson();
            log(json);
            if (!CommonUtils.isStringNull(json)) {

                MsgListBean msgListBean = new Gson().fromJson(json, MsgListBean.class);
                if (1 == msgListBean.getStatus()) {
                    List<MsgListBean.DataBean> data = msgListBean.getData();
                    if (data == null) {
                        return;
                    }
                    if (1 == helper.getCurrentPage()) {
                        mAdapter.replaceAll(data);
                        recyclerView.scrollToPosition(0);
                    } else {
                        mAdapter.addAll(data);
                    }
                    isAuto = false;
                    helper.loadDataFinish(data.size());

                } else {
                    toast(msgListBean.getMessage());
                    stateLayout.loadingFinish();
                }
            } else {
                toast(Constants.CONNECT_SERVER_FAILED);
                stateLayout.loadingFinish();

            }
        });
    }

    @Override
    public void onMSgCountChange() {
        if (helper != null) {
            helper.onPullDown();
        }
    }

    private class MsgAdapter extends MyRvAdapter<MsgListBean.DataBean> {


        public MsgAdapter(Context context) {
            super(context, R.layout.item_message);
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, MsgListBean.DataBean data) {

            long msgtime = data.getMsgtime();
            String ymd = DateUtil.getYmd(msgtime);
            holder.setText(R.id.tv_time, ymd);
            holder.setText(R.id.tv_content, data.getMsgcontent());

            holder.setOnitemClickListener(v -> {
                Intent intent = new Intent(getContext(), MsgDetailActivity.class);
                intent.putExtra(XNConstants.msgcontent, data.getMsgcontent());
                intent.putExtra(XNConstants.msgtime, ymd);
                startActivity(intent);

                if (data.getMsgstatus() == 0) {
                    readMessage(data.getMsgid());
                }
            });

        }
    }

    private void readMessage(int msgid) {

        HashMap<String, String> params = new HashMap<>();

        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("msgid", "" + msgid);
        MyPresenter.request(getContext(), UrlString.URL_READ_MESSAGE, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {

                if (recyclerView != null) {
                    SuperObservableManager.notify(IMsgCountChang.class, IMsgCountChang::onMSgCountChange);
                }
            }
            @Override
            public void onFailed(String errorMsg) {

            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
