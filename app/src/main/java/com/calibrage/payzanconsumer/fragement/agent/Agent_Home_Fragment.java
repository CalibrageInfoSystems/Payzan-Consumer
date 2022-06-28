package com.calibrage.payzanconsumer.fragement.agent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.RequestForAgent;
import com.calibrage.payzanconsumer.fragement.BroadbandFragment;
import com.calibrage.payzanconsumer.fragement.MobileRecharge;
import com.calibrage.payzanconsumer.fragement.TransactionMainFragment;
import com.calibrage.payzanconsumer.framework.adapters.BannerAdapter;
import com.calibrage.payzanconsumer.framework.adapters.RechargeAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.interfaces.CommunicateFragments;
import com.calibrage.payzanconsumer.framework.interfaces.ImageItemClickListener;
import com.calibrage.payzanconsumer.framework.interfaces.OnChildFragmentToActivityInteractionListener;
import com.calibrage.payzanconsumer.framework.interfaces.RechargeClickListiner;
import com.calibrage.payzanconsumer.framework.interfaces.TransctionClickListiner;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;

import java.util.ArrayList;


public class Agent_Home_Fragment extends BaseFragment implements RechargeClickListiner,TransctionClickListiner,CommunicateFragments, ImageItemClickListener {

    public static final String TAG = Agent_Home_Fragment.class.getSimpleName();
    private View view;
    private RecyclerView recharge_recylerview,recylerviewbanner,recylerviewbook,recylerviewpay;
    private ArrayList<Pair<Integer,String>> rechargePairList = new ArrayList<>();
    private ArrayList<Pair<Integer,String>> payPairList = new ArrayList<>();
    private Context context;
    private CardView crv_transaction,crv_commissions;
    public static TextView AgentRequestTxt,walletTxt;
    // private CommunicateFragments communicateFragments;
    private OnChildFragmentToActivityInteractionListener mListener;
    private ArrayList<Integer> bannerArrayList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setHasOptionsMenu(true);
        CommonUtil.adjustSoftKeyboard(getActivity().getWindow());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_agent__home,container,false);
        context =this.getActivity();

        //  Toast.makeText(context, "testing in fragment", Toast.LENGTH_SHORT).show();


//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        toolbar.setTitle("f");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recharge_recylerview = (RecyclerView) view.findViewById(R.id.recylerview);
        recylerviewbanner = (RecyclerView) view.findViewById(R.id.recylerviewbanner);
        recylerviewbook = (RecyclerView) view.findViewById(R.id.recylerviewbook);
        recylerviewpay = (RecyclerView) view.findViewById(R.id.recylerviewpay);
        AgentRequestTxt = (TextView) view.findViewById(R.id.AgentRequestTxt);
        walletTxt = (TextView) view.findViewById(R.id.walletTxt);
        crv_transaction=(CardView)view.findViewById(R.id.crv_transaction);
        crv_commissions=(CardView)view.findViewById(R.id.crv_commissions);
        //  pay_recylerview = (RecyclerView)findViewById(R.id.pay_recylerview);
        rechargePairList.add(Pair.create(R.drawable.ic_mobile, "Mobile"));
        rechargePairList.add(Pair.create(R.drawable.ic_landline, "Landline"));
        rechargePairList.add(Pair.create(R.drawable.ic_dth, "DTH"));
        rechargePairList.add(Pair.create(R.drawable.ic_internet, "Broadband"));
        rechargePairList.add(Pair.create(R.drawable.ic_television, "Cable TV"));
        rechargePairList.add(Pair.create(R.drawable.ic_electricity, "Electricity"));
        rechargePairList.add(Pair.create(R.drawable.ic_water_tap, "Water"));
        rechargePairList.add(Pair.create(R.drawable.ic_data_card, "Data Card"));

        payPairList.add(Pair.create(R.drawable.ic_pay_send,"Pay/Send"));
        payPairList.add(Pair.create(R.drawable.ic_add_withdraw,"Add/Withdrawal"));
        payPairList.add(Pair.create(R.drawable.ic_mytransactions,"My transctions"));

