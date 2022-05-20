package com.example.contacts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.contacts.R;
import com.example.contacts.utils.MSP;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private MaterialButton main_BTN_start;
    private String email;
    private boolean isLoggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readSavedUserFromSharedPreferences();

        if(isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        } else {
            // Set BackgroundDrawable of Action bar
            Objects.requireNonNull(getSupportActionBar()).hide();

            findViews();

            initViews();
        }
    }

    /**
     * This function read the logged in user.
     */
    private void readSavedUserFromSharedPreferences() {
        isLoggedIn = MSP.getMe(this).getBooleanFromSP("isLoggedIn",false);
        email = MSP.getMe(this).getStringFromSP("email","");
    }

    /**
     * This function init all the views.
     */
    private void initViews() { main_BTN_start.setOnClickListener(e -> startActivity(new Intent(MainActivity.this, LoginActivity.class))); }

    /**
     * This function find all the views.
     */
    private void findViews() {
        main_BTN_start = findViewById(R.id.main_BTN_start);
    }
}