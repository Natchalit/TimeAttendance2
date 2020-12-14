package com.example.timeattendance2.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.timeattendance2.utils.OleAutomationDateUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaptureCheckInOut extends AppCompatActivity {
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;

    ImageView imageView;
    TextView textType;
    Button finishCheckInBtn;

    InputImage image;
    String token;
    float Latitude, Longitude, request_id;
    int staff_id, siteIndex;
    float timeStamp;
    byte[] Image;
    boolean isCheckIn;

    Sites site;
    Sites[] getSites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_checkin);

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Intent getData = getIntent();
        site = (Sites) getData.getSerializableExtra("site");
        siteIndex = site.getIndex();

        getSites = (Sites[]) getData.getSerializableExtra("getSites");
        token = getData.getStringExtra("token");
        request_id = getData.getFloatExtra("request_id", 0);
        isCheckIn = getData.getBooleanExtra("isCheckin", true);

        Button captureBtn = findViewById(R.id.captureBtn);
        Button backBtn = findViewById(R.id.backBtn);
        finishCheckInBtn = findViewById(R.id.finishBtn);
        TextView unitName = findViewById(R.id.unitName);
        imageView = findViewById(R.id.imageView);
        textType = findViewById(R.id.typeText);

        unitName.setText(site.getName());
        finishCheckInBtn.setEnabled(false);
        if (isCheckIn) {
            textType.setText("ลงเวลาเข้างาน\nCheck In ");
            finishCheckInBtn.setText("Finish Check In");
        } else {
            textType.setText("ลงเวลาออกงาน\nCheck Out ");
            finishCheckInBtn.setText("Finish Check Out");

        }

        captureBtn.setOnClickListener(v -> {
            if (hasCameraPermission()) {
                enableCamera();
            } else {
                requestPermission();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int locationRequestCode = 1000;
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
        finishCheckInBtn.setOnClickListener(v -> dataCaptureUser());

        dateRealtime();
        refreshTime();
    }

    private void enableCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void dataCaptureUser() {
        finishCheckInBtn.setText(R.string.dd);
        try {
            Date currentTime = Calendar.getInstance().getTime();
            String a = OleAutomationDateUtil.convertToOADate(currentTime);
            timeStamp = Float.parseFloat(a);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Call<StampResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .stampUser(token, Latitude, Longitude, Image, staff_id, siteIndex, timeStamp, timeStamp, isCheckIn, request_id);
        Log.d("REQ", "request_id :" + request_id + "\n"
                + "token :" + token + "\n"
                + "latitude : " + Latitude + "\n"
                + "longitude :" + Longitude + "\n"
                + "image :" + Image.length + "\n"
                + "staffid :" + staff_id + "\n"
                + "siteid :" + siteIndex + "\n"
                + "timeStamp :" + timeStamp + "\n"
                + "isCheckin :" + isCheckIn + "\n"
                + "request_id :" + request_id + "\n");

        call.enqueue(new Callback<StampResponse>() {
            @Override
            public void onResponse(Call<StampResponse> call, Response<StampResponse> response) {
                StampResponse stampResponse = response.body();
                Toast.makeText(CaptureCheckInOut.this, "Image Length: " + Image.length, Toast.LENGTH_LONG).show();
                if (stampResponse == null) {
                    Toast.makeText(CaptureCheckInOut.this, "No response", Toast.LENGTH_LONG).show();
                    return;
                }
                if (stampResponse.isCompleted()) {
                    Toast.makeText(CaptureCheckInOut.this, "Success", Toast.LENGTH_LONG).show();
                    mainMenu(token, request_id, getSites);
                } else {
                    String er = stampResponse.getError_message();
                    Toast.makeText(CaptureCheckInOut.this, er, Toast.LENGTH_LONG).show();
                    Log.i("FAILED: ", er);

                }
                setFinishTextBack();
            }

            @Override
            public void onFailure(Call<StampResponse> call, Throwable t) {
                Log.e("FAILURE: ", call.toString() + ":" + t.getMessage());
                setFinishTextBack();
            }
        });
    }

    private void setFinishTextBack() {
        if (isCheckIn) {
            finishCheckInBtn.setText("Finish Check In");
        } else {
            finishCheckInBtn.setText("Finish Check Out");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || data.getExtras() == null) return;
        File returnFile = new File((String) data.getExtras().get("uri"));
        Uri paths = Uri.fromFile(returnFile);
        Bitmap bitmap = BitmapFactory.decodeFile((String) data.getExtras().get("uri"));
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, blob);
        Image = blob.toByteArray();

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            imageView.setImageURI(paths);
            try {
                image = InputImage.fromFilePath(CaptureCheckInOut.this, paths);
                scanBarcodes(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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
        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    if (barcodes.isEmpty()) {
                        Toast.makeText(CaptureCheckInOut.this, "Don't have QR code in this picture", Toast.LENGTH_SHORT).show();
                        finishCheckInBtn.setEnabled(false);
                        return;
                    }
                    for (Barcode barcode : barcodes) {
                        String rawValue = barcode.getRawValue();
                        if (rawValue == null) {
                            Toast.makeText(CaptureCheckInOut.this, "Don't have QR code in this picture", Toast.LENGTH_SHORT).show();
                            finishCheckInBtn.setEnabled(false);
                            return;
                        }
                        try {
                            staff_id = Integer.parseInt(rawValue);
                            Toast.makeText(CaptureCheckInOut.this, "Staff ID: " + staff_id, Toast.LENGTH_LONG).show();
                            finishCheckInBtn.setEnabled(true);
                        } catch (Exception e) {
                            Toast.makeText(CaptureCheckInOut.this, "Can't parse Staff ID from " + rawValue, Toast.LENGTH_SHORT).show();
                            finishCheckInBtn.setEnabled(false);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CaptureCheckInOut.this, "Can't read QR Code", Toast.LENGTH_SHORT).show();
                    finishCheckInBtn.setEnabled(false);
                });
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