package com.example.timeattendance2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timeattendance2.R;
import com.example.timeattendance2.model.Sites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckInOutPage extends AppCompatActivity {

    private Button nextBtn, backBtn;
    boolean isCheckin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_page);

        backBtn = findViewById(R.id.backBtn);
        nextBtn = findViewById(R.id.nextBtn);


        Intent getData = getIntent();
        float request_id = getData.getFloatExtra("request_id", 0);
        String token = getData.getStringExtra("token");
        Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");
        isCheckin = getData.getBooleanExtra("isCheckin", true);

        backBtn(token, request_id, getSites);
        nextBtn(token, request_id, getSites);
        unitList();

    }

    public void backBtn(String token, float request_id, Sites[] sites) {
        backBtn.setOnClickListener(v -> mainMenus(token, request_id, sites));
    }

    public void nextBtn(String token, float request_id, Sites[] getSites) {
        nextBtn.setOnClickListener(v -> {
            Spinner spinner = findViewById(R.id.selectUnitSpn);
            Sites site = (Sites) spinner.getSelectedItem();
            captureCheckIn(token, request_id, site, getSites);
        });

    }

    public void mainMenus(String token, float request_id, Sites[] getSites) {

        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("token", token);
        intent.putExtra("request_id", request_id);
        intent.putExtra("getSites", getSites);
        startActivity(intent);
    }

    public void captureCheckIn(String token, float request_id, Sites site, Sites[] getSites) {

        Intent intent = new Intent(this, CaptureCheckInOut.class);
        intent.putExtra("token", token);
        intent.putExtra("request_id", request_id);
        intent.putExtra("site", site);
        intent.putExtra("getSites", getSites);
        intent.putExtra("isCheckin", isCheckin);
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
    }

}