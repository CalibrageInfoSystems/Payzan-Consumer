package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.CompletePayment;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.model.PostWalletModel;
import com.calibrage.payzanconsumer.framework.model.WalletBalanceResponce;
import com.calibrage.payzanconsumer.framework.model.WalletResponse;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.DecimalDigitsInputFilter;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Calibrage11 on 9/25/2017.
 */

public class AddMoneyToWallet extends BaseFragment {
    public static final String TAG = AddMoneyToWallet.class.getSimpleName();
    private EditText enterMoneyEdt, enterpromocodeEdt;
    private Button submit;
    private Toolbar r;
    private View rootView;
    private Context context;
    private AlertDialog alertDialog;
    private Subscription mRegisterSubscription;
    private TextView addHundTxt, addfiveTxt, addthouTxt;
    private int addMoney;
    private String addMoneyStr;
    private NCBTextInputLayout addMoneyTxt;
    private Subscription mGetTransactions;
    int WaletTypeID = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_add_money_to_wallet, container, false);
        context = this.getActivity();
        WaletTypeID = SharedPrefsData.getInstance(getActivity()).getIntFromSharedPrefs("WalletIDValue");
        setHasOptionsMenu(true);

        setViews();
        initViews();


        return rootView;
    }

    private void setViews() {
        enterMoneyEdt = (EditText) rootView.findViewById(R.id.amount);
        addHundTxt = (TextView) rootView.findViewById(R.id.addHundTxt);
        addfiveTxt = (TextView) rootView.findViewById(R.id.addfiveTxt);
        addthouTxt = (TextView) rootView.findViewById(R.id.addthouTxt);
        enterpromocodeEdt = (EditText) rootView.findViewById(R.id.promocode);
        submit = (Button) rootView.findViewById(R.id.submit);
        addMoneyTxt = (NCBTextInputLayout) rootView.findViewById(R.id.addmoneytxt);

    }


    private void initViews() {

        addHundTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                addMoney(100);
            }
        });
        addfiveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                addMoney(500);
            }
        });
        addthouTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                addMoney(1000);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                if (isOnline(context)) {
                    if (checkUserLoginStatus(TAG) && validateUi()) {
                        //  showDialog(getActivity(), "Please Wait Loading... ");
                        // addWallet();
                        Intent intent = new Intent(context, CompletePayment.class);
                        intent.putExtra(CommonConstants.AMOUMNT_ADD_WALLET_KEY, enterMoneyEdt.getText().toString());
                        intent.putExtra(CommonConstants.CHECK_SCREEN_KEY, CommonConstants.CHECK_SCREEN_ADD_WALLET_VALUE);
                        startActivity(intent);
                    }
                } else {
                    showToast(context, getString(R.string.no_internet));
                }


            }
        });

        //    enterMoneyEdt.addTextChangedListener(new NumberTextWatcher(enterMoneyEdt,"#,###"));

        enterMoneyEdt.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 2)});
        enterMoneyEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    addMoneyTxt.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String x = "";
                x = s.toString();
                if (x.startsWith("0")) {
                    enterMoneyEdt.setText("");
                }else if(x.startsWith(".")){
                    enterMoneyEdt.setText("");
                }
                if (!enterMoneyEdt.getText().toString().equalsIgnoreCase(""))
                    if (Double.parseDouble(enterMoneyEdt.getText().toString()) <= 5000.00) {
                        addMoneyTxt.setErrorEnabled(false);
                    } else {
                        addMoneyTxt.setErrorEnabled(true);
                        addMoneyTxt.setError(getString(R.string.err_amt_max_limit));
                    }

            }
        });

    }

    private boolean validateUi() {
        addMoneyStr = enterMoneyEdt.getText().toString().trim();
        if (TextUtils.isEmpty(addMoneyStr)) {
            addMoneyTxt.setErrorEnabled(true);
            addMoneyTxt.setError(getString(R.string.err_enter_amount));
            enterMoneyEdt.requestFocus();
            return false;
        }
        if (Double.parseDouble(addMoneyStr) > 5000) {
            addMoneyTxt.setErrorEnabled(true);
            addMoneyTxt.setError(getString(R.string.err_amt_max_limit));
            CommonUtil.displayDialogWindow(getString(R.string.err_amt_max_limit), alertDialog, context);
            return false;
        }
        return true;
    }

    private void addMoney(int amount) {

        try {
            int value = Integer.parseInt("0" + enterMoneyEdt.getText().toString());
            addMoney = value + amount;
        } catch (Exception e) {

        }
        enterMoneyEdt.setText(String.valueOf(addMoney));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    public void getTransactions(int walletId) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        String url = ApiConstants.BASE_URL + ApiConstants.BalenceBYID + walletId;
        mGetTransactions = service.GetUserBalanceByID(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WalletBalanceResponce>() {
                    @Override
                    public void onCompleted() {
                        //Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(context, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }

                    @Override
                    public void onNext(WalletBalanceResponce transactionResponseModel) {
                        hideDialog();

                        TransactionMainFragment.walletBalanceTxt.setText(("Wallet Balance: " + transactionResponseModel.getResult().getBalance()));
                        Long val = Long.valueOf(transactionResponseModel.getResult().getBalance().toString());
                        SharedPrefsData.getInstance(getActivity()).saveWalletIdMoney(context, val);
                        //SharedPrefsData.getInstance(getActivity()).saveWalletBalance(context, String.valueOf(transactionResponseModel.getResult().getBalance()));
                        // Toast.makeText(context, "Wallet Balance: " + transactionResponseModel.getResult().getBalance(), Toast.LENGTH_SHORT).show();


                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGetTransactions != null) {
            mGetTransactions.unsubscribe();
        }
        if (mRegisterSubscription != null) {
            mRegisterSubscription.unsubscribe();
        }


    }

    private JsonObject postWalletObject() {
        PostWalletModel postWalletModel = new PostWalletModel();
        postWalletModel.setAmount(Double.parseDouble(enterMoneyEdt.getText().toString()));
//        postWalletModel.setUpdatedByUserId(SharedPrefsData.getInstance(context).getUserId(context));
//        postWalletModel.setCreatedByUserId(SharedPrefsData.getInstance(context).getUserId(context));
        postWalletModel.setWalletId(SharedPrefsData.getInstance(context).getWalletId(context));
        postWalletModel.setReasonTypeId(32);
        postWalletModel.setTransactionTypeId(29);

        return new Gson().toJsonTree(postWalletModel)
                .getAsJsonObject();
    }


}
