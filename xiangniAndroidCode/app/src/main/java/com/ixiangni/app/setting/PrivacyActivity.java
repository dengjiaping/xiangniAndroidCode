package com.ixiangni.app.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.bean.UserInfoBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.presenters.ModifySettingPresenter;
import com.ixiangni.presenters.UserInfoPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName:PrivacyActivity
 * @PackageName:com.ixiangni.app.setting
 * @Create On 2017/6/20 0020   14:10
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/20 0020 handongkeji All rights reserved.
 */
public class PrivacyActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener,OnResult<String>{

    @Bind(R.id.cb_remind_circle)
    CheckBox cbRemindCircle;
    @Bind(R.id.cb_apply)
    CheckBox cbApply;
    @Bind(R.id.cb_auth)
    CheckBox cbAuth;
    private ModifySettingPresenter settingPresenter;
    private SharedPreferences sharedPreferences;
    private UserInfoPresenter userInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        ButterKnife.bind(this);

        setListeners();
    }

    private final String circle_remind = "circle_remind";
    public  final String apply_permission = "apply_permission";
    public  final String apply_auth = "apply_auth";

    public  final String has_cache_privacy = "has_cache_privacy";
    private void setListeners() {

        sharedPreferences = LoginHelper.getInstance().getSharedPreferences(this);

        boolean aBoolean = sharedPreferences.getBoolean(has_cache_privacy, false);

        if(aBoolean){
            cbRemindCircle.setChecked(sharedPreferences.getBoolean(circle_remind, true));
            cbApply.setChecked(sharedPreferences.getBoolean(apply_permission,false));
            cbAuth.setChecked(sharedPreferences.getBoolean(apply_auth,false));

            cbRemindCircle.setOnCheckedChangeListener(this);
            cbAuth.setOnCheckedChangeListener(this);
            cbApply.setOnCheckedChangeListener(this);
        }else {
            userInfoPresenter = new UserInfoPresenter();
            loadSetting();
        }


        settingPresenter = new ModifySettingPresenter();
    }

    private void loadSetting() {

        showProgressDialog("加载中...",true);
        userInfoPresenter.getUserInfo(this, new OnResult<UserInfoBean.DataBean>() {
            @Override
            public void onSuccess(UserInfoBean.DataBean dataBean) {
                if(cbApply!=null){
                    dismissProgressDialog();

                   /* remind	否	Integer	想你圈更新提醒: 0 提醒，1 不提醒
                    applyAdd	否	Integer	允许加好友: 0 允许，1 不允许
                    authentication	否	Integer	加好友验证: 0 需要验证，1 需要不验证*/

                    int remind = dataBean.getRemind();
                    int authentication = dataBean.getAuthentication();
                    int applyadd = dataBean.getApplyadd();
                    boolean isRemind = remind==0;
                    boolean isApplyAdd = applyadd==0;
                    boolean isAuthentication = authentication==0;
                    //保存设置
                    sharedPreferences.edit().putBoolean(has_cache_privacy,true).apply();
                    sharedPreferences.edit().putBoolean(apply_permission,isApplyAdd).apply();
                    sharedPreferences.edit().putBoolean(circle_remind,isRemind).apply();
                    sharedPreferences.edit().putBoolean(apply_auth,isAuthentication).apply();

                    cbApply.setChecked(isApplyAdd);
                    cbRemindCircle.setChecked(isRemind);
                    cbAuth.setChecked(isAuthentication);

                    cbRemindCircle.setOnCheckedChangeListener(PrivacyActivity.this);
                    cbAuth.setOnCheckedChangeListener(PrivacyActivity.this);
                    cbApply.setOnCheckedChangeListener(PrivacyActivity.this);

                }

            }

            @Override
            public void onFailed(String errorMsg) {
                if(cbApply!=null){
                    dismissProgressDialog();
                    toast(errorMsg);

                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        String remind = null;
        String applyAdd = null;
        String authenticaion = null;

        switch (buttonView.getId()){
            case R.id.cb_remind_circle:
                remind = cbRemindCircle.isChecked()?"0":"1";

                if(cbRemindCircle.isChecked()){
                    //开始想你圈更新提醒
                    sendBroadcast(new Intent(XNConstants.restart_circle_remind));
                    log("开始提醒");
                }else {
                    //停止想你圈更新提醒
                    sendBroadcast(new Intent(XNConstants.stop_circle_remind));
                    log("停止");

                }


                break;
            case R.id.cb_apply:
                applyAdd = cbApply.isChecked()?"0":"1";
                break;
            case R.id.cb_auth:
                authenticaion = cbAuth.isChecked()?"0":"1";
                break;

        }
        showProgressDialog("设置中...",false);
        settingPresenter.changeSetting(this,this,null,remind,applyAdd,authenticaion);
    }

    @Override
    public void onSuccess(String s) {
        dismissProgressDialog();
        toast("设置成功");
        sharedPreferences.edit()
                .putBoolean(apply_auth,cbAuth.isChecked())
                .putBoolean(apply_permission,cbApply.isChecked())
                .putBoolean(circle_remind,cbRemindCircle.isChecked())
                .apply();

    }

    @Override
    public void onFailed(String errorMsg) {

        toast(errorMsg);
    }
}
