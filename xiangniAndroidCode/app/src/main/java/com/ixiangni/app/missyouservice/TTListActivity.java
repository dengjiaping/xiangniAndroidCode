package com.ixiangni.app.missyouservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.utils.DateUtil;
import com.handongkeji.widget.NoScrollListView;
import com.ixiangni.adapter.MyRvAdapter;
import com.ixiangni.adapter.MyRvViewHolder;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.DateParams;
import com.ixiangni.bean.SeatBean;
import com.ixiangni.bean.TParams;
import com.ixiangni.bean.TTListBean;
import com.ixiangni.bean.TTicketBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.util.OrderUtil;
import com.ixiangni.util.StateLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nevermore.oceans.utils.ListUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 火车票列表
 *
 * @ClassName:TTListActivity
 * @PackageName:com.ixiangni.app.missyouservice
 * @Create On 2017/7/26 0026   14:31
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/26 0026 handongkeji All rights reserved.
 */
public class TTListActivity extends BaseActivity {

    @Bind(R.id.tv_from_to)
    TextView tvFromTo;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.tv_day_before)
    TextView tvDayBefore;
    @Bind(R.id.tv_day_after)
    TextView tvDayAfter;
    @Bind(R.id.tv_time_show)
    TextView tvTimeShow;
    private DateParams params;
    private TTAdapter mAdapter;

    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttlist);
        ButterKnife.bind(this);

        params = getIntent().getParcelableExtra(XNConstants.plane_list_params);

        date = params.getDate();
        log(date);
        tvFromTo.setText(params.getStartCity() + "-" + params.getEndCity());

        initView();
        getTrainTicketList();
    }

    private void initView() {
        mAdapter = new TTAdapter(this);
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        stateLayout.setUpwihtRecyclerAdapter(mAdapter, "车票已售完");

        showTime();

        tvDayBefore.setOnClickListener(v -> {
            if(isLoading){
                return;
            }
            date = OrderUtil.changeDay(date, -1);
            showTime();
            getTrainTicketList();
        });

        tvDayAfter.setOnClickListener(v -> {
            if(isLoading){
                return;
            }
            date = OrderUtil.changeDay(date, 1);
            showTime();
            getTrainTicketList();
        });
    }

    //展示当前时间
    private void showTime() {

        tvTimeShow.setText(OrderUtil.getMd(date));

        if(date.equals(DateUtil.getYmd(Calendar.getInstance().getTime()))){
            //日期为今天则不允许选择前一天
            tvDayBefore.setEnabled(false);
        }else {
            tvDayBefore.setEnabled(true);
        }
    }


