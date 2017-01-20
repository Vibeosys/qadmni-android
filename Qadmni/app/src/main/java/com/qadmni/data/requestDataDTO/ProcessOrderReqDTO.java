package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 20-01-2017.
 */
public class ProcessOrderReqDTO extends BaseDTO {

    private long orderId;
    private double orderAmountInSAR;
    private double orderAmountInUSD;

    public ProcessOrderReqDTO(long orderId, double orderAmountInSAR, double orderAmountInUSD) {
        this.orderId = orderId;
        this.orderAmountInSAR = orderAmountInSAR;
        this.orderAmountInUSD = orderAmountInUSD;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public double getOrderAmountInSAR() {
        return orderAmountInSAR;
    }

    public void setOrderAmountInSAR(double orderAmountInSAR) {
        this.orderAmountInSAR = orderAmountInSAR;
    }

    public double getOrderAmountInUSD() {
        return orderAmountInUSD;
    }

    public void setOrderAmountInUSD(double orderAmountInUSD) {
        this.orderAmountInUSD = orderAmountInUSD;
    }
}
