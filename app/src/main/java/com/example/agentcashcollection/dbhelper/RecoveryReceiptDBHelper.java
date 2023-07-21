package com.example.agentcashcollection.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class RecoveryReceiptDBHelper extends SQLiteOpenHelper {

    Context mainActivity;
    private final static String DATA_NAME = "CustomerReceipt.db";
    private final static String TABLE_NAME = "Receipt_DETAILS";

    public RecoveryReceiptDBHelper(Context context) {

        super(context, DATA_NAME, null, 1);
        mainActivity = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (\n" +
                "  Account_Number TEXT,\n" +
                "  receipt_no INTEGER PRIMARY KEY  AUTOINCREMENT,\n" +
                "  receipt_date DATE DEFAULT (DATE('NOW')),\n" +
                "  recovery_amount INTEGER,\n"+
                "  FOREIGN KEY (Account_Number) REFERENCES Customer_Details(Account_Number)\n" +
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
        contentValues.put("recovery_amount", recoveryAmount);

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
        Cursor cursorCollectionSum = db.rawQuery("SELECT Sum(recovery_amount) FROM " + TABLE_NAME + " WHERE receipt_date = ?", new String[]{date});
        Cursor cursorReceiptCount = db.rawQuery("SELECT recovery_amount FROM " + TABLE_NAME + " WHERE receipt_date = ?", new String[]{date});

        ArrayList<String> receiptRecord = new ArrayList<>();

        if (cursorCollectionSum.moveToFirst()) {
            receiptRecord.add(""+cursorReceiptCount.getCount());
            receiptRecord.add(cursorCollectionSum.getString(0));
        }

        cursorReceiptCount.close();
        cursorCollectionSum.close();

        return receiptRecord;
    }

    public ArrayList<String> getMonthlyCollectionDetails(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCollectionSum = db.rawQuery("SELECT Sum(recovery_amount) FROM " + TABLE_NAME + " WHERE strftime('%m/%Y', receipt_date) = ?", new String[]{date});
        Cursor cursorReceiptCount = db.rawQuery("SELECT recovery_amount FROM " + TABLE_NAME + " WHERE strftime('%m/%Y', receipt_date) = ?", new String[]{date});

        ArrayList<String> receiptRecord = new ArrayList<>();

        if (cursorCollectionSum.moveToFirst()) {
            receiptRecord.add(""+cursorReceiptCount.getCount());
            receiptRecord.add(cursorCollectionSum.getString(0));
        }

        cursorReceiptCount.close();
        cursorCollectionSum.close();

        return receiptRecord;
    }

    public ArrayList<String> getLastTransaction(long accNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorLastTran = db.rawQuery("Select receipt_date, recovery_amount from "+TABLE_NAME+" where Account_Number = ? and receipt_no = (Select Max(receipt_no) from "+TABLE_NAME+" where Account_Number = ?)", new String[]{String.valueOf(accNumber), String.valueOf(accNumber)});
        ArrayList<String> lastTransaction = new ArrayList<>();

        if(cursorLastTran.moveToFirst()) {
            lastTransaction.add(cursorLastTran.getString(0));
            lastTransaction.add(String.valueOf(cursorLastTran.getInt(1)));
        }

        cursorLastTran.close();

        return lastTransaction;
    }

    public ArrayList<String> getPaidCusList(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorPaidList = db.rawQuery("Select Account_Number, recovery_amount From "+TABLE_NAME+" Where receipt_date = ?", new String[]{date});
        Cursor cursorPaidSum = db.rawQuery("Select Sum(recovery_amount) From "+TABLE_NAME+" Where receipt_date = ?", new String[]{date});
        ArrayList<String> arrayListReturn = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("");
        while (cursorPaidList.moveToNext()) {
            stringBuffer.append(cursorPaidList.getString(0)).append("          ");
            stringBuffer.append(cursorPaidList.getString(1)).append("\n");
        }

        String totalPaidAmount = "";

        if (cursorPaidSum.moveToNext())
            totalPaidAmount = cursorPaidSum.getString(0);

        arrayListReturn.add(stringBuffer.toString());
        arrayListReturn.add(totalPaidAmount);

        cursorPaidList.close();
        cursorPaidList.close();
        return  arrayListReturn;
    }

}
