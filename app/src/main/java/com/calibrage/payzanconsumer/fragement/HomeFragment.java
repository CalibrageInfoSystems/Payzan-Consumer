package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.BroadbandActivity;
import com.calibrage.payzanconsumer.activity.CabletvActivity;
import com.calibrage.payzanconsumer.activity.DTHActivity;
import com.calibrage.payzanconsumer.activity.DataCardActivity;
import com.calibrage.payzanconsumer.activity.ElectricityActivity;
import com.calibrage.payzanconsumer.activity.LandLineActivity;
import com.calibrage.payzanconsumer.activity.MobileRechargeActivity;
import com.calibrage.payzanconsumer.activity.RequestForAgent;
import com.calibrage.payzanconsumer.activity.WaterActivity;
import com.calibrage.payzanconsumer.framework.adapters.RechargeAdapter;
import com.calibrage.payzanconsumer.framework.adapters.WalletAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.interfaces.CommunicateFragments;
import com.calibrage.payzanconsumer.framework.interfaces.ImageItemClickListener;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentToActivityInteractionListener;
import com.calibrage.payzanconsumer.framework.interfaces.RechargeClickListiner;
import com.calibrage.payzanconsumer.framework.interfaces.TransctionClickListiner;
import com.calibrage.payzanconsumer.framework.model.WalletBalanceResponce;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.Animationt;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragment extends BaseFragment implements RechargeClickListiner,TransctionClickListiner,CommunicateFragments, ImageItemClickListener {
    public static final String TAG = HomeFragment.class.getSimpleName();
    private View view;
    private RecyclerView recharge_recylerview,recylerviewbanner,recylerviewbook,recylerviewpay;
    private ArrayList<Pair<Integer,String>> rechargePairList = new ArrayList<>();
    private ArrayList<Pair<Integer,String>> payPairList = new ArrayList<>();
    private Context context;
    public static TextView AgentRequestTxt,walletTxt;
   // private CommunicateFragments communicateFragments;
    private OnChildFragmentToActivityInteractionListener mListener;
  //  private ArrayList<Integer> bannerArrayList;
    private static ViewPager viewPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
     CirclePageIndicator indicator;
     private SwipeRefreshLayout lyt_balanceRefresh;
    private Subscription mGetTransactions;
    private static final Integer[] IMAGES= {R.drawable.ban,R.drawable.banf,R.drawable.bant,R.drawable.bans};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setHasOptionsMenu(true);
        CommonUtil.adjustSoftKeyboard(getActivity().getWindow());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_screen,container,false);
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }

        });
        context =this.getActivity();
        lyt_balanceRefresh=(SwipeRefreshLayout)view.findViewById(R.id.lyt_balanceRefresh);
        if(checkUserLoginStatusWallet(TAG)){
            int values = SharedPrefsData.getInstance(getActivity()).getIntFromSharedPrefs("WalletIDValue");

                getTransactions(values);

        }

        lyt_balanceRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int values = SharedPrefsData.getInstance(getActivity()).getIntFromSharedPrefs("WalletIDValue");
                if(isOnline(context)){
                    if(checkUserLoginStatusWallet(TAG)){
                        getTransactions(values);
                    }else {
                        lyt_balanceRefresh.setRefreshing(false);
                    }
                }else {
                    lyt_balanceRefresh.setRefreshing(false);
                    showToast(context,getString(R.string.no_internet));
                }

            }


        });


        hideKeyboard(context);
        init();
        recharge_recylerview = (RecyclerView) view.findViewById(R.id.recylerview);
        recylerviewbook = (RecyclerView) view.findViewById(R.id.recylerviewbook);
        recylerviewpay = (RecyclerView) view.findViewById(R.id.recylerviewpay);
        AgentRequestTxt = (TextView) view.findViewById(R.id.AgentRequestTxt);
        walletTxt = (TextView) view.findViewById(R.id.walletTxt);
        rechargePairList.add(Pair.create(R.drawable.ic_mobile,getString(R.string.mobile)));
        rechargePairList.add(Pair.create(R.drawable.ic_landline, getString(R.string.landline)));
        rechargePairList.add(Pair.create(R.drawable.ic_dth, getString(R.string.dth_sname)));
        rechargePairList.add(Pair.create(R.drawable.ic_internet, getString(R.string.broadband_sname)));
        rechargePairList.add(Pair.create(R.drawable.ic_television, getString(R.string.cabletv_sname)));
        rechargePairList.add(Pair.create(R.drawable.ic_electricity, getString(R.string.electricity_sname)));
        rechargePairList.add(Pair.create(R.drawable.ic_water_tap, getString(R.string.water_sname)));
        rechargePairList.add(Pair.create(R.drawable.ic_data_card, getString(R.string.datacard_sname)));

        payPairList.add(Pair.create(R.drawable.ic_pay_send,getString(R.string.paysemd)));
        payPairList.add(Pair.create(R.drawable.ic_add_withdraw,getString(R.string.add_withdraw)));
        payPairList.add(Pair.create(R.drawable.ic_mytransactions,getString(R.string.MyTransactions)));

        WalletAdapter walletAdapter = new WalletAdapter(context,payPairList);
        recylerviewpay.setAdapter(walletAdapter);
        walletAdapter.setOnAdapterListener(HomeFragment.this);
        recylerviewpay.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));


        RechargeAdapter rechargeAdapter = new RechargeAdapter(context,rechargePairList);
         rechargeAdapter.setOnAdapterListener(HomeFragment.this);
        recharge_recylerview.setAdapter(rechargeAdapter);
        recharge_recylerview.setLayoutManager(new GridLayoutManager(context,4));

        recylerviewbook.setAdapter(rechargeAdapter);
        recylerviewbook.setLayoutManager(new GridLayoutManager(context,4));
         rechargeAdapter.setOnAdapterListener(HomeFragment.this);

        AgentRequestTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, RequestForAgent.class));
            }
        });

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAdapterClickListiner(int pos) {


        switch (pos){
            case 0:{
                hideKeyboard(context);
                   Intent intent=new Intent(context,MobileRechargeActivity.class);
                   startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT,context);
                break;
            }case 1:{
                hideKeyboard(context);
                Intent intent=new Intent(context, LandLineActivity.class);
                startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT,context);
                break;
            }case 2:{
                hideKeyboard(context);
                Intent intent=new Intent(context, DTHActivity.class);
                startActivityWithAnimation(intent,Animationt.SLIDE_IN_RIGHT,context);
                break;
            }case 3:{
                hideKeyboard(context);
                   Intent intent=new Intent(context, BroadbandActivity.class);
                   startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT,context);
                break;
            }case 4:{
                hideKeyboard(context);
                   Intent intent=new Intent(context, CabletvActivity.class);
                startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT,context);
                break;
            }case 5:{
                hideKeyboard(context);
                     Intent intent=new Intent(context, ElectricityActivity.class);
                startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT,context);
                break;
            }case 6:{
                hideKeyboard(context);
                Intent intent=new Intent(context, WaterActivity.class);
                startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT,context);
                break;
            }case 7:{
                hideKeyboard(context);
               Intent intent=new Intent(context, DataCardActivity.class);
                startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT,context);
                break;
            }
            default:
        }


    }



    private void showPopup(int position) {

        View popView = LayoutInflater.from(context).inflate(R.layout.alert_image_alert, null);
        final PopupWindow popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        TextView textView = (TextView) popView.findViewById(R.id.txt);
        ImageView imageView1 = (ImageView) popView.findViewById(R.id.imageBanner);
        imageView1.setImageResource((int)ImagesArray.get(position));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(popView, 0, 0);

    }
    public  void getTransactions(int walletId) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        String url = ApiConstants.BASE_URL+ApiConstants.BalenceBYID + walletId;
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
//                        Toast.makeText(context, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(WalletBalanceResponce transactionResponseModel) {
                        hideDialog();
                        walletTxt.setText((" " + transactionResponseModel.getResult().getBalance()));
                       // Long val= Long.valueOf(transactionResponseModel.getResult().getBalance());
                       // SharedPrefsData.getInstance(getActivity()).saveWalletIdMoney(context, val);
                        SharedPrefsData.getInstance(getActivity()).saveWalletBalance(context, String.valueOf(transactionResponseModel.getResult().getBalance()));
                        // Toast.makeText(context, "Wallet Balance: "+transactionResponseModel.getResult().getBalance(), Toast.LENGTH_SHORT).show();
                        lyt_balanceRefresh.setRefreshing(false);

                    }
                });
    }
    private void init() {
        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        viewPager = (ViewPager)view.findViewById(R.id.pager);
        viewPager.setPageMargin(
                getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));

        viewPager.setAdapter(new SlidshowAdapter(context,ImagesArray));


        indicator = (CirclePageIndicator)view.findViewById(R.id.indicator);

        indicator.setViewPager(viewPager);

        final float density = getResources().getDisplayMetrics().density;


        indicator.setRadius(5 * density);

        NUM_PAGES =IMAGES.length;


        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 3000, 3000);


        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }



