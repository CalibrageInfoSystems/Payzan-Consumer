package com.calibrage.payzanconsumer.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.calibrage.payzanconsumer.Consumer.ConsumerProfileActivity;
import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.HomeFragment;
import com.calibrage.payzanconsumer.fragement.LoginFragment;
import com.calibrage.payzanconsumer.fragement.OffersFragment;
import com.calibrage.payzanconsumer.fragement.TransactionMainFragment;
import com.calibrage.payzanconsumer.fragement.UserProfileHome;
import com.calibrage.payzanconsumer.fragement.agent.Agent_Home_Fragment;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentToActivityInteractionListener;
import com.calibrage.payzanconsumer.framework.interfaces.OnFragmentInteractionListener;
import com.calibrage.payzanconsumer.framework.model.RefreshResponseModel;
import com.calibrage.payzanconsumer.framework.model.RefreshTokenModel;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.RefreshToken;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.MyCounter;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.calibrage.payzanconsumer.framework.util.CommonConstants.CLIENT_ID;
import static com.calibrage.payzanconsumer.framework.util.CommonConstants.CLIENT_SECRET;
import static com.calibrage.payzanconsumer.framework.util.CommonUtil.ISLANG;

public class HomeActivity extends BaseActivity implements OnFragmentInteractionListener, OnChildFragmentToActivityInteractionListener {
    public static final String TAG = HomeActivity.class.getSimpleName();
    private android.view.Menu menu;
    public static Toolbar toolbar;
    private FrameLayout content_frame;
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;
    private TextView textCartItemCount;
    int mCartItemCount = 10;
    int val = 0;
    int usertype = 0;
    RefreshToken broadCastReceiver = new RefreshToken();
    private IntentFilter mIntentFilter;
    private Subscription refrshSubscription;
    public static Context ctx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ctx = this;
        mIntentFilter = new IntentFilter("action_name");
        this.registerReceiver(broadCastReceiver, mIntentFilter);
        //        if(SharedPrefsData.getInstance(this).getStringFromSharedPrefs("RefreshToken")!=null &&!SharedPrefsData.getInstance(this).getStringFromSharedPrefs("RefreshToken").equalsIgnoreCase("n/a")){
//            getRefreshToken();
//        }
        if (checkUserLoginStatusWallet(TAG)) {
            getRefreshToken();
        }
        usertype = SharedPrefsData.getInstance(getApplicationContext()).getIntFromSharedPrefs("usertype");
        val = SharedPrefsData.getInstance(HomeActivity.this).getIntFromSharedPrefs(CommonConstants.ISLOGIN);
        setupToolbar();
        fragmentManager = getSupportFragmentManager();
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, homeFragment, "homeTag")
                .commit();


        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        BottomNavigationViewHelper bottomNavigationViewHelper = new BottomNavigationViewHelper();
        bottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.action_home);

        if (SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang") == 1 && ISLANG) {
            // rbEng.setChecked(true);
            updateResources(this, "en-US");
            Intent i = new Intent(this.getApplicationContext(), HomeActivity.class);
            startActivity(i);
            ISLANG = false;
        } else if (SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang") == 2 && ISLANG) {
            //rbSim.setChecked(true);
            updateResources(this, "si");
            Intent i = new Intent(this.getApplicationContext(), HomeActivity.class);
            startActivity(i);
            ISLANG = false;
        } else if (SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang") == 3 && ISLANG) {
            // rbTl.setChecked(true);
            updateResources(this, "ta");
            Intent i = new Intent(this.getApplicationContext(), HomeActivity.class);
            startActivity(i);
            ISLANG = false;
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        int menuItemId = item.getItemId();
                        if (menuItemId == R.id.action_home) {

                            if (usertype == CommonConstants.ISAGENT) {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_frame, new Agent_Home_Fragment())
                                        .commit();
                                toolbar.setNavigationIcon(null);
                                toolbar.setTitle("");
                            } else {

                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_frame, new HomeFragment())
                                        .commit();
                                toolbar.setNavigationIcon(null);
                                toolbar.setTitle("");
                            }
                            return true;
                        } else if (menuItemId == R.id.action_login) {
                            if (usertype == CommonConstants.ISAGENT) {
                                Intent I = new Intent(HomeActivity.this, ConsumerProfileActivity.class);
                                startActivity(I);
                            } else {

                                // TODO : Conver Frgment TO Activity
                              /*  Intent I=new Intent(HomeActivity.this, ConsumerProfileActivity.class);
                                startActivity(I);*/
                                if (val == CommonConstants.Login) {
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.content_frame, new UserProfileHome(), "profileHomeTag")
                                            .commit();
                                    toolbar.setNavigationIcon(null);
                                    toolbar.setTitle("");
                                } else {
                                    getSupportFragmentManager().beginTransaction()
                                            .add(R.id.content_frame, new LoginFragment(), "LoginTag")
                                            .commit();
                                }

                            }
                            if (val == CommonConstants.Login) {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_frame, new UserProfileHome(), "profileHomeTag")
                                        .commit();
                                toolbar.setNavigationIcon(null);
                                toolbar.setTitle("");
                            } else {
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.content_frame, new LoginFragment(), "LoginTag")
                                        .commit();
                            }


                            return true;
                        } else if (menuItemId == R.id.action_wallet) {
                            TransactionMainFragment transactionMainFragment = new TransactionMainFragment();
                            // transactionMainFragment.setFragmentCommunication(HomeActivity.this);
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.content_frame, transactionMainFragment, "walletTag")
                                    .commit();

                            return true;
                        } else if (menuItemId == R.id.action_offers) {
//                            startActivity(new Intent(getApplicationContext(),OffersTabActivity.class));
                            OffersFragment offersFragment = new OffersFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, offersFragment)
                                    .commit();
                            return true;
                        }
                        return true;
                    }
                });


    }

    public void ReplcaFragment(Fragment fragment) {
        fragmentManager.beginTransaction().add(R.id.content_frame, fragment).commit();
    }


    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        menu = bottomNavigationView.getMenu();
        if (val == CommonConstants.Login) {
            menu.findItem(R.id.action_login).setTitle(getString(R.string.profile));
        } else {
            menu.findItem(R.id.action_login).setTitle(getString(R.string.login));
        }

