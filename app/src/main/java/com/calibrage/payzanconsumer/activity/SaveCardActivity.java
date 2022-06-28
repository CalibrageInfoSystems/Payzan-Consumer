package com.calibrage.payzanconsumer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.adapters.SaveCardsAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.model.SaveCardsModel;
import com.calibrage.payzanconsumer.framework.util.Animationt;

import java.util.ArrayList;
import java.util.List;

public class SaveCardActivity extends BaseActivity {

   Toolbar toolbar;
    CommonButton saveCardBtn;
    RecyclerView saveCardRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_save_cards);
        ShowActionBar(getString(R.string.save_card));
        setView();
    }
    private void setView()
    {
        saveCardRecycler =(RecyclerView)findViewById(R.id.savecardrecycler);
        saveCardBtn =(CommonButton)findViewById(R.id.savecardbtn);
        SaveCardsAdapter saveCardsAdapter=new SaveCardsAdapter(this,getdata());
        saveCardRecycler.setAdapter(saveCardsAdapter);
        saveCardRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitle(getString(R.string.save_card));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        saveCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SaveCardActivity.this,AddCardActivity.class);
                startActivityWithAnimation(intent, Animationt.SLIDE_IN_RIGHT);
            }
        });

    }


    private List<SaveCardsModel> getdata()
    {
        List<SaveCardsModel> data=new ArrayList<>();
        data.add(new SaveCardsModel("12345"));
        data.add(new SaveCardsModel("12345"));
        data.add(new SaveCardsModel("12345"));
        data.add(new SaveCardsModel("12345"));

        return  data;

    }
}
