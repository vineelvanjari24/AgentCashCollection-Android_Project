package com.example.agentcashcollection.agent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpos.jniinterface.PrinterInterface;
import com.example.agentcashcollection.R;
import com.example.agentcashcollection.dbhelper.CusDetailsDBHelper;
import com.example.agentcashcollection.dbhelper.RecoveryReceiptDBHelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityCollectionSummary extends AppCompatActivity {
    EditText dateEditText;
    Button getSummary, printSummary, getCusList, getPaidList, getUnpaidList;
    TextView collecDateTextView, totalReceiptsTextView, totalCollecTextView;
    RecoveryReceiptDBHelper recoveryReceiptDBHelper;
    CusDetailsDBHelper cusDetailsDBHelper;
    ImageView goBack;
    String selectedDate;
    ArrayList<String> receiptData;
    Boolean flag = false;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_summary);

        context = this;

        recoveryReceiptDBHelper = new RecoveryReceiptDBHelper(context);
        cusDetailsDBHelper = new CusDetailsDBHelper(context);

        dateEditText = findViewById(R.id.date);
        getSummary = findViewById(R.id.getSummary);
        printSummary = findViewById(R.id.printSummary);
        getCusList = findViewById(R.id.getCusList);
        getPaidList = findViewById(R.id.getPaidList);
        getUnpaidList = findViewById(R.id.getUnpaidList);
        collecDateTextView = findViewById(R.id.collecDate);
        totalCollecTextView = findViewById(R.id.totalCollection);
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
                    receiptData = recoveryReceiptDBHelper.getReceiptRecordCount_Collection(selectedDate);
                    if(receiptData.size() == 2 && !receiptData.get(0).equals("0")) {
                        collecDateTextView.setText(selectedDate);
                        totalReceiptsTextView.setText(receiptData.get(0));
                        totalCollecTextView.setText(receiptData.get(1));
                    }
                    else {
                        Toast.makeText(context, "No Data Found on Selected Date", Toast.LENGTH_SHORT).show();
                        collecDateTextView.setText("");
                        totalReceiptsTextView.setText("");
                        totalCollecTextView.setText("");
                    }
                } else {
                    Toast.makeText(context, "Select a Date", Toast.LENGTH_SHORT).show();collecDateTextView.setText("");
                    collecDateTextView.setText("");
                    totalReceiptsTextView.setText("");
                    totalCollecTextView.setText("");
                }
            }
        });

        printSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!totalReceiptsTextView.getText().toString().equals("") && !totalCollecTextView.getText().toString().equals("")) {
                    openPrinter();
                    if(flag) {
                        try {
                            byte[] arrHeader = ("\n             SUMMARY\n\n").getBytes("GB2312");
                            byte[] arrStar = ("********************************\n").getBytes("GB2312");
                            byte[] arrText1 = ("Collection Date  :"+selectedDate+"\n\n").getBytes("GB2312");
                            byte[] arrText2 = ("Total Receipts   :"+receiptData.get(0)+"\n\n").getBytes("GB2312");
                            byte[] arrText3 = ("Total Collection :"+receiptData.get(1)+"\n\n").getBytes("GB2312");

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
                startActivity(new Intent(context, ActivityLoanAccReports.class));
                finish();
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
                columnText.setText(" Acc No.     Cus Name");

                TextView textViewCusList = dialogCusList.findViewById(R.id.customerList);


                StringBuffer cusList = cusDetailsDBHelper.getCustomerList(-1);
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
                            byte[] arrColumns = ("ACC NO.    CUS NAME\n\n").getBytes("GB2312");
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

        getPaidList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!dateEditText.getText().toString().equals("")) {
                    Dialog dialogPaidCusList = new Dialog(context);
                    dialogPaidCusList.setContentView(R.layout.customer_list);

                    TextView headerName = dialogPaidCusList.findViewById(R.id.headerText);

                    headerName.setText("PAID CUSTOMER LIST");

                    TextView columnText = dialogPaidCusList.findViewById(R.id.columnText);
                    columnText.setText(" Acc No.     Paid Amo.");

                    TextView textViewCusList = dialogPaidCusList.findViewById(R.id.customerList);

                    ArrayList<String> paidCusDetails = recoveryReceiptDBHelper.getPaidCusList(dateEditText.getText().toString());
                    textViewCusList.setText(paidCusDetails.get(0)+"\nTotal Amount  =   "+paidCusDetails.get(1)+ "\n\n");

                    dialogPaidCusList.show();

                    Button printList = dialogPaidCusList.findViewById(R.id.printList);
                    printList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openPrinter();

                            try {
                                byte[] arrHeader = ("\n         PAID CUSTOMERS\n\n").getBytes("GB2312");
                                byte[] arrStar = ("********************************\n").getBytes("GB2312");
                                byte[] arrColumns = ("ACC NO.    PAID AMO.\n\n").getBytes("GB2312");
                                byte[] arrList = (paidCusDetails.get(0)+ "\n\n").getBytes("GB2312");
                                byte[] arrTotalPaid = ("\nTotal Amount  =   "+paidCusDetails.get(1)+ "\n\n").getBytes("GB2312");

                                PrinterInterface.begin();

                                PrinterInterface.write(arrStar, arrStar.length);
                                PrinterInterface.write(arrHeader, arrHeader.length);
                                PrinterInterface.write(arrStar, arrStar.length);
                                PrinterInterface.write(arrColumns, arrColumns.length);
                                PrinterInterface.write(arrList, arrList.length);
                                PrinterInterface.write(arrTotalPaid, arrTotalPaid.length);
                                PrinterInterface.write(arrStar, arrStar.length);
                                PrinterInterface.write(getCmdEscDN(4), 4);
                                PrinterInterface.end();
                                flag = false;
                                PrinterInterface.close();

                                dialogPaidCusList.dismiss();

                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
                else Toast.makeText(context, "Select a Date", Toast.LENGTH_SHORT).show();
            }
        });

        getUnpaidList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialogUnpaidCusList = new Dialog(context);
                dialogUnpaidCusList.setContentView(R.layout.customer_list);

                TextView headerName = dialogUnpaidCusList.findViewById(R.id.headerText);

                headerName.setText("UNPAID CUSTOMER LIST");

                TextView columnText = dialogUnpaidCusList.findViewById(R.id.columnText);
                columnText.setText(" Acc No.     Cus Name");

                TextView textViewCusList = dialogUnpaidCusList.findViewById(R.id.customerList);

                StringBuffer cusList = cusDetailsDBHelper.getCustomerList(0);
                textViewCusList.setText(cusList);

                dialogUnpaidCusList.show();

                Button printList = dialogUnpaidCusList.findViewById(R.id.printList);
                printList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPrinter();

                        try {
                            byte[] arrHeader = ("\n        UNPAID CUSTOMERS\n\n").getBytes("GB2312");
                            byte[] arrStar = ("********************************\n").getBytes("GB2312");
                            byte[] arrColumns = ("ACC NO.    CUS NAME\n\n").getBytes("GB2312");
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

                            dialogUnpaidCusList.dismiss();

                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
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