package com.qadmni.data;

/**
 * Created by akshay on 16-01-2017.
 */
public class CustomerDTO {

    private long mCustomerId;
    private String mName;
    private String mPhone;
    private String mEmailId;
    private String mPassword;

    public CustomerDTO(long mCustomerId, String mName, String mPhone) {
        this.mCustomerId = mCustomerId;
        this.mName = mName;
        this.mPhone = mPhone;
    }

    public long getCustomerId() {
        return mCustomerId;
    }

    public void setCustomerId(long mCustomerId) {
        this.mCustomerId = mCustomerId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getEmailId() {
        return mEmailId;
    }

    public void setEmailId(String mEmailId) {
        this.mEmailId = mEmailId;
    }
}
