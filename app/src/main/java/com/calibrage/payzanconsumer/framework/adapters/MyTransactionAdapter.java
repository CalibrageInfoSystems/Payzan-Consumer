package com.calibrage.payzanconsumer.framework.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.model.TransactionResponseModel;
import com.calibrage.payzanconsumer.framework.util.PayZanEnums;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Calibrage11 on 10/20/2017.
 */

public class MyTransactionAdapter extends RecyclerView.Adapter<MyTransactionAdapter.MyHolder> {
    private List<TransactionResponseModel.ListResult> listResults;
    private Context context;
    private String createdDate;

    public MyTransactionAdapter(Context context, List<TransactionResponseModel.ListResult> listResults){
        this.context = context;
        this.listResults = listResults;

    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_my_transactions, null);
        MyTransactionAdapter.MyHolder mh = new MyTransactionAdapter.MyHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if(listResults.get(holder.getAdapterPosition()).getTransactionType().equalsIgnoreCase(PayZanEnums.TransactionsEnum.Deposit.TID())){

        }else if(listResults.get(holder.getAdapterPosition()).getTransactionType().equalsIgnoreCase(PayZanEnums.TransactionsEnum.Transfer.TID())){

        }else if(listResults.get(holder.getAdapterPosition()).getTransactionType().equalsIgnoreCase(PayZanEnums.TransactionsEnum.WithDrawal.TID())){

        }
        holder.imageACK.setImageResource(R.drawable.ic_up_arrow);
        holder.amountTxt.setText(String.valueOf(listResults.get(holder.getAdapterPosition()).getAmount()));

        createdDate = listResults.get(holder.getAdapterPosition()).getCreated().toString();
        /*holder.dateTxt.setText(listResults.get(holder.getAdapterPosition()).getCreated());*/
        holder.dateTxt.setText(formatDateTimeUi());
        holder.paymentModeTxt.setText(listResults.get(holder.getAdapterPosition()).getReasonType());
        holder.fromTxt.setText(listResults.get(holder.getAdapterPosition()).getTransactionId());

    }

    private String formatDateTimeUi() {
        String date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date newDate = format.parse(createdDate);
            format = new SimpleDateFormat("dd/MM/yyyy");
            date = format.format(newDate);
            return date;
        } catch (Exception e) {
            return date;
        }
    }

    @Override
    public int getItemCount() {
        return listResults.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private ImageView imageACK;
        private TextView paymentModeTxt,fromTxt,amountTxt,dateTxt;
        public MyHolder(View itemView) {
            super(itemView);
            imageACK = (ImageView)itemView.findViewById(R.id.imageACK);
            paymentModeTxt = (TextView) itemView.findViewById(R.id.paymentModeTxt);
            fromTxt = (TextView) itemView.findViewById(R.id.fromTxt);
            amountTxt = (TextView) itemView.findViewById(R.id.amountTxt);
            dateTxt = (TextView) itemView.findViewById(R.id.dateTxt);

        }
    }
}
