package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;


public class BroadbandFragment extends BaseFragment {
    public static final String TAG = BroadbandFragment.class.getSimpleName();
    private View rootView;
    private Context context;
    private CommonEditText ServiceNEdt, amountEdt;
    private NCBTextInputLayout operatorTXT, serviceNoTXT, amountTXT;
    private Spinner operatorSpn;
    private Button submit;
    private String operatorStr, servicesnoStr, amountStr;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_broadband, container, false);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        context = this.getActivity();
        //  android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        setViews();
        initViews();


        return rootView;
    }

    private void setViews() {

      //  HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.broadband_sname));
        /*HomeActivity.toolbar.setTitle(getResources().getString(R.string.broadband_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);*/
       /* setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(HomeActivity.toolbar);
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);*/
       /* HomeActivity.toolbar.setTitle(getResources().getString(R.string.broadband_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));*/
        /*HomeActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeTab();
            }
        });
*/
        serviceNoTXT = (NCBTextInputLayout) rootView.findViewById(R.id.serviceNoTXT);
        operatorTXT = (NCBTextInputLayout) rootView.findViewById(R.id.operatorTXT);
        amountTXT = (NCBTextInputLayout) rootView.findViewById(R.id.amountTXT);
        operatorSpn = (Spinner) rootView.findViewById(R.id.operatorSpn);
        ServiceNEdt = (CommonEditText) rootView.findViewById(R.id.ServiceNEdt);
        amountEdt = (CommonEditText) rootView.findViewById(R.id.amountEdt);
        submit = (Button) rootView.findViewById(R.id.submit);

        /*rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    closeTab();
                    // onCloseFragment();
                    return true;
                } else {
                    return false;
                }
            }
        });
*/

    }


    private void initViews() {
//        operatorSpn.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                operatorSpn.showDropDown();
//                return false;
//            }
//        });
        ServiceNEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    serviceNoTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        amountEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    amountTXT.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserLoginStatus(TAG)) {
                    isValidateUi();
                }
            }
        });


    }

    private boolean isValidateUi() {
        if (operatorSpn.getSelectedItem() != null) {
            operatorStr = (String) operatorSpn.getSelectedItem();
        }
        servicesnoStr = ServiceNEdt.getText().toString().trim();
        amountStr = amountEdt.getText().toString().trim();
        if (TextUtils.isEmpty(operatorStr)) {
            operatorTXT.setErrorEnabled(true);
            operatorTXT.setError(getString(R.string.err_please_select_operator));
            return false;
        } else if (TextUtils.isEmpty(servicesnoStr)) {
            serviceNoTXT.setErrorEnabled(true);
            serviceNoTXT.setError(getString(R.string.err_please_enter_service_no));
            return false;
        } else if (TextUtils.isEmpty(amountStr)) {
            amountTXT.setErrorEnabled(true);
            amountTXT.setError(getString(R.string.err_please_enter_amount));
            return false;
        }


        return true;
    }



    /*private void closeTab() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("broadbandTag");
        if (fragment != null) {
            CommonUtil.hideSoftKeyboard((AppCompatActivity) getActivity());
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            HomeActivity.toolbar.setNavigationIcon(null);
            HomeActivity.toolbar.setTitle("");
        }
    }*/
}
