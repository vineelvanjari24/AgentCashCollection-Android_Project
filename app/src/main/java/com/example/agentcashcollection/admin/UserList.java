package com.example.agentcashcollection.admin;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.agentcashcollection.R;
import com.example.agentcashcollection.agent.AgentLoginActivity;
import com.example.agentcashcollection.dbhelper.AgentLoginDBHelper;

public class UserList extends AppCompatActivity {

    private TableLayout tableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        AgentLoginDBHelper db=new AgentLoginDBHelper(this);
        Cursor cursor=db.GetAll();


        tableLayout = findViewById(R.id.table_layout);
        if (cursor.moveToFirst()) {
            TableRow headerRow = new TableRow(this);

            for (int i = 0; i < cursor.getColumnCount(); i++) {
                TextView headerTextView = new TextView(this);
                //headerTextView.setPadding(8,8,8,8);
                headerTextView.setText(String.format(cursor.getColumnName(i)));
                headerTextView.setTextAppearance(R.style.boldText);
                headerRow.addView(headerTextView);
            }

            tableLayout.addView(headerRow);

            do {
                TableRow dataRow = new TableRow(this);

                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    TextView dataTextView = new TextView(this);

                    //  dataTextView.setPadding(8,8,8,8);
                    dataTextView.setText(cursor.getString(i));
                    dataRow.addView(dataTextView);
                }

                tableLayout.addView(dataRow);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }


}