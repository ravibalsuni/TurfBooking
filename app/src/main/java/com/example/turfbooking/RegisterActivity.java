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
                if(activityName.equals("LoginActivity"))
                 startActivity(new Intent(RegisterActivity.this, UserLoginActivity.class));
                else
                    startActivity(new Intent(RegisterActivity.this, AdminLoginActivity.class));
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
            try {
                if (response != null) {
                    if (response.toString().contains("SUCCESS")) {
                        Intent i= new Intent(RegisterActivity.this, UserLoginActivity.class);
                        i.putExtra("activity","RegisterActivity");
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                    } else {
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
    /**
     * Method allows to HTTP POST request to the server to send data to a specified resource
     * @param REQUEST_URL URL of the API to be requested
     * @param params parameter that are to be send in the "body" of the request Ex: parameter=value&amp;also=another
     * returns response as a JSON object
     */
    public String post(String REQUEST_URL, Map<String, Object> params) {
        JSONObject jsonObject = null;
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
