package com.hyphenate.chatuidemo.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.handongkeji.impactlib.util.ToastUtils;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsResultAction;
import com.hyphenate.easeui.EaseConstant;
import com.ixiangni.app.R;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsManager;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;
import com.ixiangni.common.XNConstants;
import com.ixiangni.interfaces.OnGroupDelete;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.util.ArrayList;
import java.util.List;

/**
 * chat activity，EaseChatFragment was
 */
public class ChatActivity extends BaseActivity implements OnGroupDelete {
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;
    private int chatType;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        activityInstance = this;
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString("userId");
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        chatType = getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, -1);

        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

        SuperObservableManager.getInstance().getObservable(OnGroupDelete.class).registerObserver(this);


        //语音视频权限检查

        List<String> permissions = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.RECORD_AUDIO);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (permissions.size() > 0) {

            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this, permissions.toArray(new String[permissions.size()]),
                    new PermissionsResultAction() {
                        @Override
                        public void onGranted() {

                        }

                        @Override
                        public void onDenied(String permission) {

                            ToastUtils.show(ChatActivity.this,"您拒绝了一些权限，\n可能导致某些功能无法使用.");
                        }
                    });
        }


    }


    public static void startChat(Context context, String userId, int chatType) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, chatType);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    /**
     * 开始群聊
     *
     * @param context
     * @param groupid
     * @param groupname
     */
    public static void startGroupChat(Context context, String groupid, String groupname) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
        intent.putExtra(EaseConstant.EXTRA_USER_ID, groupid);
        intent.putExtra(XNConstants.groupname, groupname);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SuperObservableManager.getInstance().getObservable(OnGroupDelete.class).unregisterObserver(this);
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
//        chatFragment.onBackPressed();
//        if (EasyUtils.isSingleActivity(this)) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }
        super.onBackPressed();
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    //群删除
    @Override
    public void onDelete() {

        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            finish();
        }

    }
}