//        String valuee="profile";
////        getMenuInflater().inflate(R.menu.bottom_navigation_main, menu);//Menu Resource, Menu
//        MenuItem menuItem = menu.findItem(R.id.action_offers);
//           menuItem.setIcon(buildCounterDrawable(HomeActivity.this,2,  R.drawable.ic_notification));
//
//         final MenuItem menuItem = menu.findItem(R.id.action_cart);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);//Menu Resource, Menu
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        //menuItem.setIcon(buildCounterDrawable(HomeActivity.this,2,  R.drawable.ic_notification));

        // final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
//        String valuee="profile";
//        getMenuInflater().inflate(R.menu.bottom_navigation_main, menu);//Menu Resource, Menu
//     MenuItem menuItem = menu.findItem(R.id.action_login);
//      //   menuItem.setIcon(buildCounterDrawable(HomeActivity.this,2,  R.drawable.ic_notification));
//        menuItem.setTitle(valuee);
        // final MenuItem menuItem = menu.findItem(R.id.action_cart);
        return true;
    }


    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void getRefreshToken() {
        if (CommonUtil.isNetworkAvailable(HomeActivity.this)) {
            JsonObject object = getRefreshObject();
            MyServices service = ServiceFactory.createRetrofitService(HomeActivity.this, MyServices.class);
            refrshSubscription = service.getRefreshToken(object)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RefreshResponseModel>() {
                        @Override
                        public void onCompleted() {
                            //   Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
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
                            //Toast.makeText(HomeActivity.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(RefreshResponseModel refreshResponseModel) {
                            SharedPrefsData.getInstance(HomeActivity.this).updateStringValue(HomeActivity.this, "Token", refreshResponseModel.getResult().getTokenType() + " " + refreshResponseModel.getResult().getAccessToken());
                            SharedPrefsData.getInstance(HomeActivity.this).updateStringValue(HomeActivity.this, "RefreshToken", refreshResponseModel.getResult().getRefreshToken());
                            // Toast.makeText(HomeActivity.this, getString(R.string.toast_sucess), Toast.LENGTH_SHORT).show();
                            if (CommonUtil.timer != null)
                                CommonUtil.timer.cancel();
                            CommonUtil.timer = new MyCounter(refreshResponseModel.getResult().getExpiresIn() * 1000, 1000, HomeActivity.this);
                            CommonUtil.timer.start();
                        }
                    });
        }
    }

    private JsonObject getRefreshObject() {
        RefreshTokenModel refreshTokenModel = new RefreshTokenModel();
        refreshTokenModel.setClientId(CLIENT_ID);
        refreshTokenModel.setClientSecret(CLIENT_SECRET);
        refreshTokenModel.setRefreshToken(SharedPrefsData.getInstance(HomeActivity.this).getStringFromSharedPrefs("RefreshToken"));
        Log.e("RefreshToken", "" + new Gson().toJsonTree(refreshTokenModel)
                .getAsJsonObject());
        return new Gson().toJsonTree(refreshTokenModel)
                .getAsJsonObject();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            // Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();

        }
      /*  else if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        } */
        else if (item.getItemId() == R.id.action_cart) {
            //   item.setIcon(buildCounterDrawable(this,20,  R.drawable.ic_birthday));
            // Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        }
        // handle arrow click here

        return super.onOptionsItemSelected(item);
    }

    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(ContextCompat.getColor(HomeActivity.this, R.color.new_accent));
        toolbar.setTitle("f");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitle(getResources().getString(R.string.landline_sname));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                    toolbar.setNavigationIcon(null);
                    toolbar.setTitle("");
                    getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });


    }


    @Override
    public void messageFromParentFragmentToActivity(String myString) {

    }

    @Override
    public void messageFromChildFragmentToActivity(String myString) {
        if (myString.equalsIgnoreCase("moveTowallet")) {
            bottomNavigationView.setSelectedItemId(R.id.action_wallet);
        } else if (myString.equalsIgnoreCase("signout")) {
            val = SharedPrefsData.getInstance(HomeActivity.this).getIntFromSharedPrefs(CommonConstants.ISLOGIN);
            setupToolbar();
            menu = bottomNavigationView.getMenu();
            if (val == CommonConstants.Login) {
                menu.findItem(R.id.action_login).setTitle(getString(R.string.profile));
               /* menu.findItem(R.id.action_home).setTitle(getString(R.string.home));
                menu.findItem(R.id.action_wallet).setTitle(getString(R.string.wallet));
                menu.findItem(R.id.action_offers).setTitle(getString(R.string.offers));*/
            } else {
                menu.findItem(R.id.action_login).setTitle(getString(R.string.login));
                menu.findItem(R.id.action_home).setTitle(getString(R.string.home));
                menu.findItem(R.id.action_wallet).setTitle(getString(R.string.wallet));
                menu.findItem(R.id.action_offers).setTitle(getString(R.string.offers));
            }
        } else {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }

    }


    public class BottomNavigationViewHelper {

        @SuppressLint("RestrictedApi")
        public void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBottomIcons();
    }

    private void updateBottomIcons() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("homeTag");
        if (fragment instanceof HomeFragment) {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }
    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            HomeActivity.toolbar.setNavigationIcon(null);
            HomeActivity.toolbar.setTitle("");

         //   Toast.makeText(this, R.string.toast_obback, Toast.LENGTH_SHORT).show();

            getSupportFragmentManager().popBackStackImmediate();

        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            final Dialog dialog = new Dialog(HomeActivity.this);
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
                    finish();
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (refrshSubscription != null) {
            refrshSubscription.unsubscribe();
        }

    }


}