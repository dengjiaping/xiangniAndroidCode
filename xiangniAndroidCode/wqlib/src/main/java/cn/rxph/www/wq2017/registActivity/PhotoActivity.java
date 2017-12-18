package cn.rxph.www.wq2017.registActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.rxph.www.wq2017.R;
import cn.rxph.www.wq2017.base.BaseActivity;
import cn.rxph.www.wq2017.bean.RegisteInfo;
import cn.rxph.www.wq2017.utils.FileUtils;
import cn.rxph.www.wq2017.utils.PictureUtil;

/**
 * Created by Administrator on 2017/8/8.
 * 拍照
 */

public class PhotoActivity extends BaseActivity {
    private static final String[] PERMISSION_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 100;

    public static final int TAKE_PHOTO_SFZZM = 1;
    public static final int TAKE_PHOTO_SFZFM = 2;
    public static final int TAKE_PHOTO_SCSFZ = 3;
    public static final int TAKE_PHOTO_HY = 4;
    public static final int CHOOSE_PHOTO_SFZZM = 11;
    public static final int CHOOSE_PHOTO_SFZFM = 22;
    public static final int CHOOSE_PHOTO_SCSFZ = 33;
    public static final int CHOOSE_PHOTO_HY = 44;
    public static final int CHOOSE_PHOTO = 0;


    private Button btnPrevious, btnNext;

    private ImageView ivSfzzm, ivScsfz, ivSfzfm, ivHy, ivBack;
    private RegisteInfo registeInfo;
    private RegisteInfo registInfoAgain;


