package com.calibrage.payzanconsumer.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.HomeFragment;
import com.calibrage.payzanconsumer.fragement.LoginFragment;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentToActivityInteractionListener;
import com.calibrage.payzanconsumer.framework.model.RegisterModel;
import com.calibrage.payzanconsumer.framework.model.ResponseModel;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.calibrage.payzanconsumer.framework.util.PayZanEnums;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.calibrage.payzanconsumer.framework.util.CommonUtil.isValidEmail;

public class SigUpActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    public static final String TAG = SigUpActivity.class.getSimpleName();
    private CommonEditText reg_mobile, reg_email, reg_password, reg_confirm_password;
    private LoginButton loginButton;
    private Button fbBtn, btnRegister;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private static final int RC_SIGN_IN = 007;
    private AlertDialog alertDialog;
    // private Subscription mRegisterSubscription;
    private NCBTextInputLayout reg_mobile_til, reg_email_til, reg_password_til, reg_confirm_password_til;
    private Subscription mRegisterSubscription;
    private View rootView;
    private Context context;
    FragmentManager fragmentManager;
    public static Toolbar toolbar;
    private TextView terms_comditions, linkToLogin;
    private OnChildFragmentToActivityInteractionListener mActivityListener;
    private String reg_mobileStr, reg_emailStr, reg_passwordStr, reg_confirm_passwordStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // ShowActionBar(getString(R.string.sign_up));
        context = this;
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login);
        fbBtn = (Button) findViewById(R.id.fbBtn);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        signInButton = (SignInButton) findViewById(R.id.btn_sign_in);
        reg_mobile = (CommonEditText) findViewById(R.id.reg_mobile);
        reg_email = (CommonEditText) findViewById(R.id.reg_email);
        reg_password = (CommonEditText) findViewById(R.id.reg_password);
        reg_confirm_password = (CommonEditText) findViewById(R.id.reg_confirm_password);
        reg_mobile_til = (NCBTextInputLayout) findViewById(R.id.reg_mobile_til);
        reg_email_til = (NCBTextInputLayout) findViewById(R.id.reg_email_til);
        reg_password_til = (NCBTextInputLayout) findViewById(R.id.reg_password_til);
        reg_confirm_password_til = (NCBTextInputLayout) findViewById(R.id.reg_confirm_password_til);
        terms_comditions = (TextView) findViewById(R.id.terms_comditions);
        linkToLogin = (TextView) findViewById(R.id.linkToLogin);
        fragmentManager = getSupportFragmentManager();
        IntiateGoogleApi();
        SpannableString ss = new SpannableString(getResources().getString(R.string.terms_and_conditions_signup));
        SpannableString ssToLogin = new SpannableString(getResources().getString(R.string.already_have_account));

        setSupportActionBar(toolbar);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.sign_up));
        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment.txt_Email.setText("");
                LoginFragment.txt_password.setText("");
                LoginFragment.inp_password.setErrorEnabled(false);
                LoginFragment.inp_email.setErrorEnabled(false);
                HomeActivity.toolbar.setTitle(getResources().getString(R.string.login_sname));
                HomeActivity.toolbar.setNavigationIcon(null);
                finish();

            }
        });
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // startActivity(new Intent(MyActivity.this, NextActivity.class));
                //  Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // startActivity(new Intent(MyActivity.this, NextActivity.class));
                //  Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan1, 27, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan2, 51, 66, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpanToLogin = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
