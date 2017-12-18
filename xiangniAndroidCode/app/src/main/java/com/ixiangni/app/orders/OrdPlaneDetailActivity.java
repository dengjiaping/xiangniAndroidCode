package com.ixiangni.app.orders;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.ixiangni.bean.OrdPlaneInfo;
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

/**
 * 飞机票订单未支付
 *
 * @ClassName:OrdPlaneNoPayActivity
 * @PackageName:com.ixiangni.app.orders
 * @Create On 2017/7/24 0024   15:10
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/24 0024 handongkeji All rights reserved.
 */
public class OrdPlaneDetailActivity extends BaseActivity implements OnResult<OrderBean> {

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
        setContentView(R.layout.activity_ord_plane_no_pay);
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
        }else {
            tvPayNow.setText("立即支付");
            final Paydialog paydialog = new Paydialog(this);

            paydialog.setPasswordCallback(new ICallback<String>() {
                @Override
                public void call(String s) {

                    pay(orderBean.getOrderid()+"",s);
                }
            });

            tvPayNow.setOnClickListener(v -> {
                paydialog.show();
            });

        }
        double orderprice = orderBean.getOrderprice();
        tvNeedPay.setText(getString(R.string.rmb) + orderprice);

        String shanglvstr = orderBean.getShanglvstr();
        OrdPlaneInfo info = new Gson().fromJson(shanglvstr, OrdPlaneInfo.class);
        //出发机场
        String startPortName = info.getStartPortName();
        tvStartPort.setText(startPortName);
        //到达机场
        String endPortName = info.getEndPortName();
        tvEndPort.setText(endPortName);
        tvFlight.setText(orderBean.getDescribe3());

        //经过总时间
        tvDuration.setText(info.getRunTime());

        String offTime = info.getOffTime();
        //起飞时间
        tvStartTime.setText(getTime(offTime));

        String arriveTime = info.getArriveTime();
        //降落时间
        tvEndTime.setText(getTime(arriveTime));

        String sDate = info.getSDate();
        //起飞日期
        tvStartDate.setText(sDate);


        String startTime = sDate + " " + getTime(offTime);
        String format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.CHINA);

        String ednDate = sDate;
        try {
            Date parse = sf.parse(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);

            String runTime = info.getRunTime();
            runTime = runTime.replace("小时", ":").replace("分钟", ":");
            String[] split = runTime.split(":");
            if (split.length >= 2) {
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(split[1]));
            }
            ednDate = DateUtil.getYmd(calendar.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
//到达日期
        tvEndDate.setText(ednDate);

        tvCabName.setText(info.getCabinName());

        //价格
        tvPolicyPrice.setText(info.getAmount());

        List<OrdPlaneInfo.PassengerBean> passenger = info.getPassenger();
        if(passenger!=null&&passenger.size()>0){
            OrdPlaneInfo.PassengerBean passengerBean = passenger.get(0);
            //姓名
            tvPassangerName.setText(passengerBean.getPname());
            //手机
            tvPassangerPhone.setText(passengerBean.getMobile());
            //身份证
            tvPassangerIdno.setText(passengerBean.getIdno());


        }else {
            tvPassangerName.setText("没有乘客");
        }
        //订单号
        tvOrderNum.setText(orderBean.getShanglvsysno());

        //订单时间
        long ordercreatetime = orderBean.getOrdercreatetime();
        Calendar ca = Calendar.getInstance();
        String timeStr = DateUtil.getTimeStr(ca.getTime(), DateUtil.DF_POINT);
        tvOrderTime.setText(timeStr);

    }
//    token	是	String	用户token
//    orderid	是	String	订单id
//    paypassword	是	String	支付密码

    private void pay(String orderid, String password) {

        showProgressDialog("支付中...",false);

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("orderid",orderid);
        params.put("paypassword",password);
        MyPresenter.request(this, UrlString.URL_PAY_PLANE_ORDER, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
               if(tvPayNow!=null){
                   dismissProgressDialog();
                   toast("支付成功");

                   //刷新飞机订单
                   SuperObservableManager.notify(IOrdRefresh.IRefreshPlane.class, IOrdRefresh.IRefreshPlane::onRefresh);
                   onBackPressed();
               }
            }

            @Override
            public void onFailed(String errorMsg) {
                if(tvPayNow!=null){
                    dismissProgressDialog();
                    toast(errorMsg);
                }
            }
        });


    }

    /**
     * 23:10:00 ->23:10
     *
     * @param time
     * @return
     */
    private String getTime(String time) {

        if(TextUtils.isEmpty(time)){
            return "错误";
        }

        String[] split = time.split(":");
        if (split.length >= 2) {
            return split[0] + ":" + split[1];
        }

        return "";
    }

    @Override
    public void onFailed(String errorMsg) {
        if (tvPayNow != null) {

            toast(errorMsg);
            dismissProgressDialog();
        }
    }


}
