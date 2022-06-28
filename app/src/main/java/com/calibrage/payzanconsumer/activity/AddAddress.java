package com.calibrage.payzanconsumer.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.adapters.AddressAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.model.EditAddressModel;
import com.calibrage.payzanconsumer.framework.model.UserAddressModel;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.Animationt;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddAddress extends BaseActivity implements AddressAdapter.OnCartChangedListener {

    RecyclerView addressRecycler;
    private ArrayList<UserAddressModel.ListResult> listResults;
    private CommonButton btn_addADS;
    private Subscription operatorSubscription, deleteUserAddressSubscription;
    private AddressAdapter addressAdapter;
    private Context context;
    private int listPosition = 0;
    private TextView no_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaddress);
        ShowActionBar(getString(R.string.add_address));
        context = this;

        setView();

    }

    private void setView() {
        btn_addADS = findViewById(R.id.btn_addnewAddress);
        no_items=(TextView)findViewById(R.id.no_items);
        addressRecycler = (RecyclerView) findViewById(R.id.addressrecycler);
        btn_addADS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddAddress.this, AddNewAddressActivity.class).putExtra("comingFrom", "AddNew");
                startActivityWithAnimation(i, Animationt.SLIDE_IN_RIGHT);
            }
        });


    }

    private void getUserAddress(String UserID) {
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        operatorSubscription = service.getUserAddress(ApiConstants.GET_ADDRESS + UserID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserAddressModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override

                    public void onError(Throwable e) {
                        hideProgressDialog();
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
//                        Toast.makeText(getApplicationContext(), getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(UserAddressModel userAddressModel) {
                        listResults = (ArrayList<UserAddressModel.ListResult>) userAddressModel.getListResult();
                        if (userAddressModel.getIsSuccess()) {
                            no_items.setVisibility(View.GONE);
                            addressRecycler.setVisibility(View.VISIBLE);
                            addressAdapter = new AddressAdapter(getApplicationContext(), listResults);
                            addressRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            addressRecycler.setAdapter(addressAdapter);
                            addressRecycler.setHasFixedSize(true);
                            addressAdapter.setOnCartChangedListener(AddAddress.this);
                        }else {
                            no_items.setVisibility(View.VISIBLE);
                            addressRecycler.setVisibility(View.GONE);
                        }
                    }

                });
    }

    private void deleteAddress(String id) {
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        deleteUserAddressSubscription = service.editDeleteAddress(ApiConstants.Delete_Address + id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EditAddressModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override

                    public void onError(Throwable e) {
                        hideProgressDialog();
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
//                        Toast.makeText(getApplicationContext(), getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(EditAddressModel editAddressModel) {
                        if (editAddressModel.getIsSuccess()) {
                            Toast.makeText(getApplicationContext(), "" + editAddressModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                            listResults.remove(listPosition);
                            addressAdapter.notifyDataSetChanged();
                        }

                    }

                });


    }

    @Override
    public void setCartClickListener(String status, final int position) {
        listPosition = position;
        if (status.contains("editAddress")) {
            Intent i = new Intent(AddAddress.this, AddNewAddressActivity.class).putExtra("comingFrom", "Edit")
                    .putExtra("Id", listResults.get(position).getId());
            startActivityWithAnimation(i, Animationt.SLIDE_IN_RIGHT);
        } else if (status.contains("delete")) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want delete this?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(isOnline()){
                                deleteAddress("" + listResults.get(position).getId());
                            }else {
                                showToast(context,getString(R.string.no_internet));
                            }

                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isOnline()){
            getUserAddress(SharedPrefsData.getInstance(this).getUserId(this));
        }else {
            showToast(context,getString(R.string.no_internet));
        }

    }
}
