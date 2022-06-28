package com.calibrage.payzanconsumer.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;


/**
 * Created by Calibrage11 on 9/11/2017.
 */

public class SplashActivity extends BaseActivity {
    private static int TIMEOUT = 5000;
    private ImageView _img_splash;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        _img_splash = (ImageView) findViewById(R.id.img_splash);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
        _img_splash.setAnimation(animation);


        splash();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CommonUtil.PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  Log.v(LOG_TAG, "permission granted");

                }
                break;
        }
    }

    private void splash() {
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                if (SharedPrefsData.getInstance(getApplicationContext()).getUserName(getApplicationContext()) != null && !SharedPrefsData.getInstance(getApplicationContext()).getUserName(getApplicationContext()).equalsIgnoreCase("")) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                } else {
                    Intent intent = new Intent(getApplicationContext(), ChooseLanguageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                }
                finish();

            }
        }, TIMEOUT);
    }

}
