package com.uniride.app.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.ListenerRegistration;
import com.uniride.app.model.Trip;
import com.uniride.app.model.TripRequest;
import com.uniride.app.repository.TripRepository;
import java.util.List;

public class TripViewModel extends ViewModel {
    private final TripRepository repo = new TripRepository();
    private ListenerRegistration activeTripsListener;
    private ListenerRegistration myTripsListener;

    public final MutableLiveData<List<Trip>> activeTrips = new MutableLiveData<>();
    public final MutableLiveData<List<Trip>> myTrips = new MutableLiveData<>();
    public final MutableLiveData<List<TripRequest>> requests = new MutableLiveData<>();
    public final MutableLiveData<Boolean> publishSuccess = new MutableLiveData<>();
    public final MutableLiveData<Boolean> requestSuccess = new MutableLiveData<>();
    public final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void startListeningActiveTrips() {
        activeTripsListener = repo.listenActiveTrips(activeTrips, errorMessage);
    }

    public void startListeningMyTrips(String driverUid) {
        myTripsListener = repo.listenMyTrips(driverUid, myTrips);
    }

    public void publishTrip(Trip trip) {
        repo.publishTrip(trip, publishSuccess, errorMessage);
    }

    public void requestJoinTrip(TripRequest request) {
        repo.requestJoinTrip(request, requestSuccess, errorMessage);
    }

    public void respondToRequest(String requestId, String status) {
        repo.respondToRequest(requestId, status, new MutableLiveData<>());
    }

    @Override
    protected void onCleared() {
        if (activeTripsListener != null) activeTripsListener.remove();
        if (myTripsListener != null) myTripsListener.remove();
        super.onCleared();
    }
}