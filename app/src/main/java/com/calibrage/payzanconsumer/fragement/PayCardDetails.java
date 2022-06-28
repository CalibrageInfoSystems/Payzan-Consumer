package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.controls.CreditCardEditText;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentToActivityInteractionListener;
import com.calibrage.payzanconsumer.framework.model.PostWalletModel;
import com.calibrage.payzanconsumer.framework.model.WalletResponse;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
import com.devmarvel.creditcardentry.library.CreditCardForm;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Calibrage11 on 12/21/2017.
 */

public class PayCardDetails extends BaseFragment {
    public static final String TAG = PayCardDetails.class.getSimpleName();
    View view;
    private CommonButton submit;
    private NCBTextInputLayout cardNumberTxt, cardHolderNameTxt, cardLabelTxt, monthTxt, yearTxt, cvvTIL;
    private CommonEditText cardLabelEdt, cardHolderNameEdt, cardNumberEdt, cvvEdt;
    private Spinner monthSp, yearSp;
    private CheckBox saveCheck;
    private String cardLabelStr, cardHolderNameStr, cardNumberStr, monthStr, yearStr;
    private Integer currentYear, currentMonth;
    Calendar mCalendar = Calendar.getInstance();
    private ArrayList<String> yearsList = new ArrayList<>();
    private Context context;
    private ArrayList<String> listOfPattern = new ArrayList<String>();
    private CreditCardForm linearLayout;
    private CreditCardForm form;
    private CreditCardEditText creditCardNumberEdt;
    private Subscription mRegisterSubscription;
    private String moneyStr;
    private int paymentVia;
    private AlertDialog alertDialog;
    private OnChildFragmentToActivityInteractionListener mListener;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            paymentVia = bundle.getInt(CommonConstants.CHECK_SCREEN_KEY, 0);
            moneyStr = bundle.getString(CommonConstants.AMOUNT_KEY);
        }

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_payment_carddetails, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);
        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        listOfPattern.add(ptDiscover);
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);

        initViews();
        setViews();

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        return view;
    }


    private void initViews() {
        context = this.getActivity();
        linearLayout = (CreditCardForm) view.findViewById(R.id.credit_card_form);
        creditCardNumberEdt = (CreditCardEditText) view.findViewById(R.id.credit_test);

        form = new CreditCardForm(context);
        linearLayout.addView(form);

        //  cardNumberEdt = (CommonEditText) view.findViewById(R.id.cardNoEdt);
        cardLabelEdt = (CommonEditText) view.findViewById(R.id.cardLabelEdt);
        cardHolderNameEdt = (CommonEditText) view.findViewById(R.id.cardHolderEdt);
        cvvEdt = (CommonEditText) view.findViewById(R.id.cvvEdt);

        cardNumberTxt = (NCBTextInputLayout) view.findViewById(R.id.Card_no_TIL);
        cardHolderNameTxt = (NCBTextInputLayout) view.findViewById(R.id.card_holder_TIL);
        cardLabelTxt = (NCBTextInputLayout) view.findViewById(R.id.card_label_TIL);
        cvvTIL = (NCBTextInputLayout) view.findViewById(R.id.cvvTIL);

        submit = (CommonButton) view.findViewById(R.id.payBtn);
        monthSp = (Spinner) view.findViewById(R.id.monthSp);
        yearSp = (Spinner) view.findViewById(R.id.yearSp);
        saveCheck = (CheckBox) view.findViewById(R.id.saveRB);


       /* String[] monthlist = getResources().getStringArray(R.array.month_arrays);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(context, R.layout.month_layout, R.id.montxt, monthlist);
        monthAdapter.setDropDownViewResource(R.layout.month_layout);
        monthSp.setAdapter(monthAdapter);
        monthSp.setSelection(setCurrentMonthSpinner(context));*/


      /*  // String[] yearlist = getResources().getStringArray(R.array.year_arrays);
        ArrayList<String> years = new ArrayList<String>();
        int calendar = mCalendar.get(Calendar.YEAR);
        for (int i = calendar; i <= 2550; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, years);
        //  yearAdapter.setDropDownViewResource(R.layout.year_layout);
        yearSp.setAdapter(yearAdapter);*/

        // cardNumberEdt.addTextChangedListener(new FourDigitCardFormatWatcher());
        creditCardNumberEdt.addTextChangedListener(new FourDigitCardFormatWatcher());
        currentYear = mCalendar.get(Calendar.YEAR);
        currentMonth = mCalendar.get(Calendar.MONTH) + 1;
        yearsList.add("YY");
        for (int i = 0; i <= 10; i++)
            yearsList.add("" + (currentYear + i));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_text_black, yearsList);
        yearSp.setAdapter(adapter);

        String[] monthList = getResources().getStringArray(R.array.month);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(context, R.layout.spinner_text_black, monthList);
        monthSp.setAdapter(monthAdapter);


    }

    private void setViews() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateUi()) {
                    if (isOnline(context)) {
                        if (paymentVia == CommonConstants.CHECK_SCREEN_ADD_WALLET_VALUE) {
                            addWallet();
                        }

                    }

                }
                //startActivity(new Intent(context, SaveCardActivity.class));
            }
        });


