package com.calibrage.payzanconsumer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.MobileRecharge;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.interfaces.DrawableClickListener;
import com.calibrage.payzanconsumer.framework.model.OperatorModel;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.DecimalDigitsInputFilter;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.calibrage.payzanconsumer.framework.util.CommonConstants.MY_CHILD_ACTIVITY;

public class MobileRechargeActivity extends BaseActivity {


    private RadioButton prepaidRB, postpaidRB;
    private Button
            talktimeRB, specialRB, submit;
    private ImageView mobileNumber;
    static final int PICK_CONTACT = 1;
    private CommonEditText mobileEdt, amount;
    private LinearLayout linear_lyt;


    private Context context;
    private TextView updateOperatorId;
    private Spinner currentOperator;
    private Subscription operatorSubscription;
    private ArrayList<OperatorModel.ListResult> listResults;
    private String serviceProviderType;
    private NCBTextInputLayout mobileNumberTXT, operatorTXT, amountTXT;
    private Boolean isProvider = false;
    private AlertDialog alertDialog;
    private String mobileStr, currentOperatorStr, amountStr;
    private ArrayList<String> operatorArrayList = new ArrayList<>();
    Toolbar toolbar;
    private int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recharge);
        ShowActionBar(getString(R.string.mobile_recharge_sname));
        setView();
        initViews();


    }


    private void setView() {
        context = MobileRechargeActivity.this;
        // setHasOptionsMenu(true);
        listResults = new ArrayList<OperatorModel.ListResult>();

       /* ((AppCompatActivity) getActivity()).setSupportActionBar(HomeActivity.toolbar);*/
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.mobile_recharge_sname));
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        /*((AppCompatActivity) getActivity()).setSupportActionBar(HomeActivity.toolbar);
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.mobile_recharge_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));*/
      /*  HomeActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeTab(TAG);
            }
        });*/
        mobileNumber = (ImageView) findViewById(R.id.mobileNumber);
        mobileEdt = (CommonEditText) findViewById(R.id.mobileEdt);
        amount = (CommonEditText) findViewById(R.id.amount);
        currentOperator = (Spinner) findViewById(R.id.currentOperator);
        talktimeRB = (Button) findViewById(R.id.talktimeRB);
        specialRB = (Button) findViewById(R.id.specialRB);
        prepaidRB = (RadioButton) findViewById(R.id.prepaidRB);
        postpaidRB = (RadioButton) findViewById(R.id.postpaidRB);
        postpaidRB = (RadioButton) findViewById(R.id.postpaidRB);
        updateOperatorId = (TextView) findViewById(R.id.updateOperatorId);
        submit = (Button) findViewById(R.id.submit);
        mobileNumberTXT = (NCBTextInputLayout) findViewById(R.id.mobileNumberTXT);
        operatorTXT = (NCBTextInputLayout) findViewById(R.id.operatorTXT);
        amountTXT = (NCBTextInputLayout) findViewById(R.id.amountTXT);
        // setHasOptionsMenu(true);
        // currentOperator.setThreshold(1);

        talktimeRB.setBackgroundResource(R.drawable.roundbutton);
        specialRB.setBackgroundResource(R.drawable.roundbutton_white);
        talktimeRB.setTextColor(ContextCompat.getColor(this, R.color.white_new));
        specialRB.setTextColor(ContextCompat.getColor(this, R.color.accent));
        serviceProviderType = CommonConstants.SERVICE_PROVIDER_ID_PREPAID;
        prepaidRB.setChecked(true);
        getOperator(CommonConstants.SERVICE_PROVIDER_ID_PREPAID);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitle(getString(R.string.mobile_recharge_sname));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));
        linear_lyt = findViewById(R.id.linear_lyt);
    }


    private void initViews() {
        amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 2)});
        amount.addTextChangedListener(new TextWatcher() {
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
                x = editable.toString();
                if (x.startsWith("0")) {
                    amount.setText("");
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
        prepaidRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceProviderType = CommonConstants.SERVICE_PROVIDER_ID_PREPAID;
                getOperator(serviceProviderType);
            }
        });

        postpaidRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceProviderType = CommonConstants.SERVICE_PROVIDER_ID_POSTPAID;
                getOperator(serviceProviderType);
            }
        });

        updateOperatorId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //currentOperator.showDropDown();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                if (checkUserLoginStatus(MobileRechargeActivity.this)) {
                    if (validateUI()) {
                        startActivity(new Intent(MobileRechargeActivity.this, CompletePayment.class));
                    }
                }


            }
        });
//        currentOperator.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                currentOperator.showDropDown();
//
//
//
//                return false;
//            }
//        });

        currentOperator.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null && event.getAction() == MotionEvent.ACTION_MOVE) {
                    InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
                    boolean isKeyboardUp = imm.isAcceptingText();

                    if (isKeyboardUp) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
        currentOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentOperator.getSelectedItemPosition() != 0) {
                    currentId = listResults.get(currentOperator.getSelectedItemPosition() - 1).getId();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mobileEdt.setDrawableClickListener(new DrawableClickListener() {
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        //Do something here
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(intent, PICK_CONTACT);

                        break;

                    default:
                        break;
                }
            }

        });

        mobileEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    mobileNumberTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String x = "";
                x = editable.toString();
                if (x.startsWith("0")) {
                    mobileEdt.setText("");
                }
            }
        });
