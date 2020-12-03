package com.example.timeattendance2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timeattendance2.R;
import com.example.timeattendance2.api.RetrofitClient;
import com.example.timeattendance2.model.Sites;
import com.example.timeattendance2.model.StampResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaptureCheckin extends AppCompatActivity {

    private final int CAMERA_RESULT_CODE = 100;
    private boolean isCheckIn = true;
    private Button captureBtn, finishCheckInBtn, backBtn;
    private TextView unitName;

    ImageView imageView;
    String currentPhotoPath;
    InputImage image;
    String token;
    float Latitude, Longitude, request_id;
    int staffid, siteIndex;
    long timeStamp;
    byte[] Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_checkin);

        Intent getData = getIntent();
        Sites site = (Sites) getData.getSerializableExtra("site");
        Sites[] getSites = (Sites[]) getData.getSerializableExtra("getSites");
        String token = getData.getStringExtra("token");
        float request_id = getData.getFloatExtra("request_id", 0);


        imageView = (ImageView) findViewById(R.id.imageView);
        captureBtn = (Button) findViewById(R.id.captureBtn);
        backBtn = (Button) findViewById(R.id.backBtn);

        unitName = (TextView) findViewById(R.id.unitName);
        unitName.setText(site.getName());

        setBackBtn(token, request_id, getSites);
        setFinishCheckInBtn(token, request_id, getSites);

        setCaptureBtn();
        dateRealtime();
        refreshTime();
    }

    private void dataCaptureUser() {

        timeStamp = System.currentTimeMillis();


        Call<StampResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .stampUser(token, Latitude, Longitude, Image, staffid, siteIndex, timeStamp, isCheckIn, request_id);

        call.enqueue(new Callback<StampResponse>() {
            @Override
            public void onResponse(Call<StampResponse> call, Response<StampResponse> response) {
                StampResponse stampResponse = response.body();
                if (stampResponse.isCompleted()) {

                }
            }

            @Override
            public void onFailure(Call<StampResponse> call, Throwable t) {

            }
        });
    }

    public void setCaptureBtn() {
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CaptureCheckin.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (it.resolveActivity(getPackageManager()) != null) {
//                        Log.d("Take picture","Take picture");
                        dispatchTakePictureIntent();
                    }
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Toast.makeText(CaptureCheckin.this, "ไม่สามารถใช้งานกล้องได้", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{
                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, CAMERA_RESULT_CODE);
                }
            }
        });
    }

    public void setBackBtn(String token, float request_id, Sites[] getSites) {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenu(token, request_id, getSites);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_RESULT_CODE) {
                Log.d("Result", "!!!!!!!!!!!!!!!!!!!!!");
                File f = new File(currentPhotoPath);
                Uri uri = Uri.fromFile(f);
                imageView.setImageURI(uri);
//                Toast.makeText(getApplicationContext(), uri.getPath(), Toast.LENGTH_SHORT).show();
//                Log.d("tag", "Absolute Url of image is :" + uri);

//                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                mediaScanIntent.setData(uri);
//                this.sendBroadcast(mediaScanIntent);

                try {
                    image = InputImage.fromFilePath(this, uri);
                    scanBarcodes(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    private void scanBarcodes(InputImage image) {
        Log.d("Barcode", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE)
                        .build();

        BarcodeScanner scanner = BarcodeScanning.getClient();

        Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        for (Barcode barcode : barcodes) {
                            Rect bounds = barcode.getBoundingBox();
                            Point[] corners = barcode.getCornerPoints();

                            String rawValue = barcode.getRawValue();

//                            if (rawValue.isEmpty()) {
                            Toast.makeText(CaptureCheckin.this, rawValue, Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(CaptureCheckin.this, "Don't have QR code in this picture", Toast.LENGTH_SHORT).show();
//                            }
//                            Log.d("tag","QR Code has text : "+rawValue);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CaptureCheckin.this, "Cant read QR Code", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(CaptureCheckin.this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.d("Already", "@@@@@@@@@@@@@@@@@@@@@@@");
                try {
                    startActivityForResult(takePictureIntent, CAMERA_RESULT_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.d("CrateIMG", "@@@@@@@@@@@@@@@@@@@@@");
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_RESULT_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (it.resolveActivity(getPackageManager()) != null) {
                    dispatchTakePictureIntent();
                }
            } else {
                Toast.makeText(CaptureCheckin.this, "ไม่สามารถใช้งานกล้องได้", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void alertDialog() {
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CaptureCheckin.this);
                builder.setTitle("Capture Alert")
                        .setMessage("ไม่ผ่าน")
                        .setCancelable(false)
                        .setPositiveButton("ถ่ายรูปใหม่", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(CaptureCheckin.this, "ถ่ายรูปใหม่", Toast.LENGTH_SHORT).show();
                            }
                        });

                //Creating dialog box
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setFinishCheckInBtn(String token, float request_id, Sites[] sites) {
        finishCheckInBtn = (Button) findViewById(R.id.finishBtn);
        finishCheckInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainMenu(token, request_id, sites);
            }
        });
    }

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

        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
        Date currentDate = Calendar.getInstance().getTime();
        String formattedTime = formatTime.format(currentDate);
        TextView textViewTime = findViewById(R.id.timeText);
        textViewTime.setText(formattedTime);

        refresh(1000);
    }

    private void refresh(int milliseconds) {

        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override

            public void run() {
                refreshTime();
            }
        };

        handler.postDelayed(runnable, milliseconds);
    }
}