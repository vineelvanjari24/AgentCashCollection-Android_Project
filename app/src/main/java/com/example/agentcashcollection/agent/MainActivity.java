package com.example.agentcashcollection.agent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agentcashcollection.LoginDashBoard;
import com.example.agentcashcollection.R;

public class MainActivity extends AppCompatActivity {
    public static String agentName;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        SharedPreferences spUsername = getSharedPreferences("Username", MODE_PRIVATE);
        agentName = spUsername.getString("username", "");

        findViewById(R.id.savingAccColl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ActivitySavingsAcc.class));
            }
        });

        findViewById(R.id.loanAccColl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ActivityLoanCollection.class));
            }
        });

        findViewById(R.id.savingAccReports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ActivitySavingAccReports.class));
            }
        });

        findViewById(R.id.loanAccReports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ActivityLoanAccReports.class));
            }
        });

        findViewById(R.id.reprintReceipt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.deviceDetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.logoutAgent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putBoolean("flag", false);
                ed.apply();

                Intent intentLoginActivity = new Intent(context, LoginDashBoard.class);
                startActivity(intentLoginActivity);
                finish();
            }
        });

    }
}