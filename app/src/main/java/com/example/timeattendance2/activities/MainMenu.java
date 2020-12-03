package com.example.timeattendance2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
//        adminMenuBtn(request_id);
    }

    public void checkInBtn(String token, Sites[] sites, float request_id) {
        checkInBtn = findViewById(R.id.checkInBtn);
        checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCheckInPage(token, sites, request_id);
            }
        });
    }



    public void checkOutBtn(String token, Sites[] sites, float request_id) {
        checkOutBtn = findViewById(R.id.checkOutBtn);
        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCheckOutPage(token,sites,request_id);
            }
        });
    }

    public void adminMenuBtn(float request_id) {
        adminMenuBtn = findViewById(R.id.adminMenuBtn);
        adminMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Request", "Request id" + request_id);
//                adminLogin();
            }
        });
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

    public void adminLogin() {
        Intent intent = new Intent(this, LoginAdmin.class);
        startActivity(intent);
    }

}