package com.ixiangni.app.user;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.interfaces.OnSelectListener;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.upload.UpLoadPresenter;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.bean.DepartmentBean;
import com.ixiangni.bean.JobListBean;
import com.ixiangni.bean.UserInfoBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.Departmentdialog;
import com.ixiangni.dialog.Genderdialog;
import com.ixiangni.dialog.Icondialog;
import com.ixiangni.dialog.Jobdialog;
import com.ixiangni.interfaces.OnUserInfoChange;
import com.ixiangni.presenters.UserInfoPresenter;
import com.ixiangni.ui.SanjiLiandong;
import com.ixiangni.ui.TopBar;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人信息
 *
 * @ClassName:PersonalInfoActivity
 * @PackageName:com.ixiangni.app.user
 * @Create On 2017/6/19 0019   15:24
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/19 0019 handongkeji All rights reserved.
 */
public class PersonalInfoActivity extends BaseActivity implements
        Genderdialog.OnGenderChangeListener,//性别
        Icondialog.OnClickDialogListener,//头像
        Departmentdialog.OnDepartmentSelectListener,//部门
        OnSelectListener<JobListBean.DataBean>,//职位
        OnResult<UserInfoBean.DataBean> {

    @Bind(R.id.iv_user_icon)
    ImageView ivUserIcon;
    @Bind(R.id.rl_change_icon)
    RelativeLayout rlChangeIcon;
    @Bind(R.id.tv_gender)
    TextView tvGender;
    @Bind(R.id.tv_sign)
    TextView tvSign;
    @Bind(R.id.tv_area)
    TextView tvArea;
    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.tv_idcard_num)
    TextView tvIdcardNum;
    @Bind(R.id.tv_department)
    TextView tvDepartment;
    @Bind(R.id.tv_place)
    TextView tvPlace;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_icon)
    TextView tvIcon;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.top_bar)
    TopBar topBar;
    private Genderdialog genderDialog;
    private SanjiLiandong sanjiLiandong;
    private Icondialog icondialog;

    private final int REQUEST_CODE_SELECT = 10;

    private final int EDIT_SIGN = 1;
    private final int EDIT_PHONE_NUM = 2;
    private final int EDIT_ID_NUM = 3;
    private final int EDIT_ADDRESS = 4;
    private final int EDIT_NAME = 5;

    private final int EDIT_FAZHAN = 6;

    private Departmentdialog departmentdialog;
    private DepartmentBean.DataBean mDepartmentBean;
    private Jobdialog jobdialog;
    private JobListBean.DataBean mJobBean;
    private UserInfoPresenter userInfoPresenter;
    private HashMap<String, String> userParams = new HashMap<>();
    private String userpic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);


        initView();

        ImagePicker.getInstance().setMultiMode(false);


        userInfoPresenter = new UserInfoPresenter();

        showProgressDialog("加载中...", true);
        userInfoPresenter.getUserInfo(this, this);

        topBar.setOnRightClickListener(v -> saveUserInfo());
    }

    private void initView() {
        genderDialog = new Genderdialog(this);
        genderDialog.setListener(this);

        //地区
        sanjiLiandong = new SanjiLiandong(this);
        sanjiLiandong.initNetData(UrlString.URL_AREA);
        sanjiLiandong.setOnCitySelect(new SanjiLiandong.OnCitySelect() {
            @Override
            public void oncityselect(String province, String city, String areanem) {
                String address = null;
                areanem = CommonUtils.isStringNull(areanem) ? "" : areanem;
                if (province.equals(city)) {
                    address = city + areanem;
                } else {
                    address = province + city + areanem;
                }
                userParams.put("areainfo", address);
                tvArea.setText(address);
            }
        });
        sanjiLiandong.setOnSelectArea(new SanjiLiandong.onSelectArea() {
            @Override
            public void onSelectids(int provinceid, int cityid, int areaid) {
//                provinceid
//                        cityid
//                districtid
                userParams.put("provinceid", "" + provinceid);
                userParams.put("cityid", "" + cityid);
                if (areaid != -1) {
                    userParams.put("districtid", "" + areaid);
                }


            }
        });
        //头像
        icondialog = new Icondialog(this);
        icondialog.setClickListener(this);

        //选择部门
        departmentdialog = new Departmentdialog(this);

        departmentdialog.initData();

        departmentdialog.setDepartmentSelectListener(this);
        //职位选择
        jobdialog = new Jobdialog(this);
        jobdialog.setOnJobSelectListener(this);

    }

    @OnClick({ R.id.rl_change_icon, R.id.tv_gender, R.id.tv_sign, R.id.tv_area, R.id.tv_phone_num, R.id.tv_idcard_num, R.id.tv_department, R.id.tv_place, R.id.tv_address})
    public void onViewClicked(View view) {

        String content;
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_change_icon:
                icondialog.show();
                break;
            case R.id.tv_gender://性别
                genderDialog.show();
                break;
            case R.id.tv_sign://个性签名
                content = tvSign.getText().toString().trim();
                intent = new Intent(PersonalInfoActivity.this, EditAddressActivity.class);
                intent.putExtra("content", content);
                intent.putExtra("title", "个性签名");
                startActivityForResult(intent, EDIT_SIGN);
                break;
//            case R.id.tv_user_name:
//                content = tvUserName.getText().toString().trim();
//                intent = new Intent(PersonalInfoActivity.this, EditAddressActivity.class);
//                intent.putExtra("content", content);
//                intent.putExtra("title", "姓名");
//                startActivityForResult(intent, EDIT_NAME);
//                break;
            case R.id.tv_area:
                sanjiLiandong.show();
                break;
            case R.id.tv_phone_num://编辑手机号
//                content = tvPhoneNum.getText().toString().trim();
//                intent = new Intent(PersonalInfoActivity.this, EditAddressActivity.class);
//                intent.putExtra("content", content);
//                intent.putExtra("title", "手机号");
//                startActivityForResult(intent, EDIT_PHONE_NUM);
                intent = new Intent();
                intent.setComponent(new ComponentName(this, ChangePhoneActivity.class));
                intent.putExtra("phoneNum", tvPhoneNum.getText().toString().trim());
                startActivityForResult(intent, EDIT_PHONE_NUM);

                break;
            case R.id.tv_idcard_num:
//                content = tvIdcardNum.getText().toString().trim();
//                intent = new Intent(PersonalInfoActivity.this, EditAddressActivity.class);
//                intent.putExtra("content", content);
//                intent.putExtra("title", "身份证号");
//                startActivityForResult(intent, EDIT_ID_NUM);
                break;
            case R.id.tv_department:
//                departmentdialog.show();
                break;
//            case R.id.tv_place:
//                if (mDepartmentBean == null) {
//                    toast("请先选择部门！");
//                    return;
//                }
//                jobdialog.show();
//
//                break;


            case R.id.tv_address:
                String content1 = tvAddress.getText().toString().trim();
                Intent intent1 = new Intent(PersonalInfoActivity.this, EditAddressActivity.class);
                intent1.putExtra("content", content1);
                intent1.putExtra("title", "我的地址");
                startActivityForResult(intent1, EDIT_ADDRESS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_ADDRESS && resultCode == RESULT_OK) {//我的地址
            String content = data.getStringExtra("content");
            tvAddress.setText(content);
            //保存提交地址参数
            userParams.put("address", content);


        } else if (requestCode == EDIT_SIGN && resultCode == RESULT_OK) {//个性签名
            String content = data.getStringExtra("content");
            tvSign.setText(content);
            //保存个性签名参数
            userParams.put("charactersing", content);

        } else if (requestCode == EDIT_PHONE_NUM && resultCode == RESULT_OK) {//手机号
            String content = data.getStringExtra("phoneNum");
            tvPhoneNum.setText(content);
        } else if (requestCode == EDIT_ID_NUM && resultCode == RESULT_OK) {//身份证号
            String content = data.getStringExtra("content");
            tvIdcardNum.setText(content);
        } else if (requestCode == EDIT_NAME && resultCode == RESULT_OK) {//姓名
            String content = data.getStringExtra("content");
            tvUserName.setText(content);
            userParams.put("userNick", content);

        }
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (!ListUtil.isEmptyList(images)) {
                    String path = images.get(0).path;
                    Log.i(TAG, "onActivityResult: " + path);
                    loadRoundImage(path, ivUserIcon, R.mipmap.touxiangmoren);
                    File file = new File(path);
                    log("filesize" + file.length());

                    upLoadImage(file);

                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void upLoadImage(File file) {

        UpLoadPresenter.upLoadImage(UrlString.URL_UPLOAD_IMAGE, file, "1", new OnResult<String>() {
            @Override
            public void onSuccess(String s) {

                if (mContent != null) {

                    log("头像" + s);
                    toast("上传成功");
                    if (!s.contains("http://")) {
                        s = "http://www." + s;
                    }
                    userParams.put("userPic", s);
                }
            }

            @Override
            public void onFailed(String errorMsg) {

                toast(errorMsg);
            }
        });
    }

    private static final String TAG = "PersonalInfoActivity";

    @Override
    public void onGender(String gender, int gn) {
        userParams.put("userSex", gn + "");
        tvGender.setText(gender);
    }


    //本地上传头像
    @Override
    public void onLocalClick() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, false); // 是否是直接打开相机
        startActivityForResult(intent, REQUEST_CODE_SELECT);
    }

    //拍照上传头像
    @Override
    public void onTakepictureClick() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        startActivityForResult(intent, REQUEST_CODE_SELECT);
    }
    //查看头像大图
    @Override
    public void onLookpictureClick() {
        Intent intent = new Intent(this, BigTouxiangActivity.class);
        intent.putExtra("touxiang",userpic);
        startActivity(intent);

    }


    @Override
    public void onGetDepartment(DepartmentBean.DataBean department) {
        this.mDepartmentBean = department;
        userParams.put("branchid", department.getBranchid() + "");

        tvDepartment.setText(department.getBranchname());
        jobdialog.initJob(department.getBranchid()+"");
    }

    @Override
    public void onSelected(JobListBean.DataBean dataBean) {
        this.mJobBean = dataBean;
        userParams.put("jobid", dataBean.getJobid() + "");

        tvPlace.setText(dataBean.getJobname());
    }

    @Override
    public void onSuccess(UserInfoBean.DataBean dataBean) {
        dismissProgressDialog();
        //头像
        userpic = dataBean.getUserpic();
        Log.w("zcq", "userpic: " + userpic);
        loadRoundImage(userpic, ivUserIcon, R.mipmap.touxiangmoren);
        //昵称
        String usernick = dataBean.getUsernick();
        tvUserName.setText(CommonUtils.isStringNull(usernick) ? "" : usernick);

        //性别
        tvGender.setText(dataBean.getUsersex() == 1 ? "男" : "女");
        //个性签名
        String charactersing = dataBean.getCharactersing();
        tvSign.setText(CommonUtils.isStringNull(charactersing) ? "" : charactersing);
        //地区
        String areainfo = dataBean.getAreainfo();
        tvArea.setText(CommonUtils.isStringNull(areainfo) ? "" : areainfo);

        //手机号
        String usermobile = dataBean.getUsermobile();
        tvPhoneNum.setText(CommonUtils.isStringNull(usermobile) ? "" : usermobile);

        //身份证号
        String usernumber = dataBean.getUsernumber();
        tvIdcardNum.setText(CommonUtils.isStringNull(usernumber) ? "" : usernumber);
        //部门名称
        String departmentName = dataBean.getTaobaoid();
        tvDepartment.setText(CommonUtils.isStringNull(departmentName) ? "" : departmentName);

        //职位名称
        String jobName = dataBean.getTaobaotoken();
        tvPlace.setText(CommonUtils.isStringNull(jobName) ? "" : jobName);


        String branchid = dataBean.getBranchid();

        if(!CommonUtils.isStringNull(branchid)){
            jobdialog.initJob(branchid);

            mDepartmentBean = new DepartmentBean.DataBean();
            mDepartmentBean.setBranchid(TextUtils.isDigitsOnly(branchid)?Integer.parseInt(branchid):0);
        }




        //我的地址
        String address = dataBean.getAddress();
        tvAddress.setText(CommonUtils.isStringNull(address) ? "" : address);

    }

    private void saveUserInfo() {

        showProgressDialog("保存中...", false);
        userParams.put("token", LoginHelper.getInstance().getToken(this));

        log(userParams.toString());
        RemoteDataHandler.asyncPost(UrlString.URL_SAVE_USER_INFO, userParams, this, false, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData response) {
                if (mContext == null) {
                    return;
                }
                dismissProgressDialog();
                String json = response.getJson();
                if (CommonUtils.isStringNull(json)) {
                    toast("保存失败...");
                } else {
                    try {
                        JSONObject object = new JSONObject(json);
                        if (1 == object.getInt("status")) {
                            toast("保存成功！");
                            SuperObservableManager.
                                    getInstance().
                                    getObservable(OnUserInfoChange.class).
                                    notifyMethod(OnUserInfoChange::change);
                            finish();
                        } else {
                            toast("保存失败...");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public void onFailed(String errorMsg) {
        dismissProgressDialog();
        toast(errorMsg);
    }
}
