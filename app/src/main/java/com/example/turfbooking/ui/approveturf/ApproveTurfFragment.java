package com.example.turfbooking.ui.approveturf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.turfbooking.databinding.FragmentAddturfBinding;
import com.example.turfbooking.databinding.FragmentApproveturfBinding;

public class ApproveTurfFragment extends Fragment {

    private FragmentApproveturfBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentApproveturfBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       final EditText editTextView = binding.etApproveTurf;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}