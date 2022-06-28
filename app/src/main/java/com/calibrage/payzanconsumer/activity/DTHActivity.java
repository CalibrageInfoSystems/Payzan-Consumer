package com.calibrage.payzanconsumer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
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

public class DTHActivity extends BaseActivity {

    public static final String TAG = DTHActivity.class.getSimpleName();
    private View rootView;
    private Context context;
    private NCBTextInputLayout subscriberTXT, operatorTXT, amountTXT;
    private CommonEditText subscriberEdt, amount;
    private Spinner operatorSpn;
    private ArrayList<OperatorModel.ListResult> listResults;
    private Subscription operatorSubscription;
    private Button submit;
    private AlertDialog alertDialog;
    private String subscriberStr, operatorStr, amountStr;
    private ArrayList<String> operatorArrayList = new ArrayList<>();
    private int operatorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dth);
        ShowActionBar(getString(R.string.dth_sname));
        setViews();
        initViews();
        if (CommonUtil.isNetworkAvailable(context)) {
            getOperator(CommonConstants.SERVICE_PROVIDER_ID_DTH);
        }
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
        subscriberEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    subscriberTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
        operatorSpn.setOnTouchListener(new View.OnTouchListener() {

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
        operatorSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (operatorSpn.getSelectedItemPosition() != 0) {
                    operatorId = listResults.get(operatorSpn.getSelectedItemPosition() - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
      /*  operatorSpn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // operatorSpn.showDropDown();
                return false;
            }
        });*/

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkUserLoginStatus(DTHActivity.this)) {
                    hideSoftKeyboard();
                    if (isValidateUI()) {
                        startActivity(new Intent(DTHActivity.this, CompletePayment.class));
                    }
                }

            }
        });
    }

    private void setViews() {
        context = DTHActivity.this;
        subscriberTXT = (NCBTextInputLayout) findViewById(R.id.SubscriberTXT);
        operatorTXT = (NCBTextInputLayout) findViewById(R.id.operatorTXT);
        amountTXT = (NCBTextInputLayout) findViewById(R.id.amountTXT);

        subscriberEdt = (CommonEditText) findViewById(R.id.subscriberEdt);
        amount = (CommonEditText) findViewById(R.id.amount);
        operatorSpn = (Spinner) findViewById(R.id.operatorSpn);

        submit = (Button) findViewById(R.id.submit);
       /* ((AppCompatActivity) getActivity()).setSupportActionBar(HomeActivity.toolbar);*/
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.dth_sname));
    }

    private boolean isValidateUI() {
        subscriberStr = subscriberEdt.getText().toString().trim();
        operatorStr = (String) operatorSpn.getSelectedItem();
        amountStr = amount.getText().toString().trim();
        if (TextUtils.isEmpty(subscriberStr)) {
            subscriberTXT.setError(getString(R.string.err_enter_subscriber_id));
            subscriberTXT.setErrorEnabled(true);
            subscriberTXT.requestFocusFromTouch();

            return false;
        } else if (operatorSpn.getSelectedItemPosition()==0) {
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

    private void getOperator(String providerType) {
        if (CommonUtil.isNetworkAvailable(context)) {
            MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
            operatorSubscription = service.getOperator(ApiConstants.MOBILE_SERVICES + providerType)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<OperatorModel>() {
                        @Override
                        public void onCompleted() {
                            // Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(OperatorModel operatorModel) {

                            listResults = (ArrayList<OperatorModel.ListResult>) operatorModel.getListResult();
                            if (!listResults.isEmpty()) {
                                operatorArrayList.add(getString(R.string.spn_select_circle));
                                for (int i = 0; i < listResults.size(); i++) {
                                    operatorArrayList.add(listResults.get(i).getName());
                                }
                            }
//                        ArrayAdapter<OperatorModel.ListResult> listResultArrayAdapter = new ArrayAdapter<OperatorModel.ListResult>(context,android.R.layout.simple_dropdown_item_1line,listResults);
//                        currentOperator.setAdapter(listResultArrayAdapter);
                            ArrayAdapter aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, operatorArrayList);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            operatorSpn.setAdapter(aa);

//                            GenericAdapter genericAdapter = new GenericAdapter(context, operatorModel.getListResult(), R.layout.adapter_single_item);
//                            genericAdapter.setAdapterOnClick(PayDTHFragment.this);
//                            operatorSpn.setAdapter(genericAdapter);
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (MY_CHILD_ACTIVITY):
                if (resultCode == Activity.RESULT_OK) {
                    startActivity(new Intent(DTHActivity.this, CompletePayment.class));
                }

                break;
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
