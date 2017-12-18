package com.ixiangni.app.mine;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handongkeji.handler.RemoteDataHandler;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.SelectPwdTypedialog;
import com.ixiangni.interfaces.OnGroupDelete;
import com.ixiangni.ui.TopBar;
import com.nevermore.oceans.ob.Dispatcher;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建文件夹
 *
 * @ClassName:CreateDirActivity
 * @PackageName:com.ixiangni.app.mine
 * @Create On 2017/6/21 0021   15:28
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class CreateDirActivity extends BaseActivity
        implements SelectPwdTypedialog.OnPasswordTypeChangeListener {

    @Bind(R.id.edt_dri_name)
    EditText edtDriName;
    @Bind(R.id.tv_change_type)
    TextView tvChangeType;
    @Bind(R.id.edt_password)
    EditText edtPassword;
    @Bind(R.id.edt_password_again)
    EditText edtPasswordAgain;
    @Bind(R.id.cb_visibility)
    CheckBox cbVisibility;
    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.ll_pwd)
    LinearLayout llPwd;
    @Bind(R.id.ll_pwd_again)
    LinearLayout llPwdAgain;
    private SelectPwdTypedialog mDialog;
    private FingerprintManagerCompat fingerprintManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dir);
        ButterKnife.bind(this);

        mDialog = new SelectPwdTypedialog(this, this);

        topBar.setOnRightClickListener(saveListener);
    }

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String dirName = edtDriName.getText().toString().trim();

            if (TextUtils.isEmpty(dirName)) {
                toast("请输入文件夹名称！");
                return;
            }

            String passwordType = tvChangeType.getText().toString().trim();
            if (TextUtils.isEmpty(passwordType)) {
                toast("请选择密码类型！");
                return;
            }


            String password = edtPassword.getText().toString().trim();
            if ("普通密码".equals(passwordType)) {

                if (TextUtils.isEmpty(password)) {
                    toast("请输入密码！");
                    return;
                }

                String passwordAgain = edtPasswordAgain.getText().toString().trim();
                if (TextUtils.isEmpty(passwordAgain)) {
                    toast("请输入确认密码！");
                    return;
                }

                if (!password.equals(passwordAgain)) {
                    toast("两次密码不一致！");
                }

            }


            String visiable = cbVisibility.isChecked() ? "0" : "1";

            HashMap<String, String> params = new HashMap<>();
            params.put("token", LoginHelper.getInstance().getToken(CreateDirActivity.this));
            params.put("foldername", dirName);
            if ("普通密码".equals(passwordType)) {
                params.put("folderpassword", password);
            }
            params.put("isshow", visiable);
            params.put("delmark", "普通密码".equals(passwordType) ? "0" : "1");
            log(params.toString());

            RemoteDataHandler.asyncPost(UrlString.URL_ADD_DIRECTORY, params, CreateDirActivity.this, false, callBack -> {

                if (mContext != null) {
                    log(callBack.getJson());
                    SuperObservableManager.getInstance().getObservable(MyFilesActivity.OnFolderCahnge.class).
                            notifyMethod(new Dispatcher<MyFilesActivity.OnFolderCahnge>() {
                                @Override
                                public void call(MyFilesActivity.OnFolderCahnge onFolderCahnge) {
                                    onFolderCahnge.onChange();
                                }
                            });

                    SuperObservableManager.notify(OnGroupDelete.class,OnGroupDelete::onDelete);
//                    SuperObservableManager.getInstance().getObservable(OnGroupDelete.class).notifyMethod(OnGroupDelete::onDelete);
                    handJson(callBack.getJson(), "创建成功！");
                }
            });

        }
    };

    @OnClick(R.id.tv_change_type)
    public void onViewClicked() {
        mDialog.show();
    }

    @Override
    public void onTypeChange(String type) {
        log(type);
        if ("指纹密码".equals(type)) {
            if (Build.VERSION.SDK_INT < 23) {
                toast("指纹密码仅支持Android6.0以上手机...");
                return;
            }
            if (fingerprintManagerCompat == null) {
                fingerprintManagerCompat = FingerprintManagerCompat.from(this);
            }
            if (!fingerprintManagerCompat.isHardwareDetected()) {//是否支持指纹密码
                toast("该手机不支持指纹密码...");
            } else {
                //是否注册过指纹密码
                boolean b = fingerprintManagerCompat.hasEnrolledFingerprints();
                if (!b) {
                    toast("您还未注册指纹密码！");
                } else {

                    llPwd.setVisibility(View.GONE);
                    llPwdAgain.setVisibility(View.GONE);
                    tvChangeType.setText(type);
                }
            }

        } else {
            llPwd.setVisibility(View.VISIBLE);
            llPwdAgain.setVisibility(View.VISIBLE);
            tvChangeType.setText(type);
        }
    }
}
