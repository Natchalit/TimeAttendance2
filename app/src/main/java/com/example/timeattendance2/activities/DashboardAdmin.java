package com.example.timeattendance2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.anychart.AnyChartView;
import com.example.timeattendance2.R;
import com.example.timeattendance2.model.Sites;

public class DashboardAdmin extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        Intent getData = getIntent();
        float request_id = getData.getFloatExtra("request_id", 0);
        String token = getData.getStringExtra("token");
        Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");

        dropDownConfig();
        dropDownReport();
        backBtn(request_id,token,getSites);
    }

    public void backBtn(float request_id, String token, Sites[] getSites) {
        signOutBtn = findViewById(R.id.logOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenus(request_id,token,getSites);
            }
        });
    }

    public void mainMenus(float request_id, String token, Sites[] getSites) {
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("token",token);
        intent.putExtra("request_id",request_id);
        intent.putExtra("getSites",getSites);
        startActivity(intent);
    }

    public void dropDownConfig() {
        Spinner spinnerConfig = findViewById(R.id.configList);
        ArrayAdapter<CharSequence> adapterConfig = ArrayAdapter.createFromResource(this, R.array.configs, android.R.layout.simple_spinner_item);
        adapterConfig.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConfig.setAdapter(adapterConfig);
        spinnerConfig.setOnItemSelectedListener(this);
    }

    public void dropDownReport() {
        Spinner spinnerReport = findViewById(R.id.reportList);
        ArrayAdapter<CharSequence> adapterReport = ArrayAdapter.createFromResource(this, R.array.reports, android.R.layout.simple_spinner_item);
        adapterReport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReport.setAdapter(adapterReport);
        spinnerReport.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        ArrayAdapter<CharSequence> adapterDaily;
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

        if (parent.getItemAtPosition(position).equals("Payroll")) {

            Intent getData = getIntent();
            float request_id = getData.getFloatExtra("request_id", 0);
            String token = getData.getStringExtra("token");
            Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");

            Intent intent = new Intent(this, Payroll.class);
            intent.putExtra("token", token);
            intent.putExtra("getSites", getSites);
            intent.putExtra("request_id", request_id);
            startActivity(intent);
        }

        if (parent.getItemAtPosition(position).equals("Image")) {

            Intent getData = getIntent();
            float request_id = getData.getFloatExtra("request_id", 0);
            String token = getData.getStringExtra("token");
            Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");

            Intent intent = new Intent(this, ImageDaily.class);
            intent.putExtra("token", token);
            intent.putExtra("getSites", getSites);
            intent.putExtra("request_id", request_id);
            startActivity(intent);
        }

        if (parent.getItemAtPosition(position).equals("Data Log")) {

            Intent getData = getIntent();
            float request_id = getData.getFloatExtra("request_id", 0);
            String token = getData.getStringExtra("token");
            Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");

            Intent intent = new Intent(this, DataLog.class);
            intent.putExtra("token", token);
            intent.putExtra("getSites", getSites);
            intent.putExtra("request_id", request_id);
            startActivity(intent);
        }

        if (parent.getItemAtPosition(position).equals("Wage per site")) {
            Intent intent = new Intent(this, WagePerSite.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}