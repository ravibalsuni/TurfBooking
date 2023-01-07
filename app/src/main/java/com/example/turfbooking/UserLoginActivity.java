package com.example.turfbooking;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turfbooking.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.BufferedReader;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Intent intent = getIntent();
        String activityName = null;
        if(intent !=null)
            activityName = intent.getStringExtra("activity");
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
                    new PostAsyncTask().execute();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(UserLoginActivity.this, RegisterActivity.class);
                i.putExtra("activity","LoginActivity");
                startActivity(i);
                finish();
            }
        });

    }

    private class PostAsyncTask extends AsyncTask<String,Void,JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            Map postData = new HashMap();
            postData.put("username",phone);
            postData.put("password",password);
            return post(TEST_URL,postData);
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);
            //All your UI operation can be performed here
            //Response string can be converted to JSONObject/JSONArray like
            System.out.println("response - "+response);
            try {
                if (response != null) {
                    if (response.toString().contains("token")) {
                        Intent i= new Intent(UserLoginActivity.this, TurfBookingActivity.class);
                        i.putExtra("token",response.getString("token"));
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Login error", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
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
    public JSONObject post(String REQUEST_URL, Map<String, Object> params) {
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
            if (statusCode == 200) {
                sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                jsonObject = new JSONObject(sb.toString());
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
        return jsonObject;
    }
}
