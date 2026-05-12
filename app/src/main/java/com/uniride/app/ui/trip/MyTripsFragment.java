package com.uniride.app.ui.trip;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.uniride.app.adapter.RequestAdapter;
import com.uniride.app.databinding.FragmentMyTripsBinding;
import com.uniride.app.model.TripRequest;
import com.uniride.app.utils.Constants;
import com.uniride.app.viewmodel.TripViewModel;
import java.util.ArrayList;
import java.util.List;

public class MyTripsFragment extends Fragment {

    private FragmentMyTripsBinding binding;
    private TripViewModel viewModel;
    private RequestAdapter requestAdapter;
    private FirebaseFirestore db;
    private String currentUid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMyTripsBinding.inflate(
                inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        viewModel = new ViewModelProvider(requireActivity())
                .get(TripViewModel.class);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUid = FirebaseAuth.getInstance()
                    .getCurrentUser().getUid();
        }

        // Adapter con botones aceptar y rechazar
        requestAdapter = new RequestAdapter(
                request -> {
                    // Aceptar
                    viewModel.respondToRequest(
                            request.getRequestId(),
                            Constants.STATUS_ACCEPTED);
                    showToast("Solicitud aceptada");
                },
                request -> {
                    // Rechazar
                    viewModel.respondToRequest(
                            request.getRequestId(),
                            Constants.STATUS_REJECTED);
                    showToast("Solicitud rechazada");
                }
        );

        binding.recyclerRequests.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        binding.recyclerRequests.setAdapter(requestAdapter);

        loadPendingRequests();
    }

    private void loadPendingRequests() {
        if (currentUid == null) return;

        // Primero obtener los IDs de mis viajes
        db.collection(Constants.TRIPS_COLLECTION)
                .whereEqualTo("driverUid", currentUid)
                .get()
                .addOnSuccessListener(tripsSnapshot -> {

                    List<String> myTripIds = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : tripsSnapshot) {
                        myTripIds.add(doc.getId());
                    }

                    if (myTripIds.isEmpty()) {
                        showEmptyState();
                        return;
                    }

                    // Luego escuchar solicitudes pendientes de esos viajes
                    db.collection(Constants.REQUESTS_COLLECTION)
                            .whereIn("tripId", myTripIds)
                            .whereEqualTo("status", Constants.STATUS_PENDING)
                            .addSnapshotListener((snapshots, error) -> {
                                if (error != null || snapshots == null) return;

                                List<TripRequest> requests = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : snapshots) {
                                    TripRequest r = doc.toObject(TripRequest.class);
                                    if (r != null) requests.add(r);
                                }

                                requestAdapter.setRequests(requests);

                                if (requests.isEmpty()) {
                                    showEmptyState();
                                } else {
                                    hideEmptyState();
                                }
                            });
                })
                .addOnFailureListener(e -> showEmptyState());
    }

    private void showEmptyState() {
        if (binding == null) return;
        binding.layoutEmptyRequests.setVisibility(View.VISIBLE);
        binding.recyclerRequests.setVisibility(View.GONE);
    }

    private void hideEmptyState() {
        if (binding == null) return;
        binding.layoutEmptyRequests.setVisibility(View.GONE);
        binding.recyclerRequests.setVisibility(View.VISIBLE);
    }

    private void showToast(String message) {
        if (getContext() != null) {
            android.widget.Toast.makeText(
                    getContext(), message,
                    android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}