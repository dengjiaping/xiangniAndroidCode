package com.mydemo.yuanxin.explainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.mydemo.yuanxin.R;
import com.mydemo.yuanxin.util.UrlUtil;

public class HelpActivity extends AppCompatActivity {
    private ImageView back;
    private WebView mWebView;

    /**
     * 提现1 转账2 充值3
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Intent intent=getIntent();
        int type=intent.getIntExtra("type",0);


        back = (ImageView) findViewById(R.id.iv_back);
        mWebView= (WebView) findViewById(R.id.web_view_cz);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        switch (type){
            case 0:
                Toast.makeText(this, "帮助文档获取失败", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                mWebView.loadUrl(UrlUtil.TX_HELP_URL());
                break;
            case 2:
                mWebView.loadUrl(UrlUtil.ZZ_HELP_URL());
                break;
            case 3:
                mWebView.loadUrl(UrlUtil.CZ_HELP_URL());
                break;
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpActivity.this.finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }

    private void destroyWebView() {
            if(mWebView != null) {
                mWebView.clearHistory();
                mWebView.clearCache(true);
                mWebView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
                mWebView.freeMemory();
                mWebView.pauseTimers();
                mWebView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
            }
    }
}
