package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;

/**
 * Created by Calibrage10 on 1/3/2018.
 */

public class BillsFragment extends BaseFragment {
    private Context mContext;
    private View rootView;
    private RecyclerView addressRecycler;
    private CommonButton btn_addnewAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_addaddress, null, false);
        mContext = getActivity();
        initViews();
        setViews();
        return rootView;
    }

    private void initViews() {
        addressRecycler = (RecyclerView) rootView.findViewById(R.id.addressrecycler);
        btn_addnewAddress = (CommonButton) rootView.findViewById(R.id.btn_addnewAddress);
        btn_addnewAddress.setVisibility(View.GONE);
    }

    private void setViews() {

    }
}
