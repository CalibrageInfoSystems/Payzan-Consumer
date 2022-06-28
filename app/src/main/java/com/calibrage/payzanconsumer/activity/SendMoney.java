package com.calibrage.payzanconsumer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.adapters.MyOrderAdapter;

public class SendMoney extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private LinearLayoutManager layoutManager;
    private ImageView walletBtn,BankBtn,bankdrop;
    private LinearLayout banklayout,walletlayout;
    private EditText nameEdt,AccountNumberEdt,ifscEdt,amountEdt,amountWalletEdt,mobileEdt;
    private ImageView mobileNumber;
    private Button sendBankBtn,sendWalletBtn;
    static final int PICK_CONTACT = 1;
    private Toolbar toolbar;
    private AutoCompleteTextView BankSpn;
    String[] language ={"ICIC","HDFC","SBI","SBH","AXIS"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
        setupToolbar();
        walletBtn = (ImageView)findViewById(R.id.walletBtn);
        BankBtn = (ImageView)findViewById(R.id.BankBtn);
        bankdrop = (ImageView)findViewById(R.id.bankdrop);

        mobileNumber = (ImageView)findViewById(R.id.mobileNumber);
        banklayout = (LinearLayout) findViewById(R.id.banklayout);
        walletlayout = (LinearLayout) findViewById(R.id.walletlayout);
        nameEdt = (EditText) findViewById(R.id.nameEdt);
        AccountNumberEdt = (EditText) findViewById(R.id.AccountNumberEdt);
        ifscEdt = (EditText) findViewById(R.id.ifscEdt);
        amountEdt = (EditText) findViewById(R.id.amountEdt);
        amountWalletEdt = (EditText) findViewById(R.id.amountWalletEdt);
        mobileEdt = (EditText) findViewById(R.id.mobileEdt);

        sendBankBtn = (Button) findViewById(R.id.sendBankBtn);
        sendWalletBtn = (Button) findViewById(R.id.sendWalletBtn);

        BankSpn = (AutoCompleteTextView) findViewById(R.id.BankSpn);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_dropdown_item_1line,language);
        //Getting the instance of AutoCompleteTextView
        BankSpn.setThreshold(1);//will start working from first character
        BankSpn.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        BankSpn.setTextColor(Color.BLACK);

        bankdrop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                BankSpn.showDropDown();

                return false;
            }
        });

        walletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walletlayout.setVisibility(View.VISIBLE);
                banklayout.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    walletBtn.setBackground(ContextCompat.getDrawable(SendMoney.this,R.drawable.border_fillcompletely));
                }
                walletBtn.setImageResource(R.drawable.wallet_icon_white);
                BankBtn.setImageResource(R.drawable.bank_icon);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    BankBtn.setBackground(ContextCompat.getDrawable(SendMoney.this,R.drawable.border_accentcolor_button));
                }

            }
        });
        BankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walletlayout.setVisibility(View.GONE);
                banklayout.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    walletBtn.setBackground(ContextCompat.getDrawable(SendMoney.this,R.drawable.border_accentcolor_button));
                }
                walletBtn.setImageResource(R.drawable.wallet_icon);
                BankBtn.setImageResource(R.drawable.bank_icon_white);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    BankBtn.setBackground(ContextCompat.getDrawable(SendMoney.this,R.drawable.border_fillcompletely));
                }
            }
        });
        mobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        recyclerView =(RecyclerView)findViewById(R.id.recylerview);
        layoutManager =new LinearLayoutManager(context);
        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(this);
        recyclerView.setAdapter(myOrderAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null,null, null, null);
                    if (c.moveToFirst()) {
                     //   String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

//                        if (hasPhone.equalsIgnoreCase("1")) {
//                            Cursor phones = getContentResolver().query(
//                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
//                                    null, null);
//
//                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
//                            System.out.println("number is:" + cNumber);
//
//                        }


                        mobileEdt.setText(hasPhone.trim());
                      //  String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    }





                }
                break;
        }
    }

    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Send Money to Wallet or Bank");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

}
