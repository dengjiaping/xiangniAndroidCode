package com.ixiangni.app.missyouservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.utils.DateUtil;
import com.ixiangni.adapter.MyRvAdapter;
import com.ixiangni.adapter.MyRvViewHolder;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.DateParams;
import com.ixiangni.bean.PTListBean;
import com.ixiangni.bean.PlaneBaseBean;
import com.ixiangni.bean.PlaneDataBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.util.OrderUtil;
import com.ixiangni.util.StateLayout;
import com.nevermore.oceans.utils.ListUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 飞机票列表
 *
 * @ClassName:PTListActivity
 * @PackageName:com.ixiangni.app.missyouservice
 * @Create On 2017/7/26 0026   14:15
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/26 0026 handongkeji All rights reserved.
 */
public class PTListActivity extends BaseActivity {

    @Bind(R.id.tv_from_to)
    TextView tvFromTo;
    @Bind(R.id.tv_day_before)
    TextView tvDayBefore;
    @Bind(R.id.tv_day_after)
    TextView tvDayAfter;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.tv_time_show)
    TextView tvTimeShow;
    private String star;
    private String end;
    private String date;
    private String startCity;
    private String endCity;
    private PlaneAdapter mAdapter;
    private DateParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptlist);
        ButterKnife.bind(this);

        Intent intent = getIntent();


        params = intent.getParcelableExtra(XNConstants.plane_list_params);
        startCity = params.getStartCity();

        endCity = params.getEndCity();
        tvFromTo.setText(startCity + "-" + endCity);

        star = params.getStar();
        end = params.getEnd();
        date = params.getDate();

        mAdapter = new PlaneAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        stateLayout.setPlayAlphaAnim(true);
        stateLayout.setUpwihtRecyclerAdapter(mAdapter, "暂无机票");

        showTime();

        tvDayBefore.setOnClickListener(v -> {

            if (isLoading) {
                return;
            }
            date = OrderUtil.changeDay(date, -1);
            showTime();
            getList();
        });

        tvDayAfter.setOnClickListener(v -> {
            if (isLoading) {
                return;
            }
            date = OrderUtil.changeDay(date, 1);
            showTime();
            getList();
        });

        getList();
    }

    //展示当前时间
    private void showTime() {

        tvTimeShow.setText(OrderUtil.getMd(date));

        if (date.equals(DateUtil.getYmd(Calendar.getInstance().getTime()))) {
            //日期为今天则不允许选择前一天
            tvDayBefore.setEnabled(false);
        } else {
            tvDayBefore.setEnabled(true);
        }
    }


//    token	是	String	用户token
//    star	是	String	出发城市三字码
//    end	是	String	结束城市三字码
//    date	是	String	时间(例：2017-07-14)


    private boolean isLoading = false;

    private void getList() {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("star", star);
        params.put("end", end);
        params.put("date", date);

        isLoading = true;
        stateLayout.showLoadViewNoContent("加载中...");
        RemoteDataHandler.asyncPost(UrlString.URL_PLANE_TICKET_LIST, params, this, true, response -> {
            String json = response.getJson();
            log(json);

            if (stateLayout == null) {
                return;
            }

            isLoading = false;
            if (CommonUtils.isStringNull(json)) {
                stateLayout.loadingFinish();
                toast(Constants.CONNECT_SERVER_FAILED);
            } else {
                PTListBean ptListBean = new Gson().fromJson(json, PTListBean.class);
                if (1 == ptListBean.getStatus()) {
                    List<PlaneDataBean> data = ptListBean.getData();
                    if (ListUtil.isEmptyList(data)) {
                        stateLayout.showNoDataView("没有航班");
                    } else {

                        mAdapter.replaceAll(data);
                    }

                } else {
                    stateLayout.showNoDataView("错误");
                }
            }


        });
    }

    private class PlaneAdapter extends MyRvAdapter<PlaneDataBean> {


        public PlaneAdapter(Context context) {
            super(context, R.layout.item_plane_tpre);
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, PlaneDataBean data) {

            PlaneBaseBean base = data.getBase();

            //最低价格
            String miniPrice = base.getMinFare();
            holder.setText(R.id.tv_chuxing_mini_price, "¥" + miniPrice);

            //起飞时间
            String offTime = base.getOffTime();
            holder.setText(R.id.tv_start_time, OrderUtil.getHourMin(offTime));


            //降落时间
            String arriveTime = base.getArriveTime();
            holder.setText(R.id.tv_end_time, OrderUtil.getHourMin(arriveTime));


            //总时长
            String runTime = base.getRunTime();
            holder.setText(R.id.tv_duration, runTime);

            //起飞机场
            String startPortName = base.getStartPortName();
            String startPort = base.getStartPort();

//            holder.setText(R.id.tv_start_city, startCity + base.getStartT());
            holder.setText(R.id.tv_start_city, startPortName);
            //降落机场
            String endPortName = base.getEndPortName();
//            holder.setText(R.id.tv_end_city, endCity + base.getEndT());
            holder.setText(R.id.tv_end_city, endPortName);


            String carrinerName = base.getCarrinerName();
            String flightNo = base.getFlightNo();
            String flight = carrinerName + " " + flightNo;
            holder.setText(R.id.tv_flight, flight);

            holder.setOnItemChlidClickListener(R.id.fl_root, v -> {
                Intent intent = new Intent(PTListActivity.this, CabActivity.class);
                intent.putExtra(XNConstants.flight_data, data);
                intent.putExtra(XNConstants.plane_list_params, params);
                startActivity(intent);
            });

        }
    }


}
