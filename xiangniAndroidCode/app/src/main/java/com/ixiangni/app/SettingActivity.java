package com.ixiangni.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.handongkeji.autoupdata.CheckVersion;
import com.handongkeji.common.Constants;
import com.handongkeji.interfaces.DeleteFileCallback;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.XFileUtil;
import com.hyphenate.chatuidemo.DemoHelper;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.contactlist.BlackListActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.app.setting.FeedBackActivity;
import com.ixiangni.app.setting.HelpCenterActivity;
import com.ixiangni.app.setting.PrivacyActivity;
import com.ixiangni.app.user.ModifyPasswordActivity;
import com.ixiangni.common.XNConstants;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.presenters.GetTextPresenter;
import com.ixiangni.presenters.ModifySettingPresenter;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.switch_message)
    CheckBox switchMessage;
    @Bind(R.id.tv_change_password)
    TextView tvChangePassword;
    @Bind(R.id.tv_back_list)
    TextView tvBackList;
    @Bind(R.id.tv_secret)
    TextView tvSecret;
    @Bind(R.id.tv_help_center)
    TextView tvHelpCenter;
    @Bind(R.id.tv_about_us)
    TextView tvAboutUs;
    @Bind(R.id.tv_contact_us)
    TextView tvContactUs;
    @Bind(R.id.tv_suggest_feedback)
    TextView tvSuggestFeedback;
    @Bind(R.id.tv_clear_cache)
    TextView tvClearCache;
    @Bind(R.id.tv_check_update)
    TextView tvCheckUpdate;
    @Bind(R.id.btn_logout)
    Button btnLogout;
    @Bind(R.id.tv_cache_size)
    TextView tvCacheSize;

    private MyAlertdialog alertdialog;
    private ModifySettingPresenter settingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        if (!LoginHelper.getInstance().isLogin(mContext)) {
            btnLogout.setVisibility(View.GONE);
        } else {

            alertdialog = new MyAlertdialog(this);
            alertdialog
                    .setTitle("提示")
                    .setMessage("是否要退出登录？")
                    .setLeftText("取消")
                    .setRightText("确定")
                    .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                        @Override
                        public void onLeftClick(View view) {

                        }

                        @Override
                        public void onRightClick(View view) {
                            //退出登录
                            LoginHelper.getInstance().logout(mContext);
                            startActivity(LoginActivity.class);
                        }
                    });

        }
        //缓存大小
        calcuteCache();

        setMessageRemind();
    }

    private void calcuteCache() {
        tvCacheSize.setText(XFileUtil.formatFileSize(this, XFileUtil.getSizeOfDirectory(new File(Constants.CACHE_DIR))));
    }


    public static final String getSysMsg = "getSysMsg";

    /**
     * 设置消息提醒
     */
    private void setMessageRemind() {

        settingPresenter = new ModifySettingPresenter();

        //读取消息提醒状态
        SharedPreferences sharedPreferences = LoginHelper.getInstance().getSharedPreferences(this);
        boolean aBoolean = sharedPreferences.getBoolean(getSysMsg, true);
        DemoHelper.getInstance().getModel().setSettingMsgNotification(aBoolean);


        switchMessage.setChecked(aBoolean);

        switchMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String getMessage = switchMessage.isChecked() ? "0" : "1";
                saveGetMessageStatus(getMessage);
            }
        });
    }

    /**
     * 保存接收消息状态
     *
     * @param getMessage
     */
    private void saveGetMessageStatus(String getMessage) {
        if (settingPresenter != null) {
            showProgressDialog("修改中...", false);
            settingPresenter.changeSetting(this, new OnResult<String>() {
                @Override
                public void onSuccess(String s) {
                    dismissProgressDialog();
                    toast(s);

                    /****************************0：消息提醒，1：不提醒******************************/
                    boolean b = "0".equals(getMessage);

                    LoginHelper.getInstance().getSharedPreferences(mContext).edit().putBoolean(getSysMsg, b).apply();
                    DemoHelper.getInstance().getModel().setSettingMsgNotification(b);

                }

                @Override
                public void onFailed(String errorMsg) {
                    dismissProgressDialog();
                    toast(errorMsg);

                }
            }, getMessage, null, null, null);
        }
    }

    @OnClick({R.id.tv_change_password, R.id.tv_back_list, R.id.tv_secret, R.id.tv_help_center, R.id.tv_about_us, R.id.tv_contact_us, R.id.tv_suggest_feedback, R.id.tv_clear_cache, R.id.tv_check_update, R.id.btn_logout})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.tv_change_password://修改密码
                startActivity(ModifyPasswordActivity.class);
                break;
            case R.id.tv_back_list:
                startActivity(BlackListActivity.class);
                break;
            case R.id.tv_secret://隐私
                startActivity(PrivacyActivity.class);
                break;
            case R.id.tv_help_center:
                startActivity(HelpCenterActivity.class);
                break;
            case R.id.tv_about_us://关于我们
//                startActivity(AboutUsActivity.class);
                intent = new Intent(this, HelpCenterActivity.class);
                intent.putExtra("title", "关于我们");
                intent.putExtra(XNConstants.textflag, GetTextPresenter.TEXT_ABOUT_US);
                startActivity(intent);
                break;
            case R.id.tv_contact_us://联系我们
                intent = new Intent(this, HelpCenterActivity.class);
                intent.putExtra("title", "联系我们");
                intent.putExtra(XNConstants.textflag, GetTextPresenter.TEXT_CONTACT_US);
                startActivity(intent);
//                startActivity(ContactActivity.class);
                break;
            case R.id.tv_suggest_feedback://意见反馈
                startActivity(FeedBackActivity.class);
                break;
            case R.id.tv_clear_cache:
                if(XFileUtil.getSizeOfDirectory(new File(Constants.CACHE_DIR))<=0){
                    toast("已经清理过了！");
                    return;
                }
                final MyAlertdialog ad = new MyAlertdialog(this);
                ad.setSingLineMsg();
                ad.setDismissWhenClick(false);
                ad.setMessage("确定清除本地缓存？");
                ad.setCancelable(false);
                ad.setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                    @Override
                    public void onLeftClick(View view) {

                        ad.dismiss();
                    }

                    @Override
                    public void onRightClick(View view) {

                        XFileUtil.delFilsInDir(new File(Constants.CACHE_DIR), new DeleteFileCallback() {
                            @Override
                            public void delete(String filePath) {
                                ad.setMessage(filePath);
                            }

                            @Override
                            public void deleteFinish() {

                                if (tvCacheSize != null) {
                                    ad.dismiss();
                                    toast("清除完成！");
                                    tvCacheSize.setText("0");

                                }
                            }
                        });

                    }
                });

                ad.show();
                break;
            case R.id.tv_check_update:
                CheckVersion.update(this, true);
                break;
            case R.id.btn_logout:
                if (alertdialog != null) {
                    alertdialog.show();
                }
                break;
        }
    }
}
