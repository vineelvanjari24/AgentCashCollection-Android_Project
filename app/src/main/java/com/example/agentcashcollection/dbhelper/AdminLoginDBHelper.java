package com.example.agentcashcollection.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminLoginDBHelper extends SQLiteOpenHelper {

    private static final  String DATABSE_NAME = "AdminLoginDB.db";
    private static final String TABLE_NAME = "AdminUsers";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public AdminLoginDBHelper(Context context) {
        super(context, DATABSE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("+USERNAME+" TEXT PRIMARY KEY, "+PASSWORD+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        onCreate(db);
    }

    public Boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, username);
        contentValues.put(PASSWORD, password);
        long insertFlag = db.insert(TABLE_NAME, null, contentValues);
        if(insertFlag == -1)
            return false;
        else
            return true;
    }

    public Boolean usernameCheck(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+USERNAME+" = ?", new String[]{username});
        if(cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }

    public Boolean usernamePasswordCheck(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+USERNAME+" = ? and "+PASSWORD+" = ?", new String[]{username, password});
        if(cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }

}