//    token	是	String	用户token
//    star	是	String	出发城市名称
//    end	是	String	结束城市名称
//    date	是	String	乘车时间(例：2017-07-14)


    private boolean isLoading = false;//防止切换太频繁
    private void getTrainTicketList() {

        HashMap<String, String> params1 = new HashMap<>();
        params1.put(Constants.token, MyApp.getInstance().getUserTicket());
        params1.put("star", params.getStar());
        params1.put("end", params.getEnd());
        params1.put("date", date);

        isLoading=true;
        stateLayout.showLoadView();
        RemoteDataHandler.asyncPost(UrlString.URL_TRAIN_LIST, params1, this, true, response -> {
            String json = response.getJson();
            if (stateLayout != null) {

                isLoading=false;
                log(json);
                if (CommonUtils.isStringNull(json)) {
                    stateLayout.loadingFinish();
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    TTListBean ttListBean = new Gson().fromJson(json, TTListBean.class);
                    if (1 == ttListBean.getStatus()) {

                        mAdapter.setOpenPoi(-1);
                        List<TTicketBean> data = ttListBean.getData();

                        if(ListUtil.isEmptyList(data)){
                            stateLayout.showNoDataView("没有找到车次");
                        }else {

                        mAdapter.replaceAll(ttListBean.getData());
                        }

                    } else {
                        stateLayout.loadingFinish();
                        toast(ttListBean.getMessage());
                    }
                }

            }

        });
    }


    private class TTAdapter extends MyRvAdapter<TTicketBean> {


        private int openPoi = -1;

        public TTAdapter(Context context) {
            super(context, R.layout.item_tticket);
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, TTicketBean data) {


            //出发时间
            holder.setText(R.id.tv_start_time, data.getGoTime());
            //到达时间
            holder.setText(R.id.tv_end_time, data.getETime());

            //出发城市
            holder.setText(R.id.tv_start_city, data.getStationS());
            //到大城市
            holder.setText(R.id.tv_end_city, data.getStationE());
            //列次
            String flight = data.getTrainType() + " " + data.getTrainCode();
            holder.setText(R.id.tv_flight, flight);

            String runTime = data.getRunTime();


            int min = 0;
            try {
                min = Integer.parseInt(runTime);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            String time = String.format(Locale.CHINA, "%d时%d分", min / 60, min % 60);
            holder.setText(R.id.tv_duration, time);

            List<SeatBean> seatList = data.getSeatList();

            NoScrollListView listView = holder.getView(R.id.list_view);
            listView.setAdapter(new SeatAdapter(context, seatList));
            //是否展开
            listView.setVisibility(position == openPoi ? View.VISIBLE : View.GONE);


            //最低价
            if(ListUtil.isEmptyList(seatList)){
                holder.setText(R.id.tv_chuxing_mini_price,"无票");
            }else {
                double p = 2;
                try {
                    p += Double.parseDouble(seatList.get(0).getPrice());

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                holder.setText(R.id.tv_chuxing_mini_price,"¥"+p);

            }


            final View flTicket = holder.getView(R.id.fl_ticket);
            flTicket.setSelected(position == openPoi);
            holder.setOnItemChlidClickListener(R.id.fl_ticket, v -> {
                if (position != openPoi) {//展开座位列表
                    if (ListUtil.isEmptyList(seatList)) {
                        toast("无票");
                    } else {
                        openPoi = position;
                        notifyDataSetChanged();
                    }
                } else {//收起座位列表
                    listView.setVisibility(View.GONE);
                    flTicket.setSelected(false);
                    openPoi = -1;
                }
            });
        }

        public int getOpenPoi() {
            return openPoi;
        }

        public void setOpenPoi(int openPoi) {
            this.openPoi = openPoi;
        }
    }


    /*
    座位列表
     */
    private class SeatAdapter extends QuickAdapter<SeatBean> {

        public SeatAdapter(Context context, List<SeatBean> data) {
            super(context, R.layout.item_ticket, data);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, SeatBean seatBean) {
            helper.setText(R.id.tv_seat_type, seatBean.getType());
            String price = seatBean.getPrice();
            double p = 2;
            try {
                p += Double.parseDouble(price);

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            final TextView tvPrice = helper.getView(R.id.tv_seat_price);
            tvPrice.setText("" + p);

            TextView tvBuy = helper.getView(R.id.tv_buy);

            String count;
            int seatCount = 0;
            String shengyu = seatBean.getShengyu();
            try {
                seatCount = Integer.parseInt(shengyu);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            if (seatCount == 0) {
                count = "无票";
                tvBuy.setVisibility(View.GONE);
            } else {
                count = String.format(Locale.CHINA, "%d张", seatCount);
                tvBuy.setVisibility(View.VISIBLE);
                tvBuy.setOnClickListener(v -> {
                    TParams params = new TParams();
                    TTicketBean item = mAdapter.getItem(mAdapter.getOpenPoi());
                    params.setTrainNo(item.getTrainCode());
                    params.setSCity(item.getStationS());
                    params.setECity(item.getStationE());
                    params.setSTime(item.getGoTime());
                    params.setETime(item.getETime());
                    params.setSDate(date);
                    params.setAmount(tvPrice.getText().toString().trim());
                    params.setSeatType(seatBean.getType());
                    params.setRunTime(item.getRunTime());
                    params.setTrainType(item.getTrainType());
                    Intent intent = new Intent(TTListActivity.this, FillTrainOrdActivity.class);
                    intent.putExtra(XNConstants.train_params, params);
                    startActivity(intent);

                });
            }

            helper.setText(R.id.tv_seat_count, count);
        }
    }

//    TrainNo	是	String	车次编号
//    SCity	是	String	出发城市。火车票列表中的StationS
//    ECity	是	String	到达城市。火车票列表中的StationE
//    STime	是	String	出发时间。火车票列表中的GoTime
//    ETime	是	String	到达时间。火车票列表中的ETime
//    SDate	是	String	出发日期。查询火车票时传入的date(例:2017-07-21)
//    Amount	是	String	订单金额。火车票列表中的SeatList中的price+2

//    CardNo	是	String	身份证号
//    Phone	是	String	手机号
//    PsgName	是	String	乘车人姓名
//    paypassword	是	String	支付密码

//    SeatType	是	String	席别。火车票列表中的SeatList中的type(例:无座)
//    RunTime	是	String	运行时间。火车票列表中的RunTime


}
