package com.handongkeji.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/1/21 0021.
 */

public class DecimalEditText extends EditText {
    public DecimalEditText(Context context) {
        super(context);
        init();
    }

    public DecimalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DecimalEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DecimalEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                int indexOf = str.indexOf(".");
                if (indexOf <= 0) {
                    return;
                }
                if (str.length()-1-indexOf > 2) {
                    s.delete(indexOf+3,indexOf+4);
                }
            }
        });
    }
}
