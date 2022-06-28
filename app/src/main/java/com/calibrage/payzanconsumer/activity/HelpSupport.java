package com.calibrage.payzanconsumer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.HomeFragment;
import com.calibrage.payzanconsumer.framework.adapters.SupportAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.interfaces.HelpSupportActivity;
import com.calibrage.payzanconsumer.framework.model.SupportModel;

import java.util.ArrayList;
import java.util.List;


public class HelpSupport extends BaseActivity implements HelpSupportActivity{

    RecyclerView supportrecycleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support);
        ShowActionBar(getString(R.string.help_support));
        setView();
    }
    public void setView()
    {

        supportrecycleview=(RecyclerView)findViewById(R.id.supportrecycleview);

        SupportAdapter supportAdapter=new SupportAdapter(this,getdata());
        supportrecycleview.setAdapter(supportAdapter);
        supportAdapter.onSupportClickListener( HelpSupport.this);
        supportrecycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }
    private List<SupportModel>getdata()
    {
        List<SupportModel>data=new ArrayList();
        data.add(new SupportModel(getString(R.string.financial_services),getString(R.string.insurance_loan)));
        data.add(new SupportModel(getString(R.string.managing_my_payzan_upi),getString(R.string.upi_registration)));
        data.add(new SupportModel(getString(R.string.payzan_bhim_upi),getString(R.string.transaction_done)));
        data.add(new SupportModel(getString(R.string.managing_my_payzan_account),getString(R.string.link_your_aadhaar)));
        data.add(new SupportModel(getString(R.string.payzan_kyc),getString(R.string.insurance_loan)));
        data.add(new SupportModel(getString(R.string.managing_my_bank_account),getString(R.string.saving_account)));
        data.add(new SupportModel(getString(R.string.privacy_security),getString(R.string.report_suspicious)));
        return data;
    }


    @Override
    public void onItemSupportClickListener(int position) {


        switch (position)
        {
            case 0: {

                Intent intent = new Intent(HelpSupport.this, SupportActivitys.class);
                Bundle bundle = new Bundle();
                bundle.putString("fsStr", getdata().get(position).getHeading());
                intent.putExtras(bundle);
                //fstxt.setText(fsStr);
                startActivity(intent);
                break;
            }
            case 1:
            {

                Intent intent = new Intent(HelpSupport.this, SupportActivitys.class);
                Bundle bundle = new Bundle();
                bundle.putString("fsStr", getdata().get(position).getHeading());
                intent.putExtras(bundle);
                //fstxt.setText(fsStr);
                startActivity(intent);
                break;
            }
            case 2:
            {

                Intent intent = new Intent(HelpSupport.this, SupportActivitys.class);
                Bundle bundle = new Bundle();
                bundle.putString("fsStr", getdata().get(position).getHeading());
                intent.putExtras(bundle);
                //fstxt.setText(fsStr);
                startActivity(intent);
                break;
            }

            case 3:
            {

                Intent intent = new Intent(HelpSupport.this, SupportActivitys.class);
                Bundle bundle = new Bundle();
                bundle.putString("fsStr", getdata().get(position).getHeading());
                intent.putExtras(bundle);
                //fstxt.setText(fsStr);
                startActivity(intent);
                break;
            }
            case 4:
            {

                Intent intent = new Intent(HelpSupport.this, SupportActivitys.class);
                Bundle bundle = new Bundle();
                bundle.putString("fsStr", getdata().get(position).getHeading());
                intent.putExtras(bundle);
                //fstxt.setText(fsStr);
                startActivity(intent);
                break;
            }
            case 5:
            {

                Intent intent = new Intent(HelpSupport.this, SupportActivitys.class);
                Bundle bundle = new Bundle();
                bundle.putString("fsStr", getdata().get(position).getHeading());
                intent.putExtras(bundle);
                //fstxt.setText(fsStr);
                startActivity(intent);
                break;
            }
            case 6:
            {

                Intent intent = new Intent(HelpSupport.this, SupportActivitys.class);
                Bundle bundle = new Bundle();
                bundle.putString("fsStr", getdata().get(position).getHeading());
                intent.putExtras(bundle);
                //fstxt.setText(fsStr);
                startActivity(intent);
                break;
            }
            case 7:
            {

                Intent intent = new Intent(HelpSupport.this, SupportActivitys.class);
                Bundle bundle = new Bundle();
                bundle.putString("fsStr", getdata().get(position).getHeading());
                intent.putExtras(bundle);
                //fstxt.setText(fsStr);
                startActivity(intent);
                break;
            }
        }
    }
}