//                Intent intent = new Intent(SigUpActivity.this,HomeActivity.class);
//                intent.putExtra(CommonUtil.MOVE_TO_LOGIN, "Login");
//                startActivity(intent);
                HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
                HomeActivity.toolbar.setTitle(getResources().getString(R.string.login_sname));
                HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
                LoginFragment.inp_email.setErrorEnabled(false);
                finish();
                //  Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                // replaceFragment(SigUpActivity.this, MAIN_CONTAINER, new LoginFragment(), TAG, LoginFragment.TAG);


            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
      /*  if (SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang") == CommonConstants.English) {
            ssToLogin.setSpan(clickableSpanToLogin, 23, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else if (SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang") == CommonConstants.Sinhala) {
            ssToLogin.setSpan(clickableSpanToLogin, 23, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang") == CommonConstants.Tamil) {
            ssToLogin.setSpan(clickableSpanToLogin, 23, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssToLogin.setSpan(clickableSpanToLogin, 23, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }*/

        terms_comditions.setText(ss);
        terms_comditions.setMovementMethod(LinkMovementMethod.getInstance());
        terms_comditions.setHighlightColor(Color.TRANSPARENT);
        ssToLogin.setSpan(clickableSpanToLogin, 22, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        linkToLogin.setText(ssToLogin);
        linkToLogin.setMovementMethod(LinkMovementMethod.getInstance());
        linkToLogin.setHighlightColor(Color.TRANSPARENT);


        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* not yet implimented*/
                // loginButton.performClick();
            }
        });

        reg_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                reg_mobile_til.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    reg_mobile_til.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

                String x = " ";
                x = editable.toString();
                if (x.startsWith("0")) {
                    reg_mobile.setText("");
                }
            }
        });
        reg_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    reg_email_til.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String x = " ";
                x = s.toString();
                if (x.startsWith(".")) {
                    reg_email.setText("");
                }


            }
        });
        reg_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    //   reg_password_til.setError("Ex:abcABC@123");
                    reg_password_til.setErrorEnabled(false);
                    //reg_password_til.setError("Password Must be 6 digit");
                }
                if (!CommonUtil.passwordValidateWithUppercase(reg_password.getText().toString().trim())) {
                    reg_password_til.setErrorEnabled(true);
                    //  reg_password_til.setError("Example:- abcABC@123");
                    reg_password_til.setError(getString(R.string.example_password));
                } else {
                    reg_password_til.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        reg_confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    reg_confirm_password_til.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                signIn();

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                if (isOnline()) {
                    if (isValidateUi()) {
                        registerUser();

                    }
                } else {
                    showToast(SigUpActivity.this, getString(R.string.no_internet));
                }

            }
        });


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Main", response.toString());
                                setProfileToView(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        /*rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
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
    }

    private void closeTab() {
        // Toast.makeText(context, "ON BACK KEY PRESED", Toast.LENGTH_SHORT).show();
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new HomeFragment())
                .commit();
        HomeActivity.toolbar.setNavigationIcon(null);
        HomeActivity.toolbar.setTitle("");
        CommonUtil.hideSoftKeyboard((AppCompatActivity) this);
        mActivityListener.messageFromChildFragmentToActivity("handleBottomNavigation");
//        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("SignupTag");
//
//
//        if (fragment != null)
//            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//
//        HomeActivity.toolbar.setNavigationIcon(null);
//        HomeActivity.toolbar.setTitle("");
    }

    private void registerUser() {
        showProgressDialog();
        JsonObject object = getRegisterObject();
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        mRegisterSubscription = service.userRegister(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {
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
                        Toast.makeText(SigUpActivity.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResponseModel registerResponseModel) {
                        hideProgressDialog();
                        if (registerResponseModel.getIsSuccess()) {
                          /*  Intent i = new Intent(SigUpActivity.this, LoginFragment.class);
                            CommonUtil.displayDialogWindowToHome(registerResponseModel.getEndUserMessage(),alertDialog,context,i,TAG);*/

                            Alert(registerResponseModel.getEndUserMessage() + "  Please Login..!");
                            //  startActivity(i);

                        } else {
                            CommonUtil.displayDialogWindow(registerResponseModel.getEndUserMessage() , alertDialog, context);

                            //Toast.makeText(context, "" + registerResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });


    }

    private void Alert(String s) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View dialogRootView = layoutInflater.inflate(R.layout.dialog_custom, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(dialogRootView);
        alertDialog = alertDialogBuilder.create();
        final TextView textView = (TextView) dialogRootView.findViewById(R.id.description);
        final TextView okBtn = (TextView) dialogRootView.findViewById(R.id.okBtn);
        final AlertDialog finalAlertDialog = alertDialog;
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finalAlertDialog.dismiss();
                HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
                HomeActivity.toolbar.setTitle(getResources().getString(R.string.login_sname));
                HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
                LoginFragment.inp_email.setErrorEnabled(false);
                finish();


            }
        });
        textView.setText(s);
        // create an alert dialog

