package com.example.turfbooking;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turfbooking.constants.Constants;
import com.example.turfbooking.global.GlobalClass;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UserLoginActivity extends AppCompatActivity {
    String TEST_URL= Constants.BASE_URL+"/login/authenticate";
    Activity activity;
    private TextView tvRegister;
    private EditText etphone,etLoginPassword;
    private Button loginButton;
    String phone;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Intent intent = getIntent();
        String activityName = null;
        if(intent !=null)
            activityName = intent.getStringExtra("activity");
        System.out.println("user login activityName - "+activityName);
        if(activityName != null ){
            Intent i= new Intent(UserLoginActivity.this, TurfBookingActivity.class);
            startActivity(i);
        }
        tvRegister = findViewById(R.id.tvRegister);
        etphone = findViewById(R.id.phone);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        loginButton = findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = etphone.getText().toString().trim();
                password = etLoginPassword.getText().toString().trim();
                if (phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(UserLoginActivity.this, "Enter your Email and Password to login", Toast.LENGTH_SHORT).show();
                } else {
                    activity= UserLoginActivity.this;
                    String response=new GetAsyncTask().doInBackground(phone);
                    System.out.println("response - "+response);
                    if (response != null) {
                        if (response.toString().contains("user")) {
                            Intent i= new Intent(UserLoginActivity.this, TurfBookingActivity.class);
                            final GlobalClass globalVariableU = (GlobalClass) getApplicationContext();
                            globalVariableU.setUserID(phone);
                            globalVariableU.setRole("user");
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Error Authentication Issue", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(UserLoginActivity.this, RegisterActivity.class);
                i.putExtra("activity","UserLoginActivity");
                startActivity(i);
                finish();
            }
        });

    }

    private class GetAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String USER_PROFILE=Constants.BASE_URL+"/engine-rest/user/"+params[0]+"/profile";
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                System.out.println("USER_PROFILE - "+USER_PROFILE);
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(USER_PROFILE);

                    urlConnection = (HttpURLConnection) url
                            .openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);
                    reader = new BufferedReader(isw);
                    System.out.println("status code - "+urlConnection.getResponseCode());
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return sb.toString();
        }
    }

}
