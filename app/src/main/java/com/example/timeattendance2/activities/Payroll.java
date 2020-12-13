package com.example.timeattendance2.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timeattendance2.R;
import com.example.timeattendance2.model.Sites;

public class Payroll extends AppCompatActivity {

    Button backBtn, confirmBtn;
    Spinner spinner;

    String savedUrl1 = null;
    String savedUrl2 = null;
    String savedUrl3 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll);

        Intent getData = getIntent();
        float request_id = getData.getFloatExtra("request_id", 0);
        String token = getData.getStringExtra("token");
        savedUrl1 = getData.getStringExtra("savedUrl1");
        savedUrl2 = getData.getStringExtra("savedUrl2");
        savedUrl3 = getData.getStringExtra("savedUrl3");
        Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");


        spinner = findViewById(R.id.payrollList);
        confirmBtn = findViewById(R.id.confirmBtn);

        backBtn(request_id, token, getSites);
        payrollList(spinner);
        conFirmBtn(spinner, request_id, token, getSites);
    }

    public void backBtn(float request_id, String token, Sites[] getSites) {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> dashboardAdmin(request_id, token, getSites));
    }

    public void conFirmBtn(Spinner spinner, float request_id, String token, Sites[] getSites) {
        confirmBtn.setOnClickListener(v -> {
            long id = spinner.getSelectedItemId();
            if (id == 0) GotoReport(savedUrl1);
            else if (id == 1) GotoReport(savedUrl2);
            else if (id == 2) GotoReport(savedUrl3);
            else makeReport(request_id, token, getSites);
        });
    }

    public void payrollList(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.payrolls, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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

    private void GotoReport(String file) {
        if (file != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(file));
            startActivity(browserIntent);
        } else {
            Toast.makeText(Payroll.this, "Can't find Report Url", Toast.LENGTH_SHORT).show();
        }
    }

}