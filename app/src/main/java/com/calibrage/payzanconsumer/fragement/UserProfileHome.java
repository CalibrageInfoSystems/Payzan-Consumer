package com.calibrage.payzanconsumer.fragement;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.AboutUSActivity;
import com.calibrage.payzanconsumer.activity.AddAddress;
import com.calibrage.payzanconsumer.activity.BarcodeReader_Activity;
import com.calibrage.payzanconsumer.activity.ChangePasswordActivity;
import com.calibrage.payzanconsumer.activity.HelpSupport;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.activity.OrderHistoryActivity;
import com.calibrage.payzanconsumer.activity.ProfileActivity1;
import com.calibrage.payzanconsumer.activity.SaveCardActivity;
import com.calibrage.payzanconsumer.activity.TermsofServiceActivity;
import com.calibrage.payzanconsumer.fragement.agent.AgentLoginFragment;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentToActivityInteractionListener;
import com.calibrage.payzanconsumer.framework.model.UserProfileInfoResponseModel;
import com.calibrage.payzanconsumer.framework.model.WalletBalanceResponce;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.Animationt;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.calibrage.payzanconsumer.framework.util.CommonUtil.updateResources;


public class UserProfileHome extends BaseFragment {
    public static final String TAG = UserProfileHome.class.getSimpleName();
    private Button btn_logOut, btn_Share, addMoneyBtn, btn_QRscanner, btn_lang;
    private OnChildFragmentToActivityInteractionListener mActivityListener;
    private LinearLayout saveCardLyt, orderHistoryLyt, changePsdLyt, aboutuslyt, tofslyt, langLyt, addAddressLyt, supportlty;
    private Context context;
    private ImageView profileImage, editProfile;
    private TextView walletBalanceTxt, userName;
    public static TextView userMail;
    private View v;
    int usertype = 0;
    private Subscription mGetTransactions;
    private SwipeRefreshLayout swiprefresh;
    private Subscription userProfileSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_profile_home, container, false);
        context = this.getActivity();
        usertype = SharedPrefsData.getInstance(getContext()).getIntFromSharedPrefs("usertype");
        setViews();
        initViews();
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    closeTab();
                    return true;
                } else {
                    return false;
                }
            }
        });
        swiprefresh = (SwipeRefreshLayout) v.findViewById(R.id.swiprefresh);
        swiprefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isOnline(context)) {
                    int values = SharedPrefsData.getInstance(getActivity()).getIntFromSharedPrefs("WalletIDValue");
                    if (checkUserLoginStatusWallet(TAG)) {
                        getTransactions(values);
                    } else {
                        swiprefresh.setRefreshing(false);
                    }
                } else {
                    showToast(context, getString(R.string.no_internet));
                    swiprefresh.setRefreshing(false);
                }

            }


        });
        return v;
    }

    private void setViews() {
        btn_logOut = (Button) v.findViewById(R.id.btn_sign_out);
        btn_Share = (Button) v.findViewById(R.id.btn_share);
        addMoneyBtn = (Button) v.findViewById(R.id.addMoneyBtn);
        changePsdLyt = (LinearLayout) v.findViewById(R.id.changePsdLyt);
        aboutuslyt = (LinearLayout) v.findViewById(R.id.aboutuslyt);
        walletBalanceTxt = (TextView) v.findViewById(R.id.walletBalanceTxt);
        userName = (TextView) v.findViewById(R.id.userName);
        userMail = (TextView) v.findViewById(R.id.userMail);
        editProfile = (ImageView) v.findViewById(R.id.editProfile);
        saveCardLyt = (LinearLayout) v.findViewById(R.id.saveCardLyt);
        orderHistoryLyt = (LinearLayout) v.findViewById(R.id.orderHistoryLyt);
        addAddressLyt = v.findViewById(R.id.addAddressLyt);
        tofslyt = (LinearLayout) v.findViewById(R.id.tofslyt);
        langLyt = (LinearLayout) v.findViewById(R.id.langlyt);
        supportlty = (LinearLayout) v.findViewById(R.id.supportlty);
        btn_QRscanner = v.findViewById(R.id.btn_QRscanner);
        profileImage = (ImageView)v.findViewById(R.id.profileImage);

    }

    private void initViews() {


        walletBalanceTxt.setText(String.valueOf(SharedPrefsData.getInstance(context).getWalletBalance(context)));

//        LoginResponseModel loginResponseModel = new Gson().fromJson(SharedPrefsData.getInstance(context).getUserDetails(context), LoginResponseModel.class);
//        if(loginResponseModel!=null){
//            walletBalanceTxt.setText(String.valueOf(SharedPrefsData.getInstance(context).getWalletIdMoney(context)));
//            userName.setText(SharedPrefsData.getInstance(context).getUserName(context));
//            userMail.setText(loginResponseModel.getdata().getUser().getEmail());
//        }


        saveCardLyt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SaveCardActivity.class);
                startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT, context);


            }
        });
        orderHistoryLyt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderHistoryActivity.class);
                startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT, context);
            }
        });
        changePsdLyt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChangePasswordActivity.class);
                startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT, context);
            }
        });
        aboutuslyt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AboutUSActivity.class);
                startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT, context);
            }
        });
        tofslyt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TermsofServiceActivity.class);
                startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT, context);
            }
        });
        langLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLanguage();

            }
        });
        addAddressLyt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddAddress.class);
                startActivityWithAnimation(i, Animationt.SLIDE_IN_RIGHT, getContext());
            }
        });
        supportlty.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), HelpSupport.class);
                startActivityWithAnimation(i, Animationt.SLIDE_IN_RIGHT, getContext());
            }
        });


        addMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionMainFragment transactionMainFragment = new TransactionMainFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("position", 1);
                transactionMainFragment.setArguments(bundle);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.content_frame, transactionMainFragment);
                ft.commit();
            }
        });
        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResources(context, "en-US");
                SharedPrefsData.getInstance(getActivity()).ClearData(getActivity());
                closeTab();

            }
        });
        btn_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Ishare = new Intent(Intent.ACTION_SEND);
                Ishare.setType("text/plain");
                Ishare.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.
                Ishare.putExtra(Intent.EXTRA_SUBJECT, "PayZan Android");
                Ishare.putExtra(Intent.EXTRA_TEXT, "http://calibrage.in/");

                startActivity(Intent.createChooser(Ishare, "Share link!"));
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity1.class);
                context.startActivity(intent);


            }
        });
        btn_QRscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BarcodeReader_Activity.class);
                context.startActivity(i);
            }
        });

    }

    private void closeTab() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("profileHomeTag");
        mActivityListener.messageFromChildFragmentToActivity("signout");

        if (fragment != null) {


            if (usertype == CommonConstants.ISAGENT) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new AgentLoginFragment()).commit();
                HomeActivity.toolbar.setNavigationIcon(null);
                HomeActivity.toolbar.setTitle("");
                CommonUtil.hideSoftKeyboard((AppCompatActivity) getActivity());
            } else {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new LoginFragment()).commit();
                HomeActivity.toolbar.setNavigationIcon(null);
                HomeActivity.toolbar.setTitle("");
                CommonUtil.hideSoftKeyboard((AppCompatActivity) getActivity());
            }


        }

    }

    private void selectLanguage() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_select_language);
        dialog.setTitle("Choose Language...");
        // set the custom dialog components - text, image and button
        RadioButton rbEng = dialog.findViewById(R.id.rbEng);
        RadioGroup languageRg = dialog.findViewById(R.id.languageRg);
        RadioButton rbSim = dialog.findViewById(R.id.rbSim);
        RadioButton rbTl = dialog.findViewById(R.id.rbTl);

        if (SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang") == 1) {
            rbEng.setChecked(true);
            updateResources(context, "en-US");
        } else if (SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang") == 2) {
            rbSim.setChecked(true);
            updateResources(context, "si");
        } else if (SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang") == 3) {
            rbTl.setChecked(true);
            updateResources(context, "ta");
        }

        rbEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResources(context, "en-US");
                SharedPrefsData.getInstance(context).updateIntValue(context, "lang", 1);


                SharedPrefsData.getInstance(context.getApplicationContext()).updateIntValue(context.getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
                Intent i = new Intent(context.getApplicationContext(), HomeActivity.class);
                startActivity(i);

                dialog.dismiss();
            }
        });
        rbSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResources(context, "si");
                SharedPrefsData.getInstance(context).updateIntValue(context, "lang", 2);


                SharedPrefsData.getInstance(context.getApplicationContext()).updateIntValue(context.getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
                Intent i = new Intent(context.getApplicationContext(), HomeActivity.class);
                startActivity(i);


                dialog.dismiss();
            }
        });
        rbTl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResources(context, "ta");
                SharedPrefsData.getInstance(context).updateIntValue(context, "lang", 3);


                SharedPrefsData.getInstance(context.getApplicationContext()).updateIntValue(context.getApplicationContext(), "usertype", CommonConstants.ISCONSUMER);
                Intent i = new Intent(context.getApplicationContext(), HomeActivity.class);
                startActivity(i);

                dialog.dismiss();
            }
        });

        dialog.show();
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
    }

    public void getTransactions(int walletId) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        String url = ApiConstants.BASE_URL + ApiConstants.BalenceBYID + walletId;
        mGetTransactions = service.GetUserBalanceByID(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WalletBalanceResponce>() {
                    @Override
                    public void onCompleted() {
                        hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
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

                    }

                    @Override
                    public void onNext(WalletBalanceResponce transactionResponseModel) {
                        hideDialog();
                        walletBalanceTxt.setText((" " + transactionResponseModel.getResult().getBalance()));
                        //Long val = Long.valueOf(transactionResponseModel.getResult().getBalance());
                        SharedPrefsData.getInstance(getActivity()).saveWalletBalance(context, String.valueOf(transactionResponseModel.getResult().getBalance()));
                        swiprefresh.setRefreshing(false);

                    }
                });
    }

    private void getUserProfileInfo(String userId) {
        if (CommonUtil.isNetworkAvailable(context)) {
            MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
            userProfileSubscription = service.getUserProfileInfo(ApiConstants.GetUserPersonalInfo + userId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UserProfileInfoResponseModel>() {
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
                            Toast.makeText(context, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(UserProfileInfoResponseModel operatorModel) {
                            hideDialog();
                            if (operatorModel.getIsSuccess()) {
                                userName.setText(operatorModel.getResult().getUserName());
                                userMail.setText(operatorModel.getResult().getEmail());
                                Picasso.with(context).load(operatorModel.getResult().getProfilePicUrl()).fit().centerCrop()
                                        .placeholder(R.drawable.user)
                                        .into(profileImage);
                            } else {
                                Toast.makeText(context, "" + operatorModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGetTransactions != null) {
            mGetTransactions.unsubscribe();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CommonUtil.isNetworkAvailable(context)) {
            showDialog(getActivity(), "Please Wait Loading... ");
            getUserProfileInfo(SharedPrefsData.getInstance(context).getUserId(context));
        }
    }
}
