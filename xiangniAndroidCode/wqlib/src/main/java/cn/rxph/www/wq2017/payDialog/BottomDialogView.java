package cn.rxph.www.wq2017.payDialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import cn.rxph.www.wq2017.R;


public class BottomDialogView extends PopupWindow {
    private View dialogView;
    private EditText payPassEt;
    private Button cancelBtn, confirmBtn;
    private ImageView backDialogIv;

    public BottomDialogView(Activity context, final BottomDialogOnclickListener bottomDialogOnclickListener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogView = inflater.inflate(R.layout.dialog, null);
        backDialogIv = (ImageView) dialogView.findViewById(R.id.backDialogIv);
        payPassEt = (EditText) dialogView.findViewById(R.id.payPassEt);
        cancelBtn = (Button) dialogView.findViewById(R.id.cancelBtn);
        confirmBtn = (Button) dialogView.findViewById(R.id.confirmBtn);
        backDialogIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialogOnclickListener.onPositiveClick(payPassEt.getText().toString().trim(), BottomDialogView.this);
            }
        });


        this.setContentView(dialogView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.DialogShowStyle); //设置SelectPicPopupWindow弹出窗体动画效果
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

        dialogView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = dialogView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) { //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//                        dismiss();
                    }
                }
                return true;
            }
        });

    }

}