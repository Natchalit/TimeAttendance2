package com.example.timeattendance2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeattendance2.R;
import com.example.timeattendance2.model.Images;
import com.example.timeattendance2.model.Sites;

import java.util.ArrayList;
import java.util.Arrays;

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

    private ImageView imageView;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

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

       /* showDetail = findViewById(R.id.showDetail);
        showDetail.setText(String.format("%s %s", textSpn, textRadio));*/

        gridView = findViewById(R.id.gridView);
        //gridView.setAdapter(new ImageAdapter(this, detail));

        //imageView = (ImageView) findViewById(R.id.imageView2);
        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        //recyclerView.setLayoutManager(gridLayoutManager);

        //DataAdapter dataAdapter = new DataAdapter(getApplicationContext(), prepareData(detail));
        gridView.setAdapter(new DataAdapter(this, prepareData(detail)));

        backBtn();
    }
/*        imgs = new String[detail.length];
        for (int i = 0; i < detail.length; i++) {
            // Uri.Builder b = Uri.parse("https://advancedchecker.blob.core.windows.net/c01/" + detail[i].getUrl()).buildUpon();
            imgs[i] = "https://advancedchecker.blob.core.windows.net/c01/" + detail[i].getUrl();
        }*/

    private ArrayList prepareData(Images[] images) {

// here you should give your image URLs and that can be a link from the Internet

        ArrayList imageUrlList = new ArrayList<>();
        imageUrlList.addAll(Arrays.asList(images));
        Log.d("Thumbnail", "List count: " + imageUrlList.size());
        return imageUrlList;
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