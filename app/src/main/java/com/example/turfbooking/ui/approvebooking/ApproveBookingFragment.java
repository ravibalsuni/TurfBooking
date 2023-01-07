package com.example.turfbooking.ui.approvebooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.turfbooking.databinding.FragmentApprovebookingBinding;

public class ApproveBookingFragment extends Fragment {

    private FragmentApprovebookingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ApproveBookingModel galleryViewModel =
                new ViewModelProvider(this).get(ApproveBookingModel.class);

        binding = FragmentApprovebookingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPendingApproval;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}