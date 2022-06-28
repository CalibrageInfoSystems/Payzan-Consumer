package com.calibrage.payzanconsumer.framework.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.model.MandalModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Calibrage11 on 10/5/2017.
 */

public class SingleLineDropDownAdapterMandals extends ArrayAdapter<MandalModel.data> {
    private Context context;
    private int resource;
    private ArrayList<MandalModel.data> data;
    private AdapterMandalOnClick AdapterMandalOnClick;




    public SingleLineDropDownAdapterMandals(@NonNull Context context, @LayoutRes int resource, @NonNull List<MandalModel.data> data) {
        super(context, resource, data);
        this.context = context;
        this.resource =resource;
        this.data = (ArrayList<MandalModel.data>) data;
    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(resource, parent, false);
            }

            TextView name = (TextView) convertView.findViewById(R.id.itemId);
            name.setText(data.get(position).getName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AdapterMandalOnClick.AdapterMandalOnClick(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public int getCount() {
     return data.size();
    }

    @Nullable
    @Override
    public MandalModel.data getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return
        new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((MandalModel.data) resultValue).getName();
            }
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                if (charSequence != null) {
                    for (MandalModel.data data1 : data ) {
                        if (data1.getName().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = data;
                    filterResults.count = data.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                //mDepartments.clear();
                if (filterResults != null && filterResults.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    List<?> result = (List<?>) filterResults.values;
//                    for (Object object : result) {
//                        if (object instanceof Contact) {
//                            mDepartments.add((Contact) object);
//                        }
//                    }
//                } else if (constraint == null) {
//                    // no filter, add entire original list back in
//                    mDepartments.addAll(mDepartments_All);
                }
                notifyDataSetChanged();

            }
        };

    }

    public  interface  AdapterMandalOnClick{

        void AdapterMandalOnClick(int position);
    }

    public void setAdapterMandalOnClick(AdapterMandalOnClick adapterOnClick){
        this.AdapterMandalOnClick = adapterOnClick;
    }
}
