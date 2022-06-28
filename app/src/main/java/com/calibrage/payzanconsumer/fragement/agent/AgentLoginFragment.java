package com.calibrage.payzanconsumer.fragement.agent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.framework.adapters.MyAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentToActivityInteractionListener;
import com.calibrage.payzanconsumer.framework.model.LoginModel;
import com.calibrage.payzanconsumer.framework.model.LoginResponseModel;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
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


public class AgentLoginFragment extends BaseFragment implements MyAdapter.AdapterOnClick {
    public static final String TAG = AgentLoginFragment.class.getSimpleName();
    private View rootView;
    private Context context;
    private CallbackManager callbackManager;

    private TextView dummy, link_to_register;
    private AutoCompleteTextView autoCompleteTextView;
    private static final int RC_SIGN_IN = 007;
    String get_id, get_name, get_gender, get_email, get_birthday, get_locale, get_location;
    private Button btnLogin;

    private AlertDialog alertDialog;
    private OnChildFragmentToActivityInteractionListener mActivityListener;
    private NCBTextInputLayout inp_email, inp_password;
    private CommonEditText txt_password, txt_Email;
    private GoogleApiClient mGoogleApiClient;

    private Subscription mRegisterSubscription;
    public static Toolbar toolbar;
    private String inp_emailStr, inp_passwordStr;
    Pattern pattern;
    Matcher matcher;
    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,20}$";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.agent_login_fragment, container, false);

        context = this.getActivity();
        callbackManager = CallbackManager.Factory.create();


        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        txt_password = (CommonEditText) rootView.findViewById(R.id.txt_password);
        txt_Email = (CommonEditText) rootView.findViewById(R.id.txt_Email);
        inp_email = (NCBTextInputLayout) rootView.findViewById(R.id.inp_email);
        inp_password = (NCBTextInputLayout) rootView.findViewById(R.id.inp_password);
//        dummy = (TextView) findViewById(R.id.dummy);
        link_to_register = (TextView) rootView.findViewById(R.id.link_to_register);


        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.login_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        /*HomeActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeTab();
            }
        });*/
        SpannableString ss = new SpannableString(getResources().getString(R.string.terms_and_conditions));
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // startActivity(new Intent(MyActivity.this, NextActivity.class));
               // Toast.makeText(getActivity(), getString(R.string.toast_clicked), Toast.LENGTH_SHORT).show();
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
               //Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan1, 27, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan2, 51, 66, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView = (TextView) rootView.findViewById(R.id.txt_terms);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);

        SpannableString ss_signup = new SpannableString(getResources().getString(R.string.don_t_have_account_signup));
        ClickableSpan clickableSpan_s = new ClickableSpan() {
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
        ClickableSpan clickableSpan_s1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                //   startActivity(new Intent(getActivity(), signup.class));
              //  Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();

                // display frgamnet
              /*  getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new SignupFragment(), "SignupTag")
                        .commit();*/

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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidateUi()) {
                    showDialog(getActivity(), "Please Wait loading");
                    login();
                }

            }
        });
//        link_to_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), signup.class));
//            }
//        });

        //FacebookSdk.sdkInitialize(getApplicationContext());
        // printKeyHash(this);




        return rootView;
    }

    private void closeTab() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("LoginTag");
        mActivityListener.messageFromChildFragmentToActivity("handleBottomNavigation");

        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            HomeActivity.toolbar.setNavigationIcon(null);
            HomeActivity.toolbar.setTitle("");
            CommonUtil.hideSoftKeyboard((AppCompatActivity) getActivity());
        }

    }

    @Override
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


    @Override
    public void onResume() {
        super.onResume();
        // IntiateGoogleApi();
    }

    @Override
    public void onPause() {
        super.onPause();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
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
                        Toast.makeText(getActivity(), getString(R.string.toast_check), Toast.LENGTH_SHORT).show();

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
                        // Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(LoginResponseModel loginResponseModel) {
                        hideDialog();
                        Toast.makeText(getActivity(), getString(R.string.toast_sucess), Toast.LENGTH_SHORT).show();
                        String Token = loginResponseModel.getdata().getAccessToken();
                        // Toast.makeText(getActivity(), "Token :"+loginResponseModel.getdata().getAccessToken(), Toast.LENGTH_SHORT).show();

                     /*   CommonConstants.USERID = loginResponseModel.getData().getUser().getId();
                        CommonConstants.WALLETID = String.valueOf(loginResponseModel.getData().getUserWallet().getWalletId());*/
                        /*  if user successfully login savig success  Object */

                        int Walet=loginResponseModel.getdata().getUserWallet().getId();

                      //  Toast.makeText(context, "Walet:" +loginResponseModel.getdata().getUserWallet().getId(), Toast.LENGTH_SHORT).show();

                        SharedPrefsData.getInstance(getActivity()).updateIntValue(getActivity(),"WalletIDValue",Walet);

                        SharedPrefsData.getInstance(getActivity()).updateStringValue(getActivity(),"Token",loginResponseModel.getdata().getTokenType()+" "+Token);

                        SharedPrefsData.getInstance(getActivity()).updateIntValue(getActivity(), CommonConstants.ISLOGIN, CommonConstants.Login);
                        SharedPrefsData.getInstance(getActivity()).saveUserId(getActivity(), loginResponseModel.getdata().getUser().getId());
                        SharedPrefsData.getInstance(getActivity()).saveWalletId(getActivity(), loginResponseModel.getdata().getUserWallet().getWalletId());
                        SharedPrefsData.getInstance(getActivity()).saveWalletBalance(getActivity(), String.valueOf(loginResponseModel.getdata().getUserWallet().getBalance()));
                        SharedPrefsData.getInstance(getActivity()).saveUserName(getActivity(), loginResponseModel.getdata().getUser().getUserName());
                        SharedPrefsData.getInstance(getActivity()).saveUserDetails(getActivity(), new Gson().toJson(loginResponseModel));

                        Intent i = new Intent(getContext(), HomeActivity.class);
                        startActivity(i);

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
            inp_email.setError("Enter mobile number");
            inp_email.setErrorEnabled(true);
            return false;
        } else if (!isValidPhone()) {
            inp_email.setErrorEnabled(true);
            inp_email.setError("Enter valid mobile no");
            return false;
        }
        //   else if (!(email.length()<=20))
        // {
        //   inp_email.setErrorEnabled(true);
        //inp_email.setError("Enter user name");
        //return  false;
        // }
        else if (TextUtils.isEmpty(inp_passwordStr)) {
            inp_password.setError("Enter password");
            inp_password.setErrorEnabled(true);
            return false;
        } else if (password.length() > 4 && password.length() <= 20) {

            inp_password.setError("valid password");
            return matcher.matches();
        } else {
            inp_password.setError("Not valid password");
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
        loginModel.setPassword(txt_password.getText().toString());
        loginModel.setUserName(txt_Email.getText().toString());
        return new Gson().toJsonTree(loginModel)
                .getAsJsonObject();
    }

    @Override
    public void adapterOnClick(int position) {

    }


}
