package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.interfaces.DrawableClickListener;
import com.calibrage.payzanconsumer.framework.model.SendMoneyModel;
import com.calibrage.payzanconsumer.framework.model.SendMoneyResponseModel;
import com.calibrage.payzanconsumer.framework.model.UserWalletHistory;
import com.calibrage.payzanconsumer.framework.model.WalletBalanceResponce;
import com.calibrage.payzanconsumer.framework.model.WalletResponse;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
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

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Calibrage11 on 10/20/2017.
 */

public class SendMoneyToWallet extends BaseFragment {
    public static final String TAG = SendMoneyToWallet.class.getSimpleName();
    private View rootView;
    private Context context;
    private NCBTextInputLayout mobileNumberTXT, amountTXT, commentTXT;
    private CommonEditText mobileEdt, amount, commentEdt;
    static final int PICK_CONTACT = 1000;
    private CommonButton submitBtn;
    private Subscription sendMoneySubscription;
    private Subscription mGetTransactions;
    private String hasPhone;
    private AlertDialog alertDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_send_money, container, false);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        context = this.getActivity();
        setViews();
        initViews();
        Gson gson = new Gson();
        String json = gson.toJson(new WalletResponse());
        Log.d(TAG, "onCreateView: " + json);
        return rootView;
    }


    private void setViews() {
        mobileNumberTXT = (NCBTextInputLayout) rootView.findViewById(R.id.mobileNumberTXT);
        amountTXT = (NCBTextInputLayout) rootView.findViewById(R.id.amountTXT);
        commentTXT = (NCBTextInputLayout) rootView.findViewById(R.id.commentTXT);
        mobileEdt = (CommonEditText) rootView.findViewById(R.id.mobileEdt);
        amount = (CommonEditText) rootView.findViewById(R.id.amount);
        commentEdt = (CommonEditText) rootView.findViewById(R.id.commentEdt);
        submitBtn = (CommonButton) rootView.findViewById(R.id.submit);
    }

    private void initViews() {
       /* String code = "+94";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mobileEdt.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, getResources().getDrawable(R.drawable.prepaid_contacts_icon), null);
            mobileEdt.setCompoundDrawablePadding(code.length() * 10);
        }*/
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
                }else if(x.startsWith(".")){
                    amount.setText("");
                }
                if (!amount.getText().toString().equalsIgnoreCase(""))
                    if (Double.parseDouble(amount.getText().toString()) <= 5000.00) {
                        amountTXT.setErrorEnabled(false);
                    } else {
                        amountTXT.setErrorEnabled(true);
                        amountTXT.setError(getString(R.string.err_amt_max_limit));
                    }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                if (isOnline(context)) {
                    if (checkUserLoginStatus(TAG) && validateUi()) {
                        showDialog(getActivity(), "Please Wait Loading... ");
                        sendMoneyRequest();
                    }
                } else {
                    showToast(context, getString(R.string.no_internet));
                }


            }
        });


    }

    private boolean validateUi() {
        String mblNumber= SharedPrefsData.getInstance(context).getStringFromSharedPrefs("PhoneNumber");
       if (TextUtils.isEmpty(mobileEdt.getText().toString().trim())) {
            mobileNumberTXT.setErrorEnabled(true);
            mobileNumberTXT.setError(getString(R.string.err_enter_mobile_no));
            mobileEdt.requestFocus();
            return false;
        }if((mobileEdt.getText().toString().equals(mblNumber)))
        {
            mobileNumberTXT.setErrorEnabled(true);
            mobileNumberTXT.setError(getString(R.string.err_own_account_number));
            mobileEdt.requestFocus();
            return false;
        }
        else if (!isValidPhone()) {
            mobileNumberTXT.setErrorEnabled(true);
            mobileNumberTXT.setError(getString(R.string.err_enter_valid_mobile_no));
            return false;
        } else if (TextUtils.isEmpty(amount.getText().toString().trim())) {
            amountTXT.setErrorEnabled(true);
            amountTXT.setError(getString(R.string.err_enter_amount));
            amount.requestFocus();
            return false;
        } else if (!CommonUtil.isValidAmout(amount.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), R.string.toast_amount_is_not_valid, Toast.LENGTH_SHORT).show();
            return false;
        } else if (Double.parseDouble(amount.getText().toString().trim()) > 5000) {
            amountTXT.setErrorEnabled(true);
            amountTXT.setError(getString(R.string.err_amt_max_limit));
            CommonUtil.displayDialogWindow(getString(R.string.err_amt_max_limit), alertDialog, context);
            return false;
        }
        return true;
    }

    private boolean isValidPhone() {
        String target = mobileEdt.getText().toString().trim();
        if (target.length() < 10 || target.length() > 13) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    private void sendMoneyRequest() {
        hideDialog();
        JsonObject object = postJsonObject();
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        sendMoneySubscription = service.sendMoneyRequest(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SendMoneyResponseModel>() {
                    @Override
                    public void onCompleted() {
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
                    }

                    @Override
                    public void onNext(SendMoneyResponseModel sendMoneyResponseModel) {
                        hideDialog();
                        // Toast.makeText(getActivity(), sendMoneyResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        if (sendMoneyResponseModel.getIsSuccess()) {
                            mobileEdt.setText("");
                            amount.setText("");
                            commentEdt.setText("");
                        }

                        int values = SharedPrefsData.getInstance(getActivity()).getIntFromSharedPrefs("WalletIDValue");
                        //Toast.makeText(context, sendMoneyResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        CommonUtil.displayDialogWindow(sendMoneyResponseModel.getEndUserMessage(), alertDialog, context);
                        getTransactions(values);


                    }
                });
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
                    public void onNext(WalletBalanceResponce transactionResponseModel) {
                        hideDialog();

                     //   Long val = Long.valueOf(String.valueOf(transactionResponseModel.getResult().getBalance()));
                       // SharedPrefsData.getInstance(getActivity()).saveWalletIdMoney(context, val);
                        SharedPrefsData.getInstance(getActivity()).saveWalletBalance(context, String.valueOf(transactionResponseModel.getResult().getBalance()));
                        TransactionMainFragment.walletBalanceTxt.setText(("Wallet Balance: " + String.valueOf(transactionResponseModel.getResult().getBalance())));
                    }
                });
    }

    private JsonObject postJsonObject() {
        SendMoneyModel sendMoney = new SendMoneyModel();
        sendMoney.setRecieverUserName(mobileEdt.getText().toString());
        UserWalletHistory userWalletHistory = new UserWalletHistory();
        userWalletHistory.setAmount(Double.valueOf(amount.getText().toString()));
        // userWalletHistory.setCreated("");
        // userWalletHistory.setCreatedBy(SharedPrefsData.getInstance(context).getUserId(context));
        userWalletHistory.setId(0);
        // userWalletHistory.setModified("");
        // userWalletHistory.setModifiedBy(SharedPrefsData.getInstance(context).getUserId(context));
        userWalletHistory.setReasonTypeId(10);
        userWalletHistory.setTransactionTypeId(9);
        userWalletHistory.setIsActive(true);
        userWalletHistory.setWalletId(SharedPrefsData.getInstance(context).getWalletId(context));
        sendMoney.setUserWalletHistory(userWalletHistory);
        return new Gson().toJsonTree(sendMoney).getAsJsonObject();
    }

