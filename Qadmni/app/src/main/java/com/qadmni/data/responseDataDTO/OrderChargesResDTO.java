package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 19-01-2017.
 */
public class OrderChargesResDTO extends BaseDTO {

    private static final String TAG = OrderChargesResDTO.class.getSimpleName();
    private String chargeDetails;
    private double amount;

    public OrderChargesResDTO() {
    }

    public String getChargeDetails() {
        return chargeDetails;
    }

    public void setChargeDetails(String chargeDetails) {
        this.chargeDetails = chargeDetails;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public static ArrayList<OrderChargesResDTO> deSerializedToJson(String serializedString) {
        Gson gson = new Gson();
        ArrayList<OrderChargesResDTO> orderChargesResDTOs = null;
        try {

            OrderChargesResDTO[] deSerializedObject = gson.fromJson(serializedString, OrderChargesResDTO[].class);
            orderChargesResDTOs = new ArrayList<>();
            for (OrderChargesResDTO listResponseDTO : deSerializedObject) {
                orderChargesResDTOs.add(listResponseDTO);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
            Log.d(TAG, "Exception while deSerializing OrderItemResDTO List" + e.toString());
        }
        return orderChargesResDTOs;
    }
}
