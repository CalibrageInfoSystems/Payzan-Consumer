package com.calibrage.payzanconsumer.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.calibrage.payzanconsumer.BuildConfig;
import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.model.AddressAddResponceModel;
import com.calibrage.payzanconsumer.framework.model.AddressAddSendModel;
import com.calibrage.payzanconsumer.framework.model.DistrictModel;
import com.calibrage.payzanconsumer.framework.model.EditAddressModel;
import com.calibrage.payzanconsumer.framework.model.MandalModel;
import com.calibrage.payzanconsumer.framework.model.StatesModel;
import com.calibrage.payzanconsumer.framework.model.VillageModel;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddNewAddressActivity extends BaseActivity {
    private MandalModel mandalModelList;
    private StatesModel provinceModelList;
    private VillageModel villageModelList;

    private DistrictModel districtModelList;
    private ArrayList<String> provinceList, districtList, mandalList, villageList;
    private Subscription mGetStatesSubscription, mGetDistrictSubscription, getmGetDistrictSubscription, mRegisterSubscription, editUserAddressSubscription;
    ArrayAdapter districtArrayAdapter, villageArrayAdapter, provinanceArrayAdapter, mandalArrayAdapter;
    private Spinner villageSpin, mandalSpin, districtSpin, provinceSpin;
    private NCBTextInputLayout nameTxt, addressTxt1, addressTxt2, landmarkTxt, mobileTxt, pincodeTxt, stateTIl, districtTIl, mandalTIl, villageTIl;
    private CommonButton btn_submit;
    private CommonEditText name_Edt, address1_Edt, address2_Edt, landmark_Edt, mobile_Edt, pinCode_Edt;
    static Pattern pattern;
    static Matcher matcher;
    final String NAME_PATTERN = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
    final String ADDRESS_PATTERN = "^[/.0-9a-zA-Z\\s,-]+$";
    private String nameStr, address1Str, address2Str, landmarkStr, mobileStr, pincodeStr, stateStr, villageStr, comingFrom;
    private int villageId, provinceStr, mandalStr, districtStr, postCodeStr, idStr;
    private Context context;
    private boolean villageVar = true, mandalVar = true, districtVar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewaddress);


        provinceList = new ArrayList<>();
        districtList = new ArrayList<>();
        mandalList = new ArrayList<>();
        villageList = new ArrayList<>();
        initViews();
        setViews();

        //address1_Edt.setFilters(new InputFilter[]{EMOJI_FILTER});
        //address2_Edt.setFilters(new InputFilter[]{EMOJI_FILTER});
        //landmark_Edt.setFilters(new InputFilter[]{EMOJI_FILTER});

    }

    private void initViews() {
        context = AddNewAddressActivity.this;
        Bundle extras = getIntent().getExtras();
        btn_submit = findViewById(R.id.submit);
        if (extras != null) {
            comingFrom = extras.getString("comingFrom");
            idStr = extras.getInt("Id");
            if (comingFrom != null && !comingFrom.equalsIgnoreCase("") && comingFrom.equalsIgnoreCase("Edit")) {
                ShowActionBar(getString(R.string.edit_address));
                btn_submit.setText(getString(R.string.update_address));
                showProgressDialog();
                if (isOnline()) {
                    editAddress("" + idStr);
                } else {
                    showToast(AddNewAddressActivity.this, getString(R.string.no_internet));
                }

            } else {
                if (isOnline()) {
                    getProvince();
                } else {
                    showToast(AddNewAddressActivity.this, getString(R.string.no_internet));
                }
                ShowActionBar(getString(R.string.add_new_address));
                btn_submit.setText(getString(R.string.add_address));
            }
        }


        name_Edt = findViewById(R.id.txt_name);
        villageSpin = (Spinner) findViewById(R.id.villageSpin);
        mandalSpin = (Spinner) findViewById(R.id.mandalSpin);
        districtSpin = (Spinner) findViewById(R.id.districtSpin);
        provinceSpin = (Spinner) findViewById(R.id.provinceSpin);
        address1_Edt = findViewById(R.id.txt_address1);
        address2_Edt = findViewById(R.id.txt_address2);
        landmark_Edt = findViewById(R.id.txt_landmark);
        mobile_Edt = findViewById(R.id.txt_mobile);
        pinCode_Edt = findViewById(R.id.txt_pincode);
        nameTxt = (NCBTextInputLayout) findViewById(R.id.nameTxt);
        addressTxt1 = (NCBTextInputLayout) findViewById(R.id.addressTxt1);
        addressTxt2 = (NCBTextInputLayout) findViewById(R.id.addressTxt2);
        landmarkTxt = (NCBTextInputLayout) findViewById(R.id.landmarkTxt);
        mobileTxt = (NCBTextInputLayout) findViewById(R.id.mobileTxt);
        pincodeTxt = (NCBTextInputLayout) findViewById(R.id.pincodeTxt);

    }

    private void setViews() {
        name_Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    nameTxt.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        address1_Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    addressTxt1.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        address2_Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    addressTxt2.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        landmark_Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    landmarkTxt.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mobile_Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mobileTxt.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String x = "";
                x = editable.toString();
                if (x.startsWith("0")) {
                    mobile_Edt.setText("");
                }

            }
        });

        pinCode_Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    pincodeTxt.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        provinceSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                provinceSpin.setSelection(i);
                if (!provinceModelList.getListResult().isEmpty() && provinceSpin.getSelectedItemPosition() != 0) {
                    districtList = new ArrayList<>();
                    villageList = new ArrayList<>();
                    mandalList = new ArrayList<>();
                    pinCode_Edt.setText("");
                    getDistricts(provinceModelList.getListResult().get(i - 1).getId());
                } else {
                    districtList.clear();
                    villageList.clear();
                    mandalList.clear();


                    districtSpin.setAdapter(null);
                    mandalSpin.setAdapter(null);
                    villageSpin.setAdapter(null);
                    pinCode_Edt.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                districtSpin.setSelection(i);
                if (!districtModelList.getListResult().isEmpty() && districtSpin.getSelectedItemPosition() != 0) {
                    villageList = new ArrayList<>();
                    mandalList = new ArrayList<>();
                    pinCode_Edt.setText("");
                    getMandals(districtModelList.getListResult().get(i - 1).getId());
                } else {
                    villageList.clear();
                    mandalList.clear();
                    mandalSpin.setAdapter(null);
                    villageSpin.setAdapter(null);
                    pinCode_Edt.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AddNewAddressActivity.this, R.string.toast_iscalling, Toast.LENGTH_SHORT).show();
            }
        });
        mandalSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mandalSpin.setSelection(i);
                if (!mandalModelList.getListResult().isEmpty() && mandalSpin.getSelectedItemPosition() != 0) {
                    villageList = new ArrayList<>();
                    pinCode_Edt.setText("");
                    getVillages(mandalModelList.getListResult().get(i - 1).getId());
                } else {
                    villageList.clear();
                    villageSpin.setAdapter(null);
                    pinCode_Edt.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        villageSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (villageSpin.getSelectedItemPosition() != 0) {
                    pinCode_Edt.setText("");
                    villageId = villageModelList.getListResult().get(villageSpin.getSelectedItemPosition() - 1).getId();
                    postCodeStr = villageModelList.getListResult().get(villageSpin.getSelectedItemPosition() - 1).getPostCode();
                    pinCode_Edt.setText("" + postCodeStr);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        provinceSpin.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null && event.getAction() == MotionEvent.ACTION_MOVE) {
                    InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
                    boolean isKeyboardUp = imm.isAcceptingText();

                    if (isKeyboardUp) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
        districtSpin.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null && event.getAction() == MotionEvent.ACTION_MOVE) {
                    InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
                    boolean isKeyboardUp = imm.isAcceptingText();

                    if (isKeyboardUp) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
        mandalSpin.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null && event.getAction() == MotionEvent.ACTION_MOVE) {
                    InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
                    boolean isKeyboardUp = imm.isAcceptingText();

                    if (isKeyboardUp) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
        villageSpin.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null && event.getAction() == MotionEvent.ACTION_MOVE) {
                    InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
                    boolean isKeyboardUp = imm.isAcceptingText();

                    if (isKeyboardUp) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateUI()) {
                    if (btn_submit.getText().toString().equalsIgnoreCase(getString(R.string.update_address))) {
                        showProgressDialog();
                        updateAddress();
                    } else if (btn_submit.getText().toString().equalsIgnoreCase(getString(R.string.add_address))) {
                        showProgressDialog();
                        addNewAddress();
                    }


                }
            }
        });
    }

    private boolean validateUI() {
        if (TextUtils.isEmpty(name_Edt.getText().toString().trim())) {
            nameTxt.setErrorEnabled(true);
            nameTxt.setError(getString(R.string.err_enter_name));
            name_Edt.requestFocus();
            return false;
        } else if ((!TextUtils.isEmpty(name_Edt.getText().toString().trim()) && (name_Edt.getText().length() < 3))) {
            nameTxt.setErrorEnabled(true);
            nameTxt.setError(getString(R.string.err_please_enter_valid_name));
            name_Edt.requestFocus();
            return false;
        } else if (!nameValidate()) {
            nameTxt.setErrorEnabled(true);
            nameTxt.setError(getString(R.string.err_enter_valid_name));
            name_Edt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(address1_Edt.getText().toString().trim())) {
            addressTxt1.setError(getString(R.string.err_enter_address1));
            addressTxt1.setErrorEnabled(true);
            address1_Edt.requestFocus();
            return false;
        } else if ((!TextUtils.isEmpty(address1_Edt.getText().toString().trim()) && (address1_Edt.getText().length() < 3))) {
            addressTxt1.setError(getString(R.string.err_please_enter_valid_name));
            addressTxt1.setErrorEnabled(true);
            address1_Edt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(address2_Edt.getText().toString().trim())) {
            addressTxt2.setErrorEnabled(true);
            addressTxt2.setError(getString(R.string.err_enter_address2));
            address2_Edt.requestFocus();
            return false;
        } else if ((!TextUtils.isEmpty(address2_Edt.getText().toString().trim()) && (address2_Edt.getText().length() < 3))) {
            addressTxt2.setError(getString(R.string.err_please_enter_valid_name));
            addressTxt2.setErrorEnabled(true);
            address2_Edt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(landmark_Edt.getText().toString().trim())) {
            landmarkTxt.setError(getString(R.string.err_enter_valid_landmark));
            landmarkTxt.setErrorEnabled(true);
            landmark_Edt.requestFocus();
            return false;
        } else if ((!TextUtils.isEmpty(landmark_Edt.getText().toString().trim()) && (landmark_Edt.getText().length() < 3))) {
            landmarkTxt.setError(getString(R.string.err_please_enter_valid_name));
            landmarkTxt.setErrorEnabled(true);
            landmark_Edt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(mobile_Edt.getText().toString().trim())) {
            mobileTxt.setErrorEnabled(true);
            mobileTxt.setError(getString(R.string.err_enter_mobile_no));
            mobile_Edt.requestFocus();
            return false;
        } else if (!isValidPhone()) {
            mobileTxt.setErrorEnabled(true);
            mobileTxt.setError(getString(R.string.err_enter_valid_mobile_no));
            mobile_Edt.requestFocus();
            return false;
        } else if (provinceSpin.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.please_select_province,
                    Toast.LENGTH_LONG).show();
            return false;
        } else if (districtSpin.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.please_select_district,
                    Toast.LENGTH_LONG).show();
            return false;
        } else if (mandalSpin.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.please_select_mandal,
                    Toast.LENGTH_LONG).show();
            return false;
        } else if (villageSpin.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.please_select_village,
                    Toast.LENGTH_LONG).show();
            return false;
        } else if (villageSpin.getCount() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.select_village),
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean isValidPhone() {
        String target = mobile_Edt.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    private boolean pincode() {
        String target = pinCode_Edt.getText().toString().trim();
        if (target.length() != 6) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    public boolean nameValidate() {
        String name = name_Edt.getText().toString().trim();
        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(name);
        if (matcher.matches() == false)
            Toast.makeText(this, getString(R.string.toast_allowed_special), Toast.LENGTH_SHORT).show();
        return matcher.matches();

    }

    public boolean landmarkValidate() {
        String landmark = landmark_Edt.getText().toString().trim();
        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(landmark);
        if (matcher.matches() == false)
            Toast.makeText(this, getString(R.string.toast_allowed_special), Toast.LENGTH_SHORT).show();
        return matcher.matches();
    }

    public boolean address2Validate() {
        String address2 = address2_Edt.getText().toString().trim();
        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(address2);
        if (matcher.matches() == false)
            Toast.makeText(this, getString(R.string.toast_allowed_special), Toast.LENGTH_SHORT).show();
        return matcher.matches();
    }

    public boolean address1Validate() {
        String address1 = address1_Edt.getText().toString().trim();
        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(address1);
        if (matcher.matches() == false)
            Toast.makeText(this, getString(R.string.toast_allowed_special), Toast.LENGTH_SHORT).show();
        return matcher.matches();
    }


    private void addNewAddress() {
        showProgressDialog();
        JsonObject object = postAddNewAddressObject();
        MyServices service = ServiceFactory.createRetrofitService(AddNewAddressActivity.this, MyServices.class);
        mRegisterSubscription = service.addNewAddress(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddressAddResponceModel>() {
                    @Override
                    public void onCompleted() {
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {

                        hideProgressDialog();
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

//                        Toast.makeText(AddNewAddressActivity.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AddressAddResponceModel walletResponse) {
                        hideProgressDialog();
                        Toast.makeText(AddNewAddressActivity.this, "" + walletResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        finish();


                    }
                });
    }

    private void updateAddress() {
        showProgressDialog();
        JsonObject object = postAddNewAddressObject();
        MyServices service = ServiceFactory.createRetrofitService(AddNewAddressActivity.this, MyServices.class);
        mRegisterSubscription = service.updateAddress(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EditAddressModel>() {
                    @Override
                    public void onCompleted() {
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {

                        hideProgressDialog();
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

//                        Toast.makeText(AddNewAddressActivity.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(EditAddressModel editAddressModel) {
                        hideProgressDialog();
                        Toast.makeText(AddNewAddressActivity.this, "" + editAddressModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        finish();


                    }
                });
    }


    private void editAddress(String id) {
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        editUserAddressSubscription = service.editUserAddress(ApiConstants.Get_Address + id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EditAddressModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override

                    public void onError(Throwable e) {
                        hideProgressDialog();
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
//                        Toast.makeText(getApplicationContext(), getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(EditAddressModel editAddressModel) {
                        hideProgressDialog();
                        if (editAddressModel.getIsSuccess()) {
                            idStr = editAddressModel.getResult().getId();
                            provinceStr = editAddressModel.getResult().getProvinceId();
                            districtStr = editAddressModel.getResult().getDistrictId();
                            mandalStr = editAddressModel.getResult().getMandalId();
                            name_Edt.setText(editAddressModel.getResult().getName());
                            address1_Edt.setText(editAddressModel.getResult().getAddressLine1());
                            address2_Edt.setText(editAddressModel.getResult().getAddressLine2());
                            landmark_Edt.setText(editAddressModel.getResult().getLandmark());
                            mobile_Edt.setText(editAddressModel.getResult().getMobileNumber());
                            if (editAddressModel.getResult().getVillageId() != null && !editAddressModel.getResult().getVillageId().equals("")) {
                                villageId = editAddressModel.getResult().getVillageId();
                                pinCode_Edt.setText(editAddressModel != null && !editAddressModel.getResult().getPostCode().equals("") ? "" + editAddressModel.getResult().getPostCode() : "" + postCodeStr);
                            }

                            if (isOnline()) {
                                getProvince();
                            } else {
                                showToast(AddNewAddressActivity.this, getString(R.string.no_internet));
                            }
                        }

                    }

                });


    }

    private void getProvince() {
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        String BaseUrl = BuildConfig.LOCAL_URL + "api/Province/GetProvinces/null";
        mGetStatesSubscription = service.getProvinance(BaseUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StatesModel>() {
                    @Override
                    public void onCompleted() {
//                        Toast.makeText(AddNewAddressActivity.this, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(AddNewAddressActivity.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(StatesModel statesModel) {
                        provinceModelList = statesModel;
//                        provinceList.add("Select");

//                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_sucess), Toast.LENGTH_SHORT).show();
                        if (!provinceModelList.getListResult().isEmpty()) {
                            provinceList.add("--Select Province--");
                            for (int i = 0; i < provinceModelList.getListResult().size(); i++) {
                                provinceList.add(provinceModelList.getListResult().get(i).getName());
                            }
                        } else {
                            provinceList.clear();
                            districtList.clear();
                            villageList.clear();
                            pinCode_Edt.setText("");

                            districtArrayAdapter = new ArrayAdapter(AddNewAddressActivity.this, android.R.layout.simple_spinner_item, districtList);
                            districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            districtSpin.setAdapter(districtArrayAdapter);

                            villageArrayAdapter = new ArrayAdapter(AddNewAddressActivity.this, android.R.layout.simple_spinner_item, villageList);
                            villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            villageSpin.setAdapter(villageArrayAdapter);
                        }


                        provinanceArrayAdapter = new ArrayAdapter(AddNewAddressActivity.this, android.R.layout.simple_spinner_item, provinceList);
                        provinanceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        provinceSpin.setAdapter(provinanceArrayAdapter);

                        for (int i = 0; i < provinceModelList.getListResult().size(); i++)
                            if (provinceModelList.getListResult().get(i).getId().equals(provinceStr)) {
                                provinceSpin.setSelection(i + 1);
                                break;
                            }
                    }
                });
    }


    private void getDistricts(int id) {
        String BaseUrl = ApiConstants.BASE_URL + "api/Province/GetDistrictsByProvinceId/" + id;
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        mGetDistrictSubscription = service.getDistricts(BaseUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DistrictModel>() {
                    @Override
                    public void onCompleted() {
//                        Toast.makeText(AddNewAddressActivity.this, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(AddNewAddressActivity.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DistrictModel districtModel) {
                        districtModelList = districtModel;
//                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_sucess), Toast.LENGTH_SHORT).show();

                        districtList = new ArrayList<>();
                        if (!districtModelList.getListResult().isEmpty()) {
                            districtList.add("-- Select District --");
                            for (int i = 0; i < districtModelList.getListResult().size(); i++) {
                                districtList.add(districtModelList.getListResult().get(i).getName());
                            }
                        } else {
                            districtList.clear();
                            villageList.clear();
                            pinCode_Edt.setText("");

                            villageArrayAdapter = new ArrayAdapter(AddNewAddressActivity.this, android.R.layout.simple_spinner_item, villageList);
                            villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            villageSpin.setAdapter(villageArrayAdapter);

                        }
                        districtArrayAdapter = new ArrayAdapter(AddNewAddressActivity.this, android.R.layout.simple_spinner_item, districtList);
                        districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        districtSpin.setAdapter(districtArrayAdapter);

                        if (districtVar) {
                            for (int i = 0; i < districtModelList.getListResult().size(); i++)
                                if (districtModelList.getListResult().get(i).getId().equals(districtStr)) {
                                    districtSpin.setSelection(i + 1);
                                    break;
                                }

                            districtVar = false;
                        }

                    }
                });
    }


    private void getMandals(int Id) {
        String BaseUrl = BuildConfig.LOCAL_URL + "api/Mandals/GetMandalByDistrict/" + Id;
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        mGetDistrictSubscription = service.getMandals(BaseUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MandalModel>() {
                    @Override
                    public void onCompleted() {
//                        Toast.makeText(AddNewAddressActivity.this, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(AddNewAddressActivity.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(MandalModel mandalModel) {
                        mandalModelList = mandalModel;
//                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_sucess), Toast.LENGTH_SHORT).show();
                        mandalList = new ArrayList<>();
                        if (!mandalModelList.getListResult().isEmpty()) {
                            mandalList.add("-- Select Mandal--");
                            for (int i = 0; i < mandalModelList.getListResult().size(); i++) {
                                mandalList.add(mandalModelList.getListResult().get(i).getName());
                            }
                        } else {
                            mandalList.clear();
                            villageList.clear();
                            pinCode_Edt.setText("");

                            villageArrayAdapter = new ArrayAdapter(AddNewAddressActivity.this, android.R.layout.simple_spinner_item, villageList);
                            villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            villageSpin.setAdapter(villageArrayAdapter);

                        }


                        mandalArrayAdapter = new ArrayAdapter(AddNewAddressActivity.this, android.R.layout.simple_spinner_item, mandalList);
                        mandalArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        mandalSpin.setAdapter(mandalArrayAdapter);

                        if (mandalVar) {
                            for (int i = 0; i < mandalModelList.getListResult().size(); i++)
                                if (mandalModelList.getListResult().get(i).getId().equals(mandalStr)) {
                                    mandalSpin.setSelection(i + 1);
                                    break;
                                }

                            mandalVar = false;
                        }

                    }
                });
    }

    private void getVillages(int id) {
        String BaseUrl = BuildConfig.LOCAL_URL + "api/Villages/GetVillageByMandal/" + id;
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        mGetDistrictSubscription = service.getVillages(BaseUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VillageModel>() {
                    @Override
                    public void onCompleted() {
//                        Toast.makeText(AddNewAddressActivity.this, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(AddNewAddressActivity.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(VillageModel villageModel) {
                        villageModelList = villageModel;

//                        Toast.makeText(ProfileActivity1.this, R.string.toast_sucess, Toast.LENGTH_SHORT).show();
                        villageList = new ArrayList<>();
                        if (!villageModelList.getListResult().isEmpty()) {
                            villageList.add("-- Select Village --");
                            for (int i = 0; i < villageModelList.getListResult().size(); i++) {
                                villageList.add(villageModelList.getListResult().get(i).getName());

                            }
                        } else {
                            villageList.clear();
                            pinCode_Edt.setText("");
                        }


                        villageArrayAdapter = new ArrayAdapter(AddNewAddressActivity.this, android.R.layout.simple_spinner_item, villageList);
                        villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        villageSpin.setAdapter(villageArrayAdapter);


                        if (villageVar) {
                            for (int i = 0; i < villageModelList.getListResult().size(); i++)
                                if (villageModelList.getListResult().get(i).getId().equals(villageId)) {
                                    villageSpin.setSelection(i + 1);
                                    break;
                                }

                            villageVar = false;
                        }


                    }
                });
    }

    private JsonObject postAddNewAddressObject() {
        AddressAddSendModel postAddress = new AddressAddSendModel();
        postAddress.setAspNetUserId(SharedPrefsData.getInstance(this).getUserId(this));
        postAddress.setName(name_Edt.getText().toString());
        postAddress.setAddressLine1(address1_Edt.getText().toString());
        postAddress.setAddressLine2(address2_Edt.getText().toString());
        postAddress.setLandmark(landmark_Edt.getText().toString());
        postAddress.setMobileNumber(mobile_Edt.getText().toString());
        postAddress.setVillageId(villageId);
        if (comingFrom.equalsIgnoreCase("Edit")) {
            postAddress.setId(idStr);
        } else {
            postAddress.setId(0);
        }
        postAddress.setIsActive(true);
        postAddress.setCreatedBy(SharedPrefsData.getInstance(this).getUserId(this));
        postAddress.setModifiedBy(SharedPrefsData.getInstance(this).getUserId(this));
        postAddress.setCreated("2017-12-27T08:14:49.566Z");
        postAddress.setModified("2017-12-27T08:14:49.566Z");
        return new Gson().toJsonTree(postAddress)
                .getAsJsonObject();
    }



   /* class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context, ArrayList<String> asr) {
            this.asr = asr;
            activity = context;
        }


        public int getCount() {
            return asr.size();
        }

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(AddNewAddressActivity.this);
            txt.setPadding(16, 4, 16, 4);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(AddNewAddressActivity.this);
            txt.setGravity(Gravity.LEFT);
            txt.setPadding(16, 4, 16, 4);
            txt.setTextSize(18);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down24, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }*/
}
