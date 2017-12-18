package com.ixiangni.app.orders;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.handongkeji.interfaces.OnResult;
import com.ixiangni.adapter.MyRvAdapter;
import com.ixiangni.adapter.MyRvViewHolder;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseFragment;
import com.ixiangni.bean.OrderBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.interfaces.IOrdRefresh;
import com.ixiangni.presenters.OrderPresenter;
import com.ixiangni.util.LayouManagerHelper;
import com.ixiangni.util.LinearDecoration;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;
import com.mydemo.yuanxin.util.HttpUtil;
import com.nevermore.oceans.ob.SuperObservableManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdPlaneFragment extends BaseFragment implements OrderHelper.OnCurrentPageChange, OnResult<List<OrderBean>>, IOrdRefresh.IRefreshPlane {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    private String orderstatus;
    private OrdPlaneAdapter mAdapter;
    private OrderHelper helper;
    private OrderPresenter presenter;

    public OrdPlaneFragment() {
        // Required empty public constructor
    }


    public static OrdPlaneFragment createInstance(String orderstatus) {
        OrdPlaneFragment fragment = new OrdPlaneFragment();

        Bundle bundle = new Bundle();
        bundle.putString(XNConstants.orderstatus, orderstatus);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SuperObservableManager.registerObserver(IOrdRefresh.IRefreshPlane.class, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SuperObservableManager.unregisterObserver(IOrdRefresh.IRefreshPlane.class, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ord_plane, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderstatus = getArguments().getString(XNConstants.orderstatus);

        mAdapter = new OrdPlaneAdapter(getContext());

        recyclerView.setLayoutManager(LayouManagerHelper.createLinearManager(getContext()));
        recyclerView.addItemDecoration(new LinearDecoration(5, ContextCompat.getColor(getContext(), R.color.colorGray), LinearDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        presenter = new OrderPresenter();


        helper = new OrderHelper(smartPullLayout, stateLayout, mAdapter);
        helper.setOnCurrentPageChange(this);
        helper.init();


    }

    private boolean isAuto = true;

    @Override
    public void onChange(int currentPage) {

        if (isAuto) {
            stateLayout.showLoadView();
        }
        presenter.getOrderList(getContext(), OrderPresenter.ORDER_TYPE_PLANE, orderstatus, "" + currentPage, helper.getPageSize() + "", this);

    }

    @Override
    public void onSuccess(List<OrderBean> orderBeen) {

        if (stateLayout != null) {
            if (1 == helper.getCurrentPage()) {
                mAdapter.replaceAll(orderBeen);
                recyclerView.scrollToPosition(0);
            } else {
                mAdapter.addAll(orderBeen);
            }
            isAuto = false;
            helper.loadDataFinish(orderBeen.size());
        }
    }

    @Override
    public void onFailed(String errorMsg) {
        if (stateLayout != null) {
            stateLayout.loadingFinish();
            toast(errorMsg);
        }

    }

    @Override
    public void onRefresh() {
        if (helper != null) {
            isAuto = true;
            helper.onPullDown();
        }
    }

    private class OrdPlaneAdapter extends MyRvAdapter<OrderBean> {

        public OrdPlaneAdapter(Context context) {
            super(context, R.layout.item_order_plane);
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, OrderBean data) {

            TextView tvFromTo = holder.getView(R.id.tv_from_to);

            TextView tvPay = holder.getView(R.id.tv_pay);
            TextView tvCancel = holder.getView(R.id.tv_cancel_order);

            TextView tvTimeInfo = holder.getView(R.id.tv_plan_time);
            tvTimeInfo.setText(data.getDescribe1() + " " + data.getDescribe2());

            TextView tvFlight = holder.getView(R.id.tv_flight);
            tvFlight.setText(data.getDescribe3());

            tvFromTo.setText(data.getOrdername());


            //订单状态
            //0 待支付 1 已支付 2 支付失败 3退单成功
            // 4机票出票申请中 5机票出票成功 6机票申请未过 7酒店订单取消申请中
            //  8火车票订单取消申请中 9火车票出票申请中 10 火车票出票成功 11火车票申请未过 100默认无效状态
            int orderstatus = data.getOrderstatus();
            if (orderstatus == 0) {//未支付
                tvPay.setText(R.string.no_pay);
                tvCancel.setText("删除订单");
                tvPay.setSelected(true);
                tvCancel.setSelected(false);


            } else if (orderstatus == 1) {//已支付

                tvPay.setText(R.string.yes_pay);
                tvCancel.setText("申请退票");
                tvPay.setSelected(true);
                tvCancel.setSelected(false);
            } else if (orderstatus == 2) {
                tvPay.setText(R.string.pay_fail);
                tvCancel.setText("删除订单");
                tvPay.setSelected(true);
                tvCancel.setSelected(false);

            } else if (orderstatus == 3) {
                tvPay.setText(R.string.regist_succeed);
                tvCancel.setText("删除订单");
                tvPay.setSelected(true);
                tvCancel.setSelected(false);

            } else if (orderstatus == 4) {
                tvPay.setText(R.string.out_ing);
                tvCancel.setText("申请退票");
                tvPay.setSelected(true);
                tvCancel.setSelected(false);

            } else if (orderstatus == 5) {
                tvPay.setText(R.string.out_succeed);
                tvCancel.setText("申请退票");
                tvPay.setSelected(true);
                tvCancel.setSelected(false);

            } else if (orderstatus == 6) {
                tvPay.setText(R.string.out_fail);
                tvCancel.setText("删除订单");
                tvPay.setSelected(true);
                tvCancel.setSelected(false);

            } else {
                tvPay.setText("暂无状态");
                tvCancel.setText("删除订单");
                tvPay.setSelected(true);
                tvCancel.setSelected(false);
            }
            holder.setOnitemClickListener(v -> {
                Intent intent = new Intent(getContext(), OrdPlaneDetailActivity.class);
                intent.putExtra(XNConstants.orderid, data.getOrderid() + "");
                startActivity(intent);
            });
            holder.getView(R.id.tv_cancel_order).setOnClickListener(v -> {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("提醒");
                if (orderstatus == 1 || orderstatus == 4 || orderstatus == 5) {//4机票出票中 5机票出票成功 6机票申请未过
                    dialog.setMessage("您是否确认申请退票？");

                } else {
                    dialog.setMessage("您是否确认删除该订单？");
                }


                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String myUrl = null;
                        if (orderstatus == 1 || orderstatus == 4 || orderstatus == 5) {//4机票出票申请中 5机票出票成功 6机票申请未过
                            myUrl = UrlString.URL_PLANE_TUIPIAO;
                        } else {
                            myUrl = UrlString.URL_DELETE_ORDER;
                        }
                        showProgressDialog();
                        String shanglvSysno = data.getShanglvsysno();
                        if (TextUtils.isEmpty(shanglvSysno)) {
                            Toast.makeText(getActivity(), "订单号获取失败", Toast.LENGTH_SHORT).show();
                            closeProgressDialog();
                            return;
                        }
                        final RequestBody requsetBody = new FormBody.Builder()
                                .add("token", MyApp.getInstance().getUserTicket())
                                .add("orderId", data.getOrderid() + "")
                                .add("shanglvSysno", data.getShanglvsysno())//shanglvSysno订单号
                                .build();
                        HttpUtil.sendOkHttpPostRequest(myUrl, requsetBody, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        closeProgressDialog();
                                        Toast.makeText(getActivity(), "网络开小差了，请稍后重试", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String text = response.body().string();
                                try {
                                    JSONObject jo = new JSONObject(text);
                                    String message = jo.getString("message");
                                    String status = jo.getString("status");
                                    if ("0000".equals(status)) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                closeProgressDialog();
                                                tvCancel.setText("申请成功");
                                                Toast.makeText(getActivity(), message + ",请下拉刷新更新数据", Toast.LENGTH_SHORT).show();


                                            }
                                        });
                                    } else {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                closeProgressDialog();

                                                Toast.makeText(getActivity(), message + ",请下拉刷新更新数据", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                } catch (JSONException e) {

                                }
                            }
                        });

                    }
                });

                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            });


        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //加载中方法
    private ProgressDialog progressDialog;

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {

        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("申请中...");
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
