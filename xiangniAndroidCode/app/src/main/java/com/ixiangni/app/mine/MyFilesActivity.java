package com.ixiangni.app.mine;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback;
import android.support.v4.os.CancellationSignal;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handongkeji.impactlib.dialog.XDialog;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.FolderListBean;
import com.ixiangni.dialog.FingerDialog;
import com.ixiangni.dialog.Passwordialog;
import com.ixiangni.presenters.FolderListPresenter;
import com.ixiangni.util.CryptoObjectHelper;
import com.ixiangni.util.StateLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的文件
 *
 * @ClassName:MyFilesActivity
 * @PackageName:com.ixiangni.app.mine
 * @Create On 2017/6/21 0021   15:57
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class MyFilesActivity extends BaseActivity implements OnResult<List<FolderListBean.DataBean>> {

    @Bind(R.id.iv_add_file)
    ImageView ivAddFile;
    @Bind(R.id.rl_search)
    RelativeLayout rlSearch;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.edt_search)
    EditText edtSearch;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    private FolderListPresenter folderListPresenter;
    private FolderAdapter folderAdapter;
    private SuperObservableManager mSupserManager;


    /*****************************文件夹输入一次密码保存起来第二次不用输入******************************/
    private List<String> folders = new ArrayList<>();
    private FingerprintManagerCompat fingerprintManagerCompat;
    private String curFolderid;
    private FingerDialog dialog;
    private CancellationSignal cancel;

    public interface OnFolderCahnge {
        void onChange();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);
        ButterKnife.bind(this);
//        XDialog dialog = new XDialog(t)
        mSupserManager = SuperObservableManager.getInstance();
        mSupserManager.getObservable(OnFolderCahnge.class).registerObserver(onFolderCahnge);

        folderAdapter = new FolderAdapter(this);
        listView.setAdapter(folderAdapter);
        folderAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (folderAdapter.getCount() == 0) {
                    stateLayout.showNoDataView("暂无文件");
                } else {
                    stateLayout.showContenView();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FolderListBean.DataBean dataBean = folderAdapter.getItem(position);
                String folderpassword = dataBean.getFolderpassword();
                int delmark = dataBean.getDelmark();
                if (delmark == 0) {
                    String folderid = dataBean.getFolderid() + "";

                    if (folders.contains(folderid)) {
                        openFolder(folderid);
                    } else {
                        Passwordialog passwordialog = new Passwordialog(MyFilesActivity.this, folderpassword);
                        passwordialog.setmListener(new Passwordialog.OnPasswordResult() {
                            @Override
                            public void onCorrectPassword() {
                                folders.add(folderid);
                                openFolder(folderid);
                            }
                        });
                        passwordialog.show();

                    }

//                    inputMethodManager.toggleSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
//                            InputMethodManager.SHOW_FORCED,
//                            InputMethodManager.HIDE_IMPLICIT_ONLY);

                } else {
                    if(Build.VERSION.SDK_INT<23){
                        toast("指纹密码仅支持Android6.0以上手机...");
                        return;
                    }

                    if (fingerprintManagerCompat == null) {
                        fingerprintManagerCompat = FingerprintManagerCompat.from(MyFilesActivity.this);
                    }
                    if (!fingerprintManagerCompat.isHardwareDetected()) {
                        toast("该手机不支持指纹密码...");
                        return;
                    }
                    if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {
                        toast("您还未注册指纹密码！");
                        return;
                    }
                    dialog = new FingerDialog(MyFilesActivity.this);
                    dialog.setText("请验证指纹密码");
                    dialog.show();

                    try {
                        curFolderid = dataBean.getFolderid() + "";
                        CryptoObjectHelper helper = new CryptoObjectHelper();
                        cancel = new CancellationSignal();
                        fingerprintManagerCompat.authenticate(helper.buildCryptoObject(),0, cancel,authenticationCallback,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        folderListPresenter = new FolderListPresenter();

        getFolderList(null);
        ivSearch.setOnClickListener(v -> {
            hideSoftKeyboard();
            String text = edtSearch.getText().toString().trim();
            getFolderList(text);

        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ivSearch.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private AuthenticationCallback authenticationCallback = new AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            if(dialog!=null){
                dialog.setText("验证出错");
            }
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            if(dialog!=null){
                dialog.dismiss();
            }
            openFolder(curFolderid);

        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            if(dialog!=null){
                dialog.setText("验证失败");
            }
        }
    };

    private void openFolder(String folerid) {
        Intent intent = new Intent(MyFilesActivity.this, FileBrwseActivity.class);
        intent.putExtra("folderid", folerid);//folderid
        startActivity(intent);
    }

    private OnFolderCahnge onFolderCahnge = new OnFolderCahnge() {
        @Override
        public void onChange() {
            getFolderList(null);
        }
    };


    private void getFolderList(String o) {

        stateLayout.showLoadView("加载中...");
        folderListPresenter.getFolderList(this, o, this);
    }

    @Override
    public void onSuccess(List<FolderListBean.DataBean> dataBeanList) {
        List<FolderListBean.DataBean> mathcList = ListUtil.getMathcList(dataBeanList, new ListUtil.OnMatch<FolderListBean.DataBean>() {
            @Override
            public boolean match(FolderListBean.DataBean dataBean) {

                if (dataBean != null && dataBean.getIsshow() == 0) {
                    return true;
                }
                return false;
            }
        });

        folderAdapter.replaceAll(mathcList);
    }

    @Override
    public void onFailed(String errorMsg) {

    }

    private class FolderAdapter extends QuickAdapter<FolderListBean.DataBean> {

        private final SimpleDateFormat mFormat;

        public FolderAdapter(Context context) {
            super(context, R.layout.item_folder);
            mFormat = new SimpleDateFormat("yyyy-MM-dd");
        }

        @Override
        protected void convert(BaseAdapterHelper helper, FolderListBean.DataBean dataBean) {
            long createtime = dataBean.getCreatetime();
            String foldername = dataBean.getFoldername();
            String createTime = mFormat.format(new Date(createtime));
            helper.setText(R.id.tv_dir_name, foldername);
            helper.setText(R.id.tv_create_time, createTime);
        }
    }


    @OnClick({R.id.iv_add_file, R.id.rl_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_file:
                startActivity(CreateDirActivity.class);
                break;
            case R.id.rl_search:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        folders = null;
        SuperObservableManager instance = SuperObservableManager.getInstance();
        instance.getObservable(OnFolderCahnge.class).unregisterObserver(onFolderCahnge);

        instance.removeObservable(OnFolderCahnge.class);

        super.onDestroy();
    }
}
