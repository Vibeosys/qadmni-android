package com.qadmni.data.requestDataDTO;

/**
 * Created by shrinivas on 06-02-2017.
 */
public class UpdateDeliveryStatus {
    long orderId;
    int deliveryStatusId;

    public UpdateDeliveryStatus(long orderId, int deliveryStatusId) {
        this.orderId = orderId;
        this.deliveryStatusId = deliveryStatusId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getDeliveryStatusId() {
        return deliveryStatusId;
    }

    public void setDeliveryStatusId(int deliveryStatusId) {
        this.deliveryStatusId = deliveryStatusId;
    }
}