//        cardNumberEdt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() > 0) {
//                    cardNumberTxt.setErrorEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                String ccNum = editable.toString().trim();
//                for (String p : listOfPattern) {
//                    if (ccNum.matches(p)) {
//                        Log.d("DEBUG", "afterTextChanged : discover");
//                        showToast(context, "working");
//                        //                       cardNumberEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visa, 0);
//                        break;
//                    }
//                }
//
//            }
//        });
        cvvEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    cvvTIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String ccNum = editable.toString().trim();

            }
        });
        cardHolderNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    cardHolderNameTxt.setErrorEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        monthSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CommonUtil.hideSoftKeyboard((AppCompatActivity) getActivity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        yearSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void addWallet() {
        showDialog(getActivity(), "Please Wait Loading... ");
        JsonObject object = postWalletObject();
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        mRegisterSubscription = service.UserAddWallet(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WalletResponse>() {
                    @Override
                    public void onCompleted() {
                        //  Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
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
                    }
                    @Override
                    public void onNext(WalletResponse walletResponse) {
                        hideDialog();
                        if (walletResponse.getIsSuccess()) {
                            CommonConstants.WALLETMONEY = String.valueOf(walletResponse.getResult().getBalance());
//                            Toast.makeText(context, walletResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                            CommonUtil.displayDialogWindow("" + walletResponse.getEndUserMessage(), alertDialog, context);
                            Intent intent = new Intent(context, HomeActivity.class);
                            context.startActivity(intent);
                            getActivity().finish();
                        } else {
                            Toast.makeText(context, walletResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private JsonObject postWalletObject() {
        PostWalletModel postWalletModel = new PostWalletModel();
        try {
            postWalletModel.setAmount(Double.parseDouble(String.valueOf(moneyStr)));
        } catch (Exception e) {

        }

//        postWalletModel.setUpdatedByUserId(SharedPrefsData.getInstance(context).getUserId(context));
//        postWalletModel.setCreatedByUserId(SharedPrefsData.getInstance(context).getUserId(context));
        postWalletModel.setWalletId(SharedPrefsData.getInstance(context).getWalletId(context));
        postWalletModel.setReasonTypeId(32);
        postWalletModel.setTransactionTypeId(29);
        postWalletModel.setCardNumber(creditCardNumberEdt.getText().toString().trim().replaceAll(" ", ""));
        postWalletModel.setCardExpiry(monthSp.getSelectedItem().toString() + yearSp.getSelectedItem().toString().substring(2));
        postWalletModel.setNameOnCard(cardHolderNameEdt.getText().toString().trim());
        postWalletModel.setCVV(Integer.parseInt(cvvEdt.getText().toString()));
        postWalletModel.setCurrency("LKR");

        return new Gson().toJsonTree(postWalletModel)
                .getAsJsonObject();
    }

    private boolean validateUi() {
        if (TextUtils.isEmpty(creditCardNumberEdt.getText().toString().trim())) {
            cardNumberTxt.requestFocus();
            cardNumberTxt.setError(getString(R.string.err_enter_card_number));
            cardNumberTxt.setErrorEnabled(true);
            return false;
        }
        if (!(isValidCardNumber())) {
            cardNumberTxt.requestFocus();
            cardNumberTxt.setError(getString(R.string.err_enter_valid_card_number));
            cardNumberTxt.setErrorEnabled(true);
            return false;
        }

        if (CommonUtil.isEmptySpinner(monthSp)) {
            InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(monthSp.getWindowToken(), 0);
            showToast(context, getResources().getString(R.string.err_enter_month));
            monthSp.requestFocus();
            return false;
        }
        if (CommonUtil.isEmptySpinner(yearSp)) {
            InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(yearSp.getWindowToken(), 0);
            showToast(context, getResources().getString(R.string.err_enter_year));
            yearSp.requestFocus();
            return false;
        }
        if (currentYear == Integer.parseInt(yearSp.getSelectedItem().toString())) {
            if (currentMonth > Integer.parseInt(monthSp.getSelectedItem().toString())) {
                showToast(context, getResources().getString(R.string.err_enter_valid_month));
                return false;
            }

        }
        if (TextUtils.isEmpty(cvvEdt.getText().toString().trim())) {
            cvvTIL.setError(getString(R.string.enter_cvv));
            cardNumberTxt.setErrorEnabled(true);
            return false;
        }
        if (cvvEdt.getText().toString().trim().length() != 3) {
            cvvTIL.setError(getString(R.string.err_enter_valid_cvv));
            cardNumberTxt.setErrorEnabled(true);
            return false;
        }
        if (TextUtils.isEmpty(cardHolderNameEdt.getText().toString().trim())) {
            cardHolderNameTxt.setError(getString(R.string.err_enter_cardholder_name));
            cardHolderNameTxt.requestFocus();
            return false;
        }


        return true;
    }

    private boolean isValidCardNumber() {
        String target = creditCardNumberEdt.getText().toString().trim();
        if (target.length() != 19) {
            return false;
        }
        return true;
    }

    private class FourDigitCardFormatWatcher implements TextWatcher {

        // Change context to what you want... ' ', '-' etc..
        private static final char space = ' ';
        private boolean isDelete;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            if (count == 0)
                isDelete = false;
            else
                isDelete = true;

            if (s.length() > 0) {
                cardNumberTxt.setErrorEnabled(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove spacing char
//            if (s.length() > 0 && (s.length() % 5) == 0) {
//                final char c = s.charAt(s.length() - 1);
//                if (space == c) {
//                    s.delete(s.length() - 1, s.length());
//                }
//            }
//            // Insert char where needed.
//            if (s.length() > 0 && (s.length() % 5) == 0) {
//                char c = s.charAt(s.length() - 1);
//                // Only if its a digit where there should be a space we insert a space
//                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
//                    s.insert(s.length() - 1, String.valueOf(space));
//                }
//            }

            String source = s.toString();
            int length = source.length();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(source);

            if (length > 0 && length % 5 == 0) {
                if (isDelete)
                    stringBuilder.deleteCharAt(length - 1);
                else
                    stringBuilder.insert(length - 1, " ");

                creditCardNumberEdt.setText(stringBuilder);
                creditCardNumberEdt.setSelection(creditCardNumberEdt.getText().length());
            }

        }
    }


}