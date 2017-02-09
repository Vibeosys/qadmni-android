package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 08-02-2017.
 */
public class OrderDetailsResDTO extends BaseDTO {
    private static final String TAG = OrderDetailsResDTO.class.getSimpleName();
    private long orderId;
    private String orderDate;
    private double totalAmountInSAR;
    private double totalTaxesAndSurcharges;
    private ArrayList<OrderItems> items;

    public OrderDetailsResDTO() {
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

    public double getTotalAmountInSAR() {
        return totalAmountInSAR;
    }

    public void setTotalAmountInSAR(double totalAmountInSAR) {
        this.totalAmountInSAR = totalAmountInSAR;
    }

    public double getTotalTaxesAndSurcharges() {
        return totalTaxesAndSurcharges;
    }

    public void setTotalTaxesAndSurcharges(double totalTaxesAndSurcharges) {
        this.totalTaxesAndSurcharges = totalTaxesAndSurcharges;
    }

    public ArrayList<OrderItems> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderItems> items) {
        this.items = items;
    }

    public static OrderDetailsResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        OrderDetailsResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, OrderDetailsResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization OrderDetailsResDTO" + e.toString());
        }
        return responseDTO;
    }
}
