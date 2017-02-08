package com.qadmni.data;

import java.util.ArrayList;

/**
 * Created by shrinivas on 17-01-2017.
 */
public class ProducerLocationDetailsDTO {
    private long producerId;
    private String businessName;
    private double businessLat;
    private double businessLong;
    private String userDistance;
    private String userTime;
    private double doubleDistance;

    public ProducerLocationDetailsDTO(long producerId, String businessName, double businessLat,
                                      double businessLong, String userDistance, String userTime,
                                      double doubleDistance) {
        this.producerId = producerId;
        this.businessName = businessName;
        this.businessLat = businessLat;
        this.businessLong = businessLong;
        this.userDistance = userDistance;
        this.userTime = userTime;
        this.doubleDistance = doubleDistance;
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

    public String getUserDistance() {
        return userDistance;
    }

    public void setUserDistance(String userDistance) {
        this.userDistance = userDistance;
    }

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    public double getDoubleDistance() {
        return doubleDistance;
    }

    public void setDoubleDistance(double doubleDistance) {
        this.doubleDistance = doubleDistance;
    }

    public static ProducerLocationDetailsDTO getProducerById(long producerId, ArrayList<ProducerLocationDetailsDTO> producerLocationDetailsDTOs) {
        ProducerLocationDetailsDTO producerLocationDetailsDTO = null;
        for (ProducerLocationDetailsDTO producer : producerLocationDetailsDTOs) {
            if (producer.getProducerId() == producerId) {
                producerLocationDetailsDTO = producer;
            } else {

            }
        }
        return producerLocationDetailsDTO;
    }
}
