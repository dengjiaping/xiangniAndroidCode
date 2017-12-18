package com.ixiangni.app.missyouservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.util.ViewHelper;
import com.handongkeji.interfaces.ICallback;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.CabListBean;
import com.ixiangni.bean.DateParams;
import com.ixiangni.bean.OrderResultBean;
import com.ixiangni.bean.PlaneBaseBean;
import com.ixiangni.bean.PolicyBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.Paydialog;
import com.ixiangni.util.OrderUtil;

import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 飞机票订单填写
 *
 * @ClassName:FillPlaneOrdActivity
 * @PackageName:com.ixiangni.app.missyouservice
 * @Create On 2017/7/26 0026   15:23
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/26 0026 handongkeji All rights reserved.
 */
public class FillPlaneOrdActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_traval)
    LinearLayout llTraval;
    @Bind(R.id.tv_cab_name)
    TextView tvCabName;
    @Bind(R.id.tv_policy_price)
    TextView tvPolicyPrice;
    @Bind(R.id.ll_price)
    LinearLayout llPrice;
    @Bind(R.id.tv_jijian_price)
    TextView tvJijianPrice;
    @Bind(R.id.tv_oil_price)
    TextView tvOilPrice;
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
    private DateParams params;
    private CabListBean cabListBean;
    private PolicyBean policyBean;
    private PlaneBaseBean baseBean;
    private Paydialog paydialog;
    private String name;
    private String phone;
    private String idnum;
    private String password;
    private String discount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_plane_ord);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        params = intent.getParcelableExtra(XNConstants.plane_list_params);
        cabListBean = intent.getParcelableExtra(XNConstants.cab_data);
        policyBean = intent.getParcelableExtra(XNConstants.policy_info);
        baseBean = intent.getParcelableExtra(XNConstants.plane_base);
        discount = intent.getStringExtra(XNConstants.discount);

        ViewHelper helper = new ViewHelper(llTraval);
        initHeader(helper, baseBean);

        initView();

        paydialog = new Paydialog(this);
        showProgressDialog("加载中...", true);
        paydialog.checkSetPayPassword(this, tvSubmitOrder, new ICallback<String>() {
            @Override
            public void call(String s) {
                dismissProgressDialog();
            }
        });

    }

    private void initView() {
        //舱位
        tvCabName.setText(String.format("%s%s", cabListBean.getCabinName(), discount));
        //优惠价格
        tvPolicyPrice.setText("" + policyBean.getSale());
        //基建费
        String tax = String.format(Locale.CHINA, "%s%d", getString(R.string.rmb), baseBean.getTax());
        tvJijianPrice.setText(tax);
        //燃油费
        tvOilPrice.setText(String.format(Locale.CHINA, "%s%d", getString(R.string.rmb), baseBean.getOil()));

        int totalPrice = baseBean.getTax() + baseBean.getOil() + policyBean.getSale();

        String orderPrice = String.format(Locale.CHINA, "%s%d", getString(R.string.rmb), totalPrice);
        tvNeedPay.setText(orderPrice);

        tvSubmitOrder.setOnClickListener(this);

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

        paydialog.show();
        paydialog.setPasswordCallback(new ICallback<String>() {
            @Override
            public void call(String s) {

                password = s;
                submitOrder();
            }


        });
    }


    //飞机票下单
//    token	是	String	用户token
//    StartPortName	是	String	出发机场名称(飞机票列表中的base中的StartPortName)
//    EndPortName	是	String	到达机场名称(飞机票列表中的base中的EndPortName)
//    FlightNo	是	String	航班号(飞机票列表中的base中的FlightNo)
//    CarrinerName	是	String	航空公司简称(飞机票列表中的base中的CarrinerName)
//    OffTime	是	String	起飞时间(飞机票列表中的base中的OffTime)
//    ArriveTime	是	String	到达时间(飞机票列表中的base中的ArriveTime)
//    RunTime	是	String	飞行时间(飞机票列表中的base中的RunTime)
//    CabinName	是	String	舱位名称(飞机票列表中的CabList中的CabinName)
//    discount	是	String	优惠政策名称
//    Tax			机场建设费(飞机票列表中的base中的Tax)
//    Oil			燃油费(飞机票列表中的base中的Oil)
//    sale			销售价格(政策列表中的sale)
//    goflightdata			航班信息（取自飞机票列表中的CabList中的Bookpara）
//    policy			政策信息(取政策列表中的bookdata)
//    idno			身份证号
//    mobile			手机号
//    pname			姓名
//    paypassword			支付密码
    private void submitOrder() {

        HashMap<String, String> params1 = new HashMap<>();
        params1.put(Constants.token, MyApp.getInstance().getUserTicket());
        params1.put("StartPortName", baseBean.getStartPortName());
        params1.put("EndPortName", baseBean.getEndPortName());
        params1.put("FlightNo", baseBean.getFlightNo());
        params1.put("CarrinerName", baseBean.getCarrinerName());
        params1.put("OffTime", baseBean.getOffTime());
        params1.put("ArriveTime", baseBean.getArriveTime());
        params1.put("RunTime", baseBean.getRunTime());
        params1.put("CabinName", cabListBean.getCabinName());
        params1.put("discount", discount);
        params1.put("Tax", "" + baseBean.getTax());
        params1.put("Oil", "" + baseBean.getOil());
        params1.put("sale", policyBean.getSale() + "");
        params1.put("goflightdata", cabListBean.getBookpara());
        params1.put("policy", policyBean.getBookdata());
        params1.put("idno", idnum);
        params1.put("mobile", phone);
        params1.put("pname", name);
        params1.put("paypassword", password);
        log(params1.toString());
        showProgressDialog("提交订单", false);
        RemoteDataHandler.asyncPost(UrlString.URL_SUBMIT_PLANE_ORDER, params1, this, false, response -> {


            if (tvSubmitOrder == null) {
                return;
            }
            String json = response.getJson();
            log(json);
            if (CommonUtils.isStringNull(json)) {
                toast(Constants.CONNECT_SERVER_FAILED);
            } else {
                dismissProgressDialog();
                OrderResultBean orderResultBean = new Gson().fromJson(json, OrderResultBean.class);
                if (1 == orderResultBean.getStatus()) {
                    toast("提交成功");
                    finish();
                } else {
                    if (orderResultBean.getStatus() == 4) {
                        toast("代理商余额不足");
                    }
                    if (orderResultBean.getStatus() == 7) {
                        toast("银信币余额不足");
                    } else {

                        toast(orderResultBean.getMessage());
                    }
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            edtPassangerIdnum.clearFocus();
            edtPassengerPhone.clearFocus();
            edtPassangerName.clearFocus();
        }
        return super.onKeyDown(keyCode, event);

    }
}
