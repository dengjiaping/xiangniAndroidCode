package com.ixiangni.app.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.RegexUtils;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.constants.UrlString;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 意见反馈
 *
 * @ClassName:FeedBackActivity
 * @PackageName:com.ixiangni.app.setting
 * @Create On 2017/6/20 0020   16:12
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/20 0020 handongkeji All rights reserved.
 */
public class FeedBackActivity extends BaseActivity {

    @Bind(R.id.edt_phone)
    EditText edtPhone;
    @Bind(R.id.edt_content)
    EditText edtContent;
    @Bind(R.id.edt_submit)
    Button edtSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.edt_submit)
    public void onViewClicked() {
        String phoneNum = edtPhone.getText().toString().trim();
        if(!RegexUtils.checkMobile(phoneNum)&&!RegexUtils.isFixedPhone(phoneNum)){
            toast("联系方式不正确！");
            return;
        }

        String content = edtContent.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            toast("请填写反馈内容！");
            return;
        }

        feedBack(phoneNum,content);

    }

    /**
     * 反馈
     * @param phoneNum
     * @param content
     */
    private void feedBack(String phoneNum, String content) {

        if(!LoginHelper.getInstance().isLogin(this)){
            toast("请先登录！");
            return;
        }

        showProgressDialog("反馈中...",false);
        HashMap<String, String> params = new HashMap<>();
        params.put("token",LoginHelper.getInstance().getToken(this));
        Log.w("zcq", "token2017.11.3"+LoginHelper.getInstance().getToken(this) );

        params.put("phone",phoneNum);
        params.put("content",content);
        RemoteDataHandler.asyncPost(UrlString.URL_FEEDBACK,params,mContext,false, callback->{
            String json = callback.getJson();
            if(mContext!=null){
                dismissProgressDialog();
                log(json);
                handJson(json,"提交成功，我们会及时处理您的意见！");
            }

        });

    }
}
