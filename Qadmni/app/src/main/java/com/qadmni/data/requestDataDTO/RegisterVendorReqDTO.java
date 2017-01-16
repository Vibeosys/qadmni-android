package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

import java.io.Serializable;

/**
 * Created by akshay on 14-01-2017.
 */
public class RegisterVendorReqDTO extends BaseDTO implements Serializable {

    private String producerName;
    private String emailId;
    private String password;
    private String businessNameEn;
    private String businessNameAr;
    private String businessAddress;
    private double businessLat;
    private double businessLong;
    private String pushNotificationId;
    private String osVersionType;

    public RegisterVendorReqDTO() {
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPushNotificationId() {
        return pushNotificationId;
    }

    public void setPushNotificationId(String pushNotificationId) {
        this.pushNotificationId = pushNotificationId;
    }

    public String getOsVersionType() {
        return osVersionType;
    }

    public void setOsVersionType(String osVersionType) {
        this.osVersionType = osVersionType;
    }
}
