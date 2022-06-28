package com.calibrage.payzanconsumer.fragement.agent;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.framework.adapters.AgentSplit;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.calibrage.payzanconsumer.framework.networkservices.ApiConstants.AGENT_CATAGORYSPLITBY_ID;


public class Agent_Commission_Fragment extends BaseFragment {
    public static final String TAG = Agent_Commission_Fragment.class.getSimpleName();
    String AgentID = "a41a0f68-9c3f-45e1-a070-d8bc6837abf7";
    RecyclerView agentcommission;
    Context context;
    ArrayList<String> listComm = new ArrayList<>(Arrays.asList("Airtel", "Airtel", "Airtel", "Airtel", "Airtel", "Airtel", "Airtel"));
    private Subscription commisionSubscription;

    public Agent_Commission_Fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agent_commission, container, false);

        HomeActivity.toolbar.setTitle(getResources().getString(R.string.AgentCommision));
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        agentcommission = (RecyclerView) view.findViewById(R.id.agentcommission);
        // getCommisionResponse();
        AgentCommissionAdapter agentCommissionAdapter = new AgentCommissionAdapter(getActivity(), listComm);
        agentcommission.setAdapter(agentCommissionAdapter);
        agentcommission.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        return view;
    }

    private void getCommisionResponse() {
        /*ApiConstants.AGENT_CATAGORYSPLITBY_ID + AgentID*/

        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);

        commisionSubscription = service.GetAgentCategorySplitByAgentId(AGENT_CATAGORYSPLITBY_ID)

                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AgentSplit>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(context, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();

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
                    }

                    @Override
                    public void onNext(AgentSplit agentSplit) {


                    }
                });
    }
}






