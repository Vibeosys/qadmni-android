package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 16-01-2017.
 */
public class VendorLoginReqDTO extends BaseDTO {
    private String emailId;
    private String password;
    private String pushNotificationId;
    private String pushDeviceOsType;

    public VendorLoginReqDTO(String emailId, String password, String pushNotificationId, String pushDeviceOsType) {
        this.emailId = emailId;
        this.password = password;
        this.pushNotificationId = pushNotificationId;
        this.pushDeviceOsType = pushDeviceOsType;
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

    public String getPushNotificationId() {
        return pushNotificationId;
    }

    public void setPushNotificationId(String pushNotificationId) {
        this.pushNotificationId = pushNotificationId;
    }

    public String getPushDeviceOsType() {
        return pushDeviceOsType;
    }

    public void setPushDeviceOsType(String pushDeviceOsType) {
        this.pushDeviceOsType = pushDeviceOsType;
    }
}
