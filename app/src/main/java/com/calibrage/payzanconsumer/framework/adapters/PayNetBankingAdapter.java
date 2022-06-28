package com.calibrage.payzanconsumer.framework.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.interfaces.PayNetBankListiner;
import com.calibrage.payzanconsumer.framework.interfaces.PaySavedCardsListener;
import com.calibrage.payzanconsumer.framework.model.PayNetBankModel;

import java.util.ArrayList;

/**
 * Created by Calibrage11 on 12/22/2017.
 */

public class PayNetBankingAdapter extends RecyclerView.Adapter<PayNetBankingAdapter.Myholder> {

    private Context context;
    private PayNetBankListiner payNetBankListiner;
    private ArrayList<PayNetBankModel> payNetBankModels = new ArrayList<>();

    public PayNetBankingAdapter(Context context, ArrayList<PayNetBankModel> payNetBankModels) {
        this.payNetBankModels = payNetBankModels;
        this.context = context;

    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_net_bank, null);
        PayNetBankingAdapter.Myholder mvh = new PayNetBankingAdapter.Myholder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final Myholder holder, int position) {

        holder.imageView.setImageResource(payNetBankModels.get(holder.getAdapterPosition()).getImages());

        if (payNetBankModels.get(holder.getAdapterPosition()).getActive()) {

            holder.checkRB.setChecked(true);
        } else {

            holder.checkRB.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payNetBankListiner.OnNetBankClickListiner(holder.getAdapterPosition(), "checkRB");
            }
        });
        holder.checkRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payNetBankListiner.OnNetBankClickListiner(holder.getAdapterPosition(), "checkRB");
            }
        });

    }

    @Override
    public int getItemCount() {
        return payNetBankModels.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private RadioButton checkRB;

        public Myholder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardImg);
            checkRB = itemView.findViewById(R.id.checkRB);
        }
    }

    public void setOnAdapterListener(PayNetBankListiner payNetBankListiner) {
        this.payNetBankListiner = payNetBankListiner;
    }
}
