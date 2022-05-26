package com.example.contacts.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.contacts.R;
import com.example.contacts.retrofit.Result;
import com.example.contacts.retrofit.RetrofitClient;
import com.example.contacts.sqlite.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import java.util.regex.Pattern;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {
    private EditText edit_EDT_firstname, edit_EDT_lastname, edit_EDT_mobile;
    private MaterialButton edit_BTN_edit, edit_BTN_gender,edit_BTN_delete;

    private boolean validMobile = true;

    private String contact_id,firstName, lastName, mobileNumber, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        setActionBarSettings();

        findViews();

        initViews();

    }

    /**
     * This function sets the action bar visibility and background color.
     */
    private void setActionBarSettings() {
        ActionBar actionBar = getSupportActionBar();
        // Set BackgroundDrawable of Action bar
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF2196F3")));
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function create a confirm dialog where the user can select YES to delete contact or NO to undelete contact.
     */
    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + firstName + " " + lastName + " ?");
        builder.setMessage("Are you sure you want to delete " + firstName + " " + lastName + " ?");
        builder.setIcon(R.drawable.ic_error);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper myDB = new DatabaseHelper(UpdateActivity.this);
                myDB.deleteContactData(contact_id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // nothing happens
            }
        });
        builder.create().show();
    }


    /**
     * This function checks if string matches to mobile number pattern - A valid mobile number has 10-13 numbers and can contain + at start only.
     * @param s - The string that the function check if he matches to the pattern.
     * @return true - if the string matches the pattern, else - false.
     */
    public static boolean isValidMobileNumber(String s) {
        Pattern MOBILE_PATTERN
                = Pattern.compile(
                "^[+]?[0-9]{10,13}$");
        return !TextUtils.isEmpty(s) && MOBILE_PATTERN.matcher(s).matches();
    }

    /**
     * This function init all views.
     */
    private void initViews() {
        getIntentAndSetData();
        edit_EDT_mobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is valid email
                if(!isValidMobileNumber(edit_EDT_mobile.getText().toString())){
                    // Something into edit text. Enable the button.
                    edit_EDT_mobile.setError("Please enter valid mobile number - 10 to 13 numbers and can contain + at start.");
                    validMobile = false;
                } else {
                    edit_EDT_mobile.setError(null);
                    validMobile = true;
                }
            }
        });
        edit_BTN_edit.setOnClickListener(e -> {
            if(validMobile) {
                DatabaseHelper myDB = new DatabaseHelper(UpdateActivity.this);
                myDB.editContactData(contact_id,edit_EDT_firstname.getText().toString(),edit_EDT_lastname.getText().toString(),edit_EDT_mobile.getText().toString());
            } else {
                Toast.makeText(this,"Mobile number is required.",Toast.LENGTH_SHORT).show();
            }
        });
        edit_BTN_delete.setOnClickListener(e -> {
            confirmDialog();
        });
    }

    /**
     * This function find all views.
     */
    private void findViews() {
        edit_EDT_firstname = findViewById(R.id.edit_EDT_firstname);
        edit_EDT_lastname = findViewById(R.id.edit_EDT_lastname);
        edit_EDT_mobile = findViewById(R.id.edit_EDT_mobile);
        edit_BTN_gender = findViewById(R.id.edit_BTN_gender);
        edit_BTN_edit = findViewById(R.id.edit_BTN_edit);
        edit_BTN_delete = findViewById(R.id.edit_BTN_delete);
    }

    /**
     * This function check if there is intent extras and sets variables data with that content.
     */
    private void getIntentAndSetData() {
        if(getIntent().hasExtra("firstName") && getIntent().hasExtra("lastName") && getIntent().hasExtra("mobileNumber")) {
            // Getting data from intent
            contact_id = getIntent().getExtras().getString("contact_id","");
            firstName = getIntent().getExtras().getString("firstName","");
            lastName = getIntent().getExtras().getString("lastName","");
            mobileNumber = getIntent().getExtras().getString("mobileNumber","");

            if(!isValidName(firstName)) {
                String s = firstName.split(" ")[0];
                if(!isValidName(s)) {
                    edit_BTN_gender.setIconResource(R.drawable.ic_unknown);
                } else {
                    setContactGender(firstName);
                }
            } else {
                setContactGender(firstName);
            }

            // Setting intent data
            edit_EDT_firstname.setText(firstName);
            edit_EDT_lastname.setText(lastName);
            edit_EDT_mobile.setText(mobileNumber);

        } else {
            Toast.makeText(this,"No data.", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValidName(String s) {
        Pattern NAME_PATTERN =
                Pattern.compile(
                    "^[A-Za-z]+$"
                );
        return !TextUtils.isEmpty(s) && NAME_PATTERN.matcher(s).matches();
    }

    /**
     * This function make GET Request to the genderize api and sets the contact gender.
     * @param firstName - The name that need to be written in the GET Request url.
     */
    private void setContactGender(String firstName) {
        edit_BTN_gender.setVisibility(View.INVISIBLE);
        Call<Result> call = RetrofitClient.getInstance().getMyApi().getGender("?name="+firstName);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result myGender = response.body();
                gender = myGender.getGender();
                edit_BTN_gender.setText(gender);

                if(gender == null)
                    edit_BTN_gender.setIconResource(R.drawable.ic_unknown);
                else if(gender.equalsIgnoreCase("Male"))
                    edit_BTN_gender.setIconResource(R.drawable.ic_male);
                else if(gender.equalsIgnoreCase("Female"))
                    edit_BTN_gender.setIconResource(R.drawable.ic_female);
                else
                    edit_BTN_gender.setIconResource(R.drawable.ic_unknown);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                edit_BTN_gender.setIconResource(R.drawable.ic_unknown);
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }
        });
        edit_BTN_gender.setVisibility(View.VISIBLE);
    }
}