package com.example.turfbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private Button userLoginButton,ownerLoginButton,adminLoginButton, reportGeneration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("inside home activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userLoginButton = findViewById(R.id.btnLogin_user);
        ownerLoginButton = findViewById(R.id.btnLogin_owner);
        adminLoginButton = findViewById(R.id.btnLogin_admin);
        reportGeneration = findViewById(R.id.btn_report);

        userLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent user = new Intent(HomeActivity.this, UserLoginActivity.class);
                startActivity(user);
            }
        });

        ownerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent owner = new Intent(HomeActivity.this,OwnerLoginActivity.class);
                startActivity(owner);
            }
        });

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent admin = new Intent(HomeActivity.this,AdminLoginActivity.class);
                startActivity(admin);
            }
        });

        reportGeneration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent report = new Intent(HomeActivity.this,ReportActivity.class);
                startActivity(report);
            }
        });

    }
}
