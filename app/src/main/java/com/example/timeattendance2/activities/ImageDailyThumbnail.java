package com.example.timeattendance2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timeattendance2.R;
import com.example.timeattendance2.model.Images;
import com.example.timeattendance2.model.Sites;

public class ImageDailyThumbnail extends AppCompatActivity {

    GridView gridView;
    Button backBtn;
    TextView showDetail;

    Images[] detail;
    String textSpn, textRadio;

    //
    Sites[] getSites;
    String token;
    float request_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_daily_thumbnail);

        Intent intent = getIntent();

        request_id = intent.getFloatExtra("request_id", 0);
        token = intent.getStringExtra("token");
        getSites = (Sites[]) intent.getSerializableExtra("getSites");

        detail = (Images[]) intent.getSerializableExtra("detail");
        textSpn = intent.getStringExtra("textSpan");
        textRadio = intent.getStringExtra("textRadio");


        showDetail = findViewById(R.id.showDetail);
        showDetail.setText(String.format("%s %s", textSpn, textRadio));

        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this, detail ));

        backBtn();
    }

    public void backBtn() {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> imageDaily());
    }

    public void imageDaily() {
        Intent intent = new Intent(this, ImageDaily.class);
        intent.putExtra("token", token);
        intent.putExtra("request_id", request_id);
        intent.putExtra("getSites", getSites);
        startActivity(intent);
    }
}