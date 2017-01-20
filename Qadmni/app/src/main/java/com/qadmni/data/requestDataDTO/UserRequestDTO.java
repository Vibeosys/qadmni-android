package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by shrinivas on 12-01-2017.
 */
public class UserRequestDTO extends BaseDTO {

    private long customerId;
    private String password;

    public UserRequestDTO() {
    }

    public UserRequestDTO(long customerId, String password) {
        this.customerId = customerId;
        this.password = password;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
