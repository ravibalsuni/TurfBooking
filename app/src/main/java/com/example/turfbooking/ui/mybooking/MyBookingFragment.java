package com.example.turfbooking.ui.mybooking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turfbooking.constants.Constants;
import com.example.turfbooking.databinding.FragmentMybookingBinding;
import com.example.turfbooking.global.GlobalClass;
import com.example.turfbooking.ui.approveturf.ItemClickListener;
import com.example.turfbooking.ui.approveturf.TurfModel;
import com.example.turfbooking.ui.booknow.BookNowFragment;
import com.example.turfbooking.ui.booknow.MyAdapterBookNow;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MyBookingFragment extends Fragment implements ItemClickListener {

    private FragmentMybookingBinding binding;
    String userid,role;
    TurfBookingModel[] myListData1;
    MyBookingAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final GlobalClass globalVariable = (GlobalClass) getActivity().getApplicationContext();
        userid  = globalVariable.getUserID();
        role = globalVariable.getRole();
        binding = FragmentMybookingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        String result=null;
        try {
            result = new GetAsyncTask2().execute().get();
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
            myListData1 = new TurfBookingModel[data.length()];
            for (int i = 0; i < data.length(); i++) {
                myListData1[i] = new Gson().fromJson(data.getJSONObject(i).toString(), TurfBookingModel.class);
            }
            for (TurfBookingModel m : myListData1)
                System.out.println("model -" + m);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("myListData1 length - " + myListData1.length);
        final RecyclerView recyclerView = binding.recyclerViewMyBooking;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        adapter = new MyBookingAdapter(myListData1);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view, int pos) throws ExecutionException, InterruptedException {
        System.out.println("pos - " + pos);
        System.out.println("data based on pos - " + myListData1[pos]);
    }

    private class GetAsyncTask2 extends AsyncTask<String, String, String> {
        String API_URL = Constants.BASE_URL + "/api/turf/booking/search/bookingstatus?bookingStatus=pending&createdBy="+userid;
        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    System.out.println("API-URL - "+API_URL);
                    url = new URL(API_URL);

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
}