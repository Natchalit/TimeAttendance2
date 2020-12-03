package com.example.timeattendance2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.timeattendance2.R;

public class ImageDailyThumbnail extends AppCompatActivity {

    GridView gridView;
    Button backBtn;
    TextView showDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_daily_thumbnail);

        Intent intent = getIntent();
        String textRadio = intent.getStringExtra(ImageDaily.EXTRA_TEXT);
        String textSpn = intent.getStringExtra(ImageDaily.EXTRA_TEXT2);

        showDetail = (TextView)findViewById(R.id.showDetail);
        showDetail.setText(textSpn+"  "+textRadio);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this));


        backBtn();
    }

    public void backBtn() {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDaily();
            }
        });
    }

    public void imageDaily() {
        Intent intent = new Intent(this, ImageDaily.class);
        startActivity(intent);
    }
}