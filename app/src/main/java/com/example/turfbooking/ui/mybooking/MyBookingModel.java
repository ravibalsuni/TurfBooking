package com.example.turfbooking.ui.mybooking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyBookingModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyBookingModel() {
        mText = new MutableLiveData<>();
        mText.setValue("You don't have any upcoming Booking");
    }

    public LiveData<String> getText() {
        return mText;
    }
}