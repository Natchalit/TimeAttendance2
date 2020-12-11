package com.example.timeattendance2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Locale;

public class CheckinPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button nextBtn, backBtn;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_page);

        Intent getData = getIntent();
        float request_id = getData.getFloatExtra("request_id", 0);
        String token = getData.getStringExtra("token");
        Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");

        locationGet();
//        next();
        backBtn(token,request_id,getSites);
        nextBtn(token,request_id,getSites);
        unitList();

    }

    private void next() {
        nextBtn = findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Spinner spinner = (Spinner) findViewById(R.id.selectUnitSpn);
//                Sites site = (Sites) spinner.getSelectedItem();
////                capture();
//                captureCheckIn(token,request_id,site,getSites);
            }
        });
    }

    private void capture() {
        Intent intent = new Intent(this, CaptureCheckin.class);
        startActivity(intent);
    }

    public void locationGet() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if ((ContextCompat.checkSelfPermission(CheckinPage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                ContextCompat.checkSelfPermission(CheckinPage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CheckinPage.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

//                Log.i("Lat", String.valueOf(location.getLatitude()));
//                Log.i("Long", String.valueOf(location.getLongitude()));

                try {
                    Geocoder geocoder = new Geocoder(CheckinPage.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            1
                    );
//                    String address = addresses.get(0).getAddressLine(0);
//                    Log.i("Address",address);
                } catch (Exception e){
                    e.printStackTrace();
                }
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

    public void backBtn(String token, float request_id, Sites[] sites) {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenus(token,request_id,sites);
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
                    captureCheckIn(token,request_id,site,getSites);
            }
        });

    }

    public void mainMenus(String token, float request_id, Sites[] getSites) {

        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("token",token);
        intent.putExtra("request_id",request_id);
        intent.putExtra("getSites",getSites);
        startActivity(intent);
    }

    public void captureCheckIn(String token, float request_id, Sites site, Sites[] getSites) {

        Intent intent = new Intent(this, CaptureCheckin.class);
        intent.putExtra("token",token);
        intent.putExtra("request_id",request_id);
        intent.putExtra("site", site);
        intent.putExtra("getSites",getSites);
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}