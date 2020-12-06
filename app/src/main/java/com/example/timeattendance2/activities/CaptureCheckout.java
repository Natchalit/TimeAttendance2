package com.example.timeattendance2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.timeattendance2.model.Sites;
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

public class CaptureCheckout extends AppCompatActivity {

    Button finishCheckOutBtn, captureBtn, backBtn;
    ImageView imageView;
    TextView unitName;
    String currentPhotoPath;
    InputImage image;
    private final int CAMERA_RESULT_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_checkout);

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
        setFinishCheckOutBtn(token, request_id, getSites);

        setCaptureBtn();
        dateRealtime();
        refreshTime();
    }

    private void scanBarcodes(InputImage image) {
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
                        Log.d("tag", "@@@@@@@@@@" + barcodes);
                        for (Barcode barcode : barcodes) {
                            Log.d("tag", "!!!!!!!!!!!!!");
                            Rect bounds = barcode.getBoundingBox();
                            Point[] corners = barcode.getCornerPoints();

                            String rawValue = barcode.getRawValue();

                            Toast.makeText(CaptureCheckout.this, rawValue, Toast.LENGTH_SHORT).show();
//                            Log.d("tag","QR Code has text : "+rawValue);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CaptureCheckout.this, "Cant read QR Code", Toast.LENGTH_SHORT).show();
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

    public void setCaptureBtn() {
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null)
            if (resultCode == RESULT_OK) {
                if (requestCode == CAMERA_RESULT_CODE) {
                    File f = new File(currentPhotoPath);
                    Uri uri = Uri.fromFile(f);
                    imageView.setImageURI(uri);

//                    try {
//                        image = InputImage.fromFilePath(this, uri);
//                        scanBarcodes(image);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
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
                Uri photoURI = FileProvider.getUriForFile(CaptureCheckout.this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_RESULT_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void alertDialog() {
        captureBtn = (Button) findViewById(R.id.captureBtn);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CaptureCheckout.this);
                builder.setTitle("Capture Alert")
                        .setMessage("ไม่ผ่าน")
                        .setCancelable(false)
                        .setPositiveButton("ถ่ายรูปใหม่", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(CaptureCheckout.this, "ถ่ายรูปใหม่", Toast.LENGTH_SHORT).show();
                            }
                        });

                //Creating dialog box
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setFinishCheckOutBtn(String token, float request_id, Sites[] getSites) {
        finishCheckOutBtn = (Button) findViewById(R.id.finishBtn);
        finishCheckOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenu(token, request_id, getSites);
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