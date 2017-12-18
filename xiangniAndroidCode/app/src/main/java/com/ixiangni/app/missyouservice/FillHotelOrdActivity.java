package com.ixiangni.app.missyouservice;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handongkeji.interfaces.ICallback;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.RegexUtils;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.RoomInfo;
import com.ixiangni.bean.RoomYuding;
import com.ixiangni.common.XNConstants;
import com.ixiangni.dialog.Paydialog;
import com.ixiangni.presenters.HotelPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 填写酒店订单
 *
 * @ClassName:FillHotelOrdActivity
 * @PackageName:com.ixiangni.app.missyouservice
 * @Create On 2017/7/25 0025   17:32
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/25 0025 handongkeji All rights reserved.
 */
public class FillHotelOrdActivity extends BaseActivity implements Paydialog.OnPasswordCorrect, ICallback<String>,OnResult<String> {

    @Bind(R.id.iv_room)
    ImageView ivRoom;
    @Bind(R.id.tv_room_type)
    TextView tvRoomType;
    @Bind(R.id.tv_room_size)
    TextView tvRoomSize;
    @Bind(R.id.tv_ruzhu)
    TextView tvRuzhu;
    @Bind(R.id.tv_days_info)
    TextView tvDaysInfo;
    @Bind(R.id.rl_date)
    RelativeLayout rlDate;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.edt_name)
    EditText edtName;
    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.tv_idcard_num)
    TextView tvIdcardNum;
    @Bind(R.id.tv_jiage)
    TextView tvJiage;
    @Bind(R.id.tv_need_pay)
    TextView tvNeedPay;
    @Bind(R.id.tv_submit_order)
    TextView tvSubmitOrder;
    @Bind(R.id.edt_phone_num)
    EditText edtPhoneNum;
    @Bind(R.id.edt_id_card_num)
    EditText edtIdCardNum;
    private RoomInfo info;

    private final int REQUEST_CODE_TIME = 2;
    private String endDate;
    private String startDate;
    private HotelPresenter presenter;
    private RoomYuding.DataBean mRoomBean;
    private Paydialog paydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_hotel_ord);
        ButterKnife.bind(this);

        info = (RoomInfo) getIntent().getSerializableExtra(XNConstants.HOTEL_INFO);

        tvRoomType.setText(info.getRoomType());
        tvRoomSize.setText(info.getRoomSize());
        Glide.with(this).load(info.getRoomPic()).placeholder(R.mipmap.loading_rect).into(ivRoom);

        initView();

    }


    private void initView() {
        rlDate.setOnClickListener(v -> {


            Intent intent = new Intent(this, SelectTimeActivity.class);
            intent.putExtra(XNConstants.HOTEL_INFO, info);
            startActivityForResult(intent, REQUEST_CODE_TIME);

        });

        tvSubmitOrder.setOnClickListener(v -> {
            preSubmit();
        });

        paydialog = new Paydialog(this);

        paydialog.setPasswordCallback(this);
        showProgressDialog("加载中...",true);
        //检查是否设置支付密码
        paydialog.checkSetPayPassword(this, edtIdCardNum, new ICallback<String>() {
            @Override
            public void call(String s) {
                dismissProgressDialog();
            }
        });

    }

    /**
     * 开始提交订单
     */
    private void preSubmit() {
        String name = edtName.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            toast("请填写入住人真实姓名");
            return;
        }

        info.setContacts(name);

        String phone = edtPhoneNum.getText().toString().toString();
        if(TextUtils.isEmpty(phone)){
            toast("请填写入住人手机号");
            return;
        }
        if(!RegexUtils.checkMobile(phone)){
            toast("手机号不正确");
            return;
        }

        info.setPhone(phone);


        String idCard = edtIdCardNum.getText().toString().toString();
        if(TextUtils.isEmpty(idCard)){
            toast("请填写入住人身份证号");
            return;
        }


        if(!RegexUtils.checkIdCard(idCard)){
            toast("身份证号不正确");
            return;
        }
        info.setIdno(idCard);


        if(TextUtils.isEmpty(startDate)||TextUtils.isEmpty(endDate)){
            toast("请选择入住离店日期");
            return;
        }

        info.setTm1(startDate);
        info.setTm2(endDate);

        if(mRoomBean==null){
            toast("正在查询酒店价格...");
            return;
        }

        if(mRoomBean.getRoomtype().equals("1")){
            toast("该房间不支持预订,请选择其他房间");
            return;
        }

        info.setTitle(mRoomBean.getRoomName());

        info.setAmount(mRoomBean.getPrices().getTotalPrice());


        paydialog.show();



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TIME && resultCode == RESULT_OK) {
            endDate = data.getStringExtra(XNConstants.endDate);
            startDate = data.getStringExtra(XNConstants.startDate);
            String timeInfo = getTimeInfo(startDate, endDate);
            tvDaysInfo.setText(timeInfo);

            getRoomInfo();
        }
    }


    private void getRoomInfo() {
        if (presenter == null) {
            presenter = new HotelPresenter();
        }

        showProgressDialog("查询价格中...",true);
        presenter.getRoomYudingInfo(this, info, startDate, endDate, new OnResult<RoomYuding.DataBean>() {
            @Override
            public void onSuccess(RoomYuding.DataBean dataBean) {
                if(edtIdCardNum!=null){
                    dismissProgressDialog();

                    mRoomBean=dataBean;

                    RoomYuding.DataBean.PricesBean prices = dataBean.getPrices();
                    String totalPrice = prices.getTotalPrice();
                    tvNeedPay.setText(getString(R.string.rmb)+totalPrice);

                }

            }

            @Override
            public void onFailed(String errorMsg) {
                if(edtIdCardNum!=null){
                    dismissProgressDialog();
                    toast(errorMsg);
                }

            }
        });

    }

    /**
     * 设置住离店时间
     *
     * @param startDate
     * @param endDate
     */
    public static String getTimeInfo(String startDate, String endDate) {

        String[] start = startDate.split("-");

        String[] end = endDate.split("-");

        String night = "";
        try {
            int dayEnd = Integer.parseInt(end[2]);
            int dayStart = Integer.parseInt(start[2]);
            night += (dayEnd - dayStart);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        StringBuilder sb = new StringBuilder();
        sb.append(start[1]).append("月").append(start[2]).append("日").append("-").append(end[1]).append("月").append(end[2]).append("日")
                .append("  ").append("共").append(night).append("晚");

        return sb.toString();
    }


    @Override
    public void onCorrectInpu() {

    }

    @Override
    public void call(String s) {
        paydialog.dismiss();
        info.setPaypassword(s);
        showProgressDialog("提交中...",false);
        presenter.submitOrder(this,info,"0",this);

    }

    @Override//提交成功
    public void onSuccess(String s) {
        if(edtIdCardNum!=null){
            toast("提交成功");
            dismissProgressDialog();
            onBackPressed();
        }
    }

    @Override//提交失败
    public void onFailed(String errorMsg) {
        if(edtIdCardNum!=null){
            toast(errorMsg);
            dismissProgressDialog();

        }

    }
}
