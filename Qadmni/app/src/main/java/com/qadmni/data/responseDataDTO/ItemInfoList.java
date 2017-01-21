package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;

/**
 * Created by shrinivas on 16-01-2017.
 */
public class ItemInfoList {
    long itemId;
    String itemDesc;
    String itemName;
    double unitPrice;
    String offerText;
    double rating;
    String imageUrl;
    long producerId;
    long reviews;

    public ItemInfoList(long itemId, String itemDesc, String itemName, double unitPrice,
                        String offerText, double rating, String imageUrl, long producerId, long reviews) {
        this.itemId = itemId;
        this.itemDesc = itemDesc;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.offerText = offerText;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.producerId = producerId;
        this.reviews = reviews;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }

    public long getReviews() {
        return reviews;
    }

    public void setReviews(long reviews) {
        this.reviews = reviews;
    }

    public static ArrayList<ItemInfoList> deSerializedToJson(ItemInfoList[] deSerializedObject) {
        Gson gson = new Gson();
        ArrayList<ItemInfoList> itemInfoListDTOS = null;
        try {

            itemInfoListDTOS = new ArrayList<>();
            for (ItemInfoList ItemInfoList : deSerializedObject) {
                itemInfoListDTOS.add(ItemInfoList);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
            Log.d("TAG", "Exception while converting item list" + e.toString());
        }
        return itemInfoListDTOS;
    }

}
