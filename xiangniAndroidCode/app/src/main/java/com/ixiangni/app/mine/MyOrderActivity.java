package com.ixiangni.app.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handongkeji.common.QFragmentPagerAdapter;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.DisplayUtil;
import com.ixiangni.adapter.MyRvAdapter;
import com.ixiangni.adapter.MyRvViewHolder;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.missyouservice.FillHotelOrdActivity;
import com.ixiangni.app.orders.OrdHotelDetailActivity;
import com.ixiangni.app.orders.OrdOtherFragment;
import com.ixiangni.app.orders.OrdPlaneFragment;
import com.ixiangni.app.orders.OrdTrainFragment;
import com.ixiangni.bean.OrderBean;
import com.ixiangni.bean.OrderInfoBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.interfaces.IOrdRefresh;
import com.ixiangni.presenters.OrderPresenter;
import com.ixiangni.util.LinearDecoration;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;
import com.mydemo.yuanxin.util.HttpUtil;
import com.nevermore.oceans.ob.SuperObservableManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 我的订单
 *
 * @ClassName:MyOrderActivity
 * @PackageName:com.ixiangni.app.mine
 * @Create On 2017/7/21 0021   18:36
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/21 0021 handongkeji All rights reserved.
 */
public class MyOrderActivity extends BaseActivity implements OnResult<List<OrderBean>> {

    @Bind(R.id.rb_hotel)
    RadioButton rbHotel;
    @Bind(R.id.rb_plain)
    RadioButton rbPlain;
    @Bind(R.id.rb_train)
    RadioButton rbTrain;
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.view_pager_plain)
    ViewPager viewPagerPlain;
    @Bind(R.id.view_pager_train)
    ViewPager viewPagerTrain;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.tab_layout_plane)
    TabLayout tabLayoutPlane;
    @Bind(R.id.ll_plane)
    LinearLayout llPlane;
    @Bind(R.id.tab_layout_train)
    TabLayout tabLayoutTrain;
    @Bind(R.id.ll_train)
    LinearLayout llTrain;

    @Bind(R.id.tab_layout_other)
    TabLayout tabLayoutOther;
    @Bind(R.id.ll_other)
    LinearLayout llOther;
    @Bind(R.id.view_pager_other)
    ViewPager viewPagerOther;


    private int currentPage = 1;
    private int pageSize = 20;
    private OrderPresenter presenter;
    private HotelOrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        initViewPagers();

        ivBack.setOnClickListener(v -> onBackPressed());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_hotel:
                        currentPage = 1;
                        getHotelOrderList();
                        showOrder(stateLayout);
                        break;
                    case R.id.rb_plain:
                        viewPagerPlain.setCurrentItem(0, false);
                        SuperObservableManager.notify(IOrdRefresh.IRefreshPlane.class, IOrdRefresh.IRefreshPlane::onRefresh);
                        showOrder(llPlane);
//                        currentPage = 1;
//                        getPlaneOrderList();
//                        showOrder(stateLayout);
                        break;
                    case R.id.rb_train:
                        viewPagerTrain.setCurrentItem(0, false);
                        SuperObservableManager.notify(IOrdRefresh.IRefreshTrain.class, IOrdRefresh.IRefreshTrain::onRefresh);
                        showOrder(llTrain);
