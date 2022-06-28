package com.calibrage.payzanconsumer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.PayCardDetails;
import com.calibrage.payzanconsumer.fragement.PayNetBanking;
import com.calibrage.payzanconsumer.fragement.PaySavedCards;
import com.calibrage.payzanconsumer.framework.adapters.PaymentMethodsAdapter;
import com.calibrage.payzanconsumer.framework.interfaces.PaymentMethodClickListiner;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;

import java.util.ArrayList;


public class CompletePayment extends AppCompatActivity implements PaymentMethodClickListiner {
    private FrameLayout content_frame;
    private FragmentManager fragmentManager;
    private ArrayList<Pair<Integer, String>> menuPairList = new ArrayList<>();
    private RecyclerView lst_payment;
    private LinearLayoutManager mLayoutManager;
    PaymentMethodClickListiner methodClickListiner;
    private Intent intent;
    private String moneyStr;
    private int paymentVia;
    private TextView amountTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_payment);
        fragmentManager = getSupportFragmentManager();
        intent = getIntent();
        if(intent!=null){
            paymentVia = intent.getIntExtra(CommonConstants.CHECK_SCREEN_KEY,0);
            if(paymentVia==CommonConstants.CHECK_SCREEN_ADD_WALLET_VALUE){
                moneyStr= intent.getStringExtra(CommonConstants.AMOUMNT_ADD_WALLET_KEY);
            }
        }

        mLayoutManager = new LinearLayoutManager(this);
        menuPairList.add(Pair.create(R.drawable.ic_saved_cards, getString(R.string.save_card)));
        menuPairList.add(Pair.create(R.drawable.ic_credit_card, getString(R.string.credit_card)));
        menuPairList.add(Pair.create(R.drawable.ic_debit_card, getString(R.string.debit_card)));
        menuPairList.add(Pair.create(R.drawable.ic_net_banking, getString(R.string.net_banking)));

        PaymentMethodsAdapter paymentMethodsAdapter = new PaymentMethodsAdapter(menuPairList, getBaseContext());
        paymentMethodsAdapter.setOnAdapterListener(this);
        lst_payment = findViewById(R.id.Lst_PaymentMethods);
        amountTxt = findViewById(R.id.amountTxt);
        amountTxt.setText(moneyStr);
        lst_payment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        lst_payment.setAdapter(paymentMethodsAdapter);

        ReplcaFragment(new PaySavedCards());
    }


    @Override
    public void onPaymentMethodClickListiner(int pos) {

        Bundle bundle = new Bundle();
        bundle.putInt(CommonConstants.CHECK_SCREEN_KEY, paymentVia);
        bundle.putString(CommonConstants.AMOUNT_KEY,moneyStr);
       // fragment.setArguments(bundle);
        if (pos == 0) {
            PaySavedCards paySavedCards = new PaySavedCards();
            paySavedCards.setArguments(bundle);
            ReplcaFragment(paySavedCards);
        } else if (pos == 1 || pos == 2) {
            PayCardDetails payCardDetails =    new PayCardDetails();
            payCardDetails.setArguments(bundle);
            ReplcaFragment(payCardDetails);
        } else if (pos == 3) {
            PayNetBanking payNetBanking =   new PayNetBanking();
            payNetBanking.setArguments(bundle);
            ReplcaFragment(payNetBanking);

        }
    }

    public void ReplcaFragment(android.support.v4.app.Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }


}
