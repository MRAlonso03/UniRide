package com.uniride.app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.uniride.app.adapter.TripAdapter;
import com.uniride.app.databinding.FragmentPassengerBinding;
import com.uniride.app.ui.trip.TripDetailActivity;
import com.uniride.app.viewmodel.TripViewModel;

public class PassengerFragment extends Fragment {

    private FragmentPassengerBinding binding;
    private TripViewModel viewModel;
    private TripAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPassengerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(TripViewModel.class);

        adapter = new TripAdapter(trip -> {
            Intent intent = new Intent(requireContext(), TripDetailActivity.class);
            intent.putExtra("tripId", trip.getTripId());
            startActivity(intent);
        });

        binding.recyclerTrips.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        binding.recyclerTrips.setAdapter(adapter);

        viewModel.activeTrips.observe(getViewLifecycleOwner(), trips -> {
            adapter.setTrips(trips);
            if (trips.isEmpty()) {
                binding.layoutEmpty.setVisibility(View.VISIBLE);
                binding.recyclerTrips.setVisibility(View.GONE);
            } else {
                binding.layoutEmpty.setVisibility(View.GONE);
                binding.recyclerTrips.setVisibility(View.VISIBLE);
            }
        });

        viewModel.startListeningActiveTrips();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}