package com.example.agentcashcollection.agent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpos.jniinterface.PrinterInterface;
import com.example.agentcashcollection.R;
import com.example.agentcashcollection.dbhelper.SavingAccDBHelper;
import com.example.agentcashcollection.dbhelper.SavingReceiptDBHelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivitySavingSummary extends AppCompatActivity {

    EditText dateEditText;
    Button getSummary, printSummary, getCusList;
    TextView depositDateTextView, totalReceiptsTextView, totalDepAmountTextview;
    SavingReceiptDBHelper savingReceiptDBHelper;
    SavingAccDBHelper savingAccDBHelper;
    ImageView goBack;
    TableLayout tableLayoutCusList;
    String selectedDate;
    ArrayList<String> receiptData;
    Boolean flag = false;
    Context context;
    StringBuffer cusListString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_summary);

        context = this;

        savingReceiptDBHelper = new SavingReceiptDBHelper(context);
        savingAccDBHelper = new SavingAccDBHelper(context);

        tableLayoutCusList = findViewById(R.id.cusSavDepAccList);
        dateEditText = findViewById(R.id.date);
        getSummary = findViewById(R.id.getSummary);
        printSummary = findViewById(R.id.printSummary);
        getCusList = findViewById(R.id.getCusList);
        depositDateTextView = findViewById(R.id.depDate);
        totalDepAmountTextview = findViewById(R.id.totalDeposit);
        totalReceiptsTextView = findViewById(R.id.totalReceipts);
        goBack = findViewById(R.id.goBack);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date;
                                if(dayOfMonth < 10 && month + 1 <10) {
                                    date = year + "-" + "0" +(month + 1) + "-" + "0" + dayOfMonth;
                                } else if (dayOfMonth < 10) {
                                    date = year + "-" + (month + 1) + "-" + "0" + dayOfMonth;
                                } else if (month+1 < 10) {
                                    date = year + "-" + "0" + (month + 1) + "-" + dayOfMonth;
                                } else {
                                    date = year + "-" + (month + 1) + "-" + dayOfMonth;
                                }
                                dateEditText.setText(date);
                            }
                        },
                        year, month, day);

                datePickerDialog.show();
            }
        });

        getSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = dateEditText.getText().toString();
                if(!selectedDate.equals("")) {
                    receiptData = savingReceiptDBHelper.getReceiptRecordCount_Collection(selectedDate);
                    Cursor cursor = savingReceiptDBHelper.getCursorReceiptRecord(selectedDate);
                    if(receiptData.size() > 0) {
                        depositDateTextView.setText(selectedDate);
                        totalReceiptsTextView.setText(receiptData.get(0));
                        totalDepAmountTextview.setText(receiptData.get(1));

                        if (cursor.moveToFirst()) {
                            cusListString = new StringBuffer();

                            TableRow headerRow = new TableRow(context);

                            cusListString.append("AccNum.  RecptNo.  DepAmt"+"\n");

                            for (int i = 0; i < cursor.getColumnCount(); i++) {
                                TextView headerTextView = new TextView(context);
                                headerTextView.setText(String.format(cursor.getColumnName(i)));
                                headerTextView.setTextAppearance(R.style.boldText);
                                headerRow.addView(headerTextView);
                            }

                            tableLayoutCusList.addView(headerRow);

                            do {
                                TableRow dataRow = new TableRow(context);

                                for (int i = 0; i < cursor.getColumnCount(); i++) {
                                    TextView dataTextView = new TextView(context);

                                    String str = cursor.getString(i);
                                    dataTextView.setText(str);
                                    if(i!=1)
                                        cusListString.append(str).append("    ");
                                    else
                                        cusListString.append(str).append("        ");
                                    dataRow.addView(dataTextView);
                                }

                                tableLayoutCusList.addView(dataRow);
                                cusListString.append("\n");
                            } while (cursor.moveToNext());
                        }

                        //Log.d("Cus List", cusListString.toString());
                        cursor.close();
                    }
                    else Toast.makeText(context, "No Data Found on Selected Date", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(context, "Select Date", Toast.LENGTH_SHORT).show();
            }
        });

        printSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!totalReceiptsTextView.getText().toString().equals("") && !totalDepAmountTextview.getText().toString().equals("")) {
                    openPrinter();
                    if(flag) {
                        try {
                            byte[] arrHeader = ("\n     DAY-WISE SAVING SUMMARY\n\n").getBytes("GB2312");
                            byte[] arrStar = ("********************************\n").getBytes("GB2312");
                            byte[] arrText1 = ("Deposit Date    :"+selectedDate+"\n\n").getBytes("GB2312");
                            byte[] arrCusList = (cusListString+"\n\n").getBytes("GB2312");
                            byte[] arrText2 = ("Total Receipts  :"+receiptData.get(0)+"\n\n").getBytes("GB2312");
                            byte[] arrText3 = ("Total Deposit   :"+receiptData.get(1)+"\n\n").getBytes("GB2312");

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

        getCusList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogCusList = new Dialog(context);
                dialogCusList.setContentView(R.layout.customer_list);

                TextView headerName = dialogCusList.findViewById(R.id.headerText);

                headerName.setText("CUSTOMER LIST");

                TextView columnText = dialogCusList.findViewById(R.id.columnText);
                columnText.setText(" Acc No.    Name   Acc. Bal");

                TextView textViewCusList = dialogCusList.findViewById(R.id.customerList);


                StringBuffer cusList = savingAccDBHelper.getCustomerList(-1);
                textViewCusList.setText(cusList);

                dialogCusList.show();

                Button printList = dialogCusList.findViewById(R.id.printList);
                printList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPrinter();

                        try {
                            byte[] arrHeader = ("\n         CUSTOMERS LIST\n\n").getBytes("GB2312");
                            byte[] arrStar = ("********************************\n").getBytes("GB2312");
                            byte[] arrColumns = ("ACC NO.    CUS NAME    Acc. Bal\n\n").getBytes("GB2312");
                            byte[] arrList = (cusList+ "\n\n").getBytes("GB2312");

                            PrinterInterface.begin();

                            PrinterInterface.write(arrStar, arrStar.length);
                            PrinterInterface.write(arrHeader, arrHeader.length);
                            PrinterInterface.write(arrStar, arrStar.length);
                            PrinterInterface.write(arrColumns, arrColumns.length);
                            PrinterInterface.write(arrList, arrList.length);
                            PrinterInterface.write(arrStar, arrStar.length);
                            PrinterInterface.write(getCmdEscDN(4), 4);
                            PrinterInterface.end();
                            flag = false;
                            PrinterInterface.close();

                            dialogCusList.dismiss();

                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void openPrinter() {
        int result = PrinterInterface.open();
        if (result > 0) {
            flag = true;
        }
        else Toast.makeText(context, "Opening printer Failed!!!", Toast.LENGTH_SHORT).show();
    }

    static public byte[] getCmdEscDN(int n)
    {
        return new byte[] {
                (byte) 0x1B, (byte) 0x64, (byte) n
        };
    }
}