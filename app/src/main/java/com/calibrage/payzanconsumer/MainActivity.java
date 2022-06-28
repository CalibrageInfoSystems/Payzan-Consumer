package com.calibrage.payzanconsumer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
        Intent i = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(i);
    }
}
