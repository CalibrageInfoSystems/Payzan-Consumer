package com.calibrage.payzanconsumer.framework.adapters;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.interfaces.TransctionClickListiner;

import java.util.ArrayList;


/**
 * Created by Calibrage11 on 10/18/2017.
 */

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.MyHolder> {
    private ArrayList<Pair<Integer,String >> itemsPairArrayList;
    private Context context;
    private TransctionClickListiner transctionClickListiner;

    public WalletAdapter(Context context, ArrayList<Pair<Integer,String >> itemsPairArrayList){
        this.context = context;
        this.itemsPairArrayList = itemsPairArrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_pay, null);
        WalletAdapter.MyHolder mh = new WalletAdapter.MyHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.imageView.setImageResource(itemsPairArrayList.get(holder.getAdapterPosition()).first);
        holder.textView.setText(itemsPairArrayList.get(holder.getAdapterPosition()).second);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transctionClickListiner.onTransAdapterClickListiner(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsPairArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public MyHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.image);
            textView=(TextView) itemView.findViewById(R.id.text);
        }
    }
    public void setOnAdapterListener(TransctionClickListiner transctionClickListiner) {
        this.transctionClickListiner = transctionClickListiner;
    }

}