//    @Override
//    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
//        // Do something that differs the Activity's menu here
//        super.onCreateOptionsMenu(menu, inflater);
//
//
//        inflater.inflate(R.menu.search, menu);//Menu Resource, Menu
//       // this.menu = menu;
//        MenuItem menuItem = menu.findItem(R.id.action_cart);
//        menuItem.setIcon(buildCounterDrawable(context,2,  R.drawable.ic_notification));
//        MenuItem item = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setQueryHint("search");
//        searchView.setIconifiedByDefault(false);
//        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchEditText.setTextColor(ContextCompat.getColor(context,R.color.white));
//        searchEditText.setHintTextColor((ContextCompat.getColor(context,R.color.white)));
//        try {
//            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
//            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
//            f.setAccessible(true);
//            f.set(searchEditText, R.drawable.cursor);
//        } catch (Exception ignored) {
//        }
////        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
////        TextView textView = (TextView) searchView.findViewById(id);
////        textView.setTextColor(Color.WHITE);
//
////        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
////        ImageView magImage = (ImageView) searchView.findViewById(magId);
////        magImage.setLayoutParams(new LinearLayoutCompat.LayoutParams(0,0));
//       // searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) context);
//
//        MenuItemCompat.setOnActionExpandListener(item,
//                new MenuItemCompat.OnActionExpandListener() {
//                    @Override
//                    public boolean onMenuItemActionCollapse(MenuItem item) {
//// Do something when collapsed
//
//                        return true; // Return true to collapse action view
//                    }
//
//                    @Override
//                    public boolean onMenuItemActionExpand(MenuItem item) {
//// Do something when expanded
//                        return true; // Return true to expand action view
//                    }
//                });
//
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_search) {
//
//        }else if (item.getItemId() == android.R.id.home) {
//            //finish(); // close this activity and return to preview activity (if there is any)
//        }else if(item.getItemId()==R.id.action_cart){
//            //   item.setIcon(buildCounterDrawable(this,20,  R.drawable.ic_birthday));
//            Toast.makeText(context
//
//                    , "clicked", Toast.LENGTH_SHORT).show();
//        }
//        // handle arrow click here
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onResume() {
        super.onResume();
        walletTxt.setText(String.valueOf(SharedPrefsData.getInstance(context).getWalletBalance(context)));


    }



    @Override
    public void onPause() {
        super.onPause();
        walletTxt.setText(String.valueOf(SharedPrefsData.getInstance(context).getWalletBalance(context)));

    }

    @Override
    public void onTransAdapterClickListiner(int pos) {
        TransactionMainFragment transactionMainFragment = new TransactionMainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", pos);
        transactionMainFragment.setArguments(bundle);
      //  transactionMainFragment.setFragmentCommunication(HomeFragment.this);

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, transactionMainFragment, "walletTag");
        mListener.messageFromChildFragmentToActivity("moveTowallet");
        //ft.addToBackStack("mobileTag");
        ft.commit();

//        if(pos==0){
//
//        }else if(pos==1){
//
//        }else if(pos==2){
//
//        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChildFragmentToActivityInteractionListener) {
            mListener = (OnChildFragmentToActivityInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGetTransactions != null) {
            mGetTransactions.unsubscribe();
        }
    }


//    public void setFragmentCommunication(CommunicateFragments  fragmentCommunication){
//        this.communicateFragments = fragmentCommunication;
//    }

    @Override
    public void onFragmentInteraction(String id) {
        //  communicateFragments.onFragmentInteraction(id);
    }

    @Override
    public void setbannerOnAdapterListener(int pos) {
        showPopup(pos);
    }


//    @Override
//    public void messageFromChildToParent(String myString) {
//
//    }



    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }
}
