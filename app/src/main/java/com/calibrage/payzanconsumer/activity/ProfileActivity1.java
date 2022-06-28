package com.calibrage.payzanconsumer.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.calibrage.payzanconsumer.BuildConfig;
import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.ProfileFragment;
import com.calibrage.payzanconsumer.fragement.UserProfileHome;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.controls.CommonTextView;
import com.calibrage.payzanconsumer.framework.model.DistrictModel;
import com.calibrage.payzanconsumer.framework.model.MandalModel;
import com.calibrage.payzanconsumer.framework.model.StatesModel;
import com.calibrage.payzanconsumer.framework.model.UserProfileInfoRequestModel;
import com.calibrage.payzanconsumer.framework.model.UserProfileInfoResponseModel;
import com.calibrage.payzanconsumer.framework.model.UserProfileInfoSucessModel;
import com.calibrage.payzanconsumer.framework.model.VillageModel;
import com.calibrage.payzanconsumer.framework.networkservices.ApiConstants;
import com.calibrage.payzanconsumer.framework.networkservices.MyServices;
import com.calibrage.payzanconsumer.framework.networkservices.ServiceFactory;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.calibrage.payzanconsumer.framework.util.CommonUtil.isValidEmail;

public class
ProfileActivity1 extends BaseActivity {

    public static final String TAG = ProfileFragment.class.getSimpleName();
    private View view;
    private CommonTextView editImage;
    private CommonEditText firstNameEdt, lastNameEdt, userNameEdt, dobEdt, middleNameEdt, address1Edt, address2Edt, landMarkEdt, mobileNumEdt, emailEdt, postCode;
    private RadioButton maleRB, femaleRB;
    private CommonButton submit;
    private CircularImageView profileImage;
    private NCBTextInputLayout inp_fname, inp_username, inp_laname, inp_dob, middle_name_Text, address1_Text, address2_Text, landmark_Text, mobile_number_Text, email_Text;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    static Pattern pattern;
    static Matcher matcher;
    private Context context;
    Toolbar toolbar;
    private Subscription userProfileSubscription, mUserProfileSubscription, mProvinceSubscription, mVillageSubscription, mGetDistrictSubscription;
    private RadioGroup genderGroup;
    private MandalModel mandalModelList;
    private StatesModel provinceModelList;
    private VillageModel villageModelList;
    private DistrictModel districtModelList;
    private ArrayList<String> provinceList, districtList, mandalList, villageList;
    private ArrayAdapter provinceArrayAdapter, mandalArrayAdapter, districtArrayAdapter, villageArrayAdapter;
    private Spinner provinceSpin, districtSpin, mandalSpin, villageSpin, titleSpin;
    private UserProfileInfoResponseModel mResponseModel;
    private File Image_destination = new File(CommonUtil.StoragePath, "uploadImg" + ".jpg");

    final String NAME_PATTERN = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
    public static final int PICK_IMAGE = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String villageStr;
    private int idStr, villageId, provinceStr, mandalStr, districtStr, postCodeStr;
    private boolean villageVar = true,mandalVar = true,districtVar = true;
    private String imageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        ShowActionBar(getString(R.string.profile));
        context = this;
        setViews();
        initViews();
//        address1Edt.setFilters(new InputFilter[]{EMOJI_FILTER});
//        address2Edt.setFilters(new InputFilter[]{EMOJI_FILTER});
//        landMarkEdt.setFilters(new InputFilter[]{EMOJI_FILTER});

        provinceList = new ArrayList<>();
        districtList = new ArrayList<>();
        mandalList = new ArrayList<>();
        villageList = new ArrayList<>();
        if (CommonUtil.isNetworkAvailable(this)) {
            showProgressDialog();
            getUserProfileInfo(SharedPrefsData.getInstance(context).getUserId(context));
//            getProvince();
        } else {
//CommonUtil.displayDialogWindow();
        }

        /*Permissions*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !CommonUtil.areAllPermissionsAllowed(this, CommonUtil.PERMISSIONS_REQUIRED)) {
            ActivityCompat.requestPermissions(this, CommonUtil.PERMISSIONS_REQUIRED, CommonUtil.PERMISSION_CODE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (userProfileSubscription != null) {
            userProfileSubscription.unsubscribe();
        }
        if (mUserProfileSubscription != null) {
            mUserProfileSubscription.unsubscribe();
        }
        if (mProvinceSubscription != null) {
            mProvinceSubscription.unsubscribe();
        }
        if (mVillageSubscription != null) {
            mVillageSubscription.unsubscribe();
        }
        if (mGetDistrictSubscription != null) {
            mGetDistrictSubscription.unsubscribe();
        }

    }


    private void setViews() {
        profileImage = (CircularImageView) findViewById(R.id.profileimage);
        editImage = (CommonTextView) findViewById(R.id.editimage);
        firstNameEdt = (CommonEditText) findViewById(R.id.firstNameEdt);
        lastNameEdt = (CommonEditText) findViewById(R.id.lastNameEdt);
        userNameEdt = (CommonEditText) findViewById(R.id.userNameEdt);
        dobEdt = (CommonEditText) findViewById(R.id.dobEdt);
        middleNameEdt = (CommonEditText) findViewById(R.id.middleNameEdt);
        address1Edt = (CommonEditText) findViewById(R.id.address1Edt);
        address2Edt = (CommonEditText) findViewById(R.id.address2Edt);
        landMarkEdt = (CommonEditText) findViewById(R.id.landMarkEdt);
        mobileNumEdt = (CommonEditText) findViewById(R.id.mobileNumEdt);
        emailEdt = (CommonEditText) findViewById(R.id.emailEdt);
        postCode = (CommonEditText) findViewById(R.id.postCode);


        maleRB = (RadioButton) findViewById(R.id.maleRB);
        femaleRB = (RadioButton) findViewById(R.id.femaleRB);
        submit = (CommonButton) findViewById(R.id.submit);
        inp_fname = (NCBTextInputLayout) findViewById(R.id.inp_fname);
        inp_laname = (NCBTextInputLayout) findViewById(R.id.inp_laname);
        inp_username = (NCBTextInputLayout) findViewById(R.id.inp_username);
        inp_dob = (NCBTextInputLayout) findViewById(R.id.inp_dob);
        middle_name_Text = (NCBTextInputLayout) findViewById(R.id.middle_name_Text);
        address1_Text = (NCBTextInputLayout) findViewById(R.id.address1_Text);
        address2_Text = (NCBTextInputLayout) findViewById(R.id.address2_Text);
        landmark_Text = (NCBTextInputLayout) findViewById(R.id.landmark_Text);
        mobile_number_Text = (NCBTextInputLayout) findViewById(R.id.mobile_number_Text);
        email_Text = (NCBTextInputLayout) findViewById(R.id.email_Text);


        genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        provinceSpin = (Spinner) findViewById(R.id.provinceSpin);
        districtSpin = (Spinner) findViewById(R.id.districtSpin);
        mandalSpin = (Spinner) findViewById(R.id.mandalSpin);
        villageSpin = (Spinner) findViewById(R.id.villageSpin);
        titleSpin = (Spinner) findViewById(R.id.titleSpin);


        dobEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity1.this, R.style.datepicker
                        , onDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateset:dd/mm/yyy:" + year + "-" + month + "-" + day);
                String date = year + "-" + month + "-" + day;
                dobEdt.setText(date);

            }
        };
    }


    private void initViews() {
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        //   firstNameEdt.setFilters(new InputFilter[] { filter });
        firstNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    inp_fname.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        lastNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    inp_laname.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        userNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    inp_username.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dobEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    inp_dob.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        middleNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    middle_name_Text.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        address1Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    address1_Text.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        address2Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    address2_Text.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mobileNumEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mobile_number_Text.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        emailEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    email_Text.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String x = "";
                x = editable.toString();
                if (x.startsWith(".")) {
                    emailEdt.setText("");
                }

            }
        });
        landMarkEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    landmark_Text.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateUI()) {
                    showProgressDialog();
                    postAgentRequest();

                }
            }
        });

        provinceSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                provinceSpin.setSelection(i);
                if (provinceSpin.getSelectedItemPosition() != 0) {
                    districtList = new ArrayList<>();
                    villageList = new ArrayList<>();
                    mandalList = new ArrayList<>();
                    postCode.setText("");
//                    int idStr, villageId, provinceStr, mandalStr, districtStr, postCodeStr;
                    getDistricts(provinceModelList.getListResult().get(i - 1).getId());
                } else {
                    districtList.clear();
                    villageList.clear();
                    mandalList.clear();
                    districtSpin.setAdapter(null);
                    mandalSpin.setAdapter(null);
                    villageSpin.setAdapter(null);
                    postCode.setText("");
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
                if (districtSpin.getSelectedItemPosition() != 0) {
                    villageList = new ArrayList<>();
                    mandalList = new ArrayList<>();
                    postCode.setText("");
                    getMandals(districtModelList.getListResult().get(i - 1).getId());
                } else {
                    villageList.clear();
                    mandalList.clear();
                    mandalSpin.setAdapter(null);
                    villageSpin.setAdapter(null);
                    postCode.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        mandalSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mandalSpin.setSelection(i);
                if (mandalSpin.getSelectedItemPosition() != 0) {
                    villageList = new ArrayList<>();
                    postCode.setText("");
                    getVillages(mandalModelList.getListResult().get(i - 1).getId());
                } else {
                    villageList.clear();
                    villageSpin.setAdapter(null);
                    postCode.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        villageSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                villageSpin.setSelection(i);
                if (villageSpin.getSelectedItemPosition() != 0) {
                    postCode.setText("");
                    villageId = villageModelList.getListResult().get(villageSpin.getSelectedItemPosition() - 1).getId();
                    postCodeStr = villageModelList.getListResult().get(villageSpin.getSelectedItemPosition() - 1).getPostCode();
                    postCode.setText("" + postCodeStr);
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
    }

    /**
     * Validate the all fields
     *
     * @return
     */
    private boolean validateUI() {
        if (CommonUtil.isEmptySpinner(titleSpin)) {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(titleSpin.getWindowToken(), 0);
            showToast(getResources().getString(R.string.err_please_enter_title));
            titleSpin.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(firstNameEdt.getText().toString().trim())) {
            inp_fname.setError(getString(R.string.err_enter_first_name));
            inp_fname.setErrorEnabled(true);
            firstNameEdt.requestFocus();
            return false;

        } else if (!TextUtils.isEmpty(firstNameEdt.getText().toString().trim()) && (firstNameEdt.getText().length() < 3)) {
            inp_fname.setErrorEnabled(true);
            inp_fname.setError(getString(R.string.err_please_enter_valid_name));
            firstNameEdt.requestFocus();
            return false;
        } else if (!fNameValidate()) {
            inp_fname.setErrorEnabled(true);
            inp_fname.setError(getString(R.string.err_enter_valid_firstname));
            firstNameEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(lastNameEdt.getText().toString().trim())) {
            inp_laname.setErrorEnabled(true);
            inp_laname.setError(getString(R.string.err_enter_last_name));
            lastNameEdt.requestFocus();
            return false;
        } else if (!lNameValidate()) {
            inp_laname.setErrorEnabled(true);
            inp_laname.setError(getString(R.string.err_enter_valid_lastname));
            lastNameEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(userNameEdt.getText().toString().trim())) {
            inp_username.setError(getString(R.string.err_enter_username));
            inp_username.setErrorEnabled(true);
            userNameEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(address1Edt.getText().toString())) {
            address1_Text.setError(getString(R.string.err_enter_address1));
            address1_Text.setErrorEnabled(true);
            address1Edt.requestFocus();
            return false;
        } else if (!TextUtils.isEmpty(address1Edt.getText().toString()) && (address1Edt.getText().length() < 3)) {
            address1_Text.setErrorEnabled(true);
            address1_Text.setError(getString(R.string.err_please_enter_valid_name));
            address1Edt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(address2Edt.getText().toString())) {
            address2_Text.setError(getString(R.string.err_enter_address2));
            address2_Text.setErrorEnabled(true);
            address2Edt.requestFocus();
            return false;
        } else if (!TextUtils.isEmpty(address2Edt.getText().toString().trim()) && (address2Edt.getText().length() < 3)) {
            address2_Text.setErrorEnabled(true);
            address2_Text.setError(getString(R.string.err_please_enter_valid_name));
            address2Edt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(landMarkEdt.getText().toString())) {
            landmark_Text.setError(getString(R.string.err_enter_landmark));
            landmark_Text.setErrorEnabled(true);
            landMarkEdt.requestFocus();
            return false;
        } else if (!TextUtils.isEmpty(landMarkEdt.getText().toString().trim()) && (landMarkEdt.getText().length() < 3)) {
            landmark_Text.setErrorEnabled(true);
            landmark_Text.setError(getString(R.string.err_please_enter_valid_name));
            landMarkEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(mobileNumEdt.getText().toString())) {
            mobile_number_Text.setError(getString(R.string.err_enter_mobile_number));
            mobile_number_Text.setErrorEnabled(true);
            mobileNumEdt.requestFocus();
            return false;
        } else if (!isValidPhone()) {
            mobile_number_Text.setErrorEnabled(true);
            mobile_number_Text.setError(getString(R.string.err_enter_valid_mobile_no));
            mobileNumEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(emailEdt.getText().toString())) {
            email_Text.setError(getString(R.string.err_enter_email_id));
            email_Text.setErrorEnabled(true);
            emailEdt.requestFocus();
            return false;
        } else if (!isValidEmail(emailEdt.getText().toString())) {
            email_Text.setErrorEnabled(true);
            email_Text.setError(getString(R.string.err_enter_valid_email_id));
            emailEdt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(emailEdt.getText().toString().trim()) && (emailEdt.getText().length() < 5)) {
            email_Text.setErrorEnabled(true);
            email_Text.setError(getString(R.string.err_enter_email_id));
            email_Text.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(dobEdt.getText().toString().trim())) {
            inp_dob.setErrorEnabled(true);
            dobEdt.requestFocus();
            inp_dob.setError(getString(R.string.err_enter_dob));
            return false;
        }  else if (provinceSpin.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.please_select_province, Toast.LENGTH_SHORT).show();
            return false;
        } else if (districtSpin.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.please_select_district, Toast.LENGTH_SHORT).show();
            return false;
        } else if (mandalSpin.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.please_select_mandal, Toast.LENGTH_SHORT).show();
            return false;
        } else if (villageSpin.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.please_select_village), Toast.LENGTH_LONG).show();
            return false;
        }
        else if (genderGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(ProfileActivity1.this, getString(R.string.select_gender), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Validation for phone number
     *
     * @return
     */
    private boolean isValidPhone() {
        String target = mobileNumEdt.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    /**
     * validation for first name
     *
     * @return
     */
    public boolean fNameValidate() {
        String name = firstNameEdt.getText().toString().trim();
        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(name);
        if (matcher.matches() == false)
            Toast.makeText(this, getString(R.string.toast_allowed_special), Toast.LENGTH_SHORT).show();
        return matcher.matches();

    }

    /**
     * Validation for last name
     *
     * @return
     */
    public boolean lNameValidate() {
        String name = lastNameEdt.getText().toString().trim();
        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(name);
        if (matcher.matches() == false)
            Toast.makeText(this, getString(R.string.toast_allowed_special), Toast.LENGTH_SHORT).show();
        return matcher.matches();

    }

    /**
     * Validation for middle name
     *
     * @return
     */
    public boolean middleNameValidate() {
        String name = middleNameEdt.getText().toString().trim();
        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(name);
        if (matcher.matches() == false)
            Toast.makeText(this, getString(R.string.toast_allowed_special), Toast.LENGTH_SHORT).show();
        return matcher.matches();

    }

    /**
     * Dialog for select camera or gallery options
     */
    public void openDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Select From");
        alertDialogBuilder.setPositiveButton("camera",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dispatchTakePictureIntent();
                    }
                });

        alertDialogBuilder.setNegativeButton("gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openGallery();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            // Get the url from data
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    // Set the image in ImageView
                    profileImage.setImageURI(selectedImageUri);

                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageString = encodeImage(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImage.setImageBitmap(imageBitmap);

            //encode image to base64 string
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap = (Bitmap) extras.get("data");
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null)
                if (cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    res = cursor.getString(column_index);
                }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return res;
    }

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.toast_camera), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.toast_camera_denied, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getUserProfileInfo(String userId) {
        if (CommonUtil.isNetworkAvailable(context)) {
            MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
            userProfileSubscription = service.getUserProfileInfo(ApiConstants.GetUserPersonalInfo + userId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UserProfileInfoResponseModel>() {
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
                        public void onNext(UserProfileInfoResponseModel operatorModel) {
                            hideProgressDialog();
                            getProvince();

                            Log.e("response", operatorModel.getIsSuccess().toString());
                            if (operatorModel.getIsSuccess()) {
                                idStr = operatorModel.getResult().getId();
                                provinceStr = operatorModel.getResult().getProvinceId();
                                districtStr = operatorModel.getResult().getDistrictId();
                                mandalStr = operatorModel.getResult().getMandalId();

                                firstNameEdt.setText(operatorModel.getResult().getFirstName());
                                middleNameEdt.setText(operatorModel.getResult().getMiddleName());
                                lastNameEdt.setText(operatorModel.getResult().getLastName());
                                userNameEdt.setText(operatorModel.getResult().getUserName());
                                address1Edt.setText(operatorModel.getResult().getAddress1());
                                address2Edt.setText(operatorModel.getResult().getAddress2());
                                landMarkEdt.setText(operatorModel.getResult().getLandmark());
                                mobileNumEdt.setText(operatorModel.getResult().getPhone());
                                emailEdt.setText(operatorModel.getResult().getEmail());
                                dobEdt.setText(CommonUtil.dateFormat(operatorModel.getResult().getDOB()));
                                Picasso.with(context).load(operatorModel.getResult().getProfilePicUrl()).fit().centerCrop()
                                        .placeholder(R.drawable.user)
                                        .into(profileImage);

                                if (operatorModel.getResult().getVillageId() != null && !operatorModel.getResult().getVillageId().equals("")) {
                                    villageId = operatorModel.getResult().getVillageId();
                                    postCode.setText(operatorModel != null && !operatorModel.getResult().getPostCode().equals("") ? "" + operatorModel.getResult().getPostCode() : "" + postCodeStr);
                                }

                                if (operatorModel.getResult().getGenderTypeId() != null && !operatorModel.getResult().getGenderTypeId().equals("")) {
                                    if (operatorModel.getResult().getGenderTypeId().equals(20)) {
                                        maleRB.setChecked(true);
                                    } else {
                                        femaleRB.setChecked(true);
                                    }
                                }
                                if (operatorModel.getResult().getTitleTypeId() != null && !operatorModel.getResult().getTitleTypeId().equals("")) {
                                    if (operatorModel.getResult().getTitleTypeId().equals(17)) {
                                        titleSpin.setSelection(1);
                                    } else if (operatorModel.getResult().getTitleTypeId().equals(18)) {
                                        titleSpin.setSelection(2);
                                    } else if (operatorModel.getResult().getTitleTypeId().equals(19)) {
                                        titleSpin.setSelection(3);
                                    }
                                }

                            } else {
                                Toast.makeText(context, "" + operatorModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }
    }

    private void postAgentRequest() {
        JsonObject object = getUserProfileInfoObject();
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        mUserProfileSubscription = (Subscription) service.sendUserProfileInfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserProfileInfoSucessModel>() {
                    @Override
                    public void onCompleted() {
//                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(UserProfileInfoSucessModel AgentResponseModel) {
                        hideProgressDialog();
                        Toast.makeText(ProfileActivity1.this, AgentResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        UserProfileHome.userMail.setText(AgentResponseModel.getResult().getEmail());
                        finish();
                    }
                });

    }

    private JsonObject getUserProfileInfoObject() {
        UserProfileInfoRequestModel mModel = new UserProfileInfoRequestModel();
        mModel.setAspNetUserId(SharedPrefsData.getInstance(context).getUserId(context));
        if (titleSpin.getSelectedItem().toString().equalsIgnoreCase("Mr.")) {
            mModel.setTitleTypeId(17);
        } else if (titleSpin.getSelectedItem().toString().equalsIgnoreCase("Ms.")) {
            mModel.setTitleTypeId(18);
        } else {
            mModel.setTitleTypeId(19);
        }
        mModel.setFirstName(firstNameEdt.getText().toString());
        mModel.setMiddleName(middleNameEdt.getText().toString());
        mModel.setLastName(lastNameEdt.getText().toString());
        mModel.setPhone(mobileNumEdt.getText().toString());
        mModel.setEmail(emailEdt.getText().toString());
        mModel.setGenderTypeId(maleRB.isChecked() ? 20 : 21);
        mModel.setDOB(CommonUtil.dateFormat(dobEdt.getText().toString()) + "T00:00:00.0000");
        mModel.setAddress1(address1Edt.getText().toString());
        mModel.setAddress2(address2Edt.getText().toString());
        mModel.setLandmark(landMarkEdt.getText().toString());
        mModel.setVillageId(villageId);
        mModel.setParentAspNetUserId(SharedPrefsData.getInstance(context).getUserId(context));
        mModel.setEducationTypeId(null);
        mModel.setId(idStr);
        mModel.setIsActive(true);
        mModel.setCreatedBy(SharedPrefsData.getInstance(context).getUserId(context));
        mModel.setModifiedBy(SharedPrefsData.getInstance(context).getUserId(context));
        mModel.setCreated("2017-10-31T05:15:57.983Z");
        mModel.setModified("2017-10-31T05:15:57.983Z");
        mModel.setImageString(imageString);
        mModel.setFileExtension(null);
        return new Gson().toJsonTree(mModel).getAsJsonObject();
    }


    private void getProvince() {
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        String BaseUrl = BuildConfig.LOCAL_URL + "api/Province/GetProvinces/null";
        mProvinceSubscription = service.getProvinance(BaseUrl)
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
                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(StatesModel statesModel) {
                        provinceModelList = statesModel;

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
                            postCode.setText("");

                            districtArrayAdapter = new ArrayAdapter(ProfileActivity1.this, android.R.layout.simple_spinner_item, districtList);
                            districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            districtSpin.setAdapter(districtArrayAdapter);

                            villageArrayAdapter = new ArrayAdapter(ProfileActivity1.this, android.R.layout.simple_spinner_item, villageList);
                            villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            villageSpin.setAdapter(villageArrayAdapter);
                        }


                        provinceArrayAdapter = new ArrayAdapter(ProfileActivity1.this, android.R.layout.simple_spinner_item, provinceList);
                        provinceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        provinceSpin.setAdapter(provinceArrayAdapter);

//                        if (provinceModelList != null && provinceModelList.getListResult().get(0).getId() != null && !provinceModelList.getListResult().get(0).getId().equals(""))
//                            for (int j = 0; j < provinceModelList.getListResult().size(); j++)
//                                if (statesModel.getListResult().get(j).getId().equals(provinceStr))
//                                    provinceSpin.setSelection(j + 1);


                        for (int j = 0; j < provinceModelList.getListResult().size(); j++)
                            if (provinceModelList.getListResult().get(j).getId().equals(provinceStr)) {
                                provinceSpin.setSelection(j + 1);
                                break;
                            }


                    }
                });
    }

    private void getDistricts(int id) {
        String BaseUrl = BuildConfig.LOCAL_URL + "api/Province/GetDistrictsByProvinceId/" + id;
       /* String URL = "http://192.168.1.147/PayZanAPI/api/Districts/GetDistrictsInfo/" + id;  *//*ApiConstants.DISTRICTS + "1"*/
        MyServices service = ServiceFactory.createRetrofitService(this, MyServices.class);
        mGetDistrictSubscription = service.getDistricts(BaseUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DistrictModel>() {
                    @Override
                    public void onCompleted() {
//                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DistrictModel districtModel) {
                        districtModelList = districtModel;
//                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_sucess), Toast.LENGTH_SHORT).show();

                        districtList = new ArrayList<>();


                        if (!districtModelList.getListResult().isEmpty()) {
                            districtList.add("--Select District--");
                            for (int i = 0; i < districtModelList.getListResult().size(); i++) {
                                districtList.add(districtModelList.getListResult().get(i).getName());
                            }
                        } else {
                            districtList.clear();
                            villageList.clear();
                            postCode.setText("");

                            villageArrayAdapter = new ArrayAdapter(ProfileActivity1.this, android.R.layout.simple_spinner_item, villageList);
                            villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            villageSpin.setAdapter(villageArrayAdapter);

                        }
                        districtArrayAdapter = new ArrayAdapter(ProfileActivity1.this, android.R.layout.simple_spinner_item, districtList);
                        districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        districtSpin.setAdapter(districtArrayAdapter);

                        if (districtVar){
                            for (int k = 0; k < districtModelList.getListResult().size(); k++)
                                if (districtModelList.getListResult().get(k).getId().equals(districtStr)) {
                                    districtSpin.setSelection(k + 1);
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
//                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(MandalModel mandalModel) {
                        mandalModelList = mandalModel;
//                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_sucess), Toast.LENGTH_SHORT).show();
                        mandalList = new ArrayList<>();
                        if (!mandalModelList.getListResult().isEmpty()) {
                            mandalList.add("--Select Mandal--");
                            for (int i = 0; i < mandalModelList.getListResult().size(); i++) {
                                mandalList.add(mandalModelList.getListResult().get(i).getName());
                            }
                        } else {
                            mandalList.clear();
                            villageList.clear();
                            postCode.setText("");

                            villageArrayAdapter = new ArrayAdapter(ProfileActivity1.this, android.R.layout.simple_spinner_item, villageList);
                            villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            villageSpin.setAdapter(villageArrayAdapter);

                        }


                        mandalArrayAdapter = new ArrayAdapter(ProfileActivity1.this, android.R.layout.simple_spinner_item, mandalList);
                        mandalArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        mandalSpin.setAdapter(mandalArrayAdapter);

                        if (mandalVar){
                            for (int j = 0; j < mandalModelList.getListResult().size(); j++)
                                if (mandalModelList.getListResult().get(j).getId().equals(mandalStr)) {
                                    mandalSpin.setSelection(j + 1);
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
        mVillageSubscription = service.getVillages(BaseUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VillageModel>() {
                    @Override
                    public void onCompleted() {
//                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_check), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity1.this, getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(VillageModel villageModel) {
                        villageModelList = villageModel;

//                        Toast.makeText(ProfileActivity1.this, R.string.toast_sucess, Toast.LENGTH_SHORT).show();
                        villageList = new ArrayList<>();
                        villageList.add("--Select Village--");
                        if (!villageModelList.getListResult().isEmpty()) {
                            for (int i = 0; i < villageModelList.getListResult().size(); i++) {
                                villageList.add(villageModelList.getListResult().get(i).getName());

                            }
                        } else {
                            villageList.clear();
                            postCode.setText("");
                        }


                        villageArrayAdapter = new ArrayAdapter(ProfileActivity1.this, android.R.layout.simple_spinner_item, villageList);
                        villageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        villageSpin.setAdapter(villageArrayAdapter);


                        if (villageVar){
                            for (int j = 0; j < villageModelList.getListResult().size(); j++)
                                if (villageModelList.getListResult().get(j).getId().equals(villageId)) {
                                    villageSpin.setSelection(j + 1);
                                    break;
                                }

                            villageVar = false;
                        }



                    }
                });
    }


}
