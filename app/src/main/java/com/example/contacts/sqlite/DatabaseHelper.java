package com.example.contacts.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "Contacts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME_1 = "users";
    private static final String COLUMN_ID_1 = "users_id";
    private static final String COLUMN_EMAIL = "users_email";
    private static final String COLUMN_PASSWORD = "users_password";
    private static final String COLUMN_MOBILE_1 = "users_mobile";

    private static final String TABLE_NAME_2 = "contacts";
    private static final String COLUMN_ID_2 = "contacts_id";
    private static final String COLUMN_FIRSTNAME = "contacts_firstName";
    private static final String COLUMN_LASTNAME = "contacts_lastName";
    private static final String COLUMN_MOBILE_2 = "contacts_mobile";
    private static final String COLUMN_USERID = "user_id";

    private String currentUserId="";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 =
                "CREATE TABLE " + TABLE_NAME_1 +
                        " (" + COLUMN_ID_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_EMAIL + " TEXT, " +
                        COLUMN_PASSWORD + " TEXT, " +
                        COLUMN_MOBILE_1 + " TEXT)";
        String query2 =
                "CREATE TABLE " + TABLE_NAME_2 +
                        " (" + COLUMN_ID_2 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_FIRSTNAME + " TEXT, " +
                        COLUMN_LASTNAME + " TEXT, " +
                        COLUMN_MOBILE_2 + " TEXT, " +
                        COLUMN_USERID + " INTEGER, " +
                        " FOREIGN KEY ("+COLUMN_USERID+") REFERENCES "+TABLE_NAME_1+"("+COLUMN_ID_1+"));";


        db.execSQL(query1);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        onCreate(db);
    }

    public boolean addUser(String email, String password, String mobileNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EMAIL,email);
        cv.put(COLUMN_PASSWORD,password);
        cv.put(COLUMN_MOBILE_1,mobileNumber);

        long result = db.insert(TABLE_NAME_1,null, cv);
        if(result == -1) {
            Toast.makeText(context,"Failed to Register.", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            Toast.makeText(context,"Registered Successfully!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void addContact(String firstName, String lastName, String mobileNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FIRSTNAME,firstName);
        cv.put(COLUMN_LASTNAME,lastName);
        cv.put(COLUMN_MOBILE_2,mobileNumber);
        cv.put(COLUMN_USERID, currentUserId);

        long result = db.insert(TABLE_NAME_2,null, cv);
        if(result == -1)
            Toast.makeText(context,"Failed to Add Contact.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"Added Successfully!", Toast.LENGTH_SHORT).show();
    }

    public Cursor findUser(String email) {
        String query = "SELECT * " +
                       " FROM " + TABLE_NAME_1 +
                       " WHERE " + COLUMN_EMAIL + " =?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
            cursor = db.rawQuery(query, new String[]{email});

        return cursor;
    }

    public Cursor readAllData(String email) {
        Cursor user = findUser(email);
        user.moveToNext();
        setCurrentUserId(user.getString(0).toString());
        String query = "SELECT * " +
                " FROM " + TABLE_NAME_2 +
                " WHERE " + COLUMN_USERID + " =?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
            cursor = db.rawQuery(query, new String[]{user.getString(0).toString()});

        return cursor;
    }

    public void editContactData(String contact_id, String firstName, String lastName, String mobileNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FIRSTNAME,firstName);
        cv.put(COLUMN_LASTNAME,lastName);
        cv.put(COLUMN_MOBILE_2,mobileNumber);

        long result = db.update(TABLE_NAME_2,cv,"contacts_id=?", new String[]{contact_id});
        if(result == -1) {
            Toast.makeText(context,"Failed to Edit.",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Edited Successfully!",Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteContactData(String contact_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME_2,"contacts_id=?",new String[]{contact_id});
        if(result == -1) {
            Toast.makeText(context,"Failed to Delete.",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Deleted Successfully!",Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllContactsData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_2 + " WHERE " + COLUMN_USERID + " =?",new String[]{getCurrentUserId()});
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public DatabaseHelper setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
        return this;
    }
}
