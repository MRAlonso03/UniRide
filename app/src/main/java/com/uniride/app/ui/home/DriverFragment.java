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
import com.google.firebase.auth.FirebaseAuth;
import com.uniride.app.adapter.TripAdapter;
import com.uniride.app.databinding.FragmentDriverBinding;
import com.uniride.app.ui.trip.PublishTripActivity;
import com.uniride.app.ui.trip.TripDetailActivity;
import com.uniride.app.viewmodel.TripViewModel;

public class DriverFragment extends Fragment {

    private FragmentDriverBinding binding;
    private TripViewModel viewModel;
    private TripAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDriverBinding.inflate(inflater, container, false);
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

        binding.recyclerMyTrips.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        binding.recyclerMyTrips.setAdapter(adapter);

        // Escuchar mis viajes publicados
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (uid != null) {
            viewModel.startListeningMyTrips(uid);
        }

        viewModel.myTrips.observe(getViewLifecycleOwner(), trips -> {
            adapter.setTrips(trips);
        });

        // FAB para publicar nuevo viaje
        binding.fabPublish.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), PublishTripActivity.class))
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}