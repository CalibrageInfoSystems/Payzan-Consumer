package com.calibrage.payzanconsumer.framework.controls;

import android.content.Context;
import android.support.annotation.FloatRange;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

/**
 * Created by Calibrage11 on 10/17/2017.
 */

public class CommonCardView extends CardView {
    public CommonCardView(Context context) {
        super(context);
    }

    public CommonCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        super.setAlpha(alpha);
    }
}
