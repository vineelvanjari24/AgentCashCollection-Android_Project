package com.example.agentcashcollection.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agentcashcollection.R;
import com.example.agentcashcollection.dbhelper.SavingAccDBHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DownloadCustomer extends AppCompatActivity {
    TextView statusTextView, ipAddressTextView;
    Button startServerBtn, downloadCustomerBtn;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Handler handler = new Handler();
    boolean isDeviceConfirmed = false;
    String deviceIPAddress;
    ArrayList<String> messagesData;
    int insStartIndex = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_customer);

        messagesData = new ArrayList<>();

        startServerBtn = findViewById(R.id.startServer);
        statusTextView = findViewById(R.id.statusTextView);
        ipAddressTextView = findViewById(R.id.ipAddress);
        downloadCustomerBtn = findViewById(R.id.downloadCustomer);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        deviceIPAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        
        ipAddressTextView.setText("My Device IP Address:-"+deviceIPAddress);
        startServerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startServer();
            }
        });

        downloadCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (messagesData.size() >= 4) {
                        for (int i = insStartIndex++; i < messagesData.size(); i++) {
                            StringTokenizer customerData = new StringTokenizer(messagesData.get(i), "^");
                            ArrayList<String> cusDataDownload = new ArrayList<>();
                            while (customerData.hasMoreTokens()) {
                                String data = customerData.nextToken();
                                cusDataDownload.add(data);
                            }
                            if (cusDataDownload.size() >= 5) {
                                SavingAccDBHelper savingAccDBHelper = new SavingAccDBHelper(DownloadCustomer.this);

                                long accNum = Long.parseLong(cusDataDownload.get(4));
                                String name = cusDataDownload.get(1);
                                long accBalance = Long.parseLong(cusDataDownload.get(8));
                                long phoneNum = Long.parseLong(cusDataDownload.get(3));
                                String address = cusDataDownload.get(2);

                                if (savingAccDBHelper.insertRows(accNum, name, accBalance, phoneNum, address))
                                    Toast.makeText(DownloadCustomer.this, "Insertion of Data Successful", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(DownloadCustomer.this, "Insertion of Data Unsuccessful!!!", Toast.LENGTH_SHORT).show();

                                savingAccDBHelper.close();
                            } else
                                Toast.makeText(DownloadCustomer.this, "Insertion of data cannot be completed!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(DownloadCustomer.this, "No insertion data received yet", Toast.LENGTH_SHORT).show();
                }
        });
    }

    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(12345);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            statusTextView.setText("Waiting for PC to connect...");
                        }
                    });

                    clientSocket = serverSocket.accept();
                    reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    writer = new PrintWriter(clientSocket.getOutputStream(), true);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            statusTextView.setText("PC connected!");
                            startServerBtn.setEnabled(false);
                        }
                    });

                    // Start listening for messages from PC
                    String message;
                    while ((message = reader.readLine()) != null) {
                        String finalMessage = message;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(!checkDataExists(finalMessage)) {
                                    if(finalMessage.equals("")) {
                                        Toast.makeText(DownloadCustomer.this, "Null data received", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        if (isDeviceConfirmed) {
                                            Dialog dialog = new Dialog(DownloadCustomer.this);
                                            dialog.setContentView(R.layout.popup_window_confirmation);
                                            TextView textViewMsg = dialog.findViewById(R.id.receivedMsg);
                                            textViewMsg.setText(finalMessage);
                                            dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    messagesData.add(finalMessage);
                                                    sendMessage("1");
                                                    dialog.dismiss();
                                                }
                                            });

                                            dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            dialog.setCanceledOnTouchOutside(false);
                                            dialog.show();
                                        }
                                        else if(finalMessage.equals("$123!")) {
                                            sendMessage("1;ok");
                                            Toast.makeText(DownloadCustomer.this, "Device confirmed", Toast.LENGTH_SHORT).show();
                                            isDeviceConfirmed = true;
                                        }
                                        else
                                            Toast.makeText(DownloadCustomer.this, "Device verification failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                    Toast.makeText(DownloadCustomer.this, "Current data sent already received...", Toast.LENGTH_SHORT).show();
                            }

                            boolean checkDataExists(String data) {
                                boolean check = false;
                                for (String str:messagesData) {
                                    if(str.equals(data)) {
                                        check = true;
                                        break;
                                    }
                                }

                                return check;
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            statusTextView.setText("Error starting server");
                        }
                    });
                }
            }
        }).start();
    }

    private void sendMessage(String msg) {
        final String message = msg;

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (writer != null) {
                    writer.println(message);
                }
                return null;
            }
        }.execute();
    }
}
