package com.calibrage.payzanconsumer.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.LoginFragment;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.model.ChangePasswordModel;
import com.calibrage.payzanconsumer.framework.model.ChangePasswordResponseModel;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChangePasswordActivity extends BaseActivity{
    private NCBTextInputLayout currentpwdTXT, newpwdTXT, retypepwdTXT;
    private CommonEditText currentpwdEdt, newpwdEdt, retypepwdEdt;
    private CommonButton submit;
    private String currentpwdStr, newpwdStr, retypepwdStr;
    static Pattern pattern;
    static Matcher matcher;
    private Context context;
    private Subscription passwordSubscription;
    private ProgressDialog mProgressDialog;
    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,40}$";
    Toolbar toolbar;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_password);
        context= this;
        ShowActionBar(getString(R.string.change_password));

        setView();
        initView();
    }

    private void setView()
    {
        currentpwdEdt = (CommonEditText) findViewById(R.id.currentpwdEdt);
        newpwdEdt = (CommonEditText) findViewById(R.id.newpwdEdt);
        retypepwdEdt = (CommonEditText) findViewById(R.id.retypepwdEdt);
        currentpwdTXT = (NCBTextInputLayout) findViewById(R.id.currentpwdTXT);
        newpwdTXT = (NCBTextInputLayout) findViewById(R.id.newpwdTXT);
        retypepwdTXT = (NCBTextInputLayout) findViewById(R.id.retypepwdTXT);
        submit = (CommonButton) findViewById(R.id.submit);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitle(getString(R.string.change_password));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    private void initView() {
        currentpwdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    currentpwdTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        newpwdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    //   reg_password_til.setError("Ex:abcABC@123");
                    newpwdTXT.setErrorEnabled(false);
                }
                if(!CommonUtil.passwordValidateWithUppercase(newpwdEdt.getText().toString().trim())){
                    newpwdTXT.setErrorEnabled(true);
                    //  reg_password_til.setError("Example:- abcABC@123");
                    newpwdTXT.setError(getString(R.string.example_password));

                }else{
                    newpwdTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        retypepwdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    retypepwdTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                hideSoftKeyboard();
                if(isOnline()){
                    if (validateUI()){
                        changePassword();
                    }
                }else {
                    showToast(ChangePasswordActivity.this,getString(R.string.no_internet));
                }

            }
        });
    }

    private boolean validateUI() {
       // newpwdTXT.setError(getString(R.string.err_enter_new_password));
        if (TextUtils.isEmpty(currentpwdEdt.getText().toString().trim())) {
            currentpwdTXT.setError(getString(R.string.err_please_enter_current_password));
            currentpwdTXT.setErrorEnabled(true);
            currentpwdEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(newpwdEdt.getText().toString().trim())) {
            newpwdTXT.setErrorEnabled(true);
            newpwdTXT.setError(getString(R.string.err_enter_new_password));
            newpwdEdt.requestFocus();
            return false;
        } else if (!passwordValidate()) {
            newpwdTXT.setErrorEnabled(true);
            newpwdTXT.setError(getString(R.string.please_enter_new_valid_password));
            newpwdEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(retypepwdEdt.getText().toString().trim())) {
            retypepwdTXT.setError(getString(R.string.err_enter_retype_password));
            retypepwdTXT.setErrorEnabled(true);
            retypepwdEdt.requestFocus();
            return false;
        }else if(!newpwdEdt.getText().toString().trim().equalsIgnoreCase(retypepwdEdt.getText().toString().trim())){
            CommonUtil.displayDialogWindow(getString(R.string.err_psw_mismatch),alertDialog,context);
            retypepwdEdt.requestFocus();
            return false;
        }

        return true;
    }

//    public void hideDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing())
//            mProgressDialog.dismiss();
//    }

    public boolean passwordValidate() {

        String password = newpwdEdt.getText().toString().trim();

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        if (matcher.matches() == false)

            Toast.makeText(this, "Password Must contain: Minimum 8 characters, Maximum 40 characters," +
                    "one uppercase,one special character " + " and at least one number.", Toast.LENGTH_SHORT).show();
        return matcher.matches();

    }

    private void changePassword() {
        JsonObject object = getPassword();
        showProgressDialog();
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        passwordSubscription = service.changePassword(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangePasswordResponseModel>() {
                    @Override
                    public void onCompleted() {
                        //   Toast.makeText(getActivity(), "check", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
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
                        hideProgressDialog();
                        Toast.makeText(ChangePasswordActivity.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ChangePasswordResponseModel changePasswordResponseModel) {
                        hideProgressDialog();
                        CommonUtil.displayDialogWindow(changePasswordResponseModel.getEndUserMessage(),alertDialog,context);
                        if (changePasswordResponseModel.getIsSuccess()){
                            Toast.makeText(ChangePasswordActivity.this,changePasswordResponseModel.getEndUserMessage() , Toast.LENGTH_SHORT).show();
                            SharedPrefsData.getInstance(context).ClearData(context);
                            Intent intent = new Intent(ChangePasswordActivity.this,HomeActivity.class);
                           startActivity(intent);
                           finish();
                        }


                    }
                });
    }


    private JsonObject getPassword() {
        ChangePasswordModel changePasswordModel = new ChangePasswordModel();
        changePasswordModel.setUserName(SharedPrefsData.getInstance(context).getUserName(context));
        changePasswordModel.setOldPassword(currentpwdEdt.getText().toString().trim());
        changePasswordModel.setNewPassword(newpwdEdt.getText().toString().trim());
        changePasswordModel.setConfirmPassword(retypepwdEdt.getText().toString().trim());
        return new Gson().toJsonTree(changePasswordModel)
                .getAsJsonObject();
    }
}
