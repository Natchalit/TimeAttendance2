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

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        unitList();
        backBtn();
        conFirmBth();

    }

    public void backBtn() {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardAdmin();
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.units, android.R.layout.simple_spinner_item);
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

    public void dashboardAdmin() {
        Intent intent = new Intent(this, DashboardAdmin.class);
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