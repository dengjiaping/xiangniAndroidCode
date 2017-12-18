package com.ixiangni.app.publish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.bumptech.glide.Glide;
import com.handongkeji.utils.Bimp;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.utils.ImageItem;
import com.ixiangni.adapter.MyRvAdapter;
import com.ixiangni.adapter.MyRvViewHolder;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.interfaces.OnNewsPublish;
import com.ixiangni.records.PublishAllPresenter;
import com.ixiangni.ui.TopBar;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布图片预览
 *
 * @ClassName:PicturePreviewActivity
 * @PackageName:com.ixiangni.app.publish
 * @Create On 2017/6/21 0021   13:47
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */

public class PicturePreviewActivity extends BaseActivity implements PublishAllPresenter.IView{

    @Bind(R.id.edt_content)
    EditText edtContent;
    @Bind(R.id.rb_all_can_see)
    RadioButton rbAllCanSee;
    @Bind(R.id.rb_friend_can_see)
    RadioButton rbFriendCanSee;
    @Bind(R.id.tv_select_position)
    TextView tvSelectPosition;
    @Bind(R.id.recycle_view)
    RecyclerView recycleView;
    @Bind(R.id.top_bar)
    TopBar topBar;
    private PoiInfo poiInfo;

    private String[] picIds;

    private boolean commitLabelComplete;
    private boolean publishComplete;
    private PublishAllPresenter presenter;
    private ImageItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_preview);
        ButterKnife.bind(this);
        init();
        initView();

        presenter = new PublishAllPresenter(this,this);
    }

    private class ImageItemAdapter extends MyRvAdapter<ImageItem>{

        public ImageItemAdapter(Context context) {
            super(context, R.layout.item_label);
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, ImageItem item) {
            Glide.with(mContext)
                .load(item.getImagePath())
                .into((ImageView) holder.getView(R.id.iv_image));
            holder.setOnItemChlidClickListener(R.id.iv_image,v -> {

            });
        }
    }

    private void initView() {

        adapter = new ImageItemAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleView.setLayoutManager(manager);
        recycleView.setAdapter(adapter);

        ArrayList<ImageItem> tempSelectBitmap = Bimp.tempSelectBitmap;
        adapter.addAll(tempSelectBitmap);

        topBar.setOnRightClickListener(rightListener);

    }
    private View.OnClickListener rightListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


//            if(TextUtils.isEmpty(edtContent.getText().toString().trim())){
//                toast("请填写发布内容！");
//                return;
//            }

            showProgressDialog("发布中...",false);
            HashMap<String, String> params = getParams();
            presenter.getParam(params,PicturePreviewActivity.this);

        }
    };

    private void init() {
        Intent intent = getIntent();
        picIds = intent.getStringArrayExtra("picIds");
        Log.i("image", "init: "+picIds);
    }

    /**
     * 发布图片
     * @return
     */
    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        String momentnewscontent = edtContent.getText().toString().trim();
        params.put("momentnewscontent", momentnewscontent);//极友圈内容
        params.put("inserttype", "1");//插入类别 0：不插入 1：插入图片 2：插入语音 3：插入视频

        if(poiInfo!=null){
            String momentlng = poiInfo.location.latitude+"";
            params.put("momentlng", momentlng);//经度

            String momentlat = poiInfo.location.longitude+"";
            params.put("momentlat", momentlat);//纬度

            String momentnewsaddress = CommonUtils.isStringNull(poiInfo.address.trim())?poiInfo.name:poiInfo.address;
            params.put("momentnewsaddress", momentnewsaddress);//极友圈地标
        }

        String showtype = rbAllCanSee.isChecked()?"0":"1";
        params.put("showtype", showtype);//可见类型  0：全部  1：好友
//        params.put("inserturl",inserturl);//插入链接 当插入视频和语音是使用该字段传url

        StringBuffer sb = new StringBuffer();
        for (String picId : picIds) {
            sb.append(picId);
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        params.put("insertid", sb.toString().trim());//插入图片的Id串   逗号隔开  5,6,8,9
        return params;
    }

    @OnClick(R.id.tv_select_position)
    public void onViewClicked() {
        startActivityForResult(new Intent(this, BaiduMapActivity.class), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            poiInfo = data.getParcelableExtra(BaiduMapActivity.POIINFO);
            tvSelectPosition.setText(CommonUtils.isStringNull(poiInfo.address.trim()) ? poiInfo.name : poiInfo.address);
        }
    }

    @Override
    public void getDataFinish(String status, String message) {
        dismissProgressDialog();
        toast("发布成功！");
        SuperObservableManager.getInstance().getObservable(OnNewsPublish.class).notifyMethod(OnNewsPublish::onNewpublish);
        finish();

    }

    @Override
    public void onError() {
        dismissProgressDialog();
    }
}
