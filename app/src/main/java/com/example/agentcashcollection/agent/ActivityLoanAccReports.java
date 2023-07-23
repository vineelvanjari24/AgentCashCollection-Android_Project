package com.example.agentcashcollection.agent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.agentcashcollection.R;

public class ActivityLoanAccReports extends AppCompatActivity {

    CardView collectionSummary, monthlySummary;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_acc_reports);

        context = this;

        collectionSummary = findViewById(R.id.collectionAccDayWiseSummary);
        monthlySummary = findViewById(R.id.collectionMonthlySummary);

        collectionSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ActivityCollectionSummary.class));
            }
        });

        monthlySummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ActivityLoanMonthlySum.class));
            }
        });
    }
}