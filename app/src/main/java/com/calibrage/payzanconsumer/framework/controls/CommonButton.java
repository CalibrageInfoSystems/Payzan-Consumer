package com.calibrage.payzanconsumer.framework.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.calibrage.payzanconsumer.framework.util.Fontcache;


/**
 * Created by Calibrage19 on 05-10-2017.
 */

@SuppressLint("AppCompatCustomView")
public class CommonButton extends Button {
    public CommonButton(Context context) {
        super(context);
        ApplyCustomFont(context);
    }

    public CommonButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        ApplyCustomFont(context);

    }

    public CommonButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ApplyCustomFont(context);
    }


    private void ApplyCustomFont(Context context) {
        Typeface customFont = Fontcache.gettypeFace(context, "Font-Regular.ttf");
        setTypeface(customFont);
    }
}
