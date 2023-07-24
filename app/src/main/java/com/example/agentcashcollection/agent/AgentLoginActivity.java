package com.example.agentcashcollection.agent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agentcashcollection.LoginDashBoard;
import com.example.agentcashcollection.R;
import com.example.agentcashcollection.dbhelper.AgentLoginDBHelper;

public class AgentLoginActivity extends AppCompatActivity {

    Button login;
    EditText username, password;
    AgentLoginDBHelper agentDBHelper = new AgentLoginDBHelper(this);
    Intent intentMainActivity, intentSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_login);

        intentMainActivity = new Intent(AgentLoginActivity.this, MainActivity.class);
        intentSignUp = new Intent(AgentLoginActivity.this, AgentSignUpActivity.class);

        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();

                if(usernameText.equals("") || passwordText.equals("")) {
                    Toast.makeText(AgentLoginActivity.this, "Please enter in all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(agentDBHelper.usernameCheck(usernameText)) {
                        if(agentDBHelper.usernamePasswordCheck(usernameText, passwordText)) {
                            SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor ed = sp.edit();
                            ed.putBoolean("flag", true);
                            ed.putString("user", "user");
                            ed.apply();

                            SharedPreferences spUsername = getSharedPreferences("Username", MODE_PRIVATE);
                            SharedPreferences.Editor edUsername = spUsername.edit();
                            edUsername.putString("username", usernameText);
                            edUsername.apply();

                            startActivity(intentMainActivity);
                            new LoginDashBoard().finish();
                            finish();
                        }
                        else
                            Toast.makeText(AgentLoginActivity.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(AgentLoginActivity.this, "No user exits with the username you entered", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        findViewById(R.id.goBackToLoginDashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginDashBoard.class));
                finish();
            }
        });
    }
}