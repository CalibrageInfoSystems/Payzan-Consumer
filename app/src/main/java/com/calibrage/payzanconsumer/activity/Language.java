package com.calibrage.payzanconsumer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;

import java.util.Locale;

import static com.calibrage.payzanconsumer.framework.util.CommonUtil.updateResources;

public class Language extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button btn_continue;
    int lang = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        btn_continue = findViewById(R.id.btn_continue);
        SharedPrefsData.getInstance(this).updateIntValue(getApplication(), "lang", 3);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        checklang();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    Toast.makeText(Language.this, rb.getText(), Toast.LENGTH_SHORT).show();


                    Toast.makeText(Language.this, rb.getText(), Toast.LENGTH_SHORT).show();

                    if (rb.getId() == R.id.rdo_english) {
                     //   Toast.makeText(Language.this, "rdo_english", Toast.LENGTH_SHORT).show();

                        updateResources(getApplicationContext(), "en-US");
                        SharedPrefsData.getInstance(Language.this).updateIntValue(getApplication(), "lang", 1);


                        SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                         startActivity(i);


                    } else if (rb.getId() == R.id.rdo_telugu) {
                       // Toast.makeText(Language.this, "rdo_telugu", Toast.LENGTH_SHORT).show();
                        updateResources(getApplicationContext(), "te");
                        SharedPrefsData.getInstance(Language.this).updateIntValue(getApplication(), "lang", 2);

                        SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);

                    }else if(rb.getId()== R.id.rdo_hindhi)
                    {
                       // Toast.makeText(Language.this, "rdo_hindhi", Toast.LENGTH_SHORT).show();
                        updateResources(getApplicationContext(), "hi");
                        SharedPrefsData.getInstance(Language.this).updateIntValue(getApplication(), "lang", 3);

                        SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                    }
                    finish();
        /*else if (rb.getText()== R.id.rdo_hindhi)
        {
            Toast.makeText(this, "rdo_hindhi", Toast.LENGTH_SHORT).show();
            updateResources(getApplicationContext(),"te");
            SharedPrefsData.getInstance(this).updateIntValue(getApplication(), "lang", 3);

            SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
            Intent i = new Intent(getBaseContext(), HomeActivity.class);
           // startActivity(i);

        }else if (rb.getText()== R.id.rdo_sinhala)
        {
            updateResources(getApplicationContext(),"te");
            SharedPrefsData.getInstance(this).updateIntValue(getApplication(), "lang", 4);

            SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
            Intent i = new Intent(getBaseContext(), HomeActivity.class);
           // startActivity(i);
        }else if (rb.getText()== R.id.rdo_tamil)
        {
            updateResources(getApplicationContext(),"te");
            SharedPrefsData.getInstance(this).updateIntValue(getApplication(), "lang", 5);

            SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
            Intent i = new Intent(getBaseContext(), HomeActivity.class);
          //  startActivity(i);
        }else
        {
            updateResources(getApplicationContext(),"te");
            SharedPrefsData.getInstance(this).updateIntValue(getApplication(), "lang", 1);

            SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
            Intent i = new Intent(getBaseContext(), HomeActivity.class);
           // startActivity(i);
        }
*/
                }

            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                Toast.makeText(Language.this, rb.getText(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onClear(View v) {
        /* Clears all selected radio buttons to default */
        radioGroup.clearCheck();
    }

    public void onSubmit(View v) {
        RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
        Toast.makeText(Language.this, rb.getText(), Toast.LENGTH_SHORT).show();

        if (rb.getText() == "English") {
           // Toast.makeText(this, "rdo_english", Toast.LENGTH_SHORT).show();

            updateResources(getApplicationContext(), "en-US");
            SharedPrefsData.getInstance(this).updateIntValue(getApplication(), "lang", 1);


            SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
            Intent i = new Intent(getBaseContext(), HomeActivity.class);
            // startActivity(i);

        } else if (rb.getText() == "Telugu / తెలుగు") {
            //Toast.makeText(this, "rdo_telugu", Toast.LENGTH_SHORT).show();
            updateResources(getApplicationContext(), "te");
            SharedPrefsData.getInstance(this).updateIntValue(getApplication(), "lang", 2);

            SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
            Intent i = new Intent(getBaseContext(), HomeActivity.class);
            startActivity(i);

        }
        /*else if (rb.getText()== R.id.rdo_hindhi)
        {
            Toast.makeText(this, "rdo_hindhi", Toast.LENGTH_SHORT).show();
            updateResources(getApplicationContext(),"te");
            SharedPrefsData.getInstance(this).updateIntValue(getApplication(), "lang", 3);

            SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
            Intent i = new Intent(getBaseContext(), HomeActivity.class);
           // startActivity(i);

        }else if (rb.getText()== R.id.rdo_sinhala)
        {
            updateResources(getApplicationContext(),"te");
            SharedPrefsData.getInstance(this).updateIntValue(getApplication(), "lang", 4);

            SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
            Intent i = new Intent(getBaseContext(), HomeActivity.class);
           // startActivity(i);
        }else if (rb.getText()== R.id.rdo_tamil)
        {
            updateResources(getApplicationContext(),"te");
            SharedPrefsData.getInstance(this).updateIntValue(getApplication(), "lang", 5);

            SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
            Intent i = new Intent(getBaseContext(), HomeActivity.class);
          //  startActivity(i);
        }else
        {
            updateResources(getApplicationContext(),"te");
            SharedPrefsData.getInstance(this).updateIntValue(getApplication(), "lang", 1);

            SharedPrefsData.getInstance(getApplicationContext()).updateIntValue(getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
            Intent i = new Intent(getBaseContext(), HomeActivity.class);
           // startActivity(i);
        }
*/


    }

    private void checklang() {
        lang = SharedPrefsData.getInstance(getApplicationContext()).getIntFromSharedPrefs("lang");
        if (lang == 1) {
            radioGroup.check(R.id.rdo_english);
        } else if (lang == 2) {
            radioGroup.check(R.id.rdo_telugu);
        } else if (lang == 3) {
            radioGroup.check(R.id.rdo_hindhi);
        } else if (lang == 4) {
            radioGroup.check(R.id.rdo_sinhala);
        } else if (lang == 5) {
            radioGroup.check(R.id.rdo_tamil);
        } else {
            radioGroup.check(R.id.rdo_english);
        }

    }


}
