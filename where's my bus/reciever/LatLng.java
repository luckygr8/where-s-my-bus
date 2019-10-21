package com.jmit.wmbreciever;

public class LatLng {
    private Double Latitude;
    private Double Longitude;

    public LatLng(Double latitude, Double longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }

    public LatLng() {
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }
}
