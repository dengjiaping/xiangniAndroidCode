package com.ixiangni.app.setting;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.handongkeji.handler.RemoteDataHandler;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 关于我们
 *
 * @ClassName:AboutUsActivity
 * @PackageName:com.ixiangni.app.setting
 * @Create On 2017/6/20 0020   14:13
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/20 0020 handongkeji All rights reserved.
 */
public class AboutUsActivity extends BaseActivity {

    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    @Bind(R.id.tv_com_title)
    TextView tvComTitle;
    @Bind(R.id.tv_phone_title)
    TextView tvPhoneTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        ivLogo.setOnClickListener(v -> {
            get();
        });

    }

    private int count = 0;

    private void get() {
        HashMap<String, String> params = new HashMap<>();
        count++;
        params.put(XNConstants.textflag, count + "");
        RemoteDataHandler.asyncPost(UrlString.URL_GET_TEXT, params, this, true, response -> {

            String json = response.getJson();
            log(json);
        });
    }
}
