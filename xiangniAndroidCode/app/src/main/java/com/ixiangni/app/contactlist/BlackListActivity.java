package com.ixiangni.app.contactlist;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.BlackListBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.BlackListPresenter;
import com.ixiangni.util.GlideUtil;
import com.ixiangni.util.HuanXinHelper;
import com.ixiangni.util.NotifyHelper;
import com.ixiangni.util.SmartPullableLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 黑名单
 *
 * @ClassName:BlackListActivity
 * @PackageName:com.ixiangni.app.contactlist
 * @Create On 2017/6/22 0022   16:37
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/22 0022 handongkeji All rights reserved.
 */
public class BlackListActivity extends BaseActivity implements SmartPullableLayout.OnPullListener {


    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    @Bind(R.id.tv_no_data_message)
    TextView tvNoDataMessage;
    @Bind(R.id.rl_no_data)
    RelativeLayout rlNoData;
    private int currentPage = 1;
    private int pageSize = 20;
    private BlackListPresenter mPresenter;
    private BlackListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        ButterKnife.bind(this);


        mAdapter = new BlackListAdapter(this);
        listView.setAdapter(mAdapter);
        getBlackList();

        tvNoDataMessage.setText("暂无黑名单");
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(mAdapter.getCount()==0){
                    rlNoData.setVisibility(View.VISIBLE);
                }else {
                    rlNoData.setVisibility(View.GONE);
                }
            }
        });

        mPresenter = new BlackListPresenter();
    }

    private boolean isFirst = true;

    private void getBlackList() {

        if (isFirst) {
            showProgressDialog("加载中...", true);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("currentPage", currentPage + "");
        params.put(XNConstants.pageSize, pageSize + "");
        RemoteDataHandler.asyncPost(UrlString.URL_BLACK_LIST, params, this, true, response -> {
            String json = response.getJson();
            log(json);
            if (listView != null) {
                if(CommonUtils.isStringNull(json)){
                    toast(Constants.CONNECT_SERVER_FAILED);
                }else {

                smartPullLayout.stopPullBehavior();

                if (isFirst) {
                    dismissProgressDialog();
                    isFirst = false;
                }
                BlackListBean blackListBean = new Gson().fromJson(json, BlackListBean.class);
                if (1 == blackListBean.getStatus()) {
                    List<BlackListBean.DataBean> data = blackListBean.getData();
                    if (currentPage == 1) {
                        if(data!=null){
                        mAdapter.replaceAll(data);

                        }
                    } else {
                        mAdapter.addAll(data);
                    }
                    if (data != null && data.size() < pageSize) {
                        smartPullLayout.setPullUpEnabled(false);
                    }
                }
                }

            }


        });
    }

    private class BlackListAdapter extends QuickAdapter<BlackListBean.DataBean> {

        private final Comparator<BlackListBean.DataBean> comparator;

        @Override
        public void replaceAll(List<BlackListBean.DataBean> elem) {

            Collections.sort(elem, comparator);
            super.replaceAll(elem);

        }

        @Override
        public void addAll(List<BlackListBean.DataBean> elem) {
            this.data.addAll(elem);
            Collections.sort(data,comparator);
            notifyDataSetChanged();
        }

        public BlackListAdapter(Context context) {
            super(context, R.layout.item_black_list_edit);

            comparator = new Comparator<BlackListBean.DataBean>() {
                @Override
                public int compare(BlackListBean.DataBean o1, BlackListBean.DataBean o2) {
                    return o1.getFirstLetter().compareTo(o2.getFirstLetter());
                }
            };
        }

        @Override
        protected void convert(BaseAdapterHelper helper, BlackListBean.DataBean dataBean) {
            String usernick = dataBean.getUsernick();
            String userpic = dataBean.getUserpic();
            TextView tv = helper.getView(R.id.tv_first_letter);
            int position = helper.getPosition();
            String firstLetter = dataBean.getFirstLetter();

            //索引
            if (position - 1 >= 0 && firstLetter.equals(data.get(position - 1).getFirstLetter())) {
                tv.setVisibility(View.GONE);
            } else {
                tv.setVisibility(View.VISIBLE);
                tv.setText(firstLetter);
            }
            //头像
            ImageView iv = helper.getView(R.id.iv_user_icon);
            GlideUtil.loadRoundImage(context, userpic, iv);
            //昵称
            helper.setText(R.id.tv_user_nick, usernick);
            //删除
            helper.setOnClickListener(R.id.iv_remove, v -> {
                removeFromBlackList(dataBean.getBlacklistid(), position, dataBean.getFriendid());
            });

        }
    }

    private void removeFromBlackList(int blackListid, int position, int friendid) {


        showProgressDialog("移除中...", false);
        mPresenter.deleteFromBlackList(this, blackListid, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {

                dismissProgressDialog();
                toast("移除成功!");
                mAdapter.remove(position);
                try {
                    EMClient.getInstance().contactManager().removeUserFromBlackList(HuanXinHelper.getHuanXinidbyUseid(friendid + ""));
                    log("一处成功");
                    String stringExtra = getIntent().getStringExtra(XNConstants.USERID);

                    String userid = friendid+"";
                    if(userid.equals(stringExtra)){
                        setResult(RESULT_OK);
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    log("一处shibai");

                }
                NotifyHelper.notifyContactList();
            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();
                toast(errorMsg);

            }
        });
    }

    @Override
    public void onPullDown() {
        smartPullLayout.setPullUpEnabled(true);
        currentPage = 1;
        getBlackList();
    }

    @Override
    public void onPullUp() {
        currentPage++;
        getBlackList();

    }
}
