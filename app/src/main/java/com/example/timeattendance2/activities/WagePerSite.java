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
import com.example.timeattendance2.model.DoWageResponse;
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

public class WagePerSite extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button confirmBtn, backBtn, dateSelector;
    Spinner spinner;
    //
    Sites[] getSites;

    //data for request
    String token;
    int siteIndex;
    float fromTime, toTime, request_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wage_per_site);

        confirmBtn = findViewById(R.id.confirmBtn);
        dateSelector = findViewById(R.id.dateSelector);
        spinner = findViewById(R.id.selectUnitSpn);

        Intent getData = getIntent();
        request_id = getData.getFloatExtra("request_id", 0);
        token = getData.getStringExtra("token");
        getSites = (Sites[]) getData.getSerializableExtra("getSites");

        unitList();
        backBtn();

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

        Call<DoWageResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .doWage(token, siteIndex, fromTime, toTime, request_id);

        call.enqueue(new Callback<DoWageResponse>() {
            @Override
            public void onResponse(Call<DoWageResponse> call, Response<DoWageResponse> response) {

                DoWageResponse r = response.body();
                if (r != null) {
                    if (r.isCompleted()) {
                        String url = r.getWageUrl();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    } else {
                        Toast.makeText(WagePerSite.this, r.getError_message(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DoWageResponse> call, Throwable t) {

            }
        });
    }

    public void unitList() {

        List<Sites> sitesList = new ArrayList<>();
        Collections.addAll(sitesList, getSites);

        ArrayAdapter<Sites> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sitesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void backBtn() {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> dashboardAdmin(request_id, token, getSites));
    }

    public void dashboardAdmin(float request_id, String token, Sites[] getSites) {
        Intent intent = new Intent(this, DashboardAdmin.class);
        intent.putExtra("token", token);
        intent.putExtra("getSites", getSites);
        intent.putExtra("request_id", request_id);
        startActivity(intent);
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