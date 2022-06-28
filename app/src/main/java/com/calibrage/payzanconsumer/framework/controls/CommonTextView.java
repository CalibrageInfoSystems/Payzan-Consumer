package com.calibrage.payzanconsumer.framework.controls;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.calibrage.payzanconsumer.framework.util.Fontcache;


/**
 * Created by Calibrage19 on 05-10-2017.
 */

public class CommonTextView extends android.support.v7.widget.AppCompatTextView {

    public CommonTextView(Context context) {
        super(context);
        ApplyCustomFont(context);
    }

    public CommonTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ApplyCustomFont(context);
    }

    public CommonTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ApplyCustomFont(context);
    }



    private void ApplyCustomFont(Context context) {
        Typeface customFont = Fontcache.gettypeFace(context, "Font-Regular.ttf");
        setTypeface(customFont);
    }


}
