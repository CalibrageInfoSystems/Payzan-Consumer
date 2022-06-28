package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ChangePasswordFragment extends BaseFragment {
    public static final String TAG = ChangePasswordFragment.class.getSimpleName();
    private View view;
    private NCBTextInputLayout currentpwdTXT, newpwdTXT, retypepwdTXT;
    private CommonEditText currentpwdEdt, newpwdEdt, retypepwdEdt;
    private CommonButton submit;
    private String currentpwdStr, newpwdStr, retypepwdStr;
    static Pattern pattern;
    static Matcher matcher;
    private Context context;
    private Subscription passwordSubscription;
    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,20}$";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        context = this.getActivity();

        ((AppCompatActivity) getActivity()).setSupportActionBar(HomeActivity.toolbar);
        HomeActivity.toolbar.setTitle(getString(R.string.change_password));
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeTab();
            }
        });
        setViews();
        initView();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
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
        });

        return view;
    }

    private void closeTab() {

        HomeActivity.toolbar.setNavigationIcon(null);
        HomeActivity.toolbar.setTitle("");
        replaceFragment(getActivity(), MAIN_CONTAINER, new UserProfileHome(), TAG, ChangePasswordFragment.TAG);

    }


    private void setViews() {

        currentpwdEdt = (CommonEditText) view.findViewById(R.id.currentpwdEdt);
        newpwdEdt = (CommonEditText) view.findViewById(R.id.newpwdEdt);
        retypepwdEdt = (CommonEditText) view.findViewById(R.id.retypepwdEdt);
        currentpwdTXT = (NCBTextInputLayout) view.findViewById(R.id.currentpwdTXT);
        newpwdTXT = (NCBTextInputLayout) view.findViewById(R.id.newpwdTXT);
        retypepwdTXT = (NCBTextInputLayout) view.findViewById(R.id.retypepwdTXT);
        submit = (CommonButton) view.findViewById(R.id.submit);
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
                validateUI();
                changePassword();
            }
        });
    }

    private boolean validateUI() {
        currentpwdStr = currentpwdEdt.getText().toString().trim();
        newpwdStr = newpwdEdt.getText().toString().trim();
        retypepwdStr = retypepwdEdt.getText().toString().trim();

        if (TextUtils.isEmpty(currentpwdStr)) {
            currentpwdTXT.setError(getString(R.string.err_please_enter_current_password));
            currentpwdTXT.setErrorEnabled(true);
            return false;
        } else if (TextUtils.isEmpty(newpwdStr)) {
            newpwdTXT.setErrorEnabled(true);
            newpwdTXT.setError(getString(R.string.err_enter_new_password));
            return false;
        } else if (!passwordValidate()) {
            newpwdTXT.setErrorEnabled(true);
            newpwdTXT.setError(getString(R.string.please_enter_new_valid_password));
            return false;
        } else if (TextUtils.isEmpty(retypepwdStr)) {
            retypepwdTXT.setError(getString(R.string.err_enter_retype_password));
            retypepwdTXT.setErrorEnabled(true);
            return false;
        }

        return true;
    }

    public boolean passwordValidate() {

        String password = newpwdEdt.getText().toString().trim();

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        if (matcher.matches() == false)

            Toast.makeText(getContext(), "Password Must contain: Minimum 4 characters,Maximum 20 characters," +
                    "one uppercase,one special characters " + " and atleast one number.", Toast.LENGTH_SHORT).show();
        return matcher.matches();

    }

    private void changePassword() {
        JsonObject object = getPassword();
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        passwordSubscription = service.changePassword(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangePasswordResponseModel>() {
                    @Override
                    public void onCompleted() {
                     //   Toast.makeText(getActivity(), "check", Toast.LENGTH_SHORT).show();
                        hideDialog();
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
                        hideDialog();
//                        Toast.makeText(getActivity(), getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ChangePasswordResponseModel changePasswordResponseModel) {
                        hideDialog();
                        if (changePasswordResponseModel.getIsSuccess())
                        Toast.makeText(getActivity(),changePasswordResponseModel.getEndUserMessage() , Toast.LENGTH_SHORT).show();

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
