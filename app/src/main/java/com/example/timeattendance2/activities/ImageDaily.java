package com.example.timeattendance2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.timeattendance2.R;
import com.example.timeattendance2.model.Sites;

import java.util.ArrayList;
import java.util.List;

public class ImageDaily extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.application.example.EXTRA_TEXT2";

    Button backBtn;
    RadioButton radioButton;
    RadioGroup radioGroup;
    Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_daily);

        Intent getData = getIntent();
        float request_id = getData.getFloatExtra("request_id", 0);
        String token = getData.getStringExtra("token");
        Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        unitList();
        backBtn(request_id,token,getSites);
        conFirmBth();

    }

    public void backBtn(float request_id, String token, Sites[] getSites) {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardAdmin(request_id,token,getSites);
            }
        });
    }

    public void conFirmBth() {

        confirmBtn = findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                String  textRadio = radioButton.getText().toString();

                Spinner spinner = (Spinner) findViewById(R.id.selectUnitSpn);
                String textSpn = spinner.getSelectedItem().toString();

                if (!textSpn.equals("Select Unit")) {
                    imageThumbnailCheckIn(textRadio, textSpn);
                }
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

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);
        Toast.makeText(this, "Selected Radio Button" + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }

    public void imageThumbnailCheckIn(String textRadio, String textSpn) {
        Intent intent = new Intent(this, ImageDailyThumbnail.class);
        intent.putExtra(EXTRA_TEXT,textRadio);
        intent.putExtra(EXTRA_TEXT2,textSpn);
        startActivity(intent);
    }

    public void dashboardAdmin(float request_id, String token, Sites[] getSites) {
        Intent intent = new Intent(this, DashboardAdmin.class);
        intent.putExtra("token",token);
        intent.putExtra("request_id",request_id);
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