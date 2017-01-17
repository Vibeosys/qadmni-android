package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 17-01-2017.
 */
public class RegisterCustomerReqDTO extends BaseDTO {

    private String emailId;
    private String password;
    private String name;
    private String phone;

    public RegisterCustomerReqDTO(String emailId, String password, String name, String phone) {
        this.emailId = emailId;
        this.password = password;
        this.name = name;
        this.phone = phone;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
