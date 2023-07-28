package com.example.agentcashcollection.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SecurityDBHelper extends SQLiteOpenHelper {
    private static final  String DATABASE_NAME = "Security.db";
    private static final String TABLE_NAME = "Verification";
    private static final String UUID = "UUID";
    private static final String DEVICE_ID = "Device_id";

    public SecurityDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("+UUID+" TEXT PRIMARY KEY, "+DEVICE_ID+" INTEGER UNIQUE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        onCreate(db);
    }
    public  void insert1(String uuid,int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UUID",uuid);
        cv.put("Device_id",id);
        db.insert(TABLE_NAME,null,cv);
    }

}