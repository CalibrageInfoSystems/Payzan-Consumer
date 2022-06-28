package com.calibrage.payzanconsumer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.calibrage.payzanconsumer.R;
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

public class LandLineActivity extends BaseActivity {

    public static final String TAG = LandLineActivity.class.getSimpleName();
    private Context context;
    private NCBTextInputLayout operatorTXT, numberTXT, circleTXT, amountTXT;
    private CommonEditText mobilenoEdt, amount;
    private Spinner circleEdt, operatorEdt;
    static final int PICK_CONTACT = 1;
    private Subscription operatorSubscription;
    private ArrayList<OperatorModel.ListResult> listResults;
    private Button submit;
    private Toolbar toolbar;
    private AlertDialog alertDialog;
    private String operatorStr, mobilenoStr, circleStr, amountStr;
    private ArrayList<String> operatorArrayList = new ArrayList<>();
    private ArrayList<String> circleArrayList = new ArrayList<>();
    private int operatorId, circleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paylandline_bill);
        ShowActionBar(getString(R.string.landline_sname));
        setViews();
        initViews();
        getOperator(CommonConstants.SERVICE_PROVIDER_ID_LANDLINE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = context.getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        mobilenoEdt.setText(hasPhone);
                    }

                }
                break;
            case (MY_CHILD_ACTIVITY):
                if (resultCode == Activity.RESULT_OK) {
                    startActivity(new Intent(LandLineActivity.this, CompletePayment.class));
                }

                break;
        }
    }

    private void initViews() {
      /*  operatorEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //  operatorEdt.showDropDown();
                return false;
            }
        });*/
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
        circleEdt.setOnTouchListener(new View.OnTouchListener() {

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
        circleEdt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (circleEdt.getSelectedItemPosition() != 0) {
                    circleId = listResults.get(circleEdt.getSelectedItemPosition() - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        operatorEdt.setOnTouchListener(new View.OnTouchListener() {

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
        operatorEdt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (operatorEdt.getSelectedItemPosition() != 0) {
                    operatorId = listResults.get(operatorEdt.getSelectedItemPosition() - 1).getId();
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mobilenoEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    numberTXT.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String x="";
                x=editable.toString();
                if (x.startsWith("0"))
                {
                    mobilenoEdt.setText("");
                }

            }
        });

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

        mobilenoEdt.setDrawableClickListener(new DrawableClickListener() {


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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkUserLoginStatus(LandLineActivity.this)) {
                    //isValidateUi();
                    hideSoftKeyboard();
                    if (isValidateUi()) {
                        startActivity(new Intent(LandLineActivity.this, CompletePayment.class));
                    }
                }

            }
        });
    }

    private void setViews() {
        context = LandLineActivity.this;
        //  setHasOptionsMenu(true);

        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.landline_sname));

        operatorTXT = (NCBTextInputLayout) findViewById(R.id.operatorTXT);
        numberTXT = (NCBTextInputLayout) findViewById(R.id.numberTXT);
        circleTXT = (NCBTextInputLayout) findViewById(R.id.circleTXT);
        amountTXT = (NCBTextInputLayout) findViewById(R.id.amountTXT);

        operatorEdt = (Spinner) findViewById(R.id.operatorEdt);
        mobilenoEdt = (CommonEditText) findViewById(R.id.mobilenoEdt);
        circleEdt = (Spinner) findViewById(R.id.circleEdt);
        amount = (CommonEditText) findViewById(R.id.amount);
        submit = (Button) findViewById(R.id.submit);

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
                            //   Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
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
//                            Toast.makeText(context, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(OperatorModel operatorModel) {
                             operatorArrayList=new ArrayList<>();
                            circleArrayList=new ArrayList<>();

                            listResults = (ArrayList<OperatorModel.ListResult>) operatorModel.getListResult();
                            if (!listResults.isEmpty()) {
                                operatorArrayList.add(getString(R.string.spn_select_operator));
                                for (int i = 0; i < listResults.size(); i++) {
                                    operatorArrayList.add(listResults.get(i).getName());
                                }}

                            if (!listResults.isEmpty()) {
                                circleArrayList.add(getString(R.string.spn_select_circle));
                                for (int i = 0; i < listResults.size(); i++) {
                                    circleArrayList.add(listResults.get(i).getName());
                                }}

//                        ArrayAdapter<OperatorModel.ListResult> listResultArrayAdapter = new ArrayAdapter<OperatorModel.ListResult>(context,android.R.layout.simple_dropdown_item_1line,listResults);
//                        currentOperator.setAdapter(listResultArrayAdapter);
                            ArrayAdapter aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, operatorArrayList);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            operatorEdt.setAdapter(aa);


                            ArrayAdapter aa1 = new ArrayAdapter(context, android.R.layout.simple_spinner_item, circleArrayList);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            circleEdt.setAdapter(aa1);
//                            GenericAdapter genericAdapter = new GenericAdapter(context, operatorModel.getListResult(), R.layout.adapter_single_item);
//                            genericAdapter.setAdapterOnClick(PayLandLineBill.this);
//                            operatorEdt.setAdapter(genericAdapter);
                        }
                    });
        }
    }

    private boolean isValidateUi() {
        operatorStr = (String) operatorEdt.getSelectedItem();
        mobilenoStr = mobilenoEdt.getText().toString().trim();
        circleStr = (String) circleEdt.getSelectedItem();
        amountStr = amount.getText().toString().trim();
        if (operatorEdt.getSelectedItemPosition()==0) {
            Toast.makeText(getApplicationContext(), getString(R.string.err_please_select_operator), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(mobilenoStr)) {
            numberTXT.setError(getString(R.string.err_enter_number));
            numberTXT.setErrorEnabled(true);
            return false;
        } else if (mobilenoEdt.getText().length() < 2 || mobilenoEdt.getText().length() < 10) {
            numberTXT.setErrorEnabled(true);
            numberTXT.setError(getString(R.string.err_enter_valid_mobile_no));
            mobilenoEdt.requestFocusFromTouch();
            return false;
        } else if (circleEdt.getSelectedItemPosition()==0) {
            Toast.makeText(getApplicationContext(), getString(R.string.err_please_select_circle), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(amountStr)) {
            amountTXT.setError(getString(R.string.err_enter_amount));
            amountTXT.setErrorEnabled(true);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (operatorSubscription != null) {
            operatorSubscription.unsubscribe();
        }
    }
}
