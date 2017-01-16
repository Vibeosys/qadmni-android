package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 16-01-2017.
 */
public class VendorLoginResDTO extends BaseDTO {

    private static final String TAG = VendorLoginResDTO.class.getSimpleName();
    private long producerId;
    private String producerName;
    private String businessNameEn;
    private String businessNameAr;
    private String businessAddress;
    private double businessLat;
    private double businessLong;

    public VendorLoginResDTO() {
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getBusinessNameEn() {
        return businessNameEn;
    }

    public void setBusinessNameEn(String businessNameEn) {
        this.businessNameEn = businessNameEn;
    }

    public String getBusinessNameAr() {
        return businessNameAr;
    }

    public void setBusinessNameAr(String businessNameAr) {
        this.businessNameAr = businessNameAr;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
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

    public static VendorLoginResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        VendorLoginResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, VendorLoginResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization VendorLoginResDTO" + e.toString());
        }
        return responseDTO;
    }
}
