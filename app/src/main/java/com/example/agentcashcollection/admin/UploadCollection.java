package com.example.agentcashcollection.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agentcashcollection.R;
import com.example.agentcashcollection.dbhelper.SavingReceiptDBHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class UploadCollection extends AppCompatActivity {
    EditText ipaddr;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private Socket socket;
    Button connect,upload;
    ArrayList<StringBuffer> str;
    int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_collection);
        SavingReceiptDBHelper sa = new SavingReceiptDBHelper(this);
        ipaddr = findViewById(R.id.Ipaddr);
        connect = findViewById(R.id.Connect);
        upload = findViewById(R.id.Upload);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = String.valueOf(ipaddr.getText());
                new ConnectTask().execute(ip);
                str=sa.getDetails();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str.size()>0) {
                    new SendMessageTask().execute(String.valueOf(str.get(index)));
                    index++;
                }
                else Toast.makeText(UploadCollection.this, "No data found to upload", Toast.LENGTH_SHORT).show();
            }
        });

    }
        public class ConnectTask extends AsyncTask<String, String, Void> {

            @Override
            protected Void doInBackground(String... params) {
                String ipAddress = params[0];
                try {
                    socket = new Socket(ipAddress, 8080);
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    bufferedReader = new BufferedReader(inputStreamReader);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(UploadCollection.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        }

        private class SendMessageTask extends AsyncTask<String, Void, Void> {

            @Override
            protected Void doInBackground(String... messages) {
                for (String message : messages) {
                    if (printWriter != null) {
                        printWriter.println(message);
                    }
                }
                return null;
            }
        }

}


