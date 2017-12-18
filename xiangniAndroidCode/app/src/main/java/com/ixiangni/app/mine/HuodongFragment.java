package com.ixiangni.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ixiangni.app.R;

/**
 * Created by Administrator on 2017/10/12.
 */

public class HuodongFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_huodong, container, false);
        WebView wvhd= (WebView) view.findViewById(R.id.web_view_huodong);
        wvhd.getSettings().setJavaScriptEnabled(true);
        wvhd.setWebViewClient(new WebViewClient());
        wvhd.setHorizontalScrollBarEnabled(false);//水平不显示
        wvhd.setVerticalScrollBarEnabled(false); //垂直不显示
        wvhd.loadUrl("https://www.0000.com");
         return view;
    }
}
