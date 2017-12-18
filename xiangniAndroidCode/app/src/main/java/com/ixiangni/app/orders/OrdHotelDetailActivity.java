package com.ixiangni.app.orders;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.hyphenate.chatuidemo.Constant;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.missyouservice.FillHotelOrdActivity;
import com.ixiangni.bean.OrderBean;
import com.ixiangni.bean.OrderInfoBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.OrderPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 酒店订单详情
 *
 * @ClassName:OrdHotelNoPayActivity
 * @PackageName:com.ixiangni.app.orders
 * @Create On 2017/7/24 0024   15:06
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/24 0024 handongkeji All rights reserved.
 */
public class OrdHotelDetailActivity extends BaseActivity {

    @Bind(R.id.iv_room)
    ImageView ivRoom;
    @Bind(R.id.tv_room_type)
    TextView tvRoomType;
    @Bind(R.id.tv_room_size)
    TextView tvRoomSize;
    @Bind(R.id.tv_ruzhu)
    TextView tvRuzhu;
    @Bind(R.id.tv_time_info)
    TextView tvTimeInfo;
    @Bind(R.id.rl_date)
    RelativeLayout rlDate;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_person_name)
    TextView tvPersonName;
    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.tv_person_phone_num)
    TextView tvPersonPhoneNum;
    @Bind(R.id.tv_idcard_num)
    TextView tvIdcardNum;
    @Bind(R.id.tv_person_id_num)
    TextView tvPersonIdNum;
    @Bind(R.id.tv_the_num)
    TextView tvTheNum;
    @Bind(R.id.tv_order_num)
    TextView tvOrderNum;
    @Bind(R.id.tv_time)
    TextView tvTime;
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
        setContentView(R.layout.activity_ord_hotel_no_pay);
        ButterKnife.bind(this);

        orderid = getIntent().getStringExtra(XNConstants.orderid);
        presenter = new OrderPresenter();

        tvPayNow.setText("已预订");

        getOrderInfo();
    }

    private void getOrderInfo() {

        showProgressDialog("加载中...",true);
        presenter.getOrderDetail(this, orderid, new OnResult<OrderBean>() {
            @Override
            public void onSuccess(OrderBean orderBean) {
                if(ivRoom!=null){
                    dismissProgressDialog();

                    setOrderInfo(orderBean);
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                if(ivRoom!=null){
                    dismissProgressDialog();
                    toast(errorMsg);
                }
            }
        });

    }

    private void setOrderInfo(OrderBean orderBean) {
        String orderpic = orderBean.getOrderpic();
        Glide.with(this).load(orderpic).placeholder(R.mipmap.loading_rect).placeholder(R.mipmap.loading_rect).fallback(R.mipmap.loading_rect).into(ivRoom);
        tvRoomType.setText(orderBean.getDescribe1());
        tvRoomSize.setText(orderBean.getDescribe2() + " | " + orderBean.getDescribe3() + "平方米");

        OrderInfoBean shanglvstr = new Gson().fromJson(orderBean.getShanglvstr(), OrderInfoBean.class);

        //日期
        tvTimeInfo.setText(FillHotelOrdActivity.getTimeInfo(shanglvstr.getTm1(),shanglvstr.getTm2()));
        tvPersonName.setText(shanglvstr.getContacts());
        tvPersonPhoneNum.setText(shanglvstr.getPhone());
        tvPersonIdNum.setText(shanglvstr.getEmail());


        //订单号
        tvOrderNum.setText(orderBean.getShanglvsysno());
        String orderTime = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(new Date(orderBean.getOrdercreatetime()));
        tvOrderTime.setText(orderTime);
        String price = getString(R.string.rmb) + orderBean.getOrderprice();
        tvNeedPay.setText(price);
    }
}
