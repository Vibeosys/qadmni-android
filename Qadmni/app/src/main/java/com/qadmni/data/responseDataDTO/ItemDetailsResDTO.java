package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 04-02-2017.
 */
public class ItemDetailsResDTO extends BaseDTO {

    private static final String TAG = ItemDetailsResDTO.class.getSimpleName();
    private long itemId;
    private String itemNameEn;
    private String itemNameAr;
    private String itemDescEn;
    private String itemDescAr;
    private long categoryId;
    private double unitPrice;
    private String offerText;
    private int isActive;
    private String imageUrl;

    public ItemDetailsResDTO() {
    }


    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemNameEn() {
        return itemNameEn;
    }

    public void setItemNameEn(String itemNameEn) {
        this.itemNameEn = itemNameEn;
    }

    public String getItemNameAr() {
        return itemNameAr;
    }

    public void setItemNameAr(String itemNameAr) {
        this.itemNameAr = itemNameAr;
    }

    public String getItemDescEn() {
        return itemDescEn;
    }

    public void setItemDescEn(String itemDescEn) {
        this.itemDescEn = itemDescEn;
    }

    public String getItemDescAr() {
        return itemDescAr;
    }

    public void setItemDescAr(String itemDescAr) {
        this.itemDescAr = itemDescAr;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }

    public int isActive() {
        return isActive;
    }

    public void setActive(int active) {
        isActive = active;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static ItemDetailsResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        ItemDetailsResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, ItemDetailsResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization ItemDetailsResDTO" + e.toString());
        }
        return responseDTO;
    }
}
