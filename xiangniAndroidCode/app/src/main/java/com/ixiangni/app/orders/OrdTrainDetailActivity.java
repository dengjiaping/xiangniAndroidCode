package com.ixiangni.app.orders;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.interfaces.ICallback;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.DateUtil;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.OrdTrainInfo;
import com.ixiangni.bean.OrderBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.Paydialog;
import com.ixiangni.interfaces.IOrdRefresh;
import com.ixiangni.presenters.OrderPresenter;
import com.ixiangni.presenters.contract.MyPresenter;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrdTrainDetailActivity extends BaseActivity implements OnResult<OrderBean> {

    @Bind(R.id.tv_order_state)
    TextView tvOrderState;
    @Bind(R.id.tv_start_port)
    TextView tvStartPort;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.tv_start_date)
    TextView tvStartDate;
    @Bind(R.id.tv_flight)
    TextView tvFlight;
    @Bind(R.id.tv_duration)
    TextView tvDuration;
    @Bind(R.id.tv_end_port)
    TextView tvEndPort;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.tv_end_date)
    TextView tvEndDate;
    @Bind(R.id.ll_traval)
    LinearLayout llTraval;
    @Bind(R.id.tv_cab_name)
    TextView tvCabName;
    @Bind(R.id.tv_policy_price)
    TextView tvPolicyPrice;
    @Bind(R.id.ll_price)
    LinearLayout llPrice;
    @Bind(R.id.tv_passanger_name)
    TextView tvPassangerName;
    @Bind(R.id.tv_passanger_phone)
    TextView tvPassangerPhone;
    @Bind(R.id.tv_passanger_idno)
    TextView tvPassangerIdno;
    @Bind(R.id.tv_order_num)
    TextView tvOrderNum;
    @Bind(R.id.tv_order_time)
    TextView tvOrderTime;
    @Bind(R.id.tv_jiage)
    TextView tvJiage;
    @Bind(R.id.tv_need_pay)
    TextView tvNeedPay;
    @Bind(R.id.tv_pay_now)
    TextView tvPayNow;
    private String orderid;
    private OrderPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ord_train_detail);
        ButterKnife.bind(this);

        orderid = getIntent().getStringExtra(XNConstants.orderid);
        presenter = new OrderPresenter();
        initData();
    }

    private void initData() {
        showProgressDialog("加载中...", true);

        presenter.getOrderDetail(this, orderid, this);
    }

    @Override
    public void onSuccess(OrderBean orderBean) {
        if (tvPayNow != null) {
            dismissProgressDialog();

            setData(orderBean);
        }
    }

    private void setData(OrderBean orderBean) {
        int orderstatus = orderBean.getOrderstatus();
        String status = orderstatus == 0 ? "待付款" : "已付款";
        tvOrderState.setText(String.format(Locale.CHINA, "订单状态:%s", status));

        if (orderstatus != 0) {
            tvPayNow.setVisibility(View.GONE);
        } else {
            tvPayNow.setText("立即支付");
            final Paydialog paydialog = new Paydialog(this);

            paydialog.setPasswordCallback(new ICallback<String>() {
                @Override
                public void call(String s) {

                    pay(orderBean.getOrderid() + "", s);
                }
            });

            tvPayNow.setOnClickListener(v -> {
                paydialog.show();
            });

        }
        double orderprice = orderBean.getOrderprice();
        tvNeedPay.setText(getString(R.string.rmb) + orderprice);


        String shanglvstr = orderBean.getShanglvstr();
        OrdTrainInfo ordTrainInfo = new Gson().fromJson(shanglvstr, OrdTrainInfo.class);

        //起点站
        tvStartPort.setText(ordTrainInfo.getSCity());

        //终点站
        tvEndPort.setText(ordTrainInfo.getECity());
        //出发时间

        tvStartTime.setText(ordTrainInfo.getSTime());
        //到达时间
        tvEndTime.setText(ordTrainInfo.getETime());
        //车次
        tvFlight.setText(orderBean.getDescribe1());

        //总时间
        String runTime = ordTrainInfo.getRunTime();
        int min = 0;
        try {
            min = Integer.parseInt(runTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        tvDuration.setText(String.format(Locale.CHINA, "%d小时%d分钟", min / 60, min % 60));
        //出发日期
        tvStartDate.setText(orderBean.getDescribe2());

        //到达日期
        String startTime = orderBean.getDescribe2() + " " + ordTrainInfo.getSTime();
        String format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.CHINA);

        String ednDate = orderBean.getDescribe2();
        try {
            Date parse = sf.parse(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);


            calendar.set(Calendar.HOUR_OF_DAY, min / 60);
            calendar.set(Calendar.MINUTE, min % 60);
            ednDate = DateUtil.getYmd(calendar.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvEndDate.setText(ednDate);



        List<OrdTrainInfo.PsgInfoBean> psgInfo = ordTrainInfo.getPsgInfo();

        if (psgInfo != null && psgInfo.size() > 0) {
            OrdTrainInfo.PsgInfoBean psgInfoBean = psgInfo.get(0);
            //座位
            String seatType = psgInfoBean.getSeatType();
            tvCabName.setText(seatType);
            //姓名
            tvPassangerName.setText(psgInfoBean.getPsgName());
            //手机
            tvPassangerPhone.setText(psgInfoBean.getPhone());
            //身份证号
            tvPassangerIdno.setText(psgInfoBean.getCardNo());

            //价格
            tvPolicyPrice.setText(""+orderBean.getOrderprice());


        }else {
            tvPassangerName.setText("无");
        }

        //订单号
        tvOrderNum.setText(orderBean.getShanglvsysno());

        //订单时间
        long ordercreatetime = orderBean.getOrdercreatetime();
        Calendar ca = Calendar.getInstance();
        String timeStr = DateUtil.getTimeStr(ca.getTime(), DateUtil.DF_POINT);
        tvOrderTime.setText(timeStr);
    }

    private void pay(String orderid, String password) {

        showProgressDialog("支付中...", false);

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("orderid", orderid);
        params.put("paypassword", password);
        MyPresenter.request(this, UrlString.URL_PAY_TRAIN_ORDER, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                if (tvPayNow != null) {
                    dismissProgressDialog();
                    toast("支付成功");

                    //刷新火车票订单
                    SuperObservableManager.notify(IOrdRefresh.IRefreshTrain.class, IOrdRefresh.IRefreshTrain::onRefresh);
                    onBackPressed();
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                if (tvPayNow != null) {
                    dismissProgressDialog();
                    toast(errorMsg);
                }
            }
        });


    }

    @Override
    public void onFailed(String errorMsg) {
        if (tvPayNow != null) {

            toast(errorMsg);
            dismissProgressDialog();
        }
    }


}
