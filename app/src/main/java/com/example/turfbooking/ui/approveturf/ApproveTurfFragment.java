package com.example.turfbooking.ui.approveturf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turfbooking.AdminLoginActivity;
import com.example.turfbooking.constants.Constants;
import com.example.turfbooking.databinding.FragmentApproveturfBinding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ApproveTurfFragment extends Fragment implements ItemClickListener {
    String TEST_URL = Constants.BASE_URL + "/api/turf/search/status/pending";
    int globalPos = 0;
    private FragmentApproveturfBinding binding;

    TurfModel[] myListData1;

    MyAdapterApproveTurf adapter;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentApproveturfBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        String result = "null";
        try {
            result = new GetAsyncTask().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("result -" + result);
        JSONArray data = null;
        try {
            data = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            myListData1 = new TurfModel[data.length()];
            for (int i = 0; i < data.length(); i++) {
                myListData1[i] = new Gson().fromJson(data.getJSONObject(i).toString(), TurfModel.class);
            }
            for (TurfModel m : myListData1)
                System.out.println("model -" + m);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("myListData1 length - " + myListData1.length);
        final RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new MyAdapterApproveTurf(myListData1);
        recyclerView.setAdapter(adapter);
        // Handling the Click Events
        adapter.setClickListener(this);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view, int pos) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.out.println("pos - " + pos);
        globalPos = pos;
        System.out.println("data based on pos - " + myListData1[globalPos]);
        JSONObject response = new PostAsyncTask().doInBackground();
        if (response != null) {
            Toast.makeText(getActivity(), "Approved Success", Toast.LENGTH_SHORT).show();
            Intent i =new Intent(getActivity(), AdminLoginActivity.class);
            i.putExtra("activity","ApproveTurfFragment");
            startActivity(i);
        }
        else {
            Toast.makeText(getActivity(), "Could Not Approve", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(TEST_URL);

                    urlConnection = (HttpURLConnection) url
                            .openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);
                    reader = new BufferedReader(isw);
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

    private class PostAsyncTask extends AsyncTask<String, Void, JSONObject> {
        String UPDATE_URL = Constants.BASE_URL + "/api/turf/update/status";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            Map postData = new HashMap();
            postData.put("id",myListData1[globalPos].getId());
            return post(UPDATE_URL, postData);
        }

        public JSONObject post(String REQUEST_URL, Map<String, Object> params) {
            JSONObject jsonObject = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(REQUEST_URL);
                String postData = new ObjectMapper().writeValueAsString(params);
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");
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
                System.out.println("statusCode - " + statusCode);
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
}