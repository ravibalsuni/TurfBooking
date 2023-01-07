package com.example.turfbooking.ui.booknow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.turfbooking.databinding.FragmentBooknowBinding;

public class BookNowFragment extends Fragment {

    private FragmentBooknowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BookNowModel galleryViewModel =
                new ViewModelProvider(this).get(BookNowModel.class);

        binding = FragmentBooknowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textBookNow;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}