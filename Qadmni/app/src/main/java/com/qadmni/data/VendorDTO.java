package com.qadmni.data;

/**
 * Created by akshay on 16-01-2017.
 */
public class VendorDTO {

    private long mProducerId;
    private String mProducerName;
    private String mBusinessNameEn;
    private String mBusinessNameAr;
    private String mBusinessAddress;
    private double mBusinessLat;
    private double mBusinessLong;
    private String mEmailId;
    private String mPassword;

    public VendorDTO(long mProducerId, String mProducerName, String mBusinessNameEn,
                     String mBusinessNameAr, String mBusinessAddress, double mBusinessLat,
                     double mBusinessLong) {
        this.mProducerId = mProducerId;
        this.mProducerName = mProducerName;
        this.mBusinessNameEn = mBusinessNameEn;
        this.mBusinessNameAr = mBusinessNameAr;
        this.mBusinessAddress = mBusinessAddress;
        this.mBusinessLat = mBusinessLat;
        this.mBusinessLong = mBusinessLong;
    }

    public long getProducerId() {
        return mProducerId;
    }

    public void setProducerId(long mProducerId) {
        this.mProducerId = mProducerId;
    }

    public String getProducerName() {
        return mProducerName;
    }

    public void setProducerName(String mProducerName) {
        this.mProducerName = mProducerName;
    }

    public String getBusinessNameEn() {
        return mBusinessNameEn;
    }

    public void setBusinessNameEn(String mBusinessNameEn) {
        this.mBusinessNameEn = mBusinessNameEn;
    }

    public String getBusinessNameAr() {
        return mBusinessNameAr;
    }

    public void setBusinessNameAr(String mBusinessNameAr) {
        this.mBusinessNameAr = mBusinessNameAr;
    }

    public String getBusinessAddress() {
        return mBusinessAddress;
    }

    public void setBusinessAddress(String mBusinessAddress) {
        this.mBusinessAddress = mBusinessAddress;
    }

    public double getBusinessLat() {
        return mBusinessLat;
    }

    public void setBusinessLat(double mBusinessLat) {
        this.mBusinessLat = mBusinessLat;
    }

    public double getBusinessLong() {
        return mBusinessLong;
    }

    public void setBusinessLong(double mBusinessLong) {
        this.mBusinessLong = mBusinessLong;
    }

    public String getEmailId() {
        return mEmailId;
    }

    public void setEmailId(String mEmailId) {
        this.mEmailId = mEmailId;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
