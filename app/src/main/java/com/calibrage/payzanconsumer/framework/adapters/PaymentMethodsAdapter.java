package com.calibrage.payzanconsumer.framework.adapters;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.interfaces.PaymentMethodClickListiner;

import java.util.ArrayList;


/**
 * Created by Calibrage19 on 13-10-2017.
 */

public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.MyHolder> {
    private ArrayList<Pair<Integer, String>> itemsPairArrayList;
    private Context context;
    private PaymentMethodClickListiner methodClickListiner;
    private int focusedItem = 0;

    public PaymentMethodsAdapter(ArrayList<Pair<Integer, String>> itemsPairArrayList, Context context) {
        this.itemsPairArrayList = itemsPairArrayList;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_new_recharge, null);
        PaymentMethodsAdapter.MyHolder mh = new PaymentMethodsAdapter.MyHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.imageView.setImageResource(itemsPairArrayList.get(holder.getAdapterPosition()).first);
        holder.textView.setText(itemsPairArrayList.get(holder.getAdapterPosition()).second);
        if (focusedItem == position) {
            if (position == 0) {
                holder.mainLay.setBackgroundColor(context.getResources().getColor(R.color.accent));
                holder.imageView.setImageResource(R.drawable.ic_saved_cards_white);
                holder.textView.setTextColor(context.getResources().getColor(R.color.whitepure));
            } else if (position == 1) {
                holder.mainLay.setBackgroundColor(context.getResources().getColor(R.color.accent));
                holder.imageView.setImageResource(R.drawable.ic_credit_card_white);
                holder.textView.setTextColor(context.getResources().getColor(R.color.whitepure));
            } else if (position == 2) {
                holder.mainLay.setBackgroundColor(context.getResources().getColor(R.color.accent));
                holder.imageView.setImageResource(R.drawable.ic_debit_card_white);
                holder.textView.setTextColor(context.getResources().getColor(R.color.whitepure));
            } else if (position == 3) {
                holder.mainLay.setBackgroundColor(context.getResources().getColor(R.color.accent));
                holder.imageView.setImageResource(R.drawable.ic_net_banking_white);
                holder.textView.setTextColor(context.getResources().getColor(R.color.whitepure));
            }
        } else {
            if (position == 0) {
                holder.mainLay.setBackgroundColor(context.getResources().getColor(R.color.whitepure));
                holder.imageView.setImageResource(R.drawable.ic_saved_cards);
                holder.textView.setTextColor(context.getResources().getColor(R.color.black));
            } else if (position == 1) {
                holder.mainLay.setBackgroundColor(context.getResources().getColor(R.color.whitepure));
                holder.imageView.setImageResource(R.drawable.ic_credit_card);
                holder.textView.setTextColor(context.getResources().getColor(R.color.black));
            } else if (position == 2) {
                holder.mainLay.setBackgroundColor(context.getResources().getColor(R.color.whitepure));
                holder.imageView.setImageResource(R.drawable.ic_debit_card);
                holder.textView.setTextColor(context.getResources().getColor(R.color.black));
            } else if (position == 3) {
                holder.mainLay.setBackgroundColor(context.getResources().getColor(R.color.whitepure));
                holder.imageView.setImageResource(R.drawable.ic_net_banking);
                holder.textView.setTextColor(context.getResources().getColor(R.color.black));
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodClickListiner.onPaymentMethodClickListiner(position);

                if (position == RecyclerView.NO_POSITION) return;

                // Updating old as well as new positions
                notifyItemChanged(focusedItem);
                focusedItem = position;
                notifyItemChanged(focusedItem);

            }
        });


    }


    @Override
    public int getItemCount() {
        return itemsPairArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        private View convertView;
        private LinearLayout mainLay;

        public MyHolder(View itemView) {
            super(itemView);
            convertView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.image);
            textView = (TextView) itemView.findViewById(R.id.text);
            mainLay = (LinearLayout) itemView.findViewById(R.id.mainLay);
        }
    }

    public void setOnAdapterListener(PaymentMethodClickListiner onAdapterListener) {
        this.methodClickListiner = onAdapterListener;
    }
}
