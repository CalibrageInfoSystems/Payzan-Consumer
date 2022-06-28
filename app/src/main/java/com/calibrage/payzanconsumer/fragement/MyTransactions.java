package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.adapters.MyTransactionAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.model.TransactionResponseModel;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Calibrage11 on 10/20/2017.
 */

public class MyTransactions extends BaseFragment implements View.OnClickListener {
    public static final String TAG = MyTransactions.class.getSimpleName();
    private View rootView;
    private Button btnAll, btnPaid, btnReceived, btnAdded;
    private Context context;
    private RecyclerView recyclerView, recylerview_transaction;
    private ArrayList<String> transactionStrings = new ArrayList<>();
    private Subscription mGetTransactions;
    TextView noRecords;
    boolean loadedApi = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = this.getActivity();
        rootView = inflater.inflate(R.layout.fragment_my_transactions, container, false);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recylerview_transactions);
        btnAll = rootView.findViewById(R.id.btn_all);
        btnPaid = rootView.findViewById(R.id.btn_paid);
        btnReceived = rootView.findViewById(R.id.btn_received);
        btnAdded = rootView.findViewById(R.id.btn_added);
        btnAll.setOnClickListener(this);
        noRecords = rootView.findViewById(R.id.no_records);
        btnPaid.setOnClickListener(this);
        btnReceived.setOnClickListener(this);
        btnAdded.setOnClickListener(this);
        hideKeyboard(context);
        /*recylerview_transaction = (RecyclerView) rootView.findViewById(R.id.recylerview_transaction);*/
/*
        transactionStrings.add(getString(R.string.all));
        transactionStrings.add(getString(R.string.paid));
        transactionStrings.add(getString(R.string.received));
        transactionStrings.add(getString(R.string.added));*/
