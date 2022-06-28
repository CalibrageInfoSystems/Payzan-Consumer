package com.calibrage.payzanconsumer.framework.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.interfaces.ImageItemClickListener;

import java.util.ArrayList;


/**
 * Created by Calibrage11 on 10/7/2017.
 */

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.MyHolder> {
    private Context context;
    private ArrayList<Integer> bannerArrayList;
    private ImageItemClickListener itemClickListener;

    public BannerAdapter(Context context, ArrayList<Integer> bannerArrayList){
        this.context = context;
        this.bannerArrayList = bannerArrayList;

    }
    @Override
    public BannerAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_banner, null);
        BannerAdapter.MyHolder mh = new BannerAdapter.MyHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final BannerAdapter.MyHolder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(bannerArrayList.get(position).toString()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.setbannerOnAdapterListener(holder.getAdapterPosition());
            }
        });
    }



    @Override
    public int getItemCount() {
        return bannerArrayList.size();
    }



    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public MyHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image);

        }


    }

    public  void setbannerOnAdapterListener(ImageItemClickListener onAdapterListener)
    {
        this.itemClickListener=onAdapterListener;
    }


}
