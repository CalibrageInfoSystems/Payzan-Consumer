package com.calibrage.payzanconsumer.fragement.agent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.calibrage.payzanconsumer.R;

import java.util.ArrayList;


/**
 * Created by Calibrage25 on 11/15/2017.
 */

public class AgentCommissionAdapter extends RecyclerView.Adapter<AgentCommissionAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> listComm;
    public AgentCommissionAdapter(Context context, ArrayList<String> listComm) {
        this.context=context;
        this.listComm=listComm;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_commission_adapter,null);
        MyViewHolder mvh=new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(listComm.get(position).toString());
    }



    @Override
    public int getItemCount() {
        return listComm.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.agenttxt1);
        }
    }

}
