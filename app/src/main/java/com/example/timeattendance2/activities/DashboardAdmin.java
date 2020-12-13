package com.example.timeattendance2.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timeattendance2.R;
import com.example.timeattendance2.model.Sites;

public class DashboardAdmin extends AppCompatActivity {

    Button signOutBtn;
    Context thisContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        Intent getData = getIntent();
        float request_id = getData.getFloatExtra("request_id", 0);
        String token = getData.getStringExtra("token");
        Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");
        thisContext = this;

        dropDownConfig();
        dropDownReport();
        backBtn(request_id, token, getSites);
    }

    public void backBtn(float request_id, String token, Sites[] getSites) {
        signOutBtn = findViewById(R.id.logOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenus(request_id, token, getSites);
            }
        });
    }

    public void mainMenus(float request_id, String token, Sites[] getSites) {
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("token", token);
        intent.putExtra("request_id", request_id);
        intent.putExtra("getSites", getSites);
        startActivity(intent);
    }

    public void dropDownConfig() {
        Spinner spinnerConfig = findViewById(R.id.configList);
        ArrayAdapter<CharSequence> adapterConfig = ArrayAdapter.createFromResource(this, R.array.configs, android.R.layout.simple_spinner_item);
        adapterConfig.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConfig.setAdapter(adapterConfig);
        //spinnerConfig.setOnItemSelectedListener(() -> {});
    }

    public void dropDownReport() {
        Spinner spinnerReport = findViewById(R.id.reportList);
        ArrayAdapter<CharSequence> adapterReport = ArrayAdapter.createFromResource(this, R.array.reports, android.R.layout.simple_spinner_item);
        adapterReport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReport.setAdapter(adapterReport);
        spinnerReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Intent getData = getIntent();
                float request_id = getData.getFloatExtra("request_id", 0);
                String token = getData.getStringExtra("token");
                Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");
                if (position == 1) { //data log
                    Intent intentD = new Intent(thisContext, DataLog.class);
                    intentD.putExtra("token", token);
                    intentD.putExtra("getSites", getSites);
                    intentD.putExtra("request_id", request_id);
                    startActivity(intentD);
                } else if (position ==2) { // images
                    Intent intentI = new Intent(thisContext, ImageDaily.class);
                    intentI.putExtra("token", token);
                    intentI.putExtra("getSites", getSites);
                    intentI.putExtra("request_id", request_id);
                    startActivity(intentI);
                } else if (position == 3) { // pay roll
                    Intent intentP = new Intent(thisContext, Payroll.class);
                    intentP.putExtra("token", token);
                    intentP.putExtra("getSites", getSites);
                    intentP.putExtra("request_id", request_id);
                    startActivity(intentP);
                } else if (position == 4) { // wage per site
                    Intent intent = new Intent(thisContext, WagePerSite.class);
                    intent.putExtra("token", token);
                    intent.putExtra("getSites", getSites);
                    intent.putExtra("request_id", request_id);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
    }


}