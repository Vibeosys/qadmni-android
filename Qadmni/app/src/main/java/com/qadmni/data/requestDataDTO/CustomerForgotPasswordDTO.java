package com.qadmni.data.requestDataDTO;

/**
 * Created by shrinivas on 23-01-2017.
 */
public class CustomerForgotPasswordDTO {
    private String emailId;

    public CustomerForgotPasswordDTO(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
