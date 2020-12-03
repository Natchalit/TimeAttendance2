package com.example.timeattendance2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.timeattendance2.R;

public class DataLog extends AppCompatActivity {

    DatePicker datePicker;
    TextView showDate;
    Button confirmBtn, backBtn, getDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_log);

//        setGetDate();
        backBtn();
    }

    public void backBtn() {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardAdmin();
            }
        });
    }

    public void dashboardAdmin() {
        Intent intent = new Intent(this, DashboardAdmin.class);
        startActivity(intent);
    }

    public void setGetDate(){


        datePicker = findViewById(R.id.datePicker);
        getDate = findViewById(R.id.getDate);
        showDate = findViewById(R.id.showDate);
        getDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate.setText("Selected Date "+datePicker.getDayOfMonth()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getYear());
            }
        });

    }
}