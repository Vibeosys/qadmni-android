package com.qadmni.data;

/**
 * Created by akshay on 06-02-2017.
 */
public class FeedbackItemDTO {

    private long itemId;
    private String itemName;
    private float rating;

    public FeedbackItemDTO(long itemId, String itemName, float rating) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.rating = rating;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
