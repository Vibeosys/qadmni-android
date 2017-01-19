package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 19-01-2017.
 */
public class OrderItemResDTO extends BaseDTO {

    private static final String TAG = OrderItemResDTO.class.getSimpleName();
    private double unitPrice;
    private double itemTotalPrice;
    private String itemName;
    private long itemId;
    private int itemQty;

    public OrderItemResDTO() {
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(double itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public static ArrayList<OrderItemResDTO> deSerializedToJson(String serializedString) {
        Gson gson = new Gson();
        ArrayList<OrderItemResDTO> categoryListResponseDTOs = null;
        try {

            OrderItemResDTO[] deSerializedObject = gson.fromJson(serializedString, OrderItemResDTO[].class);
            categoryListResponseDTOs = new ArrayList<>();
            for (OrderItemResDTO listResponseDTO : deSerializedObject) {
                categoryListResponseDTOs.add(listResponseDTO);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
            Log.d(TAG, "Exception while deSerializing OrderItemResDTO List" + e.toString());
        }
        return categoryListResponseDTOs;
    }
}
