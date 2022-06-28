package com.calibrage.payzanconsumer.framework.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.calibrage.payzanconsumer.R;

import java.util.ArrayList;


/**
 * Created by Calibrage11 on 10/27/2017.
 */

public class TransactionTypeAdapter extends RecyclerView.Adapter<TransactionTypeAdapter.MyHolder> {

    private Context context;
    private ArrayList<String> transactionTypes;

    public TransactionTypeAdapter(Context context, ArrayList<String> transactionTypes){
        this.context = context;
        this.transactionTypes = transactionTypes;

    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_transactions, null);
        TransactionTypeAdapter.MyHolder mh = new TransactionTypeAdapter.MyHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder,final int position) {
        holder.text.setText(transactionTypes.get(holder.getAdapterPosition()));

    }

    @Override
    public int getItemCount() {
        return transactionTypes.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView text;
        public MyHolder(View itemView) {
            super(itemView);
            text = (TextView)itemView.findViewById(R.id.text);
        }
    }
}
