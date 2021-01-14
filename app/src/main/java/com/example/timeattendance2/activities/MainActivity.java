package com.example.timeattendance2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timeattendance2.R;
import com.example.timeattendance2.api.RetrofitClient;
import com.example.timeattendance2.model.LoginResponse;
import com.example.timeattendance2.model.Sites;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button signInBtn;
    private TextInputEditText usernameInput, passwordInput;
    private String usernameGet, passwordGet;

    private static final String APPKEY = "0819996903";
    private float request_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

        signInBtn();

    }

    public void signInBtn() {
        signInBtn = findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(v -> {

//                login();
            userLogin();

        });
    }

    private void login() {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    private void userLogin() {
        signInBtn.setEnabled(false);
        signInBtn.setText("Logging In...");
        if (request_id == 0) {
            request_id = System.currentTimeMillis();
        }

        usernameGet = usernameInput.getText().toString().trim();
        passwordGet = passwordInput.getText().toString().trim();

        if (usernameGet.isEmpty()) {
            usernameInput.setError("Username require");
            usernameInput.requestFocus();
            return;
        }

        if (passwordGet.isEmpty()) {
            passwordInput.setError("Password require");
            return;
        }

        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .userLogin(APPKEY, usernameGet, passwordGet, request_id);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                if (loginResponse.isCompleted()) {
                    if (loginResponse.getToken() != null) {

                        String token = loginResponse.getToken();
                        float request_id = loginResponse.getRequest_id();
                        Sites[] getSites = loginResponse.getSites();

                        logintoMainmenu(token,getSites,request_id);
                    }
                } else {
                    Toast.makeText(MainActivity.this, loginResponse.getError_message(), Toast.LENGTH_LONG).show();
                }
                signInBtn.setEnabled(true);
                signInBtn.setText("Login");

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                signInBtn.setEnabled(true);
                signInBtn.setText("Login");
            }
        });

        Log.d("input", "Request id :" + request_id + "\n"
                + "Appkey :" + APPKEY + "\n"
                + "Username : " + usernameGet + "\n"
                + "Password :" + passwordGet + "\n");
    }


    public void logintoMainmenu(String token, Sites[] getSites, float request_id) {
        Intent intent = new Intent(this, MainMenu.class);

        intent.putExtra("request_id",request_id);
        intent.putExtra("token", token);
        intent.putExtra("getSites", getSites);
        startActivity(intent);
    }
}