/*
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = context.getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString(phoneIndex);
            // Set the value to the textviews
            if (!hasPhone.toString().trim().contains("+91") || !hasPhone.toString().trim().contains("+94"))
                mobileEdt.setText(hasPhone.trim().replace("+91", "").replace(" ", "").replace("+94", ""));
            else
                mobileEdt.setText(hasPhone.trim().replace("+91", "").replace(" ", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = context.getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        /*contactPicked(data);*/
                        hasPhone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        /*showToast(context, hasPhone);*/
                    /*    if (!hasPhone.toString().trim().contains("+91") || !hasPhone.toString().trim().contains("+94"))
                            mobileEdt.setText(hasPhone.trim().replace("+91", "").replace(" ", "").replace("+94", ""));
                        else
                            mobileEdt.setText(hasPhone.trim().replace("+91", "").replace(" ", ""));*/

                        hasPhone = hasPhone.replace(" ", "");
                        hasPhone = hasPhone.replace("  ", "");
                        hasPhone = hasPhone.replace("-", "");
                        hasPhone = hasPhone.replace("(", "");
                        hasPhone = hasPhone.replace(")", "");
                        hasPhone = hasPhone.replace("+", "");
                        hasPhone.trim();

                        if (hasPhone.length() > 10) {
                            hasPhone = hasPhone.substring(hasPhone.length() - 10);
                        }
                        mobileEdt.setText(hasPhone.toString().trim());
                    }

                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGetTransactions != null) {
            mGetTransactions.unsubscribe();
        }
        if (sendMoneySubscription != null) {
            sendMoneySubscription.unsubscribe();
        }
    }


}
