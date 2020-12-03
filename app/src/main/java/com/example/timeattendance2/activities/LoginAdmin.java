package com.example.timeattendance2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.timeattendance2.R;
import com.google.android.material.textfield.TextInputEditText;

public class LoginAdmin extends AppCompatActivity {

    Button loginBtn;
    TextInputEditText usernameInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        loginBtn();
    }

    public void loginBtn(){
        loginBtn = findViewById(R.id.logInBtn);
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dashboardAdmin();
            }
        });
    }

    public void dashboardAdmin(){
        Intent intent = new Intent(this, DashboardAdmin.class);
        startActivity(intent);
    }
}