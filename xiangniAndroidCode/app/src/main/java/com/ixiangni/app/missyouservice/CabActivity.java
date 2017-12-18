package com.ixiangni.app.missyouservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.util.ViewHelper;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.widget.NoScrollListView;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.CabListBean;
import com.ixiangni.bean.DateParams;
import com.ixiangni.bean.PlaneBaseBean;
import com.ixiangni.bean.PlaneDataBean;
import com.ixiangni.bean.PolicyBean;
import com.ixiangni.bean.PolicyListBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.util.OrderUtil;
import com.ixiangni.util.StateLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nevermore.oceans.utils.ListUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 选座 优惠政策
 *
 * @ClassName:CabActivity
 * @PackageName:com.ixiangni.app.missyouservice
 * @Create On 2017/8/5 0005   14:53
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/8/5 0005 handongkeji All rights reserved.
 */
public class CabActivity extends BaseActivity {

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    private CabAdapter adapter;
    private DateParams params;
    private PlaneBaseBean base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab);
        ButterKnife.bind(this);

        PlaneDataBean dataBean = getIntent().getParcelableExtra(XNConstants.flight_data);
        params = getIntent().getParcelableExtra(XNConstants.plane_list_params);


        View headerView = LayoutInflater.from(this).inflate(R.layout.cab_header, null);
        ViewHelper helper = new ViewHelper(headerView);

        base = dataBean.getBase();
        initHeader(helper, base);

        listView.addHeaderView(headerView);
        adapter = new CabAdapter(this);
        listView.setAdapter(adapter);

        adapter.addAll(dataBean.getCabList());
    }

    private void initHeader(ViewHelper helper, PlaneBaseBean base) {


        helper.setText(R.id.tv_start_port, params.getStartCity() + base.getStartT());
        helper.setText(R.id.tv_end_port, params.getEndCity() + base.getEndT());

        //起飞时间
        helper.setText(R.id.tv_start_time, OrderUtil.getHourMin(base.getOffTime()));
        //降落时间
        helper.setText(R.id.tv_end_time, OrderUtil.getHourMin(base.getArriveTime()));

        //起飞日期
        helper.setText(R.id.tv_start_date, OrderUtil.getYMD(base.getOffTime()));

        //降落日期
        helper.setText(R.id.tv_end_date, OrderUtil.getYMD(base.getArriveTime()));
        //总时长
        helper.setText(R.id.tv_duration, base.getRunTime());

        //航班
        helper.setText(R.id.tv_flight, OrderUtil.getFlight(base));

    }

    class CabAdapter extends QuickAdapter<CabListBean> {

        private int openPoi = -1;

        public int getOpenPoi() {
            return openPoi;
        }

        public void setOpenPoi(int openPoi) {
            this.openPoi = openPoi;
        }

        public CabAdapter(Context context) {
            super(context, R.layout.item_cab);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, CabListBean cabListBean) {
            helper.setText(R.id.tv_cab_type, cabListBean.getCabinName());
            helper.setText(R.id.tv_cab_price, cabListBean.getFare());

            View cabView = helper.getView(R.id.rl_cab);

            int position = helper.getPosition();
            cabView.setSelected(position == getOpenPoi());

            NoScrollListView listView = helper.getView(R.id.list_view_policy);
            if (position == getOpenPoi()) {
                openPolicy(position, listView);
            }
            listView.setVisibility(position == getOpenPoi() ? View.VISIBLE : View.GONE);
            helper.setOnClickListener(R.id.rl_cab, v -> {

                if (position != getOpenPoi()) {
                    setOpenPoi(position);
                    notifyDataSetChanged();

                } else {
                    setOpenPoi(-1);
                    notifyDataSetChanged();
                }
            });
        }

        private View lastOpenedView;
        private void closeLastPolicy() {
            if(lastOpenedView!=null){
                lastOpenedView.setVisibility(View.GONE);
            }
        }

        private SparseArray<List<PolicyBean>> policyList = new SparseArray<>();

        private void openPolicy(int position, NoScrollListView listView) {
            List<PolicyBean> policyBeen = policyList.get(position);
            if (ListUtil.isEmptyList(policyBeen)) {
                getPolicyList(position, getItem(position));
            } else {
                PolicyAdapter policyAdapter = new PolicyAdapter(listView.getContext(), policyBeen);
                listView.setAdapter(policyAdapter);
                listView.setVisibility(View.VISIBLE);
                lastOpenedView=listView;
            }
        }

        public void savePolicyList(int position, List<PolicyBean> list) {
            policyList.put(position, list);
            notifyDataSetChanged();

        }


    }


    /**
     * 获取优惠政策列表
     * @param position
     * @param cabListBean
     */
    private void getPolicyList(final int position, CabListBean cabListBean) {
        HashMap<String, String> params1 = new HashMap<>();
        params1.put(Constants.token, MyApp.getInstance().getUserTicket());
        params1.put("IsTeHui", "" + cabListBean.getIsTeHui());
        params1.put("flightno", base.getFlightNo());
        params1.put("scode", params.getStar());
        params1.put("ecode", params.getEnd());
        params1.put("fare", cabListBean.getFare());
        params1.put("tax", base.getTax() + "");
        params1.put("oil", base.getOil() + "");
        params1.put("cabin", cabListBean.getCabin());
        params1.put("startime", base.getOffTime());
        params1.put("endtime", base.getArriveTime());


        showProgressDialog("加载中",true);
        RemoteDataHandler.asyncPost(UrlString.URL_POLICY_LIST, params1, this, false, response -> {

            String json = response.getJson();
            log(json);
            if (stateLayout != null) {
                dismissProgressDialog();
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    PolicyListBean policyListBean = new Gson().fromJson(json, PolicyListBean.class);
                    if (1 == policyListBean.getStatus()) {
                        adapter.savePolicyList(position, policyListBean.getData());
                    } else {
                        toast(policyListBean.getMessage());
                    }
                }

            }


        });

    }

