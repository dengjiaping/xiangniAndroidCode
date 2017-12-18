package com.ixiangni.app.user;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.SearchedUserBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.ui.SearchView;
import com.ixiangni.ui.TopBar;
import com.ixiangni.util.GlideUtil;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nevermore.oceans.utils.ListUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索用户
 *
 * @ClassName:SearchUserActivity
 * @PackageName:com.ixiangni.app.user
 * @Create On 2017/6/22 0022   17:16
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/22 0022 handongkeji All rights reserved.
 */
public class SearchUserActivity extends BaseActivity implements SmartPullableLayout.OnPullListener {


    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;

    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.search_View)
    SearchView searchView;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;

    private int currentPage = 1;
    private int pageSize = 20;
    private SearchedUserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        smartPullLayout.setOnPullListener(this);
        mAdapter = new SearchedUserAdapter(this);
        listView.setAdapter(mAdapter);
        stateLayout.setUpwithBaseAdapter(mAdapter,"未搜索到该用户");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchedUserBean.DataBean item = mAdapter.getItem(position);
                SearchedUserBean.DataBean.UserBean user = item.getUser();
                String userid = user.getUserid() + "";
                PersonPageActivity.start(SearchUserActivity.this, userid);
//                int flag = item.getFlag();
////                0陌生人，1申请中，2朋友
//                if (flag == 0) {
//                    PersonPageActivity.start(SearchUserActivity.this, userid,user.getUsernick(),user.getUserpic() );
//                }else if(flag==2){
//                    ChatActivity.startChat(SearchUserActivity.this, HuanXinHelper.getHuanXinidbyUseid(userid),1);
//                }else {
//                    toast("好友申请中...");
//                }
            }
        });

        searchView.edtSearch.setHint("搜索用户...");

        searchView.setListener(v -> {
            String content = searchView.getEdtiText().getText().toString().trim();
            if (TextUtils.isEmpty(content)) {

                toast("请输出您要搜索的用户名称！");
            } else {
                searchText = content;
                getSearchUser();
            }
        });

    }


    @OnClick(R.id.iv_search)
    public void onViewClicked() {

    }

    private String searchText = "";

    @Override
    public void onPullDown() {
//        currentPage = 1;
//        getSearchUser();
        currentPage++;
        getSearchUser();
    }

    @Override
    public void onPullUp() {
        currentPage++;
        getSearchUser();
    }

    //    flag
// int	0陌生人，
// 1申请中，
// 2朋友
    private class SearchedUserAdapter extends QuickAdapter<SearchedUserBean.DataBean> {

        public SearchedUserAdapter(Context context) {
            super(context, R.layout.item_search);
        }


        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        protected void convert(BaseAdapterHelper helper, SearchedUserBean.DataBean data) {

            SearchedUserBean.DataBean.UserBean userBean = data.getUser();
            String usernick = userBean.getUsernick();
            helper.setText(R.id.tv_user_nick, usernick);
            String userpic = userBean.getUserpic();
            ImageView iv = helper.getView(R.id.iv_user_icon);
            GlideUtil.loadRoundImage(context, userpic, iv, R.mipmap.touxiangmoren);
        }
    }

    private void getSearchUser() {
        stateLayout.showLoadView("搜索中...");
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("searchText", searchText);
        params.put("currentPage", currentPage + "");
        params.put("pageSize", pageSize + "");
        RemoteDataHandler.asyncPost(UrlString.URL_SEARCH_USER, params, this, false, callback -> {
            if (smartPullLayout != null) {
                smartPullLayout.stopPullBehavior();
                String json = callback.getJson();
                log(json);
//                Log.w("zcq", "sousuo好友liebiao: "+json );
                if (!TextUtils.isEmpty(json)) {
                    SearchedUserBean searchedUserBean = new Gson().fromJson(json, SearchedUserBean.class);
                    if (1 == searchedUserBean.getStatus()) {
                        List<SearchedUserBean.DataBean> data = searchedUserBean.getData();
                        if (1 == currentPage) {
                            mAdapter.replaceAll(data);
                            smartPullLayout.setPullUpEnabled(true);
                            //暂时改为无法上啦刷新，回头删除掉
                            smartPullLayout.setPullUpEnabled(false);
                        } else {
                            mAdapter.addAll(data);
                        }
                        if (ListUtil.isEmptyList(data) || data.size() < pageSize) {
                            smartPullLayout.setPullUpEnabled(false);
                        }

                    } else {
                        toast(searchedUserBean.getMessage());
                    }

                } else {
                    toast(Constants.CONNECT_SERVER_FAILED);
                }
            }
        });
    }
}
