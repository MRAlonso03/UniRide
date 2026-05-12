package com.uniride.app.repository;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.*;
import com.uniride.app.model.Trip;
import com.uniride.app.model.TripRequest;
import com.uniride.app.utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class TripRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Publicar un viaje nuevo
    public void publishTrip(Trip trip,
                            MutableLiveData<Boolean> successLiveData,
                            MutableLiveData<String> errorLiveData) {

        DocumentReference ref = db.collection(Constants.TRIPS_COLLECTION).document();
        trip.setTripId(ref.getId());
        trip.setStatus("active");

        ref.set(trip)
                .addOnSuccessListener(v -> successLiveData.setValue(true))
                .addOnFailureListener(e -> errorLiveData.setValue(e.getMessage()));
    }

    // Escuchar viajes activos en tiempo real
    public ListenerRegistration listenActiveTrips(
            MutableLiveData<List<Trip>> tripsLiveData,
            MutableLiveData<String> errorLiveData) {

        return db.collection(Constants.TRIPS_COLLECTION)
                .whereEqualTo("status", "active")
                .orderBy("departureTime", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        errorLiveData.setValue(error.getMessage());
                        return;
                    }
                    List<Trip> trips = new ArrayList<>();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots.getDocuments()) {
                            Trip t = doc.toObject(Trip.class);
                            if (t != null) trips.add(t);
                        }
                    }
                    tripsLiveData.setValue(trips);
                });
    }

    // Escuchar viajes del conductor actual
    public ListenerRegistration listenMyTrips(
            String driverUid,
            MutableLiveData<List<Trip>> tripsLiveData) {

        return db.collection(Constants.TRIPS_COLLECTION)
                .whereEqualTo("driverUid", driverUid)
                .addSnapshotListener((snapshots, e) -> {
                    List<Trip> trips = new ArrayList<>();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots.getDocuments()) {
                            Trip t = doc.toObject(Trip.class);
                            if (t != null) trips.add(t);
                        }
                    }
                    tripsLiveData.setValue(trips);
                });
    }

    // Pasajero solicita unirse a un viaje
    public void requestJoinTrip(TripRequest request,
                                MutableLiveData<Boolean> successLiveData,
                                MutableLiveData<String> errorLiveData) {

        DocumentReference ref = db.collection(Constants.REQUESTS_COLLECTION).document();
        request.setRequestId(ref.getId());
        request.setStatus("pending");
        request.setCreatedAt(Timestamp.now());

        ref.set(request)
                .addOnSuccessListener(v -> successLiveData.setValue(true))
                .addOnFailureListener(e -> errorLiveData.setValue(e.getMessage()));
    }

    // Conductor responde solicitud
    public void respondToRequest(String requestId, String newStatus,
                                 MutableLiveData<Boolean> successLiveData) {
        db.collection(Constants.REQUESTS_COLLECTION)
                .document(requestId)
                .update("status", newStatus)
                .addOnSuccessListener(v -> successLiveData.setValue(true));
    }

    // Escuchar solicitudes para un viaje específico
    public ListenerRegistration listenRequestsForTrip(
            String tripId,
            MutableLiveData<List<TripRequest>> requestsLiveData) {

        return db.collection(Constants.REQUESTS_COLLECTION)
                .whereEqualTo("tripId", tripId)
                .whereEqualTo("status", "pending")
                .addSnapshotListener((snapshots, e) -> {
                    List<TripRequest> list = new ArrayList<>();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots.getDocuments()) {
                            TripRequest r = doc.toObject(TripRequest.class);
                            if (r != null) list.add(r);
                        }
                    }
                    requestsLiveData.setValue(list);
                });
    }
}