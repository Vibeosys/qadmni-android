package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.jar.JarException;

/**
 * Created by shrinivas on 23-01-2017.
 */
public class PastOrderResponseDTO {
    private long orderId;
    private String orderDate;
    private String producerBusinessName;
    private String paymentMode;
    private String deliveryMode;
    private String amountInSAR;
    private String deliveryStatus;
    private String deliveryStatusCode;

    public PastOrderResponseDTO(long orderId, String orderDate, String producerBusinessName,
                                String paymentMode, String deliveryMode, String amountInSAR,
                                String deliveryStatus, String deliveryStatusCode) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.producerBusinessName = producerBusinessName;
        this.paymentMode = paymentMode;
        this.deliveryMode = deliveryMode;
        this.amountInSAR = amountInSAR;
        this.deliveryStatus = deliveryStatus;
        this.deliveryStatusCode = deliveryStatusCode;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getProducerBusinessName() {
        return producerBusinessName;
    }

    public void setProducerBusinessName(String producerBusinessName) {
        this.producerBusinessName = producerBusinessName;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getAmountInSAR() {
        return amountInSAR;
    }

    public void setAmountInSAR(String amountInSAR) {
        this.amountInSAR = amountInSAR;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryStatusCode() {
        return deliveryStatusCode;
    }

    public void setDeliveryStatusCode(String deliveryStatusCode) {
        this.deliveryStatusCode = deliveryStatusCode;
    }

    public static ArrayList<PastOrderResponseDTO> deserialize(String jsonString)
    {
        Gson gson = new Gson();
        ArrayList<PastOrderResponseDTO> pastOrderResponseDTOs=new ArrayList<>();
        PastOrderResponseDTO[] pastOrderResponse=null;
        try
        {
            pastOrderResponse =gson.fromJson(jsonString,PastOrderResponseDTO[].class);
            Collections.addAll(pastOrderResponseDTOs,pastOrderResponse);

        }catch (JsonParseException e)
        {
            Log.d("TAG","Past order deserialize exception");
        }
        return pastOrderResponseDTOs;
    }
}
