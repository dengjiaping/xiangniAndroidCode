package com.ixiangni.app.mine;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.emotion.BroughtEmActivity;
import com.ixiangni.bean.EmotionListBean;
import com.ixiangni.common.EmotionManager;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.Paydialog;
import com.ixiangni.interfaces.OnUserInfoChange;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 表情购买
 *
 * @ClassName:EmotionActivity
 * @PackageName:com.ixiangni.app.mine
 * @Create On 2017/6/21 0021   14:20
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class EmotionActivity extends BaseActivity implements SmartPullableLayout.OnPullListener {

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    private EmotionAdapter adapter;
    private Paydialog paydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);
        ButterKnife.bind(this);

        adapter = new EmotionAdapter(this);
        listView.setAdapter(adapter);
        smartPullLayout.setOnPullListener(this);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(adapter.getCount()==0){
                    stateLayout.showNoDataView("暂无表情包");
                }else {
                    stateLayout.showContenView();
                }
            }
        });

        paydialog = new Paydialog(this);
        paydialog.checkSetPayPassword(this,listView);
        getEmotionList();
    }


    /**
     * 获取表情包列表
     * token	是	String	用户token
     * currentPage	否	int	当前页，默认为1
     * pageSize	否	int	每页记录数，默认为10
     */

    private boolean isFrist = true;

    private int currentPage = 1;
    private int pageSize = 20;

    private void getEmotionList() {

        if (isFrist) {
            stateLayout.showLoadView();
        }

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put(XNConstants.currentPage, "" + currentPage);
        params.put(XNConstants.pageSize, "" + pageSize);

        RemoteDataHandler.asyncPost(UrlString.URL_EMOTION_LIST, params, this, true, response -> {

            String json = response.getJson();
            log(json);
            if (listView != null) {

                if (isFrist) {
                    stateLayout.showContenView();
                    isFrist = false;
                }
                smartPullLayout.stopPullBehavior();
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                    return;
                } else {
                    EmotionListBean emotionListBean = new Gson().fromJson(json, EmotionListBean.class);
                    if (1 == emotionListBean.getStatus()) {
                        List<EmotionListBean.DataBean> data = emotionListBean.getData();
                        if (currentPage == 1) {
                            adapter.replaceAll(data);

                        } else {
                            adapter.addAll(data);
                        }
                        if (ListUtil.isEmptyList(data)) {
                            smartPullLayout.setPullUpEnabled(false);
                        }

                    } else {
                        toast(emotionListBean.getMessage());
                    }
                }
            }

        });

    }

    @Override
    public void onPullDown() {
        smartPullLayout.setPullUpEnabled(true);
        currentPage = 1;
        getEmotionList();
    }

    @Override
    public void onPullUp() {
        currentPage++;
        getEmotionList();

    }

    private class EmotionAdapter extends QuickAdapter<EmotionListBean.DataBean> {

        public EmotionAdapter(Context context) {
            super(context, R.layout.item_emotion);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, EmotionListBean.DataBean dataBean) {
            helper.setOnClickListener(R.id.xh_relativelayout, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "都能买啥表情呢", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EmotionActivity.this, BroughtEmActivity.class);
                    /***
                     * 传递加载的表情的具体的信息
                     */
                    startActivity(intent);
                }
            });

            ImageView iv = helper.getView(R.id.iv_emotion_icon);
            Glide.with(context).load(dataBean.getBrowbaginfo()).error(R.mipmap.biaoqing).into(iv);
            helper.setText(R.id.tv_emotion_describe, dataBean.getBrowbagname());

            int position = helper.getPosition();
            int browbagid = dataBean.getBrowbagid();
            int isbuy = dataBean.getIsbuy();


            String money = "银信币:" + dataBean.getBrowbagprice();
            helper.setText(R.id.tv_price, money);
            Button btnBuy = helper.getView(R.id.btn_buy);

            if (isbuy == 1) {
                btnBuy.setText("已购买");
                btnBuy.setEnabled(false);
            } else {
                btnBuy.setText("购买");
                btnBuy.setEnabled(true);

                btnBuy.setOnClickListener(v -> {
                    paydialog.show();
                    paydialog.setmListener(() -> buyEmotion(position, browbagid));
                });
            }


        }
    }

    /**
     * 购买表情包
     *
     * @param position
     * @param browbagid token	是	String	用户token
     *                  browbaguserid	是	Long	用户表情包id
     */
    private void buyEmotion(int position, int browbagid) {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("browbagid", browbagid + "");

        showProgressDialog("购买中", false);
        RemoteDataHandler.asyncPost(UrlString.URL_BUY_EMOTION, params, this, false, response -> {
            if (listView != null) {

                dismissProgressDialog();
                String json = response.getJson();
                if (TextUtils.isEmpty(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                    return;
                }
                try {
                    JSONObject object = new JSONObject(json);
                    int anInt = object.getInt(Constants.status);
                    if (1 == anInt) {
                        toast("购买成功！");
                        //开始下载表情包
                        EmotionManager.getInstance().notifyEmotionChange();
                        SuperObservableManager
                                .getInstance()
                                .getObservable(OnUserInfoChange.class)
                                .notifyMethod(OnUserInfoChange::change);
                        //金币扣除更新我的
                        adapter.getItem(position).setIsbuy(1);
                        adapter.notifyDataSetChanged();
                    } else {
                        toast(object.getString(Constants.message));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

    }

}
