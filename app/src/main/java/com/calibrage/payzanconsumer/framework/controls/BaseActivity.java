package com.calibrage.payzanconsumer.framework.controls;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.activity.LoginViaPaymentActivity;
import com.calibrage.payzanconsumer.activity.SigUpActivity;
import com.calibrage.payzanconsumer.fragement.LoginFragment;
import com.calibrage.payzanconsumer.framework.util.Animationt;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;

public class  BaseActivity extends AppCompatActivity {
    public static final int MAIN_CONTAINER = R.id.content_frame;
    private ProgressDialog mProgressDialog;
    private int LoginStatus = 0;
    public void ShowActionBar(String Title) {
        final android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(Title);
        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.toolbar.setTitle("");
                HomeActivity.toolbar.setNavigationIcon(null);
                finish();

            }
        });
    }
    public void hideSoftKeyboard() {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public boolean checkUserLoginStatus(final Context TAG) {

        /* it checks for User Login or not if user not login it redirects tologin screen */

        LoginStatus = SharedPrefsData.getInstance(this).getIntFromSharedPrefs(CommonConstants.ISLOGIN);

        if (LoginStatus == CommonConstants.Login) {

            // Toast.makeText(getContext(), "user alredy Login", Toast.LENGTH_SHORT).show();
            return true;

        } else {


            Intent intent = new Intent(TAG, LoginViaPaymentActivity.class);
            //startActivity(intent);
            startActivityForResult(intent, CommonConstants.MY_CHILD_ACTIVITY);

           // return false;
//            Toast.makeText(this, "user Not Yet  Login", Toast.LENGTH_SHORT).show();
//
//            /* Show Dialogue Login OR cancel */
//
//            /* If Login Redirect login OR close popup*/
//
//            Intent intent1 = new Intent(Intent.ACTION_MAIN);
//            final Dialog dialog1 = new Dialog(this);
//            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
//                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//            }
//
//            dialog1.setContentView(R.layout.alert_dialouge_login);
//
//            Button ok_btn1 = (Button) dialog1.findViewById(R.id.ok_btn);
//            Button cancel_btn1 = (Button) dialog1.findViewById(R.id.cancel_btn);
//
//
//            ok_btn1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //
//
//
//                  //  finish();
//
//                    dialog1.dismiss();
//                }
//            });
//            cancel_btn1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog1.dismiss();
//                }
//            });
//
//            //dialog.setCancelable(false);
//            dialog1.show();

            return false;
        }


    }

  /*  public boolean checkUserLoginStatusWallet(final String TAG) {

        *//* it checks for User Login or not if user not login it redirects tologin screen *//*

        LoginStatus = SharedPrefsData.getInstance(this).getIntFromSharedPrefs(CommonConstants.ISLOGIN);

        if (LoginStatus == CommonConstants.Login) {

            // Toast.makeText(getContext(), "user alredy Login", Toast.LENGTH_SHORT).show();
            return true;

        } else {
            return false;
        }


    }*/

    public boolean checkUserLoginStatusWallet(final String TAG) {

      /* it checks for User Login or not if user not login it redirects tologin screen */

                LoginStatus = SharedPrefsData.getInstance(this).getIntFromSharedPrefs(CommonConstants.ISLOGIN);

        if (LoginStatus == CommonConstants.Login) {

            // Toast.makeText(getContext(), "user alredy Login", Toast.LENGTH_SHORT).show();
            return true;

        } else {
            return false;
        }


    }


    @Override
    public void onBackPressed() {
        HomeActivity.toolbar.setTitle("");
        HomeActivity.toolbar.setNavigationIcon(null);
        super.onBackPressed();
    }

    private void initProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }
    public void showProgressDialog() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog == null)
                        initProgressDialog();
                    if (!mProgressDialog.isShowing())
                        mProgressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void hideProgressDialog() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (mProgressDialog != null && mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            mProgressDialog = null;
                        }
                    }
                });
    }



    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public void checkIsLogin(Context context) {
        Intent intent1 = new Intent(Intent.ACTION_MAIN);
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.setContentView(R.layout.alert_dialouge_login);
        Button ok_btn1 = (Button) dialog1.findViewById(R.id.ok_btn);
        Button cancel_btn1 = (Button) dialog1.findViewById(R.id.cancel_btn);
        ok_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                dialog1.dismiss();
            }
        });
        cancel_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }
    public void startActivityWithAnimation(Intent intent, Animationt animationType) {
        startActivity(intent);
        switch (animationType) {
            case SLIDE_IN_RIGHT:
                overridePendingTransition(R.anim.enter_from_right, R.anim.stay_still);
                break;
            case SLIDE_IN_BOTTOM:
                overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.stay_still);
                break;
            case SLIDE_IN_LEFT:
                overridePendingTransition(R.anim.enter_from_left, R.anim.stay_still);
                break;
            case SCALE:
                overridePendingTransition(R.anim.scale_up, R.anim.scale_down);
                break;
        }
    }

    public void replaceFragment(final FragmentActivity activity, final int container, final Fragment
            fragment, final String cuurentFragmentTag, final String newFragmentTag) {
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                if (activity !=null )// update the main content by replacing fragments
                {

                    FragmentTransaction fragmentTransaction = activity
                            .getSupportFragmentManager()
                            .beginTransaction();
                    fragmentTransaction
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction
                            .addToBackStack(cuurentFragmentTag)
                            .add(container, fragment, newFragmentTag);
                    fragmentTransaction.commitAllowingStateLoss();
                }


              /*  closeTab(cuurentFragmentTag);*/

            }
        };
        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            new Handler().post(mPendingRunnable);
        }
    }


}
