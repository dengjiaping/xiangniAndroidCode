package com.ixiangni.app.publish;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.handongkeji.common.Constants;
import com.handongkeji.common.HttpHelper;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.Bimp;
import com.handongkeji.utils.BitmapUtils;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.adapter.MyRvAdapter;
import com.ixiangni.adapter.MyRvViewHolder;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.bean.BaseBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.CallDialog;
import com.ixiangni.ui.ImageLabel.Dialog_tag;
import com.ixiangni.ui.ImageLabel.TagEntity;
import com.ixiangni.ui.ImageLabel.TagInfo;
import com.ixiangni.ui.ImageLabel.TagView;
import com.ixiangni.ui.ImageLabel.TagViewLeft;
import com.ixiangni.ui.ImageLabel.TagViewRight;
import com.ixiangni.util.Constant1;
import com.ixiangni.util.GsonUtils;
import com.ixiangni.util.LinearDecoration;
import com.ixiangni.util.NormalUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 发布图片-添加标签
 *
 * @ClassName:PictureTagActivity
 * @PackageName:com.ixiangni.app.publish
 * @Create On 2017/6/21 0021   13:54
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class PictureTagActivity extends BaseActivity implements TagView.TagViewListener {

    @Bind(R.id.tv_cancel)
    TextView tvCancel;
    @Bind(R.id.tv_next_step)
    TextView tvNextStep;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.rl_image_parent)
    RelativeLayout rl_image_parent;
    private int IMAGE_PICKER = 100;
    private ImageItemAdapter imageAdapter;


    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;
    private final static int TEXTSIZE = 12;        //Tagview的字体大小
    private float mPointX = 0;
    private float mPointY = 0;
    private Boolean isTagLayShow = false;    //标签图标区域是否显示

    private HashMap<Integer, List<TagView>> tagViewMaps = new HashMap<>();
    private HashMap<Integer, List<TagInfo>> tagInfoMaps = new HashMap<>();

    private List<TagView> tagViews;//view集合
    private List<TagInfo> tagInfos;//标签的集合
    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;
    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;
    private float x1;
    private float y1;
    private List<TagEntity> TagList = new ArrayList<>();
    TagEntity entity = new TagEntity();
    String[] picIds;//图片的id/////////////////////////////////
    List<com.handongkeji.utils.ImageItem> ImageList;//图片的url本地的
    List<Uri> showIamgelist;
    private int maxHeight;
    private int maxWidth;
    private int currentPosition;

    private boolean uploadFinish;
    private int scaledTouchSlop;
    private MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_tag);
        ButterKnife.bind(this);

