package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentInteractionListener;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentToActivityInteractionListener;
import com.calibrage.payzanconsumer.framework.model.WalletBalanceResponce;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TransactionMainFragment extends BaseFragment {
    public static final String TAG = TransactionMainFragment.class.getSimpleName();
    private TabLayout tabs;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Context context;
    private View rootview;
    private int currentItem;
    public static TextView walletBalanceTxt;
    private SwipeRefreshLayout lyt_balanceRefresh;
    //  private CommunicateFragments  communicateFragments;
    private OnChildFragmentToActivityInteractionListener mActivityListener;
    private OnChildFragmentInteractionListener mParentListener;
    private Subscription mGetTransactions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentItem = bundle.getInt("position", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_transactions, container, false);
        rootview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        context = getActivity();
        ((AppCompatActivity) getActivity()).setSupportActionBar(HomeActivity.toolbar);
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.wallet_sname));


        lyt_balanceRefresh = rootview.findViewById(R.id.lyt_balanceRefresh);
        HomeActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeTab();
            }
        });
        tabs = (TabLayout) rootview.findViewById(R.id.tabs);
        walletBalanceTxt = (TextView) rootview.findViewById(R.id.walletBalanceTxt);
        walletBalanceTxt.setText((getString(R.string.wallet_balance)) + " " + String.valueOf(SharedPrefsData.getInstance(context).getWalletBalance(context)));
        // tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager = (ViewPager) rootview.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(currentItem, true);
        tabs.setupWithViewPager(viewPager);

        rootview.setFocusableInTouchMode(true);
        rootview.requestFocus();
        rootview.setOnKeyListener(new View.OnKeyListener() {
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
        lyt_balanceRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isOnline(context)){
                    int values = SharedPrefsData.getInstance(getActivity()).getIntFromSharedPrefs("WalletIDValue");
                    if (checkUserLoginStatusWallet(TAG)) {
                        getTransactions(values);
                    } else {
                        lyt_balanceRefresh.setRefreshing(false);
                    }
                }else {
                    showToast(context,getString(R.string.no_internet));
                    lyt_balanceRefresh.setRefreshing(false);
                }
                // Toast.makeText(context, "OnRefreshListener", Toast.LENGTH_SHORT).show();



            }


        });



        return rootview;
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
                        // Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(WalletBalanceResponce transactionResponseModel) {
                        hideDialog();
                        walletBalanceTxt.setText(("Wallet Balance: " + transactionResponseModel.getResult().getBalance()));
                     //   Long val = Long.valueOf(transactionResponseModel.getResult().getBalance());
                        SharedPrefsData.getInstance(getActivity()).saveWalletBalance(context, String.valueOf(transactionResponseModel.getResult().getBalance()));

                        // Toast.makeText(context, "Wallet Balance: "+transactionResponseModel.getResult().getBalance(), Toast.LENGTH_SHORT).show();
                        lyt_balanceRefresh.setRefreshing(false);

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGetTransactions != null) {
            mGetTransactions.unsubscribe();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }


    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        String[] strings = new String[]{getString(R.string.send_money_to_wallet), getString(R.string.add_money_to_wallet), getString(R.string.my_transaction)};
//        for (int i = 0; i <strings.length ; i++) {
//            final MyOrderFragment myBottomSheetSort = MyOrderFragment.
//                    newInstance("",0);
//            adapter.addFragment(myBottomSheetSort,strings[i]);
//        }

        adapter.addFragment(new SendMoneyToWallet(), strings[0]);
        adapter.addFragment(new AddMoneyToWallet(), strings[1]);
        adapter.addFragment(new MyTransactions(), strings[2]);
        viewPager.setAdapter(adapter);


    }

//    public void setFragmentCommunication(CommunicateFragments  fragmentCommunication){
//        this.communicateFragments = fragmentCommunication;
//    }

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

    private void closeTab() {

        HomeActivity.toolbar.setNavigationIcon(null);
        HomeActivity.toolbar.setTitle("");
        replaceFragment(getActivity(), MAIN_CONTAINER, new HomeFragment(), TAG, TransactionMainFragment.TAG);
       /* Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("walletTag");
       // communicateFragments.onFragmentInteraction("");

        mActivityListener.messageFromChildFragmentToActivity("handleBottomNavigation");

        if (fragment != null)
        {
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            HomeActivity.toolbar.setNavigationIcon(null);
            HomeActivity.toolbar.setTitle("");

        }*/

    }

}
