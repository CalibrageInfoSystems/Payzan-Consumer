package com.calibrage.payzanconsumer.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.calibrage.payzanconsumer.BuildConfig;
import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.model.AgentModel;
import com.calibrage.payzanconsumer.framework.model.AgentResponseModel;
import com.calibrage.payzanconsumer.framework.model.BusinessCategoryModel;
import com.calibrage.payzanconsumer.framework.model.DistrictModel;
import com.calibrage.payzanconsumer.framework.model.MandalModel;
import com.calibrage.payzanconsumer.framework.model.StatesModel;
import com.calibrage.payzanconsumer.framework.model.VillageModel;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonConstants;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.calibrage.payzanconsumer.framework.util.CommonConstants.AGENT_REQUEST_CATEGORY_ID;

;


/**
 * Created by Calibrage11 on 10/5/2017.
 */

public class RequestForAgent extends BaseActivity {

    private NCBTextInputLayout stateTIl, districtTIl, mandalTIl, villageTIl, firstNameTIl, middleNameTIL, lastNameTIL, mobileTIL, emailTIL, address1TIL, address2TIL, landmarkTIL, commentTIL;
    private CommonEditText commentsEdt, landmarkEdt, address2Edt, address1Edt, emailEdt, mobileEdt, lastNameEdt, middleNameEdt, firstNameEdt;
    private Spinner villageSpn, mandalSpn, districtSpn, stateSpn,spinnerTitleType;
    private Subscription mGetStatesSubscription, mGetDistrictSubscription, getmGetDistrictSubscription;
    private MandalModel mandalModelList;
    private StatesModel provinceModelList;
    private VillageModel villageModelList;
    private DistrictModel districtModelList;
    private Button btn_submit;
    private Context context;
    private Subscription mRegisterSubscription;
    private ArrayList<String> provinceList, districtList, mandalList, villageList;
    ArrayAdapter districtArrayAdapter, villageArrayAdapter, provinceArrayAdapter, mandalArrayAdapter;
    private String firstNameStr, middleNameStr, lastNameStr, mobileStr, emailStr, stateStr, districtStr, mandalStr, villageStr, address1Str,
            address2Str, landmarkStr, commentsStr;
    private int idStr, villageId;
    ArrayList<String> titleArrayList = new ArrayList<String>();
    private ArrayList<BusinessCategoryModel.ListResult> titleListResults = new ArrayList<>();
    private Subscription operatorSubscription;
    ArrayAdapter titleArrayAdapter;

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile
            ("[a-zA-Z0-9+._%-+]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "." + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_as_agent);
        provinceList = new ArrayList<>();
        districtList = new ArrayList<>();
        villageList = new ArrayList<>();
        mandalList = new ArrayList<>();
        setViews();
        initViews();
        initTitleSpinner();

//        address1Edt.setFilters(new InputFilter[]{EMOJI_FILTER});
//        address2Edt.setFilters(new InputFilter[]{EMOJI_FILTER});
//        landmarkEdt.setFilters(new InputFilter[]{EMOJI_FILTER});
//        commentsEdt.setFilters(new InputFilter[]{EMOJI_FILTER});
        ShowActionBar(getString(R.string.request_for_agent));

        if (CommonUtil.isNetworkAvailable(this)) {
            getProvince();
            getRequestTitle(CommonConstants.TITLE_ID);
        }

    }

