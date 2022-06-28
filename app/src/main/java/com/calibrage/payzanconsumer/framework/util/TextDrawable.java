package com.calibrage.payzanconsumer.framework.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by Calibrage10 on 12/22/2017.
 */

public class TextDrawable extends Drawable {

    private final String text;
    private final Paint paint;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextDrawable(String text) {
        this.text = text;
        this.paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(22f);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void draw(Canvas canvas) {
//        canvas.drawText(text, -65, 20, paint);
        canvas.drawText(text, -15, 9, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


}
