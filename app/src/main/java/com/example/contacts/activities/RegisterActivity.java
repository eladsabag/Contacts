package com.example.contacts.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import com.example.contacts.R;
import com.example.contacts.sqlite.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText register_EDT_email, register_EDT_password, register_EDT_mobile;
    private MaterialButton register_BTN_signup;
    private MaterialTextView register_TXT_error;
    private boolean validEmail = false , validPassword = false, validMobile = false;

    private DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set BackgroundDrawable of Action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF2196F3")));

        findViews();

        initViews();

        myDB = new DatabaseHelper(RegisterActivity.this);
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
     * This function checks if the string match to password pattern - A valid password has 8-24 characters and/or numbers and/or symbols ! @ # $.
     * @param s - The string that the function check if he matches to the pattern.
     * @return true - if the string matches the pattern, else - false.
     */
    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");
        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }

    /**
     * This function checks if the string match to email pattern - A valid email need to have this structure 'name@mail.com'.
     * @param target - The char sequence that the function check if he matches to the pattern.
     * @return true - if the char sequence matches the pattern, else - false.
     */
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    /**
     * This function init all the views.
     */
    private void initViews() {
        register_BTN_signup.setEnabled(false);
        // Set the textchange listener for edittext
        register_EDT_email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is valid email
                if(!isValidEmail(register_EDT_email.getText().toString())){
                    // Something into edit text. Enable the button.
                    register_EDT_email.setError("Enter valid email address, for example 'name@mail.com' accepted.");
                    validEmail = false;
                } else {
                    register_EDT_email.setError(null);
                    validEmail = true;
                }
                setSignupButton();
            }
        });

        // Set the textchange listener for edittext
        register_EDT_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is valid password
                if(!isValidPassword(register_EDT_password.getText().toString())){
                    // Something into edit text. Enable the button.
                    register_EDT_password.setError("Enter a valid password. valid password size should be at least 8 and contain characters and/or numbers and/or symbols as $#@! only.");
                    validPassword = false;
                } else {
                    register_EDT_password.setError(null);
                    validPassword = true;
                }
                setSignupButton();
            }
        });

        // Set the textchange listener for edittext
        register_EDT_mobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is valid password
                if(!isValidMobileNumber(register_EDT_mobile.getText().toString())){
                    // Something into edit text. Enable the button.
                    register_EDT_mobile.setError("Enter a valid mobile number. Valid number size should be between 10 to 13 and contain only numbers or + in case of writing the country area code.");
                    validMobile = false;
                } else {
                    register_EDT_mobile.setError(null);
                    validMobile = true;
                }
                setSignupButton();
            }
        });

        register_BTN_signup.setOnClickListener(e -> {
            Cursor cursor = myDB.findUser(register_EDT_email.getText().toString());
            if(cursor.getCount() > 0) {
                register_TXT_error.setVisibility(View.VISIBLE);
            } else {
                myDB.addUser(register_EDT_email.getText().toString(),
                             register_EDT_password.getText().toString(),
                             register_EDT_mobile.getText().toString());
                Intent intent = new Intent(RegisterActivity.this, ContactsActivity.class);
                intent.putExtra("email", register_EDT_email.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * This function enable the sign in button if valid email,password and mobile is entered , else it disable the sign up button.
     */
    private void setSignupButton() {
        if(validEmail && validPassword && validMobile)
            register_BTN_signup.setEnabled(true);
        else
            register_BTN_signup.setEnabled(false);
    }

    /**
     * This function find all the views.
     */
    private void findViews() {
        register_EDT_email = findViewById(R.id.register_EDT_email);
        register_EDT_password = findViewById(R.id.register_EDT_password);
        register_EDT_mobile = findViewById(R.id.register_EDT_mobile);
        register_BTN_signup = findViewById(R.id.register_BTN_signup);
        register_TXT_error = findViewById(R.id.register_TXT_error);
    }
}