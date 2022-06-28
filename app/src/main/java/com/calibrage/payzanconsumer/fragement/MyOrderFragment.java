package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.adapters.MyOrderAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;


/**
 * Created by Calibrage11 on 9/21/2017.
 */

public class MyOrderFragment extends BaseFragment {
    public static final String TAG = MyOrderFragment.class.getSimpleName();
    private View view;
    private RecyclerView  recyclerView;
    private Context context;
    private LinearLayoutManager layoutManager;
    private boolean isLoading = false;
    private boolean hasMoreItems = true;

    public static MyOrderFragment newInstance(String i, int position) {
        MyOrderFragment f = new MyOrderFragment();
        Bundle args = new Bundle();
        args.putString("CategoryValue", i);
        args.putInt("position", position);
        f.setArguments(args);
        return f;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fargment_myorders,container,false);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        context =this.getActivity();
        recyclerView =(RecyclerView)view.findViewById(R.id.recylerview);
        layoutManager =new LinearLayoutManager(context);
        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(context);
        recyclerView.setAdapter(myOrderAdapter);
        recyclerView.setLayoutManager(layoutManager);


         RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        if (!hasMoreItems) {
//                        Toast.makeText(SearchFarmerScreen.this, "No more items", Toast.LENGTH_SHORT).show();
                        } else {
                            isLoading = true;

                        }

                    }
                }
            }
        };
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        return view;
    }
}
