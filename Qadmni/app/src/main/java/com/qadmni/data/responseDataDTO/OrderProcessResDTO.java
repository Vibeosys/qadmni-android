package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 20-01-2017.
 */
public class OrderProcessResDTO extends BaseDTO {

    private static final String TAG = OrderProcessResDTO.class.getSimpleName();
    private long orderId;
    private boolean transactionRequired;
    private String transactionId;
    private double amount;
    private String currency;
    private PayPalData paypalEnvValues;

    public OrderProcessResDTO() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public boolean isTransactionRequired() {
        return transactionRequired;
    }

    public void setTransactionRequired(boolean transactionRequired) {
        this.transactionRequired = transactionRequired;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PayPalData getPaypalEnvValues() {
        return paypalEnvValues;
    }

    public void setPaypalEnvValues(PayPalData paypalEnvValues) {
        this.paypalEnvValues = paypalEnvValues;
    }

    public static OrderProcessResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        OrderProcessResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, OrderProcessResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization OrderProcessResDTO" + e.toString());
        }
        return responseDTO;
    }
}
