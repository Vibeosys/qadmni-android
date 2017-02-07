package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 07-02-2017.
 */
public class SubmitFeedReqDTO extends BaseDTO {

    private long orderId;
    private ArrayList<FeedbackItemSubReqDTO> items;

    public SubmitFeedReqDTO(long orderId, ArrayList<FeedbackItemSubReqDTO> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public ArrayList<FeedbackItemSubReqDTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<FeedbackItemSubReqDTO> items) {
        this.items = items;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
