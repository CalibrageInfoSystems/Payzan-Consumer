package com.calibrage.payzanconsumer.framework.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.model.SaveCardsModel;

import java.util.List;

/**
 * Created by Calibrage25 on 11/21/2017.
 */
public class SaveCardsAdapter extends RecyclerView.Adapter<SaveCardsAdapter.MyViewHolder> {
    Context ctx;
    List<SaveCardsModel> listdata;




    public SaveCardsAdapter(Context context, List<SaveCardsModel> listdata) {
        this.ctx= context;
        this.listdata=listdata;
    }

    @Override
    public SaveCardsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.save_cards_adapter,null);
        SaveCardsAdapter.MyViewHolder mvh=new SaveCardsAdapter.MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);

        }
    }
}
