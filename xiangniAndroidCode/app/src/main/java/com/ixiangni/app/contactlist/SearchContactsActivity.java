package com.ixiangni.app.contactlist;

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
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.user.PersonPageActivity;
import com.ixiangni.app.user.SearchUserActivity;
import com.ixiangni.bean.SearchFriendBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.ui.SearchView;
import com.ixiangni.util.GlideUtil;
import com.ixiangni.util.StateLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 搜索联系人
 *
 * @ClassName:SearchContactsActivity
 * @PackageName:com.ixiangni.app.contactlist
 * @Create On 2017/6/21 0021   18:05
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class SearchContactsActivity extends BaseActivity {

    @Bind(R.id.search_View)
    SearchView searchView;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    private FriendAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contacts);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        searchView.edtSearch.setHint("搜索联系人...");
        searchView.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = searchView.edtSearch.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    toast("请输入搜索内容");
                    return;
                }

                search(content);
            }


        });

        mAdapter = new FriendAdapter(this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchFriendBean.DataBean item = mAdapter.getItem(position);
                int userid = item.getUserid();
                PersonPageActivity.start(SearchContactsActivity.this,""+userid);


            }
        });

        stateLayout.setUpwithBaseAdapter(mAdapter,"未搜索到该用户...");
    }

    /**
     * 搜索联系人
     *
     * @param content
     */
    private void search(String content) {
        stateLayout.showLoadViewNoContent("搜索中...");
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("usernick", content);
        RemoteDataHandler.asyncPost(UrlString.URL_SEARCH_FRIEND, params, this, false, response -> {
            if(stateLayout!=null){
            String json = response.getJson();
            log(json);
                if(CommonUtils.isStringNull(json)){

                    toast(Constants.CONNECT_SERVER_FAILED);
                }else {
                    SearchFriendBean searchFriendBean = new Gson().fromJson(json, SearchFriendBean.class);
                    if(1==searchFriendBean.getStatus()){
                        mAdapter.replaceAll(searchFriendBean.getData());
                    }else {
                        toast(searchFriendBean.getMessage());
                    }
                }



            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
    }

    private class FriendAdapter extends QuickAdapter<SearchFriendBean.DataBean> {

        public FriendAdapter(Context context) {
            super(context, R.layout.item_search);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, SearchFriendBean.DataBean dataBean) {
            String usernick = dataBean.getUsernick();
            String userpic = dataBean.getUserpic();
            helper.setText(R.id.tv_user_nick, usernick);
            ImageView iv = helper.getView(R.id.iv_user_icon);

            GlideUtil.loadRoundImage(context, userpic, iv, R.mipmap.touxiangmoren);

        }
    }
}
