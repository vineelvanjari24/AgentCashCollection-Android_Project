package com.example.agentcashcollection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.agentcashcollection.dbhelper.CusDetailsDBHelper;
import com.example.agentcashcollection.dbhelper.SavingAccDBHelper;

public class SplashScreenActivity extends AppCompatActivity {

    Button begin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        CusDetailsDBHelper dataBaseHelper = new CusDetailsDBHelper(this);
        dataBaseHelper.insertRows( 1001,"test1",50000, 9138461494L, "L.B. Nagar");
        dataBaseHelper.insertRows( 1002,"test2",100000,  7248923112L, "Dilsuknagar");
        dataBaseHelper.insertRows( 1003,"test3",75000, 6138461494L, "Koti");
        dataBaseHelper.insertRows( 1004,"test4",5000, 7447983978L, "Medhipatnam");
        dataBaseHelper.insertRows( 1005,"test5",30000, 9945031564L, "yosufguda");
        dataBaseHelper.insertRows( 1006,"test6",56500, 8879031999L, "manojNagar");
        dataBaseHelper.insertRows( 1007,"test7",23400, 8896781595L, "Ramanthapur");
        dataBaseHelper.insertRows( 1008,"test8",40000, 6703127882L, "vanasthalipuram");
        dataBaseHelper.insertRows( 1009,"test9",32000, 9948456788L, "ghatkesar");
        dataBaseHelper.insertRows( 1010,"test10",44000, 9957839232L, "uppal");

        dataBaseHelper.insertRows( 1011,"test11",50000, 9138453494L, "L.B. Nagar");
        dataBaseHelper.insertRows( 1012,"test12",100000,  7008923112L, "Dilsuknagar");
        dataBaseHelper.insertRows( 1013,"test13",75000, 61384121494L, "Koti");
        dataBaseHelper.insertRows( 1014,"test14",5000, 7447912978L, "Medhipatnam");
        dataBaseHelper.insertRows( 1015,"test15",30000, 6645031564L, "yosufguda");
        dataBaseHelper.insertRows( 1016,"test16",56500, 8879001799L, "manojNagar");
        dataBaseHelper.insertRows( 1017,"test17",23400, 84566781595L, "Ramanthapur");
        dataBaseHelper.insertRows( 1018,"test18",40000, 6703127682L, "vanasthalipuram");
        dataBaseHelper.insertRows( 1019,"test19",32000, 9900056788L, "ghatkesar");
        dataBaseHelper.insertRows( 1020,"test20",44000, 7057839232L, "uppal");

        SavingAccDBHelper savingAccDBHelper = new SavingAccDBHelper(this);
        savingAccDBHelper.insertRows(12345, "test", 50000, 7384614453L, "Vanasthalipuram");
        savingAccDBHelper.insertRows(123456, "test2", 100000, 6110023239L, "Saroornagar");

        begin = findViewById(R.id.button);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDeviceInfo = new Intent(getApplicationContext(), DeviceStartingPage.class);
                startActivity(intentDeviceInfo);
                finish();
            }
        });
    }
}