package com.ixiangni.app.missyouservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.widget.SideBar;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.CCityListBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.ui.SearchView;
import com.ixiangni.ui.TopBar;
import com.ixiangni.util.StateLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 酒店火车票 的城市列表
 *
 * @ClassName:CityList0Activity
 * @PackageName:com.ixiangni.app.missyouservice
 * @Create On 2017/7/27 0027   14:15
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/27 0027 handongkeji All rights reserved.
 */
public class CityList0Activity extends BaseActivity {


    @Bind(R.id.search_view)
    SearchView searchView;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.side_bar)
    SideBar sideBar;
    @Bind(R.id.tv_show)
    TextView tvShow;
    @Bind(R.id.top_bar)
    TopBar topBar;

    private String searchCity = null;
    private CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list0);
        ButterKnife.bind(this);

        adapter = new CityAdapter(this);
        listView.setAdapter(adapter);
        stateLayout.setUpwithBaseAdapter(adapter, "未搜索到结果...");

        topBar.setOnRightClickListener(v -> {
            onBackPressed();
        });
        searchView.setListener(v -> {
            EditText edtiText = searchView.getEdtiText();
            searchCity = edtiText.getText().toString().trim();
            getCityList();
        });
        searchView.getEdtiText().setHint("请输入要搜索的城市...");


        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {

                int poi = adapter.getPoi(s);
                if (poi != -1) {
                    listView.setSelection(poi);
                }
            }
        });

        sideBar.setTextView(tvShow);
        getCityList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getCityList() {


        stateLayout.showLoadView();
        HashMap<String, String> params = null;
        if (!TextUtils.isEmpty(searchCity)) {
            params = new HashMap<>();
            params.put("areaname", searchCity);
        }
        RemoteDataHandler.asyncPost(UrlString.URL_CITY_LIST2, params, this, true, response -> {
            String json = response.getJson();
            if (stateLayout != null) {
                log(json);
                if (TextUtils.isEmpty(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    CCityListBean cCityListBean = new Gson().fromJson(json, CCityListBean.class);
                    if (1 == cCityListBean.getStatus()) {
                        List<CCityListBean.DataBean> data = cCityListBean.getData();
                        adapter.replaceAll(data);

                    } else {

                        toast(cCityListBean.getMessage());
                    }
                }

            }
        });
    }

    private class CityAdapter extends QuickAdapter<CCityListBean.DataBean> {

        private final Comparator<CCityListBean.DataBean> comparator;

        public CityAdapter(Context context) {
            super(context, R.layout.item_city);
            comparator = new Comparator<CCityListBean.DataBean>() {
                @Override
                public int compare(CCityListBean.DataBean o1, CCityListBean.DataBean o2) {

                    return o1.getFirstLetter().compareTo(o2.getFirstLetter());
                }
            };
        }

        public int getPoi(String s) {
            for (int i = 0; i < data.size(); i++) {

                CCityListBean.DataBean dataBean = data.get(i);
                if (dataBean.getFirstLetter().equals(s)) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public void replaceAll(List<CCityListBean.DataBean> elem) {
            Collections.sort(elem, comparator);

            super.replaceAll(elem);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, CCityListBean.DataBean dataBean) {
            int position = helper.getPosition();
            TextView tvLetter = helper.getView(R.id.tv_letter);

            if (position - 1 >= 0 && dataBean.getFirstLetter().equals(getItem(position - 1).getFirstLetter())) {
                tvLetter.setVisibility(View.GONE);
            } else {
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText(dataBean.getFirstLetter());
            }
            helper.setText(R.id.tv_city_name, dataBean.getAreaname());

            helper.setOnClickListener(R.id.tv_city_name, v -> {
                Intent intent = new Intent();
                intent.putExtra(XNConstants.CITY_NAME, dataBean.getAreaname());
                setResult(RESULT_OK, intent);
                onBackPressed();
            });
        }
    }
}
