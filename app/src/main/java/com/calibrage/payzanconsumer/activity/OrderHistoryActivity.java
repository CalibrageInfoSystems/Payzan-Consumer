package com.calibrage.payzanconsumer.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.adapters.OlderHistroyAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends BaseActivity {
Toolbar toolbar;

    RecyclerView olderhistroyrecycler;
    List<OrderModel> listdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_history);
        ShowActionBar(getString(R.string.order_history));
        setView();

    }
    private void setView()
    {

        olderhistroyrecycler=(RecyclerView)findViewById(R.id.olderhistroyrecycler);
        OlderHistroyAdapter olderHistroyAdapter=new OlderHistroyAdapter(this,getdata());
        olderhistroyrecycler.setAdapter(olderHistroyAdapter);
        olderhistroyrecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitle(getString(R.string.order_history));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private   List<OrderModel> getdata()
    {
        List<OrderModel> data=new ArrayList<>();
        data.add(new OrderModel("214325745674","500","sucess"));
        data.add(new OrderModel("214325745674","500","sucess"));
        data.add(new OrderModel("214325745674","500","sucess"));
        data.add(new OrderModel("214325745674","500","sucess"));


        return  data;


    }

}
