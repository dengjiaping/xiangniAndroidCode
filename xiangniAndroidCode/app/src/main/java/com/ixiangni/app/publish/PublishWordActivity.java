package com.ixiangni.app.publish;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.constants.UrlString;
import com.ixiangni.interfaces.OnNewsPublish;
import com.ixiangni.records.PublishAllPresenter;
import com.ixiangni.ui.TopBar;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发布文字
 *
 * @ClassName:PublishWordActivity
 * @PackageName:com.ixiangni.app.publish
 * @Create On 2017/6/21 0021   11:10
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class PublishWordActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.rb_all_can_see)
    RadioButton rbAllCanSee;
    @Bind(R.id.rb_friend_can_see)
    RadioButton rbFriendCanSee;
    @Bind(R.id.tv_select_position)
    TextView tvSelectPosition;
    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.edt_word)
    EditText edtWord;
    private PoiInfo poiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_word);
        ButterKnife.bind(this);


        topBar.setOnRightClickListener(v -> {
            publishText();
        });
        tvSelectPosition.setOnClickListener(this);
    }

    private static final String TAG = "PublishWordActivity";

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_select_position) {

            startActivityForResult(new Intent(PublishWordActivity.this,BaiduMapActivity.class),101);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101&&resultCode==RESULT_OK){
            poiInfo = data.getParcelableExtra("poiInfo");
            tvSelectPosition.setText(poiInfo.address);
        }
    }

    private void publishText() {
        String content = edtWord.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            toast("内容不能为空！");
            return;
        }

        HashMap<String, String> params = new HashMap<>();

        params.put("token", LoginHelper.getInstance().getToken(this));
        params.put("momentnewscontent", content);
        params.put("inserttype", "0");
        params.put("showtype", rbAllCanSee.isChecked() ? "0" : "1");

//        momentlng
//                momentlat
//        momentnewsaddress
        if(poiInfo!=null){
            params.put("momentlng",poiInfo.location.longitude+"");
            params.put("momentlat",poiInfo.location.latitude+"");
            params.put("momentnewsaddress", CommonUtils.isStringNull(poiInfo.address.trim())?poiInfo.name:poiInfo.address);
        }


        PublishAllPresenter presenter = new PublishAllPresenter(iView,PublishWordActivity.this);
        presenter.getParam(params,this);
    }

    PublishAllPresenter.IView iView = new PublishAllPresenter.IView() {
        @Override
        public void getDataFinish(String status, String message) {
            toast("发布成功！");
            SuperObservableManager.getInstance().getObservable(OnNewsPublish.class)
                    .notifyMethod(OnNewsPublish::onNewpublish);
            finish();
        }

        @Override
        public void onError() {

            toast("发布失败,请重试！");
        }
    };
}
