package com.example.contacts.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contacts.R;
import com.example.contacts.sqlite.DatabaseHelper;
import com.google.android.material.button.MaterialButton;

import java.util.regex.Pattern;

public class AddActivity extends AppCompatActivity {
    private EditText add_EDT_firstname, add_EDT_lastname, add_EDT_mobile;
    private MaterialButton add_BTN_add;
    private String email;
    private boolean validFirstName = false, validLastName = false, validMobile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        email = getIntent().getExtras().getString("email","");

        setActionBarSettings();

        findViews();

        initViews();
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
     * This function set the action bar visibility and background color.
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
     * This function init all the views.
     */
    private void initViews() {
        add_EDT_mobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is valid email
                if(!isValidMobileNumber(add_EDT_mobile.getText().toString())){
                    // Something into edit text. Enable the button.
                    add_EDT_mobile.setError("Please enter valid mobile number - 10 to 13 numbers and can contain + at start.");
                    validMobile = false;
                } else {
                    add_EDT_mobile.setError(null);
                    validMobile = true;
                }
            }
        });
        add_BTN_add.setOnClickListener(e -> {
            if(validMobile) {
                DatabaseHelper myDB = new DatabaseHelper(AddActivity.this);
                Cursor user = myDB.findUser(email);
                user.moveToNext();
                myDB.setCurrentUserId(user.getString(0));
                myDB.addContact(add_EDT_firstname.getText().toString(), add_EDT_lastname.getText().toString(), add_EDT_mobile.getText().toString());

                // Clean and reset content
                add_EDT_firstname.setText("");
                add_EDT_lastname.setText("");
                add_EDT_mobile.setText("");
                add_EDT_mobile.setError(null);
            } else {
                Toast.makeText(this,"Mobile number is required.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This function find all the views.
     */
    private void findViews() {
        add_EDT_firstname = findViewById(R.id.add_EDT_firstname);
        add_EDT_lastname = findViewById(R.id.add_EDT_lastname);
        add_EDT_mobile = findViewById(R.id.add_EDT_mobile);
        add_BTN_add = findViewById(R.id.add_BTN_add);
    }
}