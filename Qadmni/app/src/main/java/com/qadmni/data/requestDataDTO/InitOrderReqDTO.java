package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 19-01-2017.
 */
public class InitOrderReqDTO extends BaseDTO {

    private ArrayList<OrderItemDTO> productInfo;
    private String deliveryAddress;
    private double deliveryLat;
    private double deliveryLong;
    private String deliveryMethod;
    private long deliverySchedule;
    private String paymentMethod;

    public InitOrderReqDTO(ArrayList<OrderItemDTO> productInfo, String deliveryAddress,
                           double deliveryLat, double deliveryLong, String deliveryMethod,
                           long deliverySchedule, String paymentMethod) {
        this.productInfo = productInfo;
        this.deliveryAddress = deliveryAddress;
        this.deliveryLat = deliveryLat;
        this.deliveryLong = deliveryLong;
        this.deliveryMethod = deliveryMethod;
        this.deliverySchedule = deliverySchedule;
        this.paymentMethod = paymentMethod;
    }

    public ArrayList<OrderItemDTO> getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ArrayList<OrderItemDTO> productInfo) {
        this.productInfo = productInfo;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public double getDeliveryLat() {
        return deliveryLat;
    }

    public void setDeliveryLat(double deliveryLat) {
        this.deliveryLat = deliveryLat;
    }

    public double getDeliveryLong() {
        return deliveryLong;
    }

    public void setDeliveryLong(double deliveryLong) {
        this.deliveryLong = deliveryLong;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public long getDeliverySchedule() {
        return deliverySchedule;
    }

    public void setDeliverySchedule(long deliverySchedule) {
        this.deliverySchedule = deliverySchedule;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