//        alertDialog.getWindow()
//                .getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                }
                return true;
            }
        });
    }

    private void IntiateGoogleApi() {
//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                Log.d("Text", messageText);
//                Toast.makeText(getActivity(), "Message: " + messageText, Toast.LENGTH_LONG).show();
//            }
//        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .enableAutoManage(getActivity(), this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
        loginButton.setReadPermissions("email");
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            try {
                mGoogleApiClient = new GoogleApiClient.Builder((FragmentActivity) context)
                        .enableAutoManage((FragmentActivity) context /* FragmentActivity */, this /* OnConnectionFailedListener */)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

                loginButton.setReadPermissions("email");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private JsonObject getRegisterObject() {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setMobileNumber(reg_mobile.getText().toString().trim());
        registerModel.setPassword(reg_password.getText().toString().trim());
        registerModel.setEmail(reg_email.getText().toString().trim());
        registerModel.setConfirmPassword(reg_confirm_password.getText().toString().trim());
        registerModel.setRoleId(PayZanEnums.RoleIdEnum.Consumer.RID());
        return new Gson().toJsonTree(registerModel)
                .getAsJsonObject();
    }

    private boolean isValidateUi() {
        if (TextUtils.isEmpty(reg_mobile.getText().toString().trim())) {
            reg_mobile_til.setErrorEnabled(true);
            reg_mobile_til.setError(getString(R.string.err_please_enter_mobile_no));
            reg_mobile.requestFocus();
            return false;
        } else if (!isValidPhone()) {
            reg_mobile_til.setErrorEnabled(true);
            reg_mobile_til.setError(getString(R.string.err_enter_valid_mobile_no));
            return false;
        } else if (TextUtils.isEmpty(reg_email.getText().toString().trim()) && (reg_email.getText().length() < 5)) {
            reg_email_til.setErrorEnabled(true);
            reg_email_til.setError(getString(R.string.err_enter_email_id));
            reg_email.requestFocus();
            return false;
        } else if (!isValidEmail(reg_email.getText().toString().trim())) {
            reg_email_til.setErrorEnabled(true);
            reg_email_til.setError(getString(R.string.err_enter_valid_email_id));
            reg_email.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(reg_password.getText().toString().trim())) {
            reg_password_til.setErrorEnabled(true);
            reg_password_til.setError(getString(R.string.err_enter_password));
            reg_password.requestFocus();
            return false;
        } else if (!CommonUtil.passwordValidateWithUppercase(reg_password.getText().toString().trim())) {
            reg_password_til.setErrorEnabled(true);
           // reg_password_til.setError(getString(R.string.err_enter_valid_password));
            reg_password_til.setError(getString(R.string.err_please_not_enter_character));
            return false;
        } else if (TextUtils.isEmpty(reg_confirm_password.getText().toString().trim())) {
            reg_confirm_password_til.setErrorEnabled(true);
            reg_confirm_password_til.setError(getString(R.string.err_enter_confirm_password));
            reg_confirm_password.requestFocus();
            return false;
        } else if (!reg_confirm_password.getText().toString().equalsIgnoreCase(reg_password.getText().toString().trim())) {
            reg_confirm_password_til.setErrorEnabled(true);
            reg_confirm_password_til.setError(getString(R.string.confirm_password_msg));
            return false;
        }
        return true;
    }

    private boolean isValidPhone() {
        String target = reg_mobile.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    private void setProfileToView(JSONObject jsonObject) {
//        try {
//            dummy.setText(jsonObject.getString("email") + "\n" + jsonObject.getString("gender") + "\n" + jsonObject.getString("name"));

//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(SigUpActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // check if Activity implements listener
        if (context instanceof OnChildFragmentToActivityInteractionListener) {
            mActivityListener = (OnChildFragmentToActivityInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChildFragmentToActivityInteractionListener");
        }

        // check if parent Fragment implements listener
//        if (getActivity().getSupportFragmentManager().findFragmentByTag("walletTag") instanceof OnChildFragmentInteractionListener) {
//
//            mParentListener = (OnChildFragmentInteractionListener) getParentFragment();
//        } else {
//            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
//        }
    }
*/

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(

                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //updateUI(false);
                        Toast.makeText(SigUpActivity.this, getString(R.string.toast_signout), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }

        if (mRegisterSubscription != null) {
            mRegisterSubscription.unsubscribe();
        }

    }

  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_SIGN_IN) {
            SignupFragment fragment = (SignupFragment) this.getSupportFragmentManager()
                    .findFragmentByTag("SignupTag");
            fragment.onActivityResult(requestCode, resultCode, data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        // super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
    }*/

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(" ", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(" ", "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = null;
            if (acct.getPhotoUrl() != null) {
                personPhotoUrl = acct.getPhotoUrl().toString();
            }

            String email = acct.getEmail();

            Log.e(" ", "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }

    @Override
    public void onBackPressed() {

        LoginFragment.txt_Email.setText("");
        LoginFragment.txt_password.setText("");
        LoginFragment.inp_password.setErrorEnabled(false);
        LoginFragment.inp_email.setErrorEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitle(getResources().getString(R.string.login_sname));
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));

        finish();
        super.onBackPressed();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


}
