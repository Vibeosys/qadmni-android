package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by shrinivas on 23-01-2017.
 */
public class LiveOrdersResponseDTO {
    private long orderId;
    private String orderDate;
    private String producerBusinessName;
    private String paymentMode;
    private String deliveryMode;
    private double amountInSAR;
    private int stageNo;
    private String currentStatusCode;
    private int deliveryStatus;

    public LiveOrdersResponseDTO(long orderId, String orderDate, String producerBusinessName,
                                 String paymentMode, String deliveryMode, double amountInSAR, int stageNo,
                                 String currentStatusCode, int deliveryStatus) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.producerBusinessName = producerBusinessName;
        this.paymentMode = paymentMode;
        this.deliveryMode = deliveryMode;
        this.amountInSAR = amountInSAR;
        this.stageNo = stageNo;
        this.currentStatusCode = currentStatusCode;
        this.deliveryStatus = deliveryStatus;
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

    public double getAmountInSAR() {
        return amountInSAR;
    }

    public void setAmountInSAR(double amountInSAR) {
        this.amountInSAR = amountInSAR;
    }

    public int getStageNo() {
        return stageNo;
    }

    public void setStageNo(int stageNo) {
        this.stageNo = stageNo;
    }

    public String getCurrentStatusCode() {
        return currentStatusCode;
    }

    public void setCurrentStatusCode(String currentStatusCode) {
        this.currentStatusCode = currentStatusCode;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public static ArrayList<LiveOrdersResponseDTO> deserializeJson(String serializedString) {
        Gson gson = new Gson();

        LiveOrdersResponseDTO liveOrdersResponseDTO = null;
        LiveOrdersResponseDTO[] liveOrdersResponseDTOs = null;
        ArrayList<LiveOrdersResponseDTO> liveOrdersResponseDTOArrayList = new ArrayList<>();
        try {
            liveOrdersResponseDTOs = gson.fromJson(serializedString, LiveOrdersResponseDTO[].class);
            Collections.addAll(liveOrdersResponseDTOArrayList, liveOrdersResponseDTOs);
        } catch (JsonParseException e) {
            Log.d("TAG", "Exception in deserialization LivOrders" + e.toString());
        }
        return liveOrdersResponseDTOArrayList;
    }
}
