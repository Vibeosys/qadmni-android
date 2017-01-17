package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 16-01-2017.
 */
public class CustomerLoginResDTO extends BaseDTO {
    private static final String TAG = CustomerLoginResDTO.class.getSimpleName();
    private long customerId;
    private String name;
    private String phone;

    public CustomerLoginResDTO() {
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
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

    public static CustomerLoginResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        CustomerLoginResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, CustomerLoginResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization CustomerLoginResDTO" + e.toString());
        }
        return responseDTO;
    }
}
