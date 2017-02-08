package com.qadmni.data.responseDataDTO;

/**
 * Created by shrinivas on 08-02-2017.
 */
public class OrderDetailsResDTO {
    private long orderId;
    private String orderDate;
    private long totalAmountInSAR;
    private long totalTaxesAndSurCharges;
    private OrderItems[]orderItems;

    public OrderDetailsResDTO(long orderId, String orderDate, long totalAmountInSAR, long totalTaxesAndSurCharges, OrderItems[] orderItems) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmountInSAR = totalAmountInSAR;
        this.totalTaxesAndSurCharges = totalTaxesAndSurCharges;
        this.orderItems = orderItems;
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

    public long getTotalAmountInSAR() {
        return totalAmountInSAR;
    }

    public void setTotalAmountInSAR(long totalAmountInSAR) {
        this.totalAmountInSAR = totalAmountInSAR;
    }

    public long getTotalTaxesAndSurCharges() {
        return totalTaxesAndSurCharges;
    }

    public void setTotalTaxesAndSurCharges(long totalTaxesAndSurCharges) {
        this.totalTaxesAndSurCharges = totalTaxesAndSurCharges;
    }

    public OrderItems[] getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(OrderItems[] orderItems) {
        this.orderItems = orderItems;
    }

   
}
