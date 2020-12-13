package com.example.timeattendance2.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.timeattendance2.R;
import com.example.timeattendance2.api.RetrofitClient;
import com.example.timeattendance2.model.LogResponse;
import com.example.timeattendance2.model.Sites;
import com.example.timeattendance2.utils.OleAutomationDateUtil;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataLog extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button confirmBtn, backBtn, dateSelector;
    Spinner spinner;
    //
    Sites[] getSites;

    //Data for request
    //token, siteIndex, fromTime, toTime, request_id
    float request_id, fromTime, toTime;
    String token;
    int siteIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_log);

        Intent getData = getIntent();
        request_id = getData.getFloatExtra("request_id", 0);
        token = getData.getStringExtra("token");
        getSites = (Sites[]) getData.getSerializableExtra("getSites");

        unitList();
        backBtn = findViewById(R.id.backBtn);
        spinner = findViewById(R.id.selectUnitSpn);
        confirmBtn = findViewById(R.id.confirmBtn);
        dateSelector = findViewById(R.id.dateSelector);

        backBtn.setOnClickListener(v -> dashboardAdmin(request_id, token, getSites));

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
        picker.show(this.getSupportFragmentManager(), picker.toString());
        picker.addOnCancelListener(v -> {
            Log.d("DatePicker Activity", "Dialog was cancelled");
            confirmBtn.setEnabled(false);
        });
        picker.addOnNegativeButtonClickListener(v -> {
            Log.d("DatePicker Activity", "Dialog Negative Button was clicked");
            confirmBtn.setEnabled(false);
        });
        picker.addOnPositiveButtonClickListener(v -> {
            Log.d("DatePicker Activity", "Date String = " + picker.getHeaderText() + " Date epoch " + v.first + " to " + v.second);

            confirmBtn.setEnabled(true);
            try {
                if (v.first != null && v.second != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(v.first);
                    Date aTime = cal.getTime();
                    cal.setTimeInMillis(v.second);
                    Date bTime = cal.getTime();
                    String a = OleAutomationDateUtil.convertToOADate(aTime);
                    String b = OleAutomationDateUtil.convertToOADate(bTime);
                    fromTime = Float.parseFloat(a);
                    toTime = Float.parseFloat(b);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        });

        confirmBtn.setOnClickListener(v -> {
            Sites site = (Sites) spinner.getSelectedItem();
            siteIndex = site.getIndex();
            callApi();
        });

        dateSelector.setOnClickListener(v -> picker.show(this.getSupportFragmentManager(), picker.toString()));


    }

    private void callApi() {

        Call<LogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .doLog(token, siteIndex, fromTime, toTime, request_id);

        call.enqueue(new Callback<LogResponse>() {
            @Override
            public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {

                LogResponse logResponse = response.body();
                if (logResponse != null) {
                    if (logResponse.isCompleted()) {
                        String logUrl = logResponse.getLogUrl();
                        Toast.makeText(DataLog.this, logUrl, Toast.LENGTH_LONG).show();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(logUrl));
                        startActivity(browserIntent);
                    } else {
                        Toast.makeText(DataLog.this, logResponse.getError_message(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LogResponse> call, Throwable t) {

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
        Collections.addAll(sitesList, sites);

        ArrayAdapter<Sites> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sitesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
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