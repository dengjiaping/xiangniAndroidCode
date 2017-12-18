package com.ixiangni.app.money;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.utils.DateUtil;
import com.handongkeji.utils.FormatUtil;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.bean.ShouzhiListBean;
import com.ixiangni.bean.UsableCoinBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.interfaces.OnUserInfoChange;
import com.ixiangni.ui.TopBar;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的钱包
 *
 * @ClassName:MineWalletActivity
 * @PackageName:com.ixiangni.app.money
 * @Create On 2017/6/21 0021   13:00
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class MineWalletActivity extends BaseActivity implements OnUserInfoChange {

    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.tv_usable_coin)
    TextView tvUsableCoin;

    @Bind(R.id.tv_get_cash)
    TextView tvGetCash;
    @Bind(R.id.tv_charge)
    TextView tvCharge;
    @Bind(R.id.tv_payment_manage)
    TextView tvPaymentManage;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    private ShouzhiAdapter mAdapter;
    private String userIdCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_wallet);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        userIdCard=intent.getStringExtra("userIdCard");

        SuperObservableManager.getInstance()
                .getObservable(OnUserInfoChange.class)
                .registerObserver(this);
        initView();
        initData();
    }

    private void initData() {
        getYue();
        getShouzhiRecord();
    }

    private static final String TAG = "MineWalletActivity";

    private boolean isFirstLoad = true;

    private void getShouzhiRecord() {
        //交易记录

        if (isFirstLoad) {
            stateLayout.showLoadView("加载中...");
            isFirstLoad = false;
        }
        HashMap<String, String> params1 = new HashMap<>();
        params1.put("token", MyApp.getInstance().getUserTicket());
        RemoteDataHandler.asyncPost(UrlString.URL_SHOUZHI_RECORD, params1, this, true, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData response) {
                if (stateLayout != null) {
//                    LogHelper.log(TAG, response.getJson());
                    smartPullLayout.stopPullBehavior();
                    String json = response.getJson();
                    if (CommonUtils.isStringNull(json)) {
                        toast(Constants.CONNECT_SERVER_FAILED);
                    } else {
                        ShouzhiListBean shouzhiListBean = new Gson().fromJson(json, ShouzhiListBean.class);
                        int status = shouzhiListBean.getStatus();
                        if (1 == status) {
                            mAdapter.replaceAll(shouzhiListBean.getData());
                        } else {
                            toast(shouzhiListBean.getMessage());
                        }
                    }

                }
            }
        });
    }

    private class ShouzhiAdapter extends QuickAdapter<ShouzhiListBean.DataBean> {

        public ShouzhiAdapter(Context context) {
            super(context, R.layout.item_business_record);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, ShouzhiListBean.DataBean dataBean) {

//            OrderStatus  0审核中；1审核通过；2审核不通过
            int ordertype = dataBean.getOrdertype();


            int orderstatus = dataBean.getOrderstatus();
            if (ordertype == 1) {//提现交易
                if (orderstatus == 12) {
                    helper.setText(R.id.tv_describe, "提现审核中");
                } else if (orderstatus == 13) {
                    helper.setText(R.id.tv_describe, "提现成功");
                } else {//失败14
                    helper.setText(R.id.tv_describe, "提现失败退款");
                }
            } else {//其他交易

                if (ordertype == 6) {
                    helper.setText(R.id.tv_describe, "转账支出");
                } else if (ordertype == 11) {
                    helper.setText(R.id.tv_describe, "转账收入");
                } else {
                    helper.setText(R.id.tv_describe, dataBean.getOrdername());
                }
            }

            long ordercreatetime = dataBean.getOrdercreatetime();
            String ymd = DateUtil.getYmd(ordercreatetime);
            helper.setText(R.id.tv_time, ymd);

            String type = "";

            switch (ordertype) {
                case 0:
                case 4:
                case 11:
                    type = "+";
                    break;


                case 1:
//                    if(orderstatus==0||orderstatus==1){
//                        type="-";
//                    }
                    if(orderstatus==13){
                        type="-";
                    }else if (orderstatus==14){
                        type="+";
                    }
                    break;

                case 2:
                case 3:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    type = "-";
                    break;
                default:
                    break;

            }

            type += String.format(Locale.CHINA, "%.2f", dataBean.getOrderprice());


            helper.setText(R.id.tv_money_des, type);

        }
    }

    private void getYue() {
        //可用余额查询
        HashMap<String, String> params = new HashMap<>();
        params.put("token", LoginHelper.getInstance().getToken(this));
        RemoteDataHandler.asyncPost(UrlString.URL_AVAILABLE_MONEY, params, this, false, callback -> {

            String json = callback.getJson();
            if (mContext != null) {
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    UsableCoinBean usableCoinBean = new Gson().fromJson(json, UsableCoinBean.class);
                    if (1 == usableCoinBean.getStatus()) {
                        double usableCoinBeanData = usableCoinBean.getData();
                        tvUsableCoin.setText(FormatUtil.format2Decimal(usableCoinBeanData));
                    } else {
                        tvUsableCoin.setText("0.00");
                    }
                }
            }
        });
    }

    private void initView() {
        //交易记录
        topBar.setOnRightClickListener(v -> startActivity(BusinessRecordActivity.class));
        mAdapter = new ShouzhiAdapter(this);
        listView.setAdapter(mAdapter);
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mAdapter.getCount() == 0) {
                    stateLayout.showNoDataView("暂无记录");
                } else {
                    stateLayout.showContenView();
                }
            }
        });
        smartPullLayout.setOnPullListener(new SmartPullableLayout.OnPullListener() {
            @Override
            public void onPullDown() {
                getShouzhiRecord();
            }

            @Override
            public void onPullUp() {

            }
        });
    }

    @OnClick({R.id.tv_get_cash, R.id.tv_charge, R.id.tv_payment_manage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_cash:
                startActivity(IWantCashActivity.class);
                break;
            case R.id.tv_charge:
//                startActivity(IWantChargeActivity.class);
                Intent intent=new Intent(MineWalletActivity.this,IWantCZActivity.class);
                intent.putExtra("userIdCard",userIdCard);
                startActivity(intent);
                break;
            case R.id.tv_payment_manage:
                startActivity(PaymentManageActivity.class);
                break;
        }
    }

    @Override
    public void change() {
        initData();
    }

    @Override
    protected void onDestroy() {
        SuperObservableManager
                .getInstance()
                .getObservable(OnUserInfoChange.class)
                .unregisterObserver(this);
        super.onDestroy();

    }
}
