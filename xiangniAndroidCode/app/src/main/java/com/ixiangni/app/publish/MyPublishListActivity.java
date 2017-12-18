package com.ixiangni.app.publish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.handongkeji.handler.RemoteDataHandler;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.constants.UrlString;

import java.util.HashMap;
/**
 * 我的发布列表
 * @ClassName:MyPublishListActivity

 * @PackageName:com.ixiangni.app.publish

 * @Create On 2017/6/26 0026   15:35

 * @Site:http://www.handongkeji.com

 * @author:xuchuanting

 * @Copyrights 2017/6/26 0026 handongkeji All rights reserved.
 */
public class MyPublishListActivity extends BaseActivity {

    private int currentPage = 1;
    private int pageSize =10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish_list);


        getPublishList();
    }


    private void getPublishList() {


        HashMap<String, String> params = new HashMap<>();
        params.put("token", LoginHelper.getInstance().getToken(this));
        params.put("currentPage",""+currentPage);
        params.put("pageSize",""+pageSize);
        RemoteDataHandler.asyncPost(UrlString.URL_MY_PUBLISH_LIST,params,this,false, callback->{
            String json = callback.getJson();

            log(json);

        });

    }
}
