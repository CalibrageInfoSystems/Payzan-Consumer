package com.calibrage.payzanconsumer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.controls.CreditCardEditText;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class AddCardActivity extends BaseActivity implements View.OnClickListener {
    private CommonButton submit;
    private NCBTextInputLayout cardNumberTxt, cardHolderNameTxt, cardLabelTxt, monthTxt, yearTxt;
    private CommonEditText cardLabelEdt, cardHolderNameEdt;
    private CreditCardEditText cardNumberEdt;
    private Spinner monthSp, yearSp;
    private CheckBox saveCheck;
    private String cardLabelStr, cardHolderNameStr, cardNumberStr, monthStr, yearStr;
    private Integer currentYear, currentMonth;
    Calendar mCalendar = Calendar.getInstance();
    private ArrayList<String> yearsList = new ArrayList<>();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_card);
        ShowActionBar(getString(R.string.add_new_card));
        initViews();
        setViews();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitle(getString(R.string.addcard));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cardNumberEdt = (CreditCardEditText) findViewById(R.id.cardNumberEdt);
        cardLabelEdt = (CommonEditText) findViewById(R.id.cardLabelEdt);
        cardHolderNameEdt = (CommonEditText) findViewById(R.id.cardHolderNameEdt);
        cardNumberTxt = (NCBTextInputLayout) findViewById(R.id.cardNumberTXT);
        cardHolderNameTxt = (NCBTextInputLayout) findViewById(R.id.cardHolderNameTXT);
        cardLabelTxt = (NCBTextInputLayout) findViewById(R.id.cardLabelTXT);
        submit = (CommonButton) findViewById(R.id.submit);
        monthSp = (Spinner) findViewById(R.id.monthSp);
        yearSp = (Spinner) findViewById(R.id.yearSp);
        saveCheck = (CheckBox) findViewById(R.id.saveCheck);
        cardNumberEdt.addTextChangedListener(new FourDigitCardFormatWatcher());
        currentYear = mCalendar.get(Calendar.YEAR);
        currentMonth = mCalendar.get(Calendar.MONTH) + 1;
        yearsList.add("YY");
        for (int i = 0; i <= 10; i++)
            yearsList.add("" + (currentYear + i));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text_black, yearsList);
        yearSp.setAdapter(adapter);

        String[] monthList = getResources().getStringArray(R.array.month);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text_black, monthList);
        monthSp.setAdapter(monthAdapter);

    }

    private void setViews() {
        submit.setOnClickListener(this);
//        cardNumberEdt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() > 0) {
//                    cardNumberTxt.setErrorEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//
//        });
//        cardNumberEdt.addTextChangedListener(new TextWatcher() {
//            private static final char space = ' ';
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // Remove spacing char
//                if (s.length() > 0 && (s.length() % 5) == 0) {
//                    final char c = s.charAt(s.length() - 1);
//                    if (space == c) {
//                        s.delete(s.length() - 1, s.length());
//                    }
//                }
//                // Insert char where needed.
//                if (s.length() > 0 && (s.length() % 5) == 0) {
//                    char c = s.charAt(s.length() - 1);
//                    // Only if its a digit where there should be a space we insert a space
//                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
//                        s.insert(s.length() - 1, String.valueOf(space));
//                    }
//                }
//            }
//        });
        cardHolderNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    cardHolderNameTxt.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        monthSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        yearSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (validateUi())
                    startActivity(new Intent(AddCardActivity.this, SaveCardActivity.class));
                break;
        }

    }

    private boolean validateUi() {

        if (TextUtils.isEmpty(cardNumberEdt.getText().toString().trim())) {
            cardNumberTxt.requestFocus();
            cardNumberTxt.setError(getString(R.string.err_enter_card_number));
            return false;
        }
        if (!(isValidCardNumber())) {
            cardNumberTxt.requestFocus();
            cardNumberTxt.setError(getString(R.string.err_enter_valid_card_number));
            cardNumberTxt.setErrorEnabled(true);
            return false;
        }

        if (CommonUtil.isEmptySpinner(monthSp)) {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(monthSp.getWindowToken(), 0);
            showToast(getResources().getString(R.string.err_enter_month));
            monthSp.requestFocus();
            return false;
        }
        if (CommonUtil.isEmptySpinner(yearSp)) {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(yearSp.getWindowToken(), 0);
            showToast(getResources().getString(R.string.err_enter_year));
            yearSp.requestFocus();
            return false;
        }
        if (currentYear == Integer.parseInt(yearSp.getSelectedItem().toString())) {
            if (currentMonth > Integer.parseInt(monthSp.getSelectedItem().toString())) {
                showToast(getApplicationContext(), getResources().getString(R.string.err_enter_valid_month));
                return false;
            }
        }
        if (TextUtils.isEmpty(cardHolderNameEdt.getText().toString().trim())) {
            cardHolderNameTxt.setError(getString(R.string.err_enter_cardholder_name));
            cardHolderNameTxt.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isValidCardNumber() {
        String target = cardNumberEdt.getText().toString().trim();
        if (target.length() != 19) {
            return false;
        }
        return true;
    }


    private class FourDigitCardFormatWatcher implements TextWatcher {

        // Change this to what you want... ' ', '-' etc..
        private static final char space = ' ';
        private boolean isDelete;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            if (count == 0)
                isDelete = false;
            else
                isDelete = true;

            if (s.length() > 0) {
                cardNumberTxt.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove spacing char
            /*if (s.length() > 0 && (s.length() % 5) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (space == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }*/

            String source = s.toString();
            int length = source.length();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(source);

            if (length > 0 && length % 5 == 0) {
                if (isDelete)
                    stringBuilder.deleteCharAt(length - 1);
                else
                    stringBuilder.insert(length - 1, " ");

                cardNumberEdt.setText(stringBuilder);
                cardNumberEdt.setSelection(cardNumberEdt.getText().length());
            }
        }

    }


}
