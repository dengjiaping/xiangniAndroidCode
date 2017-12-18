package com.ixiangni.app.orders;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handongkeji.interfaces.OnResult;
import com.ixiangni.adapter.MyRvAdapter;
import com.ixiangni.adapter.MyRvViewHolder;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseFragment;
import com.ixiangni.bean.OrderBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.interfaces.IOrdRefresh;
import com.ixiangni.presenters.OrderPresenter;
import com.ixiangni.util.LayouManagerHelper;
import com.ixiangni.util.LinearDecoration;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 火车票订单列表
 * @ClassName:OrdTrainFragment

 * @PackageName:com.ixiangni.app.orders

 * @Create On 2017/8/8 0008   15:20

 * @Site:http://www.handongkeji.com

 * @author:xuchuanting

 * @Copyrights 2017/8/8 0008 handongkeji All rights reserved.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdOtherFragment extends BaseFragment implements OrderHelper.OnCurrentPageChange, OnResult<List<OrderBean>>,IOrdRefresh.IRefreshOther {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    private String orderstatus;
    private OrderHelper helper;
    private OrderPresenter presenter;
    private OrdOtherAdapter mAdapter;

    public OrdOtherFragment() {
        // Required empty public constructor
    }


    public static OrdOtherFragment createInstance(String orderstatus) {
        OrdOtherFragment fragment = new OrdOtherFragment();

        Bundle bundle = new Bundle();
        bundle.putString(XNConstants.orderstatus, orderstatus);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        SuperObservableManager.registerObserver(IOrdRefresh.IRefreshOther.class,this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        SuperObservableManager.unregisterObserver(IOrdRefresh.IRefreshOther.class,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ord_other, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderstatus = getArguments().getString(XNConstants.orderstatus);


        mAdapter = new OrdOtherAdapter(getContext());
        recyclerView.setLayoutManager(LayouManagerHelper.createLinearManager(getContext()));
        recyclerView.addItemDecoration(new LinearDecoration(5, ContextCompat.getColor(getContext(),R.color.colorGray),LinearDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        presenter = new OrderPresenter();


        helper = new OrderHelper(smartPullLayout,stateLayout, mAdapter);
        helper.setOnCurrentPageChange(this);
        helper.init();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onSuccess(List<OrderBean> orderBeen) {

    }

    @Override
    public void onFailed(String errorMsg) {

    }

    @Override
    public void onRefresh() {

    }
//
//    @Override
//    public void onSuccess(List<OrderBean> orderBeen) {
//        if(stateLayout!=null){
//            if(1==helper.getCurrentPage()){
//                mAdapter.replaceAll(orderBeen);
//                recyclerView.scrollToPosition(0);
//            }else {
//                mAdapter.addAll(orderBeen);
//            }
//            isAuto=false;
//            helper.loadDataFinish(orderBeen.size());
//        }
//    }

//    @Override
//    public void onFailed(String errorMsg) {
//        if(stateLayout!=null){
//            stateLayout.loadingFinish();
//            toast(errorMsg);
//        }
//    }
//
//    @Override
//    public void onRefresh() {
//        if(helper!=null){
//            isAuto=true;
//            helper.onPullDown();
//        }
//    }

    private class OrdOtherAdapter extends MyRvAdapter<OrderBean> {

        public OrdOtherAdapter(Context context) {
            super(context, R.layout.item_order_other);
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, OrderBean data) {

            TextView tvFromTo = holder.getView(R.id.tv_from_to);

            TextView tvPay = holder.getView(R.id.tv_pay);

            TextView tvTimeInfo = holder.getView(R.id.tv_plan_time);

            tvTimeInfo.setText(data.getDescribe2()+" "+data.getDescribe3());

            TextView tvFlight = holder.getView(R.id.tv_flight);
            tvFlight.setText(data.getDescribe1());

            tvFromTo.setText(data.getOrdername());


            //订单状态
            int orderstatus = data.getOrderstatus();
            if(orderstatus==0){//未支付
                tvPay.setText(R.string.no_pay);
                tvPay.setSelected(true);


            }else {//已支付

                tvPay.setText(R.string.yes_pay);
                tvPay.setSelected(false);
            }

            holder.setOnitemClickListener(v -> {
//                Intent intent = new Intent(getContext(),OrdTrainDetailActivity.class);
//                intent.putExtra(XNConstants.orderid,data.getOrderid()+"");
//                startActivity(intent);
            });

        }
    }

    private boolean isAuto = true;
    @Override
    public void onChange(int currentPage) {
//        if(isAuto){
//            stateLayout.showLoadView();
//        }
//        presenter.getOrderList(getContext(),OrderPresenter.ORDER_TYPE_OTHER,orderstatus,""+currentPage,helper.getPageSize()+"",this);
    }
}
