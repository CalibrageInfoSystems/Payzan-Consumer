package com.calibrage.payzanconsumer.activity;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.widget.ImageView;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Calibrage11 on 12/29/2017.
 */

public class FinalPaymentActivity extends BaseActivity {
//private ImageView mImgCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_payment);
//        mImgCheck = findViewById(R.id.imageView);
//        ((Animatable) mImgCheck.getDrawable()).start();

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Payment Success")
                .setContentText("your transaction id xxxxxxxxxxx")
                .setConfirmText("ok")
                .show();
    }
}
