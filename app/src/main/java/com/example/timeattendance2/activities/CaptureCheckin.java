package com.example.timeattendance2.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.timeattendance2.R;
import com.example.timeattendance2.api.RetrofitClient;
import com.example.timeattendance2.model.Sites;
import com.example.timeattendance2.model.StampResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaptureCheckin extends AppCompatActivity {
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private int locationRequestCode = 1000;

    ImageView imageView;

    InputImage image;
    String token;
    float Latitude, Longitude, request_id;
    int staffid, siteIndex;
    long timeStamp;
    byte[] Image;
    boolean isCheckIn = true;

    Sites site;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_checkin);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Intent getData = getIntent();
        site = (Sites) getData.getSerializableExtra("site");
        siteIndex = site.getIndex();

        Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");
        token = getData.getStringExtra("token");
        request_id = getData.getFloatExtra("request_id", 0);

        Button captureBtn = (Button) findViewById(R.id.captureBtn);
        Button backBtn = (Button) findViewById(R.id.backBtn);
        Button finishCheckInBtn = (Button) findViewById(R.id.finishBtn);
        TextView unitName = (TextView) findViewById(R.id.unitName);
        imageView = (ImageView) findViewById(R.id.imageView);

        unitName.setText(site.getName());

        captureBtn.setOnClickListener(v -> {
            if (hasCameraPermission()) {
                enableCamera();
            } else {
                requestPermission();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                Latitude = (float) location.getLatitude();
                Longitude = (float) location.getLongitude();
            }
        });

        backBtn.setOnClickListener(v -> mainMenu(token, request_id, getSites));
        finishCheckInBtn.setOnClickListener(v -> mainMenu(token, request_id, getSites));


        dateRealtime();
        refreshTime();
    }

    private void enableCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void dataCaptureUser() {

        Date currentTime = Calendar.getInstance().getTime();
        timeStamp = currentTime.getTime();
        Call<StampResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .stampUser(token, Latitude, Longitude, Image, staffid, siteIndex, timeStamp, isCheckIn, request_id);

        Log.d("REQ", "request_id :" + request_id + "\n"
                + "token :" + token + "\n"
                + "Latitude : " + Latitude + "\n"
                + "Longitude :" + Longitude + "\n"
                + "Image :" + Image.toString() + "\n"
                + "staffid :" + staffid + "\n"
                + "siteIndex :" + siteIndex + "\n"
                + "timeStamp :" + timeStamp + "\n"
                + "isCheckIn :" + isCheckIn + "\n"
                + "request_id :" + request_id + "\n");

        call.enqueue(new Callback<StampResponse>() {
            @Override
            public void onResponse(Call<StampResponse> call, Response<StampResponse> response) {
                StampResponse stampResponse = response.body();
                if (stampResponse != null && stampResponse.isCompleted()) {
                    Log.i("STAMP RESPONSE", response.code() + ":" + response.message());
                } else {
                    Log.i("STAMP RESPONSE", response.message());
                }
            }

            @Override
            public void onFailure(Call<StampResponse> call, Throwable t) {
                Log.e("STAMP RESPONSE", call.toString() + ":" + t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File returnFile = new File((String) data.getExtras().get("uri"));
        Uri paths = Uri.fromFile(returnFile);
        try {
            Image = readFile(returnFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (data != null && resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {

            imageView.setImageURI(paths);
            try {
                image = InputImage.fromFilePath(CaptureCheckin.this, paths);
                scanBarcodes(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] readFile(File file) throws IOException {
        // Open file
        try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    private void scanBarcodes(InputImage image) {

        BarcodeScanner scanner = BarcodeScanning.getClient();
        Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    for (Barcode barcode : barcodes) {
                        String rawValue = barcode.getRawValue();
                        if (!rawValue.isEmpty()) {
                            Toast.makeText(CaptureCheckin.this, rawValue, Toast.LENGTH_SHORT).show();
                            try {
                                staffid = Integer.parseInt(rawValue);
                                dataCaptureUser();
                            } catch (Exception e) {
                                Toast.makeText(CaptureCheckin.this, "Can't parse Staff ID from " + rawValue, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CaptureCheckin.this, "Don't have QR code in this picture", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(CaptureCheckin.this, "Can't read QR Code", Toast.LENGTH_SHORT).show());
    }
/*
    public void alertDialog() {
        captureBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CaptureCheckin.this);
            builder.setTitle("Capture Alert")
                    .setMessage("ไม่ผ่าน")
                    .setCancelable(false)
                    .setPositiveButton("ถ่ายรูปใหม่", (dialog, which) -> Toast.makeText(CaptureCheckin.this, "ถ่ายรูปใหม่", Toast.LENGTH_SHORT).show());

            //Creating dialog box
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }*/

    public void mainMenu(String token, float request_id, Sites[] getSites) {
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("token", token);
        intent.putExtra("request_id", request_id);
        intent.putExtra("getSites", getSites);
        startActivity(intent);
    }

    public void dateRealtime() {
        Date currentDate = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentDate);
        TextView textViewDate = findViewById(R.id.dateText);
        textViewDate.setText(formattedDate);
    }

    public void refreshTime() {

        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss", Locale.US);
        Date currentDate = Calendar.getInstance().getTime();
        String formattedTime = formatTime.format(currentDate);
        TextView textViewTime = findViewById(R.id.timeText);
        textViewTime.setText(formattedTime);
        refresh();
    }

    private void refresh() {

        final Handler handler = new Handler();

        final Runnable runnable = this::refreshTime;

        handler.postDelayed(runnable, 1000);
    }
}