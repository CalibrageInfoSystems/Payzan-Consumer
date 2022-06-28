package com.calibrage.payzanconsumer.fragement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.ChooseLanguageActivity;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.activity.LoginViaPaymentActivity;
import com.calibrage.payzanconsumer.activity.SigUpActivity;
import com.calibrage.payzanconsumer.framework.adapters.MyAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentToActivityInteractionListener;
import com.calibrage.payzanconsumer.framework.model.LoginModel;
import com.calibrage.payzanconsumer.framework.model.LoginResponseModel;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.Animationt;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.MyCounter;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
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
import static com.calibrage.payzanconsumer.framework.util.CommonUtil.stopSmsService;


public class LoginFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener, MyAdapter.AdapterOnClick {
    public static final String TAG = LoginFragment.class.getSimpleName();
    private View rootView;
    private Context context;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView dummy, link_to_register;
    private AutoCompleteTextView autoCompleteTextView;
    private static final int RC_SIGN_IN = 007;
    String get_id, get_name, get_gender, get_email, get_birthday, get_locale, get_location;
    private Button fbBtn, btnLogin;
//    private Button fbBtn, btnLogin;

    private AlertDialog alertDialog;
    private OnChildFragmentToActivityInteractionListener mActivityListener;
    public static NCBTextInputLayout inp_email, inp_password;
    public static CommonEditText txt_password, txt_Email;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInBtn;
    private Subscription mRegisterSubscription;
    public static Toolbar toolbar;
    private String inp_emailStr, inp_passwordStr;
    Pattern pattern;
    Matcher matcher;

    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,40}$";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_login, container, false);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        context = this.getActivity();
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) rootView.findViewById(R.id.login);
        fbBtn = (Button) rootView.findViewById(R.id.fbBtn);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        signInBtn = (SignInButton) rootView.findViewById(R.id.btn_sign_in);
        txt_password = (CommonEditText) rootView.findViewById(R.id.txt_password);
        txt_Email = (CommonEditText) rootView.findViewById(R.id.txt_Email);
        inp_email = (NCBTextInputLayout) rootView.findViewById(R.id.inp_email);
        inp_password = (NCBTextInputLayout) rootView.findViewById(R.id.inp_password);
        link_to_register = (TextView) rootView.findViewById(R.id.link_to_register);
        IntiateGoogleApi();

        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.login_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));

        HomeActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HomeActivity.class);
                startActivityWithAnimation(i, Animationt.SLIDE_IN_RIGHT, context);
            }
        });


        SpannableString ss = new SpannableString(getResources().getString(R.string.terms_and_conditions));
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
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
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        if (SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang") == CommonConstants.English) {
            ss.setSpan(clickableSpan1, 29, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(clickableSpan2, 52, 67, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang") == CommonConstants.Sinhala) {
            ss.setSpan(clickableSpan1, 27, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(clickableSpan2, 51, 64, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang") == CommonConstants.Tamil) {
            ss.setSpan(clickableSpan1, 27, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(clickableSpan2, 51, 66, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ss.setSpan(clickableSpan1, 29, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(clickableSpan2, 52, 67, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        TextView textView1 = rootView.findViewById(R.id.txt_terms);
        textView1.setText(ss);
        textView1.setMovementMethod(LinkMovementMethod.getInstance());
        textView1.setHighlightColor(Color.parseColor("#bdbdbd"));

        SpannableString ss_signup = new SpannableString(getResources().getString(R.string.don_t_have_account_signup));
        ClickableSpan clickableSpan_s = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
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
                // display frgamnet
                txt_Email.setText("");
                txt_password.setText("");
                Intent intent = new Intent(context, SigUpActivity.class);
                startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT, context);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };


        if (SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang") == CommonConstants.English) {
            ss_signup.setSpan(clickableSpan_s1, 21, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else if (SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang") == CommonConstants.Sinhala) {
            ss_signup.setSpan(clickableSpan_s1, 27, 42, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang") == CommonConstants.Tamil) {
            ss_signup.setSpan(clickableSpan_s1, 17, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ss_signup.setSpan(clickableSpan_s1, 21, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        link_to_register.setText(ss_signup);
        link_to_register.setMovementMethod(LinkMovementMethod.getInstance());
        link_to_register.setHighlightColor(Color.parseColor("#bdbdbd"));

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

//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                Log.d("Text", messageText);
//                //Toast.makeText(getActivity(), "Message: " + messageText, Toast.LENGTH_LONG).show();
//            }
//        });


        stopSmsService(context, new SmsReceiver());

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
                String x = "";
                x = s.toString();
                if (x.startsWith("0")) {
                    txt_Email.setText("");
                }
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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                if (isOnline(context)) {
                    if (isValidateUi()) {
                        showDialog(getActivity(), "Please Wait loading");
                        login();
                    } else {
                        /*snackbar.show();*/
                    }
                } else {
                    showToast(getActivity(), getString(R.string.no_internet));
                }


            }
        });
        link_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SigUpActivity.class));
            }
        });

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
        rootView.setFocusableInTouchMode(true);
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
        });
        return rootView;
    }

    private void closeTab() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("LoginTag");
        if (!(context instanceof LoginViaPaymentActivity)) {
            mActivityListener.messageFromChildFragmentToActivity("handleBottomNavigation");

            if (fragment != null) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                HomeActivity.toolbar.setNavigationIcon(null);
                HomeActivity.toolbar.setTitle("");
                CommonUtil.hideSoftKeyboard((AppCompatActivity) getActivity());
            }
        } else {
            ((LoginViaPaymentActivity) context).finish();
        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");


            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // check if Activity implements listener
        if ((!(context instanceof LoginViaPaymentActivity))) {
            if ((!(context instanceof ChooseLanguageActivity))) {
                if ((!(context instanceof SigUpActivity))) {
                    if (context instanceof OnChildFragmentToActivityInteractionListener) {
                        mActivityListener = (OnChildFragmentToActivityInteractionListener) context;
                    } else {
                        throw new RuntimeException(context.toString()
                                + " must implement OnChildFragmentToActivityInteractionListener");
                    }
                }
            }
        }

    }

    private void IntiateGoogleApi() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
                loginButton.setReadPermissions("email");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
        super.onResume();
        // IntiateGoogleApi();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }

        if (mRegisterSubscription != null) {
            mRegisterSubscription.unsubscribe();
        }

    }

    private void login() {
        JsonObject object = getLoginObject();
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        mRegisterSubscription = service.UserLogin(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponseModel>() {
                    @Override
                    public void onCompleted() {
                        //  Toast.makeText(getActivity(), "check", Toast.LENGTH_SHORT).show();

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
                        hideDialog();
//                        Toast.makeText(getActivity(), getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(LoginResponseModel loginResponseModel) {
                        hideDialog();
                        if (loginResponseModel.getIsSuccess()) {
                            Toast.makeText(getActivity(), loginResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                            String Token = loginResponseModel.getdata().getAccessToken();

                            // Toast.makeText(getActivity(), "Token :"+loginResponseModel.getdata().getAccessToken(), Toast.LENGTH_SHORT).show();

                     /*   CommonConstants.USERID = loginResponseModel.getData().getUser().getId();
                        CommonConstants.WALLETID = String.valueOf(loginResponseModel.getData().getUserWallet().getWalletId());*/
                        /*  if user successfully login savig success  Object */
                            /*Token*/
                            int Walet = loginResponseModel.getdata().getUserWallet().getId();

                            //  Toast.makeText(context, "Walet:" +loginResponseModel.getdata().getUserWallet().getId(), Toast.LENGTH_SHORT).show();

                            SharedPrefsData.getInstance(getActivity()).updateIntValue(getActivity(), "WalletIDValue", Walet);
                            SharedPrefsData.getInstance(getActivity()).updateStringValue(getActivity(), "PhoneNumber", loginResponseModel.getdata().getUser().getPhoneNumber().toString().trim());

                            int values = SharedPrefsData.getInstance(getActivity()).getIntFromSharedPrefs("WalletIDValue");
                            SharedPrefsData.getInstance(getActivity()).updateStringValue(getActivity(), "Token", loginResponseModel.getdata().getTokenType() + " " + Token);
                            SharedPrefsData.getInstance(getActivity()).updateStringValue(getActivity(), "RefreshToken", loginResponseModel.getdata().getRefreshToken());


                            SharedPrefsData.getInstance(getActivity()).updateIntValue(getActivity(), CommonConstants.ISLOGIN, CommonConstants.Login);
                            SharedPrefsData.getInstance(getActivity()).saveUserId(getActivity(), loginResponseModel.getdata().getUser().getId());
                            SharedPrefsData.getInstance(getActivity()).saveProfilePic(getActivity(), loginResponseModel.getdata().getUser().getProfilePicUrl());
                            SharedPrefsData.getInstance(getActivity()).saveWalletId(getActivity(), loginResponseModel.getdata().getUserWallet().getWalletId());
                            SharedPrefsData.getInstance(getActivity()).saveWalletBalance(getActivity(), String.valueOf(loginResponseModel.getdata().getUserWallet().getBalance()));
                            SharedPrefsData.getInstance(getActivity()).saveUserName(getActivity(), loginResponseModel.getdata().getUser().getUserName());
                            SharedPrefsData.getInstance(getActivity()).saveUserDetails(getActivity(), new Gson().toJson(loginResponseModel));


                            if (CommonUtil.timer != null)
                                CommonUtil.timer.cancel();
                            CommonUtil.timer = new MyCounter(loginResponseModel.getdata().getExpiresIn() * 1000, 1000, context);
                            CommonUtil.timer.start();

                            Intent i = new Intent(getContext(), HomeActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getActivity(), loginResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }


                        //finish();
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private boolean isValidateUi() {
        String password = txt_password.getText().toString().trim();
        String email = txt_Email.getText().toString().trim();

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        if (TextUtils.isEmpty(txt_Email.getText().toString().trim())) {
            inp_email.setError(getString(R.string.err_enter_mobile_number));
            inp_email.setErrorEnabled(true);
            inp_email.requestFocusFromTouch();
            // inp_email.requestFocus();
            return false;
        } else if (!isValidPhone()) {
            inp_email.setErrorEnabled(true);
            inp_email.setError(getString(R.string.err_enter_valid_mobile_no));
            inp_email.requestFocusFromTouch();
            // inp_email.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(txt_password.getText().toString().trim())) {
            inp_password.setError(getString(R.string.err_enter_password));
            inp_password.setErrorEnabled(true);
            inp_password.requestFocusFromTouch();
            // inp_password.requestFocus();
            return false;
        } else if (!(password.length() >= 8 && password.length() <= 40)) {
            inp_password.setError(getString(R.string.err_password_length_invalid));
            inp_password.requestFocusFromTouch();
            // inp_password.requestFocus();
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
            Toast.makeText(getActivity(), "" + jsonObject.getString("email") + "\n" + jsonObject.getString("gender") + "\n" + jsonObject.getString("name"), Toast.LENGTH_SHORT).show();
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
        Log.e("Login request", "" + new Gson().toJsonTree(loginModel).getAsJsonObject());
        return new Gson().toJsonTree(loginModel).getAsJsonObject();
    }

    @Override
    public void adapterOnClick(int position) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }
}
