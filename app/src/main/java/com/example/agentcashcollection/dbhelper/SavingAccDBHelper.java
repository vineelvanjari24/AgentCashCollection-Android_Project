package com.example.agentcashcollection.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SavingAccDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="SavingAcc.db";
    private static final int DB_VERSION=1;

    public SavingAccDBHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("Create Table Saving_Acc_Details (Account_Number INTEGER Primary key, Name TEXT ,Balance_Amount INTEGER, Phone_Number Integer Unique, Address Text)");
    }
    public void onUpgrade(SQLiteDatabase db , int OldVersion,int NewVersion)
    {

        db.execSQL("drop table if Exists Saving_Acc_Details");
        onCreate(db);
    }
    public Boolean insertRows(long Account_num, String Name, long balanceAmount, long phoneNumber, String address)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Values = new ContentValues();
        Values.put("Name",Name);
        Values.put("Account_Number",Account_num);
        Values.put("Balance_Amount",balanceAmount);
        Values.put("Phone_Number", phoneNumber);
        Values.put("Address", address);
        long flag = db.insert( "Saving_Acc_Details", null, Values);
        if(flag == -1)
            return false;
        else
            return true;
    }

    public Boolean updateBalanceAmount(String accNumber, long prevBalance, long depAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Balance_Amount", (prevBalance + depAmount));
        long flag = db.update("Saving_Acc_Details", values, "Account_Number = ?", new String[]{accNumber});
        if(flag == -1)
            return false;
        else
            return true;
    }

    public ArrayList<String> getIdentifyingNum() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorAccNumbers = db.rawQuery("Select Account_Number, Phone_Number From Saving_Acc_Details", null);

        ArrayList<String> numbers = new ArrayList<>();

        while (cursorAccNumbers.moveToNext()) {
            numbers.add(cursorAccNumbers.getString(0));
            numbers.add(cursorAccNumbers.getString(1));
        }

        cursorAccNumbers.close();

        return numbers;
    }

    public Boolean checkAccountNumber(String accountNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Saving_Acc_Details WHERE Account_Number = ? OR Phone_Number = ?", new String[]{accountNumber, accountNumber});
        if(cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }
    public ArrayList<String> getAccDetails(String  acc_num)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Saving_Acc_Details WHERE Account_Number = ? OR Phone_Number = ?", new String[]{acc_num, acc_num});

        ArrayList<String> row= new ArrayList<>();
        if (cursor.moveToNext()) {
            row.add(""+cursor.getInt(0));
            row.add(cursor.getString(1));
            row.add(""+cursor.getInt(2));
            row.add(""+cursor.getLong(3));
            row.add(cursor.getString(4));
        }
        cursor.close();
        return  row;
    }

    public StringBuffer getCustomerList(long limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCusList = db.rawQuery("Select Account_Number, Name, Balance_Amount from Saving_Acc_Details Where Balance_Amount > ?", new String[]{""+limit});

        StringBuffer stringBuffer = new StringBuffer();

        while (cursorCusList.moveToNext()) {
            stringBuffer.append(cursorCusList.getString(0)).append("        ");
            stringBuffer.append(cursorCusList.getString(1).substring(0,4)).append("      ");
            stringBuffer.append(cursorCusList.getString(2)).append("\n");
        }

        cursorCusList.close();

        return stringBuffer;
    }

}