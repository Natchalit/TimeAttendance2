package com.example.timeattendance2.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import com.example.timeattendance2.R;
import com.example.timeattendance2.api.RetrofitClient;
import com.example.timeattendance2.model.DoReportResponse1;
import com.example.timeattendance2.model.DoReportResponse2;
import com.example.timeattendance2.model.DoReportResponse3;
import com.example.timeattendance2.model.Sites;
import com.example.timeattendance2.utils.OleAutomationDateUtil;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.shuhart.stepview.StepView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeReport extends AppCompatActivity {

    StepView stepView;
    Button confirmBtn, backBtn, dateSelector;
    TextView resetTV, makeReportTv;
    int step = 0;
    //
    Sites[] getSites;
    MaterialDatePicker<Pair<Long, Long>> picker;

    //data for request
    String token;
    float fromTime, toTime, request_id;

    String savedUrl1 = null;
    String savedUrl2 = null;
    String savedUrl3 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_report);

        Intent getData = getIntent();
        request_id = getData.getFloatExtra("request_id", 0);
        token = getData.getStringExtra("token");
        getSites = (Sites[]) getData.getSerializableExtra("getSites");

        makeReportTv = findViewById(R.id.makeReportTv);
        confirmBtn = findViewById(R.id.confirmBtn);
        stepView = findViewById(R.id.stepView);
        backBtn = findViewById(R.id.backBtn);
        resetTV = findViewById(R.id.resetTv);
        dateSelector = findViewById(R.id.dateSelector);

        selectDateRange();

        confirmBtn();
        resetTVBtn();
        stepView();
        backBtn(request_id, token, getSites);
        dateSelector.setOnClickListener(v -> picker.show(this.getSupportFragmentManager(), picker.toString()));

    }

    public void selectDateRange() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        picker = builder.build();
        picker.show(this.getSupportFragmentManager(), picker.toString());
        picker.addOnCancelListener(v -> {
            Log.d("DatePicker Activity", "Dialog was cancelled");
            //confirmBtn.setEnabled(false);
        });
        picker.addOnNegativeButtonClickListener(v -> {
            Log.d("DatePicker Activity", "Dialog Negative Button was clicked");
            //confirmBtn.setEnabled(false);
        });
        picker.addOnPositiveButtonClickListener(v -> {
            Log.d("DatePicker Activity", "Date String = " + picker.getHeaderText() + " Date epoch " + v.first + " to " + v.second);

            confirmBtn.setEnabled(true);
            try {
                if (v.first != null && v.second != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(v.first);
                    Date aTime = cal.getTime();
                    cal.setTimeInMillis(v.second);
                    Date bTime = cal.getTime();
                    String a = OleAutomationDateUtil.convertToOADate(aTime);
                    String b = OleAutomationDateUtil.convertToOADate(bTime);
                    fromTime = Float.parseFloat(a);
                    toTime = Float.parseFloat(b);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        });
    }


    public void confirmBtn() {
        confirmBtn.setOnClickListener(v -> callApi(step));
    }

    private void callApi(int st) {
        switch (st) {
            case 0:
                Call<DoReportResponse1> call1 = RetrofitClient
                        .getInstance()
                        .getApi()
                        .doReport1(token, fromTime, toTime, request_id);
                call1.enqueue(new Callback<DoReportResponse1>() {
                    @Override
                    public void onResponse(Call<DoReportResponse1> call, Response<DoReportResponse1> response) {
                        DoReportResponse1 r = response.body();
                        if (r != null) {
                            if (r.isCompleted()) {
                                step++;
                                Toast.makeText(MakeReport.this, "Make Report 1 Success", Toast.LENGTH_LONG).show();
                                stepView.go(step, true);
                                dateSelector.setEnabled(false);
                                makeReportTv.setText(String.format("Make Report %s", step + 1));
                                savedUrl1 = r.getReport1_url();
                                gotoReport(savedUrl1);
                            } else {
                                Toast.makeText(MakeReport.this, r.getError_message(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DoReportResponse1> call, Throwable t) {

                    }
                });
                break;
            case 1:
                Call<DoReportResponse2> call2 = RetrofitClient
                        .getInstance()
                        .getApi()
                        .doReport2(token, request_id);
                call2.enqueue(new Callback<DoReportResponse2>() {
                    @Override
                    public void onResponse(Call<DoReportResponse2> call, Response<DoReportResponse2> response) {
                        DoReportResponse2 r = response.body();
                        if (r != null) {
                            if (r.isCompleted()) {
                                step++;
                                Toast.makeText(MakeReport.this, "Make Report 2 Success", Toast.LENGTH_LONG).show();
                                stepView.go(step, true);
                                resetTV.setEnabled(false);
                                resetTV.setVisibility(View.INVISIBLE);
                                makeReportTv.setText(String.format("Make Report %s", step + 1));
                                savedUrl2 = r.getReport2_url();
                                gotoReport(savedUrl2);
                            } else {
                                Toast.makeText(MakeReport.this, r.getError_message(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DoReportResponse2> call, Throwable t) {

                    }
                });
                break;
            case 2:
                Call<DoReportResponse3> call3 = RetrofitClient
                        .getInstance()
                        .getApi()
                        .doReport3(token, request_id);
                call3.enqueue(new Callback<DoReportResponse3>() {
                    @Override
                    public void onResponse(Call<DoReportResponse3> call, Response<DoReportResponse3> response) {
                        DoReportResponse3 r = response.body();
                        if (r != null) {
                            if (r.isCompleted()) {
                                Toast.makeText(MakeReport.this, "Make Report 3 Success", Toast.LENGTH_LONG).show();
                                stepView.go(step, true);
                                dateSelector.setEnabled(false);
                                confirmBtn.setEnabled(false);
                                savedUrl3 = r.getReport3_url();
                                gotoReport(savedUrl3);
                            } else {
                                Toast.makeText(MakeReport.this, r.getError_message(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DoReportResponse3> call, Throwable t) {

                    }
                });
                break;
        }
    }

    public void resetTVBtn() {
        resetTV.setOnClickListener(v -> {
            step = 0;
            stepView.go(step, true);
            makeReportTv.setText("Make Report 1");
            dateSelector.setEnabled(true);
            confirmBtn.setEnabled(true);
        });
    }

    public void backBtn(float request_id, String token, Sites[] getSites) {
        backBtn.setOnClickListener(v -> payRoll(request_id, token, getSites));
    }

    private void gotoReport(String file) {
        if (file != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(file));
            startActivity(browserIntent);
        } else {
            Toast.makeText(MakeReport.this, "Can't find Report Url", Toast.LENGTH_SHORT).show();
        }
    }

    public void stepView() {
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
        intent.putExtra("savedUrl1", savedUrl1);
        intent.putExtra("savedUrl2", savedUrl2);
        intent.putExtra("savedUrl3", savedUrl3);
        startActivity(intent);
    }
}