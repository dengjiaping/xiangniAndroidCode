package com.ixiangni.app.missyouservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.widget.SideBar;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.CityBean;
import com.ixiangni.bean.CityListBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.interfaces.OnSelectCity;
import com.ixiangni.ui.SearchView;
import com.ixiangni.util.StateLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 城市列表
 *
 * @ClassName:CityListActivity
 * @PackageName:com.ixiangni.app.missyouservice
 * @Create On 2017/7/24 0024   15:34
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/24 0024 handongkeji All rights reserved.
 */
public class CityListActivity extends BaseActivity {

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.side_bar)
    SideBar sideBar;
    @Bind(R.id.tv_show)
    TextView tvShow;
    @Bind(R.id.search_view)
    SearchView searchView;
    private CityAdapter adapter;
    private String searchCity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        adapter = new CityAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityBean item = adapter.getItem(position);

                Intent intent = new Intent();
                intent.putExtra(XNConstants.ari_city,item);
                setResult(RESULT_OK,intent);

                finish();
            }
        });

        stateLayout.setUpwithBaseAdapter(adapter, "未获取到城市");
        sideBar.setTextView(tvShow);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {

                int letterFistPoi = adapter.getLetterFistPoi(s);
                if (letterFistPoi > -1) {
                    listView.setSelection(letterFistPoi);
                }

            }
        });
        getCityList();

        EditText edtiText = searchView.getEdtiText();
        edtiText.setHint("搜索城市...");
        searchView.setListener(v -> {
            searchCity=edtiText.getText().toString().trim();
            getCityList();
            hideSoftKeyboard();
        });

    }

    private void getCityList() {


        stateLayout.showLoadView();
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());

        if (!CommonUtils.isStringNull(searchCity)) {
            params.put("cityname", searchCity);
        }

        RemoteDataHandler.asyncPost(UrlString.URL_CITY_LIST, params, this, true, response -> {
            String json = response.getJson();
            if (stateLayout != null) {
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);

                } else {
                    CityListBean cityListBean = new Gson().fromJson(json, CityListBean.class);
                    if (1 == cityListBean.getStatus()) {
                        adapter.replaceAll(cityListBean.getData());
                    } else {
                        toast(cityListBean.getMessage());
                    }
                }
            }

        });
    }

    private class CityAdapter extends QuickAdapter<CityBean> {

        private final Comparator<CityBean> comparator;

        public CityAdapter(Context context) {
            super(context, R.layout.item_city);
            comparator = new Comparator<CityBean>() {
                @Override
                public int compare(CityBean o1, CityBean o2) {
                    return o1.getFirstLetter().compareTo(o2.getFirstLetter());
                }
            };
        }

        public int getLetterFistPoi(String letter) {
            for (int i = 0; i < data.size(); i++) {
                String firstLetter = data.get(i).getFirstLetter();
                if (firstLetter.equals(letter)) {
                    log(letter + i);
                    return i;
                }
            }
            return -1;
        }

        @Override
        public void replaceAll(List<CityBean> elem) {
            Collections.sort(elem, comparator);
            super.replaceAll(elem);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, CityBean cityBean) {
            TextView tvLetter = helper.getView(R.id.tv_letter);
            int position = helper.getPosition();
            if (position - 1 >= 0 && data.get(position - 1).getFirstLetter().equals(cityBean.getFirstLetter())) {
                tvLetter.setVisibility(View.GONE);
            } else {
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText(cityBean.getFirstLetter());
            }

            helper.setText(R.id.tv_city_name, cityBean.getCityname());

        }
    }
}
