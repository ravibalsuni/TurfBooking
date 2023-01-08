package com.example.turfbooking.ui.addturf;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.turfbooking.ApproveBookingActivity;
import com.example.turfbooking.OwnerLoginActivity;
import com.example.turfbooking.R;
import com.example.turfbooking.constants.Constants;
import com.example.turfbooking.databinding.FragmentAddturfBinding;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AddTurfFragment extends Fragment {
    String TEST_URL= Constants.BASE_URL+"/api/turf/save";
    private FragmentAddturfBinding binding;
    private EditText etTurfName,etTurfPin,etTurfAddress,etOwnerId;
    private String turfName,turfPin,turfAddress,ownerId, status;
    private Spinner dropdown;
    private Button submit;
    private String myResponse;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddturfBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dropdown = root.findViewById(R.id.spinnerTurfStatus);
        String[] items = new String[]{"Available", "Not Available"};
        ArrayAdapter<CharSequence> mSortAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, items);
        mSortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(mSortAdapter);

        etTurfName=root.findViewById(R.id.etTurfName);
        etTurfPin=root.findViewById(R.id.etTurfPin);
        etTurfAddress=root.findViewById(R.id.etTurfAddress);
        etOwnerId=root.findViewById(R.id.etOwnerId);
        submit=root.findViewById(R.id.btnAddTurfSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turfName=etTurfName.getText().toString().trim();
                turfPin=etTurfPin.getText().toString().trim();
                turfAddress=etTurfAddress.getText().toString().trim();
                ownerId=etOwnerId.getText().toString().trim();
                status=dropdown.getSelectedItem().toString();

                if (turfName.isEmpty() || turfPin.isEmpty() || turfAddress.isEmpty() || ownerId.isEmpty()) {
                    Toast.makeText(getActivity(), "Fields can not be blank", Toast.LENGTH_SHORT).show();
                } else {
                    new PostAsyncTask().execute();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class PostAsyncTask extends AsyncTask<String,Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            Map postData = new HashMap();
            postData.put("id",1);
            postData.put("name",turfName);
            postData.put("pin",turfPin);
            postData.put("address",turfAddress);
            postData.put("ownerId",ownerId);
            postData.put("turfStatus",status);
            postData.put("approvalStatus","Pending");
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
                    Toast.makeText(getActivity(), "save success", Toast.LENGTH_LONG).show();
                    etTurfName.setText("");
                    etTurfPin.setText("");
                    etTurfAddress.setText("");
                    etOwnerId.setText("");
                }else {
                    Toast.makeText(getActivity(), "save error", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), String.format("%s","Something went wrong!!!!!!"), Toast.LENGTH_LONG).show();
            }
            myResponse=response.toString();
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