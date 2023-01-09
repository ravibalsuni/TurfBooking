package com.example.turfbooking.ui.approveturf;

import android.view.View;

import java.util.concurrent.ExecutionException;

public interface ItemClickListener {
    void onClick(View view, int pos) throws ExecutionException, InterruptedException;
}