//        WalletAdapter walletAdapter = new WalletAdapter(context,payPairList);
//        recylerviewpay.setAdapter(walletAdapter);
//        walletAdapter.setOnAdapterListener(Agent_Home_Fragment.this);
//        recylerviewpay.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));


        RechargeAdapter rechargeAdapter = new RechargeAdapter(context,rechargePairList);
        rechargeAdapter.setOnAdapterListener(Agent_Home_Fragment.this);
        recharge_recylerview.setAdapter(rechargeAdapter);
        recharge_recylerview.setLayoutManager(new GridLayoutManager(context,4));

        recylerviewbook.setAdapter(rechargeAdapter);
        recylerviewbook.setLayoutManager(new GridLayoutManager(context,4));
        rechargeAdapter.setOnAdapterListener(Agent_Home_Fragment.this);
        bannerArrayList = new ArrayList<>();
        bannerArrayList.add(R.drawable.bant);
        bannerArrayList.add(R.drawable.bans);
        bannerArrayList.add(R.drawable.ban);
        bannerArrayList.add(R.drawable.banf);
        BannerAdapter bannerAdapter = new BannerAdapter(context,bannerArrayList);
        bannerAdapter.setbannerOnAdapterListener(Agent_Home_Fragment.this);
        recylerviewbanner.setAdapter(bannerAdapter);
        recylerviewbanner.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

        AgentRequestTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, RequestForAgent.class));
            }
        });

        crv_commissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // replaceFragment(getActivity(), MAIN_CONTAINER, new Agent_Commission_Fragment(), TAG, TestFragment.TAG);
            }
        });
        crv_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  replaceFragment(getActivity(), MAIN_CONTAINER, new Agent_Transaction_Fragment(), TAG, TestFragment.TAG);
            }
        });
        return view;
    }


    @Override
    public void onAdapterClickListiner(int pos) {


        switch (pos){
            case 0:{
                /*final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.content_frame, new MobileRecharge(), "mobileTag");
                //ft.addToBackStack("mobileTag");
                ft.commit();*/
               /* addFragment(getActivity(), MAIN_CONTAINER, new MobileRecharge(), TAG, MobileRecharge.TAG);
*/

              //  replaceFragment(getActivity(), MAIN_CONTAINER, new MobileRecharge(), TAG, TestFragment.TAG);

                break;
            }case 1:{
                /*final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.content_frame, new PayLandLineBill(), "landlineTag");
                //ft.addToBackStack("mobileTag");
                ft.commit();*/

             //   replaceFragment(getActivity(), MAIN_CONTAINER, new PayLandLineBill(), TAG, PayLandLineBill.TAG);
                break;
            }case 2:{
                /*final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.content_frame, new PayDTHFragment(), "dthTag");
                //ft.addToBackStack("mobileTag");
                ft.commit();*/

              //  replaceFragment(getActivity(), MAIN_CONTAINER, new PayDTHFragment(), TAG, PayDTHFragment.TAG);
                break;
            }case 3:{
              /* // startActivity(new Intent(context, PayLandLineBill.class));broadbandTag
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.content_frame, new BroadbandFragment(), "broadbandTag");
                //ft.addToBackStack("mobileTag");
                ft.commit();*/

               // replaceFragment(getActivity(), MAIN_CONTAINER, new BroadbandFragment(), TAG, BroadbandFragment.TAG);
                break;
            }case 4:{
              /*  final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.content_frame, new PayCableFragment(), "cableTag");
                //ft.addToBackStack("mobileTag");
                ft.commit();*/

               // replaceFragment(getActivity(), MAIN_CONTAINER, new PayCableFragment(), TAG, PayCableFragment.TAG);

                break;
            }case 5:{
                /*final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.content_frame, new PayElectrictyFragment(), "elctricityTag");
                //ft.addToBackStack("mobileTag");
                ft.commit();*/

             //   replaceFragment(getActivity(), MAIN_CONTAINER, new PayElectrictyFragment(), TAG, PayElectrictyFragment.TAG);
                break;
            }case 6:{
               /* final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.content_frame, new PayWaterFragment(), "waterTag");
                //ft.addToBackStack("mobileTag");
                ft.commit();*/

               // replaceFragment(getActivity(), MAIN_CONTAINER, new PayWaterFragment(), TAG, PayWaterFragment.TAG);
                break;
            }case 7:{
               /* final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.content_frame, new DataCardFragment(), "datacardTag");
                //ft.addToBackStack("mobileTag");
                ft.commit();*/
              //  replaceFragment(getActivity(), MAIN_CONTAINER, new DataCardFragment(), TAG, DataCardFragment.TAG);
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
        imageView1.setImageResource((int)bannerArrayList.get(position));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(popView, 0, 0);

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
        walletTxt.setText(String.valueOf(SharedPrefsData.getInstance(context).getWalletIdMoney(context)));


    }



    @Override
    public void onPause() {
        super.onPause();
        walletTxt.setText(String.valueOf(SharedPrefsData.getInstance(context).getWalletIdMoney(context)));

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
        if(pos==0){

        }else if(pos==1){

        }else if(pos==2){

        }

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
