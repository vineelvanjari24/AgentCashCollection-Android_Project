package com.example.agentcashcollection.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agentcashcollection.R;
import com.example.agentcashcollection.dbhelper.SavingAccDBHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DownloadCustomer extends AppCompatActivity {

    EditText editTextIPAddress;
    private BufferedReader bufferedReader;
    private Socket socket;
    boolean connectionStatus = false;
    String receivedMessage = "";
    ArrayList<String> cusDataDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_customer);

        editTextIPAddress = findViewById(R.id.editTextIPAddress);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ipAdd = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        editTextIPAddress.setText(ipAdd);
        findViewById(R.id.connectServer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddressDevice = editTextIPAddress.getText().toString();
                new ConnectTask().execute(ipAddressDevice);
            }
        });

        findViewById(R.id.downloadCustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionStatus) {
                    if(!receivedMessage.equals("")) {
                        StringTokenizer customerData = new StringTokenizer(receivedMessage, ",");
                        cusDataDownload = new ArrayList<>();
                        while(customerData.hasMoreTokens()) {
                            String data = customerData.nextToken();
                            Log.d("CusData", data);
                            cusDataDownload.add(data);
                        }
                        SavingAccDBHelper savingAccDBHelper = new SavingAccDBHelper(DownloadCustomer.this);

                        long accNum = Long.parseLong(cusDataDownload.get(0));
                        String name = cusDataDownload.get(1);
                        long accBalance = Long.parseLong(cusDataDownload.get(2));
                        long phoneNum = Long.parseLong(cusDataDownload.get(3));
                        String address = cusDataDownload.get(4);

                        Log.d("CusData", phoneNum+"");

                        Boolean insertStatus = savingAccDBHelper.insertRows(accNum, name, accBalance, phoneNum, address);
                        if(insertStatus)
                            Toast.makeText(DownloadCustomer.this, "Insertion of Data Successful", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(DownloadCustomer.this, "Insertion of Data Unsuccessful!!!", Toast.LENGTH_SHORT).show();

                        //savingAccDBHelper.close();
                    }
                    else Toast.makeText(DownloadCustomer.this, "No Data Received", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(DownloadCustomer.this, "Server not connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DownloadCustomer.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ConnectTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String ipAddress = params[0];
            try {
                socket = new Socket(ipAddress, 8080);
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                displayToast("Connected to Server: " + ipAddress);
                connectionStatus = true;
                startReceivingMessages();
            } catch (IOException e) {
                e.printStackTrace();
                displayToast("Error: " + e.getMessage());
            }
            return null;
        }
    }

    private void startReceivingMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String message;
                    while ((message = bufferedReader.readLine()) != null) {
                        receivedMessage = message;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    displayToast("Error: " + e.getMessage());
                }
            }
        }).start();
    }
}