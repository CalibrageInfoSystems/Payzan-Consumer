package com.calibrage.payzanconsumer.framework.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.model.OperatorModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Calibrage11 on 10/16/2017.
 */

public class GenericAdapter<T> extends ArrayAdapter<T> {

    private Context context;
    private int resource;
    private ArrayList<OperatorModel.ListResult> data;
    private AdapterOnClick adapterOnClick;


    public GenericAdapter(Context context, List<T> tList, int res){
        super(context, res, tList);
        data = (ArrayList<OperatorModel.ListResult>) tList;
        this.context =context;
        resource=res;

    }
    @Override
    public int getCount() {
        return data.size();
    }



    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(resource, viewGroup, false);
            }

            TextView name = (TextView) convertView.findViewById(R.id.itemId);
            name.setText(data.get(i).getName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterOnClick.adapterOnClick(i);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return
                new Filter() {
                    @Override
                    public String convertResultToString(Object resultValue) {
                        return ((OperatorModel.ListResult) resultValue).getName();
                    }
                    @Override
                    protected FilterResults performFiltering(CharSequence charSequence) {

                        if (charSequence != null) {
                            for (OperatorModel.ListResult data1 : data ) {
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

    public  interface  AdapterOnClick{
        void adapterOnClick(int position);
    }

    public void setAdapterOnClick(AdapterOnClick adapterOnClick){
        this.adapterOnClick = adapterOnClick;
    }
}