//        currentOperator.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() > 0) {
//                    operatorTXT.setErrorEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//
//
//
//
//            }
//        });
        amount.addTextChangedListener(new TextWatcher() {
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

            }
        });

        // currentOperator.setAdapter();


       /* rootview.setFocusableInTouchMode(true);
        rootview.requestFocus();
        rootview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    closeTab();
                    // onCloseFragment();
                    return true;
                } else {
                    return false;
                }
            }
        });*/

        talktimeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                talktimeRB.setBackgroundResource(R.drawable.roundbutton);
                specialRB.setBackgroundResource(R.drawable.roundbutton_white);
                talktimeRB.setTextColor(ContextCompat.getColor(MobileRechargeActivity.this, R.color.white_new));
                /*specialRB.setBackgroundResource(R.color.white_new);*/
                specialRB.setTextColor(ContextCompat.getColor(MobileRechargeActivity.this, R.color.accent));

            }
        });

        specialRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                specialRB.setBackgroundResource(R.drawable.roundbutton);
                talktimeRB.setBackgroundResource(R.drawable.roundbutton_white);
                specialRB.setTextColor(ContextCompat.getColor(MobileRechargeActivity.this, R.color.white_new));
                // talktimeRB.setBackgroundResource(R.drawable.border_accent);
                /*talktimeRB.setBackgroundColor(ContextCompat.getColor(MobileRechargeActivity.this, R.color.white_new));*/
                talktimeRB.setTextColor(ContextCompat.getColor(MobileRechargeActivity.this, R.color.accent));

            }
        });

        mobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getOperator(String providerType) {
        if (CommonUtil.isNetworkAvailable(context)) {
            MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
            operatorSubscription = service.getOperator(ApiConstants.MOBILE_SERVICES + providerType)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<OperatorModel>() {
                        @Override
                        public void onCompleted() {
                            //  Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof HttpException) {
                                ((HttpException) e).code();
                                ((HttpException) e).message();
                                ((HttpException) e).response().errorBody();
                                try {
                                    ((HttpException) e).response().errorBody().string();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                e.printStackTrace();
                            }
                            Toast.makeText(context, R.string.toast_fail, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(OperatorModel operatorModel) {
                            operatorArrayList = new ArrayList<>();
                            listResults = (ArrayList<OperatorModel.ListResult>) operatorModel.getListResult();
                            if (!listResults.isEmpty()) {
                                operatorArrayList.add(getString(R.string.spn_select_operator));
                                for (int i = 0; i < listResults.size(); i++) {
                                    operatorArrayList.add(listResults.get(i).getName());
                                }
                            }
                            //                      ArrayAdapter<OperatorModel.ListResult> listResultArrayAdapter = new ArrayAdapter<OperatorModel.ListResult>(context,android.R.layout.simple_dropdown_item_1line,listResults);
//                        currentOperator.setAdapter(listResultArrayAdapter);
                            ArrayAdapter aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, operatorArrayList);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            currentOperator.setAdapter(aa);

                            //                          GenericAdapter genericAdapter = new GenericAdapter(context, operatorModel.getListResult(), R.layout.adapter_single_item);
//                            genericAdapter.setAdapterOnClick(MobileRecharge.this);
//                            currentOperator.setAdapter(genericAdapter);
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = this.getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        mobileEdt.setText(hasPhone.replaceAll("\\s", ""));
                    }

                }
                break;
            case (MY_CHILD_ACTIVITY):
                if (resultCode == Activity.RESULT_OK) {
                    startActivity(new Intent(MobileRechargeActivity.this, CompletePayment.class));
                }

                break;
        }
    }

    private boolean validateUI() {
        //  isProviderExists();
        mobileStr = mobileEdt.getText().toString().trim();
        currentOperatorStr = (String) currentOperator.getSelectedItem();
        amountStr = amount.getText().toString().trim();

        if (TextUtils.isEmpty(mobileStr)) {
            mobileNumberTXT.setErrorEnabled(true);
            mobileNumberTXT.setError(getString(R.string.err_enter_mobile_number));
            mobileEdt.requestFocusFromTouch();
            return false;
        } else if (mobileEdt.getText().length() < 2 || mobileEdt.getText().length() < 10) {
            mobileNumberTXT.setErrorEnabled(true);
            mobileNumberTXT.setError(getString(R.string.err_enter_valid_mobile_no));
            mobileEdt.requestFocusFromTouch();
            return false;
        } else if (currentOperator.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.err_please_select_operator), Toast.LENGTH_SHORT).show();
            return false;
        } else if (amountStr.equalsIgnoreCase("")) {
            amountTXT.setErrorEnabled(true);
            amountTXT.setError(getString(R.string.err_enter_amount));
            amount.requestFocusFromTouch();
            return false;
        } else if (Double.parseDouble(amountStr) > 5000) {
            amountTXT.setErrorEnabled(true);
            amountTXT.setError(getString(R.string.err_amt_max_limit));
            CommonUtil.displayDialogWindow(getString(R.string.err_amt_max_limit), alertDialog, context);
            amount.requestFocusFromTouch();
            return false;
        }
        return true;

    }

    private boolean isValidPhone() {
        String target = mobileEdt.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (operatorSubscription != null) {
            operatorSubscription.unsubscribe();
        }
    }
}
