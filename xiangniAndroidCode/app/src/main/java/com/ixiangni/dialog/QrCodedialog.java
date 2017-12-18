package com.ixiangni.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.zxing.WriterException;
import com.handongkeji.impactlib.dialog.XDialog;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.zxing.encoding.EncodingHandler;
import com.ixiangni.app.R;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.constants.UrlString;
import com.ixiangni.util.GlideUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class QrCodedialog extends XDialog {

    @Bind(R.id.iv_user_icon)
    ImageView ivUserIcon;
    @Bind(R.id.tv_user_nick)
    TextView tvUserNick;
    @Bind(R.id.iv_qrcode)
    ImageView ivQrcode;

    public QrCodedialog(Context context,String userPic,String userNick) {
        super(context, R.layout.dialog_qrcode);
        ButterKnife.bind(this);
        tvUserNick.setText(userNick);
        GlideUtil.loadRoundImage(getContext(),userPic,ivUserIcon);
        int width = CommonUtils.dip2px(getContext(), 180);

        String content = UrlString.URL_QCODE+"?"+ LoginHelper.getInstance().getUserid(getContext());
        try {
            Bitmap qrCode = EncodingHandler.createQRCode(content, width);
            ivQrcode.setImageBitmap(qrCode);
        } catch (WriterException e) {
            toast("生成二维码失败！");
            e.printStackTrace();
        }

    }
}
