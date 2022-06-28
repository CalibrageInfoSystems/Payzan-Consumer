package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.framework.adapters.OlderHistroyAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.model.OrderModel;

import java.util.ArrayList;
import java.util.List;


public class OrderHistoryFragment extends BaseFragment {
    public static final String TAG = OrderHistoryFragment.class.getSimpleName();
    Context context;
    View  view;
     RecyclerView olderhistroyrecycler;
     List<OrderModel> listdata;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view= inflater.inflate(R.layout.fragment_order_history, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        olderhistroyrecycler=(RecyclerView)view.findViewById(R.id.olderhistroyrecycler);
        OlderHistroyAdapter olderHistroyAdapter=new OlderHistroyAdapter(context,getdata());
        olderhistroyrecycler.setAdapter(olderHistroyAdapter);
        olderhistroyrecycler.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        ((AppCompatActivity) getActivity()).setSupportActionBar(HomeActivity.toolbar);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.order_history));
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);

        HomeActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeTab();
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
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


        return view;
    }

    private   List<OrderModel> getdata()
    {
        List<OrderModel> data=new ArrayList<>();
        data.add(new OrderModel("214325745674","500","sucess"));
        data.add(new OrderModel("214325745674","500","sucess"));
        data.add(new OrderModel("214325745674","500","sucess"));
        data.add(new OrderModel("214325745674","500","sucess"));


        return  data;


    }

    private void closeTab() {

        HomeActivity.toolbar.setNavigationIcon(null);
        HomeActivity.toolbar.setTitle("");
        replaceFragment(getActivity(), MAIN_CONTAINER, new UserProfileHome(), TAG, OrderHistoryFragment.TAG);

    }




}
