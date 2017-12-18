package com.ixiangni.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.bean.CircleListBean.DataBean.UserinfoBean;
import com.ixiangni.bean.CommentBean;
import com.ixiangni.util.CircleMovementMethod;
import com.ixiangni.util.FileUtil;
import com.ixiangni.util.SpannableClickable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiwei on 16/7/9.
 */
public class CommentListView extends LinearLayout {
    private int itemColor;
    private int itemSelectorColor;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private List<CommentBean> mDatas;
    private UserinfoBean mys;
    private LayoutInflater layoutInflater ;
    private MyApp myApp;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
    //第一个是评论的数据    第一个是我的
    public void setDatas(List<CommentBean> datas){//, UserinfoBean my
//        if (my==null){
//            my = new UserinfoBean();
//        }
        if(datas == null ){
            datas = new ArrayList<CommentBean>();
        }
        mDatas = datas;
//        mys=my;
        notifyDataSetChanged();
    }

    public List<CommentBean> getDatas(){
        return mDatas;
    }

    public CommentListView(Context context) {
        super(context);
    }

    public CommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public CommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PraiseListView, 0, 0);
        try {
            //textview的默认颜色
            itemColor = typedArray.getColor(R.styleable.PraiseListView_item_color, 0x60000000);
            itemSelectorColor = typedArray.getColor(R.styleable.PraiseListView_item_selector_color, 0xffffffff);

        }finally {
            typedArray.recycle();
        }
    }

    public void notifyDataSetChanged(){

        removeAllViews();
        if(mDatas == null || mDatas.size() == 0){
            return;
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0; i<mDatas.size(); i++){
            final int index = i;
            View view = getView(index);
            if(view == null){
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }

            addView(view, index, layoutParams);
        }

    }

    private View getView(final int position){
        if(layoutInflater == null){
            layoutInflater = LayoutInflater.from(getContext());
        }
        View convertView = layoutInflater.inflate(R.layout.item_comment, null, false);

        TextView commentTv = (TextView) convertView.findViewById(R.id.commentTv);
        final CircleMovementMethod circleMovementMethod = new CircleMovementMethod(itemColor, itemSelectorColor);

        final CommentBean bean = mDatas.get(position);//这个是评论里面的数据
        String isTag = bean.getAdditionalmark();//1.是回复 0是自己的
        String toReplyName = "";//不是自己 是其他人
        String usernick="";
            if ("0".equals(isTag)){
                //String mainnick = bean.getMainnick();//别人的
                String userid = bean.getUserid();//该条userid   0
                 usernick = bean.getUsernick();//该条评论的昵称

            }else if ("1".equals(isTag)){
                String userid = bean.getUserid();//该条userid   0
                 usernick = bean.getUsernick();//该条评论的昵称

                if (bean.getUsernick() != null) {
                    toReplyName =bean.getMainnick();//评论的用户
                }

            }
        usernick = CommonUtils.isStringNull(usernick)?"":usernick;
        toReplyName = CommonUtils.isStringNull(toReplyName)?"":toReplyName;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(setClickableSpan(usernick, bean.getUserid()));

        if (!TextUtils.isEmpty(toReplyName)) {

            builder.append(" 回复 ");
            builder.append(setClickableSpan(toReplyName, bean.getUserid()));
        }
        builder.append(": ");
        //转换表情字符
        String contentBodyStr = bean.getCommentcontent();
//        builder.append(UrlUtils.formatUrlString(contentBodyStr));
        FileUtil.handlerEmojiTextWidthNick(getContext(),commentTv,builder,contentBodyStr);
        commentTv.setText(builder);

        commentTv.setMovementMethod(circleMovementMethod);
        commentTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(position);
                    }
                }
            }
        });
        commentTv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if(onItemLongClickListener!=null){
                        onItemLongClickListener.onItemLongClick(position);
                    }
                    return true;
                }
                return false;
            }
        });

        return convertView;
    }

    @NonNull
    private SpannableString setClickableSpan(final String textStr, final String id) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(itemColor){
                                    @Override
                                    public void onClick(View widget) {
//                                        Toast.makeText(MyApp.getInstance(), textStr + " &id = " + id, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(Color.parseColor("#0667e9"));
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    public static interface OnItemClickListener{
        public void onItemClick(int position);
    }

    public static interface OnItemLongClickListener{
        public void onItemLongClick(int position);
    }

}
