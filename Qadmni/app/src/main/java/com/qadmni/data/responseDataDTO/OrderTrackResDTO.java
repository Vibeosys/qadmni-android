package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 10-02-2017.
 */
public class OrderTrackResDTO extends BaseDTO {

    private static final String TAG = OrderTrackResDTO.class.getSimpleName();
    private long orderId;
    private String deliveryAddress;
    private String timeRequiredInMinutes;
    private String currentStatusCode;
    private int stageNo;
    private int deliveryStatus;
    private String deliveryMode;

    public OrderTrackResDTO() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }


    public String getTimeRequiredInMinutes() {
        return timeRequiredInMinutes;
    }

    public void setTimeRequiredInMinutes(String timeRequiredInMinutes) {
        this.timeRequiredInMinutes = timeRequiredInMinutes;
    }

    public String getCurrentStatusCode() {
        return currentStatusCode;
    }

    public void setCurrentStatusCode(String currentStatusCode) {
        this.currentStatusCode = currentStatusCode;
    }

    public int getStageNo() {
        return stageNo;
    }

    public void setStageNo(int stageNo) {
        this.stageNo = stageNo;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public static OrderTrackResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        OrderTrackResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, OrderTrackResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization OrderTrackResDTO" + e.toString());
        }
        return responseDTO;
    }
}
