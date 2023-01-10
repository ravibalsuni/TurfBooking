package com.example.turfbooking;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {
    private Button turfReportButton,bookingReportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("inside report activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        turfReportButton = findViewById(R.id.btn_report_turf);
        bookingReportButton = findViewById(R.id.btn_booking_report);
        turfReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "turfReportButton", Toast.LENGTH_LONG).show();
            }
        });

        bookingReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "bookingReportButton", Toast.LENGTH_LONG).show();
            }
        });

    }
}
