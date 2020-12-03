package com.example.timeattendance2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.timeattendance2.R;
import com.example.timeattendance2.model.Sites;

import java.util.ArrayList;
import java.util.List;

public class CheckoutPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button nextBtn, backBtn;
    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_page);
        Intent getData = getIntent();
        float request_id = getData.getFloatExtra("request_id", 0);
        String token = getData.getStringExtra("token");
        Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");

        locationGet();
        backBtn(token,request_id,getSites);
        nextBtn(token,request_id,getSites);
        unitList();
    }

    public void locationGet() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if ((ContextCompat.checkSelfPermission(CheckoutPage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                ContextCompat.checkSelfPermission(CheckoutPage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CheckoutPage.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

    }

    public void backBtn(String token, float request_id, Sites[] getSites) {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenus(token,request_id,getSites);
            }
        });
    }

    public void nextBtn(String token, float request_id, Sites[] getSites) {
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner spinner = (Spinner) findViewById(R.id.selectUnitSpn);
                Sites site = (Sites) spinner.getSelectedItem();

                captureCheckOut(token,request_id,site,getSites);
            }
        });
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

    public void mainMenus(String token, float request_id, Sites[] getSites) {
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("token",token);
        intent.putExtra("request_id",request_id);
        intent.putExtra("getSites",getSites);
        startActivity(intent);
    }

    public void captureCheckOut(String token, float request_id, Sites site, Sites[] getSites) {
        Intent intent = new Intent(this, CaptureCheckout.class);
        intent.putExtra("token",token);
        intent.putExtra("request_id",request_id);
        intent.putExtra("site", site);
        intent.putExtra("getSites",getSites);
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