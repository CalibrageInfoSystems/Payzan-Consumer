package com.calibrage.payzanconsumer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;

import static com.calibrage.payzanconsumer.framework.util.CommonUtil.updateResources;

/**
 * Created by Calibrage10 on 1/2/2018.
 */

public class ChooseLanguageActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout english_lang, sinhala_lang, tamil_lang;
    private Button continueBtn;
    private ImageView imgSelect1, imgSelect2, imgSelect3;
    private TextView txtChooseLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_language);
        initViews();
        setViews();
    }

    private void initViews() {
        english_lang = (LinearLayout) findViewById(R.id.english_lang);
        sinhala_lang = (LinearLayout) findViewById(R.id.sinhala_lang);
        tamil_lang = (LinearLayout) findViewById(R.id.tamil_lang);
        continueBtn = (Button) findViewById(R.id.continueBtn);
        imgSelect1 = (ImageView) findViewById(R.id.imgSelect1);
        imgSelect2 = (ImageView) findViewById(R.id.imgSelect2);
        imgSelect3 = (ImageView) findViewById(R.id.imgSelect3);
        txtChooseLanguage = (TextView) findViewById(R.id.txtChooseLanguage);
        updateResources(ChooseLanguageActivity.this, "en-US");
        SharedPrefsData.getInstance(ChooseLanguageActivity.this).updateIntValue(ChooseLanguageActivity.this, "lang", 1);
        SharedPrefsData.getInstance(ChooseLanguageActivity.this.getApplicationContext()).updateIntValue(ChooseLanguageActivity.this.getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
        imgSelect1.setVisibility(View.VISIBLE);
    }

    private void setViews() {
        continueBtn.setOnClickListener(this);
        english_lang.setOnClickListener(this);
        sinhala_lang.setOnClickListener(this);
        tamil_lang.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueBtn:
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                LoginFragment loginFragment = new LoginFragment();
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.content_frame,  loginFragment)
//                        .commit();
                break;
            case R.id.english_lang:
                updateResources(ChooseLanguageActivity.this, "en-US");
                SharedPrefsData.getInstance(ChooseLanguageActivity.this).updateIntValue(ChooseLanguageActivity.this, "lang", 1);
                SharedPrefsData.getInstance(ChooseLanguageActivity.this.getApplicationContext()).updateIntValue(ChooseLanguageActivity.this.getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
                imgSelect1.setVisibility(View.VISIBLE);
                imgSelect2.setVisibility(View.GONE);
                imgSelect3.setVisibility(View.GONE);
                break;
            case R.id.sinhala_lang:
                updateResources(ChooseLanguageActivity.this, "si");
                SharedPrefsData.getInstance(ChooseLanguageActivity.this).updateIntValue(ChooseLanguageActivity.this, "lang", 2);
                SharedPrefsData.getInstance(ChooseLanguageActivity.this.getApplicationContext()).updateIntValue(ChooseLanguageActivity.this.getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
                imgSelect2.setVisibility(View.VISIBLE);
                imgSelect3.setVisibility(View.GONE);
                imgSelect1.setVisibility(View.GONE);
                break;
            case R.id.tamil_lang:
                updateResources(ChooseLanguageActivity.this, "ta");
                SharedPrefsData.getInstance(ChooseLanguageActivity.this).updateIntValue(ChooseLanguageActivity.this, "lang", 3);
                SharedPrefsData.getInstance(ChooseLanguageActivity.this.getApplicationContext()).updateIntValue(ChooseLanguageActivity.this.getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
                imgSelect3.setVisibility(View.VISIBLE);
                imgSelect2.setVisibility(View.GONE);
                imgSelect1.setVisibility(View.GONE);
                break;
        }
    }


}