/*
        TransactionTypeAdapter transactionTypeAdapter = new TransactionTypeAdapter(context, transactionStrings);
        recyclerView.setAdapter(transactionTypeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));*/
        // showDialog(getActivity(), "Please Wait Loading wallet.... ");
        if (checkUserLoginStatus(TAG)) {
          /*  showDialog(getActivity(), "Please Wait Loading ... ");*/
            /*btnAll.setBackgroundColor(getResources().getColor(R.color.Maroon));*/
            btnAll.setBackgroundResource(R.drawable.border_accent_whiteborder_button);
            btnAll.setTextColor(getResources().getColor(R.color.white_new));

            btnPaid.setBackgroundResource(R.drawable.border_accent);
            btnPaid.setTextColor(getResources().getColor(R.color.Maroon));

            btnReceived.setBackgroundResource(R.drawable.border_accent);
            btnReceived.setTextColor(getResources().getColor(R.color.Maroon));

            btnAdded.setBackgroundResource(R.drawable.border_accent);
            btnAdded.setTextColor(getResources().getColor(R.color.Maroon));
           // getTransactionsAll(SharedPrefsData.getInstance(context).getWalletId(context));
            if(isOnline(getActivity())){
                getTransactions(SharedPrefsData.getInstance(getActivity()).getWalletId(getActivity()),null);
            }else {
                showToast(getActivity(),getString(R.string.no_internet));
            }

        } else {
            hideDialog();
        }


        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && !loadedApi && getActivity()!=null) {
            hideKeyboard(context);
            btnAll.setBackgroundResource(R.drawable.border_accent_whiteborder_button);
            btnAll.setTextColor(getResources().getColor(R.color.white_new));

            btnPaid.setBackgroundResource(R.drawable.border_accent);
            btnPaid.setTextColor(getResources().getColor(R.color.Maroon));

            btnReceived.setBackgroundResource(R.drawable.border_accent);
            btnReceived.setTextColor(getResources().getColor(R.color.Maroon));

            btnAdded.setBackgroundResource(R.drawable.border_accent);
            btnAdded.setTextColor(getResources().getColor(R.color.Maroon));
            if(isOnline(getActivity())){
                getTransactions(SharedPrefsData.getInstance(getActivity()).getWalletId(getActivity()),null);
            }else {
                showToast(getActivity(),getString(R.string.no_internet));
            }
          //  loadedApi = true;
        }

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_all:
                if (checkUserLoginStatus(TAG)) {
                    btnAll.setBackgroundResource(R.drawable.border_accent_whiteborder_button);
                    btnAll.setTextColor(getResources().getColor(R.color.white_new));
                    btnPaid.setBackgroundResource(R.drawable.border_accent);
                    btnPaid.setTextColor(getResources().getColor(R.color.Maroon));
                    btnReceived.setBackgroundResource(R.drawable.border_accent);
                    btnReceived.setTextColor(getResources().getColor(R.color.Maroon));
                    btnAdded.setBackgroundResource(R.drawable.border_accent);
                    btnAdded.setTextColor(getResources().getColor(R.color.Maroon));
                    //getTransactionsAll(SharedPrefsData.getInstance(context).getWalletId(context));
                    if(isOnline(context)){
//                        getTransactions(SharedPrefsData.getInstance(context).getWalletId(context),"29,30,31,32,33,34,35,36,37");
                        getTransactions(SharedPrefsData.getInstance(context).getWalletId(context),null);
                    }else {
                        showToast(context,getString(R.string.no_internet));
                    }

                } else {
                    hideDialog();
                }
               /* showToast(getActivity(), "allllllllllllll");*/
                break;

            case R.id.btn_paid:

                btnAll.setBackgroundResource(R.drawable.border_accent);
                btnAll.setTextColor(getResources().getColor(R.color.Maroon));
                btnPaid.setBackgroundResource(R.drawable.border_accent_whiteborder_button);
                btnPaid.setTextColor(getResources().getColor(R.color.white));
                btnReceived.setBackgroundResource(R.drawable.border_accent);
                btnReceived.setTextColor(getResources().getColor(R.color.Maroon));
                btnAdded.setBackgroundResource(R.drawable.border_accent);
                btnAdded.setTextColor(getResources().getColor(R.color.Maroon));
            //    getTransactionsPaid(SharedPrefsData.getInstance(context).getWalletId(context));
                if(isOnline(context)){
                    getTransactions(SharedPrefsData.getInstance(context).getWalletId(context),CommonConstants.STATUSTYPE_ID_PAID);
                }else {
                    showToast(context,getString(R.string.no_internet));
                }

                /*showToast(getActivity(), "paiddddddd");*/
                break;
            case R.id.btn_received:
                btnAll.setBackgroundResource(R.drawable.border_accent);
                btnAll.setTextColor(getResources().getColor(R.color.Maroon));
                btnPaid.setBackgroundResource(R.drawable.border_accent);
                btnPaid.setTextColor(getResources().getColor(R.color.Maroon));
                btnReceived.setBackgroundResource(R.drawable.border_accent_whiteborder_button);
                btnReceived.setTextColor(getResources().getColor(R.color.white_new));
                btnAdded.setBackgroundResource(R.drawable.border_accent);
                btnAdded.setTextColor(getResources().getColor(R.color.Maroon));
                if(isOnline(context)){
                    getTransactions(SharedPrefsData.getInstance(context).getWalletId(context),CommonConstants.STATUSTYPE_ID_RECEIVED);
                }else {
                    showToast(context,getString(R.string.no_internet));
                }


               /* showToast(getActivity(), "receiveddddddddddd");*/
                break;
            case R.id.btn_added:
                btnAll.setBackgroundResource(R.drawable.border_accent);
                btnAll.setTextColor(getResources().getColor(R.color.Maroon));
                btnPaid.setBackgroundResource(R.drawable.border_accent);
                btnPaid.setTextColor(getResources().getColor(R.color.Maroon));
                btnReceived.setBackgroundResource(R.drawable.border_accent);
                btnReceived.setTextColor(getResources().getColor(R.color.Maroon));
                btnAdded.setBackgroundResource(R.drawable.border_accent_whiteborder_button);
                btnAdded.setTextColor(getResources().getColor(R.color.white_new));
              //  getTransactionsAdded(SharedPrefsData.getInstance(context).getWalletId(context));
                if(isOnline(context)){
                    getTransactions(SharedPrefsData.getInstance(context).getWalletId(context),CommonConstants.STATUSTYPE_ID_ADDED);
                }else {
                    showToast(context,getString(R.string.no_internet));
                }


                /*showToast(getActivity(), "addeddddddddddd");*/
                break;
        }

    }



    private void getTransactions(String walletId,String statusType) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
      /*  mGetTransactions = service.myTransactions(ApiConstants.PASSBOOK + walletId )*/
        mGetTransactions = service.myTransactions(ApiConstants.PASSBOOK + walletId + "/" + statusType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TransactionResponseModel>() {
                    @Override
                    public void onCompleted() {
                        hideDialog();
                        //  Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
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
//                        Toast.makeText(context, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }

                    @Override
                    public void onNext(TransactionResponseModel transactionResponseModel) {
                        hideDialog();
                        if (transactionResponseModel.getListResult().isEmpty()) {
                            noRecords.setVisibility(View.VISIBLE);

                        } else {
                            noRecords.setVisibility(View.GONE);
                        }
                        MyTransactionAdapter myTransactionAdapter = new MyTransactionAdapter(context, transactionResponseModel.getListResult());
                        recyclerView.setAdapter(myTransactionAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));

                    }
                });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGetTransactions != null) {
            mGetTransactions.unsubscribe();
        }
    }
}
