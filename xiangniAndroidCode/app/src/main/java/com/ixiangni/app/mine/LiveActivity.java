package com.ixiangni.app.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.R;
import com.ixiangni.app.WebActivity;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.LiveListBean;
import com.ixiangni.constants.UrlString;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 直播
 *
 * @ClassName:LiveActivity
 * @PackageName:com.ixiangni.app.mine
 * @Create On 2017/6/21 0021   14:34
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */

public class LiveActivity extends BaseActivity {

    @Bind(R.id.list_view)
    ListView listView;
    private LiveAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        ButterKnife.bind(this);
        adapter = new LiveAdapter(this);
        listView.setAdapter(adapter);
        RemoteDataHandler.asyncPost(UrlString.URL_LIVE_LIST, null, this, false, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData response) {

                String json = response.getJson();
                log(json);
                if (listView != null) {
                    if (CommonUtils.isStringNull(json)) {
                        toast(Constants.CONNECT_SERVER_FAILED);
                    } else {
                        LiveListBean liveListBean = new Gson().fromJson(json, LiveListBean.class);
                        if (1 == liveListBean.getStatus()) {
                            adapter.replaceAll(liveListBean.getData());

                        }
                    }
                }
            }
        });
    }

    class LiveAdapter extends QuickAdapter<LiveListBean.DataBean> {

        public LiveAdapter(Context context) {
            super(context, R.layout.item_live);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, LiveListBean.DataBean dataBean) {
            String adtitle = dataBean.getAdtitle();
            String adpicture = dataBean.getAdpicture();
            helper.setText(R.id.tv_title, adtitle);
            ImageView iv = helper.getView(R.id.iv_pic);
            Glide.with(context).load(adpicture).error(R.mipmap.zhibo).into(iv);
            helper.setOnClickListener(R.id.iv_pic,v -> {

                Intent intent = new Intent(LiveActivity.this, WebActivity.class);
                intent.putExtra(WebActivity.title,dataBean.getAdtitle());
                intent.putExtra(WebActivity.url,dataBean.getAdcontents());
                startActivity(intent);

            });

        }
    }
}
