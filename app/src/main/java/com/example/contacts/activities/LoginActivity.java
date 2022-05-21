package com.example.contacts.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.contacts.R;
import com.example.contacts.sqlite.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText login_EDT_email, login_EDT_password;
    private MaterialButton login_BTN_signin;
    private MaterialTextView login_TXT_create, login_TXT_error;
    private boolean validEmail = false , validPassword = false;

    private DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set BackgroundDrawable of Action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF2196F3")));

        findViews();

        initViews();

        myDB = new DatabaseHelper(LoginActivity.this);
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
        login_BTN_signin.setEnabled(false);
        // Set the textchange listener for edittext
        login_EDT_email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is valid email
                if(!isValidEmail(login_EDT_email.getText().toString())){
                    // Something into edit text. Enable the button.
                    login_EDT_email.setError("Enter valid email address, for example 'name@mail.com' accepted.");
                    validEmail = false;
                } else {
                    login_EDT_email.setError(null);
                    validEmail = true;
                }
                setSigninButton();
            }
        });

        // Set the textchange listener for edittext
        login_EDT_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is valid password
                if(!isValidPassword(login_EDT_password.getText().toString())){
                    // Something into edit text. Enable the button.
                    login_EDT_password.setError("Enter a valid password. valid password size should be at least 8 and contain characters and/or numbers and/or symbols as $#@! only.");
                    validPassword = false;
                } else {
                    login_EDT_password.setError(null);
                    validPassword = true;
                }
                setSigninButton();
            }
        });

        login_BTN_signin.setOnClickListener(e -> {
            Cursor cursor = myDB.findUser(login_EDT_email.getText().toString());
            cursor.moveToNext();
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
                login_TXT_error.setVisibility(View.VISIBLE);
            } else if(!cursor.getString(2).equals(login_EDT_password.getText().toString())) {
                Toast.makeText(this, "Wrong password.", Toast.LENGTH_SHORT).show();
                login_TXT_error.setVisibility(View.VISIBLE);
            } else {
                Intent intent = new Intent(LoginActivity.this, ContactsActivity.class);
                intent.putExtra("email", login_EDT_email.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        SpannableString ss = new SpannableString(login_TXT_create.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 23, login_TXT_create.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        login_TXT_create.setText(ss);
        login_TXT_create.setMovementMethod(LinkMovementMethod.getInstance());
        login_TXT_create.setHighlightColor(Color.TRANSPARENT);
    }

    /**
     * This function enable the sign in button if valid email,password and mobile is entered , else it disable the sign in button.
     */
    private void setSigninButton() {
        if(validEmail && validPassword)
            login_BTN_signin.setEnabled(true);
        else
            login_BTN_signin.setEnabled(false);
    }

    /**
     * This function find all the views.
     */
    private void findViews() {
        login_EDT_email = findViewById(R.id.login_EDT_email);
        login_EDT_password = findViewById(R.id.login_EDT_password);
        login_BTN_signin = findViewById(R.id.login_BTN_signin);
        login_TXT_create = findViewById(R.id.login_TXT_create);
        login_TXT_error = findViewById(R.id.login_TXT_error);
    }
}