package com.calibrage.payzanconsumer.framework.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calibrage.payzanconsumer.R;


/**
 * Created by Calibrage11 on 9/21/2017.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyHolder> {
    private Context context;

    public MyOrderAdapter(Context context){
        this.context = context;

    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_myorders, null);
        MyOrderAdapter.MyHolder mh = new MyOrderAdapter.MyHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
