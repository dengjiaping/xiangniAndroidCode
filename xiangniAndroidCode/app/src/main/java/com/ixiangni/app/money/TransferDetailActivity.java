package com.ixiangni.app.money;

import android.os.Bundle;
import android.widget.TextView;

import com.easemob.redpacket.utils.TransferConstance;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.utils.DateUtil;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.TferDetailBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.ui.TopBar;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 转账详情
 *
 * @ClassName:TransferDetailActivity
 * @PackageName:com.ixiangni.app.money
 * @Create On 2017/6/19 0019   14:23
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/19 0019 handongkeji All rights reserved.
 */
public class TransferDetailActivity extends BaseActivity {

    @Bind(R.id.topbar)
    TopBar topbar;
    @Bind(R.id.tv_yishou)
    TextView tvYishou;
    @Bind(R.id.tv_yxb)
    TextView tvYxb;
    @Bind(R.id.tv_look)
    TextView tvLook;
    @Bind(R.id.tv_time_zhuanzhang)
    TextView tvTimeZhuanzhang;
    private String transferid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_detail);
        ButterKnife.bind(this);
        transferid = getIntent().getStringExtra(TransferConstance.TransferId);
        initView();
        getDetail();
    }


    private void initView() {

        tvLook.setOnClickListener(v -> startActivity(MineWalletActivity.class));
    }

    //    token	是	String	用户token
//    orderid	是	Long	订单id
    private void getDetail() {

        showProgressDialog(Constants.MESSAGE_LOADING, true);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("orderid", transferid);
        RemoteDataHandler.asyncPost(UrlString.URL_TRANSFER_DETAIL, params, this, false, response -> {
            if (topbar != null) {
                dismissProgressDialog();
                String json = response.getJson();
                if(CommonUtils.isStringNull(json)){
                    toast(Constants.CONNECT_SERVER_FAILED);
                }else {
                    TferDetailBean tferDetailBean = new Gson().fromJson(json, TferDetailBean.class);
                    if(1==tferDetailBean.getStatus()){
                        TferDetailBean.DataBean data = tferDetailBean.getData();
                        double orderprice = data.getOrderprice();
                        String yxb = String.format(getString(R.string.ixn_yxb), "" + orderprice);
                        tvYxb.setText(yxb);

                        tvTimeZhuanzhang.setText("转账时间:"+ DateUtil.getTimeStr(data.getOrdercreatetime()));

                    }else {
                        toast(tferDetailBean.getMessage());
                    }

                }

            }

        });
    }
}
