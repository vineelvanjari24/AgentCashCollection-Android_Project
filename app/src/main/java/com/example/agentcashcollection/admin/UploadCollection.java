package com.example.agentcashcollection.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agentcashcollection.R;
import com.example.agentcashcollection.dbhelper.SavingReceiptDBHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class UploadCollection extends AppCompatActivity {

    private PrintWriter writer;
    Button connect, upload;
    TextView ipaddr;
    private BufferedReader reader;
    ArrayList<StringBuffer> str;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Handler handler = new Handler();
    int index = 0;
    String ip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_collection);
        SavingReceiptDBHelper sa = new SavingReceiptDBHelper(this);
        ipaddr = findViewById(R.id.ipaddr);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        ipaddr.setText("IP ADDRESS:-"+ip);
        str=sa.getDetails();
        connect = findViewById(R.id.Connect);
        upload = findViewById(R.id.Upload);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startServer();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str.size() > 0) {
                    new SendMessageTask().execute(String.valueOf(str.get(index)));
                    index++;
                } else
                    Toast.makeText(UploadCollection.this, "No data found to upload", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(UploadCollection.this, "Waiting for PC to connect...", Toast.LENGTH_SHORT).show();
                        }
                    });

                    clientSocket = serverSocket.accept();
                    reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    writer = new PrintWriter(clientSocket.getOutputStream(), true);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadCollection.this, "Connected", Toast.LENGTH_SHORT).show();
                            connect.setEnabled(false);
                            upload.setEnabled(true);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadCollection.this, "Error starting server", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }).start();
    }
    private class SendMessageTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... messages) {
            for (String message : messages) {
                if (writer != null) {
                    writer.println(message);
                }
            }
            return null;
        }
    }

}