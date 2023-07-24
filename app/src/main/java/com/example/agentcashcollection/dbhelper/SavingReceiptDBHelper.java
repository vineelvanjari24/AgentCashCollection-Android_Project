package com.example.agentcashcollection.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SavingReceiptDBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "SavingReceipt.db";
    private final static String TABLE_NAME = "Saving_Receipt_Details";

    public SavingReceiptDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (\n" +
                "  Account_Number TEXT,\n" +
                "  receipt_no INTEGER PRIMARY KEY  AUTOINCREMENT,\n" +
                "  receipt_date DATE DEFAULT (DATE('NOW')),\n" +
                "  deposit_amount INTEGER,\n"+
                "  FOREIGN KEY (Account_Number) REFERENCES Saving_Acc_Details(Account_Number)\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public Boolean insertReceiptRecord(long accNumber, long recoveryAmount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Account_Number", accNumber);
        contentValues.put("deposit_amount", recoveryAmount);

        long flag = db.insert(TABLE_NAME, null, contentValues);

        if(flag == -1) return false;
        else return true;
    }

    public long getReceiptNo(String date, String accNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select Max(receipt_no) From "+TABLE_NAME+" Where Account_Number = ? AND receipt_date = ?", new String[]{accNumber, date});
        long receiptNo = 0;
        if(cursor.moveToFirst()) {
            receiptNo = cursor.getInt(0);
        }
        cursor.close();
        return receiptNo;
    }

    public ArrayList<String> getReceiptRecordCount_Collection(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorDepositSum = db.rawQuery("Select Sum(deposit_amount) From "+TABLE_NAME+" Where receipt_date = ?", new String[]{date});
        Cursor cursorReceiptCount = db.rawQuery("Select * From "+TABLE_NAME+" Where receipt_date = ?", new String[]{date});

        ArrayList<String> receiptRecord = new ArrayList<>();

        if(cursorDepositSum.moveToFirst() && cursorReceiptCount.getCount()>0) {
            receiptRecord.add(cursorReceiptCount.getCount()+"");
            receiptRecord.add(cursorDepositSum.getString(0));
        }

        cursorReceiptCount.close();
        cursorDepositSum.close();

        return receiptRecord;
    }

    public Cursor getCursorReceiptRecord(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("Select Account_Number, receipt_no, deposit_amount From "+TABLE_NAME+" Where receipt_date = ?", new String[]{date});
    }

    public ArrayList<String> getLastTransaction(long accNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorLastTran = db.rawQuery("Select receipt_date, deposit_amount from "+TABLE_NAME+" where Account_Number = ? and receipt_no = (Select Max(receipt_no) from "+TABLE_NAME+" where Account_Number = ?)", new String[]{String.valueOf(accNumber), String.valueOf(accNumber)});
        ArrayList<String> lastTransaction = new ArrayList<>();

        if(cursorLastTran.moveToFirst()) {
            lastTransaction.add(cursorLastTran.getString(0));
            lastTransaction.add(cursorLastTran.getString(1));
        }

        cursorLastTran.close();

        return lastTransaction;
    }

    public ArrayList<String> getMonthlyDepAmtDetails(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorDepAmtSum = db.rawQuery("SELECT Sum(deposit_amount) FROM " + TABLE_NAME + " WHERE strftime('%m/%Y', receipt_date) = ?", new String[]{date});
        Cursor cursorReceiptCount = db.rawQuery("SELECT deposit_amount FROM " + TABLE_NAME + " WHERE strftime('%m/%Y', receipt_date) = ?", new String[]{date});

        ArrayList<String> receiptRecord = new ArrayList<>();

        if (cursorDepAmtSum.moveToFirst()) {
            receiptRecord.add(""+cursorReceiptCount.getCount());
            receiptRecord.add(cursorDepAmtSum.getString(0));
        }

        cursorReceiptCount.close();
        cursorDepAmtSum.close();

        return receiptRecord;
    }
}
