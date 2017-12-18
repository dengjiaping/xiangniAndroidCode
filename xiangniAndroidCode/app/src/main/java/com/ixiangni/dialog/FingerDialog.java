package com.ixiangni.dialog;

import android.content.Context;
import android.widget.TextView;

import com.handongkeji.impactlib.dialog.XDialog;
import com.ixiangni.app.R;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class FingerDialog extends XDialog {

    private final TextView tv;

    public FingerDialog(Context context) {
        super(context, R.layout.finger_show);
        tv = (TextView) findViewById(R.id.tv_finger);
    }

    public void setText(String string){
        tv.setText(string);
    }
}
