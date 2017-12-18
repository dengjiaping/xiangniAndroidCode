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
import com.ixiangni.constants.UrlString;

/**
 * Created by Administrator on 2017/10/12.
 */

public class GonggaoFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gonggao, container, false);
        WebView wvgg= (WebView) view.findViewById(R.id.web_view_gonggao);
        wvgg.getSettings().setJavaScriptEnabled(true);
        wvgg.setWebViewClient(new WebViewClient());
        wvgg.setHorizontalScrollBarEnabled(false);//水平不显示
        wvgg.setVerticalScrollBarEnabled(false); //垂直不显示
        wvgg.loadUrl(UrlString.URL_GONGGAO);
         return view;
    }
}
