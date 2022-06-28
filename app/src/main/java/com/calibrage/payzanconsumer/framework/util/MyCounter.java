package com.calibrage.payzanconsumer.framework.util;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

/**
 * Created by Calibrage11 on 12/28/2017.
 */

public class MyCounter extends CountDownTimer {
    Context mContext;

    public MyCounter(long millisInFuture, long countDownInterval, Context mCon) {
        super(millisInFuture, countDownInterval);
        mContext = mCon;

    }

    @Override
    public void onFinish() {
        Log.e("CountDown is Finish","CountDown is Finish");
        Intent intent = new Intent();
        intent.setAction("com.PayZan.CUSTOM_INTENT");
        mContext.sendBroadcast(intent);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        Log.e("CountDown is ","time : "+(millisUntilFinished/1000)+"");
    }
}
