package com.example.agentcashcollection.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.agentcashcollection.LoginDashBoard;
import com.example.agentcashcollection.R;

public class AdminMainActivity extends AppCompatActivity {

    CardView uploadDownload, deleteFiles, setting, machineInfo, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        uploadDownload = findViewById(R.id.uploadDownload);
        deleteFiles = findViewById(R.id.deleteFiles);
        setting = findViewById(R.id.setting);
        machineInfo = findViewById(R.id.machineInfo);
        logout = findViewById(R.id.logoutAdmin);

        uploadDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(getApplicationContext(), UploadDownload.class);
                startActivity(intentActivity);
            }
        });

        deleteFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(getApplicationContext(), DeleteFiles.class);
                startActivity(intentActivity);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(getApplicationContext(), Setting.class);
                startActivity(intentActivity);
            }
        });

        machineInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(getApplicationContext(), MachineInfo.class);
                startActivity(intentActivity);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putBoolean("flag", false);
                ed.apply();

                Intent intent = new Intent(getApplicationContext(), LoginDashBoard.class);
                startActivity(intent);
                finish();
            }
        });
    }
}