package com.qadmni.data;

/**
 * Created by shrinivas on 17-01-2017.
 */
public class ProducerLocationDetailsDTO {
    private long producerId;
    private String businessName;
    private double businessLat;
    private double businessLong;
    private double userLat;
    private double userLon;
    private float userDistance;
    private float userTime;

    public ProducerLocationDetailsDTO(long producerId, String businessName, double businessLat,
                                      double businessLong, double userLat, double userLon,
                                      float userDistance, float userTime) {
        this.producerId = producerId;
        this.businessName = businessName;
        this.businessLat = businessLat;
        this.businessLong = businessLong;
        this.userLat = userLat;
        this.userLon = userLon;
        this.userDistance = userDistance;
        this.userTime = userTime;
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public double getBusinessLat() {
        return businessLat;
    }

    public void setBusinessLat(double businessLat) {
        this.businessLat = businessLat;
    }

    public double getBusinessLong() {
        return businessLong;
    }

    public void setBusinessLong(double businessLong) {
        this.businessLong = businessLong;
    }

    public double getUserLat() {
        return userLat;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }

    public double getUserLon() {
        return userLon;
    }

    public void setUserLon(double userLon) {
        this.userLon = userLon;
    }

    public float getUserDistance() {
        return userDistance;
    }

    public void setUserDistance(float userDistance) {
        this.userDistance = userDistance;
    }

    public float getUserTime() {
        return userTime;
    }

    public void setUserTime(float userTime) {
        this.userTime = userTime;
    }
}
