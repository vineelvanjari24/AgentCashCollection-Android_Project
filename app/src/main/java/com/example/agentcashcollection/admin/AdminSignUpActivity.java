package com.example.agentcashcollection.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agentcashcollection.R;
import com.example.agentcashcollection.dbhelper.AdminLoginDBHelper;

public class AdminSignUpActivity extends AppCompatActivity {

    Button signup;
    EditText username, password, rePassword;

    AdminLoginDBHelper adminLoginDBHelper = new AdminLoginDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);

        signup = findViewById(R.id.signup);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.re_password);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                String rePasswordText = rePassword.getText().toString();

                if(usernameText.equals("") || passwordText.equals("") || rePasswordText.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter in all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(passwordText.equals(rePasswordText)) {
                        if(!adminLoginDBHelper.usernameCheck(usernameText)) {
                            if(passwordText.length() >= 8) {
                                if(adminLoginDBHelper.insertUser(usernameText, passwordText)) {
                                    Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else
                                    Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Password must be minimum 8 characters", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "User already exists, enter username uniquely", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Passwords should match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}