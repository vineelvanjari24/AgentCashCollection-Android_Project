package com.example.agentcashcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agentcashcollection.admin.AdminMainActivity;
import com.example.agentcashcollection.agent.MainActivity;

public class DeviceStartingPage extends AppCompatActivity {

    Intent intentNextActivity;
    SharedPreferences sharedPreferencesLogin, sharedPreferencesDeviceInfo;

    EditText deviceIDText, agentNameText, agentNumText;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_starting_page);

        start = findViewById(R.id.start);
        deviceIDText = findViewById(R.id.deviceID);
        agentNameText = findViewById(R.id.agentName);
        agentNumText = findViewById(R.id.agentNumber);

        sharedPreferencesDeviceInfo = getSharedPreferences("Device Info", MODE_PRIVATE);

        deviceIDText.setText(sharedPreferencesDeviceInfo.getString("Device ID", ""));
        if(!deviceIDText.getText().toString().equals("")) {
            deviceIDText.setClickable(false);
            deviceIDText.setCursorVisible(false);
            deviceIDText.setFocusable(false);
            deviceIDText.setFocusableInTouchMode(false);
        }

        agentNameText.setText(sharedPreferencesDeviceInfo.getString("Agent Name", ""));
        if(!agentNameText.getText().toString().equals("")) {
            agentNameText.setClickable(false);
            agentNameText.setCursorVisible(false);
            agentNameText.setFocusable(false);
            agentNameText.setFocusableInTouchMode(false);
        }

        agentNumText.setText(sharedPreferencesDeviceInfo.getString("Agent Number", ""));
        if(!agentNumText.getText().toString().equals("")) {
            agentNumText.setClickable(false);
            agentNumText.setCursorVisible(false);
            agentNumText.setFocusable(false);
            agentNumText.setFocusableInTouchMode(false);
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deviceIDText.getText().toString().equals(""))
                    Toast.makeText(DeviceStartingPage.this, "Enter Device ID", Toast.LENGTH_SHORT).show();
                else if (agentNameText.getText().toString().equals(""))
                    Toast.makeText(DeviceStartingPage.this, "Enter Agent Name", Toast.LENGTH_SHORT).show();
                else if (agentNumText.getText().toString().equals(""))
                    Toast.makeText(DeviceStartingPage.this, "Enter Agent Number", Toast.LENGTH_SHORT).show();
                else {
                    sharedPreferencesDeviceInfo = getSharedPreferences("Device Info", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sharedPreferencesDeviceInfo.edit();
                    ed.putString("Device ID", deviceIDText.getText().toString());
                    ed.putString("Agent Name", agentNameText.getText().toString());
                    ed.putString("Agent Number", agentNumText.getText().toString());
                    ed.apply();

                    deviceIDText.setText(sharedPreferencesDeviceInfo.getString("Device ID", ""));

                    agentNameText.setText(sharedPreferencesDeviceInfo.getString("Agent Name", ""));

                    agentNumText.setText(sharedPreferencesDeviceInfo.getString("Agent Number", ""));



                    sharedPreferencesLogin = getSharedPreferences("Login", MODE_PRIVATE);
                    boolean checkLoggedIn = sharedPreferencesLogin.getBoolean("flag", false);
                    String user = sharedPreferencesLogin.getString("user", "");

                    if(!checkLoggedIn) {
                        intentNextActivity = new Intent(getApplicationContext(), LoginDashBoard.class);
                    }
                    else {
                        if(user.equals("admin"))
                            intentNextActivity = new Intent(getApplicationContext(), AdminMainActivity.class);
                        else if (user.equals("user"))
                            intentNextActivity = new Intent(getApplicationContext(), MainActivity.class);
                        else
                            intentNextActivity = new Intent(getApplicationContext(), LoginDashBoard.class);
                    }

                    startActivity(intentNextActivity);
                    finish();
                }
            }
        });
    }
}