//        ImagePicker imagePicker = ImagePicker.getInstance();
//        imagePicker.setMultiMode(true);
//        imagePicker.setSelectLimit(9);

        init();
    }

    private void init() {
        initViews();
        onTouchListener();

    }

    private void initViews() {
        myApp = MyApp.getInstance();

        scaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        Constant1.scale = getResources().getDisplayMetrics().density;

        showIamgelist = new ArrayList<>();
        ImageList = Bimp.tempSelectBitmap;//获取图片 先上传到服务器然后再 显示图片
        for (int i = 0; i < ImageList.size(); i++) {
            String imagePath = ImageList.get(i).getImagePath();
            Uri uri = Uri.parse(imagePath);
            showIamgelist.add(uri);
        }
//        if (showIamgelist.size()>0){
//            iv_labelImage.setImageURI(showIamgelist.get(0));
//        }
        imageAdapter = new ImageItemAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(new LinearDecoration(5, Color.WHITE,LinearDecoration.HORIZONTAL));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(imageAdapter);

        imageAdapter.addAll(ImageList);


        getPicUrl();//把url变成图片的url


//        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
//
//            @Override
//            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//            }
//        });



        rl_image_parent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl_image_parent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                maxHeight = rl_image_parent.getHeight();
                maxWidth = rl_image_parent.getWidth();
                currentPosition = 0;
                setImage(ImageList.get(0).getImagePath());
                getListForPosition(0);
            }
        });
    }

    private class ImageItemAdapter extends MyRvAdapter<com.handongkeji.utils.ImageItem> {

        public ImageItemAdapter(Context context) {
            super(context, R.layout.item_label);
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, com.handongkeji.utils.ImageItem item) {
            Glide.with(mContext)
                    .load(item.getImagePath())
                    .into((ImageView) holder.getView(R.id.iv_image));
            holder.setOnItemChlidClickListener(R.id.iv_image,v -> {
                String imagePath = ImageList.get(position).getImagePath();
                setImage(imagePath);
                currentPosition = position;
                getListForPosition(position);
                addTagView();
            });
        }
    }

    private void getListForPosition(int position){
        tagViews = tagViewMaps.get(position);
        if (tagViews == null) {
            tagViews = new ArrayList<>();
            tagViewMaps.put(position,tagViews);
        }

        tagInfos = tagInfoMaps.get(position);
        if (tagInfos == null) {
            tagInfos = new ArrayList<>();
            tagInfoMaps.put(position,tagInfos);
        }
    }

    private void addTagView(){
        rl_image_parent.removeAllViews();
        for (TagView tagView : tagViews) {
            rl_image_parent.addView(tagView);
        }
    }

    private void setImage(String pathName) {
//        Bitmap bitmap = BitmapFactory.decodeFile(pathName);
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        float scaleX = maxWidth * 1f / width;
//        float scaleY = maxHeight * 1f / height;
//        float minScale = Math.min(scaleX, scaleY);
//        Matrix matrix = new Matrix();
//        matrix.postScale(minScale, minScale);
//        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
//        bitmap.recycle();
//
//        int newWidth = newBitmap.getWidth();
//        int newHeight = newBitmap.getHeight();
//        Constant1.displayWidth = newWidth;
//        Constant1.displayHeight = newHeight;
//        ViewGroup.LayoutParams rlparams = rl_image_parent.getLayoutParams();
//        rlparams.width = newWidth;
//        rlparams.height = newHeight;
//        rl_image_parent.setLayoutParams(rlparams);
//
//        ViewGroup.LayoutParams ivparams = imageView.getLayoutParams();
//        ivparams.width = newWidth;
//        ivparams.height = newHeight;
//        imageView.setLayoutParams(ivparams);
//
//        imageView.setImageBitmap(newBitmap);
        Glide.with(this).load(pathName).into(imageView);
    }

    CopyOnWriteArrayList<String> pList;

    private void getPicUrl() {
        showProgressDialog("图片上传中...", false);
        uploadFinish = false;
        RemoteDataHandler.threadPool.execute(() -> {
            CountDownLatch latch = new CountDownLatch(ImageList.size());
            pList = new CopyOnWriteArrayList<>();
            picIds = new String[ImageList.size()];
            for (int i = 0; i < ImageList.size(); i++) {
                com.handongkeji.utils.ImageItem imageItem = ImageList.get(i);
                Log.i("image", "imagePath:上传前 "+imageItem.getImagePath());

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
                    if(tvNextStep==null){
                        return;
                    }
                    mProgressDialog.setMessage("上传完毕");
                    AndroidSchedulers.mainThread().createWorker().schedule(() -> mProgressDialog.dismiss(), 1200, TimeUnit.MILLISECONDS);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
//            if (data != null && requestCode == IMAGE_PICKER) {
//                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//
//            } else {
//                toast("没有数据");
//            }
//        }

    }

    @OnClick({R.id.tv_cancel, R.id.tv_next_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_next_step:

                Intent intent = new Intent(this, PicturePreviewActivity.class);
                intent.putExtra("picIds",picIds);
                startActivity(intent);
                finish();
                break;
        }
    }

    private String uploadPic(com.handongkeji.utils.ImageItem imageItem, int index, String[] picIds) {
        String imagePath = imageItem.getImagePath();

        String path = Constants.CACHE_DIR_UPLOADING_IMG + "/" + UUID.randomUUID().toString()+ ".jpg";

        File resultFile = BitmapUtils.compressBitmap(imagePath, path, -1);

        Log.i("image", "path:压缩后 "+path);
        String url = UrlString.URL_UPLOAD_IMAGE;//上传图片接口
        HashMap<String, File> fileMap = new HashMap<String, File>();

        fileMap.put("file", resultFile);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("filemark", "6");
        try {
            String result = HttpHelper.multipartPost(url, params, fileMap);
            Log.i("image", "上传:result "+result);
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

    private void getpicID(final String data, int index, String[] picIds) {
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

                Log.i("image", "insertid: "+insertid);
                picIds[index] = insertid;
            }else {
                Log.i("image", "insertFialed: "+data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //点击图片的ontouche事件

    private void onTouchListener() {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        mPointX = motionEvent.getX();
                        mPointY = motionEvent.getY();
                        showTagLinearLayout(mPointX, mPointY);
                        break;
                }
                return true;
            }

        });
    }

    private void showTagLinearLayout(float mPointX, float mPointY) {
        if (isTagLayShow) {
            isTagLayShow = false;
            //设置TagView的可以移动
//            setTagViewEnable(true);
        } else {
//            addPoint(mPointX, mPointY,currentPosition);//添加原点然后去显示动画的控件
            Dialog_tag tagDialog = new Dialog_tag(this);
            tagDialog.setListener((view, text) -> {
                editTagInfo(text);
            });
            tagDialog.show();
            isTagLayShow = true;
            //取消TagView的移动
//            setTagViewEnable(false);
        }
    }

    /**
     * 编辑标签信息
     */
    private void editTagInfo(String text) {
        //标签图标区域消失
        showTagLinearLayout(0, 0);
        //		Toast.makeText(this, k + "", Toast.LENGTH_SHORT).show();

        TagInfo tagInfo = new TagInfo();
        tagInfo.bid = 2L;
        tagInfo.bname = text;
        tagInfo.direct = getDirection(tagInfo.bname);
        tagInfo.pic_x = mPointX / Constant1.displayWidth;
        tagInfo.pic_y = mPointY / Constant1.displayHeight;
        tagInfo.type = getRandomType();
        tagInfo.type = TagInfo.Type.CustomPoint;
        switch (tagInfo.direct) {
            case Left:
                tagInfo.leftMargin = (int) (mPointX - 15 * Constant1.scale);    //根据屏幕密度计算使动画中心在点击点，15dp是margin
                tagInfo.topMargin = (int) (mPointY - 15 * Constant1.scale);
                tagInfo.rightMargin = 0;
                tagInfo.bottomMargin = 0;
                tagInfo.pic_x = tagInfo.leftMargin*1f/Constant1.displayWidth;
                tagInfo.pic_y = tagInfo.topMargin*1f/Constant1.displayHeight;
                break;
            case Right:
                tagInfo.leftMargin = 0;
                tagInfo.topMargin = (int) (mPointY - 15 * Constant1.scale);
                tagInfo.rightMargin = Constant1.displayWidth - (int) mPointX - (int) (15 * Constant1.scale);
                tagInfo.bottomMargin = 0;
                tagInfo.pic_x = tagInfo.rightMargin*1f/ Constant1.displayWidth;
                tagInfo.pic_y = tagInfo.topMargin*1f/Constant1.displayHeight;
                break;
        }
        addLabel(tagInfo, picIds[currentPosition]);
        addTagInfo(tagInfo);
    }

    /**
     * 添加标签
     */
    private void addTagInfo(final TagInfo tagInfo) {
        TagView tagView = null;
        switch (tagInfo.direct) {
            case Left:
                tagView = new TagViewLeft(this, null);
                break;
            case Right:
                tagView = new TagViewRight(this, null);
                break;
        }
        tagView.setData(tagInfo);
        tagView.setTagViewListener(this);
        tagView.setTag(tagInfo);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(tagInfo.leftMargin, tagInfo.topMargin, tagInfo.rightMargin, tagInfo.bottomMargin);
        rl_image_parent.addView(tagView, params);
        //添加TagView的移动事件
        tagView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                        xInView = event.getX();
                        yInView = event.getY();
                        xDownInScreen = event.getRawX();
                        yDownInScreen = event.getRawY();

                        xInScreen = event.getRawX();
                        yInScreen = event.getRawY();

                        x1 = event.getRawX();
                        y1 = event.getRawY();

                        break;
                    case MotionEvent.ACTION_MOVE:

                        xInScreen = event.getRawX();
                        yInScreen = event.getRawY();

                        updateTagViewPosition(v, tagInfo);

                        xDownInScreen = xInScreen;
                        yDownInScreen = yInScreen;


                        break;
                    case MotionEvent.ACTION_UP:

                        switch (tagInfo.direct) {
                            case Left:
                                tagInfo.pic_x = (v.getLeft() + 15 * Constant1.scale) / Constant1.displayWidth;
                                tagInfo.pic_y = (v.getTop() + 15 * Constant1.scale) / Constant1.displayHeight;
//                                tagInfo.pic_x = tagInfo.leftMargin*1f/Constant1.displayWidth;
//                                tagInfo.pic_y = tagInfo.topMargin*1f/Constant1.displayHeight;
                                break;
                            case Right:
                                tagInfo.pic_x = (v.getRight() - 15 * Constant1.scale) / Constant1.displayWidth;
                                tagInfo.pic_y = (v.getTop() + 15 * Constant1.scale) / Constant1.displayHeight;
//                                tagInfo.pic_x = tagInfo.rightMargin*1f/ Constant1.displayWidth;
//                                tagInfo.pic_y = tagInfo.topMargin*1f/Constant1.displayHeight;
                                break;
                        }
                        updateLabel(tagInfo);

                        if (Math.abs(x1 - xInScreen) < scaledTouchSlop && Math.abs(y1 - yInScreen) < scaledTouchSlop) {
                            return false;
                        } else {
                            return true;
                        }
                        //					break;
                }

                return false;
            }
        });
        tagInfos.add(tagInfo);
        tagViews.add(tagView);
    }

    /**
     * 移动TagView，更新位置
     */
    private void updateTagViewPosition(View v, TagInfo tagInfo) {
        //计算位移
        float xMove = xInScreen - xDownInScreen;
        float yMove = yInScreen - yDownInScreen;

        //获取View的宽度，为什么不用v.getWidth();?因为Right方向的布局有问题，是从屏幕左边缘开始，不符合需求
        int viewWidth = (int) getTagViewWidth(tagInfo.bname);

        switch (tagInfo.direct) {
            case Left:
                tagInfo.leftMargin += xMove;
                tagInfo.topMargin += yMove;
                //边界计算
                if (tagInfo.leftMargin < 0) {
                    tagInfo.leftMargin = 0;
                } else if ((tagInfo.leftMargin + viewWidth) > Constant1.displayWidth) {
                    tagInfo.leftMargin = Constant1.displayWidth - viewWidth;
                }
                break;
            case Right:
                tagInfo.topMargin += yMove;
                tagInfo.rightMargin -= xMove;
                //边界计算
                if (tagInfo.rightMargin < 0) {
                    tagInfo.rightMargin = 0;
                } else if ((tagInfo.rightMargin + viewWidth) > Constant1.displayWidth) {
                    tagInfo.rightMargin = Constant1.displayWidth - viewWidth;
                }
                break;
        }
        //边界计算
        if (tagInfo.topMargin < 0) {
            tagInfo.topMargin = 0;
        } else if ((tagInfo.topMargin + v.getHeight()) > Constant1.displayHeight) {
            tagInfo.topMargin = Constant1.displayHeight - v.getHeight();
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(tagInfo.leftMargin, tagInfo.topMargin, tagInfo.rightMargin, tagInfo.bottomMargin);
        v.setLayoutParams(params);
    }

    /**
     * 获取TagView的宽度
     */
    private float getTagViewWidth(String str) {
        float viewWidth = NormalUtils.GetTextWidth(str, TEXTSIZE * Constant1.scale);
        viewWidth += (46 * Constant1.scale);            //46dp是TagView除去text部分的宽度，可以从布局中查看
        return viewWidth;
    }

    /**
     * 获取TagView的高度
     */
    private float getTagViewHeight(String str) {
        float viewHeight = (30 * Constant1.scale);    //30dp是TagView的高度，可以从布局中查看
        return viewHeight;
    }

    /**
     * 获取TagView的方向
     */
    private TagInfo.Direction getDirection(String str) {
        float showSize = NormalUtils.GetTextWidth(str, TEXTSIZE * Constant1.scale);
        showSize += (32 * Constant1.scale);
        if ((mPointX + showSize) > Constant1.displayWidth) {
            return TagInfo.Direction.Right;
        } else {
            return TagInfo.Direction.Left;
        }
    }

    private TagInfo.Type getRandomType() {
        Random random = new Random();
        int ran = random.nextInt(TagInfo.Type.size());
        if (0 == ran) {
            return TagInfo.Type.Undefined;
        } else if (1 == ran) {
            return TagInfo.Type.Exists;
        } else if (2 == ran) {
            return TagInfo.Type.CustomPoint;
        } else {
            return TagInfo.Type.OfficalPoint;
        }
    }

    /**
     * 点击图片后添加小圆点
     */
//    private void addPoint(float x, float y,int currentPosition) {
////        Bitmap photo = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();
//        String mImagePath = ImageList.get(currentPosition).getImagePath();
//        entity.setTagurl(mImagePath);
//        entity.setTagX(x);
//        entity.setTagY(y);
//        Bitmap photo = BitmapFactory.decodeFile(mImagePath);
//        Bitmap point = BitmapFactory.decodeResource(this.getResources(), R.drawable.brand_tag_point_white_bg);
//        Bitmap a = createBitmap(photo, point, x, y);
//        iv_labelImage.setImageBitmap(a);
//        photo.recycle();
//        point.recycle();
//    }

    /**
     * 点击图片后添加小圆点
     */
    private Bitmap createBitmap(Bitmap src, Bitmap point, float x, float y) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = point.getWidth();
        int wh = point.getHeight();
        //create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        //draw src into
        cv.drawBitmap(src, 0, 0, null);//在 0，0坐标开始画入src
        //draw watermark into
        cv.drawBitmap(point, (x - (ww / 2)), (y - (wh / 2)), null);//在src的右下角画入水印
        //save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);//保存
        //store
        cv.restore();//存储

        point.recycle();
        return newb;
    }

    /**
     * 点击标签的监听
     */
    @Override
    public void onTagViewClicked(View view, TagInfo info) {
        CallDialog dialog = new CallDialog(this);
        dialog.setMsg("确定要删除标签吗?");
        dialog.setNegativeButtonListener(v -> {
            deleteLabel(view, info);
            dialog.dismissDialog();
        });
        dialog.showDialog();
    }

    private void addLabel(TagInfo tagInfo, String picId) {

        double pic_x = tagInfo.pic_x;
        double pic_y = tagInfo.pic_y;

        String url = UrlString.URL_ADD_LABEL;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", myApp.getUserTicket());
        params.put("insertid", picId);
        params.put("labelx", pic_x + "");
        params.put("labely", pic_y + "");
        params.put("labelcontent", tagInfo.bname);
        RemoteDataHandler.asyncTokenPost(url, this, false, params, data -> {
            String json = data.getJson();
            if (!CommonUtils.isStringNull(json)) {
                try {
                    JSONObject object = new JSONObject(json);
                    int status = object.getInt("status");
                    if (status == 1) {
                        JSONObject jsonObject = object.getJSONObject("data");
                        String labelid = jsonObject.getString("labelid");
                        tagInfo.tagid = labelid;
                        Log.d(TAG, "addLabel: 上传成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static final String TAG = "PictureTagActivity";


    /**
     * 修改标签
     *
     * @param tagInfo
     */
    private void updateLabel(TagInfo tagInfo) {

        double pic_x = tagInfo.pic_x;
        double pic_y = tagInfo.pic_y;

        String url = UrlString.URL_UPDATE_LABEL;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", myApp.getUserTicket());
        params.put("labelid", tagInfo.tagid);
        params.put("labelx", pic_x + "");
        params.put("labely", pic_y + "");
        params.put("labelcontent", tagInfo.bname);
        RemoteDataHandler.asyncTokenPost(url, this, false, params, data -> {
            String json = data.getJson();
            if (!CommonUtils.isStringNull(json)) {
                BaseBean baseBean = (BaseBean) GsonUtils.fromJson(json, BaseBean.class);
                if (baseBean.getStatus() == 1) {
                    Log.d(TAG, "updateLabel: 修改成功 " + json);
                }
            }
        });
    }

    private void deleteLabel(View view, TagInfo tagInfo) {

        String url = UrlString.URL_DELETE_LABEL;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", myApp.getUserTicket());
        params.put("labelid", tagInfo.tagid);
        RemoteDataHandler.asyncTokenPost(url, this, false, params, data -> {
            String json = data.getJson();
            if (!CommonUtils.isStringNull(json)) {
                BaseBean baseBean = (BaseBean) GsonUtils.fromJson(json, BaseBean.class);
                if (baseBean.getStatus() == 1) {
                    ViewParent parent = view.getParent();
                    if (parent != null) {
                        ViewGroup p = (ViewGroup) parent;
                        p.removeView(view);
                    }
                    Log.d(TAG, "deleteLabel: 删除成功 " + json);
                }
            }
        });
    }

//    private class IMageAdapter extends BaseQuickAdapter<com.handongkeji.utils.ImageItem, BaseViewHolder> {
//
//        public IMageAdapter() {
//            super(R.layout.item_label);
//        }
//
//        @Override
//        protected void convert(BaseViewHolder helper, com.handongkeji.utils.ImageItem item) {
//            Glide.with(PictureTagActivity.this)
//                    .load(item.getImagePath())
//                    .into((ImageView) helper.getView(R.id.iv_image));
//            helper.addOnClickListener(R.id.iv_image);
//        }
//    }
}
