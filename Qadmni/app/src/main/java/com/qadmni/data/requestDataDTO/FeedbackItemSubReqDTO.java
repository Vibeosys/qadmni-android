package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 07-02-2017.
 */
public class FeedbackItemSubReqDTO extends BaseDTO {

    private long itemId;
    private float rating;

    public FeedbackItemSubReqDTO(long itemId, float rating) {
        this.itemId = itemId;
        this.rating = rating;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
