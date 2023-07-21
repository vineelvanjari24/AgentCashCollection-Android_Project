package com.example.agentcashcollection.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class CusDetailsDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="Customer.db";
    private static final int DB_VERSION=1;
    Context context;

    public CusDetailsDBHelper(Context context)
    {

        super(context,DB_NAME,null,DB_VERSION);
        this.context = context;
    }
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("Create Table Customer_Details (Account_Number INTEGER Primary key, Name TEXT , Due_Amount Number, Date DATE DEFAULT (DATE('NOW')), Phone_Number Integer Unique, Address Text)");
    }
    public void onUpgrade(SQLiteDatabase db , int OldVersion,int NewVersion)
    {

        db.execSQL("drop table if Exists Customer_Details");
        onCreate(db);
    }
    public Boolean insertRows(long Account_num, String Name, int Due, long phoneNumber, String address)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Values = new ContentValues();
        Values.put("Name",Name);
        Values.put("Account_Number",Account_num);
        Values.put("Due_Amount",Due);
        Values.put("Phone_Number", phoneNumber);
        Values.put("Address", address);
        long flag = db.insert( "Customer_Details", null, Values);
        if(flag == -1)
            return false;
        else
            return true;
    }

    public Boolean updateDueAmount(String accNumber, long prevDueAmount, long depAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Due_Amount", (prevDueAmount - depAmount));
        long flag = db.update("Customer_Details", values, "Account_Number = ?", new String[]{accNumber});
        if(flag == -1)
            return false;
        else
            return true;
    }

    public ArrayList<String> getIdentifyingNum() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorAccNumbers = db.rawQuery("Select Account_Number, Phone_Number From Customer_Details", null);

        ArrayList<String> numbers = new ArrayList<>();

        while (cursorAccNumbers.moveToNext()) {
            numbers.add(cursorAccNumbers.getString(0));
            numbers.add(cursorAccNumbers.getString(1));
        }

        cursorAccNumbers.close();

        return numbers;
    }

    public boolean checkAccountNumber(String identificationNum) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Customer_Details WHERE Account_Number = ? OR Phone_Number = ?", new String[]{identificationNum, identificationNum});
        if(cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }
    public ArrayList<String> getAccDetails(String acc_num)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Customer_Details WHERE Account_Number = ? OR Phone_Number = ?", new String[]{acc_num, acc_num});

        ArrayList<String> row= new ArrayList<>();
        if (cursor.moveToNext()) {
            row.add(""+cursor.getInt(0));
            row.add(cursor.getString(1));
            row.add(""+cursor.getLong(2));
            row.add(cursor.getString(3));
            row.add(""+cursor.getLong(4));
            row.add(cursor.getString(5));
        }
        cursor.close();
        return  row;
    }

    public StringBuffer getCustomerList(int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCusList = db.rawQuery("Select Account_Number, Name from Customer_Details Where Due_Amount > ?", new String[]{""+limit});

        StringBuffer stringBuffer = new StringBuffer();

        while (cursorCusList.moveToNext()) {
            stringBuffer.append(cursorCusList.getString(0)).append("        ");
            stringBuffer.append(cursorCusList.getString(1)).append("\n");
        }

        cursorCusList.close();

        return stringBuffer;
    }
}