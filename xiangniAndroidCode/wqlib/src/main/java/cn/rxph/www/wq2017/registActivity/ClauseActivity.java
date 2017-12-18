package cn.rxph.www.wq2017.registActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import cn.rxph.www.wq2017.R;
import cn.rxph.www.wq2017.base.BaseActivity;
import cn.rxph.www.wq2017.bean.RegisteInfo;
import cn.rxph.www.wq2017.utils.UrlUtil;


public class ClauseActivity extends BaseActivity {


    private CheckBox cbAgree;
    private RegisteInfo registeInfo;
    private ImageView ivBack;
    private Button previous,next;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clause);
        cbAgree= (CheckBox) findViewById(R.id.cb_agree);
        ivBack= (ImageView) findViewById(R.id.iv_back);
        previous= (Button) findViewById(R.id.btn_previous);
        next= (Button) findViewById(R.id.btn_next);

        setlistener();
        cbAgree.setChecked(true);
        registeInfo = (RegisteInfo) getIntent().getSerializableExtra("registInfo");

        try {
             mWebView= (WebView) findViewById(R.id.wv_tiaokaung);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
            mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
            mWebView.loadUrl(UrlUtil.CLAUSE_URL());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setlistener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClauseActivity.this.finish();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClauseActivity.this.finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAgree.isChecked()) {
                    Toast.makeText(ClauseActivity.this, "同意协议才可以进行下一步^_^", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent_n = new Intent(ClauseActivity.this, InfoCheckActivity.class);
                    intent_n.putExtra("registInfo", registeInfo);
                    startActivity(intent_n);

                }
            }
        });
        cbAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
