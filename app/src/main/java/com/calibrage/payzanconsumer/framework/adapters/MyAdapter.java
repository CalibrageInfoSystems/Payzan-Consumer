package com.calibrage.payzanconsumer.framework.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.model.Contact;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Calibrage11 on 9/18/2017.
 */

public class MyAdapter extends ArrayAdapter<Contact> {
    private final Context mContext;
    private final List<Contact> mDepartments;
    private final List<Contact> mDepartments_All;
    private final List<Contact> mDepartments_Suggestion;
    private final int mLayoutResourceId;
    private AdapterOnClick adapterOnClick;

    public MyAdapter(Context context, int resource, List<Contact> departments) {
        super(context, resource, departments);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mDepartments = new ArrayList<>(departments);
        this.mDepartments_All = new ArrayList<>(departments);
        this.mDepartments_Suggestion = new ArrayList<>();
    }

    public int getCount() {
        return mDepartments.size();
    }

    public Contact getItem(int position) {
        return mDepartments.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }
            Contact contact = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView operator = (TextView) convertView.findViewById(R.id.operator);
            name.setText(contact.getName());
            operator.setText(contact.getPhoneNumber());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterOnClick.adapterOnClick(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((Contact) resultValue).getName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    mDepartments_Suggestion.clear();
                    for (Contact department : mDepartments_All) {
                        if (department.getPhoneNumber().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            mDepartments_Suggestion.add(department);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mDepartments_Suggestion;
                    filterResults.count = mDepartments_Suggestion.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDepartments.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof Contact) {
                            mDepartments.add((Contact) object);
                        }
                    }
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    mDepartments.addAll(mDepartments_All);
                }
                notifyDataSetChanged();
            }
        };
    }
    public void setAdapterOnClick(AdapterOnClick adapterOnClick){
        this.adapterOnClick = adapterOnClick;
    }
    public  interface  AdapterOnClick{
        void adapterOnClick(int position);
    }
}