package com.ixiangni.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.ui.TopBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 网页浏览
 *
 * @ClassName:WebActivity
 * @PackageName:com.ixiangni.app
 * @Create On 2017/8/15 0015   17:39
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/8/15 0015 handongkeji All rights reserved.
 */
public class WebActivity extends BaseActivity {


    public static final String title = "title";
    public static final String url = "url";
    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        Intent intent = getIntent();
        String title = intent.getStringExtra(WebActivity.title);
        topBar.setCenterText(title);

        String url = intent.getStringExtra(WebActivity.url);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                webView.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.loadUrl(url);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }

    private void destroyWebView() {
        if(webView != null) {
            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            webView.freeMemory();
            webView.pauseTimers();
            webView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
        }
    }

}
