package cn.rxph.www.wq2017.registActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.rxph.www.wq2017.R;
import cn.rxph.www.wq2017.adapter.MyAdapter;
import cn.rxph.www.wq2017.base.BaseActivity;
import cn.rxph.www.wq2017.bean.Company;
import cn.rxph.www.wq2017.bean.RegisteInfo;
import cn.rxph.www.wq2017.utils.HttpUtil;
import cn.rxph.www.wq2017.utils.UrlUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistWordActivity extends BaseActivity {

    private EditText etUserName, etUserId, etUserCompany, etUserHome, etUserTel, etRefereeId,
            etContactName, etContactTel, etShehuiguanxi, etContactName1, etContactTel1, etShehuiguanxi1;
    private TextView tvRefereeName, tvRefereeTel;

    private ImageView ivBack;
    private Button btnPrevious, btnNext;

    private RegisteInfo registeInfo;
    private RegisteInfo registInfoAgain;

    private ListView lvzllm;
    private List<Company> zllms = new ArrayList<>();
    private MyAdapter adapter;
    private String userId; //签约人的Id
    private String token = null;

    private String zllmId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_word);
        initView();
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        token = intent.getStringExtra("token");
        registeInfo = new RegisteInfo();
        registInfoAgain = (RegisteInfo) getIntent().getSerializableExtra("registeInfoAgain");


        lvzllm = (ListView) findViewById(R.id.lv_zllm);
        adapter = new MyAdapter(this, R.layout.zllm_lv_item, zllms);
        lvzllm.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lvzllm);
        //加监听
        setListener();

        if (registInfoAgain != null) {
            againWrite();
        }


    }


    private void initView() {

        etUserName = (EditText) findViewById(R.id.et_user_name);
        etUserId = (EditText) findViewById(R.id.et_user_id);
        etUserCompany = (EditText) findViewById(R.id.et_user_company);
        etUserHome = (EditText) findViewById(R.id.et_user_home);
        etUserTel = (EditText) findViewById(R.id.et_user_tel);
        etRefereeId = (EditText) findViewById(R.id.et_referee_id);
        tvRefereeName = (TextView) findViewById(R.id.tv_referee_name);
        tvRefereeTel = (TextView) findViewById(R.id.tv_referee_tel);
        etContactName = (EditText) findViewById(R.id.et_contact_name);
        etContactTel = (EditText) findViewById(R.id.et_contact_tel);
        etShehuiguanxi = (EditText) findViewById(R.id.et_shehuiguanxi);
        etContactName1 = (EditText) findViewById(R.id.et_contact_name1);
        etContactTel1 = (EditText) findViewById(R.id.et_contact_tel1);
        etShehuiguanxi1 = (EditText) findViewById(R.id.et_shehuiguanxi1);

        btnPrevious = (Button) findViewById(R.id.btn_previous);
        btnNext = (Button) findViewById(R.id.btn_next);
        ivBack = (ImageView) findViewById(R.id.iv_back);


    }

    private void againWrite() {
        zllmId = registInfoAgain.getZllmId();
        registeInfo.setApply_id(registInfoAgain.getApply_id());
        etUserName.setText(registInfoAgain.getUserName());
        etUserId.setText(registInfoAgain.getUserSfz());
        etUserCompany.setText(registInfoAgain.getZllm());

        etUserHome.setText(registInfoAgain.getUserAddress());
        etUserTel.setText(registInfoAgain.getUserTel());

        etRefereeId.setText(registInfoAgain.getTjrSfz());
        tvRefereeName.setText(registInfoAgain.getTjrName());
        tvRefereeTel.setText(registInfoAgain.getTjrTel());

        etContactName.setText(registInfoAgain.getContactName());
        etContactTel.setText(registInfoAgain.getContactTel());
        etShehuiguanxi.setText(registInfoAgain.getContactShgx());

        etContactName1.setText(registInfoAgain.getContactName1());
        etContactTel1.setText(registInfoAgain.getContactTel1());
        etShehuiguanxi1.setText(registInfoAgain.getContactShgx1());

    }

    private void setListener() {


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistWordActivity.this.finish();
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistWordActivity.this.finish();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userWriteInfo();
            }
        });


        lvzllm.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String zllm = zllms.get(position).getBranchName();
                zllmId = zllms.get(position).getBranchId();
                etUserCompany.setText(zllm);


                lvzllm.setVisibility(View.GONE);
            }
        });

        etRefereeId.addTextChangedListener(new TextWatcher() {
            public CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int ss = temp.length();
                if (temp.length() == 18) {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userNumber", temp.toString())
                            .build();

                    HttpUtil.sendOkHttpPostRequest(UrlUtil.URL_REFEREE_INFO, requestBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(RegistWordActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String body = response.body().string();


                            try {
                                final JSONObject jo = new JSONObject(body);
                                final int status = jo.getInt("status");


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (status == 0) {
                                            JSONObject data = null;
                                            Toast.makeText(RegistWordActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                                            try {
                                                data = jo.getJSONObject("data");
                                                String RefereeName = data.getString("userNick");
                                                String RefereeTel = data.getString("userMobile");
                                                tvRefereeName.setText(RefereeName);
                                                tvRefereeTel.setText(RefereeTel);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            return;
                                        }
                                        if (status == 1) {
                                            Toast.makeText(RegistWordActivity.this, "该推荐人账号不存在，请与总部联系。", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (status == -1) {
                                            Toast.makeText(RegistWordActivity.this, "必传参数不能为空", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (status == -2) {
                                            Toast.makeText(RegistWordActivity.this, "后台异常...", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });


        etUserCompany.addTextChangedListener(new TextWatcher() {
            public CharSequence gjz;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                gjz = s;

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lvzllm.setVisibility(View.VISIBLE);
                try {
                    queryCompany(gjz.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        });

    }


    //获取用户填写信息并写入对象
    private void userWriteInfo() {

        String uName = etUserName.getText().toString();
        String uId = etUserId.getText().toString();
        String uCompany = etUserCompany.getText().toString();
        String uHome = etUserHome.getText().toString();
        String uTel = etUserTel.getText().toString();

        String rId = etRefereeId.getText().toString();
        String rName = tvRefereeName.getText().toString();
        String rTel = tvRefereeTel.getText().toString();

        String cName = etContactName.getText().toString();
        String cTel = etContactTel.getText().toString();
        String shgx = etShehuiguanxi.getText().toString();

        String cName1 = etContactName1.getText().toString();
        String cTel1 = etContactTel1.getText().toString();
        String shgx1 = etShehuiguanxi1.getText().toString();

                if (!isLegalId(uId)) {
                    Toast.makeText(this, "签约人身份证填写有误，请检查第二项", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isTelPhoneNumber(uTel)) {
                    Toast.makeText(this, "签约人手机号填写有误，请检查第五项", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(uName) || TextUtils.isEmpty(uId) || TextUtils.isEmpty(uCompany)
                        || TextUtils.isEmpty(uHome) || TextUtils.isEmpty(uTel) || TextUtils.isEmpty(rId)
                        || TextUtils.isEmpty(rName) || TextUtils.isEmpty(rTel) || TextUtils.isEmpty(cName)
                        || TextUtils.isEmpty(cTel) || TextUtils.isEmpty(shgx)) {
                    Toast.makeText(this, "请完善必填信息！！", Toast.LENGTH_SHORT).show();
                } else {
        registeInfo.setQianyuerenId(userId);
        registeInfo.setQianyuerentoken(token);


        registeInfo.setUserName(uName);
        registeInfo.setUserSfz(uId);
        registeInfo.setZllm(uCompany);

        registeInfo.setZllmId(zllmId);


        registeInfo.setUserAddress(uHome);
        registeInfo.setUserTel(uTel);

        registeInfo.setTjrName(rName);
        registeInfo.setTjrSfz(rId);
        registeInfo.setTjrTel(rTel);

        registeInfo.setContactName(cName);
        registeInfo.setContactShgx(shgx);
        registeInfo.setContactTel(cTel);

        registeInfo.setContactName1(cName1);
        registeInfo.setContactShgx1(shgx1);
        registeInfo.setContactTel1(cTel1);

        //判断手机号是否存在
        final RequestBody requestBody = new FormBody.Builder()
                .add("userMobile", uTel)
                .build();
        HttpUtil.sendOkHttpPostRequest(UrlUtil.URL_QUERY_TEL_REPEAT, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(RegistWordActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //                    Log.w("zcq", "状态码: "+body);

                try {
                    JSONObject jo = new JSONObject(body);
                    final int status = jo.getInt("data");
                    //                        Log.w("zcq", "状态码: "+status);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status == 0) {
                                Toast.makeText(RegistWordActivity.this, "手机号已被占用，数据库里已存在,请检查第五项", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (status == 1) {

                                Intent intentInfo = new Intent(RegistWordActivity.this, PhotoActivity.class);
                                intentInfo.putExtra("registInfo", registeInfo);
                                intentInfo.putExtra("registeInfoAgain", registInfoAgain);
                                startActivity(intentInfo);

                                return;
                            }
                            if (status == -2) {
                                Toast.makeText(RegistWordActivity.this, "后台异常,错误码-2", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(RegistWordActivity.this, "未知错误", Toast.LENGTH_SHORT).show();

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


                }

    }

    private void queryCompany(String gjz) {

        RequestBody requestBody = new FormBody.Builder()
                .add("branchName", gjz)
                .build();

        HttpUtil.sendOkHttpPostRequest(UrlUtil.URL_QUERY_COMPANY, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(RegistWordActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                try {
                    final JSONObject jo = new JSONObject(body);
                    final int status = jo.getInt("status");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status == 0) {
                                String data = null;
                                try {
                                    data = jo.getString("data");
                                    parseJsonWithGson(data);
                                    adapter.notifyDataSetChanged();
                                    setListViewHeightBasedOnChildren(lvzllm);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                return;
                            }

                            if (status == 1) {
                                Toast.makeText(RegistWordActivity.this, "暂无此发展中心", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                                setListViewHeightBasedOnChildren(lvzllm);
                                return;
                            }
                            if (status == -1) {
                                Toast.makeText(RegistWordActivity.this, "必传参数不能为空", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (status == -2) {
                                Toast.makeText(RegistWordActivity.this, "后台异常...", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    private void parseJsonWithGson(String jsonData) {
        Gson gson = new Gson();
        List<Company> companyList;

        companyList = gson.fromJson(jsonData, new TypeToken<List<Company>>() {
        }.getType());
        zllms.clear();
        zllms.addAll(companyList);
    }

    /**
     * 根据条目设定listview高度
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    /**
     * 验证输入的身份证号是否合法
     */
    public boolean isLegalId(String id) {
        if (id.toUpperCase().matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)")) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 手机号号段校验，
     * 第1位：1；
     * 第2位：{3、4、5、6、7、8}任意数字；
     * 第3—11位：0—9任意数字
     *
     * @param value
     * @return
     */
    public boolean isTelPhoneNumber(String value) {
        if (value != null && value.length() == 11) {
            Pattern pattern = Pattern.compile("^1[3|4|5|6|7|8|9][0-9]\\d{8}$");
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        }
        return false;
    }

}
