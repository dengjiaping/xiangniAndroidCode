package com.ixiangni.app.publish;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.handongkeji.common.Constants;
import com.handongkeji.common.HttpHelper;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.Bimp;
import com.handongkeji.utils.BitmapUtils;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.common.BusAction;
import com.ixiangni.constants.UrlString;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.nevermore.oceans.utils.ListUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;

public class PublishPictActivity extends BaseActivity {


    private int REQUEST_CODE_SELECT =100;
    private String Pic_type;
    private boolean uploadFinish;
    String[] picIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_pict);
         Pic_type = getIntent().getStringExtra(BusAction.PIC_TAG);

        ImagePicker instance = ImagePicker.getInstance();
        instance.setMultiMode(true);
        instance.setSelectLimit(9);
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, false); // 是否是直接打开相机
        startActivityForResult(intent, REQUEST_CODE_SELECT);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED){
            finish();
        }
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                ArrayList<com.handongkeji.utils.ImageItem> items = new ArrayList<>();

                for (int i = 0; i < images.size(); i++) {
                    ImageItem imageItem = images.get(i);
                    com.handongkeji.utils.ImageItem item= new com.handongkeji.utils.ImageItem();
                    item.setImagePath(imageItem.path);
                    items.add(item);
                }

                Bimp.tempSelectBitmap = items;

                if (BusAction.PIC_TAG_ADD.equals(Pic_type)){
                    //从文件夹添加图片跳转过来的

                        getPicUrl(items);

                }else if (BusAction.PIC_TAG_CIRCLE.equals(Pic_type)){
                    //从朋友圈调转过来的
                    Intent intent = new Intent(this, PictureTagActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0,0);
                }



            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }
    CopyOnWriteArrayList<String> pList;
    private void getPicUrl(ArrayList<com.handongkeji.utils.ImageItem> ImageList) {
        showProgressDialog("图片上传中...", false);
        uploadFinish = false;
        RemoteDataHandler.threadPool.execute(() -> {
            CountDownLatch latch = new CountDownLatch(ImageList.size());
            pList = new CopyOnWriteArrayList<>();
            picIds = new String[ImageList.size()];
            for (int i = 0; i < ImageList.size(); i++) {
                com.handongkeji.utils.ImageItem imageItem = ImageList.get(i);
                final int index = i;
                RemoteDataHandler.threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        pList.add(uploadPic(imageItem, index, picIds));
                        latch.countDown();
                    }
                });
            }
            try {
                latch.await();
                uploadFinish = true;
                AndroidSchedulers.mainThread().createWorker().schedule(() -> {
                    mProgressDialog.setMessage("上传完毕");
                    AndroidSchedulers.mainThread().createWorker().schedule(() ->
                        mProgressDialog.dismiss(), 1200, TimeUnit.MILLISECONDS
                    );
                    PublishPictActivity.this.finish();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
    private String uploadPic(com.handongkeji.utils.ImageItem imageItem, int index, String[] picIds) {
        String imagePath = imageItem.getImagePath();
        String path = Constants.CACHE_DIR_UPLOADING_IMG + "/" + System.currentTimeMillis() + ".jpg";
        File resultFile = BitmapUtils.compressBitmap(imagePath, path, -1);

        String url = UrlString.URL_UPLOAD_IMAGE;//上传图片接口
        HashMap<String, File> fileMap = new HashMap<String, File>();

        fileMap.put("file", resultFile);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("filemark", "6");
        try {
            String result = HttpHelper.multipartPost(url, params, fileMap);
            if (result != null && !"null".equals(result)) {
                JSONObject object = new JSONObject(result);
                String data = object.getString("data");
                getpicID(data, index, picIds);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void getpicID(String data, int index, String[] picIds) {
        String url = UrlString.ADD_PUBLISH_IMAGE;
        HashMap<String, String> params = new HashMap<>();
        params.put("inserturl", data);
        params.put("token", LoginHelper.getInstance().getToken(this));
        try {
            String json = HttpHelper.post(url, params);
            if (json == null || "null".equals(json)) return;
            JSONObject object = new JSONObject(json);
            int status = object.getInt("status");
            if (status == 1) {
                JSONObject result = object.getJSONObject("data");
                String insertid = result.getString("insertid");
                picIds[index] = insertid;

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
