package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 19-01-2017.
 */
public class InitOrderResDTO extends BaseDTO {

    private static final String TAG = InitOrderResDTO.class.getSimpleName();
    private long orderId;
    private ArrayList<OrderItemResDTO> orderedItems;
    private ArrayList<OrderChargesResDTO> chargeBreakup;
    private double totalAmountInSAR;
    private double totalAmountInUSD;

    public InitOrderResDTO() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public ArrayList<OrderItemResDTO> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(ArrayList<OrderItemResDTO> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public ArrayList<OrderChargesResDTO> getChargeBreakup() {
        return chargeBreakup;
    }

    public void setChargeBreakup(ArrayList<OrderChargesResDTO> chargeBreakup) {
        this.chargeBreakup = chargeBreakup;
    }

    public double getTotalAmountInSAR() {
        return totalAmountInSAR;
    }

    public void setTotalAmountInSAR(double totalAmountInSAR) {
        this.totalAmountInSAR = totalAmountInSAR;
    }

    public double getTotalAmountInUSD() {
        return totalAmountInUSD;
    }

    public void setTotalAmountInUSD(double totalAmountInUSD) {
        this.totalAmountInUSD = totalAmountInUSD;
    }

    public static InitOrderResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        InitOrderResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, InitOrderResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization InitOrderResDTO" + e.toString());
        }
        return responseDTO;
    }
}
