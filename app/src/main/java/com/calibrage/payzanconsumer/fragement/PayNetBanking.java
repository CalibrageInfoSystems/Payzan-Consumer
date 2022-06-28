package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.adapters.PayNetBankingAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.interfaces.PayNetBankListiner;
import com.calibrage.payzanconsumer.framework.model.NetBanksModel;
import com.calibrage.payzanconsumer.framework.model.PayNetBankModel;
import com.calibrage.payzanconsumer.framework.model.PaySavedCardModel;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Calibrage11 on 12/22/2017.
 */

public class PayNetBanking extends BaseFragment implements PayNetBankListiner {
    private Context context;
    private View view;
    private RecyclerView recylerviewPopular;
    private PayNetBankingAdapter payNetBankingAdapter;
    private ArrayList<PayNetBankModel> payNetBankModels = new ArrayList<>();
    private Spinner bankSpn;
    private Subscription operatorSubscription;
    private ArrayList<NetBanksModel.ListResult> listResults = new ArrayList<>();
    ArrayList<String> bankArrayList = new ArrayList<String>();
    private int lastPos = -1;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = this.getActivity();
        view = inflater.inflate(R.layout.fragment_net_bank, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setView();
        getRequestBank("1");
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    private void setView() {
        payNetBankModels.add(new PayNetBankModel(R.drawable.icic, false));
        payNetBankModels.add(new PayNetBankModel(R.drawable.banner_image, false));
        payNetBankModels.add(new PayNetBankModel(R.drawable.cellon, false));
        payNetBankModels.add(new PayNetBankModel(R.drawable.icic, false));
        recylerviewPopular = view.findViewById(R.id.recylerviewPopular);
        bankSpn = view.findViewById(R.id.bankSpn);
        // bankSpn.setAdapter();

        payNetBankingAdapter = new PayNetBankingAdapter(context, payNetBankModels);
        payNetBankingAdapter.setOnAdapterListener(PayNetBanking.this);
        recylerviewPopular.setAdapter(payNetBankingAdapter);
        recylerviewPopular.setLayoutManager(new GridLayoutManager(context, 3));

        bankSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int i = 0; i < payNetBankModels.size(); i++) {
                        payNetBankModels.get(i).setActive(false);
                    }
                    payNetBankingAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });
    }

    private void getRequestBank(String providerType) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getBankRequest(ApiConstants.BUSINESS_CAT_REQUESTS + providerType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NetBanksModel>() {
                    @Override
                    public void onCompleted() {
                        //   Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
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
                        //Toast.makeText(context, getString(R.string.fail_response), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(NetBanksModel bankListResults) {
                        hideDialog();
                        Log.d("response", bankListResults.getIsSuccess().toString());
                        listResults = (ArrayList<NetBanksModel.ListResult>) bankListResults.getListResult();
                        bankArrayList.add(0, "--Select Bank--");
                        for (int i = 0; i < listResults.size(); i++) {
                            bankArrayList.add(listResults.get(i).getDescription());

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_text_black, bankArrayList);
                        bankSpn.setAdapter(adapter);


                    }

                });


    }

    @Override
    public void OnNetBankClickListiner(int pos, String parameter) {
        bankSpn.setSelection(0);
        if (lastPos != -1 && lastPos != pos && payNetBankModels.size() - 1 >= lastPos)
            payNetBankModels.get(lastPos).setActive(false);
        lastPos = pos;
        if (parameter.equalsIgnoreCase("checkRB")) {
            payNetBankModels.get(pos).setActive(payNetBankModels.get(pos).getActive() ? false : true);
            payNetBankingAdapter.notifyDataSetChanged();
        }
    }
}
