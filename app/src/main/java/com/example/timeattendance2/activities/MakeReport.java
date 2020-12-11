package com.example.timeattendance2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.timeattendance2.R;
import com.example.timeattendance2.model.Sites;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.shuhart.stepview.StepView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MakeReport extends AppCompatActivity {

    StepView stepView;
    Button confirmBtn, backBtn;
    TextView resetTV, makeReportTv;
    EditText editTextDateRange;
    int step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_report);

        Intent getData = getIntent();
        float request_id = getData.getFloatExtra("request_id", 0);
        String token = getData.getStringExtra("token");
        Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");

        makeReportTv = findViewById(R.id.makeReportTv);
        editTextDateRange = findViewById(R.id.editTextDateRange);
        editTextDateRange.setInputType(InputType.TYPE_NULL);

        selectDateRange();

        confirmBtn();
        resetTVBtn();
        stepView();
        backBtn(request_id, token, getSites);

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


    public void confirmBtn() {
        confirmBtn = findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StepView stepView = findViewById(R.id.stepView);
                step = step + 1;
                stepView.go(step, true);

                if (step == 1) {
                    editTextDateRange.setEnabled(false);
                    makeReportTv.setText("Make Report 2");
                } else if (step == 2) {
                    resetTV.setEnabled(false);
                    resetTV.setVisibility(View.INVISIBLE);
                    makeReportTv.setText("Make Report 3");

                }
            }
        });
    }

    public void resetTVBtn() {
        resetTV = findViewById(R.id.resetTv);
        resetTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step = 0;
                StepView stepView = findViewById(R.id.stepView);
                stepView.go(step, true);
                makeReportTv.setText("Make Report");
                editTextDateRange.setEnabled(true);
                editTextDateRange.setText("Select Date");


            }
        });
    }

    public void backBtn(float request_id, String token, Sites[] getSites) {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payRoll(request_id, token, getSites);
            }
        });
    }

    public void stepView() {
        stepView = findViewById(R.id.stepView);
        stepView.getState()
                .selectedTextColor(ContextCompat.getColor(this, R.color.hardBlue))
                .animationType(StepView.ANIMATION_CIRCLE)
                .selectedCircleColor(ContextCompat.getColor(this, R.color.hardBlue))
                .selectedCircleRadius(getResources().getDimensionPixelSize(R.dimen.dp14))
                .selectedStepNumberColor(ContextCompat.getColor(this, R.color.softBlue))
                // You should specify only stepsNumber or steps array of strings.
                // In case you specify both steps array is chosen.
                .steps(new ArrayList<String>() {{
                    add("First step");
                    add("Second step");
                    add("Third step");
                }})
                // You should specify only steps number or steps array of strings.
                // In case you specify both steps array is chosen.
                .stepsNumber(3)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .stepLineWidth(getResources().getDimensionPixelSize(R.dimen.dp1))
                .textSize(getResources().getDimensionPixelSize(R.dimen.sp14))
                .stepNumberTextSize(getResources().getDimensionPixelSize(R.dimen.sp16))
                // other state methods are equal to the corresponding xml attributes
                .commit();
    }

    public void payRoll(float request_id, String token, Sites[] getSites) {
        Intent intent = new Intent(this, Payroll.class);
        intent.putExtra("token", token);
        intent.putExtra("request_id", request_id);
        intent.putExtra("getSites", getSites);
        startActivity(intent);
    }
}