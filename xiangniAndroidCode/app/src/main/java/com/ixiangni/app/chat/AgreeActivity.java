package com.ixiangni.app.chat;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.util.ViewHelper;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.db.InviteMessgeDao;
import com.hyphenate.exceptions.HyphenateException;
import com.ixiangni.adapter.MultiItemAdapter;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.user.SearchUserActivity;
import com.ixiangni.bean.AddRecordBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.ui.TopBar;
import com.ixiangni.util.GlideUtil;
import com.ixiangni.util.HuanXinHelper;
import com.ixiangni.util.NotifyHelper;
import com.ixiangni.util.SmartPullableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建群，好友申请
 *
 * @ClassName:AgreeActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/7/3 0003   09:31
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/3 0003 handongkeji All rights reserved.
 */
public class AgreeActivity extends BaseActivity {

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.rl_search_user)
    RelativeLayout rlSearchUser;
    private AddRecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);
        ButterKnife.bind(this);

        initView();

        InviteMessgeDao messgeDao = new InviteMessgeDao(this);
        messgeDao.saveUnreadMessageCount(0);

    }

    private void initView() {
        mAdapter = new AddRecordAdapter(this);
        listView.setAdapter(mAdapter);
        smartPullLayout.setOnPullListener(new SmartPullableLayout.OnPullListener() {
            @Override
            public void onPullDown() {
                getAddRecord();
            }

            @Override
            public void onPullUp() {

            }
        });

        topBar.setOnRightClickListener(v -> {
            startActivity(CreatGroupActivity.class);
        });
        getAddRecord();

        rlSearchUser.setOnClickListener(v -> {
            startActivity(SearchUserActivity.class);
        });
    }

    private void getAddRecord() {
              showProgressDialog("加载中...", true);
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
//        RequestBody requestBody = new FormBody.Builder()
//                .add("token", MyApp.getInstance().getUserTicket())
//                .build();

//        HttpUtil.sendOkHttpPostRequest(UrlString.URL_GET_ADD_RECORD, requestBody, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                dismissProgressDialog();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                            if (smartPullLayout != null) {
//                                dismissProgressDialog();
//                                smartPullLayout.stopPullBehavior();
//                                String json = response.body().string();
////                                Log.w("zcq", "okhttp添加记录: "+json);
//                                AddRecordBean addRecordBean = new Gson().fromJson(json, AddRecordBean.class);
//                                if (1 == addRecordBean.getStatus()) {
//                                    List<AddRecordBean.DataBean> data = addRecordBean.getData();
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mAdapter.replaceAll(data);
//
//                                        }
//                                    });
//                                }
//
//                            }
//
//
//            }
//        });

        RemoteDataHandler.asyncPost(UrlString.URL_GET_ADD_RECORD, params, this, true, (ResponseData responseData) -> {
            if (smartPullLayout != null) {
                dismissProgressDialog();
                smartPullLayout.stopPullBehavior();
                String json = responseData.getJson();
                if (!TextUtils.isEmpty(json)) {
                    log("添加记录：" + json);
                    Log.w("zcq", "asy添加记录: "+json);

                    AddRecordBean addRecordBean = new Gson().fromJson(json, AddRecordBean.class);
                    if (1 == addRecordBean.getStatus()) {
                        List<AddRecordBean.DataBean> data = addRecordBean.getData();
                        mAdapter.replaceAll(data);
                    }
                }
            }
        });

    }

    private class AddRecordAdapter extends MultiItemAdapter<AddRecordBean.DataBean> {

        public AddRecordAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public int getDataType(AddRecordBean.DataBean dataBean) {
            //是否是邀请人：0不是，1:是
            int isinviter = dataBean.getIsinviter();
            //好友状态: 0 申请中，1 好友 （已通过）
            int friendshipstatus = dataBean.getFriendshipstatus();
            //别人加我
            if (isinviter == 0) {
                if (friendshipstatus == 0) {//别人加我我还未同意，显示同意按钮
                    return 0;
                } else if (friendshipstatus == 1) {//别人加我，已同意
                    return 1;
                }
            } else {
                if (friendshipstatus == 0) {
                    return 2;

                } else if (friendshipstatus == 1) {
                    return 3;
                }
            }

            return 0;
        }

        @Override
        protected void bindView(int position, AddRecordBean.DataBean dataBean, ViewHelper helper, ViewGroup parent) {

            //用户头像
            String userpic = dataBean.getUserpic();
            ImageView ivUserIcon = helper.getView(R.id.iv_user_icon);
            GlideUtil.loadRoundImage(parent.getContext(), userpic, ivUserIcon, R.mipmap.touxiangmoren);


            //用户昵称
            String usernick = dataBean.getUsernick();


            int itemViewType = getItemViewType(position);
            if (itemViewType == 0) {//待同意
                helper.setOnClickListener(R.id.btn_agree, v -> {
                    agreeAddFrient(dataBean.getUserid(), dataBean.getShowtype());
                });

                String addmsg = dataBean.getAddmsg();
                if (CommonUtils.isStringNull(addmsg)) {
                    helper.setText(R.id.tv_user_nick, usernick);
                } else {
                    helper.setText(R.id.tv_user_nick, usernick + "("+dataBean.getAddmsg()+")");
                }

            } else {
                helper.setText(R.id.tv_user_nick, usernick);
            }


        }

        @Override
        protected void initItemLayout() {

            addTypeLayoutRes(0, R.layout.item_add_agreeing);
            addTypeLayoutRes(1, R.layout.item_add_agreed);
            addTypeLayoutRes(2, R.layout.item_add_wait);
            addTypeLayoutRes(3, R.layout.item_add_agreed);
        }
    }

    /**
     * 同意对方申请我为好友
     *
     * @param friendid
     * @param showtype
     */
    private void agreeAddFrient(int friendid, int showtype) {

        //同意加为好友
        try {
            EMClient.getInstance().contactManager().acceptInvitation(HuanXinHelper.getHuanXinidbyUseid(friendid + ""));
            showProgressDialog("同意中...", true);
            HashMap<String, String> params = new HashMap<>();
            params.put("token", MyApp.getInstance().getUserTicket());
            params.put("friendid", friendid + "");
            params.put("showType", "0");
            RemoteDataHandler.asyncPost(UrlString.URL_AGREE_FRIENDSHIP, params, this, false, callback -> {

                if (smartPullLayout != null) {
                    dismissProgressDialog();
                    String json = callback.getJson();
                    log("同意结果" + json);
                    if (!TextUtils.isEmpty(json)) {
                        try {
                            JSONObject object = new JSONObject(json);
                            int anInt = object.getInt(Constants.status);
                            if (1 == anInt) {
                                toast("添加成功！");
                                NotifyHelper.notifyContactList();
                                getAddRecord();
                            } else {
                                toast(object.getString(Constants.message));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (HyphenateException e) {
            e.printStackTrace();
        }

    }


}
