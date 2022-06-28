package com.calibrage.payzanconsumer.framework.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HelpSupport;
import com.calibrage.payzanconsumer.activity.SupportActivitys;
import com.calibrage.payzanconsumer.framework.interfaces.HelpSupportActivity;
import com.calibrage.payzanconsumer.framework.model.SupportModel;

import java.util.List;

/**
 * Created by Calibrage25 on 12/30/2017.
 */

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.MyViewHolder> {
    Context context;
    List<SupportModel> getdata;
   private HelpSupportActivity helpSupportActivity;

    public SupportAdapter(Context context, List<SupportModel> getdata) {
        this.context=context;
        this.getdata=getdata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.support_adapter,null);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.fstxt.setText(getdata.get(holder.getAdapterPosition()).getHeading());
        holder.sub_head_Txt.setText(getdata.get(holder.getAdapterPosition()).getSubHeading());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpSupportActivity.onItemSupportClickListener(holder.getAdapterPosition());
            }
        });



    }

    @Override
    public int getItemCount() {
        return getdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fstxt,sub_head_Txt;
        public MyViewHolder(View itemView) {
            super(itemView);
            fstxt=itemView.findViewById(R.id.fstxt);
            sub_head_Txt=(TextView)itemView.findViewById(R.id.sub_head_Txt);

        }
    }

    public void onSupportClickListener(HelpSupportActivity onAdapterListener) {
        this.helpSupportActivity = onAdapterListener;
    }
}
