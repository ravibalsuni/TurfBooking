package com.example.turfbooking;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.turfbooking.constants.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReportActivity extends AppCompatActivity {
    private Button turfReportButton,bookingReportButton;

    int pageHeight = 1120;
    int pagewidth = 792;

    Bitmap bmp, scaledbmp;

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.out.println("inside report activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        turfReportButton = findViewById(R.id.btn_report_turf);
        bookingReportButton = findViewById(R.id.btn_booking_report);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.turf);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
        turfReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data= "Turf Data";
                data = new GetAsyncTask().doInBackground(Constants.BASE_URL+"/api/turf/search");
                data=data.replace("},{","},\r\n{");
                data=data.replace("[{","[\r\n{");
                data=data.replace("}]","}\r\n]");
                data=data.replace(",\"",",\r\n\"");
                data=data.replace("{\"","{\r\n\"");
                data=data.replace("},","}\r\n,");
                data=data.replace("\"}","\"\r\n}");
               // System.out.println("data - "+data);
                generatePDF("turfdata.pdf", data);
                Toast.makeText(getApplicationContext(), "turfdata.pdf", Toast.LENGTH_LONG).show();
            }
        });

        bookingReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data= "Turf Booking Data";
                data = new GetAsyncTask().doInBackground(Constants.BASE_URL+"/api/turf/booking/search");
                data=data.replace("},{","},\r\n{");
                data=data.replace("[{","[\r\n{");
                data=data.replace("}]","}\r\n]");
                data=data.replace(",\"",",\r\n\"");
                data=data.replace("{\"","{\r\n\"");
                data=data.replace("},","}\r\n,");
                data=data.replace("\"}","\"\r\n}");
               // System.out.println("data - "+data);
                generatePDF( "turfbookingdata.pdf", data);
                Toast.makeText(getApplicationContext(), "turfbookingdata.pdf", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void generatePDF(String name,String data) {
        //System.out.println("generatePDF - "+data);
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        Canvas canvas = myPage.getCanvas();

        canvas.drawBitmap(scaledbmp, 56, 40, paint);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        title.setTextSize(15);

        title.setColor(ContextCompat.getColor(this, R.color.purple_200));


        canvas.drawText("For Sports Professionals!", 209, 100, title);
        canvas.drawText("Book My Turf", 209, 80, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));
        title.setTextSize(15);


        title.setTextAlign(Paint.Align.LEFT);
        System.out.println("data before canvas -"+data);
        canvas.drawText(data, 396, 560, title);
        pdfDocument.finishPage(myPage);
        File directory = new ContextWrapper(getApplicationContext()).getExternalFilesDir("Downloads");
        File file = new File(directory, name);
        System.out.println("file -"+file);
        try {

            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(ReportActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ReportActivity.this, "PDF generation error.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        pdfDocument.close();
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private class GetAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String getUrl= params[0];
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                System.out.println("USER_PROFILE - "+getUrl);
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(getUrl);

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
