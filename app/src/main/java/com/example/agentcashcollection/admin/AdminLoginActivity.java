package com.example.agentcashcollection.admin;

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
import com.example.agentcashcollection.dbhelper.AdminLoginDBHelper;

public class AdminLoginActivity extends AppCompatActivity {

    Button login;
    TextView signup;
    EditText username, password;
    AdminLoginDBHelper adminLoginDBHelper = new AdminLoginDBHelper(this);
    Intent intentAdminMain, intentSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        intentAdminMain = new Intent(getApplicationContext(), AdminMainActivity.class);
        intentSignUp = new Intent(getApplicationContext(), AdminSignUpActivity.class);

        login = findViewById(R.id.login);
        signup = findViewById(R.id.textView);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentSignUp);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();

                if(usernameText.equals("") || passwordText.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter in all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(adminLoginDBHelper.usernameCheck(usernameText)) {
                        if(adminLoginDBHelper.usernamePasswordCheck(usernameText, passwordText)) {
                            SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor ed = sp.edit();
                            ed.putBoolean("flag", true);
                            ed.putString("user", "admin");
                            ed.apply();

                            startActivity(intentAdminMain);
                            new LoginDashBoard().finish();
                            finish();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Password didn't match", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No user exits with the username you entered", Toast.LENGTH_SHORT).show();
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