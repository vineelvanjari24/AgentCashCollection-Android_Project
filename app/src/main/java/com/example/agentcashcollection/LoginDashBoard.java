package com.example.agentcashcollection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.agentcashcollection.admin.AdminLoginActivity;
import com.example.agentcashcollection.agent.AgentLoginActivity;

public class LoginDashBoard extends AppCompatActivity {

    CardView admin, user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dash_board);

        admin = findViewById(R.id.admin);
        user = findViewById(R.id.user);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdminLogin = new Intent(getApplicationContext(), AdminLoginActivity.class);
                startActivity(intentAdminLogin);
                finish();
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUserLogin = new Intent(getApplicationContext(), AgentLoginActivity.class);
                startActivity(intentUserLogin);
                finish();
            }
        });
    }
}