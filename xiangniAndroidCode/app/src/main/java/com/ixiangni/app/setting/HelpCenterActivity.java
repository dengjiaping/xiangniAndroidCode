package com.ixiangni.app.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.handongkeji.common.Constants;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.TextBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.presenters.GetTextPresenter;
import com.ixiangni.ui.TopBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HelpCenterActivity extends BaseActivity {

    @Bind(R.id.web_view)
    WebView webView;
    @Bind(R.id.top_bar)
    TopBar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        ButterKnife.bind(this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        initData();
    }

    private void initData() {
        showProgressDialog(Constants.MESSAGE_LOADING, true);
        String textflag = getIntent().getStringExtra(XNConstants.textflag);
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            title = "帮助中心";
        }
        topBar.setCenterText(title);

        if (CommonUtils.isStringNull(textflag)) {
            textflag = GetTextPresenter.TEXT_HELP_CENTER;
        }
        new GetTextPresenter().getText(this, textflag, new OnResult<TextBean.DataBean>() {
            @Override
            public void onSuccess(TextBean.DataBean dataBean) {
                if (webView != null) {
                    dismissProgressDialog();
                    String text = dataBean.getText();
                    webView.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
                }

            }

            @Override
            public void onFailed(String errorMsg) {

                if (webView != null) {
                    dismissProgressDialog();
                    toast(errorMsg);
                }
            }
        });

    }
}
