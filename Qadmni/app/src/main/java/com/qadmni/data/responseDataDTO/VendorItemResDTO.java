package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by akshay on 16-01-2017.
 */
public class VendorItemResDTO extends BaseDTO implements Serializable {
    private static final String TAG = VendorItemResDTO.class.getSimpleName();
    private long itemId;
    private String itemName;
    private String itemDesc;
    private String imageUrl;
    private double price;
    private String category;
    private int availableForSell;

    public VendorItemResDTO() {
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAvailableForSell() {
        return availableForSell;
    }

    public void setAvailableForSell(int availableForSell) {
        this.availableForSell = availableForSell;
    }

    public static VendorItemResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        VendorItemResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, VendorItemResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization VendorItemResDTO" + e.toString());
        }
        return responseDTO;
    }

    public static ArrayList<VendorItemResDTO> deSerializeToArray(String serializedString) {
        Gson gson = new Gson();
        ArrayList<VendorItemResDTO> vendorItemResDTOs = null;
        try {

            VendorItemResDTO[] deSerializedObject = gson.fromJson(serializedString, VendorItemResDTO[].class);
            vendorItemResDTOs = new ArrayList<>();
            for (VendorItemResDTO vendorItemResDTO : deSerializedObject) {
                vendorItemResDTOs.add(vendorItemResDTO);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
            Log.d(TAG, "Exception while deSerializing VendorItemResDTO list" + e.toString());
        }
        return vendorItemResDTOs;
    }

}
