package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 16-01-2017.
 */
public class VendorReqDTO extends BaseDTO {

    private long producerId;
    private String password;

    public VendorReqDTO(long producerId, String password) {
        this.producerId = producerId;
        this.password = password;
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
