package com.example.agentcashcollection.agent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpos.jniinterface.PrinterInterface;
import com.example.agentcashcollection.R;
import com.example.agentcashcollection.dbhelper.RecoveryReceiptDBHelper;
import com.example.agentcashcollection.dbhelper.SavingReceiptDBHelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ActivityMonthlySavingSum extends AppCompatActivity {

    Context context;
    TextView selectedMonth, totalReceipts, totalDepAmount;
    EditText editTextMonth;
    Button getSummary, printSummary;
    ImageView goBack;
    Boolean flag = false;
    ArrayList<String> receiptData;
    String selectedDate;
    SavingReceiptDBHelper savingReceiptDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_saving_sum);

        context = this;

        savingReceiptDBHelper = new SavingReceiptDBHelper(context);

        editTextMonth = findViewById(R.id.date);

        selectedMonth = findViewById(R.id.selectedMonth);
        totalReceipts = findViewById(R.id.totalReceipts);
        totalDepAmount = findViewById(R.id.totalDepositAmt);

        getSummary = findViewById(R.id.getSummary);
        printSummary = findViewById(R.id.printSummary);
        goBack = findViewById(R.id.goBack);


        getSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = editTextMonth.getText().toString();
                if(!selectedDate.equals("")) {
                    if(selectedDate.contains("/") && selectedDate.length() == 7 && selectedDate.indexOf("/") == 2) {
                        receiptData = savingReceiptDBHelper.getMonthlyDepAmtDetails(selectedDate);
                        if(receiptData.size() == 2 && !receiptData.get(0).equals("0")) {
                            selectedMonth.setText(selectedDate);
                            totalReceipts.setText(receiptData.get(0));
                            totalDepAmount.setText(receiptData.get(1));
                        }
                        else {
                            Toast.makeText(context, "No Data Found on Selected Month", Toast.LENGTH_SHORT).show();
                            selectedMonth.setText("");
                            totalReceipts.setText("");
                            totalDepAmount.setText("");
                        }
                    } else {
                        Toast.makeText(context, "Enter Data in format MM/YYYY", Toast.LENGTH_SHORT).show();
                        selectedMonth.setText("");
                        totalReceipts.setText("");
                        totalDepAmount.setText("");
                    }
                } else {
                    Toast.makeText(context, "Select a Month", Toast.LENGTH_SHORT).show();
                    selectedMonth.setText("");
                    totalReceipts.setText("");
                    totalDepAmount.setText("");
                }
            }
        });

        printSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!totalReceipts.getText().toString().equals("") && !totalDepAmount.getText().toString().equals("")) {
                    openPrinter();
                    if(flag) {
                        try {
                            byte[] arrHeader = ("\n             SUMMARY\n\n").getBytes("GB2312");
                            byte[] arrStar = ("********************************\n").getBytes("GB2312");
                            byte[] arrText1 = ("Selected Month   :"+selectedDate+"\n\n").getBytes("GB2312");
                            byte[] arrText2 = ("Total Receipts   :"+receiptData.get(0)+"\n\n").getBytes("GB2312");
                            byte[] arrText3 = ("Total Dep. Amt   :"+receiptData.get(1)+"\n\n").getBytes("GB2312");

                            PrinterInterface.begin();

                            PrinterInterface.write(arrStar, arrStar.length);
                            PrinterInterface.write(arrHeader, arrHeader.length);
                            PrinterInterface.write(arrStar, arrStar.length);
                            PrinterInterface.write(arrText1, arrText1.length);
                            PrinterInterface.write(arrText2, arrText2.length);
                            PrinterInterface.write(arrText3, arrText3.length);
                            PrinterInterface.write(arrStar, arrStar.length);
                            PrinterInterface.write(getCmdEscDN(4), 4);
                            PrinterInterface.end();
                            flag = false;
                            PrinterInterface.close();

                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                else Toast.makeText(context, "Get Summary Data First", Toast.LENGTH_SHORT).show();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivitySavingAccReports.class));
                finish();
            }
        });
    }

    public void openPrinter() {
        int result = PrinterInterface.open();
        if (result > 0) {
            flag = true;
        }
        else Toast.makeText(this, "Opening printer Failed!!!", Toast.LENGTH_SHORT).show();
    }

    static public byte[] getCmdEscDN(int n)
    {
        return new byte[] {
                (byte) 0x1B, (byte) 0x64, (byte) n
        };
    }
}