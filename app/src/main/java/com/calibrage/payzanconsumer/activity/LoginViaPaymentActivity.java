package com.calibrage.payzanconsumer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.LoginFragment;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentToActivityInteractionListener;
import com.calibrage.payzanconsumer.framework.model.LoginModel;
import com.calibrage.payzanconsumer.framework.model.LoginResponseModel;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.Animationt;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
import com.calibrage.payzanconsumer.framework.util.SmsListener;
import com.calibrage.payzanconsumer.framework.util.SmsReceiver;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.calibrage.payzanconsumer.framework.util.CommonConstants.CLIENT_ID;
import static com.calibrage.payzanconsumer.framework.util.CommonConstants.CLIENT_SECRET;
import static com.calibrage.payzanconsumer.framework.util.CommonConstants.SCOPE;

/**
 * Created by Calibrage11 on 12/27/2017.
 */

public class LoginViaPaymentActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {


    public static final String TAG = LoginFragment.class.getSimpleName();
    private Context context;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView dummy, link_to_register;
    private AutoCompleteTextView autoCompleteTextView;
    private static final int RC_SIGN_IN = 007;
    String get_id, get_name, get_gender, get_email, get_birthday, get_locale, get_location;
    private Button fbBtn, btnLogin;

    private AlertDialog alertDialog;
    private OnChildFragmentToActivityInteractionListener mActivityListener;
    public static NCBTextInputLayout inp_email, inp_password;
    public static CommonEditText txt_password, txt_Email;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInBtn;
    private Subscription mRegisterSubscription;
    public Toolbar toolbar;
    private String inp_emailStr, inp_passwordStr;
    Pattern pattern;
    Matcher matcher;
    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,20}$";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_via_payment);

        context = this;
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login);
        toolbar = findViewById(R.id.toolbar);
        fbBtn = (Button) findViewById(R.id.fbBtn);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        signInBtn = (SignInButton) findViewById(R.id.btn_sign_in);
        txt_password = (CommonEditText) findViewById(R.id.txt_password);
        txt_Email = (CommonEditText) findViewById(R.id.txt_Email);
        inp_email = (NCBTextInputLayout) findViewById(R.id.inp_email);
        inp_password = (NCBTextInputLayout) findViewById(R.id.inp_password);
//        dummy = (TextView) findViewById(R.id.dummy);
        link_to_register = (TextView) findViewById(R.id.link_to_register);
        IntiateGoogleApi();

        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitle(getResources().getString(R.string.login_sname));
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(context, HomeActivity.class);
//                startActivityWithAnimation(i, Animationt.SLIDE_IN_RIGHT);
                finish();
            }
        });
        SpannableString ss = new SpannableString(getResources().getString(R.string.terms_and_conditions));
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // startActivity(new Intent(MyActivity.this, NextActivity.class));
                // Toast.makeText( context, "clicked", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText( context, "clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan1, 27, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan2, 51, 66, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView1 = (TextView) findViewById(R.id.txt_terms);
        textView1.setText(ss);
        textView1.setMovementMethod(LinkMovementMethod.getInstance());
        textView1.setHighlightColor(Color.TRANSPARENT);

        SpannableString ss_signup = new SpannableString(getResources().getString(R.string.don_t_have_account_signup));
        ClickableSpan clickableSpan_s = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // startActivity(new Intent(MyActivity.this, NextActivity.class));
                //Toast.makeText( context, "clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan_s1 = new ClickableSpan() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View textView) {
                //   startActivity(new Intent( context, signup.class));
                //  Toast.makeText( context, "clicked", Toast.LENGTH_SHORT).show();

                // display frgamnet
                Intent intent = new Intent(context, SigUpActivity.class);
                startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        // ss_signup.setSpan(clickableSpan_s, 1, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss_signup.setSpan(clickableSpan_s1, 21, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        link_to_register.setText(ss_signup);
        link_to_register.setMovementMethod(LinkMovementMethod.getInstance());
        link_to_register.setHighlightColor(Color.TRANSPARENT);

        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                loginButton.performClick();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                signIn();

            }
        });

        txt_Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    inp_email.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

              /*  if (!s.toString().startsWith("+94")) {
                    txt_Email.setText("+94");
                    Selection.setSelection(txt_Email.getText(), txt_Email
                            .getText().length());

                }*/

            }
        });
        txt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    inp_password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       /* snackbar.dismiss();*/
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                if (isOnline()) {
                    if (isValidateUi()) {
                        showProgressDialog();
                        login();
                    } else {

                    }
                } else {
                    /*snackbar.show();*/
                }


            }
        });
//        link_to_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent( context, signup.class));
//            }
//        });

        //FacebookSdk.sdkInitialize(getApplicationContext());
        // printKeyHash(this);


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


    }

    private void IntiateGoogleApi() {
//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                Log.d("Text", messageText);
//                //Toast.makeText( context, "Message: " + messageText, Toast.LENGTH_LONG).show();
//            }
//        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this, this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
                loginButton.setReadPermissions("email");
            } catch (Exception e) {
                e.printStackTrace();
            }
