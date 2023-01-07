package com.example.turfbooking.ui.booknow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BookNowModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BookNowModel() {
        mText = new MutableLiveData<>();
        mText.setValue("BookNow screen ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}