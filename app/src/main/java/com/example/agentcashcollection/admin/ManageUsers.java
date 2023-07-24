package com.example.agentcashcollection.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.agentcashcollection.R;
import com.example.agentcashcollection.agent.AgentSignUpActivity;

public class ManageUsers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        Button create_User=findViewById(R.id.Create_acc);
        Button List=findViewById(R.id.Users_List);
        create_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AgentSignUpActivity.class);
                startActivity(intent);

            }
        });

        List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),UserList.class);
                startActivity(intent);
            }
        });

    }
}