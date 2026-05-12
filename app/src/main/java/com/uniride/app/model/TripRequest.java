package com.uniride.app.model;

import com.google.firebase.Timestamp;

public class TripRequest {

    private String requestId;
    private String tripId;
    private String passengerUid;
    private String passengerName;
    private String passengerPhotoUrl;
    private String status;
    private Timestamp createdAt;

    public TripRequest() {}

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }

    public String getPassengerUid() { return passengerUid; }
    public void setPassengerUid(String passengerUid) { this.passengerUid = passengerUid; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public String getPassengerPhotoUrl() { return passengerPhotoUrl; }
    public void setPassengerPhotoUrl(String passengerPhotoUrl) { this.passengerPhotoUrl = passengerPhotoUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}