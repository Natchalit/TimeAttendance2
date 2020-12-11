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

import com.example.timeattendance2.R;
import com.example.timeattendance2.model.Sites;

public class Payroll extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button backBtn, confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll);

        Intent getData = getIntent();
        float request_id = getData.getFloatExtra("request_id", 0);
        String token = getData.getStringExtra("token");
        Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");

        Spinner spinner = findViewById(R.id.payrollList);

        backBtn(request_id, token, getSites);
        payrollList(spinner);
        conFirmBtn(spinner, request_id, token, getSites);
    }

    public void backBtn(float request_id, String token, Sites[] getSites) {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardAdmin(request_id, token, getSites);
            }
        });
    }

    public void conFirmBtn(Spinner spinner, float request_id, String token, Sites[] getSites) {
        confirmBtn = findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = spinner.getSelectedItem().toString();
                if (text.equals("Make report")) {
                    makeReport(request_id, token, getSites);
                }
            }
        });
    }

    public void payrollList(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.payrolls, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void makeReport(float request_id, String token, Sites[] getSites) {
        Intent intent = new Intent(this, MakeReport.class);
        intent.putExtra("token", token);
        intent.putExtra("getSites", getSites);
        intent.putExtra("request_id", request_id);
        startActivity(intent);
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