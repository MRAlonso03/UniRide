package com.uniride.app.model;

import com.google.firebase.Timestamp;

public class Trip {
    private String tripId;
    private String driverUid;
    private String driverName;
    private String driverPhotoUrl;
    private String originName;
    private String destinationName;
    private double originLat, originLng;
    private double destinationLat, destinationLng;
    private Timestamp departureTime;
    private int availableSeats;
    private String status; // "active", "completed", "cancelled"

    public Trip() {} // Requerido por Firestore

    // Getters y Setters
    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }

    public String getDriverUid() { return driverUid; }
    public void setDriverUid(String driverUid) { this.driverUid = driverUid; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getDriverPhotoUrl() { return driverPhotoUrl; }
    public void setDriverPhotoUrl(String p) { this.driverPhotoUrl = p; }

    public String getOriginName() { return originName; }
    public void setOriginName(String o) { this.originName = o; }

    public String getDestinationName() { return destinationName; }
    public void setDestinationName(String d) { this.destinationName = d; }

    public double getOriginLat() { return originLat; }
    public void setOriginLat(double v) { this.originLat = v; }

    public double getOriginLng() { return originLng; }
    public void setOriginLng(double v) { this.originLng = v; }

    public double getDestinationLat() { return destinationLat; }
    public void setDestinationLat(double v) { this.destinationLat = v; }

    public double getDestinationLng() { return destinationLng; }
    public void setDestinationLng(double v) { this.destinationLng = v; }

    public Timestamp getDepartureTime() { return departureTime; }
    public void setDepartureTime(Timestamp t) { this.departureTime = t; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int s) { this.availableSeats = s; }

    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
}