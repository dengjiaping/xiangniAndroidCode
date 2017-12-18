package com.ixiangni.app.missyouservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.ICallback;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.utils.DateUtil;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.OrderResultBean;
import com.ixiangni.bean.TParams;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.Paydialog;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 火车票订单填写
 *
 * @ClassName:FillTrainOrdActivity
 * @PackageName:com.ixiangni.app.missyouservice
 * @Create On 2017/7/26 0026   14:58
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/26 0026 handongkeji All rights reserved.
 */
public class FillTrainOrdActivity extends BaseActivity implements View.OnClickListener {

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
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.edt_passanger_name)
    EditText edtPassangerName;
    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.edt_passenger_phone)
    EditText edtPassengerPhone;
    @Bind(R.id.tv_idcard_num)
    TextView tvIdcardNum;
    @Bind(R.id.edt_passanger_idnum)
    EditText edtPassangerIdnum;
    @Bind(R.id.tv_jiage)
    TextView tvJiage;
    @Bind(R.id.tv_need_pay)
    TextView tvNeedPay;
    @Bind(R.id.tv_submit_order)
    TextView tvSubmitOrder;
    private TParams tParams;

    private String name;
    private String phone;
    private String idnum;
    private String password;
    private Paydialog paydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_train_ord);
        ButterKnife.bind(this);
        tParams = getIntent().getParcelableExtra(XNConstants.train_params);
        initView();
    }

    private void initView() {

        //出发城市
        tvStartPort.setText(tParams.getSCity());
        //到达城市
        tvEndPort.setText(tParams.getECity());
        //出发时间
        tvStartTime.setText(tParams.getSTime());
        //到达时间
        tvEndTime.setText(tParams.getETime());
        String runTime = tParams.getRunTime();


        int min = 0;
        try {
            min = Integer.parseInt(runTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        String time = String.format(Locale.CHINA, "%d时%d分", min / 60, min % 60);
        //花费时间
        tvDuration.setText(time);
        //列次
        String flight = tParams.getTrainType() + " " + tParams.getTrainNo();
        tvFlight.setText(flight);

        tvCabName.setText(tParams.getSeatType());
        //价格
        tvPolicyPrice.setText(tParams.getAmount());
        tvNeedPay.setText(tParams.getAmount());

        //出发日期
        tvStartDate.setText(tParams.getSDate());


        long end = 0;
        try {
            end = DateUtil.DF_CENTER_LINE.parse(tParams.getSDate()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(end);
        String sTime = tParams.getSTime();
        String[] split = sTime.split(":");

        try {
            instance.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
            instance.set(Calendar.MINUTE, Integer.parseInt(split[1]));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        instance.add(Calendar.MINUTE, min);
        String dateString = DateUtil.getDateString(instance.getTime());
        tvEndDate.setText(dateString);

//        instance.set


        paydialog = new Paydialog(this);
        showProgressDialog("加载中...", true);
        paydialog.checkSetPayPassword(this, tvSubmitOrder, new ICallback<String>() {
            @Override
            public void call(String s) {
                dismissProgressDialog();
            }
        });

        tvSubmitOrder.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        name = edtPassangerName.getText().toString().trim();
        phone = edtPassengerPhone.getText().toString().trim();
        idnum = edtPassangerIdnum.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            toast("请输入姓名");
            return;
        }

        if (!checkPhoneNum(phone)) {
            return;
        }

        if (!checkIdNum(idnum)) {
            return;
        }
        tParams.setPsgName(name);
        tParams.setPhone(phone);
        tParams.setCardNo(idnum);

        paydialog.show();
        paydialog.setPasswordCallback(new ICallback<String>() {
            @Override
            public void call(String s) {

                password = s;
                tParams.setPaypassword(password);
                hideSoftKeyboard();
                submitOrder();
            }

        });
    }


//    token	是	String	用户token
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
//    SeatType	是	String	席别。火车票列表中的SeatList中的type(例:无座)
//    paypassword	是	String	支付密码
//    RunTime	是	String	运行时间。火车票列表中的RunTime

    private void submitOrder() {


        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("TrainNo", tParams.getTrainNo());
        params.put("SCity", tParams.getSCity());
        params.put("ECity", tParams.getECity());
        params.put("STime", tParams.getSTime());
        params.put("ETime", tParams.getETime());
        params.put("SDate", tParams.getSDate());
        params.put("Amount", tParams.getAmount());
        params.put("CardNo", tParams.getCardNo());
        params.put("Phone", tParams.getPhone());
        params.put("PsgName", tParams.getPsgName());
        params.put("SeatType", tParams.getSeatType());
        params.put("paypassword", tParams.getPaypassword());
        params.put("RunTime", tParams.getRunTime());

        showProgressDialog("提交中...", true);
        RemoteDataHandler.asyncPost(UrlString.URL_SUBMIT_TRAIN_ORDER, params, this, false, response -> {
            if (tvSubmitOrder != null) {
                dismissProgressDialog();
                String json = response.getJson();
                log(json);
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    OrderResultBean orderResultBean = new Gson().fromJson(json, OrderResultBean.class);
                    if (1 == orderResultBean.getStatus()) {

                        toast("提交成功");
                        finish();
                    }
                    if (orderResultBean.getStatus() == 4) {
                        toast("代理商余额不足");
                    }
                    if (orderResultBean.getStatus() == 7) {
                        toast("银信币余额不足");
                    } else {

                        toast(orderResultBean.getMessage());
                    }
                    toast(orderResultBean.getMessage());
                }
            }

        });
    }

}
