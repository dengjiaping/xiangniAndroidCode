package com.ixiangni.app.money;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.utils.DateUtil;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.ChargeBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 交易记录
 *
 * @ClassName:BusinessRecordActivity
 * @PackageName:com.ixiangni.app.money
 * @Create On 2017/6/21 0021   11:55
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class BusinessRecordActivity extends BaseActivity implements SmartPullableLayout.OnPullListener {

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    private ChargeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_record);
        ButterKnife.bind(this);

        mAdapter = new ChargeAdapter(this);
        listView.setAdapter(mAdapter);
        //暂无数据判断
        stateLayout.setUpwithBaseAdapter(mAdapter,"暂无记录");

        smartPullLayout.setOnPullListener(this);
        onPullDown();
    }

    /**
     * 获取交易记录
     */
    private void getRecodr() {
        stateLayout.showLoadView(Constants.MESSAGE_LOADING);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        RemoteDataHandler.asyncPost(UrlString.URL_RECORD_CHARGE, params, this, true, response -> {

            if(stateLayout!=null){
            String json = response.getJson();
            log(json);
                smartPullLayout.stopPullBehavior();
                if(!CommonUtils.isStringNull(json)){
                    ChargeBean chargeBean = new Gson().fromJson(json, ChargeBean.class);
                    if(1==chargeBean.getStatus()){
                        mAdapter.replaceAll(chargeBean.getData());
                    }else {
                        toast(chargeBean.getMessage());
                    }
                }else {
                    toast(Constants.CONNECT_SERVER_FAILED);
                }

            }
        });

    }

    @Override
    public void onPullDown() {
        getRecodr();
    }

    @Override
    public void onPullUp() {

    }

    private class ChargeAdapter extends QuickAdapter<ChargeBean.DataBean>{

        public ChargeAdapter(Context context) {
            super(context, R.layout.item_business_record);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, ChargeBean.DataBean dataBean) {
            long createtime = dataBean.getCreatetime();
            String ymd = DateUtil.getYmd(createtime);
            helper.setText(R.id.tv_time,ymd);

            double refillnum = dataBean.getRefillnum();

            String desc = String.format("%.2f", refillnum);


            helper.setText(R.id.tv_describe,"平台充值"+desc+"个银信币");

            helper.setText(R.id.tv_money_des,"+"+desc);

        }
    }
}
