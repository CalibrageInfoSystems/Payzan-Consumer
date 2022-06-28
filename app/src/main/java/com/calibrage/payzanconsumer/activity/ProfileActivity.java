package com.calibrage.payzanconsumer.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Calendar;

import static com.calibrage.payzanconsumer.framework.util.CommonUtil.isValidEmail;


public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText firstNameEdt, lastNameEdt, displayNameEdt, emailEdt, mobileEdt, DOBEdt;
    private ImageView firstclearBtn, lastClearBtn;
    private CircularImageView profileimage;
    private TextView editimage;
    public static final int PICK_IMAGE = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String firstNameStr,lastNameStr,displayNameStr,emailStr,mobileStr;
    private int mYear, mMonth, mDay;
    private RadioGroup gendergroup;
    private RadioButton maleRB,femaleRB;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
//        alertDialog = alertDialogBuilder.create();
        setViews();
        initViews();

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

    private boolean validateUi() {
        firstNameStr = firstNameEdt.getText().toString();
        lastNameStr=lastNameEdt.getText().toString();
        displayNameStr = displayNameEdt.getText().toString();
        emailStr=emailEdt.getText().toString();
        mobileStr = mobileEdt.getText().toString();
        if (TextUtils.isEmpty(firstNameStr)) {
            CommonUtil.displayDialogWindow("Please enter first name",alertDialog,ProfileActivity.this);
            return false;
        }else if(TextUtils.isEmpty(lastNameStr)){
            CommonUtil.displayDialogWindow("Please enter last name",alertDialog,ProfileActivity.this);
            return false;
        }else if(TextUtils.isEmpty(displayNameStr)){
            CommonUtil.displayDialogWindow("Please enter display name",alertDialog,ProfileActivity.this);
            return false;
        }else if(TextUtils.isEmpty(emailStr)){
            CommonUtil.displayDialogWindow("Please enter email ",alertDialog,ProfileActivity.this);
            return false;
        }else if (!TextUtils.isEmpty(emailStr) && !isValidEmail(emailStr)) {
            CommonUtil.displayDialogWindow("Please enter valid email ",alertDialog,ProfileActivity.this);
            return false;
        }
        else if(TextUtils.isEmpty(mobileStr)){
            CommonUtil.displayDialogWindow("Please enter mobile no.",alertDialog,ProfileActivity.this);
            return false;
        } else if(!TextUtils.isEmpty(mobileStr)&& (mobileStr.length()>14||mobileStr.length()<10)){
            CommonUtil.displayDialogWindow("Please enter valid mobile no.",alertDialog,ProfileActivity.this);
            return false;
        }
        return true;
    }




    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
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



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void initViews() {
        firstclearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstNameEdt.setText("");

            }
        });
        lastClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastNameEdt.setText("");
            }
        });
        displayNameEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                firstclearBtn.setVisibility(View.GONE);
                lastClearBtn.setVisibility(View.GONE);
                return false;
            }
        });
        firstNameEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                firstclearBtn.setVisibility(View.VISIBLE);
                lastClearBtn.setVisibility(View.GONE);
                return false;
            }
        });
        lastNameEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                lastClearBtn.setVisibility(View.VISIBLE);
                firstclearBtn.setVisibility(View.GONE);
                return false;
            }
        });

        editimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        DOBEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();

            }
        });

    maleRB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            maleRB.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.black));
            femaleRB.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.list_bg_pressed));
        }
    });
        femaleRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                femaleRB.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.black));
                maleRB.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.list_bg_pressed));
            }
        });



    }

    private void openDatePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        DOBEdt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

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

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


    private void setViews() {
        firstNameEdt = (EditText) findViewById(R.id.firstNameEdt);
        lastNameEdt = (EditText) findViewById(R.id.lastNameEdt);
        displayNameEdt = (EditText) findViewById(R.id.displayNameEdt);
        emailEdt = (EditText) findViewById(R.id.emailEdt);
        mobileEdt = (EditText) findViewById(R.id.mobileEdt);
        DOBEdt = (EditText) findViewById(R.id.DOBEdt);
        firstclearBtn = (ImageView) findViewById(R.id.firstclearBtn);
        lastClearBtn = (ImageView) findViewById(R.id.lastClearBtn);
        profileimage = (CircularImageView) findViewById(R.id.profileimage);
        editimage = (TextView) findViewById(R.id.editimage);
        gendergroup = (RadioGroup) findViewById(R.id.gendergroup);
        femaleRB = (RadioButton) findViewById(R.id.femaleRB);
        maleRB = (RadioButton) findViewById(R.id.maleRB);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        } else if (item.getItemId() == R.id.menu_save) {
            if(validateUi()){

            }
        }
        return super.onOptionsItemSelected(item);
    }
}

