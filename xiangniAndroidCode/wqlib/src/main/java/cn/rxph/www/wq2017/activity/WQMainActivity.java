package cn.rxph.www.wq2017.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.concurrent.ExecutionException;

import cn.rxph.www.wq2017.R;
import cn.rxph.www.wq2017.base.BaseActivity;
import cn.rxph.www.wq2017.registActivity.RegistWordActivity;


public class WQMainActivity extends BaseActivity {
    private RollPagerView mRollViewPager;
    private Button wyqy;
    private Button wdqy;
    private Button cjwt;
    private Button yjfk;
    private ImageView back;
    private String userId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wqmain);
        Intent intent = getIntent();

        userId = intent.getStringExtra("userId");
        token = intent.getStringExtra("token");
        wyqy = (Button) findViewById(R.id.wyqy);
        wdqy = (Button) findViewById(R.id.wdqy);
        cjwt = (Button) findViewById(R.id.cjwt);
        yjfk = (Button) findViewById(R.id.yjfk);
        back = (ImageView) findViewById(R.id.iv_back);
        mRollViewPager = (RollPagerView) findViewById(R.id.roll_view_pager_lunbo);


        //设置播放时间间隔
        mRollViewPager.setPlayDelay(3000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(2000);
        //设置适配器
        mRollViewPager.setAdapter(new TestNormalAdapter());

        //设置指示器（顺序依次）
        //自定义指示器图片
        //设置圆点指示器颜色
        //设置文字指示器
        //隐藏指示器
        //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
        mRollViewPager.setHintView(new ColorPointHintView(this, Color.BLUE, Color.WHITE));
        //mRollViewPager.setHintView(new TextHintView(this));
        //mRollViewPager.setHintView(null);


        wyqy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WQMainActivity.this, RegistWordActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
        wdqy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WQMainActivity.this, MyNetSignActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        cjwt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WQMainActivity.this, QuestionActivity.class));
            }
        });
        yjfk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WQMainActivity.this, FeedbackActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WQMainActivity.this.finish();

            }
        });


    }

    //通过url获取轮播图片
    private Bitmap requestBitmap(String binnerUrl) {
        Bitmap myBitmap = null;
        try {
            myBitmap = Glide.with(getApplicationContext())
                    .load(binnerUrl)
                    .asBitmap() //必须
                    .centerCrop()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return myBitmap;
    }


    private class TestNormalAdapter extends StaticPagerAdapter {

        private int[] imgs = {
                R.drawable.aaa,
                R.drawable.bbb,
//                R.drawable.ccc,
        };

//        private Bitmap[] bitmaps = {
//                requestBitmap(""),
//                requestBitmap(""),
//        };


        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
//            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }


        @Override
        public int getCount() {
            return imgs.length;
        }
    }



}
