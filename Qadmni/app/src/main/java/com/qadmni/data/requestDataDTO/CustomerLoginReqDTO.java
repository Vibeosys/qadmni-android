package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 16-01-2017.
 */
public class CustomerLoginReqDTO extends BaseDTO {

    private String emailId;
    private String password;
    private String pushId;
    private String osVersionType;

    public CustomerLoginReqDTO(String emailId, String password, String pushId, String osVersionType) {
        this.emailId = emailId;
        this.password = password;
        this.pushId = pushId;
        this.osVersionType = osVersionType;
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

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getOsVersionType() {
        return osVersionType;
    }

    public void setOsVersionType(String osVersionType) {
        this.osVersionType = osVersionType;
    }
}
