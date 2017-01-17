package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;

/**
 * Created by shrinivas on 16-01-2017.
 */
public class ProducerLocations {
    private long producerId;
    private String businessName;
    private double businessLat;
    private double businessLong;

    public ProducerLocations(long producerId, String businessName, long businessLat, long businessLong) {
        this.producerId = producerId;
        this.businessName = businessName;
        this.businessLat = businessLat;
        this.businessLong = businessLong;
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

    public void setBusinessLat(long businessLat) {
        this.businessLat = businessLat;
    }

    public double getBusinessLong() {
        return businessLong;
    }

    public void setBusinessLong(long businessLong) {
        this.businessLong = businessLong;
    }

    public static ArrayList<ProducerLocations> deSerializedToJson(ProducerLocations[] deSerializedObject) {
        Gson gson = new Gson();
        ArrayList<ProducerLocations> producerLocationseDTOs = null;
        try {
            producerLocationseDTOs = new ArrayList<>();
            for (ProducerLocations producerLocations : deSerializedObject) {
                producerLocationseDTOs.add(producerLocations);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
            Log.d("TAG", "Exception while deSerializing Category List" + e.toString());
        }
        return producerLocationseDTOs;
    }

}
