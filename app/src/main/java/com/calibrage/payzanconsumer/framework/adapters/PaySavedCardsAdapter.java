package com.calibrage.payzanconsumer.framework.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.PaySavedCards;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.interfaces.PaySavedCardsListener;
import com.calibrage.payzanconsumer.framework.interfaces.PaymentMethodClickListiner;
import com.calibrage.payzanconsumer.framework.model.PaySavedCardModel;
import com.calibrage.payzanconsumer.framework.model.SaveCardsModel;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;

import java.util.List;

import okhttp3.internal.cache.DiskLruCache;

/**
 * Created by Calibrage11 on 12/22/2017.
 */

public class PaySavedCardsAdapter extends RecyclerView.Adapter<PaySavedCardsAdapter.MyViewHolder> {
    Context ctx;
    List<PaySavedCardModel> listdata;
    private  PaySavedCardsListener paySavedCardsListener;
    private TextView cardNameTxt;


  private String cvvStr;



    public PaySavedCardsAdapter(Context context, List<PaySavedCardModel> listdata) {
        this.ctx = context;
        this.listdata = listdata;
    }

    @Override
    public PaySavedCardsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pay_saved_cards, null);
        PaySavedCardsAdapter.MyViewHolder mvh = new PaySavedCardsAdapter.MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final PaySavedCardsAdapter.MyViewHolder holder, int position) {

        if(listdata.get(holder.getAdapterPosition()).getInActive()){
            holder.payLyt.setVisibility(View.VISIBLE);
            holder.checkRB.setChecked(true);
        }else {
            holder.payLyt.setVisibility(View.GONE);
            holder.checkRB.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paySavedCardsListener.OnPayClickListiner(holder.getAdapterPosition(),"checkRb");
                //holder.payLyt.setVisibility(View.VISIBLE);
            }
        });
        holder.checkRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paySavedCardsListener.OnPayClickListiner(holder.getAdapterPosition(),"checkRb");
                //holder.payLyt.setVisibility(View.VISIBLE);
            }
        });

        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paySavedCardsListener.OnPayClickListiner(holder.getAdapterPosition(),"deleteImg");
            }
        });
        holder.cvvEdt.setImeOptions(EditorInfo.IME_NULL);
        holder.cvvEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    holder.cvvTIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isValidation(holder);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CommonButton payBtn;
        private RadioButton checkRB;
        private ImageView deleteImg, cardImg;
        private LinearLayout payLyt;
        private NCBTextInputLayout cvvTIL;
        private CommonEditText cvvEdt;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardNameTxt =  itemView.findViewById(R.id.cardNameTxt);
            deleteImg = (ImageView) itemView.findViewById(R.id.deleteImg);
            cardImg = (ImageView) itemView.findViewById(R.id.cardImg);
            cvvTIL = (NCBTextInputLayout) itemView.findViewById(R.id.cvvTIL);
            cvvEdt = (CommonEditText) itemView.findViewById(R.id.cvvEdt);
            payBtn = (CommonButton) itemView.findViewById(R.id.payBtn);
            checkRB = (RadioButton) itemView.findViewById(R.id.checkRB);
            payLyt = (LinearLayout) itemView.findViewById(R.id.payLyt);




        }

    }
    public boolean isValidation(final PaySavedCardsAdapter.MyViewHolder holder)
    {
        cvvStr = holder.cvvEdt.getText().toString().trim();
        if (TextUtils.isEmpty(cvvStr)) {
            holder.cvvTIL.setErrorEnabled(true);
            holder.cvvTIL.setError(ctx.getString(R.string.enter_cvv));
            return false;
        }
        return true;
    }
    public void setOnAdapterListener(PaySavedCardsListener paySavedCardsListener) {
        this.paySavedCardsListener =  paySavedCardsListener;
    }
}
