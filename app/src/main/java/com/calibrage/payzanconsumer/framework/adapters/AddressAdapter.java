package com.calibrage.payzanconsumer.framework.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.interfaces.RequestClickListiner;
import com.calibrage.payzanconsumer.framework.model.UserAddressModel;

import java.util.ArrayList;

import rx.Subscription;

/**
 * Created by Calibrage25 on 12/27/2017.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<UserAddressModel.ListResult> data;
    int pinCode;
    private RequestClickListiner requestClickListiner;
    private Subscription commentSubscription;
    private OnCartChangedListener onCartChangedListener;


    public AddressAdapter(Context context, ArrayList<UserAddressModel.ListResult> data) {
        this.context = context;
        this.data = data;

    }


    @Override
    public AddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_adapter, null);
        AddressAdapter.MyViewHolder mh = new AddressAdapter.MyViewHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final AddressAdapter.MyViewHolder holder, final int position) {
        holder.tvPostalCode.setText(Html.fromHtml("<font color='#000000'>" + context.getString(R.string.pincode_adp) + "</font> <font color='#454545'>" + data.get(holder.getAdapterPosition()).getPostCode() + "</font>"));
        holder.tvName.setText(Html.fromHtml("<font color='#000000'>" + context.getString(R.string.name_adp) + "</font> <font color='#454545'>" + data.get(holder.getAdapterPosition()).getName() + "</font>"));
        holder.tvAddress1.setText(Html.fromHtml("<font color='#000000'>" + context.getString(R.string.address1_adp) + "</font> <font color='#454545'>" + data.get(holder.getAdapterPosition()).getAddressLine1() + "</font>"));
        holder.tvAddress2.setText(Html.fromHtml("<font color='#000000'>" + context.getString(R.string.address2_adp) + "</font> <font color='#454545'>" + data.get(holder.getAdapterPosition()).getAddressLine2() + "</font>"));


        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCartChangedListener.setCartClickListener("editAddress", position);

            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCartChangedListener.setCartClickListener("delete", position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvAddress1, tvAddress2, tvPostalCode;
        ImageView btnEdit, btnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            tvName = itemView.findViewById(R.id.name_txt);
            tvAddress1 = itemView.findViewById(R.id.address1_txt);
            tvAddress2 = itemView.findViewById(R.id.address2_txt);
            tvPostalCode = itemView.findViewById(R.id.pincode_txt);

        }
    }

    /**
     * Container Activity must implement this interface
     * you can define any parameter as per your requirement
     */
    public interface OnCartChangedListener {
        void setCartClickListener(String status, int position);

    }

    /**
     * @param onCartChangedListener
     */
    public void setOnCartChangedListener(OnCartChangedListener onCartChangedListener) {
        this.onCartChangedListener = onCartChangedListener;
    }
}
