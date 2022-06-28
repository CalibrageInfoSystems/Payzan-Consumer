package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.AddCardActivity;
import com.calibrage.payzanconsumer.activity.SaveCardActivity;
import com.calibrage.payzanconsumer.framework.adapters.PaySavedCardsAdapter;
import com.calibrage.payzanconsumer.framework.adapters.SaveCardsAdapter;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.interfaces.PaySavedCardsListener;
import com.calibrage.payzanconsumer.framework.model.PaySavedCardModel;
import com.calibrage.payzanconsumer.framework.model.SaveCardsModel;
import com.calibrage.payzanconsumer.framework.util.Animationt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calibrage11 on 12/21/2017.
 */

public class PaySavedCards extends BaseFragment implements PaySavedCardsListener{
    private View view;
    RecyclerView savecardrecycler;
    private Context context;
    private  PaySavedCardsAdapter saveCardsAdapter;
    private List<PaySavedCardModel> data;
    private  int  lastPos= -1;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = this.getActivity();
        view = inflater.inflate(R.layout.fragment_pay_saved_cards, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setView();

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        return view;
    }

    private void setView()
    {

         data=new ArrayList<>();
        data.add(new PaySavedCardModel("12345",false));
        data.add(new PaySavedCardModel("12345",false));
        data.add(new PaySavedCardModel("12345",false));
        data.add(new PaySavedCardModel("12345",false));
        savecardrecycler=(RecyclerView)view.findViewById(R.id.savecardrecycler);
       // savecardbtn=(CommonButton)findViewById(R.id.savecardbtn);
         saveCardsAdapter=new PaySavedCardsAdapter(context,data);
        saveCardsAdapter.setOnAdapterListener(PaySavedCards.this);
        savecardrecycler.setAdapter(saveCardsAdapter);
        savecardrecycler.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

//        if (listPosition != -1 && listPosition != selectPos && customerClaimsRootModel.getData().size() - 1 >= listPosition)
//            customerClaimsRootModel.getData().get(listPosition).setIsVisible(false);
        /**
         * Selected item open in list
         */
//        if (listPosition != -1 &&  customerClaimsRootModel.getData().size() - 1 >= listPosition){
//            viewOrderRecycler.scrollToPosition(listPosition);
//            customerClaimsRootModel.getData().get(listPosition).setIsVisible(true);
//            adapter.notifyDataSetChanged();
//        }




    }

//    private List<PaySavedCardModel> getdata()
//    {
//
//
//        return  data;
//
//    }

    @Override
    public void OnPayClickListiner(int pos,String parameter) {
        if (lastPos != -1 && lastPos != pos && data.size() - 1 >= lastPos && pos!=-1)
            data.get(lastPos).setInActive(false);
        lastPos = pos;

       // savecardrecycler.scrollToPosition(listPosition);
            if(parameter.equalsIgnoreCase("checkRb")){
//                getdata().get(pos).setInActive(true);
//                saveCardsAdapter.notifyDataSetChanged();
               data.get(pos).setInActive(data.get(pos).getInActive()? false : true);
                saveCardsAdapter.notifyDataSetChanged();
            }else if(parameter.equalsIgnoreCase("deleteImg")){

            }
    }
}