//    token	是	String	用户token
//    IsTeHui	是	String	是否特惠(0=否,1=是 )(飞机票列表中CabList中的IsTeHui)
//    flightno	是	String	航班号(飞机票列表中base中的FlightNo)
//    scode	是	String	出发城市三字码
//    ecode	是	String	到达城市三字码
//    fare	是	String	票面价(飞机票列表中CabList中的Fare)
//    tax	是	String	机建(飞机票列表中base中的Tax)
//    oil	是	String	燃油(飞机票列表中base中的Oil)
//    cabin	是	String	仓位(飞机票列表中CabList中的Cabin)
//    startime	是	String	起飞时间(飞机票列表中base中的OffTime)
//    endtime	是	String	到达时间(飞机票列表中base中的ArriveTime)


    class PolicyAdapter extends QuickAdapter<PolicyBean> {

        public PolicyAdapter(Context context) {
            super(context, R.layout.item_policy);
        }

        public PolicyAdapter(Context context, List<PolicyBean> data) {
            super(context, R.layout.item_policy, data);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, PolicyBean policyBean) {


            int fare = policyBean.getSale();
            helper.setText(R.id.tv_policy_price, "" + fare);

            int index = helper.getPosition() + 1;
            final String policyName = "优惠政策".concat("" + index);
            helper.setText(R.id.tv_policy_name, policyName);
            helper.setOnClickListener(R.id.tv_buy,v -> {

                Intent intent = new Intent(CabActivity.this,FillPlaneOrdActivity.class);
                intent.putExtra(XNConstants.plane_list_params,params);
                intent.putExtra(XNConstants.plane_base,base);

                intent.putExtra(XNConstants.cab_data,adapter.getItem(adapter.getOpenPoi()));

                int i = helper.getPosition() + 1;

                intent.putExtra(XNConstants.discount,policyName);
                intent.putExtra(XNConstants.policy_info,policyBean);

                startActivity(intent);

            });

        }
    }

}
