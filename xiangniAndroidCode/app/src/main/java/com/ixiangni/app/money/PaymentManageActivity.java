package com.ixiangni.app.money;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.setting.HelpCenterActivity;
import com.ixiangni.bean.PaywayListBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.interfaces.OndefaultbankChange;
import com.ixiangni.presenters.GetTextPresenter;
import com.ixiangni.presenters.contract.MyPresenter;
import com.ixiangni.ui.TopBar;
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
 * 支付管理
 *
 * @ClassName:PaymentManageActivity
 * @PackageName:com.ixiangni.app.money
 * @Create On 2017/6/22 0022   15:41
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/22 0022 handongkeji All rights reserved.
 */
public class PaymentManageActivity extends BaseActivity implements AddBankCardActivity.OnBankCardChange {

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.btn_add_bank_card)
    Button btnAddBankCard;
    @Bind(R.id.top_bar)
    TopBar topBar;
    private PaymentAdapter paymentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_manage);
        ButterKnife.bind(this);
        topBar.setOnClickListener(v -> {
            Intent intent = new Intent(this, HelpCenterActivity.class);
            intent.putExtra("title", "支付说明");
            intent.putExtra(XNConstants.textflag, GetTextPresenter.TEXT_PAYMENT_INTRODUCE);
            startActivity(intent);
        });

        SuperObservableManager.getInstance()
                .getObservable(AddBankCardActivity.OnBankCardChange.class)
                .registerObserver(this);

        stateLayout.setNodataMessage("暂无银行卡...");
        paymentAdapter = new PaymentAdapter(this);
        listView.setAdapter(paymentAdapter);

        paymentAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (paymentAdapter.getCount() == 0) {
                    stateLayout.showNoDataView();
                } else {
                    stateLayout.showContenView();
                }
            }
        });

        getPayList();
    }

    private void getPayList() {

        stateLayout.showLoadView();
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        RemoteDataHandler.asyncPost(UrlString.URL_PAY_WAY_LIST, params, this, true, response -> {
            if (stateLayout != null) {
                String json = response.getJson();
                if (TextUtils.isEmpty(json)) {
                    stateLayout.showNoDataView();
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    PaywayListBean paywayListBean = new Gson().fromJson(json, PaywayListBean.class);
                    if (1 == paywayListBean.getStatus()) {
                        stateLayout.showContenView();
                        paymentAdapter.replaceAll(paywayListBean.getData());

                    } else {
                        toast(paywayListBean.getMessage());
                        stateLayout.showNoDataView();
                    }
                }
            }

        });
    }

    @Override
    public void onChange() {
        getPayList();
    }

    private class PaymentAdapter extends QuickAdapter<PaywayListBean.DataBean> {

        public PaymentAdapter(Context context) {
            super(context, R.layout.item_manage_bank_card);
        }

        @Override
        protected void convert(BaseAdapterHelper baseAdapterHelper, PaywayListBean.DataBean dataBean) {
            View ivSelect = baseAdapterHelper.getView(R.id.iv_select);
            ivSelect.setSelected(dataBean.getIsdefault() == 1);
            baseAdapterHelper.setVisible(R.id.tv_default, dataBean.getIsdefault() == 1);
            String format = "%s(%s)";
            String name = String.format(Locale.CHINA, format, dataBean.getSpare1(), dataBean.getAccountcode());
            baseAdapterHelper.setText(R.id.tv_bank_card, name);

            //设置默认支付方式
            if (dataBean.getIsdefault() == 0) {//不是默认

                baseAdapterHelper.setOnClickListener(R.id.iv_select,v -> {
                    setDefaultPayment(dataBean.getPaymentid());

                });
            }


            //删除支付方式
            baseAdapterHelper.setOnClickListener(R.id.iv_delete, v -> {
                MyAlertdialog alertdialog = new MyAlertdialog(context);
                alertdialog.setMessage("您确定要删除该银行卡？")
                        .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                            @Override
                            public void onLeftClick(View view) {
                            }

                            //确定
                            @Override
                            public void onRightClick(View view) {
                                deletePayment(baseAdapterHelper.getPosition(), dataBean.getPaymentid());

                            }
                        });
                alertdialog.show();

            });

        }
    }

    /**
     * 设置默认支付方式
     *
     * @param paymentid
     */
    private void setDefaultPayment(int paymentid) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("paymentId", "" + paymentid);

        showProgressDialog("设置中...", false);
        MyPresenter.request(this, UrlString.URL_SET_DEFAULT_PAYMENT, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {

                dismissProgressDialog();
                SuperObservableManager
                        .getInstance()
                        .getObservable(OndefaultbankChange.class)
                        .notifyMethod(OndefaultbankChange::onbankChange);
                toast("设置成功！");
                getPayList();

            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();
                toast(errorMsg);

            }
        });

    }

    /**
     * 删除支付方式
     *
     * @param position
     * @param paymentid
     */
    private void deletePayment(final int position, int paymentid) {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("paymentId", "" + paymentid);

        showProgressDialog("删除中...", false);

        MyPresenter.request(this, UrlString.URL_DELETE_PAYMENT, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                dismissProgressDialog();
                toast("删除成功！");
                getPayList();
            }

            @Override
            public void onFailed(String errorMsg) {

                dismissProgressDialog();
                toast(errorMsg);
            }
        });

    }


    @OnClick(R.id.btn_add_bank_card)
    public void onViewClicked() {
        startActivity(AddBankCardActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SuperObservableManager
                .getInstance()
                .getObservable(AddBankCardActivity.OnBankCardChange.class)
                .unregisterObserver(this);
    }
}
