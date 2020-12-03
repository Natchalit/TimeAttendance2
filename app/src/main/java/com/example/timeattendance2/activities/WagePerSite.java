package com.example.timeattendance2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.timeattendance2.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;

public class WagePerSite extends AppCompatActivity {

    Button backBtn;
    EditText editTextDateRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wage_per_site);

        editTextDateRange = findViewById(R.id.editTextDateRange);
        editTextDateRange.setInputType(InputType.TYPE_NULL);

        selectDateRange();
        backBtn();
    }

    public void selectDateRange() {

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Date");
        final MaterialDatePicker materialDatePicker = builder.build();
        editTextDateRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                materialDatePicker.show(getSupportFragmentManager(), "Date_range");
//
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        Long startDate = selection.first;
                        Long endDate = selection.second;

                        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd MMM yyyy");

                        editTextDateRange.setText(simpleFormat.format(startDate) + " - " + simpleFormat.format(endDate));
                    }
                });
            }
        });
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

    public void dashboardAdmin() {
        Intent intent = new Intent(this, DashboardAdmin.class);
        startActivity(intent);
    }
}