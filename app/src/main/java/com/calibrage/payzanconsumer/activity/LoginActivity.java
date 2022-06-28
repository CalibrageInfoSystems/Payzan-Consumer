package com.calibrage.payzanconsumer.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.adapters.MyAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.model.LoginModel;
import com.calibrage.payzanconsumer.framework.model.LoginResponseModel;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.Animationt;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.calibrage.payzanconsumer.framework.util.CommonConstants.CLIENT_ID;
import static com.calibrage.payzanconsumer.framework.util.CommonConstants.CLIENT_SECRET;
import static com.calibrage.payzanconsumer.framework.util.CommonConstants.SCOPE;


public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,MyAdapter.AdapterOnClick {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView dummy,link_to_register;
    private AutoCompleteTextView autoCompleteTextView;
    private static final int RC_SIGN_IN = 007;
    String get_id, get_name, get_gender, get_email, get_birthday, get_locale, get_location;
    private Button fbBtn,btnLogin;
    private EditText txt_Email,txt_password;
    private AlertDialog alertDialog;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton button;
    private Subscription mRegisterSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login);
        fbBtn = (Button) findViewById(R.id.fbBtn);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        button = (SignInButton) findViewById(R.id.btn_sign_in);
        txt_password = (EditText) findViewById(R.id.txt_password);
        txt_Email = (EditText) findViewById(R.id.txt_Email);
//        dummy = (TextView) findViewById(R.id.dummy);
        link_to_register = (TextView) findViewById(R.id.link_to_register);
        link_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginActivity.this,SigUpActivity.class);
                startActivityWithAnimation(i, Animationt.SLIDE_IN_RIGHT);
            }
        });

                SpannableString ss = new SpannableString(getResources().getString(R.string.terms_and_conditions));
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
               // startActivity(new Intent(MyActivity.this, NextActivity.class));
              //  Toast.makeText(LoginActivity.this, "clicked", Toast.LENGTH_SHORT).show();
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
              //  Toast.makeText(LoginActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan1, 27, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan2, 51, 66, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView = (TextView) findViewById(R.id.txt_terms);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(isValidateUi()){

                   login();
               }

            }
        });


        //FacebookSdk.sdkInitialize(getApplicationContext());
        // printKeyHash(this);
//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                Log.d("Text", messageText);
//                Toast.makeText(LoginActivity.this, getString(R.string.toast_message) + messageText, Toast.LENGTH_LONG).show();
//            }
//        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        loginButton.setReadPermissions("email");



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

    private boolean isValidateUi(){

        return true;
    }

    private void login(){
        JsonObject object = getLoginObject();
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        mRegisterSubscription = service.UserLogin(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponseModel>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(LoginActivity.this, "check", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e instanceof HttpException){
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
                        Toast.makeText(LoginActivity.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(LoginResponseModel loginResponseModel) {
                        Toast.makeText(LoginActivity.this, getString(R.string.toast_sucess), Toast.LENGTH_SHORT).show();
                        String data=  loginResponseModel.getdata().getAccessToken();
                        Toast.makeText(LoginActivity.this, getString(R.string.toast_token)+data, Toast.LENGTH_SHORT).show();
                        /*CommonConstants.USERID = loginResponseModel.getdata().getTokenType().toString();
                        CommonConstants.WALLETID = String.valueOf(loginResponseModel.getdata()..getUserWallet().getWalletId());*/
                        finish();
                    }
                });
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



    private void setProfileToView(JSONObject jsonObject) {
        try {
            Toast.makeText(this, ""+jsonObject.getString("email") + "\n" + jsonObject.getString("gender") + "\n" + jsonObject.getString("name"), Toast.LENGTH_SHORT).show();
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(LoginActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //updateUI(false);
                        Toast.makeText(LoginActivity.this, "signout", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CommonUtil.PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  Log.v(LOG_TAG, "permission granted");

                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
//            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(" ", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(" ", "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl=null;
            if(acct.getPhotoUrl()!=null){
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void adapterOnClick(int position) {
        Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();

    }
}

