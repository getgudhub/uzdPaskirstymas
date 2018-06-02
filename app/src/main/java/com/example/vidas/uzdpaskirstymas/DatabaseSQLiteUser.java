package com.example.vidas.uzdpaskirstymas;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSQLiteUser extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION   = 2;
    private static final String DATABASE_NAME   = "db";

    private static final String adminName = "Admin101p";
    private static final int ADMIN_LEVEL        = 9;

    private static final String TABLE_USERS     = "users";
    private static final String USER_ID         = "userid";
    private static final String USER_LEVEL      = "userlevel";
    private static final String USER_NAME       = "name";
    private static final String USER_PASSWORD   = "password";
    private static final String USER_FULLNAME   = "fullname";
    private static final String USER_EMAIL      = "email";

    String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS
            + "("
            + USER_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_LEVEL        + " INTEGER,"
            + USER_NAME         + " TEXT,"
            + USER_PASSWORD     + " TEXT,"
            + USER_FULLNAME     + " TEXT,"
            + USER_EMAIL        + " TEXT"
            + ")";

    public DatabaseSQLiteUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        if(user.getUsernameForRegister().equals(adminName)){
            user.setUserlevel(9);
        }else {
            user.setUserlevel(1);
        }

        ContentValues values = new ContentValues();
        values.put(USER_LEVEL,      user.getUserlevel());
        values.put(USER_NAME,       user.getUsernameForRegister());
        values.put(USER_PASSWORD,   user.getPasswordForRegister());
        values.put(USER_FULLNAME,   user.getFullNameForRegister());
        values.put(USER_EMAIL,      user.getEmailForRegister());

        // Inserting Row
        db.execSQL(CREATE_USERS_TABLE);
        db.insert(TABLE_USERS, null, values);

        // Closing database connection
        db.close();
    }

    User getUser(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{
                        USER_ID,
                        USER_LEVEL,
                        USER_NAME,
                        USER_PASSWORD,
                        USER_FULLNAME,
                        USER_EMAIL
                },
                USER_NAME + "=?",
                new String[]{String.valueOf(name)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst() ){


            user = new User(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            cursor.close();
        }
        db.close();
        return user;

    }

    public int isAdmin(String username){
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE "
                        + USER_NAME + "='" + username + "' AND " + USER_LEVEL + " = " + ADMIN_LEVEL , null);
        if(c.getCount() >0){
            c.close();
            return ADMIN_LEVEL;
        }else {
            c.close();
            return 1;
        }

    }

    public boolean isValidUser(String username, String password){
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE "
                        + USER_NAME + "='" + username + "'AND " +
                        USER_PASSWORD + "='" + password + "'" , null);
        if (c.getCount() > 0){
            c.close();
            return true;
        }else {
            c.close();
            return false;
        }

    }

    public boolean checkName(String  name){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_USERS + " where " + USER_NAME + " = '" + name + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        }else{
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean checkFullName(String  name){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_USERS + " where " + USER_FULLNAME + " LIKE '" + name + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        }else{
            cursor.close();
            db.close();
            return false;
        }
    }

    public String getFullName(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select "+ USER_FULLNAME + " from " + TABLE_USERS + " where "+ USER_NAME + " = '"+ username + "' ";
        String fullName="0";
        Cursor c = db.rawQuery(Query, null);
        if (c != null && c.moveToFirst() ) {
            fullName = c.getString(0);
        }
        c.close();
        db.close();
        return fullName;
    }

    public ArrayList<String> getAllUsers() {
        ArrayList<String> users = new ArrayList<String>();
        String temp = "";
        users.add(temp);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_USERS_TABLE);

        // Select All Query
        String selectQuery = "SELECT " + USER_FULLNAME +" FROM " + TABLE_USERS;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                temp = cursor.getString(0);
                users.add(temp);
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return users;
    }

}
