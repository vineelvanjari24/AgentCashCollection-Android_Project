package com.example.agentcashcollection.agent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agentcashcollection.R;
import com.example.agentcashcollection.dbhelper.SavingAccDBHelper;
import com.example.agentcashcollection.dbhelper.SavingReceiptDBHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivitySavingsAcc extends AppCompatActivity {

    Button getAccDetails, deposit;
    AutoCompleteTextView identifyingNum;
    EditText depositAmount;
    TextView accNumberTextView, accHolderTextView, curBalanceTextView, phoneNumTextView;
    ArrayList<String> accountDetails, lastTransactionDetails, acc_PhoneNumbers;
    SavingAccDBHelper savingAccDBHelper;
    SavingReceiptDBHelper savingReceiptDBHelper;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings_acc);

        context = this;

        savingAccDBHelper = new SavingAccDBHelper(context);
        savingReceiptDBHelper = new SavingReceiptDBHelper(context);

        getAccDetails = findViewById(R.id.getAccDetails);
        deposit = findViewById(R.id.deposit);
        identifyingNum = findViewById(R.id.identifyingNumber);
        depositAmount = findViewById(R.id.depositAmount);
        accNumberTextView = findViewById(R.id.accNumberTextview);
        accHolderTextView = findViewById(R.id.accHolderTextview);
        curBalanceTextView = findViewById(R.id.curBalanceTextView);
        phoneNumTextView = findViewById(R.id.phoneNumTextview);

        acc_PhoneNumbers = savingAccDBHelper.getIdentifyingNum();
        ArrayAdapter<String> adapterAutoComTextView = new ArrayAdapter<>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, acc_PhoneNumbers);
        identifyingNum.setAdapter(adapterAutoComTextView);

        getAccDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountNo = identifyingNum.getText().toString();
                if(accountNo.equals(""))
                    Toast.makeText(context, "Enter Account Number", Toast.LENGTH_SHORT).show();
                else if (savingAccDBHelper.checkAccountNumber(accountNo)){
                    accountDetails = savingAccDBHelper.getAccDetails(accountNo);
                    accNumberTextView.setText(accountDetails.get(0));
                    accHolderTextView.setText(accountDetails.get(1));
                    curBalanceTextView.setText(accountDetails.get(2));
                    phoneNumTextView.setText(accountDetails.get(3));
                } else {
                    Toast.makeText(context , "No Account Found", Toast.LENGTH_SHORT).show();
                    accNumberTextView.setText("");
                    accHolderTextView.setText("");
                    curBalanceTextView.setText("");
                    phoneNumTextView.setText("");
                }
            }
        });

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String depAmountStr = depositAmount.getText().toString();
                if(!depAmountStr.equals("")) {
                    if(!accHolderTextView.getText().toString().equals("")) {
                        long prevDueAmount = Long.parseLong(accountDetails.get(2));
                        long depAmount = Long.parseLong(depositAmount.getText().toString());
                        long accNum = Long.parseLong(accountDetails.get(0));

                            Intent intentReceiptActivity = new Intent(context, SavingReceiptActivity.class);

                            if(savingAccDBHelper.updateBalanceAmount(accountDetails.get(0), prevDueAmount, depAmount)) {
                                lastTransactionDetails = savingReceiptDBHelper.getLastTransaction(accNum);

                                if(savingReceiptDBHelper.insertReceiptRecord(accNum, depAmount)) {
                                    final Calendar calendar = Calendar.getInstance();
                                    int year = calendar.get(Calendar.YEAR);
                                    int month = calendar.get(Calendar.MONTH);
                                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                    int minute = calendar.get(Calendar.MINUTE);
                                    int second = calendar.get(Calendar.SECOND);
                                    String receiptTime = hour+":"+minute+":"+second;
                                    String receiptDate;
                                    if(dayOfMonth < 10 && month + 1 <10) {
                                        receiptDate = year + "-" + "0" +(month + 1) + "-" + "0" + dayOfMonth;
                                    } else if (dayOfMonth < 10) {
                                        receiptDate = year + "-" + (month + 1) + "-" + "0" + dayOfMonth;
                                    } else if (month+1 < 10) {
                                        receiptDate = year + "-" + "0" + (month + 1) + "-" + dayOfMonth;
                                    } else {
                                        receiptDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                                    }
                                    long receiptNo = savingReceiptDBHelper.getReceiptNo(receiptDate, ""+accNum);

                                    Bundle bundle = new Bundle();
                                    if(lastTransactionDetails.size() == 2) {
                                        bundle.putString("Last Bill Date", lastTransactionDetails.get(0));
                                        bundle.putString("Last Paid", lastTransactionDetails.get(1));
                                    }
                                    else {
                                        bundle.putString("Last Bill Date", "NIL");
                                        bundle.putString("Last Paid", "NIL");
                                    }
                                    bundle.putString("Receipt No", ""+receiptNo);
                                    bundle.putString("Receipt Date", receiptDate);
                                    bundle.putString("Receipt Time", receiptTime);
                                    bundle.putString("Account No", accountDetails.get(0));
                                    bundle.putString("Account Holder", accountDetails.get(1));
                                    bundle.putString("Old Balance", accountDetails.get(2));
                                    bundle.putString("Dep Amount", ""+depAmount);
                                    bundle.putString("New Balance", ""+(prevDueAmount + depAmount));
                                    bundle.putString("Phone Number", accountDetails.get(3));
                                    bundle.putString("Address", accountDetails.get(4));
                                    intentReceiptActivity.putExtra("Account Details Bundle", bundle);

                                    startActivity(intentReceiptActivity);

                                    accNumberTextView.setText("");
                                    accHolderTextView.setText("");
                                    curBalanceTextView.setText("");
                                    phoneNumTextView.setText("");
                                    identifyingNum.setText("");
                                    depositAmount.setText("");
                                }
                                else
                                    Toast.makeText(context, "Unsuccessful, Linking Failed", Toast.LENGTH_SHORT).show();

                            } else
                                Toast.makeText(context, "Amount Deposit Failed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "Get Details Account Details of Customer", Toast.LENGTH_SHORT).show();
                    }
                }
                else Toast.makeText(context, "Enter Deposit Amount", Toast.LENGTH_SHORT).show();
            }
        });
    }
}