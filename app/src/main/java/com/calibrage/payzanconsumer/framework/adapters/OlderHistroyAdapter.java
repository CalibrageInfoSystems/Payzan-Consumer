package com.calibrage.payzanconsumer.framework.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.model.OrderModel;

import java.util.List;

/**
 * Created by Calibrage25 on 11/21/2017.
 */

public class OlderHistroyAdapter extends RecyclerView.Adapter<OlderHistroyAdapter.MyViewHolder> {
   Context ctx;
    List<OrderModel> listdata;


    public OlderHistroyAdapter(Context context, List<OrderModel> listdata) {
        this.ctx= context;
        this.listdata=listdata;
    }

    @Override
    public OlderHistroyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_adapter,null);
        OlderHistroyAdapter.MyViewHolder mvh=new OlderHistroyAdapter.MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(OlderHistroyAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.cardview);
        }
    }
}
