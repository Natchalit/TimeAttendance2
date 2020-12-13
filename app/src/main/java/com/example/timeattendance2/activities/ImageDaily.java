package com.example.timeattendance2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.timeattendance2.R;
import com.example.timeattendance2.api.RetrofitClient;
import com.example.timeattendance2.model.GetImage;
import com.example.timeattendance2.model.Images;
import com.example.timeattendance2.model.Sites;
import com.example.timeattendance2.utils.OleAutomationDateUtil;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageDaily extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.example.application.example.EXTRA_TEXT2";

    RadioButton radioButton;
    RadioGroup radioGroup;
    Button backBtn, confirmBtn, dateSelector;
    Spinner spinner;
    //
    Sites[] getSites;

    //Data for request
    float request_id, fromTime, toTime;
    String token;
    int siteIndex;
    boolean isCheckin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_daily);

        Intent getData = getIntent();
        request_id = getData.getFloatExtra("request_id", 0);
        token = getData.getStringExtra("token");
        getSites = (Sites[]) getData.getSerializableExtra("getSites");

        radioGroup = findViewById(R.id.radioGroup);
        backBtn = findViewById(R.id.backBtn);
        spinner = findViewById(R.id.selectUnitSpn);
        dateSelector = findViewById(R.id.dateSelector);
        confirmBtn = findViewById(R.id.confirmBtn);

        unitList();
        backBtn.setOnClickListener(v -> dashboardAdmin(request_id, token, getSites));

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
        picker.show(this.getSupportFragmentManager(), picker.toString());
        picker.addOnCancelListener(v -> {
            Log.d("DatePicker Activity", "Dialog was cancelled");
            confirmBtn.setEnabled(false);
        });
        picker.addOnNegativeButtonClickListener(v -> {
            Log.d("DatePicker Activity", "Dialog Negative Button was clicked");
            confirmBtn.setEnabled(false);
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

        dateSelector.setOnClickListener(v -> picker.show(this.getSupportFragmentManager(), picker.toString()));

        confirmBtn.setOnClickListener(v -> {
            Sites site = (Sites) spinner.getSelectedItem();
            siteIndex = site.getIndex();

            int radioId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(radioId);
            String textRadio = radioButton.getText().toString();

            String textSpn = spinner.getSelectedItem().toString();
            callApi();

        /*    if (!textSpn.equals("Select Unit")) {
                imageThumbnailCheckIn(textRadio, textSpn);
            }*/
        });
    }

    private void callApi() {

        Call<GetImage> call = RetrofitClient
                .getInstance()
                .getApi()
                .images(token, siteIndex, fromTime, toTime, isCheckin, request_id);

        call.enqueue(new Callback<GetImage>() {
            @Override
            public void onResponse(Call<GetImage> call, Response<GetImage> response) {

                GetImage imagesResponse = response.body();
                if (imagesResponse != null) {

                    if (imagesResponse.isCompleted()) {
                        Images[] imgs = imagesResponse.getImages();
                        Toast.makeText(ImageDaily.this, imgs.length, Toast.LENGTH_LONG).show();
                        Log.i("IMAGES RESPONSE", imagesResponse.toString());
                    } else {
                        Log.i("IMAGES RESPONSE", imagesResponse.getError_message());
                        Toast.makeText(ImageDaily.this, imagesResponse.getError_message(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetImage> call, Throwable t) {

            }
        });
    }

    public void unitList() {
        Spinner spinner = findViewById(R.id.selectUnitSpn);

        Intent getData = getIntent();
        Sites[] sites = (Sites[]) getData.getSerializableExtra("getSites");

        List<Sites> sitesList = new ArrayList<>();
        Collections.addAll(sitesList, sites);

        ArrayAdapter<Sites> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sitesList);
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
        intent.putExtra(EXTRA_TEXT, textRadio);
        intent.putExtra(EXTRA_TEXT2, textSpn);
        startActivity(intent);
    }

    public void dashboardAdmin(float request_id, String token, Sites[] getSites) {
        Intent intent = new Intent(this, DashboardAdmin.class);
        intent.putExtra("token", token);
        intent.putExtra("request_id", request_id);
        intent.putExtra("getSites", getSites);
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