    private void getRequestTitle(String titleType) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getBusinessRequest(ApiConstants.BUSINESS_CAT_REQUESTS + Integer.parseInt(titleType))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BusinessCategoryModel>() {
                    @Override
                    public void onCompleted() {

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
                        Toast.makeText(context, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BusinessCategoryModel businessCategoryModel) {
                        titleListResults = (ArrayList<BusinessCategoryModel.ListResult>) businessCategoryModel.getListResult();
                        titleArrayList.add(0, getString(R.string.select_title));
                        for (int i = 0; i < businessCategoryModel.getListResult().size(); i++) {
                            titleArrayList.add(businessCategoryModel.getListResult().get(i).getDescription());
                        }

                        titleArrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, titleArrayList);
                        titleArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner

                        spinnerTitleType.setAdapter(titleArrayAdapter);


                    }

                });
    }

    private void initTitleSpinner() {
        spinnerTitleType = (Spinner) findViewById(R.id.spinner_title_type);
        spinnerTitleType.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null && event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
                    boolean isKeyboardUp = imm.isAcceptingText();

                    if (isKeyboardUp)
                    {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false;
            }
        }) ;
        spinnerTitleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGetStatesSubscription != null) {
            mGetStatesSubscription.unsubscribe();
        }
        if (mGetDistrictSubscription != null) {
            mGetDistrictSubscription.unsubscribe();
        }
        if (getmGetDistrictSubscription != null) {
            getmGetDistrictSubscription.unsubscribe();
        }
        if (mGetDistrictSubscription != null) {
            mGetDistrictSubscription.unsubscribe();
        }

    }

    private void setViews() {
        context = RequestForAgent.this;
        firstNameTIl = (NCBTextInputLayout) findViewById(R.id.firstNameTIl);
        middleNameTIL = (NCBTextInputLayout) findViewById(R.id.middleNameTIL);
        lastNameTIL = (NCBTextInputLayout) findViewById(R.id.lastNameTIL);
        mobileTIL = (NCBTextInputLayout) findViewById(R.id.mobileTIL);
        emailTIL = (NCBTextInputLayout) findViewById(R.id.emailTIL);
        address1TIL = (NCBTextInputLayout) findViewById(R.id.address1TIL);
        address2TIL = (NCBTextInputLayout) findViewById(R.id.address2TIL);
        landmarkTIL = (NCBTextInputLayout) findViewById(R.id.landmarkTIL);
        commentTIL = (NCBTextInputLayout) findViewById(R.id.commentTIL);
        commentTIL = (NCBTextInputLayout) findViewById(R.id.commentTIL);
        commentsEdt = (CommonEditText) findViewById(R.id.commentsEdt);
        landmarkEdt = (CommonEditText) findViewById(R.id.landmarkEdt);
        address2Edt = (CommonEditText) findViewById(R.id.address2Edt);
        address1Edt = (CommonEditText) findViewById(R.id.address1Edt);
        emailEdt = (CommonEditText) findViewById(R.id.emailEdt);
        mobileEdt = (CommonEditText) findViewById(R.id.mobileEdt);
        lastNameEdt = (CommonEditText) findViewById(R.id.lastNameEdt);
        middleNameEdt = (CommonEditText) findViewById(R.id.middleNameEdt);
        firstNameEdt = (CommonEditText) findViewById(R.id.firstNameEdt);
        villageSpn = (Spinner) findViewById(R.id.villageSpn);
        mandalSpn = (Spinner) findViewById(R.id.mandalSpn);
        districtSpn = (Spinner) findViewById(R.id.districtSpn);
        stateSpn = (Spinner) findViewById(R.id.stateSpn);
        btn_submit = (Button) findViewById(R.id.submit);

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
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(StatesModel statesModel) {
                        provinceModelList = statesModel;
                        if (!provinceModelList.getListResult().isEmpty()) {
                            provinceList.add("--Select Province--");
                            for (int i = 0; i < provinceModelList.getListResult().size(); i++) {
                                provinceList.add(provinceModelList.getListResult().get(i).getName());
                            }
                        } else {
                            provinceList.clear();
                            districtList.clear();
                            villageList.clear();

                            districtArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, districtList);
                            districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            districtSpn.setAdapter(districtArrayAdapter);

                            villageArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, villageList);
                            villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            villageSpn.setAdapter(villageArrayAdapter);
                        }

                        provinceArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, provinceList);
                        provinceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        stateSpn.setAdapter(provinceArrayAdapter);
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
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DistrictModel districtModel) {
                        districtModelList = districtModel;

                        districtList = new ArrayList();

                        if (!districtModelList.getListResult().isEmpty()) {
                            districtList.add("--Select District--");
                            for (int i = 0; i < districtModelList.getListResult().size(); i++) {
                                districtList.add(districtModelList.getListResult().get(i).getName());
                            }
                        } else {
                            districtList.clear();
                            villageList.clear();
                            villageArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, villageList);
                            villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            villageSpn.setAdapter(villageArrayAdapter);
                        }
                        districtArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, districtList);
                        districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        districtSpn.setAdapter(districtArrayAdapter);
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
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(MandalModel mandalModel) {
                        mandalModelList = mandalModel;
                        mandalList = new ArrayList<>();
                        if (!mandalModelList.getListResult().isEmpty()) {
                            mandalList.add("--Select Mandal--");
                            for (int i = 0; i < mandalModelList.getListResult().size(); i++) {
                                mandalList.add(mandalModelList.getListResult().get(i).getName());
                            }
                        } else {
                            mandalList.clear();
                            villageList.clear();

                            villageArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, villageList);
                            villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            villageSpn.setAdapter(villageArrayAdapter);
                        }

                        mandalArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, mandalList);
                        mandalArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        mandalSpn.setAdapter(mandalArrayAdapter);
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
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(VillageModel villageModel) {
                        villageModelList = villageModel;

                        villageList = new ArrayList<>();
                        if (!villageModelList.getListResult().isEmpty()) {
                            villageList.add("--Select Village--");
                            for (int i = 0; i < villageModelList.getListResult().size(); i++) {
                                villageList.add(villageModelList.getListResult().get(i).getName());
                            }
                        } else {
                            villageList.clear();
                        }

                        villageArrayAdapter = new ArrayAdapter(RequestForAgent.this, android.R.layout.simple_spinner_item, villageList);
                        villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        villageSpn.setAdapter(villageArrayAdapter);

                    }
                });
    }


    private void initViews() {
        firstNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    firstNameTIl.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        middleNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    middleNameTIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lastNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    lastNameTIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mobileEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    mobileTIL.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String x = "";
                x = s.toString();
                if (x.startsWith("0")) {
                    mobileEdt.setText("");
                }

            }
        });
        emailEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    emailTIL.setErrorEnabled(false);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String x = "";
                x = s.toString();
                if (x.startsWith(".")) {
                    emailEdt.setText("");
                }

            }
        });
        villageSpn.setOnTouchListener(new View.OnTouchListener() {

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
        mandalSpn.setOnTouchListener(new View.OnTouchListener() {

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
        districtSpn.setOnTouchListener(new View.OnTouchListener() {

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
        stateSpn.setOnTouchListener(new View.OnTouchListener() {

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
        stateSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!provinceModelList.getListResult().isEmpty() && stateSpn.getSelectedItemPosition() != 0) {
                    districtList = new ArrayList<>();
                    villageList = new ArrayList<>();
                    mandalList = new ArrayList<>();
                    getDistricts(provinceModelList.getListResult().get(i - 1).getId());
                } else {
                    districtList.clear();
                    villageList.clear();
                    mandalList.clear();
                    districtSpn.setAdapter(null);
                    mandalSpn.setAdapter(null);
                    villageSpn.setAdapter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        districtSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!districtModelList.getListResult().isEmpty() && districtSpn.getSelectedItemPosition() != 0) {
                    villageList = new ArrayList<>();
                    mandalList = new ArrayList<>();
                    getMandals(districtModelList.getListResult().get(i - 1).getId());
                } else {
                    villageList.clear();
                    mandalList.clear();
                    mandalSpn.setAdapter(null);
                    villageSpn.setAdapter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(RequestForAgent.this, R.string.toast_iscalling, Toast.LENGTH_SHORT).show();
            }
        });
        mandalSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!mandalModelList.getListResult().isEmpty() && mandalSpn.getSelectedItemPosition() != 0) {
                    villageList = new ArrayList<>();
                    getVillages(mandalModelList.getListResult().get(i - 1).getId());
                } else {
                    villageList.clear();
                    villageSpn.setAdapter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        villageSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (villageSpn.getSelectedItemPosition() != 0) {
                    villageId = villageModelList.getListResult().get(villageSpn.getSelectedItemPosition() - 1).getId();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        address1Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    address1TIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        address2Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    address2TIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        landmarkEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    landmarkTIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        commentsEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    commentTIL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateUi() && CommonUtil.isNetworkAvailable(RequestForAgent.this)) {
                    postAgentRequest();
                }
            }
        });
    }


    private boolean validateUi() {
        if (spinnerTitleType.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.err_please_enter_title, Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(firstNameEdt.getText().toString().trim())) {
            firstNameTIl.setError(getString(R.string.err_enter_first_name));
            firstNameTIl.setErrorEnabled(true);
            firstNameEdt.requestFocus();
            return false;
        } else if (!TextUtils.isEmpty(firstNameEdt.getText().toString().trim()) && (firstNameEdt.getText().length() < 3)) {
            firstNameTIl.setErrorEnabled(true);
            firstNameTIl.setError(getString(R.string.err_please_enter_valid_name));
            firstNameEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(lastNameEdt.getText().toString().trim())) {
            lastNameTIL.setError(getString(R.string.err_enter_last_name));
            lastNameTIL.setErrorEnabled(true);
            lastNameEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(mobileEdt.getText().toString().trim())) {
            mobileTIL.setErrorEnabled(true);
            mobileTIL.setError(getString(R.string.err_enter_mobile_no));
            mobileEdt.requestFocus();
            return false;
        } else if (!isValidPhone()) {
            mobileTIL.setErrorEnabled(true);
            mobileTIL.setError(getString(R.string.err_enter_valid_mobile_no));
            mobileEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(emailEdt.getText().toString().trim())) {
            emailTIL.setError(getString(R.string.err_enter_email_id));
            emailTIL.setErrorEnabled(true);
            emailEdt.requestFocus();
            return false;
        } else if (!checkEmail()) {
            emailTIL.setError(getString(R.string.err_enter_valid_email_id));
            emailTIL.setErrorEnabled(true);
            emailEdt.requestFocus();
            return false;
        } else if (stateSpn.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.please_select_province, Toast.LENGTH_SHORT).show();
            return false;
        } else if (districtSpn.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.please_select_district, Toast.LENGTH_SHORT).show();
            return false;
        } else if (mandalSpn.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.please_select_mandal, Toast.LENGTH_SHORT).show();
            return false;
        } else if (villageSpn.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.please_select_village), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(address1Edt.getText().toString().trim())) {
            address1TIL.setError(getString(R.string.err_enter_address1));
            address1Edt.requestFocus();
            address1TIL.setErrorEnabled(true);
            return false;
        } else if (!TextUtils.isEmpty(address1Edt.getText().toString().trim()) && (address1Edt.getText().length() < 3)) {
            address1TIL.setErrorEnabled(true);
            address1TIL.setError(getString(R.string.err_please_enter_valid_name));
            address1Edt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(address2Edt.getText().toString().trim())) {
            address2TIL.setError(getString(R.string.err_enter_address2));
            address2TIL.setErrorEnabled(true);
            address2Edt.requestFocus();
            return false;
        } else if (!TextUtils.isEmpty(address2Edt.getText().toString().trim()) && (address2Edt.getText().length() < 3)) {
            address2TIL.setErrorEnabled(true);
            address2TIL.setError(getString(R.string.err_please_enter_valid_name));
            address2Edt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(landmarkEdt.getText().toString().trim())) {
            landmarkTIL.setErrorEnabled(true);
            landmarkTIL.setError(getString(R.string.err_enter_landmark));
            landmarkEdt.requestFocus();
            return false;
        } else if (!TextUtils.isEmpty(landmarkEdt.getText().toString().trim()) && (landmarkEdt.getText().length() < 3)) {
            landmarkTIL.setErrorEnabled(true);
            landmarkTIL.setError(getString(R.string.err_please_enter_valid_name));
            landmarkEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(commentsEdt.getText().toString().trim())) {
            commentTIL.setErrorEnabled(true);
            commentTIL.setError(getString(R.string.err_enter_comments));
            commentsEdt.requestFocus();
            return false;
        } else if (!TextUtils.isEmpty(commentsEdt.getText().toString().trim()) && (commentsEdt.getText().length() < 3)) {
            commentTIL.setErrorEnabled(true);
            commentTIL.setError(getString(R.string.err_please_enter_valid_name));
            commentsEdt.requestFocus();
            return false;
        }
        return true;
    }

    private boolean checkEmail() {
        String email = emailEdt.getText().toString().trim();
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private boolean isValidPhone() {
        String target = mobileEdt.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }


    private void postAgentRequest() {
        JsonObject object = getAgentObject();
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        mRegisterSubscription = (Subscription) service.agentRequest(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AgentResponseModel>() {
                    @Override
                    public void onCompleted() {
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
                        Toast.makeText(RequestForAgent.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AgentResponseModel AgentResponseModel) {
                        if (AgentResponseModel.getIsSuccess()) {

                            Toast.makeText(RequestForAgent.this, AgentResponseModel.getEndUserMessage(), Toast.LENGTH_LONG).show();
                            finish();

                        } else {
                            if (AgentResponseModel.getEndUserMessage() == null) {
                                Toast.makeText(RequestForAgent.this, AgentResponseModel.getValidationErrors().get(0).getDescription(), Toast.LENGTH_LONG).show();

                                finish();
                            } else {
                                Toast.makeText(RequestForAgent.this, AgentResponseModel.getEndUserMessage(), Toast.LENGTH_LONG).show();

                                finish();

                            }

                        }



/*
                        Toast.makeText(RequestForAgent.this, AgentResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        finish();*/
                    }
                });

    }

    private JsonObject getAgentObject() {
        commentsStr = commentsEdt.getText().toString().trim();
        lastNameStr = lastNameEdt.getText().toString().trim();
        firstNameStr = firstNameEdt.getText().toString().trim();
        middleNameStr = middleNameEdt.getText().toString().trim();
        mobileStr = mobileEdt.getText().toString().trim();
        emailStr = emailEdt.getText().toString().trim();
        address1Str = address1Edt.getText().toString().trim();
        address2Str = address2Edt.getText().toString().trim();
        landmarkStr = landmarkEdt.getText().toString().trim();


        AgentModel agentModel = new AgentModel();
        agentModel.setId(0);
        agentModel.setAgentRequestCategoryId(Integer.parseInt(AGENT_REQUEST_CATEGORY_ID));
        if(spinnerTitleType.getSelectedItemPosition()!=0)
        {
            agentModel.setTitleTypeId(titleListResults.get(spinnerTitleType.getSelectedItemPosition()-1).getId());
        }

        agentModel.setFirstName(firstNameStr);
        agentModel.setMiddleName(middleNameStr);
        agentModel.setLastName(lastNameStr);
        agentModel.setMobileNumber(mobileStr);
        agentModel.setEmail(emailStr);
        agentModel.setAddressLine1(address1Str);
        agentModel.setAddressLine2(address2Str);
        agentModel.setLandmark(landmarkStr);
        agentModel.setVillageId(villageId);
        agentModel.setComments(commentsStr);
        agentModel.setCreated("2017-10-31T05:15:57.983Z");
        return new Gson().toJsonTree(agentModel)
                .getAsJsonObject();

    }
}






