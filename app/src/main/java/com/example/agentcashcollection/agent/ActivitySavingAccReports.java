package com.example.agentcashcollection.agent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.agentcashcollection.R;

public class ActivitySavingAccReports extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_acc_reports);

        findViewById(R.id.savingAccDayWiseSummary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivitySavingSummary.class));
                finish();
            }
        });

        findViewById(R.id.savingMonthlySummary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivityMonthlySavingSum.class));
                finish();
            }
        });
    }
}