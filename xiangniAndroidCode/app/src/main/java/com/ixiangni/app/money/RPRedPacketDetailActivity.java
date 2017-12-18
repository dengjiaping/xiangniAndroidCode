package com.ixiangni.app.money;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.widget.AutoListView;
import com.handongkeji.widget.RoundImageView;
import com.hyphenate.chat.EMClient;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.bean.BaseBean;
import com.ixiangni.bean.RedPacketDetialBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.util.GsonUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RPRedPacketDetailActivity extends BaseActivity {
    @Bind(R.id.sender_pic)
    RoundImageView senderPic;
    @Bind(R.id.sender_name)
    TextView senderName;
    @Bind(R.id.tv_money_greeting)
    TextView tvMoneyGreeting;
    @Bind(R.id.luckSum)
    TextView luckSum;
    @Bind(R.id.tv_surplus)
    TextView tvSurplus;
    @Bind(R.id.listview)
    AutoListView listview;
    @Bind(R.id.activity_rpred_packet_detail)
    LinearLayout activityRpredPacketDetail;


//    @Bind(R.id.ll_back)
//    TextView back8;
//    @Bind(R.id.sender_pic)
//    RoundImageView senderPic;
//    @Bind(R.id.sender_name)
//    TextView senderName;
//    @Bind(R.id.tv_money_greeting)
//    TextView tvMoneyGreeting;
//    @Bind(R.id.luckSum)
//    TextView luckSum;
//    @Bind(R.id.tv_surplus)
//    TextView tvSurplus;
//    @Bind(R.id.listview)
//    AutoListView listview;
//    @Bind(R.id.activity_rpred_packet_detail)
//    LinearLayout activityRpredPacketDetail;

    private int pageSize = 500;
    private String luckmoneyid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpred_packet_detail);
        ButterKnife.bind(this);
        init();
        initView();
        getLuckMoneyList();
    }

    private void initView() {
    }

    private void init() {
        luckmoneyid = getIntent().getStringExtra("luckmoneyid");
    }

    private void getLuckMoneyList() {
        String url = UrlString.URL_LUCKMONEY_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("luckmoneyid", luckmoneyid);
        params.put("pageSize", pageSize + "");
        params.put("currentPage", "1");
        RemoteDataHandler.asyncTokenPost(url, this, false, params, data -> {
            String json = data.getJson();
            if (!CommonUtils.isStringNull(json)) {
                Type type = new TypeToken<BaseBean<RedPacketDetialBean>>() {
                }.getType();
                BaseBean<RedPacketDetialBean> baseBean = (BaseBean<RedPacketDetialBean>) GsonUtils.fromJson(json, type);
                if (baseBean.getStatus() == 1) {
                    RedPacketDetialBean baseBeanData = baseBean.getData();
                    double luckymoney = baseBeanData.getLuckymoney();  //  红包总金额
                    String message = baseBeanData.getMessage();
                    int luckmoneynum = baseBeanData.getLuckmoneynum();  //  红包总个数
                    int yilingnum = baseBeanData.getYilingnum();     //  已领个数
                    double yilingmoney = baseBeanData.getYilingmoney();   //  已领金额
                    double weilingmoney = baseBeanData.getWeilingmoney();

                    tvMoneyGreeting.setText(CommonUtils.isStringNull(message)?"":message);

                    int userid = baseBeanData.getUserid();
                    getUserInfoById(userid + "");

                    int luckymoneytype = baseBeanData.getAllocationtype();
                    if (luckymoneytype == 0) {
                        if (yilingmoney == 0) {
                            if (EMClient.getInstance().getCurrentUser().equals("ixn" + userid)) {
                                tvSurplus.setText("红包金额" + luckymoney + "元，等待对方领取");
                            }
                        } else {
                            tvSurplus.setText("共一个红包都被抢光了");
                        }
                    } else {
                        String format = String.format("%2.2f", weilingmoney);

                        tvSurplus.setText("已领取" + yilingnum + "/" + luckmoneynum + "个" + "，剩余" + format + "/" + String.format("%2.2f", luckymoney) + "元");


                        tvMoneyGreeting.setText(message);
                    }


                    List<RedPacketDetialBean.LuckMoneyAllocationVoListBean> list = baseBeanData.getLuckMoneyAllocationVoList();

                    if (list != null) {
                        for (RedPacketDetialBean.LuckMoneyAllocationVoListBean bean : list) {
                            int userid1 = bean.getUserid();
                            if (LoginHelper.getInstance().getUserid(RPRedPacketDetailActivity.this).equals(userid1 + "")) {
                                double moneynum = bean.getMoneynum();
                                luckSum.setText(getString(R.string.red_packet_sum, moneynum));
                                break;
                            }
                        }
                    }

                    listview.setAdapter(new MyAdapter(RPRedPacketDetailActivity.this, list));

                }
            }
        });
    }

    private void getUserInfoById(String userId) {
        String url = UrlString.URL_GET_USERINFOBYID;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("goalId", userId);
        RemoteDataHandler.asyncPost(url, params, this, true, new RemoteDataHandler.Callback() {

            @Override
            public void dataLoaded(ResponseData data) {
                String json = data.getJson();
                if (!CommonUtils.isStringNull(json)) {
                    try {
                        JSONObject obj = new JSONObject(json);
                        String status = obj.getString("status");
                        if (status.equals("1")) {
                            JSONObject userData = obj.getJSONObject("data");
                            String userpic = userData.getString("userpic");
                            Glide.with(RPRedPacketDetailActivity.this).load(userpic).error(R.mipmap.touxiangmoren).into(senderPic);
                            String usernick = userData.getString("usernick");
                            senderName.setText(usernick);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    toast(Constants.CONNECT_SERVER_FAILED);
                }
            }
        });
    }

    static class MyAdapter extends BaseAdapter {
        List<RedPacketDetialBean.LuckMoneyAllocationVoListBean> list;
        Context mContext;
        SimpleDateFormat sdf;

        public MyAdapter(Context context, List<RedPacketDetialBean.LuckMoneyAllocationVoListBean> list) {
            this.list = list;
            mContext = context;
            sdf = new SimpleDateFormat("HH:mm");
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_rp_detial_layout, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            RedPacketDetialBean.LuckMoneyAllocationVoListBean bean = list.get(position);
            String usernick = bean.getUsernick();
            holder.name.setText(usernick);

            long occupancytime = bean.getOccupancytime();
            String format = sdf.format(new Date(occupancytime));
            holder.time.setText(format);

            String userpic = bean.getUserpic();
            Glide.with(mContext).load(userpic).error(R.mipmap.touxiangmoren).into(holder.headImage);

            double moneynum = bean.getMoneynum();
            holder.money.setText(mContext.getString(R.string.red_packet_sum, moneynum));
            return convertView;
        }
    }

    static class ViewHolder {
        @Bind(R.id.head_img)
        RoundImageView headImage;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.money)
        TextView money;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
