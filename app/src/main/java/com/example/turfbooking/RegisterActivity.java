package com.example.turfbooking;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turfbooking.constants.Constants;
import com.example.turfbooking.global.GlobalClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    String TEST_URL=Constants.BASE_URL+"/engine-rest/user/create";
    Activity activity;
    private Button registerBtn,gotoLoginBtn;

    String fname, fPhone, fGmail, fPassword;
    private EditText regName,regPhone,regGmail,regPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerBtn = findViewById(R.id.btnRegLogin);
        gotoLoginBtn = findViewById(R.id.btnGotoLogin);
        regName = findViewById(R.id.etRegName);
        regPhone = findViewById(R.id.etRegPhone);
        regGmail = findViewById(R.id.etRegGmail);
        regPassword = findViewById(R.id.etRegPassword);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = regName.getText().toString().trim();
                fPhone = regPhone.getText().toString().trim();
                fGmail = regGmail.getText().toString().trim();
                 fPassword = regPassword.getText().toString().trim();
                if (fname.isEmpty() || fPassword.isEmpty() || fGmail.isEmpty() || fPhone.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    activity=RegisterActivity.this;
                    new PostAsyncTask().execute();
                }
            }
        });

        gotoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                String activityName=i.getStringExtra("activity");
                System.out.println("activityName - "+activityName);
                if(activityName.equals("UserLoginActivity"))
                 startActivity(new Intent(RegisterActivity.this, UserLoginActivity.class));
                else if(activityName.equals("AdminLoginActivity"))
                    startActivity(new Intent(RegisterActivity.this, AdminLoginActivity.class));
                else if(activityName.equals("OwnerLoginActivity"))
                    startActivity(new Intent(RegisterActivity.this, OwnerLoginActivity.class));
                else
                finish();
            }
        });
    }
    private class PostAsyncTask extends AsyncTask<String,Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject parent = new JSONObject();
            JSONObject child1 = new JSONObject();
            JSONObject child2 = new JSONObject();
            Intent intent = getIntent();
            String activityName=intent.getStringExtra("activity");
            if (activityName.equals("UserLoginActivity")) {
                try {
                    child1.put("lastName", "user");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (activityName.equals("AdminLoginActivity")) {
                try {
                    child1.put("lastName", "admin");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (activityName.equals("OwnerLoginActivity")) {
                try {
                    child1.put("lastName", "owner");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
            child2.put("password", fPassword);
            child1.put("id", fPhone);
            child1.put("firstName", fname);
            child1.put("email", fGmail);
            parent.put("profile", child1);
            parent.put("credentials", child2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Map postData = new Gson().fromJson(String.valueOf(parent), HashMap.class);
            String response=post(TEST_URL,postData);
            JSONObject respJson = new JSONObject();
            try {
                respJson.put("response", response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return respJson;
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);
            //All your UI operation can be performed here
            //Response string can be converted to JSONObject/JSONArray like
            System.out.println("response - "+response);
            Intent intent = getIntent();
            String activityName=intent.getStringExtra("activity");
            System.out.println("in Register activity - activityName - "+activityName);
            try {
                if (response != null) {
                    if (response.toString().contains("SUCCESS") && activityName.equals("UserLoginActivity")) {
                        Intent i= new Intent(RegisterActivity.this, UserLoginActivity.class);
                        i.putExtra("activity","RegisterActivity");
                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                        globalVariable.setUserID(fPhone);
                        globalVariable.setRole("user");
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                    } else if(response.toString().contains("SUCCESS") && activityName.equals("OwnerLoginActivity")) {
                        Intent i= new Intent(RegisterActivity.this, OwnerLoginActivity.class);
                        i.putExtra("activity","RegisterActivity");
                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                        globalVariable.setUserID(fPhone);
                        globalVariable.setRole("Owner");

                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();

                    } else if(response.toString().contains("SUCCESS") && activityName.equals("AdminLoginActivity")) {
                        Intent i= new Intent(RegisterActivity.this, AdminLoginActivity.class);
                        i.putExtra("activity","RegisterActivity");
                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                        globalVariable.setUserID(fPhone);
                        globalVariable.setRole("user");
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Registration Error", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "Invalid Data", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(activity, String.format("%s","Something went wrong!!!!!!"), Toast.LENGTH_LONG).show();

            }
            System.out.println(response);
        }
    }

    public String post(String REQUEST_URL, Map<String, Object> params) {
        BufferedReader reader = null;
        try { URL url = new URL(REQUEST_URL);
            System.out.println("params - "+params);
            String postData = new ObjectMapper().writeValueAsString(params);
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            System.out.println("postData - "+postData);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("accept", "*/*");
            connection.setConnectTimeout(8000);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.getOutputStream().write(postDataBytes);
            connection.connect();
            StringBuilder sb;
            int statusCode = connection.getResponseCode();
            System.out.println("statusCode - "+statusCode);
            if (statusCode == 204) {
               return "SUCCESS";
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "FAILURE";
    }
}
