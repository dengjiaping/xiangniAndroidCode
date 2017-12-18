package com.ixiangni.app.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.R;
import com.ixiangni.app.WebActivity;
import com.ixiangni.bean.LiveListBean;
import com.ixiangni.constants.UrlString;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

/**
 * Created by Administrator on 2017/10/12.
 */

public class ZhiboFragment extends Fragment{
    private ListView listView;
    private LiveAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_live, container, false);
        listView= (ListView) view.findViewById(R.id.list_view);
        adapter = new LiveAdapter(getActivity());
        listView.setAdapter(adapter);
        RemoteDataHandler.asyncPost(UrlString.URL_LIVE_LIST, null, getActivity(), false, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData response) {

                String json = response.getJson();
                if (listView != null) {
                    if (CommonUtils.isStringNull(json)) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), Constants.CONNECT_SERVER_FAILED, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        LiveListBean liveListBean = new Gson().fromJson(json, LiveListBean.class);
                        if (1 == liveListBean.getStatus()) {
                            adapter.replaceAll(liveListBean.getData());

                        }
                    }
                }
            }
        });
         return view;
    }

    class LiveAdapter extends QuickAdapter<LiveListBean.DataBean> {

        public LiveAdapter(Context context) {
            super(context, R.layout.item_live);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, LiveListBean.DataBean dataBean) {
            String adtitle = dataBean.getAdtitle();
            String adpicture = dataBean.getAdpicture();
//            Log.w("zcq", "图片地址: "+adpicture+"标题"+adtitle );
            helper.setText(R.id.tv_title, adtitle);
            ImageView iv = helper.getView(R.id.iv_pic);
            Glide.with(context).load(adpicture).error(R.mipmap.zhibo).into(iv);
            helper.setOnClickListener(R.id.iv_pic,v -> {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra(WebActivity.title,dataBean.getAdtitle());
                intent.putExtra(WebActivity.url,dataBean.getAdcontents());
                startActivity(intent);

            });

        }
    }
}
