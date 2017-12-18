package com.ixiangni.app.chat;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.GroupSearchBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.util.GlideUtil;
import com.ixiangni.util.StateLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nevermore.oceans.utils.ListUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddGroupActivity extends BaseActivity implements View.OnKeyListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.edt_search)
    EditText edtSearch;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.activity_add_group)
    LinearLayout activityAddGroup;
    @Bind(R.id.list_view)
    ListView listView;
    private GroupSearchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        ButterKnife.bind(this);
        edtSearch.setOnKeyListener(this);
        mAdapter = new GroupSearchAdapter(this);
        listView.setAdapter(mAdapter);


    }

    @OnClick({R.id.iv_back, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                searchGroup();
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

            searchGroup();
            return true;
        }
        return false;
    }


    private class GroupSearchAdapter extends QuickAdapter<GroupSearchBean.DataBean>{

        public GroupSearchAdapter(Context context) {
            super(context, R.layout.item_contact);
        }

        @Override
        protected void convert(BaseAdapterHelper baseAdapterHelper, GroupSearchBean.DataBean dataBean) {
            String groupchatname = dataBean.getGroupchatname();
            String groupchatpic = dataBean.getGroupchatpic();
            baseAdapterHelper.setText(R.id.tv_user_nick,groupchatname);
            ImageView imageView = baseAdapterHelper.getView(R.id.iv_icon);
            GlideUtil.loadRoundImage(context,groupchatpic,imageView,R.mipmap.qunliao);

        }
    }
    /**
     * 搜索群
     */
    private void searchGroup() {
        String groupName = edtSearch.getText().toString().trim();
        if (TextUtils.isEmpty(groupName)) {
            toast("请输入您要搜索的群名称...");
            return;
        }
        hideSoftKeyboard();
        stateLayout.showLoadView();
        HashMap<String, String> params = new HashMap<>();
        params.put("groupchatname", groupName);
        RemoteDataHandler.asyncPost(UrlString.URL_SEARCH_ADD_GROUP, params, this, false, response -> {
            if (stateLayout != null) {
                stateLayout.showContenView();
                String json = response.getJson();
                log(json);
                if (!CommonUtils.isStringNull(json)) {
                    GroupSearchBean groupSearchBean = new Gson().fromJson(json, GroupSearchBean.class);
                    if (1 == groupSearchBean.getStatus()) {
                        List<GroupSearchBean.DataBean> data = groupSearchBean.getData();
                        if (ListUtil.isEmptyList(data)) {
                            stateLayout.showNoDataView();
                            stateLayout.setNodataMessage("抱歉，未搜索到你要查找的群");
                        } else {
                            stateLayout.showContenView();
                            mAdapter.replaceAll(data);

                        }

                    }
                }

            }
        });
    }
}
