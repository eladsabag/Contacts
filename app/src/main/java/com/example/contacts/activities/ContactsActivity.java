package com.example.contacts.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.contacts.utils.ContactAdapter;
import com.example.contacts.R;
import com.example.contacts.objects.Contact;
import com.example.contacts.sqlite.DatabaseHelper;
import com.example.contacts.utils.MSP;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ContactsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton contacts_BTN_add;
    private ImageView contacts_IMG_empty;
    private MaterialTextView contacts_TXT_nodata;
    private DatabaseHelper myDB;
    private ArrayList<Contact> allContacts;
    private ContactAdapter contactAdapter;

    private String email;
    private boolean isPauseAtAddActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // Set BackgroundDrawable of Action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF2196F3")));

        email = getIntent().getExtras().getString("email","");

        findViews();

        initViews();

        initAdapterAndDB();
    }

    /**
     * This function create a confirm dialog where the user can select YES to delete all contacts or NO to undelete them.
     */
    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Contacts ?");
        builder.setMessage("Are you sure you want to delete all contacts ?");
        builder.setIcon(R.drawable.ic_error);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDB.deleteAllContactsData();
                // Call function in order to refresh activity data
                initAdapterAndDB();
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
     * This function init the database and adapter variable and calls another function to store all of the database content in contacts array.
     */
    private void initAdapterAndDB() {
        myDB = new DatabaseHelper(ContactsActivity.this);
        allContacts = new ArrayList<>();

        storeAllDataInArray();

        contactAdapter = new ContactAdapter(ContactsActivity.this,this,allContacts);
        recyclerView.setAdapter(contactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ContactsActivity.this));
    }

    /**
     * This function store all of the database data in contacts array.
     */
    private void storeAllDataInArray() {
        Cursor cursor = myDB.readAllData(email);
        if(cursor.getCount() == 0) {
            contacts_IMG_empty.setVisibility(View.VISIBLE);
            contacts_TXT_nodata.setVisibility(View.VISIBLE);
        } else {
            contacts_IMG_empty.setVisibility(View.INVISIBLE);
            contacts_TXT_nodata.setVisibility(View.INVISIBLE);
            while(cursor.moveToNext()) {
                allContacts.add(new Contact()
                        .set_id(cursor.getString(0))
                        .setFirstName(cursor.getString(1))
                        .setLastName(cursor.getString(2))
                        .setMobileNumber(cursor.getString(3)));
            }
            Collections.sort(allContacts, new Comparator<Contact>() {
                @Override
                public int compare(Contact c1, Contact c2) {
                    String fullName1 = c1.getFirstName() + " " + c1.getLastName();
                    String fullName2 = c2.getFirstName() + " " + c2.getLastName();

                    return fullName1.compareTo(fullName2);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            // Call function in order to refresh activity data
            initAdapterAndDB();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_all) {
            if(contactAdapter.getItemCount() == 0) {
                Toast.makeText(this,"No data to delete.",Toast.LENGTH_SHORT).show();
            } else {
                confirmDialog();
            }
        } else if(item.getItemId() == R.id.logout) {
            resetSaveUserOnSharedPreferences();
            startActivity(new Intent(ContactsActivity.this,MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetSaveUserOnSharedPreferences() {
        MSP.getMe(this).putBooleanToSP("isLoggedIn",false);
        MSP.getMe(this).putStringToSP("email","");
    }

    /**
     * This function init all views.
     */
    private void initViews() {
        contacts_BTN_add.setOnClickListener(e -> {
            Intent intent = new Intent(ContactsActivity.this, AddActivity.class);
            intent.putExtra("email",email);
            isPauseAtAddActivity = true;
            startActivity(intent);
        });
    }

    /**
     * This function find all views.
     */
    private void findViews() {
        recyclerView = findViewById(R.id.recyclerView);
        contacts_BTN_add = findViewById(R.id.contacts_BTN_add);
        contacts_IMG_empty = findViewById(R.id.contacts_IMG_empty);
        contacts_TXT_nodata = findViewById(R.id.contacts_TXT_nodata);
    }

    /**
     * This function saves the logged in user.
     */
    private void saveUserToSharedPreferences() {
        MSP.getMe(this).putBooleanToSP("isLoggedIn",true);
        MSP.getMe(this).putStringToSP("email",email);
    }

    // -------------------- Life Cycle -------------------- //

    @Override
    protected void onResume() {
        super.onResume();
        if(isPauseAtAddActivity) {
            isPauseAtAddActivity = false;
            // Call function in order to refresh activity data
            initAdapterAndDB();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveUserToSharedPreferences();
    }
}