    private File sfzzmImage;
    private Uri sfzzmImageUri;
    private File sfzfmImage;
    private Uri sfzfmImageUri;
    private File scsfzImage;
    private Uri scsfzImageUri;
    private File hyImage;
    private Uri hyImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photos);
        initeView();
        //动态sd权限申请
        verifyStoragePermissions(this);

        setListner();
        //创建存放图片的文件
        createPicFile();
        registeInfo = (RegisteInfo) getIntent().getSerializableExtra("registInfo");
        registInfoAgain = (RegisteInfo) getIntent().getSerializableExtra("registeInfoAgain");
        if (registInfoAgain != null) {
            registeInfo.setAgain(1);
            againWrite();
        }
    }

    private void setListner() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoActivity.this.finish();
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoActivity.this.finish();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File zm = registeInfo.getSfzzm();
                File fm = registeInfo.getSfzfm();
                File sc = registeInfo.getScsfz();
                File hy = registeInfo.getHy();
                if (zm == null || fm == null || sc == null || hy == null) {
                    Toast.makeText(PhotoActivity.this, "您有未拍摄的照片或者照片错误请重新拍摄！！", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent_n = new Intent(PhotoActivity.this, ClauseActivity.class);
                    intent_n.putExtra("registInfo", registeInfo);
                    startActivity(intent_n);
                }
            }
        });


        ivSfzzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListAlertDialog(v, TAKE_PHOTO_SFZZM, CHOOSE_PHOTO_SFZZM);

            }
        });
        ivSfzfm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListAlertDialog(v, TAKE_PHOTO_SFZFM, CHOOSE_PHOTO_SFZFM);
            }
        });
        ivScsfz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListAlertDialog(v, TAKE_PHOTO_SCSFZ, CHOOSE_PHOTO_SCSFZ);
            }
        });
        ivHy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListAlertDialog(v, TAKE_PHOTO_HY, CHOOSE_PHOTO_HY);
            }
        });

    }

    private void initeView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivSfzzm = (ImageView) findViewById(R.id.iv_sfzzm);
        ivSfzfm = (ImageView) findViewById(R.id.iv_sfzfm);
        ivScsfz = (ImageView) findViewById(R.id.iv_scsfz);
        ivHy = (ImageView) findViewById(R.id.iv_hy);

        btnPrevious = (Button) findViewById(R.id.btn_previous);
        btnNext = (Button) findViewById(R.id.btn_next);


    }

    private void againWrite() {

        try {
            ivSfzzm.setImageBitmap(BitmapFactory.decodeFile(registInfoAgain.getSfzzm().getPath()));
            ivSfzfm.setImageBitmap(BitmapFactory.decodeFile(registInfoAgain.getSfzfm().getPath()));
            ivScsfz.setImageBitmap(BitmapFactory.decodeFile(registInfoAgain.getScsfz().getPath()));
            ivHy.setImageBitmap(BitmapFactory.decodeFile(registInfoAgain.getHy().getPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        registeInfo.setSfzzm(registInfoAgain.getSfzzm());
        registeInfo.setSfzfm(registInfoAgain.getSfzfm());
        registeInfo.setScsfz(registInfoAgain.getScsfz());
        registeInfo.setHy(registInfoAgain.getHy());
    }

    private void createPicFile() {
        // 创建File对象，用于存储拍照后的图片
        sfzzmImage = new File(getExternalCacheDir(), "sfzzm_image.JPEG");
        sfzfmImage = new File(getExternalCacheDir(), "sfzfm_image.JPEG");
        scsfzImage = new File(getExternalCacheDir(), "scsfz_image.JPEG");
        hyImage = new File(getExternalCacheDir(), "hy_image.JPEG");
        try {
            if (sfzzmImage.exists()) {
                sfzzmImage.delete();
            }
            sfzzmImage.createNewFile();

            if (sfzfmImage.exists()) {
                sfzfmImage.delete();
            }
            sfzfmImage.createNewFile();

            if (scsfzImage.exists()) {
                scsfzImage.delete();
            }
            scsfzImage.createNewFile();

            if (hyImage.exists()) {
                hyImage.delete();
            }
            hyImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            sfzzmImageUri = Uri.fromFile(sfzzmImage);
            sfzfmImageUri = Uri.fromFile(sfzfmImage);
            scsfzImageUri = Uri.fromFile(scsfzImage);
            hyImageUri = Uri.fromFile(hyImage);
        } else {
            sfzzmImageUri = FileProvider.getUriForFile(PhotoActivity.this, "com.rx.www.netsignnew", sfzzmImage);
            sfzfmImageUri = FileProvider.getUriForFile(PhotoActivity.this, "com.rx.www.netsignnew", sfzfmImage);
            scsfzImageUri = FileProvider.getUriForFile(PhotoActivity.this, "com.rx.www.netsignnew", scsfzImage);
            hyImageUri = FileProvider.getUriForFile(PhotoActivity.this, "com.rx.www.netsignnew", hyImage);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        switch (requestCode) {
            case TAKE_PHOTO_SFZZM:
                if (resultCode == RESULT_OK) {

                    try {
                        //                        BitmapFactory.Options options = new BitmapFactory.Options();
                        //                        options.inSampleSize=8;
                        //                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        //                        options.inPurgeable = true;
                        //                        options.inInputShareable = true;
                        Bitmap sfzzm = BitmapFactory.decodeStream(getContentResolver().openInputStream(sfzzmImageUri), null, options);
                        //横向测试
                        //                        Matrix matrix = new Matrix();
                        //                        matrix.setRotate(-90);
                        //                        sfzzm = Bitmap.createBitmap(sfzzm, 0, 0, sfzzm.getWidth(), sfzzm.getHeight(), matrix, true);
                        //                        ivSfzzm.setImageBitmap(sfzzm);

                        File filePh = FileUtils.saveBitmap(sfzzm, "sfzzm_image");
                        File fzm = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(filePh.getPath(), 1280, 720), "ys_sfzzm_image" + System.currentTimeMillis());
                        Glide.with(this).load(fzm).into(ivSfzzm);

                        registeInfo.setSfzzm(fzm);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case TAKE_PHOTO_SFZFM:
                if (resultCode == RESULT_OK) {
                    try {


                        Bitmap sfzfm = BitmapFactory.decodeStream(getContentResolver().openInputStream(sfzfmImageUri), null, options);
                        //横向测试
                        //                        Matrix matrix = new Matrix();
                        //                        matrix.setRotate(-90);
                        //                        sfzfm = Bitmap.createBitmap(sfzfm, 0, 0, sfzfm.getWidth(), sfzfm.getHeight(), matrix, true);
                        //                        ivSfzfm.setImageBitmap(sfzfm);
                        File filePh = FileUtils.saveBitmap(sfzfm, "sfzfm_image");
                        File ffm = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(filePh.getPath(), 1280, 720), "ys_sfzfm_image" + System.currentTimeMillis());
                        Glide.with(this).load(ffm).into(ivSfzfm);

                        registeInfo.setSfzfm(ffm);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
                break;
            case TAKE_PHOTO_SCSFZ:
                if (resultCode == RESULT_OK) {


                    try {
                        Bitmap scsfz = BitmapFactory.decodeStream(getContentResolver().openInputStream(scsfzImageUri), null, options);
                        //                        ivScsfz.setImageBitmap(scsfz);
                        File filePh = FileUtils.saveBitmap(scsfz, "scsfz_image");
                        File fsc = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(filePh.getPath(), 1280, 720), "ys_scsfz_image" + System.currentTimeMillis());
                        Glide.with(this).load(fsc).into(ivScsfz);

                        registeInfo.setScsfz(fsc);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case TAKE_PHOTO_HY:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap hy = BitmapFactory.decodeStream(getContentResolver().openInputStream(hyImageUri), null, options);
                        //                        ivHy.setImageBitmap(hy);
                        File filePh = FileUtils.saveBitmap(hy, "hy_image");
                        File fhy = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(filePh.getPath(), 1280, 720), "ys_hy_image" + System.currentTimeMillis());
                        Glide.with(this).load(fhy).into(ivHy);

                        registeInfo.setHy(fhy);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
                break;


            case CHOOSE_PHOTO_SFZZM:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data, CHOOSE_PHOTO_SFZZM);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data, CHOOSE_PHOTO_SFZZM);
                    }
                }
                break;
            case CHOOSE_PHOTO_SFZFM:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data, CHOOSE_PHOTO_SFZFM);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data, CHOOSE_PHOTO_SFZFM);
                    }
                }
                break;
            case CHOOSE_PHOTO_SCSFZ:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data, CHOOSE_PHOTO_SCSFZ);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data, CHOOSE_PHOTO_SCSFZ);
                    }
                }
                break;
            case CHOOSE_PHOTO_HY:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data, CHOOSE_PHOTO_HY);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data, CHOOSE_PHOTO_HY);
                    }
                }
                break;
        }
    }


    /**
     * 信息提示框
     *
     * @param view
     */
    private AlertDialog alertDialog1;

    public void showListAlertDialog(View view, final int take, final int choose) {
        final String[] items = {"拍摄", "相册"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("上传照片");
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                switch (index) {
                    case 0:

                        switch (take) {
                            case 1:
                                // 启动相机程序
                                Intent intentzm = new Intent("android.media.action.IMAGE_CAPTURE");
                                intentzm.putExtra(MediaStore.EXTRA_OUTPUT, sfzzmImageUri);
                                startActivityForResult(intentzm, TAKE_PHOTO_SFZZM);
                                break;
                            case 2:
                                Intent intentfm = new Intent("android.media.action.IMAGE_CAPTURE");
                                intentfm.putExtra(MediaStore.EXTRA_OUTPUT, sfzfmImageUri);
                                startActivityForResult(intentfm, TAKE_PHOTO_SFZFM);
                                break;
                            case 3:
                                Intent intentsc = new Intent("android.media.action.IMAGE_CAPTURE");
                                intentsc.putExtra(MediaStore.EXTRA_OUTPUT, scsfzImageUri);
                                startActivityForResult(intentsc, TAKE_PHOTO_SCSFZ);
                                break;
                            case 4:
                                Intent intenthy = new Intent("android.media.action.IMAGE_CAPTURE");
                                intenthy.putExtra(MediaStore.EXTRA_OUTPUT, hyImageUri);
                                startActivityForResult(intenthy, TAKE_PHOTO_HY);
                                break;
                        }

                        break;
                    case 1:
                        if (ContextCompat.checkSelfPermission(PhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            switch (choose) {
                                case 11:
                                    openAlbum(CHOOSE_PHOTO_SFZZM);
                                    break;
                                case 22:
                                    openAlbum(CHOOSE_PHOTO_SFZFM);
                                    break;
                                case 33:
                                    openAlbum(CHOOSE_PHOTO_SCSFZ);
                                    break;
                                case 44:
                                    openAlbum(CHOOSE_PHOTO_HY);
                                    break;
                            }

                        }
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = alertBuilder.create();
        alertDialog1.show();
    }


    private void openAlbum(int choose) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, choose); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //                    openAlbum(CHOOSE_PHOTO_SFZZM);
                    Toast.makeText(this, "访问权限获取成功，请重试打开相册", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "您无访问权限，原因可能是您拒绝了本软件获取访问相册的权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data, int num) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        try {
            displayImage(imagePath, num);
        } catch (Exception e) {
            e.printStackTrace();
        } // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data, int num) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        try {
            displayImage(imagePath, num);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath, int num) {

        if (imagePath != null) {


            //            File filePh = FileUtils.saveBitmap(bitmap, "xcpz_image");
            //压缩后的图片
            if (num == 11) {
                Bitmap bm = BitmapFactory.decodeFile(imagePath);
                //                ivSfzzm.setImageBitmap(bm);
                File fYsZm = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(imagePath, 1280, 720), "ys_zm_image" + System.currentTimeMillis());
                Glide.with(this).load(fYsZm).into(ivSfzzm);
                registeInfo.setSfzzm(fYsZm);
            } else if (num == 22) {
                Bitmap bm = BitmapFactory.decodeFile(imagePath);
                //                ivSfzfm.setImageBitmap(bm);
                File fYsFm = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(imagePath, 1280, 720), "ys_fm_image" + System.currentTimeMillis());
                Glide.with(this).load(fYsFm).into(ivSfzfm);

                registeInfo.setSfzfm(fYsFm);
                Log.w("zcq", "图片路径: " + fYsFm.getAbsolutePath());

            } else if (num == 33) {
                Bitmap bm = BitmapFactory.decodeFile(imagePath);
                //                ivScsfz.setImageBitmap(bm);
                File fYsSc = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(imagePath, 1280, 720), "ys_sc_image" + System.currentTimeMillis());
                Glide.with(this).load(fYsSc).into(ivScsfz);
                registeInfo.setScsfz(fYsSc);

            } else if (num == 44) {
                Bitmap bm = BitmapFactory.decodeFile(imagePath);
                //                ivHy.setImageBitmap(bm);
                File fYsHy = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(imagePath, 1280, 720), "ys_hy_image" + System.currentTimeMillis());
                Glide.with(this).load(fYsHy).into(ivHy);

                registeInfo.setHy(fYsHy);

            }


        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyStoragePermissions(Activity activity) {
        int permissionWrite = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSION_EXTERNAL_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


}