//        mGoogleApiClient = new GoogleApiClient.Builder( context)
//                .enableAutoManage( context, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//        loginButton.setReadPermissions("email");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // IntiateGoogleApi();
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

    private void login() {
        JsonObject object = getLoginObject();
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        mRegisterSubscription = service.UserLogin(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponseModel>() {
                    @Override
                    public void onCompleted() {
                        //  Toast.makeText( context, "check", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {
                      /*  snackbar.show();*/
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
                        Toast.makeText(context, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(LoginResponseModel loginResponseModel) {
                        hideProgressDialog();
                        if (loginResponseModel.getIsSuccess()) {
                            String Token = loginResponseModel.getdata().getAccessToken();

                            // Toast.makeText( context, "Token :"+loginResponseModel.getdata().getAccessToken(), Toast.LENGTH_SHORT).show();

                     /*   CommonConstants.USERID = loginResponseModel.getData().getUser().getId();
                        CommonConstants.WALLETID = String.valueOf(loginResponseModel.getData().getUserWallet().getWalletId());*/
                        /*  if user successfully login savig success  Object */
                            /*Token*/
                            int Walet = loginResponseModel.getdata().getUserWallet().getId();

                            //  Toast.makeText(context, "Walet:" +loginResponseModel.getdata().getUserWallet().getId(), Toast.LENGTH_SHORT).show();

                            SharedPrefsData.getInstance(context).updateIntValue(context, "WalletIDValue", Walet);


                            int values = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("WalletIDValue");
                            SharedPrefsData.getInstance(context).updateStringValue(context, "Token", loginResponseModel.getdata().getTokenType() + " " + Token);

                            SharedPrefsData.getInstance(context).updateStringValue(context, "RefreshToken", loginResponseModel.getdata().getRefreshToken());
                            SharedPrefsData.getInstance(context).updateIntValue(context, CommonConstants.ISLOGIN, CommonConstants.Login);
                            SharedPrefsData.getInstance(context).saveUserId(context, loginResponseModel.getdata().getUser().getId());
                            SharedPrefsData.getInstance(context).saveWalletId(context, loginResponseModel.getdata().getUserWallet().getWalletId());
                            SharedPrefsData.getInstance(context).saveWalletBalance(context, String.valueOf(loginResponseModel.getdata().getUserWallet().getBalance()));
                            SharedPrefsData.getInstance(context).saveUserName(context, loginResponseModel.getdata().getUser().getUserName());
                            SharedPrefsData.getInstance(context).saveUserDetails(context, new Gson().toJson(loginResponseModel));
                            Toast.makeText(context, loginResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
// TODO Add extras or a data URI to this intent as appropriate.
                            resultIntent.putExtra("login", "Success");
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();

                        }
                        Toast.makeText(context, loginResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();


                        //finish();
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private boolean isValidateUi() {

        inp_emailStr = txt_Email.getText().toString().trim();
        inp_passwordStr = txt_password.getText().toString().trim();
        String password = txt_password.getText().toString().trim();
        String email = txt_Email.getText().toString().trim();

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        if (TextUtils.isEmpty(inp_emailStr)) {
            inp_email.setError(getString(R.string.err_enter_mobile_number));
            inp_email.setErrorEnabled(true);
            return false;
        } else if (!isValidPhone()) {
            inp_email.setErrorEnabled(true);
            inp_email.setError(getString(R.string.err_enter_valid_mobile_no));
            return false;
        }
        //   else if (!(email.length()<=20))
        // {
        //   inp_email.setErrorEnabled(true);
        //inp_email.setError("Enter user name");
        //return  false;
        // }
        else if (TextUtils.isEmpty(inp_passwordStr)) {
            inp_password.setError(getString(R.string.err_enter_password));
            inp_password.setErrorEnabled(true);
            return false;
        } else if (!(password.length() > 4 && password.length() <= 20)) {

            inp_password.setError(getString(R.string.err_password_length_invalid));
            return false;

        }
        return true;

    }

    private boolean isValidPhone() {
        String target = txt_Email.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    private void setProfileToView(JSONObject jsonObject) {
        try {
            Toast.makeText(this, "" + jsonObject.getString("email") + "\n" + jsonObject.getString("gender") + "\n" + jsonObject.getString("name"), Toast.LENGTH_SHORT).show();
            //finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JsonObject getLoginObject() {
        LoginModel loginModel = new LoginModel();
        loginModel.setClientId(CLIENT_ID);
        loginModel.setClientSecret(CLIENT_SECRET);
        loginModel.setScope(SCOPE);
        loginModel.setPassword(txt_password.getText().toString());
        loginModel.setUserName(txt_Email.getText().toString());
        return new Gson().toJsonTree(loginModel)
                .getAsJsonObject();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
