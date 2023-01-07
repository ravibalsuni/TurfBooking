package com.example.turfbooking.ui.approvebooking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ApproveBookingModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ApproveBookingModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pending Bookings Approval screen ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}