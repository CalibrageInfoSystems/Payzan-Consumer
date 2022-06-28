package com.calibrage.payzanconsumer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Spinner;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.BroadbandFragment;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.DecimalDigitsInputFilter;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;

import static com.calibrage.payzanconsumer.framework.util.CommonConstants.MY_CHILD_ACTIVITY;

public class BroadbandActivity extends BaseActivity {

    public static final String TAG = BroadbandFragment.class.getSimpleName();
    private CommonEditText ServiceNEdt, amountEdt;
    private NCBTextInputLayout operatorTXT, serviceNoTXT, amountTXT;
    private Spinner operatorSpn;
    private Button submit;
    private Context context;
    private AlertDialog alertDialog;
    private String operatorStr, servicesnoStr, amountStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_broadband);
        ShowActionBar(getString(R.string.broadband_sname));
        setViews();
        initViews();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case (MY_CHILD_ACTIVITY):
                if (resultCode == Activity.RESULT_OK) {
                    startActivity(new Intent(BroadbandActivity.this,CompletePayment.class));
                }

                break;
        }
    }

    private void setViews() {
        context = BroadbandActivity.this;
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.broadband_sname));
        serviceNoTXT = (NCBTextInputLayout) findViewById(R.id.serviceNoTXT);
        operatorTXT = (NCBTextInputLayout) findViewById(R.id.operatorTXT);
        amountTXT = (NCBTextInputLayout) findViewById(R.id.amountTXT);
        operatorSpn = (Spinner) findViewById(R.id.operatorSpn);
        ServiceNEdt = (CommonEditText) findViewById(R.id.ServiceNEdt);
        amountEdt = (CommonEditText) findViewById(R.id.amountEdt);
        submit = (Button) findViewById(R.id.submit);

    }

    private void initViews() {
        amountEdt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(4,2)});
        amountEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    amountTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String x = "";
                x=editable.toString();
                if(x.startsWith("0")){
                    amountEdt.setText("");
                }
               /* if(!amount.getText().toString().equalsIgnoreCase(""))
                    if(Double.parseDouble(amount.getText().toString())<=5000.00){
                        amountTXT.setErrorEnabled(false);
                    }else{
                        amountTXT.setErrorEnabled(true);
                        amountTXT.setError(getString(R.string.err_amt_max_limit));
                    }*/
            }
        });

        operatorSpn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null && event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
                    boolean isKeyboardUp = imm.isAcceptingText();

                    if (isKeyboardUp)
                    {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false;
            }
        }) ;

        ServiceNEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    serviceNoTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        amountEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    amountTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserLoginStatus(BroadbandActivity.this)) {
                    hideSoftKeyboard();
                    if(isValidateUi()){
                        startActivity(new Intent(BroadbandActivity.this,CompletePayment.class));
                    }
                }

            }
        });


    }

    private boolean isValidateUi() {
        if (operatorSpn.getSelectedItem() != null) {
            operatorStr = (String) operatorSpn.getSelectedItem();
        }
        servicesnoStr = ServiceNEdt.getText().toString().trim();
        amountStr = amountEdt.getText().toString().trim();
       /* if (TextUtils.isEmpty(operatorStr)) {
            operatorTXT.setErrorEnabled(true);
            operatorTXT.setError(getString(R.string.err_please_select_operator));
            return false;
        } else */if (TextUtils.isEmpty(servicesnoStr)) {
            serviceNoTXT.setErrorEnabled(true);
            serviceNoTXT.setError(getString(R.string.err_please_enter_service_no));
            return false;
        } else if (TextUtils.isEmpty(amountStr)) {
            amountTXT.setErrorEnabled(true);
            amountTXT.setError(getString(R.string.err_please_enter_amount));
            amountEdt.requestFocusFromTouch();
            return false;
        }else if (Double.parseDouble(amountStr)>5000) {
            amountTXT.setErrorEnabled(true);
            amountTXT.setError(getString(R.string.err_amt_max_limit));
            CommonUtil.displayDialogWindow(getString(R.string.err_amt_max_limit),alertDialog,context);
            amountEdt.requestFocusFromTouch();
            return false;
        }


        return true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (operatorSubscription != null) {
//            operatorSubscription.unsubscribe();
//        }
    }

}
