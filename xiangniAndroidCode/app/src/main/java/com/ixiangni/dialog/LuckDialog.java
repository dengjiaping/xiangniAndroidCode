package com.ixiangni.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.widget.RoundImageView;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.money.RPRedPacketDetailActivity;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by John on 2016/11/27.
 */

public class LuckDialog extends Dialog {

    @Bind(R.id.root)
    FrameLayout root;
    @Bind(R.id.container)
    RelativeLayout container;
    @Bind(R.id.luck_detail)
    TextView luckDetail;
    @Bind(R.id.btn_open)
    ImageView btnOpen;
    @Bind(R.id.close)
    ImageView close;
    @Bind(R.id.head_img)
    RoundImageView headImg;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.type)
    TextView type;
    @Bind(R.id.message)
    TextView message;
    @Bind(R.id.background)
    ImageView background;
    private Window mWindow;

    private String redPacketId;

    private String nick;
    private String avatar;
    private String redPacketType;
    private String greeting;

    private Activity mActivity;

    public void setListener(View.OnClickListener listener) {
        mListener = listener;
    }

    private View.OnClickListener mListener;

    public LuckDialog(Context context, String redPacketId, String nick, String avatar, String redPacketType, String greeting) {
        super(context);
        this.redPacketId = redPacketId;
        this.nick = nick;
        this.avatar = avatar;
        this.redPacketType = redPacketType;
        this.greeting = greeting;
        mActivity = (Activity) context;
        init();
    }

    public LuckDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected LuckDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        mWindow = getWindow();
        mWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
        setCanceledOnTouchOutside(true);
//        mWindow.setWindowAnimations(R.style.showDialogBottom);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_luck_layout);
        ButterKnife.bind(this);

        WindowManager.LayoutParams attributes = mWindow.getAttributes();
        attributes.width = MyApp.getScreenWidth();
        attributes.height = MyApp.getScreenHeight();
        attributes.gravity = Gravity.CENTER;
        mWindow.setAttributes(attributes);

        ViewGroup.LayoutParams params = root.getLayoutParams();
        params.width = (int) (MyApp.getScreenWidth() * 0.82f);
        params.height = (int) (MyApp.getScreenHeight() * 0.7f);
        root.setLayoutParams(params);

        if (!CommonUtils.isStringNull(avatar)) {
            Glide.with(mActivity).load(avatar).error(R.mipmap.touxiangmoren).into(headImg);
        } else {
            headImg.setImageResource(R.mipmap.touxiangmoren);
        }
        name.setText(CommonUtils.isStringNull(nick) ? "" : nick);

        if ("0".equals(redPacketType) || "1".equals(redPacketType)) {
            type.setText("给你发了个红包");
        } else {
            type.setText("发了个红包，金额随机");
        }

        message.setText(CommonUtils.isStringNull(greeting) ? "" : greeting);

        btnOpen.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                btnOpen.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = btnOpen.getHeight();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) btnOpen.getLayoutParams();
                params.topMargin = (int) (420f / 652 * root.getHeight() - height / 2);
//                params.topMargin = (int) ((int) (MyApp.getScreenHeight() * 0.55f) * 0.58 - height / 2);
                btnOpen.setLayoutParams(params);
            }
        });

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1.1f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1.1f, 1f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(root, scaleX, scaleY);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
            }
        });
        animator.start();
    }

    @OnClick({R.id.btn_open, R.id.close, R.id.luck_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                if (mListener != null) {
                    mListener.onClick(view);
                }
                break;
            case R.id.close:
                this.dismiss();
                break;

            case R.id.luck_detail:
                getContext().startActivity(new Intent(getContext(), RPRedPacketDetailActivity.class).putExtra("luckmoneyid", redPacketId));
                this.dismiss();
                break;
        }
    }

    public void startAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(btnOpen, "rotationY", 0f, 360f);
        animator.setDuration(500);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(3);
        animator.setInterpolator(new LinearOutSlowInInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1f / 0.82f);
                PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 1f / 0.7f);
                ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(root, scaleX, scaleY);
                objectAnimator.setDuration(600);
                objectAnimator.setInterpolator(new LinearInterpolator());

                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        getContext().startActivity(new Intent(getContext(), RPRedPacketDetailActivity.class).putExtra("luckmoneyid", redPacketId));
                        mActivity.overridePendingTransition(R.anim.umeng_socialize_fade_in, R.anim.umeng_socialize_fade_out);
                        dismiss();
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        background.setVisibility(View.VISIBLE);
                    }
                });
                ObjectAnimator alpha = ObjectAnimator.ofFloat(container, "alpha", 1f, 0.2f).setDuration(600);
                alpha.setInterpolator(new LinearInterpolator());
                alpha.start();
                objectAnimator.start();
            }
        });
        animator.start();
    }

    public void luckExpir(){
        btnOpen.setVisibility(View.GONE);
        type.setVisibility(View.GONE);
        luckDetail.setVisibility(View.GONE);
        message.setText("该红包已超过24小时。如已领取，可在“我的钱包中查看领取记录");
    }

    public void hasNoRedPacket(){
        btnOpen.setVisibility(View.GONE);
        type.setVisibility(View.GONE);
        luckDetail.setVisibility(View.VISIBLE);
        message.setText("红包已领完");
    }
}
