package com.calibrage.payzanconsumer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

public class ElectricityActivity extends BaseActivity {

    public static final String TAG = ElectricityActivity.class.getSimpleName();
    private Context context;
    private NCBTextInputLayout districtTXT, serviceNoTXT, amountTXT;
    private CommonEditText amountEdt, ServiceNEdt;
    private Spinner districtSpn;
    private Subscription operatorSubscription;
    private ArrayList<OperatorModel.ListResult> listResults;
    private Button submit;
    private AlertDialog alertDialog;
    private String districtStr,ServiceStr,amountStr;
    private ArrayList<String> districtArrayList = new ArrayList<>();
    private int districtId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_electrictybill);
        ShowActionBar(getString(R.string.electricity_sname));

        setViews();
        initViews();

        getOperator("9");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (MY_CHILD_ACTIVITY):
                if (resultCode == Activity.RESULT_OK) {
                    startActivity(new Intent(ElectricityActivity.this,CompletePayment.class));
                }

                break;
        }
    }

    private void setViews() {
        context=ElectricityActivity.this;
        districtTXT = (NCBTextInputLayout) findViewById(R.id.districtTXT);
        serviceNoTXT = (NCBTextInputLayout) findViewById(R.id.serviceNoTXT);
        amountTXT = (NCBTextInputLayout) findViewById(R.id.amountTXT);
        amountEdt = (CommonEditText) findViewById(R.id.amountEdt);
        ServiceNEdt = (CommonEditText) findViewById(R.id.ServiceNEdt);
        districtSpn = (Spinner) findViewById(R.id.districtSpn);
        submit=(Button)findViewById(R.id.submit);
       /* setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(HomeActivity.toolbar);*/
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.electricity_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));

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
        districtSpn.setOnTouchListener(new View.OnTouchListener() {

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
        districtSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (districtSpn.getSelectedItemPosition()!=0)
                {
                    districtId=listResults.get(districtSpn.getSelectedItemPosition()-1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ServiceNEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    serviceNoTXT.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        amountEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    amountTXT.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserLoginStatus(ElectricityActivity.this)){
                    hideSoftKeyboard();
                    if(isValidateUi()){
                        startActivity(new Intent(ElectricityActivity.this,CompletePayment.class));
                    }
                }

            }
        });

    }

    private boolean isValidateUi() {
        districtStr  = (String) districtSpn.getSelectedItem();
        ServiceStr  =ServiceNEdt.getText().toString().trim();
        amountStr  =amountEdt.getText().toString().trim();
        if (districtSpn.getSelectedItemPosition()==0) {
            Toast.makeText(getApplicationContext(), R.string.err_please_select_district, Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(ServiceStr)) {
            serviceNoTXT.setError(getString(R.string.err_enter_service_no));
            serviceNoTXT.setErrorEnabled(true);
            ServiceNEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(amountStr)) {
            amountTXT.setError(getString(R.string.err_enter_amount));
            amountTXT.setErrorEnabled(true);
            amountEdt.requestFocus();
            return false;
        }
        else if (Double.parseDouble(amountStr)>5000) {
            amountTXT.setErrorEnabled(true);
            amountTXT.setError(getString(R.string.err_amt_max_limit));
            CommonUtil.displayDialogWindow(getString(R.string.err_amt_max_limit),alertDialog,context);
            amountEdt.requestFocusFromTouch();
            return false;
        }
        return true;
    }

    private void getOperator(String providerType) {
        if(CommonUtil.isNetworkAvailable(context)){
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
                            Toast.makeText(context, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(OperatorModel operatorModel) {

                            listResults = (ArrayList<OperatorModel.ListResult>) operatorModel.getListResult();
                            if (!listResults.isEmpty()) {
                                districtArrayList.add(getString(R.string.spn_select_district));
                                for (int i = 0; i < listResults.size(); i++) {
                                    districtArrayList.add(listResults.get(i).getName());
                                }
                            }
//                        ArrayAdapter<OperatorModel.ListResult> listResultArrayAdapter = new ArrayAdapter<OperatorModel.ListResult>(context,android.R.layout.simple_dropdown_item_1line,listResults);
//                        currentOperator.setAdapter(listResultArrayAdapter);
                            ArrayAdapter aa = new ArrayAdapter(context,android.R.layout.simple_spinner_item,districtArrayList);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            districtSpn.setAdapter(aa);

//                        GenericAdapter genericAdapter = new GenericAdapter(context, operatorModel.getListResult(), R.layout.adapter_single_item);
//                        genericAdapter.setAdapterOnClick(PayElectrictyFragment.this);
//                        districtSpn.setAdapter(genericAdapter);
                        }
                    });
        }}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (operatorSubscription != null) {
            operatorSubscription.unsubscribe();
        }
    }
}
