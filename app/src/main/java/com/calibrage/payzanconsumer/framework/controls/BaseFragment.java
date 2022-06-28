package com.calibrage.payzanconsumer.framework.controls;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.fragement.LoginFragment;
import com.calibrage.payzanconsumer.framework.util.Animationt;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;

public class BaseFragment extends Fragment {
    private ProgressDialog mProgressDialog;
    private int LoginStatus = 0;
    public static final int MAIN_CONTAINER = R.id.content_frame;
    protected void popUpFromBackStack(FragmentActivity activity) {
        activity.getSupportFragmentManager().popBackStack();
    }
    public static void addFragment(FragmentActivity activity, int container, Fragment fragment,
                                   String cuurentFragmentTag, String newFragmentTag) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(cuurentFragmentTag)
                .add(container, fragment, newFragmentTag)
                .commit();
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

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    public void showDialog(FragmentActivity activity, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
        }
        if (mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public void closeTab(String TAG) {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(TAG);


        if (fragment != null) {
            HomeActivity.toolbar.setNavigationIcon(null);
            HomeActivity.toolbar.setTitle("");
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                dialog.setContentView(R.layout.alert_dialouge_home);

                Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);


                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //dialog.setCancelable(false);
                dialog.show();


            }
          /* getActivity().getSupportFragmentManager().popBackStackImmediate();*/
           /* getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            HomeActivity.toolbar.setNavigationIcon(null);
            HomeActivity.toolbar.setTitle("");
            CommonUtil.hideSoftKeyboard((AppCompatActivity)getActivity());*/
        }
    }

    public boolean checkUserLoginStatus(final String TAG) {

        /* it checks for User Login or not if user not login it redirects tologin screen */

        LoginStatus = SharedPrefsData.getInstance(getContext()).getIntFromSharedPrefs(CommonConstants.ISLOGIN);

        if (LoginStatus == CommonConstants.Login) {

            // Toast.makeText(getContext(), "user alredy Login", Toast.LENGTH_SHORT).show();
            return true;

        } else {
            replaceFragment(getActivity(), MAIN_CONTAINER, new LoginFragment(), TAG, LoginFragment.TAG);
//            Toast.makeText(getContext(), "user Not Yet  Login", Toast.LENGTH_SHORT).show();
//
//            /* Show Dialogue Login OR cancel */
//
//            /* If Login Redirect login OR close popup*/
//
//            Intent intent1 = new Intent(Intent.ACTION_MAIN);
//            final Dialog dialog1 = new Dialog(getActivity());
//            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
////                    Intent intent = new Intent(Intent.ACTION_MAIN);
////                    intent.addCategory(Intent.CATEGORY_HOME);
////                    startActivity(intent);
////                    getActivity().finish();
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

    public static void PleaseLogin(Context context) {
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
                //
//                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_HOME);
//                    startActivity(intent);
//                    getActivity().finish();
              //  replaceFragment(getActivity(), MAIN_CONTAINER, new LoginFragment(), TermsofServicesFragment.TAG, LoginFragment.TAG);
                dialog1.dismiss();
            }
        });
        cancel_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        //dialog.setCancelable(false);
        dialog1.show();

    }

    public boolean checkUserLoginStatusWallet(final String TAG) {

        /* it checks for User Login or not if user not login it redirects tologin screen */

        LoginStatus = SharedPrefsData.getInstance(getContext()).getIntFromSharedPrefs(CommonConstants.ISLOGIN);

        if (LoginStatus == CommonConstants.Login) {

            // Toast.makeText(getContext(), "user alredy Login", Toast.LENGTH_SHORT).show();
            return true;

        } else {
            return false;
        }


    }

    public void ShowImagePopUp(String Img_url, String msg_str, String TAG) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dialog.setContentView(R.layout.alert_image_alert);

//        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
//        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);


//        ok_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });
//        cancel_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        //dialog.setCancelable(false);
        dialog.show();

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void startActivityWithAnimation(Intent intent, Animationt animationType, Context ctx) {
      //  startActivity(intent);
        Bundle bundle;


        switch (animationType) {
            case SLIDE_IN_RIGHT:
                 bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.enter_from_right, R.anim.stay_still).toBundle();
                 ctx.startActivity(intent, bundle);
                break;
            case SLIDE_IN_BOTTOM:
                bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.slide_in_from_bottom, R.anim.stay_still).toBundle();
                ctx.startActivity(intent, bundle);
                break;
            case SLIDE_IN_LEFT:
                bundle = ActivityOptions.makeCustomAnimation(ctx, R.anim.enter_from_left, R.anim.stay_still).toBundle();
                ctx.startActivity(intent, bundle);
                break;
            case SCALE:
                bundle = ActivityOptions.makeCustomAnimation(ctx,R.anim.scale_up, R.anim.scale_down).toBundle();
                ctx.startActivity(intent, bundle);
                break;
        }
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
