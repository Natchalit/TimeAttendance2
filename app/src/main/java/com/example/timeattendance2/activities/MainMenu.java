package com.example.timeattendance2.activities;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timeattendance2.R;
import com.example.timeattendance2.model.Sites;

public class MainMenu extends AppCompatActivity {
    private Button checkInBtn;
    private Button checkOutBtn;
    private Button adminMenuBtn;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Intent getData = getIntent();
        float request_id = getData.getFloatExtra("request_id", 0);
        String token = getData.getStringExtra("token");
        Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");

        checkInBtn(token, getSites, request_id);
        checkOutBtn(token, getSites, request_id);
        adminMenuBtn(token, getSites, request_id);
    }

    public void checkInBtn(String token, Sites[] sites, float request_id) {
        checkInBtn = findViewById(R.id.checkInBtn);
        checkInBtn.setOnClickListener(v -> openCheckInPage(token, sites, request_id));
    }

    public void checkOutBtn(String token, Sites[] sites, float request_id) {
        checkOutBtn = findViewById(R.id.checkOutBtn);
        checkOutBtn.setOnClickListener(v -> openCheckOutPage(token,sites,request_id));
    }

    public void adminMenuBtn(String token, Sites[] getSites, float request_id) {
        adminMenuBtn = findViewById(R.id.adminMenuBtn);
        adminMenuBtn.setOnClickListener(v -> {
            Log.i("Request", "Request id" + request_id);
            dashBoardAdmin(token, getSites, request_id);
        });
    }

    private void dashBoardAdmin(String token, Sites[] getSites, float request_id) {
        Intent intent = new Intent(this, DashboardAdmin.class);
        intent.putExtra("token", token);
        intent.putExtra("getSites", getSites);
        intent.putExtra("request_id", request_id);
        startActivity(intent);
    }

    public void openCheckInPage(String token, Sites[] getSites, float request_id) {
        Intent intent = new Intent(this, CheckinPage.class);
        intent.putExtra("token", token);
        intent.putExtra("getSites", getSites);
        intent.putExtra("request_id", request_id);
        startActivity(intent);
    }

    public void openCheckOutPage(String token, Sites[] getSites, float request_id) {
        Intent intent = new Intent(this, CheckoutPage.class);
        intent.putExtra("token", token);
        intent.putExtra("getSites", getSites);
        intent.putExtra("request_id", request_id);
        startActivity(intent);
    }

}