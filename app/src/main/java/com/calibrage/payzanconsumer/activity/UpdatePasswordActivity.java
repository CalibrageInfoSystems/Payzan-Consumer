package com.calibrage.payzanconsumer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.model.ChangePasswordModel;
import com.calibrage.payzanconsumer.framework.model.ChangePasswordResponseModel;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
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
 * Created by Calibrage11 on 11/8/2017.
 */

public class UpdatePasswordActivity extends BaseActivity {
    private CommonEditText oldPsdEdt, newPsdEdt, confirmPsdEdt;
    private NCBTextInputLayout oldPsdTIL, newPsdTIL, confirmPsdTIL;
    private Button saveBtn;
    private Subscription passwordSubscription;
    private Toolbar toolbar;
    private String oldPsdStr,newPsdStr,confirmPsdStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        setView();
        initView();
    }

    private void initView() {

        oldPsdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    oldPsdTIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newPsdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    newPsdTIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPsdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    confirmPsdTIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidateUi()) {
                    showProgressDialog();
                    changePassword();

                }

            }
        });

    }

    private void setView() {
        oldPsdEdt = (CommonEditText) findViewById(R.id.oldPsdEdt);
        newPsdEdt = (CommonEditText) findViewById(R.id.newPsdEdt);
        confirmPsdEdt = (CommonEditText) findViewById(R.id.confirmPsdEdt);
        oldPsdTIL = (NCBTextInputLayout) findViewById(R.id.oldPsdTIL);
        newPsdTIL = (NCBTextInputLayout) findViewById(R.id.newPsdTIL);
        confirmPsdTIL = (NCBTextInputLayout) findViewById(R.id.confirmPsdTIL);
        saveBtn = (Button) findViewById(R.id.saveBtn);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(getResources().getString(R.string.Update_Password));

    }

    private boolean isValidateUi() {
        oldPsdStr=oldPsdEdt.getText().toString().trim();
        newPsdStr=newPsdEdt.getText().toString().trim();
        confirmPsdStr=confirmPsdEdt.getText().toString().trim();

        if (TextUtils.isEmpty(oldPsdStr))
        {
            oldPsdTIL.setError("please enter old password");
            oldPsdTIL.setErrorEnabled(true);
            return false;
        }
        else if (TextUtils.isEmpty(newPsdStr))
        {
            newPsdTIL.setErrorEnabled(true);
            newPsdTIL.setError("enter new Password");
            return false;
        }
        else if (TextUtils.isEmpty(confirmPsdStr))
        {
            confirmPsdTIL.setError("enter confirm password");
            confirmPsdTIL.setErrorEnabled(true);
            return false;
        }
        return true;
    }

    private void changePassword() {
        JsonObject object = getPassword();
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        passwordSubscription = service.changePassword(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangePasswordResponseModel>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(UpdatePasswordActivity.this, "check", Toast.LENGTH_SHORT).show();
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

                    }

                    @Override
                    public void onNext(ChangePasswordResponseModel changePasswordResponseModel) {

                        Toast.makeText(UpdatePasswordActivity.this, "sucess", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private JsonObject getPassword() {
        ChangePasswordModel changePasswordModel = new ChangePasswordModel();
        changePasswordModel.setUserName(SharedPrefsData.getInstance(this).getUserName(this));
        changePasswordModel.setOldPassword(oldPsdEdt.getText().toString().trim());
        changePasswordModel.setNewPassword(newPsdEdt.getText().toString().trim());
        changePasswordModel.setConfirmPassword(confirmPsdEdt.getText().toString().trim());
        return new Gson().toJsonTree(changePasswordModel)
                .getAsJsonObject();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (passwordSubscription != null) {
            passwordSubscription.unsubscribe();
        }
    }
}
