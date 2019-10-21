package com.jmit.wmbreciever;

import java.util.Objects;

public class Bus {
    private double Latitude,longitude;
    private String status,name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bus)) return false;
        Bus bus = (Bus) o;
        return Double.compare(bus.getLatitude(), getLatitude()) == 0 &&
                Double.compare(bus.getLongitude(), getLongitude()) == 0 &&
                getStatus().equals(bus.getStatus()) &&
                getName().equals(bus.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude(), getStatus(), getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bus() {
    }

    @Override
    public String toString() {
        return "Bus{" +
                "Latitude=" + Latitude +
                ", longitude=" + longitude +
                ", status='" + status + '\'' +
                '}';
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