//                        currentPage = 1;
//                        getTrainOrderList();
//                        showOrder(stateLayout);
                        break;
                    case R.id.rb_other:
                        viewPagerOther.setCurrentItem(0, false);
                        SuperObservableManager.notify(IOrdRefresh.IRefreshOther.class, IOrdRefresh.IRefreshOther::onRefresh);
                        showOrder(llOther);
                        break;
                }
            }
        });

        presenter = new OrderPresenter();
        mAdapter = new HotelOrderAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        //分割线
        recyclerView.addItemDecoration(new LinearDecoration(DisplayUtil.dip2px(this, 5),
                ContextCompat.getColor(this, R.color.divider), LinearDecoration.VERTICAL));
        stateLayout.setUpwihtRecyclerAdapter(mAdapter, "暂无订单");
        smartPullLayout.setOnPullListener(new SmartPullableLayout.OnPullListener() {
            @Override
            public void onPullDown() {
                currentPage = 1;
                getHotelOrderList();
            }

            @Override
            public void onPullUp() {
                currentPage++;
                getHotelOrderList();
            }
        });

        getHotelOrderList();
    }

    private void initViewPagers() {
        String[] stringArray = getResources().getStringArray(R.array.orderType);
        List<String> titles = Arrays.asList(stringArray);

        List<Fragment> planeFragmentList = new ArrayList<>();
        planeFragmentList.add(OrdPlaneFragment.createInstance(null));
        planeFragmentList.add(OrdPlaneFragment.createInstance(OrderPresenter.PLAINE_ORDER_STATUS_NO));
        planeFragmentList.add(OrdPlaneFragment.createInstance(OrderPresenter.PLAINE_ORDER_STATUS_YSE));

        planeFragmentList.add(OrdPlaneFragment.createInstance(OrderPresenter.PLAINE_ORDER_STATUS_ING));

        planeFragmentList.add(OrdPlaneFragment.createInstance(OrderPresenter.TUIPIAO_ORDER_STATUS_SUCCESS));

        List<Fragment> trainFragmentList = new ArrayList<>();
        trainFragmentList.add(OrdTrainFragment.createInstance(null));
        trainFragmentList.add(OrdTrainFragment.createInstance(OrderPresenter.TRAIN_ORDER_STATUS_NO));
        trainFragmentList.add(OrdTrainFragment.createInstance(OrderPresenter.TRAIN_ORDER_STATUS_YES));


        trainFragmentList.add(OrdTrainFragment.createInstance(OrderPresenter.TRAIN_ORDER_STATUS_ING));
        trainFragmentList.add(OrdTrainFragment.createInstance(OrderPresenter.TUIPIAO_ORDER_STATUS_SUCCESS));


        List<Fragment> otherFragmentList = new ArrayList<>();
        otherFragmentList.add(OrdOtherFragment.createInstance(null));
        otherFragmentList.add(OrdOtherFragment.createInstance(OrderPresenter.ORDER_STATUS_NO));
        otherFragmentList.add(OrdOtherFragment.createInstance(OrderPresenter.ORDER_STATUS_YES));

        QFragmentPagerAdapter planeAdapter = new QFragmentPagerAdapter(getSupportFragmentManager(), planeFragmentList);
        planeAdapter.setTitles(titles);
        viewPagerPlain.setOffscreenPageLimit(planeFragmentList.size());
        viewPagerPlain.setAdapter(planeAdapter);
        tabLayoutPlane.setupWithViewPager(viewPagerPlain);


        QFragmentPagerAdapter trainAdapter = new QFragmentPagerAdapter(getSupportFragmentManager(), trainFragmentList);
        trainAdapter.setTitles(titles);
        viewPagerTrain.setOffscreenPageLimit(trainFragmentList.size());
        viewPagerTrain.setAdapter(trainAdapter);
        tabLayoutTrain.setupWithViewPager(viewPagerTrain);

        QFragmentPagerAdapter otherAdapter = new QFragmentPagerAdapter(getSupportFragmentManager(), otherFragmentList);
        otherAdapter.setTitles(titles);
        viewPagerOther.setOffscreenPageLimit(otherFragmentList.size());
        viewPagerOther.setAdapter(otherAdapter);
        tabLayoutOther.setupWithViewPager(viewPagerOther);
    }

    private boolean isAuto = true;

    private void getHotelOrderList() {
        if (isAuto) {
            stateLayout.showLoadView();
        }
        presenter.getOrderList(this, OrderPresenter.ORDER_TYPE_HOTEL, null, "" + currentPage, "" + pageSize, this);
    }

    //获取火车所有订单
    private void getTrainOrderList() {
        if (isAuto) {
            stateLayout.showLoadView();
        }
        presenter.getOrderList(this, OrderPresenter.ORDER_TYPE_TRAIN, null, "" + currentPage, "" + pageSize, this);
    }
    private void getPlaneOrderList() {
        if (isAuto) {
            stateLayout.showLoadView();
        }
        presenter.getOrderList(this, OrderPresenter.ORDER_TYPE_PLANE, null, "" + currentPage, "" + pageSize, this);
    }


    //切换订单
    private void showOrder(View view) {

        if (stateLayout != view) {
            stateLayout.setVisibility(View.GONE);
        }

        if (llPlane != view) {
            llPlane.setVisibility(View.GONE);
        }

        if (llTrain != view) {
            llTrain.setVisibility(View.GONE);
        }
        if (llOther != view) {
            llOther.setVisibility(View.GONE);
        }

        view.setVisibility(View.VISIBLE);

    }

    @Override
    public void onSuccess(List<OrderBean> orderBeen) {
        if (stateLayout != null) {
            smartPullLayout.stopPullBehavior();
            if (currentPage == 1) {
                mAdapter.replaceAll(orderBeen);
                recyclerView.scrollToPosition(0);
            } else {
                mAdapter.addAll(orderBeen);
            }
            isAuto = false;
        }
    }

    @Override
    public void onFailed(String errorMsg) {

        if (stateLayout != null) {
            smartPullLayout.stopPullBehavior();
            stateLayout.loadingFinish();
            toast(errorMsg);
            isAuto = false;
        }
    }

    class HotelOrderAdapter extends MyRvAdapter<OrderBean> {

        public HotelOrderAdapter(Context context) {
            super(context, R.layout.item_order_hotel);
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, OrderBean data) {
            String orderpic = data.getOrderpic();
            ImageView iv = holder.getView(R.id.iv_hotel_pic);
            TextView yiyuding = holder.getView(R.id.tv_yiyuding);
            TextView tvQuxiao = holder.getView(R.id.tv_quxiao);
            yiyuding.setSelected(true);

            Glide.with(context).load(orderpic).placeholder(R.mipmap.loading_rect).error(R.mipmap.loading_rect)
                    .fallback(R.mipmap.loading_rect).into(iv);
            String ordername = data.getOrdername();
            //酒店名称
            holder.setText(R.id.tv_hotel_name, ordername);

            String describe2 = data.getDescribe2();
            String describe3 = data.getDescribe3();
            holder.setText(R.id.tv_hotel_size, describe2 + " | " + describe3 + "平方米");

            OrderInfoBean shanglvstr = new Gson().fromJson(data.getShanglvstr(), OrderInfoBean.class);
            String tm1 = shanglvstr.getTm1();
            String tm2 = shanglvstr.getTm2();
            String timeInfo = FillHotelOrdActivity.getTimeInfo(tm1, tm2);
            holder.setText(R.id.tv_plan_time, timeInfo);

            //订单状态
            //0 待支付 1 已支付 2 支付失败 3退单成功
            // 4机票出票申请中 5机票出票成功 6机票申请未过 7酒店订单取消申请中
            // 8火车票订单取消申请中 9火车票出票申请中 10 火车票出票成功 11火车票申请未过 100默认无效状态
            int orderstatus = data.getOrderstatus();
            if (orderstatus == 7) {
                yiyuding.setText("取消中");
                tvQuxiao.setText("删除订单");
                tvQuxiao.setSelected(false);//false是黑的，true是彩的


            } else if (orderstatus == 3) {
                yiyuding.setText("已取消");
                tvQuxiao.setText("删除订单");
                tvQuxiao.setSelected(false);//false是黑的，true是彩的

            } else {

                yiyuding.setText("已预定");
                tvQuxiao.setText("取消预定");
                tvQuxiao.setSelected(false);
            }

            holder.getView().setOnClickListener(v -> {
                //进入详情
                String orderid = data.getOrderid() + "";
                Intent intent = new Intent(MyOrderActivity.this, OrdHotelDetailActivity.class);
                intent.putExtra(XNConstants.orderid, orderid);
                startActivity(intent);
            });

            holder.getView(R.id.tv_quxiao).setOnClickListener(v -> {
                //取消订单
                AlertDialog.Builder dialog = new AlertDialog.Builder(MyOrderActivity.this);
                dialog.setTitle("提醒");
                if (orderstatus == 7 || orderstatus == 3) {//7酒店订单取消申请中  取消成功
                    dialog.setMessage("您是否确认删除该订单？");


                } else {//已预定
                    dialog.setMessage("您是否确认取消预定？");
                }


                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String myUrl = null;
                        if (orderstatus == 7 || orderstatus == 3) {//7酒店订单取消申请中
                            myUrl = UrlString.URL_DELETE_ORDER;
                        } else {//已预定
                            myUrl = UrlString.URL_HOTEL_CANCEL;
                        }
                        showProgressDialog("提示", "请求中...", true);
                        String shanglvSysno = data.getShanglvsysno();
                        if (TextUtils.isEmpty(shanglvSysno)) {
                            Toast.makeText(MyOrderActivity.this, "订单号获取失败", Toast.LENGTH_SHORT).show();
                            dismissProgressDialog();
                            return;
                        }
                        final RequestBody requsetBody = new FormBody.Builder()
                                .add("token", MyApp.getInstance().getUserTicket())
                                .add("orderId", data.getOrderid() + "")
//                                .add("shanglvSysno", data.getShanglvsysno())//shanglvSysno订单号
                                .build();
                        HttpUtil.sendOkHttpPostRequest(myUrl, requsetBody, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissProgressDialog();

                                        Toast.makeText(MyOrderActivity.this, "网络开小差了，请稍后重试", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String text = response.body().string();
                                try {
                                    JSONObject jo = new JSONObject(text);
                                    String message = jo.getString("message");
                                    String status = jo.getString("status");
                                    if ("1".equals(status)) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dismissProgressDialog();

//                                                tvQuxiao.setText("申请成功");
                                                Toast.makeText(MyOrderActivity.this, message + ",请下拉刷新更新数据", Toast.LENGTH_SHORT).show();


                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dismissProgressDialog();
                                                Toast.makeText(MyOrderActivity.this, message + ",请下拉刷新更新数据", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                } catch (JSONException e) {

                                }

                            }
                        });
                    }
                });

                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

            });
        }
    }

}
