package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 20-01-2017.
 */
public class PayPalData extends BaseDTO {

    private static final String TAG = PayPalData.class.getSimpleName();
    private String paypalDesc;
    private String environment;
    private String clientId;

    public PayPalData() {
    }

    public String getPaypalDesc() {
        return paypalDesc;
    }

    public void setPaypalDesc(String paypalDesc) {
        this.paypalDesc = paypalDesc;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public static PayPalData deserializeJson(String serializedString) {
        Gson gson = new Gson();
        PayPalData responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, PayPalData.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization PayPalData" + e.toString());
        }
        return responseDTO;
    }
}
