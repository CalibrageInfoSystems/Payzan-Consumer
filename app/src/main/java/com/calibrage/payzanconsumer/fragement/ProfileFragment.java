package com.calibrage.payzanconsumer.fragement;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.calibrage.payzanconsumer.framework.controls.BaseFragment;
import com.calibrage.payzanconsumer.framework.controls.CommonButton;
import com.calibrage.payzanconsumer.framework.controls.CommonEditText;
import com.calibrage.payzanconsumer.framework.controls.CommonTextView;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.NCBTextInputLayout;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;



public class  ProfileFragment extends BaseFragment {

    public static final String TAG = ProfileFragment.class.getSimpleName();
    private View view;
    private CommonTextView editimage, firstNametxt, lastNametxt, genderlayout, doblayout, usernamelayout;
    private CommonEditText firstNameEdt, lastNameEdt, userNameEdt, dobEdt;
    private RadioButton maleRB, femaleRB;
    private CommonButton submit;
    private CircularImageView profileimage;
    private NCBTextInputLayout inp_fname, inp_username, inp_laname, inp_dob;
    private String firstNameStr, lastNameStr, userNameStr, dobStr, maleStr, femaleStr, editimageStr;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    static Pattern pattern;
    static Matcher matcher;
    private Context context;

    final String NAME_PATTERN = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
    public static final int PICK_IMAGE = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
//        view.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        ((AppCompatActivity) getActivity()).setSupportActionBar(HomeActivity.toolbar);
         HomeActivity.toolbar.setTitle(getResources().getString(R.string.profile));
        HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        HomeActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeTab();
            }
        });

        setViews();
        initViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !CommonUtil.areAllPermissionsAllowed(getActivity(), CommonUtil.PERMISSIONS_REQUIRED)) {
            ActivityCompat.requestPermissions(getActivity(), CommonUtil.PERMISSIONS_REQUIRED, CommonUtil.PERMISSION_CODE);
        }


//        if (ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.CAMERA},
//                    MY_CAMERA_REQUEST_CODE);
//        }
        return view;
    }
    private void closeTab() {

        HomeActivity.toolbar.setNavigationIcon(null);
        HomeActivity.toolbar.setTitle("");
        replaceFragment(getActivity(), MAIN_CONTAINER, new UserProfileHome(), TAG, ProfileFragment.TAG);

    }

    private void setViews() {
        profileimage = (CircularImageView) view.findViewById(R.id.profileimage);
        editimage = (CommonTextView) view.findViewById(R.id.editimage);
        firstNameEdt = (CommonEditText) view.findViewById(R.id.firstNameEdt);
        lastNameEdt = (CommonEditText) view.findViewById(R.id.lastNameEdt);
        userNameEdt = (CommonEditText) view.findViewById(R.id.userNameEdt);
        dobEdt = (CommonEditText) view.findViewById(R.id.dobEdt);
        maleRB = (RadioButton) view.findViewById(R.id.maleRB);
        femaleRB = (RadioButton) view.findViewById(R.id.femaleRB);
        submit = (CommonButton) view.findViewById(R.id.submit);
        inp_fname = (NCBTextInputLayout) view.findViewById(R.id.inp_fname);
        inp_laname = (NCBTextInputLayout) view.findViewById(R.id.inp_laname);
        inp_username = (NCBTextInputLayout) view.findViewById(R.id.inp_username);
        inp_dob = (NCBTextInputLayout) view.findViewById(R.id.inp_dob);


        dobEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , onDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateset:dd/mm/yyy:" + day + "/" + month + "/" + year);
                String date = day + "/" + month + "/" + year;
                dobEdt.setText(date);

            }
        };
    }


    private void initViews() {
        editimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
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
        maleRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        femaleRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUI();
            }
        });
    }


    private boolean validateUI() {
        firstNameStr = firstNameEdt.getText().toString().trim();
        lastNameStr = lastNameEdt.getText().toString().trim();
        userNameStr = userNameEdt.getText().toString().trim();
        dobStr = dobEdt.getText().toString().trim();

        if (TextUtils.isEmpty(firstNameStr)) {
            inp_fname.setError(getString(R.string.err_enter_first_name));
            inp_fname.setErrorEnabled(true);
            return false;
        } else if (!fnameValidate()) {
            inp_fname.setErrorEnabled(true);
            inp_fname.setError(getString(R.string.err_enter_valid_firstname));
            return false;
        } else if (TextUtils.isEmpty(lastNameStr)) {
            inp_laname.setErrorEnabled(true);
            inp_laname.setError(getString(R.string.err_enter_last_name));
            return false;
        } else if (!lnameValidate()) {
            inp_laname.setErrorEnabled(true);
            inp_laname.setError(getString(R.string.err_enter_valid_lastname));
            return false;
        } else if (TextUtils.isEmpty(userNameStr)) {
            inp_username.setError(getString(R.string.err_enter_username));
            inp_username.setErrorEnabled(true);
            return false;
        } else if (TextUtils.isEmpty(dobStr)) {
            inp_dob.setErrorEnabled(true);
            inp_dob.setError(getString(R.string.err_enter_dob));
            return false;
        }

        return true;
    }


    public boolean fnameValidate() {

        String name = firstNameEdt.getText().toString().trim();

        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(name);

        if (matcher.matches() == false)

            Toast.makeText(getContext(), getString(R.string.toast_allowed_special), Toast.LENGTH_SHORT).show();
        return matcher.matches();

    }

    public boolean lnameValidate() {

        String name = lastNameEdt.getText().toString().trim();

        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(name);

        if (matcher.matches() == false)

            Toast.makeText(getContext(), getString(R.string.toast_allowed_special), Toast.LENGTH_SHORT).show();
        return matcher.matches();

    }


    public void openDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                // Get the path from the Uri
                String path = getPathFromURI(selectedImageUri);
                // Set the image in ImageView
                profileimage.setImageURI(selectedImageUri);
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileimage.setImageBitmap(imageBitmap);
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
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

                Toast.makeText(getActivity(), getString(R.string.toast_camera), Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getActivity(), R.string.toast_camera_denied, Toast.LENGTH_LONG).show();

            }

        }



    }
}
