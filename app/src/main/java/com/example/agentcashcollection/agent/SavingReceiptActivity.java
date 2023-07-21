package com.example.agentcashcollection.agent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudpos.jniinterface.PrinterInterface;
import com.example.agentcashcollection.R;
import com.wizarpos.htmllibrary.PrinterBitmapUtil;

import java.io.UnsupportedEncodingException;

public class SavingReceiptActivity extends AppCompatActivity {

    Button newCollection, print;
    TextView agentName, receiptNo, receiptDate, receiptTime, accHolder, accNumber, prevBalance, depAmount, curBalance, lastBill, lastPaid, cusPhoneNumber, address;
    Bundle bundleDetails;
    Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_receipt);

        newCollection = findViewById(R.id.newCollection);
        print = findViewById(R.id.printSummary);

        agentName = findViewById(R.id.agentNameTextView);
        receiptDate = findViewById(R.id.receiptDate);
        receiptTime = findViewById(R.id.timeTextview);
        receiptNo = findViewById(R.id.receiptNo);
        accHolder = findViewById(R.id.accHolder);
        accNumber = findViewById(R.id.accNumber);
        prevBalance = findViewById(R.id.prevBal);
        depAmount = findViewById(R.id.depAmount);
        curBalance = findViewById(R.id.curBal);
        lastBill = findViewById(R.id.lastBill);
        lastPaid = findViewById(R.id.lastPaid);
        cusPhoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);

        bundleDetails = getIntent().getBundleExtra("Account Details Bundle");
        String agentNameText, receiptDateText, receiptTimeText, receiptNoText, accHolderText, accNoText, oldBalText, depAmountText, newBalText, lastBillText, lastPaidText, cusPhoneNumberText, addressText;

        agentNameText = MainActivity.agentName;
        receiptDateText = bundleDetails.getString("Receipt Date");
        receiptTimeText = bundleDetails.getString("Receipt Time");
        receiptNoText = bundleDetails.getString("Receipt No");
        accHolderText = bundleDetails.getString("Account Holder");
        accNoText = bundleDetails.getString("Account No");
        oldBalText = bundleDetails.getString("Old Balance");
        depAmountText = bundleDetails.getString("Dep Amount");
        newBalText = bundleDetails.getString("New Balance");
        lastBillText = bundleDetails.getString("Last Bill Date");
        lastPaidText = bundleDetails.getString("Last Paid");
        cusPhoneNumberText = bundleDetails.getString("Phone Number");
        addressText = bundleDetails.getString("Address");


        agentName.setText(agentNameText);
        receiptDate.setText(receiptDateText);
        receiptTime.setText(receiptTimeText);
        receiptNo.setText(receiptNoText);
        accHolder.setText(accHolderText);
        accNumber.setText(accNoText);
        prevBalance.setText(oldBalText);
        depAmount.setText(depAmountText);
        curBalance.setText(newBalText);
        lastBill.setText(lastBillText);
        lastPaid.setText(lastPaidText);
        cusPhoneNumber.setText(cusPhoneNumberText);
        address.setText(addressText);

        newCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPrinter();
                if (flag) {

                    try {
                        Bitmap header = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.header_receipt);
                        Bitmap thankYou = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.thankyou);
                        byte[] arrStar = ("********************************\n").getBytes("GB2312");
                        byte[] arrBillReceiptHeader = ("\n          BILL RECEIPT\n\n").getBytes("GB2312");
                        byte[] agentName = ("Agent Name     : "+agentNameText+"\n\n").getBytes("GB2312");
                        byte[]  dateTime = ("DT : "+receiptDateText+"  TM : "+receiptTimeText+"\n\n").getBytes("GB2312");
                        byte[] receiptNumber = ("Receipt Number : "+receiptNoText+"\n\n").getBytes("GB2312");
                        byte[] cusName = ("Account Holder : "+accHolderText+"\n\n").getBytes("GB2312");
                        byte[] accNumber = ("Account Number : "+accNoText+"\n\n").getBytes("GB2312");
                        byte[] address = ("Address        : "+addressText+"\n\n").getBytes("GB2312");
                        byte[] cusPhoneNo = ("Phone Number   : "+cusPhoneNumberText+"\n\n").getBytes("GB2312");
                        byte[] arrLastBill = ("Last Bill      : "+lastBillText+"\n\n").getBytes("GB2312");
                        byte[] arrLastPaid = ("Last Paid      : "+lastPaidText+"\n\n").getBytes("GB2312");
                        byte[] arrRefNo = ("Ref No.        : HFEIFHOEHFYRYFT\n\n").getBytes("GB2312");
                        byte[] arrOpeningBal = ("Opening Balance: "+oldBalText+"\n\n").getBytes("GB2312");
                        byte[] arrDepAmount = ("Deposit Amount : "+depAmountText+"\n\n").getBytes("GB2312");
                        byte[] curBalance = ("Cur.  Balance  : "+newBalText+"\n\n").getBytes("GB2312");
                        byte[] companyNumber =   ("       PH No. 99839829842\n").getBytes("GB2312");

                        PrinterInterface.begin();
                        PrinterInterface.write(arrStar, arrStar.length);
                        PrinterBitmapUtil.printBitmap(header, 0, 0, true);
                        PrinterInterface.write(arrStar, arrStar.length);
                        PrinterInterface.write(arrBillReceiptHeader, arrBillReceiptHeader.length);
                        PrinterInterface.write(arrStar, arrStar.length);
                        PrinterInterface.write(agentName, agentName.length);
                        PrinterInterface.write(dateTime, dateTime.length);
                        PrinterInterface.write(receiptNumber, receiptNumber.length);
                        PrinterInterface.write(cusName, cusName.length);
                        PrinterInterface.write(accNumber, accNumber.length);
                        PrinterInterface.write(address, address.length);
                        PrinterInterface.write(cusPhoneNo, cusPhoneNo.length);
                        PrinterInterface.write(arrRefNo, arrRefNo.length);
                        PrinterInterface.write(arrLastBill, arrLastBill.length);
                        PrinterInterface.write(arrLastPaid, arrLastPaid.length);
                        PrinterInterface.write(arrOpeningBal, arrOpeningBal.length);
                        PrinterInterface.write(arrDepAmount, arrDepAmount.length);
                        PrinterInterface.write(curBalance, curBalance.length);
                        PrinterInterface.write(arrStar, arrStar.length);
                        PrinterBitmapUtil.printBitmap(thankYou, 0, 0, true);
                        PrinterInterface.write(companyNumber, companyNumber.length);
                        PrinterInterface.write(arrStar, arrStar.length);
                        PrinterInterface.write(getCmdEscDN(4), 4);

                        PrinterInterface.end();
                        PrinterInterface.close();
                        flag = false;
                        finish();
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
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