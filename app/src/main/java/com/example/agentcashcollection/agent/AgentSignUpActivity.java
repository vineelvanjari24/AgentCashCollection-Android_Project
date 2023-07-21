package com.example.agentcashcollection.agent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agentcashcollection.R;
import com.example.agentcashcollection.dbhelper.AgentLoginDBHelper;

public class AgentSignUpActivity extends AppCompatActivity {

    Button signup;
    EditText username, password, rePassword;

    AgentLoginDBHelper agentLoginDBHelper = new AgentLoginDBHelper(this);
    Intent intentLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_sign_up);

        intentLogin = new Intent(this, AgentLoginActivity.class);

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
                    Toast.makeText(AgentSignUpActivity.this, "Enter in all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(passwordText.equals(rePasswordText)) {
                        if(!agentLoginDBHelper.usernameCheck(usernameText)) {
                            if(passwordText.length() >= 8) {
                                if(agentLoginDBHelper.insertUser(usernameText, passwordText)) {
                                    Toast.makeText(AgentSignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    startActivity(intentLogin);
                                    finish();
                                }
                                else
                                    Toast.makeText(AgentSignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(AgentSignUpActivity.this, "Password must be minimum 8 characters", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AgentSignUpActivity.this, "User already exists, enter username uniquely", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(AgentSignUpActivity.this, "Passwords should match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}