package com.example.timeattendance2.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.scales.DateTime;
import com.example.timeattendance2.R;
import com.example.timeattendance2.api.RetrofitClient;
import com.example.timeattendance2.model.LogResponse;
import com.example.timeattendance2.model.Sites;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataLog extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatePicker datePicker;
    TextView showDate;
    Button confirmBtn, backBtn, getDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_log);

        Intent getData = getIntent();
        float request_id = getData.getFloatExtra("request_id", 0);
        String token = getData.getStringExtra("token");
        Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");

//        setGetDate();
        unitList();
//        confirmBtn(token);
        backBtn(request_id,token,getSites);
    }

//    private void confirmBtn(String token) {
//        confirmBtn = (Button) findViewById(R.id.confirmBtn);
//
//        confirmBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent getData = getIntent();
//                float request_id = getData.getFloatExtra("request_id", 0);
//
//                Spinner spinner = (Spinner) findViewById(R.id.selectUnitSpn);
//                Sites site = (Sites) spinner.getSelectedItem();
//
//                Call<LogResponse> call = RetrofitClient
//                        .getInstance()
//                        .getApi()
//                        .doLog(token, site.getIndex(), , , request_id);
//
//                call.enqueue(new Callback<LogResponse>() {
//                    @Override
//                    public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
//
//                        LogResponse logResponse = response.body();
//
//                        if (logResponse.isCompleted()) {
//                            String logUrl = logResponse.getLoUrl();
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setData(Uri.parse(logUrl));
//                            startActivity(intent);
//
//                        } else {
//                            Toast.makeText(DataLog.this, logResponse.getError_message(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<LogResponse> call, Throwable t) {
//
//                    }
//                });
//            }
//
//
//        });
//    }

    public void backBtn(float request_id, String token, Sites[] getSites) {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardAdmin(request_id,token,getSites);
            }
        });
    }

    public void dashboardAdmin(float request_id, String token, Sites[] getSites) {
        Intent intent = new Intent(this, DashboardAdmin.class);
        intent.putExtra("token", token);
        intent.putExtra("getSites", getSites);
        intent.putExtra("request_id", request_id);
        startActivity(intent);
    }

    public void unitList() {

        Spinner spinner = findViewById(R.id.selectUnitSpn);

        Intent getData = getIntent();
        Sites[] sites = (Sites[]) getData.getSerializableExtra("getSites");

        List<Sites> sitesList = new ArrayList<>();
        for (int i = 0; i < sites.length; i++) {
            sitesList.add(sites[i]);
        }

        ArrayAdapter<Sites> adapter = new ArrayAdapter<Sites>(this, android.R.layout.simple_spinner_item, sitesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void setGetDate() {

//        getDate = findViewById(R.id.getDate);
//        showDate = findViewById(R.id.showDate);
        datePicker = findViewById(R.id.datePicker);
        getDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dates = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();

                showDate.setText(dates);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}