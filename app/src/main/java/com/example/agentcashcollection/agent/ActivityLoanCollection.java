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
import com.example.agentcashcollection.dbhelper.CusDetailsDBHelper;
import com.example.agentcashcollection.dbhelper.RecoveryReceiptDBHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityLoanCollection extends AppCompatActivity {

    Button getAccDetails, recovery;
    AutoCompleteTextView identifyingNum;
    EditText recoveryAmount;
    TextView accNumberTextView, accHolderTextView, dueAmountTextView, accOpenDateTextView, phoneNumTextView;
    ArrayList<String> acc_PhoneNumbers, accountDetails, lastTransactionDetails;
    CusDetailsDBHelper cusDetailsDBHelper;
    RecoveryReceiptDBHelper recoveryReceiptDBHelper;
    Context context;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_collection);
        
        context = this;

        cusDetailsDBHelper = new CusDetailsDBHelper(context);
        recoveryReceiptDBHelper = new RecoveryReceiptDBHelper(context);

        getAccDetails = findViewById(R.id.getAccDetails);
        recovery = findViewById(R.id.recovery);
        identifyingNum = findViewById(R.id.identifyingNumber);
        recoveryAmount = findViewById(R.id.recoveryAmount);
        accNumberTextView = findViewById(R.id.accNumberTextview);
        accHolderTextView = findViewById(R.id.accHolderTextview);
        dueAmountTextView = findViewById(R.id.dueAmountTextView);
        accOpenDateTextView = findViewById(R.id.accOpenDateTextview);
        phoneNumTextView = findViewById(R.id.phoneNumTextview);

        acc_PhoneNumbers = cusDetailsDBHelper.getIdentifyingNum();
        ArrayAdapter<String> adapterAutoComTextView = new ArrayAdapter<>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, acc_PhoneNumbers);
        identifyingNum.setAdapter(adapterAutoComTextView);

        getAccDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountNo = identifyingNum.getText().toString();
                if(accountNo.equals(""))
                    Toast.makeText(getApplicationContext(), "Enter Account Number", Toast.LENGTH_SHORT).show();
                else if (cusDetailsDBHelper.checkAccountNumber(accountNo)){
                    accountDetails = cusDetailsDBHelper.getAccDetails(accountNo);
                    accNumberTextView.setText(accountDetails.get(0));
                    accHolderTextView.setText(accountDetails.get(1));
                    dueAmountTextView.setText(accountDetails.get(2));
                    accOpenDateTextView.setText(accountDetails.get(3));
                    phoneNumTextView.setText(accountDetails.get(4));
                } else {
                    Toast.makeText(context , "No Account Found", Toast.LENGTH_SHORT).show();
                    accNumberTextView.setText("");
                    accHolderTextView.setText("");
                    dueAmountTextView.setText("");
                    accOpenDateTextView.setText("");
                    phoneNumTextView.setText("");
                }
            }
        });

        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!accHolderTextView.getText().toString().equals("")) {
                    long prevDueAmount = Integer.parseInt(accountDetails.get(2));
                    long dedAmount = Integer.parseInt(recoveryAmount.getText().toString());
                    long accNum = Integer.parseInt(accountDetails.get(0));

                    if (prevDueAmount != 0)  {
                        if(!recoveryAmount.getText().toString().equals("")) {
                            Intent intentReceiptActivity = new Intent(getApplicationContext(), RecoveryReceiptActivity.class);

                            if(cusDetailsDBHelper.updateDueAmount(accountDetails.get(0), prevDueAmount, dedAmount)) {
                                lastTransactionDetails = recoveryReceiptDBHelper.getLastTransaction(accNum);

                                if(recoveryReceiptDBHelper.insertReceiptRecord(accNum, dedAmount)) {
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
                                    long receiptNo = recoveryReceiptDBHelper.getReceiptNo(receiptDate, ""+accNum);

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
                                    bundle.putString("Acc Open Date", accountDetails.get(3));
                                    bundle.putString("Ded Amount", ""+dedAmount);
                                    bundle.putString("New Balance", ""+(prevDueAmount - dedAmount));
                                    bundle.putString("Phone Number", accountDetails.get(4));
                                    bundle.putString("Address", accountDetails.get(5));
                                    intentReceiptActivity.putExtra("Account Details Bundle", bundle);

                                    startActivity(intentReceiptActivity);

                                    accNumberTextView.setText("");
                                    accHolderTextView.setText("");
                                    phoneNumTextView.setText("");
                                    dueAmountTextView.setText("");
                                    accOpenDateTextView.setText("");
                                    identifyingNum.setText("");
                                    recoveryAmount.setText("");
                                }
                                else
                                    Toast.makeText(context, "Unsuccessful, Linking Failed", Toast.LENGTH_SHORT).show();

                            } else
                                Toast.makeText(context, "Amount Deposit Failed", Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(context, "Enter Deposit Amount", Toast.LENGTH_SHORT).show();

                    } else Toast.makeText(context, "No due amount for the account", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Get Details Account Details of Customer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}