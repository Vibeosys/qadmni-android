package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 21-01-2017.
 */
public class ConfirmOrderReqDTO extends BaseDTO {

    private long orderId;
    private String transactionId;
    private double amountInSAR;
    private double amountInUSD;
    private String paypalId;

    public ConfirmOrderReqDTO(long orderId, String transactionId, double amountInSAR,
                              double amountInUSD, String paypalId) {
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.amountInSAR = amountInSAR;
        this.amountInUSD = amountInUSD;
        this.paypalId = paypalId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmountInSAR() {
        return amountInSAR;
    }

    public void setAmountInSAR(double amountInSAR) {
        this.amountInSAR = amountInSAR;
    }

    public double getAmountInUSD() {
        return amountInUSD;
    }

    public void setAmountInUSD(double amountInUSD) {
        this.amountInUSD = amountInUSD;
    }

    public String getPaypalId() {
        return paypalId;
    }

    public void setPaypalId(String paypalId) {
        this.paypalId = paypalId;
    }
}
